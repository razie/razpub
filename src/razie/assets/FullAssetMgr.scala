/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie.assets

import com.razie.pub.base._
import com.razie.pub.assets._
import com.razie.pub.base.data._

/** complete assetmgr - allows injection of actions on assets 
 * 
 * @author razvanc
 */
class FullAssetMgr extends InventoryAssetMgr with AssetMgrInjector {

   /** this version takes into account possible injections first */
   override def getSupportedActions(ref:AssetKey) : Array[ActionItem] =
      super.getSupportedActions(ref) ++  injections (ref.getType())

   /** this version takes into account possible injections first */
   override def doAction(cmd:String, ref:AssetKey, ctx:ScriptContext) : Object = injection (ref.getType(), cmd) match {
      case Some(x) => x(ref, null, cmd, ctx)
      case None => super.doAction(cmd,ref,ctx);
      }

}

object FullAssetMgr {
   def instance : FullAssetMgr = AssetMgr.instance.asInstanceOf[FullAssetMgr]
}