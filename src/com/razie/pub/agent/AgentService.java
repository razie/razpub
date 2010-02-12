/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.agent;

import razie.base.AttrAccess;

import com.razie.pub.comms.AgentHandle;
import com.razie.pub.events.EvListener;

/**
 * basic interface/contract with the Agent - it's not an interface so methods
 * are not public
 * 
 * <p> In your service: you CAN implement onStartup(). You SHOULD implement onShutdown().
 * 
 * <p>
 * A service will be instantiated and registered by the agent on startup, in the
 * sequence from the config.xml/services. A service is a single instance. Each
 * service needs a plain constructor to be instantiated.
 * 
 * <p>
 * You can also programatically register a service with the agent, after
 * startup...the service's onStartup() will be called right away.
 * 
 * <p>
 * The initialization happens in two stages: first all services are
 * instantiated. Here's where you want to register whatever you have to
 * register. Hopefully, all prereq services were instantiated as well. At this
 * stage, you cannot use any of the other services (i.e. the web server), since
 * they also did not startup yet. You can only register callbacks/strategies
 * with the other services.
 * 
 * <p>
 * The second phase: Once the agent is ready to start, all services will be
 * called onStartup(). Since services may depend on one another, it is wise to
 * wait until this call to initialise...
 * 
 * <p>TODO 1-1 THREADS: if you spawn any background threads, please ... do what? 1) register with Agent or 2) return in onShutdown...?
 * 
 * @author razvanc
 * @version $Id$
 * 
 */
public abstract class AgentService implements EvListener {
	/**
	 * the agent sets itself when the service is registered. that will
	 * guaranteed hapen before onStartup()
	 */
	protected Agent agent;

	/** the second initialization phase: the agent is starting up */
	protected void onStartup() {}

	/** run diagnostics and report */
	protected DiagReport diagnose() {
		DiagReport res = new DiagReport();
		return res;
	}

	/** get status and report */
	public StatusReport status() {
		StatusReport res = new StatusReport();
		return res;
	}

	/**
	 * the agent needs to shutdown this service. You must join() all threads and
	 * return to agent.
	 */
	protected void onShutdown() {}

	/**
	 * called when the agent connects to another agent the first time (remote
	 * agent was restarted, I came up etc)
	 */
	protected void onConnectToOtherAgent(AgentHandle remote) {
	}

	/**
	 * @return the list of event types you're interested in or null/empty if
	 *         interested in all
	 */
	public String[] interestedIn() {
		return EMPTY;
	}

	/** main method to be notified about an event */
	public void eatThis(String srcID, String eventId, AttrAccess info) {
	}

	/** diagnostics results */
	public static class DiagReport extends StatusReport {
	}

	/** simple utility to locate a service given its class, from the current agent */
	@SuppressWarnings("unchecked")
    public static AgentService locate(Class cls) {
		AgentService aus = (AgentService) Agent.instance().locateService(cls.getSimpleName());
		return aus;
	}

	private static final String[] EMPTY = new String[0];
}
