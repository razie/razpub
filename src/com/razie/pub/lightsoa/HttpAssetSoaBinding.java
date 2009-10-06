/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.lightsoa;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.razie.pub.assets.AssetBrief;
import com.razie.pub.assets.AssetKey;
import com.razie.pub.assets.AssetLocation;
import com.razie.pub.assets.AssetMgr;
import com.razie.pub.assets.AssetPres;
import com.razie.pub.base.AttrAccess;
import com.razie.pub.base.data.HttpUtils;
import com.razie.pub.comms.MyServerSocket;
import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.Renderer.Technology;
import com.razie.pub.http.StreamConsumedReply;

/**
 * special binding for assets.
 * 
 * there is only one instance of this, registered as the service name "asset"
 * 
 * @author razvanc99
 */
public class HttpAssetSoaBinding extends HttpSoaBinding {
   Map<String, HttpSoaBinding> bindings            = new HashMap<String, HttpSoaBinding>();

   // hack: null has no methods and, in HttpSoaBinding, will default to delegating to AssetMgr
   static HttpSoaBinding       defaultAssetBinding = new HttpSoaBinding((Class<?>) null, "");

   /**
    * create a simple binding - you then have to register it with the server
    */
   public HttpAssetSoaBinding() {
      // dummy - i have no soa methods
      super(HttpAssetSoaBinding.class, "asset");

      // now should register all asset types as listeners...
   }

   public void register(Class<?> c, AssetMgr.Meta... meta) {
      String m = meta.length > 0 && meta[0] != null ? meta[0].id.name : ((SoaAsset) c
            .getAnnotation(SoaAsset.class)).meta();
      if (!bindings.containsKey(m))
         bindings.put(m, new HttpSoaBinding(c, m));
   }

   public boolean has(String type) {
      return bindings.containsKey(type);
   }

   @Override
   public Object execServer(String actionName, String protocol, String cmdargs, Properties parms,
         MyServerSocket socket) {
      // two ways to specify an asset: /asset/TYPE/KEY/ (localassets) or /asset/razie.uri....(local
      // and remote)
      AssetKey key = null;
      if (actionName.startsWith(AssetKey.PREFIX))
         // url is /asset/KEY/cmd
         key = AssetKey.fromString(HttpUtils.fromUrlEncodedString(actionName));
      else if (cmdargs.length() == 0) {
         // TODO url is /asset/TYPE - by convention, list all of type
         listLocal (actionName, "", true, makeDrawStream(socket, protocol));
         return new StreamConsumedReply();
      } else {
         // url is /asset/TYPE/KEY/cmd
         String[] xx = cmdargs.split("/", 2);
         key = new AssetKey(actionName, HttpUtils.fromUrlEncodedString(xx[0]), null);
      }

      HttpSoaBinding binding = bindings.get(key.getType());
      if (binding == null) {
         // throw new IllegalArgumentException("ERR_HTTPSOAASSET type is not bound: " +
         // key.getType());
         // try to call a generic asset via its inventory or other injection mechanism...
         return defaultAssetBinding.execServer(actionName, protocol, cmdargs, parms, socket);
      } else
         return binding.execServer(actionName, protocol, cmdargs, parms, socket);
   }

   /** list some assets directly to the output stream */
   public static void listLocal(String ttype, String location, boolean recurse, DrawStream out) {
      AssetLocation env = AssetLocation.mutantEnv(location);

      Map<AssetKey, AssetBrief> movies = AssetMgr.find(ttype, env, recurse);

      if (Technology.TEXT.equals(out.getTechnology())) {
         out.write(movies.toString());
      } else {
         AssetPres.instance().toDrawable(movies.values(), out, null);
      }
   }


   /** invoke a lightsoa method on a given service */
   public Object invokeLocal(AssetKey key, String action, AttrAccess inparms) {
      HttpSoaBinding binding = bindings.get(key.getType());
      return binding.invoke(key, action, inparms);
   }

}
