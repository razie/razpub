package com.razie.pub.hframe.http.test;

import junit.framework.TestCase;

import com.razie.pub.hframe.base.log.Log;
import com.razie.pub.hframe.http.LightAuth;
import com.razie.pub.hframe.http.LightCmdGET;
import com.razie.pub.hframe.http.LightServer;

/**
 * test the light server
 * 
 * @author razvanc99
 * 
 */
public class TestLightBase extends TestCase {
    static LightServer server;
    static int         PORT   = 4445;
    static LightCmdGET cmdGET = new LightCmdGET();

    public void setUp() {
        if (server == null) {
            LightAuth.init(new LightAuth("lightsoa"));

            server = new LightServer(PORT);
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
