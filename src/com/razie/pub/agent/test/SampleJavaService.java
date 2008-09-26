package com.razie.pub.agent.test;

import com.razie.pub.agent.AgentService;
import com.razie.pub.base.log.Log;
import com.razie.pub.lightsoa.SoaMethod;
import com.razie.pub.lightsoa.SoaService;

/**
 * sample agent service in java
 * 
 * @author razvanc
 * @version $Id$
 * 
 */
@SoaService(name = "samplejavaservice", bindings = { "http" }, descr = "sample in java")
public class SampleJavaService extends AgentService {

    public SampleJavaService() {
    }

    protected void onStartup() {
        Log.logThis("SampleJavaService onStartup()");
    }

    protected void onShutdown() {
        Log.logThis("SampleJavaService onShutdown()");
    }

    @SoaMethod(descr = "echo", args = { "msg" })
    public String echo1(String msg) {
        return "echo1: " + msg;
    }

    @SoaMethod(descr = "eno ho args")
    public String echo2() {
        return "echo2...";
    }
}
