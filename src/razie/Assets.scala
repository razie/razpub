/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie

import org.w3c.dom._
import com.razie.pub.agent._
import com.razie.pub.lightsoa._
import com.razie.pub.assets._
//import com.razie.assets._
import scala.collection.mutable._
import com.razie.pub.base.data._
import com.razie.pub.assets._
import com.razie.pub.base._
import com.razie.pub.base.data._
import razie.assets._

/** simplified asset management functionality 
 * 
 * @author razvanc
 */
object Assets {

   //------------------------- XP shorthands for accessing assets 
   def xpl (path:String)  = new XP[AssetBase] (path).xpl(new XpAssetsSolver, null) 
   def xpe (path:String)  = new XP[AssetBase] (path).xpe(new XpAssetsSolver, null) 
   def xpa (path:String)  = new XP[AssetBase] (path).xpa(new XpAssetsSolver, null) 
   def xpla (path:String) = new XP[AssetBase] (path).xpla(new XpAssetsSolver, null) 
   
   /** add a managed asset instance. These assets do not need an inventory - will all use the proxy inventory 
    * 
    * @param ass the asset to be managed
    * @param meta - the meta
    */ 
   def manage(ass:AssetBase, m:String):Unit = manage (ass, new Meta(new ActionItem(m), null, null, null, null))

   /** add a managed asset instance. These assets do not need an inventory - will all use the proxy inventory 
    * 
    * @param ass the asset to be managed
    * @param meta - the meta
    */ 
   def manage(ass:AssetBase, m:Meta):Unit = manage (ass, new MetaSpec(m))
   
   /** add a managed asset instance. These assets do not need an inventory - will all use the proxy inventory 
    * 
    * @param ass the asset to be managed
    * @param meta - optionally, the meta - if not passed in, the asset must be tagged with SoaAsset with a valid meta
    */ 
   def managejj(ass:AssetBase):Unit = manage(ass)
   
   def manage(ass:AssetBase , m:MetaSpec*):Unit = {
      val ms = if (m.length > 0) m(0) else ass.asInstanceOf[HasMeta].metaSpec
      val inv = 
         if (InventoryAssetMgr.instance().hasInventory (ass.getKey.getType)) 
            InventoryAssetMgr.instance().findInventory(ass.getKey().getType()) 
         else null

      if (ass.getClass.getAnnotation(classOf[SoaAsset]) != null) 
         ass.getClass.getAnnotation(classOf[SoaAsset]).asInstanceOf[SoaAsset].bindings.foreach ( _ match {
            //TODO should i bind them all by default? use bindings just to restrict binding if needed?
            case "http" => 
               if (AgentHttpService.instance()!=null)
                  AgentHttpService.registerSoaAsset(ass.getClass, ms.meta)
         })
      
      ms.meta.inventory = classOf[ProxyInventory].getName();

      if (inv == null)
         razie.Metas.add(ms)

      InventoryAssetMgr.instance().proxyInventory().register(ass.getKey(), ass);
   }
  
   
   // ------------------------ associations
  
   /** create an instance association between the two. These instances are kept in sync with the entities 
    * 
    * @param a from 
    * @param b to
    * @param ma association type
    */
   def associate (a:AssetKey, b:AssetKey, ma:MetaAssoc*):Unit = 
     associate (Asset.apply(a), Asset.apply(b), ma:_*)
   
   def associate (a:AssetBase, b:AssetBase, ma:MetaAssoc*):Unit = 
      AssetAssocs.associate (a,b,ma:_*)
}

/** asset association management 
 * 
 * @author razvanc
 */
object AssetAssocs {

   /** creates an instance association between the two assets. 
    * @parm a 
    * @parm z
    * @parm ma - the type of association. If not given, will look it up in metas
    */
   def associate (a:AssetBase, b:AssetBase, ma:MetaAssoc*):Unit = {
      // TODO keep in sync- register with inventory and delte assocs or cascade or exception
      val mas = if (ma.size > 0) ma.head else {
         // TODO not both ways, just one way - between is both ways
         val temp = Metas.assocsBetween(a.getKey.meta, b.getKey.meta)
         require (temp.size == 1)
         temp.head
      }
      
     require (mas.aEnd == a.getKey.meta && mas.zEnd == b.getKey.meta) 
     assocs += ((Asset.apply(a), Asset.apply(b), mas))
   }

      // TODO 2-2 add inventory of associations
   // TODO 2-2 add remote discovery of associations between remote entities
   /** find associated assets to the given key, both ways - browse only the given type
    * 
    * @param key the key to start from
    * @param a - oiptional, an asset object to reflect
    * @param ma - the association type to follow
    *  */
   protected def iassociated(key:AssetKey, a:Referenceable, ma:MetaAssoc):List[AssetBase] = {
      val ret = if (ma.aEnd == key.meta)
         assocs.filter (x => x._1.key  == key && x._3 == ma).map (_._2.resolveIfLocal.asInstanceOf[AssetBase])
      else
         assocs.filter (x => x._2.key == key && x._3 == ma).map (_._1.resolveIfLocal.asInstanceOf[AssetBase])
      
      if (ret.isEmpty && key.loc.isLocal) {
         // try reflection of associations - only for local assets
         val reala = if (a!= null) { 
            if (a.isInstanceOf[AssetHandle])
               a.asInstanceOf[AssetHandle].resolve 
            else a
            } 
         else 
            Asset.apply(key).resolve
            
         val methods = if (ma.aEnd == key.meta)
            reala.getClass.getMethods.filter (_.getName==ma.zRole)
            else 
            reala.getClass.getMethods.filter (_.getName==ma.aRole)
            
         methods.headOption match {
               case Some(method) => method.invoke (reala) match {
                  case l:List[AssetBase] => l
                  case a:AssetBase => List(a)
                  case _ => ret.toList
               }
                case _ => ret.toList
            }
      } else 
         ret.toList
   }
   

         // TODO 2-2 add inventory of associations
         // TODO 2-2 add remote discovery of associations between remote entities
   /** find associated assets to the given key, both ways - browse only the given type
    * 
    * @param key the key to start from
    * @param ma - the association type to follow
    *  */
   def associated(key:AssetKey, ma:MetaAssoc) = 
      iassociated (key, null, ma)

   /** find associated assets to the given key, both ways - browse only the given type
    * 
    * @param a - an asset object to reflect
    * @param ma - the association type to follow
    *  */
   def associated (a:AssetBase, ma:MetaAssoc) = 
      iassociated(a.getKey, a, ma)
//      associatedKeys (a.getKey, ma).map (Asset.apply(_).resolve)

   /** find keys of associated assets to the given key, both ways - browse only the given type
    * 
    * @param key the key to start from
    * @param ma - the association type to follow
    *  */
   def associatedKeys (k:AssetKey, ma:MetaAssoc) = iassociated(k, null, ma).map (_.getKey)

   // TODO 3-2 optimize - index by key or something
   def assocs = assocsns.get
     
   private val assocsns = new razie.NoStatic[ListBuffer[(AssetHandle, AssetHandle, MetaAssoc)]] ("Assets.Assocs" , 
         {new ListBuffer[(AssetHandle, AssetHandle, MetaAssoc)] ()} )

         //   def assocs : TAssocs = com.razie.pub.base.NoStaticS.get[TAssocs] match {
//      case Some (l) => l
//      case None => com.razie.pub.base.NoStaticS.put (new TAssocs ())
//   }
//     
//   private type TAssocs = ListBuffer[(AssetKey, AssetKey, MetaAssoc)]
}

