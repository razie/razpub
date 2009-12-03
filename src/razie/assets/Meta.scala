/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie.assets;

import java.lang.reflect.Method;

import com.razie.pub.base.ActionItem;
//import com.razie.pub.lightsoa.SoaMethod;
//import com.razie.pub.lightsoa.SoaMethodSink;

/**
 * a meta-description of an asset type
 * 
 * The inventory and asset class specs are used elsewhere...didn't bother extending...you don't have
 * to use them
 * 
 * TODO 1-1 metas need a namespace
 */
class Meta (val id:ActionItem, val baseMeta:String, val assetCls:String, var inventory:String, val namespace:String) {

   var supportedActions : Array[ActionItem] = null

   // for older java code
   def getId = id
   def getBaseMeta = baseMeta
   def assetCld = assetCls
   def getInv = inventory
   
   /** basic constructor - the inventory/class are set automatically when registered */
   def this (id:ActionItem, base:String ) = this (id, base, null, null, null)

   /** basic constructor - the inventory/class are set automatically when registered */
   def this (id:ActionItem) = this (id, null, null, null, null)

   /** a meta for assets with an inventroy */
   def this(id:ActionItem, base:String , inv:String ) = this (id, base, null, inv, null)

//   private void reflectActions () {
//      List<ActionItem> l = new ArrayList<ActionItem>();
//      if (assetCls != null || assetCls.length() > 0)
//         try {
//            for (Method m : Class.forName(assetCls, true, ClassLoader.getSystemClassLoader()).getMethods()) {
//                 if (m.getAnnotation(SoaMethod.class) != null 
//                       && m.getAnnotation(SoaMethodSink.class) == null) {
//                     l.add(new ActionItem (m.getName()));
//                 }
//             }
//         } catch (Exception e) {
//            e.printStackTrace();
//         }
//  }

   def toAA : razie.AA = {
      val aa = razie.AA ()
      aa.set("name", id.name)
      aa.set("inventory", inventory)
      if (baseMeta != null && baseMeta.length() > 0)
         aa.set("base", baseMeta)

      aa
   }
   
   def toBriefXml() : String = {
      val b = new StringBuilder();
      b.append("<metaspec name=\"" + id.name + "\"");
      b.append(" inventory=\"" + inventory + "\"");
      if (baseMeta!= null && baseMeta.length() > 0)
         b.append(" base=\"" + baseMeta + "\"");
      b.append(">");

      b.append("</metaspec>");

      return b.toString();
   }

   def toDetailedXml() : String = {
      val b = new StringBuilder();
      b.append("<metaspec name=\"" + id.name + "\"");
      b.append(" inventory=\"" + inventory + "\"");
      if (baseMeta != null && baseMeta.length() > 0)
         b.append(" base=\"" + baseMeta + "\"");
      b.append(">");

      for (ai <- supportedActions) {
         b.append("<action name=\"" + ai.name + "\"");
         b.append("/>");
      }
      // TODO 1-1 add assocs

      b.append("</metaspec>");

      return b.toString();
   }

   override def toString() : String = toBriefXml()
}

/** describes an association between metas 
 * 
 * TODO 1-1 do assocs need a namespace?
 * */
class MetaAssoc (
      val name:String,
      val aEnd:String, val zEnd:String, val stereotype:String, 
      val card:String="0..1-0..n", val aRole:String="", val zRole:String="") { 
   // TODO i need to implement the traceback: who injected/defined this association
   val traceback:String = null
}

/** this is a full specification of a meta: the meta itself together with all neccessary associations. Note that the associations can be only from and to the current meta
 * 
 * @param m the meta specification 
 * @param assocs all associations to and from the meta
 */
class MetaSpec (val meta:Meta, val assocs:List[MetaAssoc]){
   def this (m:Meta) = this (m, List())
   def this (m:String) = this (new Meta (razie.AI.stoai(m)), List())
}

/** implemented by all classes who know their metas - simplifies code Assets.manage(new MyAsset()) */
trait HasMeta {
   def metaSpec : MetaSpec
}
