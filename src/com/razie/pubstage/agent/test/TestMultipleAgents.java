/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pubstage.agent.test;

import junit.framework.TestCase;

import com.razie.pub.base.log.Log;
import com.razie.pub.http.AgentHandle;
import com.razie.pub.http.LightAuth;
import com.razie.pub.http.LightCmdGET;
import com.razie.pub.http.LightServer;
import com.razie.pubstage.agent.Agent;
import com.razie.pubstage.agent.AgentGroup;
import com.razie.pubstage.agent.webservice.AgentHttpService;

/**
 * test the light server
 * 
 * @author razvanc99
 */
public class TestMultipleAgents extends TestCase {
    static AgentHandle me    = new AgentHandle("testagent1", "localhost", "127.0.0.1", "4444",
                                     "http://localhost:4444");
    static AgentHandle other = new AgentHandle("testagent2", "localhost", "127.0.0.1", "4445",
                                     "http://localhost:4445");

    public void setUp() {
        LightAuth.init(new LightAuth("lightsoa"));
    }

    /** start an agent and mount basic services */
    public Agent startAgent(AgentHandle h, AgentGroup g) {
        Agent agent = new Agent(me, g);
        agent.onInit();

        LightAuth.init(new LightAuth("lightsoa"));

        // we need a server with cmdget to accept bindings for services. You should do this for any
        // agent
        LightServer server = new LightServer(Integer.parseInt(h.port), agent.getMainContext());
        server.registerCmdListener(new LightCmdGET());
        agent.register(new AgentHttpService(agent, server));

        agent.onStartup();
        return agent;
    }

    // create an agent, do something with it and destroy it
    public void testDumbAgent() throws InterruptedException {
        AgentGroup group = new AgentGroup();
        group.put(me.name, me);
        Agent agent = startAgent(me, group);
        // ...

        agent.onShutdown();
        Thread.sleep(500);
    }

    // create an agent, do something with it and destroy it
    public void testHttpAgent() {
        AgentGroup group = new AgentGroup();
        group.put(me.name, me);
        Agent agent = startAgent(me, group);

        // ...

        agent.onShutdown();
    }

    // two agents in the same JVM, see if they connect to each-other
    public void testConnectingAgents() {
        AgentGroup group = new AgentGroup();
        group.put(me.name, me);
        group.put(other.name, other);
        Agent agent1 = startAgent(me, group);
        Agent agent2 = startAgent(other, group);

        // ...

        agent1.onShutdown();
        agent2.onShutdown();
    }

    static final Log logger = Log.Factory.create(TestMultipleAgents.class.getName());
}
