/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.agent.test;

import junit.framework.TestCase;

import com.razie.pub.agent.Agent;
import com.razie.pub.agent.AgentHttpService;
import com.razie.pub.base.ActionItem;
import com.razie.pub.base.log.Log;
import com.razie.pub.comms.ActionToInvoke;
import com.razie.pub.comms.AgentGroup;
import com.razie.pub.comms.AgentHandle;
import com.razie.pub.comms.LightAuth;
import com.razie.pub.comms.ServiceActionToInvoke;
import com.razie.pub.http.LightCmdGET;
import com.razie.pub.http.LightServer;

/**
 * sample for a simple agent
 * 
 * @author razvanc99
 */
public class TestSimpleAgent extends TestCase {
	static AgentHandle me = new AgentHandle("testagent1", "localhost",
			"127.0.0.1", "4446", "http://localhost:4446");
	static AgentHandle other = new AgentHandle("testagent2", "localhost",
			"127.0.0.1", "4447", "http://localhost:4447");

	public void setUp() {
		LightAuth.init(new LightAuth("lightsoa"));
	}

	/** start an agent and mount basic services */
	public static Agent startAgent(AgentHandle h, AgentGroup g) {
		Agent agent = new Agent(me, g);
		agent.getMainContext().enter();
		agent.onInit();

		LightAuth.init(new LightAuth("lightsoa"));

		// we need a server with cmdget to accept bindings for services. You
		// should do this for any
		// agent
		LightServer server = new LightServer(Integer.parseInt(h.port), agent
				.getMainContext());
		server.registerCmdListener(new LightCmdGET());
		agent.register(new AgentHttpService(agent, server));

		agent.onStartup();
		agent.getMainContext().exit();
		return agent;
	}

	// create an agent, do something with it and destroy it
	public void testDumbAgent() throws InterruptedException {
		AgentGroup group = new AgentGroup();
		group.put(me.name, me);
		Agent agent = startAgent(me, group);

		try {
			SampleJavaService sampleService= new SampleJavaService();
			agent.register(sampleService);
			Thread.sleep(500);

			ActionToInvoke a = new ServiceActionToInvoke(me.url, "samplejavaservice",
					new ActionItem("echo2"));
			Object oo = a.exec(null);
			Log.logThis("REPLY: " + oo.toString());
			assertTrue(oo.toString().contains("echo2"));
		} finally {
			agent.onShutdown();
		}

		Thread.sleep(500);
	}

	static final Log logger = Log.Factory.create(TestSimpleAgent.class
			.getName());
}
