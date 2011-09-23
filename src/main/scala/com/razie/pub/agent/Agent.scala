package com.razie.pub.agent

import com.razie.pub.comms._
import com.razie.pubstage.comms._
import razie.base._
import razie.Logging

object Agent {
//  val logger = razie.Log.factory.create("agent", classOf[Agent].getSimpleName())

  def instance() = AgentJava.instance
  def apply() = instance()
}

class Agent(me: AgentHandle, hc: AgentCloud) extends AgentJava(me, hc) with Logging {
   
  /**
   * notify another specific agent. Note that in order to use this, the remote agent must have the
   * AgentHttpService initialized
   * 
   * @param device agent to notify
   * @param eventId event
   * @param args event data
   * @return response of remote - usually meaningless
   */
  def notifyOther(device: AgentHandle, eventId: String, args: AttrAccess): (String, Boolean) = {
    try {
      debug("AGENT_NOTIFIYING_OTHER: " + device + " event: " + eventId);

      // TODO if device is me call notify local directly
      // TODO 1-1 use ServiceActionItemtoinvoke with lightauth
      var url = "http://" + device.ip + ":" + device.port + "/mutant/control/" +
        "Notify?name=" + eventId + "&srcAgentNm=" + Agents.getMyHostName;
      url = args.addToUrl(url);
      var resp = (Comms.readUrl(url));
      resp = HtmlContents.justBody(resp);

      (resp, true)
    } catch {
      case e@_ => (e.toString, false)
    }
  }

  /** users can call this to send notifications to the other agents */
  def notifyOthers(eventId: String, args: AttrAccess): Map[String, (String, Boolean)] = {
    // TODO add SLA, i.e. will backup and notify when others come online etc
    var notified = razie.Mapi[String, (String, Boolean)]()

    debug("AGENT_NOTIFIYING_OTHERS: ");

    import scala.collection.JavaConversions._

    homeCloud.agents().values().filter(!_.equals(this.me)) foreach { d =>
      // TODO ?- optimize this - notify can figure out if it's up at the same time
      if (AgentHandle.DeviceStatus.UP.equals(d.status))
        notified put (d.name, notifyOther(d, eventId, args))
      else
        notified put (d.name, ("not up", false))
    }

    //      for (d <- homeCloud.agents().values()) {
    //         if (!d.equals(this.me)) {
    //            // TODO ?- optimize this - notify can figure out if it's up at the
    //            // same time
    //            if (AgentHandle.DeviceStatus.UP.equals(d.status)) {
    //               notified put (d.name, notifyOther(d, eventId, args))
    //            } else {
    //               notnotified put (d.name, "not up")
    //            }
    //         }
    //      }

    debug( "AGENT_NOTIFIED_OTHERS: " + notified.filter(_._2._2) + " / NOTNOTIFIED: " + notified.filter(! _._2._2));
    Map() ++ notified
  }
}
