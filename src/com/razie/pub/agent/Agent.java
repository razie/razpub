/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import razie.base.AttrAccessImpl;

import com.razie.pub.base.ExecutionContext;
import com.razie.pub.base.NoStatics;
import com.razie.pub.base.log.Log;
import com.razie.pub.comms.AgentCloud;
import com.razie.pub.comms.AgentHandle;
import com.razie.pub.comms.Agents;
import com.razie.pub.comms.Comms;
import com.razie.pub.events.PostOffice;
import com.razie.pub.events.RazResource.RazResourceLocator;
import com.razie.pub.events.RazResource.RazResourceManager;
import com.razie.pub.lightsoa.HttpSoaBinding;
import com.razie.pub.lightsoa.SoaService;
import com.razie.pubstage.comms.HtmlContents;

/**
 * Agents run at different locations and they cooperate. An agent has a bunch of services that run
 * on it and it offers these a platform for inter-operation.
 * 
 * This is a skeleton agent, should work fine for most purposes...the mutant agent adds a lot of
 * other functionality...
 * 
 * There are many platforms for this type of "services", including OCAP, OSGI etc.
 * 
 * @author razvanc
 * @version $Id$
 */
public class Agent {

   private Map<String, AgentService> services    = new HashMap<String, AgentService>();
   protected boolean                 stopped     = false;
   protected boolean                 initialized = false;
   protected boolean                 started     = false;
   protected NoStatics               myStatics   = new NoStatics();
   protected AgentHandle             me          = null;
   public    AgentCloud              homeCloud;
   private ExecutionContext          mainContext;
   private final static Log          logger      = Log.factory.create("agent", Agent.class.getSimpleName());

   /** initialize agent with given info */
   public Agent(AgentHandle me, AgentCloud homeCloud) {
      this.me = me;
      this.homeCloud = homeCloud;

      NoStatics ns = new NoStatics();
      mainContext = new ExecutionContext(ns);
      mainContext.enter();

      mainContext.setAttr("Agent", this);
      mainContext.setAttr("NoStatics", ns);

      // initialize required static services
      NoStatics.register(me.name, ns);

      if (NoStatics.get(Agents.class) == null)
         NoStatics.put(Agents.class, new Agents(homeCloud, me));

      if (NoStatics.get(RazResourceLocator.class) == null)
         NoStatics.put(RazResourceLocator.class, new RazResourceManager());
   }

   /** return the instance to use for the current thread... */
   public static Agent instance() {
      return (Agent) ExecutionContext.instance().getAttr("Agent");
   }

   public AgentHandle getHandle() { return me; }
   public AgentHandle handle() { return me; }

   /**
    * shorthand for other registerbyname: use the simple class name as service name
    */
   public void register(AgentService l) {
      register(l.getClass().getSimpleName(), l);
   }

   /**
    * register a new service with the agent - private so folks don't use funny names yet...
    */
   protected synchronized void register(String name, AgentService l) {
      Log.traceThis("REGISTERING_SERVICE: " + name);
      this.services.put(name, l);
      l.agent = this;

      // delegate notifications to the post office
      PostOffice.register(PostOffice.DFLT_LOCAL_TOPIC, l);

      if (started && !stopped) {
         // make sure it's started in my context, since this is called by others
         ExecutionContext old = this.mainContext.enter();
         startSvc(l);
         ExecutionContext.exit(old);
      }

      Log.audit("REGISTERED_SERVICE: " + name);
   }

   private void startSvc(AgentService svc) {
      svc.onStartup();

      SoaService soas = svc.getClass().getAnnotation(SoaService.class);
      if (soas != null && soas.bindings().length > 0) {
         for (String binding : soas.bindings()) {
            // TODO 2-2 register the binding types and use them here rather than just http
            if ("http".equals(binding)) {
               AgentHttpService.registerSoa(new HttpSoaBinding(svc));
            }
         }
      }
   }

   /**
    * called when main() starts up but before onStartup(). Initialize all services from the
    * configuration file
    * 
    * overload this to usually register your own service on init
    * 
    * @return this agent
    */
   public synchronized Agent onInit() {
      initialized = true;
      return this;
   }

   /**
    * called when main() is done initializing everything else
    * 
    * @return this agent
    */
   public synchronized Agent onStartup() {
      ExecutionContext old = this.mainContext.enter();

      for (AgentService s : services.values()) {
         startSvc(s);
      }

      started = true;

      ExecutionContext.exit(old);
      return this;
   }

   /** can't be synchronized since it may wait too long to notify others */
   public void onConnectToOtherAgent(AgentHandle remote) {
      ExecutionContext old = this.mainContext.enter();

      // not me...
      if (remote.hostname.equals(Agents.getMyHostName())) {
         logger.log("AGENT_CONNECTED_TO_SELF - ignoring...");
      } else {
         for (AgentService s : copyOfServices()) {
            s.onConnectToOtherAgent(remote);
         }
      }

      ExecutionContext.exit(old);
   }

   /**
    * shutdown all services. Note that it doesn't way for background threads to finish - you should
    * call keepOnTrucking!
    * 
    * @return this agent
    */
   public synchronized Agent onShutdown() {
      ExecutionContext old = this.mainContext.enter();

      stopped = true;
      // TODO 2-1 shutdown in the reverse sequence
      for (AgentService s : services.values()) {
         // TODO 2-2 give them some time to cleanup and then kill them if they didn't stop
         logger.log("AGENT_SHUTDOWN service: " + s.getClass().getName());
         s.onShutdown();
      }

      ExecutionContext.exit(old);
      logger.log("AGENT_SHUTDOWN_COMPLETE - some daemons may still be alive, though - " + me);
      return this;
   }

   /** indicates if the agent has been shutdown */
   public boolean hasStopped() {
      return stopped;
   }

   /**
    * main loop - will wait here until the agent is shutdown, joining all spawned threads. Call from
    * your main() after all initialization...
    * 
    * <p>
    * This will also wait for the server to die... it is the first to start and last to die. For the
    * server to die, note that onShutdown must be called from elsewhere.
    * 
    * <p>
    * A usual sequence for unit tests is <code>a.{onShutdown(); keepOnTrucking()}</code> which will
    * ask it to die and then will wait until it dies.
    */
   public void join() {
      AgentHttpService ahs = ((AgentHttpService) locateService(AgentHttpService.class.getSimpleName()));
      // TODO 1-1 there may be some more background threads - do i need a facility for these now?.
      // option 1 is to register daemon threads iwth the agent which will then know to wait for them
      // - this will fxi the todoencapsulate as well
      // see also comment THREADS in AgentService
      if (ahs != null)
         ahs.todoEncapsulateSomehowJoin();
   }

   public synchronized List<AgentService> copyOfServices() {
      List<AgentService> ret = new ArrayList<AgentService>();
      ret.addAll(services.values());
      return ret;
   }

   /** locate the implementation of a given service */
   public synchronized AgentService locateService(String name) {
      return services.get(name);
   }

   /**
    * notify another specific agent. Note that in order to use this, the remote agent must have the
    * AgentHttpService initialized
    * 
    * @param device agent to notify
    * @param eventId event
    * @param args event data
    * @return response of remote - usually meaningless
    */
   public String notifyOther(AgentHandle device, String eventId, Object... args) {
      if (logger.isTraceLevel(3)) {
         logger.trace(3, "AGENT_NOTIFIYING_OTHER: " + device + " event: " + eventId);
      }

      String srcAgentNm = Agents.getMyHostName();

      // TODO if device is me call notify local directly
      // TODO 1-1 use ServiceActionItemtoinvoke with lightauth
      String url = "http://" + device.ip + ":" + device.port + "/mutant/control/";
      url += "Notify?name=" + eventId + "&srcAgentNm=" + srcAgentNm;
      url = new AttrAccessImpl(args).addToUrl(url);
      String otherList = (Comms.readUrl(url));
      otherList = HtmlContents.justBody(otherList);

      return otherList;
   }

   /** users can call this to send notifications to the other agents */
   public void notifyOthers(String eventId, Object... args) {
      // TODO add SLA, i.e. will backup and notify when others come online etc
      String notified = "";
      String notnotified = "";

      if (logger.isTraceLevel(3)) {
         logger.trace(3, "AGENT_NOTIFIYING_OTHERS: " + notified + " / NOTNOTIFIED: " + notnotified);
      }

      for (AgentHandle d : this.homeCloud.agents().values()) {
         if (!d.equals(this.me)) {
            // TODO ?- optimize this - notify can figure out if it's up at the
            // same time
            if (AgentHandle.DeviceStatus.UP.equals(d.status)) {
               notified += d.name + ":" + notifyOther(d, eventId, args) + " , ";
            } else {
               notnotified += d.name + " ' ";
            }
         }
      }

      logger.log("AGENT_NOTIFIED_OTHERS: " + notified + " / NOTNOTIFIED: " + notnotified);
   }

   public ExecutionContext getContext() { return mainContext; }
   public ExecutionContext context() { return mainContext; }
}
