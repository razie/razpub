/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie

import com.razie.pub.base._
import com.razie.pub.comms._
import com.razie.pub.draw._
import com.razie.pub.draw.widgets._

/** condense most useful drawing primitives */
object Draw {

   /** put some objects in a sequence - sequence is not formatted */
   def seq (stuff:Any*) = {
      val s = new DrawSequence()
      stuff.foreach (x => s.write(x))
      s
   }
  
   /** put some objects in a list - lists are html table */
   def list (stuff:Any*) = {
      val s = new DrawList()
      stuff.foreach (x => s.write(x))
      s
   }

   /** make a link - it displays a different ActionItem than the actual link to call */
   def link (ai:ActionItem, ati:ActionToInvoke) = 
      new NavLink (ai, ati)
   /** make a button - it displays a different ActionItem than the actual link to call */
   def button (ai:ActionItem, url:String) = 
      new NavButton (ai, url)
}
