/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package razie

import com.razie.pub.base.ExecutionContext

object NoStatic {
   /** this will cleanup all the execution contexts in this JVM...as if you'd kill and restart the JVM */
   def resetJVM = ExecutionContext.resetJVM()
}

/**
 * thread local static object - ThreadLocal is easy to implement, not neccessarily to use
 * 
 * Use like
 * <code> public static NoStatic<MyClass> myStatic = new NoStatic<MyClass>("myStatic", new MyClass(...))</code>
 * .
 * 
 * On a thread, reset the default value if needed:
 * <code>myStatic.set(newValue)</code>
 * 
 * In code, access like a static, don't worry about thread:
 * <code>myStatic.get</code> or <code>myStatic()</code>
 * 
 * NOTE this is a fairly limited implementation: uses only one instance per class type - use NoStatics instead!
 * 
 * TODO 3-2 make mtsafe - didn't need it so far
 * 
 * see TLNoStatic for why ThreadLocal is not usable
 * 
 * @see com.razie.pub.base.test.TestNoStatic
 * @author razvanc99
 */
class NoStatic[T] (val id:String, initialValue : => T) {
   private var value : T = initialValue

   def get = find.value
   def apply() = find.value

   /**
    * here's the tricky part... will set only on the particular thread ... IF
    * it has a context...
    */
   def set(newValue:T) : T = { find.value = newValue; newValue }

   def find : NoStatic[T] = {
      val tx = ExecutionContext.instance();
      
      if (tx.isPopulated(id)) {
         (ExecutionContext.instance().getAttr(id)).asInstanceOf[NoStatic[T]]
      } else {
         // here's the magic: clone itself for new context with new value
         // TODO explicit samples/tests for this
         val newInst = 
            if (tx == ExecutionContext.DFLT_CTX) { 
               // resetJVM was performed
               this.value=initialValue; 
               this 
               } 
            else new NoStatic[T](id, initialValue)
         tx.set(id, newInst)
         newInst
      }
   }
}
