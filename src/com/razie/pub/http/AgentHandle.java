/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.http;

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
    public transient DeviceStatus status=DeviceStatus.UNKOWN;

    /** format: "http://[ip|hostname]:port" */
    public String                 url;                // convenience - you normally need to talk
                                                        // to it

    // through http

    public AgentHandle(String name, String hostname, String ip, String port, String url) {
        super(sCLASS, name, new AssetLocation(url));
        this.name = name;
        this.hostname = hostname;
        this.ip = ip;
        this.port = port;
        this.url = url;
    }

    public String toString() {
        return name + ":" + hostname + ":" + ip + ":" + port + ":" + url;
    }
}
