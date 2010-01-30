/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie

import org.w3c.dom._
import com.razie.pub.base.data._

/** xml conversions.
 * 
 *  This stuff here is more from my learning scala days, but i got used to the shortcuts...
 */
object RJX {
   def apply (e:org.w3c.dom.Element) : RazElement = new RazElementJava (e)
     
   def apply (e:XmlDoc) : RazElement = new RazElementJava (e e)
     
   def apply (e:scala.xml.Elem) : RazElement = new RazElementScala (e)
}
