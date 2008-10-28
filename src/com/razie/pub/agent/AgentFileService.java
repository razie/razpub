/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.agent;

import com.razie.pub.base.NoStatics;
import com.razie.pub.base.log.Log;

/**
 * basic file services - read/write files...since agents can run all over the place, don't count on
 * proper file system functionality
 * 
 * @author razvanc
 * @version $Id$
 * 
 */
public class AgentFileService extends AgentService {
    StatusReport status = new StatusReport();
    String       path;

    public AgentFileService(Agent agent, String path) {
        this.agent = agent;
        this.path = path;

        AgentFileService singleton = (AgentFileService) NoStatics.get(AgentFileService.class);

        if (singleton != null) {
            status.lastError = new IllegalStateException("ERR_SVC_INIT AgentWebSrevice already initialized!");
            throw (IllegalStateException) status.lastError;
        }

        NoStatics.put(AgentFileService.class, this);

        status.ok();
    }

    /** factory stub */
    public static AgentFileService getInstance() {
        AgentFileService f = (AgentFileService) NoStatics.get(AgentFileService.class);
        if (f == null)
            throw new IllegalStateException("AgentFileService not initialized!");
        return f;
    }

    public String path() {
        return path;
    }

    protected void onShutdown() {
        Log.logThis("AGENTWEB_SHUTDOWN");
    }

    protected void onStartup() {
        Log.logThis("AGENTWEB_STARTUP");
    }

    /** get status and report */
    public StatusReport status() {
        return status;
    }
}
