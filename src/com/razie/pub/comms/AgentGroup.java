/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.comms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * a group of agents representing a logical structure, i.e. all the agents in my home (that is
 * actually a special group, the "home group". The different groups you could have/belong to are
 * equivalent, except the home group. The home group is by default the current group that is the
 * target of distributed operations.
 * 
 * agent groups are the logical unit for distributed services, including negociation, database sync
 * etc.
 * 
 * agent groups can be configured statically (config.xml) or dynamically, based on
 * AgentGroupNegociation
 * 
 * TODO detailed docs
 * 
 * @author razvanc
 * 
 */
public class AgentGroup {
    /** map<name,handle> */
    private Map<String, AgentHandle> agents    = Collections
                                                      .synchronizedMap(new HashMap<String, AgentHandle>());

    // TODO implement this
    public static AgentGroup        homeGroup = null;

    /**
     * only access is to clone the sync'd collection. The individual agents may still be modified by
     * async status updates, but as assignments are atomic should be ok
     */
    public Map<String, AgentHandle> agents() {
        synchronized (agents) {
            Map<String, AgentHandle> copy = new HashMap<String, AgentHandle>();
            copy.putAll(agents);
            return copy;
        }
    }

    public AgentHandle put (String name, AgentHandle h) {
        agents.put(name, h);
        return h;
    }
    
    public AgentHandle get (String name) {
        return agents.get(name);
    }
}
