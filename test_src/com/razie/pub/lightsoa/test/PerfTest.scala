package com.razie.pub.lightsoa.test

import com.razie.pub.base.ExecutionContext;
import com.razie.pub.base.NoStatics;
import com.razie.pub.base.log.Log;
import com.razie.pub.comms.AgentCloud;
import com.razie.pub.comms.AgentHandle;
import com.razie.pub.comms.Agents;
import com.razie.pub.comms.LightAuth;
import com.razie.pub.http.LightCmdGET;
import com.razie.pub.http.LightCmdPOST;
import com.razie.pub.http.LightServer;

object PerfTest extends Application {
   val PORT = 4445;
   val ME = new AgentHandle("localhost", "localhost", "127.0.0.1", PORT
                                               .toString(), "http://localhost:" + PORT.toString());

}
