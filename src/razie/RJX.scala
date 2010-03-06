/**  ____    __    ____  ____  ____/___     ____  __  __  ____
 *  (  _ \  /__\  (_   )(_  _)( ___) __)   (  _ \(  )(  )(  _ \           Read
 *   )   / /(__)\  / /_  _)(_  )__)\__ \    )___/ )(__)(  ) _ <     README.txt
 *  (_)\_)(__)(__)(____)(____)(____)___/   (__)  (______)(____/   LICENESE.txt
 */
package razie

import org.w3c.dom._
import com.razie.pub.base.data._
import razie.base.data._

/** xml conversions.
 * 
 *  This stuff here is more from my learning scala days, but i got used to the shortcuts...
 */
object RJX {
   def apply (e:org.w3c.dom.Element) : RazElement = new RazElementJava (e)
     
   def apply (e:XmlDoc) : RazElement = new RazElementJava (e e)
     
   def apply (e:scala.xml.Elem) : RazElement = new RazElementScala (e)
}
