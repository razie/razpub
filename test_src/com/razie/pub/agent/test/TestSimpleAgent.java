/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.agent.test;

import junit.framework.TestCase;

import com.razie.pub.agent.Agent;
import com.razie.pub.agent.TempUtilAgent;
import com.razie.pub.base.ActionItem;
import com.razie.pub.base.log.Log;
import com.razie.pub.comms.ActionToInvoke;
import com.razie.pub.comms.AgentCloud;
import com.razie.pub.comms.AgentHandle;
import com.razie.pub.comms.LightAuth;
import com.razie.pub.comms.ServiceActionToInvoke;

/**
 * sample for a simple agent
 * 
 * @author razvanc99
 */
public class TestSimpleAgent extends TestCase {

   static AgentHandle me = TempUtilAgent.me;
   static AgentHandle other = TempUtilAgent.other;

   @Override
   public void setUp() {
      LightAuth.init(new LightAuth("lightsoa"));
   }

   /** start an agent and mount basic services */
   public static Agent startAgent(AgentHandle h, AgentCloud g) {
      // TODO inline
      return TempUtilAgent.startAgent(h, g);
   }

   // create an agent, do something with it and destroy it
   public void testDumbAgent() throws InterruptedException {
      AgentCloud group = new AgentCloud();
      group.put(me);
      Agent agent = startAgent(me, group);

      try {
         SampleJavaService sampleService = new SampleJavaService();
         agent.register(sampleService);
         Thread.sleep(500);

         ActionToInvoke a = new ServiceActionToInvoke(me.url, "samplejavaservice",
                 new ActionItem("echo2"));
         Object oo = a.act(null);
         Log.logThis("REPLY: " + oo.toString());
         assertTrue(oo.toString().contains("echo2"));
      } finally {
         agent.onShutdown();
      }

      Thread.sleep(500);
   }
   static final Log logger = Log.Factory.create(TestSimpleAgent.class.getName());
}
