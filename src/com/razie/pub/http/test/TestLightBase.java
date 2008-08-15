/**
 * Razvan's code. 
 * Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.http.test;

import junit.framework.TestCase;

import com.razie.pub.base.log.Log;
import com.razie.pub.http.LightAuth;
import com.razie.pub.http.LightCmdGET;
import com.razie.pub.http.LightServer;

/**
 * test the light server
 * 
 * @author razvanc99
 * 
 */
public class TestLightBase extends TestCase {
    protected static LightServer server;
    protected static int         PORT   = 4445;
    protected static LightCmdGET cmdGET = new LightCmdGET();

    public void setUp() {
        if (server == null) {
            LightAuth.init(new LightAuth("lightsoa"));

            server = new LightServer(PORT, null);
            server.registerCmdListener(cmdGET);

            // you can start the server in its dedicated thread or use a pool
            Thread serverThread = new Thread(server, "AgentServerThread");

            // for testing, we want it to die at the end
            serverThread.setDaemon(true);

            // start the server thread...
            serverThread.start();
        }
    }

    static final Log logger = Log.Factory.create(TestLightBase.class.getName());
}
