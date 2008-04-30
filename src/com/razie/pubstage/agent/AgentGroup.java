package com.razie.pubstage.agent;

import java.util.HashMap;
import java.util.Map;

import com.razie.pub.hframe.http.AgentHandle;

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
    public Map<String, AgentHandle> agents = new HashMap<String, AgentHandle>();
}
