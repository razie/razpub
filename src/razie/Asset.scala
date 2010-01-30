/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie

import com.razie.pub.base.ActionItem;
import com.razie.pub.base.AttrAccess;
import com.razie.pub.base.AttrAccessImpl;
import com.razie.pub.comms.ActionToInvoke;
import razie.assets._

/** 
 * simplified asset functionality 
 * 
 * @author razvanc
 */
object Asset {
   def apply (key:AssetKey) = new AssetHandle (key)
   def apply (key:String) = new AssetHandle (AssetKey.fromString(key))
   def apply (a:AssetBase) = 
      if (a.isInstanceOf[AssetHandle])  a.asInstanceOf[AssetHandle] 
      else new AssetHandle (a)
}

/**
 * this stands in for a full asset - used by razie.Asset 
 * 
 * TODO junit
 * 
 * @author razvanc
 */
class AssetHandle (val key:AssetKey) extends AssetBase {
   var actual: AssetBase = null
  
   def this (a : AssetBase) = {
      this (a.getKey())
      actual=a
   }

   def brief = if (isResolved) actual.brief else AssetMgr.getBrief(key)
   def supportedActions = AssetMgr.getSupportedActions (key)
   
   def resolve = if (actual == null) AssetMgr.getAsset(key) else actual
   def isResolved = actual != null
   def resolveIfLocal = if (isResolved || key.loc.isLocal) resolve else this
   
   /**
    * convenience method - another form of invoke
    *
    * @param method action/method name
    * @param args
    * @return
    */
   def invoke(method:String, args:AnyRef* ) : AnyRef =
      invoke(method, (new AttrAccessImpl(args)).asInstanceOf[AttrAccess]);

   /**
    * invoke a local/remote asset
    *
    * TODO implement sync/async flavors via a messaging service abstraction
    *
    * @param method action/method name
    * @param args
    * @return
    */
   def invoke(method:String , aa:AttrAccess ) : AnyRef =
      action (method, aa).act(null)

   /**
    * make an invocable action-to-invoke
    *
    * @param method action/method name
    * @param args
       * @return an action-to-invoke
    */
   def action(method:String , aa:AttrAccess* ) : AssetActionToInvoke =
      action (new ActionItem(method), aa:_*)
   
   /**
    * make an invocable action-to-invoke
    *
    * @param method action/method name
    * @param args
       * @return an action-to-invoke
    */
   def action(method:ActionItem , aa:AttrAccess* ) : AssetActionToInvoke =
      if (key.getLocation() == null || (key.getLocation().isLocal() || !key.getLocation().isRemote()))
         new AssetActionToInvoke(key, method, aa:_*);
   else
      new AssetActionToInvoke(key.getLocation().toHttp(), key, method, aa:_*);
   
}
