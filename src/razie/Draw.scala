/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie

import com.razie.pub.base._
import com.razie.pub.comms._
import com.razie.pub.draw._
import com.razie.pub.draw.widgets._

// TODO when Drawable looses this stupid method, drop this class (or move Drawable here)
trait Drawable extends com.razie.pub.draw.Drawable {
   override def getRenderer(t:Technology) : Renderer[_] = Drawable.DefaultRenderer.singleton
   
}

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
//      stuff.foreach (x => s.write(x))
      stuff.foreach {
         case l:List[Any] => for (x <- l) s.write(x)
         case i:Iterator[Any] => for (x <- i) s.write(x)
         case t:Iterable[Any] => for (x <- t) s.write(x)
         case a:Array[Any] => for (x <- a) s.write(x)
         case _ => s.write(_)  
      }
      s
   }

   /** make a link - it displays a different ActionItem than the actual link to call */
   def link (ai:ActionItem, ati:ActionToInvoke) = 
      new NavLink (ai, ati)
   /** make a button - it displays a different ActionItem than the actual link to call */
   def button (ai:ActionItem, url:String) = 
      new NavButton (ai, url)
  
   // text, formatted accordingly, escaped and everything
   def text (s:String) = new DrawText (s)
   // xml - in the future we'll find a nice colored formatter
   def xml (s:String) = new DrawText (s)
   // html section - best avoided since only works nice with some clients (web)
   def html (s:String) = new DrawToString (s)
   // any object to String - no other html formatting
   def toString (s:Any) = new DrawToString (s)
   def hBar = new com.razie.pub.draw.widgets.HorizBar()

   /** make sure the first item has all the attributes - it will dictate the columns */
   def attrTable (o:Iterable[AttrAccess]) : DrawTable = {
      val table = new DrawTableScala()
      // the headings are the name of the attributes
      val whatamess:Iterable[String]= scala.collection.JavaConversions.asIterable(o.head.getPopulatedAttr)
      table.headers = whatamess
      table.prefCols = whatamess.size

      for (x <- o; a <- table.headers) table.write (x.a(a))
             
      table
   }
}
