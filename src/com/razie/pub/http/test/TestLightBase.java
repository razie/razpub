/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.http.test;

import junit.framework.TestCase;

import com.razie.pub.base.NoStatics;
import com.razie.pub.base.log.Log;
import com.razie.pub.comms.AgentGroup;
import com.razie.pub.comms.AgentHandle;
import com.razie.pub.comms.Agents;
import com.razie.pub.comms.LightAuth;
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
    protected static Integer     PORT   = 4445;
    protected static LightCmdGET cmdGET = new LightCmdGET();

    static AgentHandle           me     = new AgentHandle("localhost", "localhost", "127.0.0.1", PORT
                                                .toString(), "http://localhost:" + PORT.toString());

    public void setUp() {
        if (server == null) {
            LightAuth.init(new LightAuth("lightsoa"));

            AgentGroup group = new AgentGroup();
            group.put(me.name, me);
            NoStatics.put(Agents.class, new Agents(group, me));

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
