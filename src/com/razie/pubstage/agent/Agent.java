package com.razie.pubstage.agent;

import java.util.ArrayList;
import java.util.List;

import com.razie.pub.base.AttrAccess;
import com.razie.pub.base.log.Log;
import com.razie.pub.http.AgentHandle;
import com.razie.pub.http.Agents;

/**
 * Agents run at different locations and they cooperate. An agent has a bunch of services that run
 * on it and it offers these a platform for inter-operation.
 * 
 * @author razvanc
 * @version $Id$
 * 
 */
public class Agent {
    private List<AgentService> services    = new ArrayList<AgentService>();
    protected boolean          stopped     = false;
    protected boolean          initialized = false;
    protected boolean          started     = false;
    /** register a new service with the agent */
    public synchronized void register(AgentService l) {
        this.services.add(l);
        if (started && !stopped) {
            l.onStartup();
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
        for (AgentService s : services) {
            s.onStartup();
        }

        started = true;
    }

    /** can't be synchronized since it may wait too long to notify others */
    public void onConnectToOtherAgent(AgentHandle remote) {
        // not me...
        if (remote.hostname.equals(Agents.getMyHostName())) {
            Log.logThis("AGENT_CONNECTED_TO_SELF - ignoring...");
        } else {
            // copy devices to avoid sync'd
            List<AgentService> copy = new ArrayList<AgentService>();
            synchronized (this) {
                copy.addAll(services);
            }

            for (AgentService s : copy) {
                s.onConnectToOtherAgent(remote);
            }
        }
    }

    public synchronized void onShutdown() {
        stopped = true;
        // shutdown in the reverse sequence
        for (int i = services.size() - 1; i >= 0; i--) {
            // TODO give them some time to cleanup and then kill them if they didn't stop
            Log.logThis("AGENT_SHUTDOWN service: " + services.get(i).getClass().getName());
            services.get(i).onShutdown();
        }

        // TODO are there any threads/workers/beings i should kill?
        Log.logThis("AGENT_SHUTDOWN_COMPLETE");
    }

    /** notify all local services about an event */
    public void notifyLocal(AgentEvent event) {
        // copy devices to avoid sync'd
        List<AgentService> copy = new ArrayList<AgentService>();
        synchronized (this) {
            copy.addAll(services);
        }

        for (AgentService l : this.services) {
            l.notified(event);
        }
    }

    /** indicates if the agent has been shutdown */
    public boolean hasStopped() {
        return stopped;
    }

    /** main loop - will wait here until the agent is shutdown. Call from your main() at the end */
    public void keepOnTrucking() {
        // wait for the server to die... it is the first to start and last to
        // die
        // note that shutdown is called from elsewhere

        // THIS normally just join()s the main server thread
    }

    public synchronized List<AgentService> copyOfServices() {
        List<AgentService> ret = new ArrayList<AgentService>();
        ret.addAll(services);
        return ret;
    }

    /**
     * an event that can be sent to other agents.
     * 
     * all services will be notified of events. It is good practice that the event name starts with
     * the service name if only you care about it.
     * 
     * TODO sync-up with the post office local eventing system
     * 
     * @author razvanc
     * 
     */
    public static class AgentEvent {
        public String     name;
        public String     srcAgentNm; // initialized by agent when sending to avoid screwups
        public AttrAccess info;

        /**
         * create a new event
         * 
         * @param name the event name, should start with the service name if service specific using
         *        dot notation
         * @param srcAgentNm the name of the source agent
         * @param info extra information carried by the event
         */
        public AgentEvent(String name, AttrAccess info) {
            this.name = name;
            this.info = info;
        }

        public AgentEvent(AttrAccess info) {
            this.name = (String) info.getAttr("name");
            this.srcAgentNm = (String) info.getAttr("srcAgentNm");
            this.info = info;
        }
    }

}
