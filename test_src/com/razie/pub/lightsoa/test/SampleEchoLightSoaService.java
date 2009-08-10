/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.lightsoa.test;

import com.razie.pub.base.AttrAccess;
import com.razie.pub.base.log.Log;
import com.razie.pub.lightsoa.SoaAllparms;
import com.razie.pub.lightsoa.SoaMethod;
import com.razie.pub.lightsoa.SoaMethodSink;

/**
 * sample command listener implementing the echo command as a lightsoa
 *  $
 * @author razvanc99
 * 
 */
public class SampleEchoLightSoaService {
    public String input = null;

    @SoaMethod(descr = "echo service", args = { "msg" })
    public String echo(String msg) {
        String m = input = "echo " + msg;
        Log.logThis(m);
        return m;
    }
    
    @SoaMethod(descr = "echo service", args = { "msg" })
    @SoaMethodSink
    @SoaAllparms
    public String blackhole (AttrAccess all) {
        String msg = all.sa ("msg");
        String m = input = "echo " + msg + " sinked method: " + all.sa(SoaMethodSink.SOA_METHODNAME);
        Log.logThis(m);
        return m;
    }
}
