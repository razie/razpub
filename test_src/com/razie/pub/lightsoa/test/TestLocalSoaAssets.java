/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.lightsoa.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;
import razie.JAS;
import razie.assets.AssetKey;
import razie.base.AttrAccess;
import razie.base.AttrAccessImpl;
import razie.draw.DrawStream;
import razie.draw.SimpleDrawStream;

import com.razie.pub.assets.JavaAssetMgr;
import com.razie.pub.base.NoStatics;
import com.razie.pub.base.log.Log;
import com.razie.pub.comms.LightAuthBase;
import com.razie.pub.lightsoa.SoaBinding;
import com.razie.pub.lightsoa.SoaResponse;

/** local junit tests for lightsoa assets - for full server tests, see the test in the http server */
public class TestLocalSoaAssets extends TestCase {

   public static AssetKey PLAYERKEY = new AssetKey("raz.test.Player", "1");
   public static AssetKey MOVIEKEY = new AssetKey("raz.test.Movie", "2");

   public void setUp() {
      NoStatics.resetJVM();
      JavaAssetMgr.init(new razie.assets.InventoryAssetMgr());
     JAS.manage(new SampleAsset("1")); 
     JAS.manage (new SampleAsset2("2")); 
//      JavaAssetMgr.init(new SampleAssetMgr());
      LightAuthBase.init(new LightAuthBase("lightsoa"));
   }

   public void testSampleServiceA() {
      AttrAccess aa = new AttrAccessImpl();
      aa.setAttr("movie", MOVIEKEY.toString());
      Object resp = new SoaBinding(SampleAsset.class, "").invoke(PLAYERKEY, "play", aa);
      assertTrue(MOVIEKEY.getId().equals(resp));
   }

   // test non-annotated asset class
   public void testSampleServiceANA() {
      AttrAccess aa = new AttrAccessImpl();
      aa.setAttr("movie", MOVIEKEY.toString());
      Object resp = new SoaBinding(SampleAsset2.class, "").invoke(MOVIEKEY, "play", aa);
      assertTrue(MOVIEKEY.getId().equals(resp));
   }

   public void testSampleService2A() {
      Object resp = new SoaBinding(SampleAsset.class, "").invoke(PLAYERKEY, "play", new AttrAccessImpl(
              "movie", MOVIEKEY.toString()));
      assertTrue(MOVIEKEY.getId().equals(resp));
   }

   public void testSampleService() {
      AttrAccess aa = new AttrAccessImpl();
      aa.setAttr("parm1", "a");
      aa.setAttr("parm2", "b");
      SoaResponse resp = (SoaResponse) new SoaBinding(SampleAsset.class, "").invoke(PLAYERKEY,
              "concatenate", aa);
      assertTrue("ab".equals(resp.getAttr("Result")));
   }

   public void testSampleService2() {
      SoaResponse resp = (SoaResponse) new SoaBinding(SampleAsset.class, "").invoke(PLAYERKEY,
              "concatenate", new AttrAccessImpl("parm1", "a", "parm2", "b"));
      assertTrue("ab".equals(resp.getAttr("Result")));
   }

   public void testSampleService3() {
      Properties aa = new Properties();
      aa.setProperty("parm1", "a");
      aa.setProperty("parm2", "b");
      SoaResponse resp = (SoaResponse) new SoaBinding(SampleAsset.class, "").invoke(PLAYERKEY,
              "concatenate", new AttrAccessImpl(aa));
      assertTrue("ab".equals(resp.getAttr("Result")));
   }

   public void testSampleService4() {
      Map<String, String> aa = new HashMap<String, String>();
      aa.put("parm1", "a");
      aa.put("parm2", "b");
      SoaResponse resp = (SoaResponse) new SoaBinding(SampleAsset.class, "").invoke(PLAYERKEY,
              "concatenate", new AttrAccessImpl(aa));
      assertTrue("ab".equals(resp.getAttr("Result")));
   }

   public void testStreamable() throws IOException {
      Map<String, String> aa = new HashMap<String, String>();
      aa.put("parm1", "a");
      aa.put("parm2", "b");
      DrawStream out = new SimpleDrawStream();
      new SoaBinding(SampleAsset.class, "").invokeStreamable(PLAYERKEY, "concatenateStream", out,
              new AttrAccessImpl(aa));
      assertTrue("ab".equals(out.toString()));
   }

   public void testVoids() {
      SoaResponse resp = (SoaResponse) new SoaBinding(SampleAsset.class, "").invoke(PLAYERKEY, "doNothing",
              new AttrAccessImpl("parm1", "a", "parm2", "b"));
   }
   static final Log logger = Log.factory.create(TestLocalSoaAssets.class.getName());
}
