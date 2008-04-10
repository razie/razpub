package com.razie.pub.hframe.http;

/**
 * represents a remote agent. Agent's name should normally be the same as the hostname, but do as
 * you must...
 * 
 * @author razvanc99
 * 
 */
public class AgentHandle {
    public String name;
    public String hostname;
    public String ip;
    public String port;

    /** format: "http://[ip|hostname]:port" */
    public String url;     // convenience - you normally need to talk to it through http
   
    public AgentHandle(String name, String hostname, String ip, String port, String url) {
        super();
        this.name = name;
        this.hostname = hostname;
        this.ip = ip;
        this.port = port;
        this.url = url;
    }
}
