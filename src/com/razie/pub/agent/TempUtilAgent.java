/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.agent;

import com.razie.pub.base.ExecutionContext;
import com.razie.pub.comms.AgentCloud;
import com.razie.pub.comms.AgentHandle;
import com.razie.pub.comms.LightAuth;
import com.razie.pub.http.LightCmdGET;
import com.razie.pub.http.*;

/** netbeans can't share unit classes forcing me to move testing code here - this is temp until i figure a workaround
 *
 * @author razvanc
 */
public class TempUtilAgent {
	public static AgentHandle me = new AgentHandle("testagent1", "localhost",
			"127.0.0.1", "4446", "http://localhost:4446");
	public static AgentHandle other = new AgentHandle("testagent2", "localhost",
			"127.0.0.1", "4447", "http://localhost:4447");


   /** start an agent and mount basic services */
   public static Agent startAgent(AgentHandle h, AgentCloud g) {
      Agent agent = new Agent(h, g);
      agent.getContext().enter();
      agent.onInit();

      LightAuth.init(new LightAuth("lightsoa"));

      // we need a server with cmdget to accept bindings for services. You
      // should do this for any
      // agent
      LightServer server = new LightServer(Integer.parseInt(h.port), 20, agent.getContext(), new LightContentServer(null));
      server.registerHandler(new LightCmdGET());
      agent.register(new AgentHttpService(agent, server));

      agent.onStartup();
      ExecutionContext.exit();
      return agent;
   }
}
