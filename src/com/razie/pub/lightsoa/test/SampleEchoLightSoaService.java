// ==========================================================================
// $Id: Svc.java,v 1.63 2005/04/01 16:22:12 davidx Exp $
// (@) Copyright Sigma Systems (Canada) Inc.
// ==========================================================================
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
