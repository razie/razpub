/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.lightsoa.test;

import com.razie.pub.base.log.Log;
import com.razie.pub.lightsoa.SoaMethod;

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
        input = "echo " + msg;
        String m = "echo: " + msg;
        Log.logThis(m);
        return m;
    }
}
