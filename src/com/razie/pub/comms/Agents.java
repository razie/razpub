/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.comms;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.razie.pub.base.NoStatics;
import com.razie.pub.base.log.Log;

/**
 * basic proxy to whatever db of hosts you like to use - there's soooo many.
 * 
 * It's basic responsibility is mapping hosts to URLs, knowing the local host etc
 * 
 * TODO use factory/singleton to allow users to use different implementations
 * 
 * TODO this approach limits me to one agent per box
 * 
 * TODO sync this class since it is updated at runtime
 * 
 * @author razvanc99
 * 
 */
public class Agents {
    protected AgentGroup       myGroup  = null;
    public boolean             testing  = false;
    public static final String TESTHOST = "TEST-host";
    public static String       homeNetPrefix;
    private AgentHandle        me       = null;

    public Agents(AgentGroup homeGroup, AgentHandle me) {
        myGroup = homeGroup;
        this.me = me;
    }

    /** THIS must be initialized in the nostatic context before this... */
    public static Agents instance() {
        Agents singleton = (Agents) NoStatics.get(Agents.class);
        return singleton;
    }

    public static AgentHandle me() {
        return instance().me;
    }

    /** @return my host name */
    public static String getMyHostName() throws RuntimeException {
        return instance().getMyHostNameImpl();
    }

    /** @return my url, of the form http://ip:port */
    public static String getMyUrl() throws RuntimeException {
        return instance().me.url;
    }

    /** @return the URL for the mentioned remote agent, in the form "http://IP:PORT" */
    public static AgentHandle agent(String remote) {
        AgentHandle ah = instance().agentImpl(remote);
        return ah;
    }

    /** TODO fix this - should not get by IP since there can be many at the same ip */
    public static AgentHandle agentByIp(String remote) {
        return instance().agentByIpImpl(remote);
    }

    public String getMyHostNameImpl() throws RuntimeException {
        if (testing) {
            return TESTHOST;
        }
        try {
            String n = InetAddress.getLocalHost().getHostName();
            return n;
        } catch (UnknownHostException e1) {
            throw new RuntimeException("ERR: " + e1.toString(), e1);
        }
    }

    public static String findMyHostName(boolean testing) throws RuntimeException {
        if (testing) {
            return TESTHOST;
        }
        try {
            String n = InetAddress.getLocalHost().getHostName();
            return n;
        } catch (UnknownHostException e1) {
            throw new RuntimeException("ERR: " + e1.toString(), e1);
        }
    }

    public AgentHandle agentImpl(String host) {
        return instance().myGroup.get(host);
    }

    /** TODO sync this */
    public AgentHandle agentByIpImpl(String ip) {
        for (AgentHandle a : instance().myGroup.agents().values()) {
            if (a.ip.equals(ip)) {
                return a;
            }
        }

        Log.logThis("ERR_AGENTBYIP_NOTFOUND - you may get a null pointer about now. ip=" + ip
                + instance().myGroup.agents().values());
        return null;
    }

    public static String getHomeNetPrefix() {
        return homeNetPrefix;
    }

    public static AgentGroup homeGroup() {
        return instance().myGroup;
    }

    public static void setMe(AgentHandle myHandle) {
        Agents i = instance();
        if (i.me != null && ! i.me.name.equals(myHandle.name))
            throw new IllegalStateException("Can't change who I am...");
        else
            i.me = myHandle;
    }
}
