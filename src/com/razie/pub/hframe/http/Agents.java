package com.razie.pub.hframe.http;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.razie.pub.hframe.base.log.Log;

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
    protected Map<String, AgentHandle> byName   = null;       // lazy
    public static boolean              testing  = false;
    public static final String         TESTHOST = "TEST-host";
    private static Agents              singleton;
    public static String homeNetPrefix;

    private Agents() {
        byName = new HashMap<String, AgentHandle>();
    }

    protected static Agents instance() {
        if (singleton == null)
            singleton = new Agents();
        return singleton;
    }

    /** @return my host name */
    public static String getMyHostName() throws RuntimeException {
        return instance().getMyHostNameImpl();
    }

    /** @return my url */
    public static String getMyUrl() throws RuntimeException {
        return instance().agentImpl(getMyHostName()).url;
    }

    /** @return the URL for the mentioned remote agent, in the form "http://IP:PORT" */
    public static AgentHandle agent(String remote) {
        return instance().agentImpl(remote);
    }

    /** TODO fix this - should not get by IP since there can be many at the same ip */
    public static AgentHandle agentByIp(String remote) {
        return instance().agentByIpImpl(remote);
    }

    /** TODO remove - this is temporary */
    public static void add(AgentHandle remote) {
        instance().addImpl(remote);
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

    public AgentHandle agentImpl(String host) {
        return byName.get(host);
    }

    protected void addImpl(com.razie.pub.hframe.http.AgentHandle remote) {
        byName.put(remote.name, remote);
    }

    /** TODO sync this */
    public AgentHandle agentByIpImpl(String ip) {
        for (AgentHandle a : byName.values()) {
            if (a.ip.equals(ip)) {
                return a;
            }
        }

        Log.logThis("ERR_AGENTBYIP_NOTFOUND - you may get a null pointer about now. ip=" + ip);
        return null;
    }

    public static String getHomeNetPrefix() {
        return homeNetPrefix;
    }
}
