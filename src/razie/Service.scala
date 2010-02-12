/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie

import razie.base._
import com.razie.pub.base._
import com.razie.pub.agent._
import com.razie.pub.assets._
import com.razie.pub.comms._

/** syntax simplifier
 * 
 * <code>Service ("player") action ("play", AA ("movie", ref)) </code>
 * 
 * TODO junit
 */
object Service {
   def apply (location:AgentHandle, name:String) : ServiceHandle = 
      new ServiceHandle (location, name)
   
   def apply (name:String) : ServiceHandle = 
      new ServiceHandle (Agents.me(), name)
}

/** a service handle 
 * 
 * TODO 3-2 poltergeist
 * */
class ServiceHandle (val loc:AgentHandle, val name:String) {
	
	   /**
	    * create an action to invoke
	    * 
	    * @param method action/method name
	    * @param args
	    * @return an action-to-invoke
	    */
   def action (method:String, args:AttrAccess*) = 
	   if (loc == Agents.me)
	      new ServiceActionToInvoke(name, AI(method), args:_*)
	   else
	      new ServiceActionToInvoke(loc.url, name, AI(method), args:_*)
   
   /**
    * create an action to invoke
    * 
    * @param action action/method name
    * @param args
    * @return an action-to-invoke
    */
   def action (action:ActionItem, args:AttrAccess*) = {
	   if (loc == Agents.me)
	      new ServiceActionToInvoke(name, action, args:_*)
	   else
	      new ServiceActionToInvoke(loc.url, name, action, args:_*)
   }

   /**
    * create an action to invoke
    * 
    * @param method action/method name
    * @param args
    * @return an action-to-invoke
    */
   def action(method:String, args:AnyRef* ) : AnyRef = 
       action(method, (new AttrAccessImpl(args)).asInstanceOf[AttrAccess]);

   /** simplify sending messages: Service ("mysvc").msg(myobject).sendTo(remote) */
   def msg (payload : Any) = 
      ServiceMsg (name, payload)
}