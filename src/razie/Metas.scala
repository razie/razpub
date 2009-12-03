/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie

import org.w3c.dom._
import com.razie.pub.agent._
import com.razie.pub.lightsoa._
import com.razie.pub.assets._
import com.razie.pub.base._
import com.razie.pub.base.data._
import razie.assets._

/** simplified meta info management
 * 
 * @author razvanc
 */
object Metas {
   /** get a meta by name */
   def meta (name:String) : Meta = AssetMgr.meta(name) match {
      case m:Meta => m
      case _ => throw new IllegalArgumentException ("ERR_PROG Meta not found: " + name)
   }
   
   def add(m:Meta):Unit = AssetMgr.register(m)
         
   def add(ms:MetaSpec):Unit =  {
      AssetMgr.register(ms.meta)
      if (ms.assocs != null) ms.assocs.foreach(addAssoc (_))
   }
   
   //------------------ associations
   
   // TODO 1-2 optimize (index) assocs
   def assocsFrom (aEnd:String) = assocs.filter (_.aEnd == aEnd)
   def assocsTo (zEnd:String) = assocs.filter (_.zEnd == zEnd)
   def assocsFromAndTo (aEnd:String) = assocs.filter (x => x.aEnd == aEnd || x.zEnd == aEnd)
   def assocsBetween (aEnd:String, zEnd:String) = 
      assocs.filter (x => x.aEnd == aEnd && x.zEnd == zEnd || x.aEnd == zEnd && x.zEnd == aEnd)
      
   def addAssoc (ma:MetaAssoc):Unit = 
      if (!assocs.contains(ma)) assocs.append(ma)
   
//   def assoc (ma:MetaAssoc) = assocs.append(ma)
   
   // TODO 3-2 optimize
   def assocs = assocsns.get
     
   private val assocsns = new razie.NoStatic[collection.mutable.ListBuffer[MetaAssoc]] ("Metas.Assocs" , 
         {new collection.mutable.ListBuffer[MetaAssoc] ()} )
}

trait SimpleXml {
   def ax (e:Element, name:String, dflt:String="") = 
      if (e.hasAttribute (name)) e.getAttribute(name) else dflt

   def a (e:RazElement, name:String, dflt:String="") = 
      if (e.ha(name)) e.a(name) else dflt
}

object Meta extends SimpleXml {
   def fromXml (e:RazElement) =
      new Meta(
            razie.AI cmdicon (a(e, "name"), a(e, "icon")), 
            "", a(e, "inventory"))
}

object MetaAssoc  extends SimpleXml {
   /** when defined by itself */
   def fromXml (e:RazElement) =
      new MetaAssoc (
            name = a(e,"name"),
            aEnd = a(e,"aEnd"),
            zEnd = a(e, "zEnd"),
            stereotype = a(e, "stereotype", "assoc"), 
            card = a(e, "aCard", "0-n"), 
            aRole = a(e, "aRole"), 
            zRole = a(e, "zRole")
            )
   
   /** when defined under a meta parent tag, which is the "aEnd" */
   def fromXml (e:RazElement, m:RazElement) =
      new MetaAssoc (
            name = a(e, "name"),
            aEnd = a(e,"aEnd", a(m, "name")),
            zEnd = a(e, "zEnd"),
            stereotype = a(e, "stereotype", "assoc"), 
            card = a(e, "aCard", "0-n"), 
            aRole = a(e, "aRole"), 
            zRole = a(e, "zRole")
            )
}