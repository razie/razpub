/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.razie.pub.base.AttrAccess;
import com.razie.pub.base.NoStatics;
import com.razie.pub.base.ThreadContext;
import com.razie.pub.base.log.Log;
import com.razie.pub.comms.AgentGroup;
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
 * There are many platforms for this type of "services", including OCAP, OSGI etc.
 * 
 * @author razvanc
 * @version $Id$
 * 
 */
public class Agent {
    private Map<String, AgentService> services    = new HashMap<String, AgentService>();
    protected boolean                 stopped     = false;
    protected boolean                 initialized = false;
    protected boolean                 started     = false;
    protected NoStatics               myStatics   = new NoStatics();
    protected AgentHandle             myHandle    = null;
    protected AgentGroup              homeGroup;
    private ThreadContext             mainContext;

    private final static Log          logger      = Log.Factory.create("agent", Agent.class.getSimpleName());

    /** initialize agent with given info */
    public Agent(AgentHandle myHandle, AgentGroup homeGroup) {
        NoStatics ns = new NoStatics();
        mainContext = new ThreadContext(ns);
        mainContext.enter();

        mainContext.setAttr("Agent", this);
        mainContext.setAttr("NoStatics", ns);

        // initialize required static services
        NoStatics.register(myHandle.name, ns);

        if (NoStatics.get(Agents.class) == null)
            NoStatics.put(Agents.class, new Agents(homeGroup, myHandle));
        NoStatics.put(RazResourceLocator.class, new RazResourceManager());

        this.myHandle = myHandle;
        this.homeGroup = homeGroup;
    }

    /** return the instance to use for the current thread... */
    public static Agent instance() {
        return (Agent) ThreadContext.instance().getAttr("Agent");
    }

    public AgentHandle getHandle() {
        return myHandle;
    }

    /**
     * shorthand for other registerbyname: use the simple class name as service name
     */
    public void register(AgentService l) {
        register(l.getClass().getSimpleName(), l);
    }

    /**
     * register a new service with the agent - private so folks don't use funny names yet...
     */
    private synchronized void register(String name, AgentService l) {
        this.services.put(name, l);
        l.agent = this;

        // delegate notifications to the post office
        PostOffice.register(PostOffice.DFLT_LOCAL_TOPIC, l);

        if (started && !stopped) {
            ThreadContext old = this.mainContext.enter();
            startSvc(l);
            ThreadContext.exit(old);
        }
    }

    private void startSvc(AgentService svc) {
        svc.onStartup();

        SoaService soas = svc.getClass().getAnnotation(SoaService.class);
        if (soas != null && soas.bindings().length > 0) {
            for (String binding : soas.bindings()) {
                // TODO register the binding types and use them here rather than just http
                if ("http".equals(binding)) {
                    AgentHttpService.registerSoa(new HttpSoaBinding(svc));
                }
            }
        }
    }

    /**
     * called when main() starts up but before onStartup(). Initialize all services from the
     * configuration file
     */
    public synchronized void onInit() {
        // overload this to usually register your own service on init

        initialized = true;
    }

    /** called when main() is done initializing everything else */
    public synchronized void onStartup() {
        ThreadContext old = this.mainContext.enter();

        for (AgentService s : services.values()) {
            startSvc(s);
        }

        started = true;

        ThreadContext.exit(old);
    }

    /** can't be synchronized since it may wait too long to notify others */
    public void onConnectToOtherAgent(AgentHandle remote) {
        ThreadContext old = this.mainContext.enter();

        // not me...
        if (remote.hostname.equals(Agents.getMyHostName())) {
            logger.log("AGENT_CONNECTED_TO_SELF - ignoring...");
        } else {
            for (AgentService s : copyOfServices()) {
                s.onConnectToOtherAgent(remote);
            }
        }

        ThreadContext.exit(old);
    }

    public synchronized void onShutdown() {
        ThreadContext old = this.mainContext.enter();

        stopped = true;
        // TODO shutdown in the reverse sequence
        for (AgentService s : services.values()) {
            // TODO give them some time to cleanup and then kill them if they
            // didn't stop
            logger.log("AGENT_SHUTDOWN service: " + s.getClass().getName());
            s.onShutdown();
        }

        ThreadContext.exit(old);

        // TODO are there any threads/workers/beings i should kill?
        logger.log("AGENT_SHUTDOWN_COMPLETE");
    }

    /** indicates if the agent has been shutdown */
    public boolean hasStopped() {
        return stopped;
    }

    /**
     * main loop - will wait here until the agent is shutdown. Call from your main() at the end
     */
    public void keepOnTrucking() {
        // wait for the server to die... it is the first to start and last to
        // die
        // note that shutdown is called from elsewhere

        // THIS normally just join()s the main server thread
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
        if (logger.isTraceLevel(3))
            logger.trace(3, "AGENT_NOTIFIYING_OTHER: " + device + " event: " + eventId);

        String srcAgentNm = Agents.getMyHostName();

        // TODO if device is me call notify local directly
        String url = "http://" + device.ip + ":" + device.port + "/mutant/control/";
        url += "Notify?name=" + eventId + "&srcAgentNm=" + srcAgentNm;
        url = new AttrAccess.Impl(args).addToUrl(url);
        String otherList = (Comms.readUrl(url));
        otherList = HtmlContents.justBody(otherList);

        return otherList;
    }

    /** users can call this to send notifications to the other agents */
    public void notifyOthers(String eventId, Object... args) {
        // TODO add SLA, i.e. will backup and notify when others come online etc
        String notified = "";
        String notnotified = "";

        if (logger.isTraceLevel(3))
            logger.trace(3, "AGENT_NOTIFIYING_OTHERS: " + notified + " / NOTNOTIFIED: " + notnotified);
        
        for (AgentHandle d : this.homeGroup.agents().values()) {
            if (!d.equals(this.myHandle)) {
                // TODO optimize this - notify can figure out if it's up at the
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

    public ThreadContext getMainContext() {
        return mainContext;
    }

}
