/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pubstage.agent;

import com.razie.pub.base.AttrAccess;
import com.razie.pub.draw.Drawable;
import com.razie.pub.events.EvListener;
import com.razie.pub.http.AgentHandle;

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
public abstract class AgentService implements EvListener {
    /**
     * the agent sets itself when the service is registered. that will guaranteed hapen before
     * onStartup()
     */
    protected Agent agent;

    /** the second initialization phase: the agent is starting up */
    protected abstract void onStartup();

    /** run diagnostics and report */
    protected DiagReport diagnose() {
        DiagReport res = new DiagReport();
        return res;
    }

    /** get status and report */
    protected StatusReport status() {
        StatusReport res = new StatusReport();
        return res;
    }

    /** the agent needs to shutdown this service. You must join() all threads and return to agent. */
    protected abstract void onShutdown();

    /**
     * called when the agent connects to another agent the first time (remote agent was restarted, I
     * came up etc)
     */
    protected void onConnectToOtherAgent(AgentHandle remote) {
    }

    /** @return the list of event types you're interested in or null/empty if interested in all */
    public String[] interestedIn() {
        return EMPTY;
    }

    /** main method to be notified about an event */
    public void eatThis(String srcID, String eventId, AttrAccess info) {
    }

    /** diagnostics results */
    public static class DiagReport extends StatusReport {
    }

    /** status report */
    public static class StatusReport {
        public static enum Status {
            UNKNOWN, GREEN, YELLOW, RED
        };

        public Status   status = Status.UNKNOWN;
        public Drawable details;
    }

    private static final String[] EMPTY = new String[0];
}
