/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liabity assumed for this code.
 */
package com.razie.pub.agent.test;

import com.razie.pub.agent.AgentService;
import com.razie.pub.base.log.Log;
import com.razie.pub.lightsoa.SoaMethod;
import com.razie.pub.lightsoa.SoaService;

/**
 * sample agent service in java
 * 
 * @author razvanc
 */
@SoaService(name = "samplejavaservice", bindings = { "http" }, descr = "sample in java")
public class SampleJavaService extends AgentService {

    protected void onStartup() {
        Log.logThis("SampleJavaService onStartup()");
    }

    protected void onShutdown() {
        Log.logThis("SampleJavaService onShutdown()");
    }

    @SoaMethod(descr = "echo with args", args = { "msg" })
    public String echo1(String msg) {
        return "echo1: " + msg;
    }

    @SoaMethod(descr = "echo no args")
    public String echo2() {
        return "echo2...";
    }
}
