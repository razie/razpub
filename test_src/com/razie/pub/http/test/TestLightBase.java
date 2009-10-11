/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.http.test;

import junit.framework.TestCase;

import com.razie.pub.base.NoStatics;
import com.razie.pub.base.log.Log;
import com.razie.pub.comms.AgentCloud;
import com.razie.pub.comms.AgentHandle;
import com.razie.pub.comms.Agents;
import com.razie.pub.comms.LightAuth;
import com.razie.pub.http.LightCmdGET;
import com.razie.pub.http.LightCmdPOST;
import com.razie.pub.http.LightServer;

/**
 * test the light server
 * 
 * @author razvanc99
 */
public class TestLightBase extends TestCase {
    protected LightServer server;
    protected Thread serverThread ;
    
    public static Integer     PORT   = 4445;
    protected static LightCmdGET cmdGET = new LightCmdGET();
    protected static LightCmdPOST cmdPOST = new LightCmdPOST();

    static AgentHandle           ME     = new AgentHandle("localhost", "localhost", "127.0.0.1", PORT
                                                .toString(), "http://localhost:" + PORT.toString());
    static AgentHandle           MEPLUS1     = new AgentHandle("localhost", "localhost", "127.0.0.1", String.valueOf(PORT+1), "http://localhost:" + (PORT+1));

    static int setupc=0;
    static int teardc=0;
    
    @Override
    public void setUp() {
        if (server == null) {
           setupc++;
           
            LightAuth.init(new LightAuth("lightsoa"));

            AgentCloud group = new AgentCloud(ME);
            NoStatics.put(Agents.class, new Agents(group, ME));

            server = new LightServer(PORT, null);
            server.registerHandler(cmdGET);
            server.registerHandler(cmdPOST);

            // you can start the server in its dedicated thread or use a pool
            serverThread = new Thread(server, "AgentServerThread");

            // for testing, we want it to die at the end
            serverThread.setDaemon(false);

            // start the server thread...
            serverThread.start();
        }
    }

    @Override
    public void tearDown () {
       if (server != null) {
          teardc++;
          server.shutdown();
          try {
            serverThread.join();
         } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
          server=null;
       }
    }
    
    static final Log logger = Log.Factory.create(TestLightBase.class.getName());
}
