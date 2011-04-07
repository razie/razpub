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
   def apply (name:String) = AssetMgr.meta(name)
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

