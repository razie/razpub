package com.razie.pubstage.agent;

import com.razie.pub.hframe.http.AgentHandle;
import com.razie.pubstage.agent.Agent.AgentEvent;

/**
 * basic interface/contract with the Agent - it's not an interface so methods are not public
 * 
 * <p>
 * A service will be instantiated and registered by the agent on startup, in the sequence from the
 * config.xml/services. A service is a single instance. Each service needs a plain constructor to be
 * instantiated.
 * 
 * <p>
 * You can also programatically register a service with the agent, after startup...the service's
 * onStartup() will be called right away.
 * 
 * <p>
 * The initialization happens in two stages: first all services are instantiated. Here's where you
 * want to register whatever you have to register. Hopefully, all prereq services were instantiated
 * as well. At this stage, you cannot use any of the other services (i.e. the web server), since
 * they also did not startup yet. You can only register callbacks/strategies with the other
 * services.
 * 
 * <p>
 * The second phase: Once the agent is ready to start, all services will be called onStartup().
 * Since services may depend on one another, it is wise to wait until this call to initialise...
 * 
 * @author razvanc
 * @version $Id$
 * 
 */
public abstract class AgentService {
    /** the second initialization phase: the agent is starting up */
    protected abstract void onStartup();

    /** the agent needs to shutdown this service. You must join() all threads and return to agent. */
    protected abstract void onShutdown();

    /**
     * called when the agent connects to another agent the first time (remote agent was restarted, I
     * came up etc)
     */
    protected void onConnectToOtherAgent(AgentHandle remote) {
    }

    /** distributed notification between agents/services */
    protected void notified(AgentEvent event) {
    }
}
