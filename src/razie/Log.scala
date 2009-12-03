/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie

/** some logging basics 
 * 
 * @author razvanc
 */
object Log {
   def apply (msg:String) = com.razie.pub.base.log.Log.logThis (msg)
   def apply (msg:String, e:Throwable) = com.razie.pub.base.log.Log.logThis (msg, e)
   
   /** @return the same message, so you can return it */
   def alarmThis (msg:String) = {
      com.razie.pub.base.log.Log.alarmThis (msg) 
      msg
	   }
	   
   /** @return the same message, so you can return it */
   def alarmThis (msg:String, e:Throwable) = {
      com.razie.pub.base.log.Log.alarmThis (msg, e) 
      msg
	   }

   /** optimized so the code is not even invoked if tracing is off ... don't suppose this will cause side-effects? */
   def traceThis (f : => Any) = {
      if (com.razie.pub.base.log.Log.isTraceOn()) {
         val p = f
         p match {
            case s:String => com.razie.pub.base.log.Log.traceThis (s) 
            case (s:String,e:Throwable) => com.razie.pub.base.log.Log.traceThis (s,e) 
            case _ => com.razie.pub.base.log.Log.traceThis (p.toString)
         }
      }
   }
}

object Debug {
   def apply (f : => Any) = Log.traceThis (f)
}
