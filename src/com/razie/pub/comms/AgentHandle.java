/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.comms;

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
public class AgentHandle extends AssetKey {

    /** the handles keep transient peer status info, if an updater service is used */
    public static enum DeviceStatus {
        UNKOWN, DOWN, UP
    }

    public static final String    sCLASS = "RazAgent";
    public String                 name;
    public String                 hostname;
    public String                 ip;
    public String                 port;
    public transient DeviceStatus status = DeviceStatus.UNKOWN;

    // TODO why not use the AssetLocation?
    /** cache the url, format: "http://[ip|hostname]:port" */
    public String                 url;         

    public AgentHandle(String name, String hostname, String ip, String port, String url) {
        super(sCLASS, name, new AssetLocation(url));
        this.name = name;
        this.hostname = hostname;
        this.ip = ip;
        this.port = port;
        this.url = url;

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
        return name + ":" + hostname + ":" + ip + ":" + port + ":" + url;
    }

    public static AgentHandle fromString(String s) {
        String ss[] = s.split(":");
        return new AgentHandle(ss[0], ss[1], ss[2], ss[3], ss[4]);
    }

    public AgentHandle clone() {
        return new AgentHandle(this.name, this.hostname, this.ip, this.port, this.url);
    }
}
