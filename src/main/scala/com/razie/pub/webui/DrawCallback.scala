/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.draw

import com.razie.pub.comms._
import razie.base.ActionItem
import com.razie.pub.base._
//import com.razie.pub.agent._
import com.razie.pub.base.log._
import razie.base._
import razie.draw._

/** 
 * often you want different screens pre-built and invoked at a later time. 
 * This class allows you to register a pre-built screen and you get an invocable URL for that screen
 * 
 * you can either pre-build a Drawable3 or inject a function that will build it dynamically
 */
object DrawCallback {
   type CallbackFun = (AttrAccess) => Drawable
   
   def apply (screen:Drawable, validSec:Int) = DrawCallback1 (screen, validSec)
   def apply (screen:CallbackFun, validSec:Int) = DrawCallback2 (screen, validSec)
}

/** simple self-cleaning cache */
protected object MyCache {
   final val MAXTIMEMSEC = 5 * 60 * 1000
   
   var counter:Int = 1
   
   var cache : collection.mutable.Map[String,(AnyRef,Int, Long)] = 
     new collection.mutable.HashMap[String, (AnyRef,Int, Long)]()

   def put (screen:Drawable, validSec:Int) : String = 
      iput (screen,validSec)
      
   def put (screen:DrawCallback.CallbackFun, validSec:Int) : String = 
      iput (screen,validSec)
   
   private def iput (screen:AnyRef, validSec:Int) : String = synchronized {
     clean
     val url:String = counter.toString
     counter+=1
     cache.put (url,(screen,validSec, System.currentTimeMillis()))
     url
     }
  
   /** the return is either a Drawable3 or a CallbackFun */
   def get (url:String) : Option[AnyRef] = synchronized {
      clean
      cache.get(url) match {
        case Some((d, _, _)) => new Some(d)
        case None => None
        }
     }
    
   // TODO unit-test this
   def clean () = synchronized {
     // TODO only do this every like 1 min - or better, based on size, not more often, eh?
     val curt = System.currentTimeMillis
     cache.retain((x,y) => (curt - y._3 < MAXTIMEMSEC))
   }
   
   def clear () = synchronized { cache.clear }
   
   def keys = synchronized { cache.keys.toList ::: List() } // clones it I hope ?
}

/** often you want different screens pre-built and invoked at a later time. 
 * This class allows you to register a pre-built screen and you get an invocable URL for that screen
 */
abstract class DrawCallback (screen:AnyRef, validSec:Int, url:String) 
   extends ServiceActionToInvoke (classOf[DrawScreenService].getSimpleName, 
                                  DrawScreenServiceStatic.DRAW, "screen", 
                                  url) {
     DrawScreenServiceStatic.autoRegister
}

case class DrawCallback1 (screen:Drawable, validSec:Int) extends DrawCallback(screen, validSec, MyCache.put (screen,validSec))
case class DrawCallback2 (screen:DrawCallback.CallbackFun, validSec:Int) extends DrawCallback(screen, validSec, MyCache.put (screen,validSec))

class InternalDrawCallback (screenNo:String) 
   extends ServiceActionToInvoke (classOf[DrawScreenService].getSimpleName, 
                                  DrawScreenServiceStatic.DRAW, "screen", 
                                  screenNo) {
     DrawScreenServiceStatic.autoRegister
}
