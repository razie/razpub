/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie

import com.razie.pub.assets._
import com.razie.pub.comms._
import razie.assets._

/** simplified access to agent functionality
 * 
 * @author razvanc
 */
object Agent {

   implicit def instance () : com.razie.pub.agent.Agent = com.razie.pub.agent.Agent.instance()

   def me = Agents.me()
   
   def apply () = com.razie.pub.agent.Agent.instance()
   
   def apply (url:String) = handle(url)

   /** 
    * execute a piece of code in my context - used during initialization normally. Threads inside an agent are 
    * already in that agent's context but, during initialization, when you startup a few agents, 
    * the code is not running in an agent's context, so using this is very important.
    * 
    * You can safely embedd these calls
    */
   def inContext[A] (f: => A) : A = {
      // use my thread context now
      val old = apply().getContext().enter();
      val res = f 
      com.razie.pub.base.ExecutionContext.exit(old); //agent.getMainContext().exit(null);
      res 
      }
   
   /** create a localhost handle with the given port
    * 
    * @param port the port the agent listens on
    */
   def local (port:String) = {
	  new AgentHandle("localhost-"+port, "localhost", "127.0.0.1", port, "http://localhost:"+port) 
   }

   /** create a handle to the respective agent */
   def handle (url:String) = {
      val (host,port) = hostport(url)
	  new AgentHandle(host+"-"+port, host, host, port, url) 
   }
   
   /** parse the host:port out of a URL */
	def hostport (url:String) : (String,String) = {
		val v = new AssetLocation (url)
		(v.getHost, v.getPort)
	}

	/** proxy an action to another agent */
   def proxy (ati:ActionToInvoke) = 
      Service ("proxy") action ("serve", AA ("via", Agents.me.url, "url", ati.makeActionUrl))
      
	/** proxy an access to a url */
   def proxy (url:String) = 
         Service ("proxy") action ("serve", AA ("via", Agents.me.url, "url", url))
}

// TODO - this is for what? sending events/messages?
class SmartHandle (val h:AgentHandle) {
   /** proxy an action to another agent */
   def proxy (ati:ActionToInvoke) = 
      Service (h, "proxy") action ("serve", AA ("via", h.url, "url", ati.makeActionUrl))
      
   /** proxy an access to a url */
   def proxy (url:String) = 
         Service (h, "proxy") action ("serve", AA ("via", h.url, "url", url))
}
