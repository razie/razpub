/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.comms;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.razie.pub.assets.AssetKey;
import com.razie.pub.assets.AssetLocation;

/**
 * represents a remote agent. Agent's name should normally be the same as the hostname, but do as
 * you must...
 * 
 * @author razvanc99
 * 
 */
@SuppressWarnings("serial")
public class AgentHandle extends AssetKey implements Cloneable {

    /** the handles keep transient peer status info, if an updater service is used */
    public static enum DeviceStatus {
        UNKOWN, DOWN, UP, EXCLUDED
    }

    public static final String    sCLASS = "RazAgent";
    public String                 name;
    public String                 hostname;
    public String                 ip;
    public String                 port;
    public String                 localdir;
    public String                 os;
    public transient DeviceStatus status = DeviceStatus.UNKOWN;

    // TODO why not use the AssetLocation?
    /** cache the url, format: "http://[ip|hostname]:port" */
    public String                 url;

    /** minimum information - use it for temporary handles or small tests...you'll have to */
    public AgentHandle(String name, String hostname, String ip, String port, String url) {
        this(name, hostname, ip, port, url, "", "");
    }

    /** full constructor */
    public AgentHandle(String name, String hostname, String ip, String port, String url, String os,
            String localdir) {
        super(sCLASS, name, new AssetLocation(url));
        this.name = name;
        this.hostname = hostname;
        this.ip = ip;
        this.port = port;
        this.url = url;
        this.os = os;
        this.localdir = localdir;

        // make sure port is ok...
        try {
            if (port != null && port.length() > 0) {
                int p = Integer.parseInt(port);
            }
        } catch (Throwable t) {
            String msg = "PORT " + port + " FOR device=" + hostname
                    + " WRONG - please check again. Must be a 4 digit number!";
            throw new IllegalStateException(msg, t);
        }
    }

    public String toString() {
        return "AgentHandle(" + name + ";" + hostname + ";" + ip + ";" + port + ";" + url + ";" + os + ";"
                + localdir + ")";
    }

    public static AgentHandle fromString(String s) {
        if (! s.startsWith ("AgentHandle("))
            throw new IllegalArgumentException("String is not an AgentHandle: " + s);

        String ss[] = s.split("[;()]");
        return new AgentHandle(ss[1], ss[2], ss[3], snull(ss,4), snull(ss,5), snull(ss,6), snull(ss,7));
    }

    private static final String snull (String[] ss, int idx) {
        if (ss.length > idx) return ss[idx]; else return "";
    }

    public AgentHandle clone() {
        return new AgentHandle(this.name, this.hostname, this.ip, this.port, this.url, this.os, this.localdir);
    }

    public boolean equals(Object o) {
        AgentHandle other = (AgentHandle) o;
        if (this.name.equals(other.name) && this.hostname.equals(other.hostname)
                && this.port.equals(other.port))
            return true;
        return false;
    }

    public boolean isUpNow() {
        // timeout quickly
        if (this.port.length() <= 0) {
            // for now ignore those that don't run agents:
            return false;
        }

        try {
            Socket server = new Socket();
            int port = Integer.parseInt(this.port);
            server.connect(new InetSocketAddress(ip, port), 250);
            server.close();
            return true;
        } catch (IOException e1) {
            return false;
        } catch (Exception e) {
            // don't care what hapened...
            return false;
        }
    }

    // public boolean isUpNow() {
    // // timeout quickly
    // try {
    // RazClientSocket server;
    // int port = Integer.parseInt(Agents.agent(name).port);
    // server = new RazClientSocket(Agents.agent(name).ip, port, 250);
    // server.close();
    // return true;
    // } catch (IOException e1) {
    // return false;
    // } catch (Exception e) {
    // // don't care what hapened...
    // return false;
    // }
    // }
}
