/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie;

import razie.assets.AssetActionToInvoke;
import razie.assets.AssetBase;
import razie.assets.AssetKey;
import razie.assets.Meta;

import com.razie.pub.base.ActionItem;

/** Java compatible hacks for asset management - if you write Java clients, this class should be of great interest to you... 
 * 
 * @author razvanc
 * */
public class JAS {
  
   /** make a AATI */
   public static AssetActionToInvoke aati (String target, AssetKey k, ActionItem ai, Object ...pairs) {
      // TODO 1-2 optimize
     AssetActionToInvoke ati = new AssetActionToInvoke (target, k, ai);
     ati.setAttr(pairs); 
     return ati;
   }
  
   /** delegate an instance to the default inventory */
   public static void manage (AssetBase a, Meta m) {
      Assets$.MODULE$.manage(a, m);
   }
   /** delegate an instance to the default inventory */
   public static void manage (AssetBase a) {
      Assets$.MODULE$.managejj(a);
   }
   
   /** register a new asset type */
   public static void register (Meta m) {
      Metas$.MODULE$.add(m);
   }
}