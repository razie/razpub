package com.razie.pub.agent

import com.razie.pub.lightsoa._
import com.razie.pub.agent.AgentService
import com.razie.pub.assets._
import com.razie.pub.comms._
import com.razie.pub.base._
import com.razie.pub.http._
import razie._
import razie.assets._
import java.io._

/** very basic messaging support
 * 
 * TODO 2-2 enhance using the new actors model from lift: 
 * 
 * TODO 1-1 unit test
 * 
 * @author razvanc
 */
@SoaService (name="msg", descr="messaging service", bindings=Array("http"))
class AgentMsgService extends AgentService {

	@SoaMethod (descr="receive a message")
	@SoaAllParms
	def receive (a:AttrAccess) = {
	   // TODO ugly hack to send binary
		val o = a.a("razie.content.mime.octet-stream")
		o match {
			case AgentMsgEnvelope(from,to,m) => handleMsg (from, to, m)
			case _ => { val ms="Message not understood: " + o; Log(ms); ms}
		}
	}
	
   // override this in derived to handle the message
   def handleMsg(from:AgentHandle, to:AgentHandle, m:Any) = {
		m match {
			case AssetMsg (key, o) => 
			   razie.Asset(key).resolveIfLocal match {
			      case h : AgentMsgHandler => h.receiveRemote (from, o)
			      case _ => 
			         razie.Log.alarmThis ("ERR_MSGASSET could not resolve local asset for message target: " + key)
   			}
			case ServiceMsg (service, o) =>  
			   Agent.instance().locateService(service) match {
               case h : AgentMsgHandler => h.receiveRemote (from, o)
               case _ => 
                  razie.Log.alarmThis ("ERR_MSGSERVICE could not resolve local service for message target: " + service)
            }
			case _ => { val ms="Message not understood: " + m; Log(ms); ms}
		}
   }
}

/** implemented by all targets that can receive messages. 
 * You have NO control over threading and can be called from multiple threads at 
 * the same time. 
 * GOOD LUCK! */
trait AgentMsgHandler {
   def receiveRemote (from:AgentHandle, msg:Any) : Any 
}

class AgentMsg(val msg:Any) extends Serializable {
   def sendTo (to:AgentHandle) = AgentMsg.isend (to, this)
   def sendTo (to:String) = AgentMsg.isend (to, this)
}

case class AssetMsg (key:AssetKey, override msg:Any) extends AgentMsg (msg) 
case class ServiceMsg (service:String, override msg:Any) extends AgentMsg  (msg) 

case class AgentMsgEnvelope (from:AgentHandle, to:AgentHandle, msg:AgentMsg)

object AgentMsg {
   def sendTo (to:AgentHandle, key:AssetKey, msg:Any) = isend (to, new AssetMsg(key, msg))
   def sendTo (to:AgentHandle, service:String, msg:Any) = isend (to, new ServiceMsg(service, msg))
   def sendTo (to:String, key:AssetKey, msg:Any) = isend (to, new AssetMsg(key, msg))
   def sendTo (to:String, service:String, msg:Any) = isend (to, new ServiceMsg(service, msg))
   
   // TODO optimize sending to local
   def isend (to:AgentHandle, m:AgentMsg) : String = {
      val httpArgs = new AttrAccessImpl() 
      val cmd = "POST /mutant/msg/receive HTTP/1.1"
      
      val socket = HttpHelper.sendBinaryPOST(to.hostname, Integer.parseInt(to.port), cmd, httpArgs, new AgentMsgEnvelope (Agents.me, to, m)) 
   
      val resp = Comms.readStream (socket.getInputStream())
      socket.close()
      resp
   }
   
   def isend (to:String, m:AgentMsg) =  
      for (a <- Agents.homeCloud.agents.values if (a.name matches (to))) yield isend (_, m)
}

