/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.lightsoa.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import com.razie.pub.assets.AssetKey;
import com.razie.pub.assets.AssetMgr;
import com.razie.pub.base.AttrAccess;
import com.razie.pub.base.log.Log;
import com.razie.pub.comms.LightAuth;
import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.SimpleDrawStream;
import com.razie.pub.lightsoa.SoaBinding;
import com.razie.pub.lightsoa.SoaResponse;

/** local junit tests for lightsoa assets - for full server tests, see the test in the http server */
public class TestLocalSoaAssets extends TestCase {
    public static AssetKey PLAYERKEY = new AssetKey("raz.test.Player", "winfile");
    public static AssetKey MOVIEKEY  = new AssetKey("raz.test.Movie", "300.avi");

    public void setUp() {
        AssetMgr.init(new SampleAssetMgr());
        LightAuth.init(new LightAuth("lightsoa"));
    }

    public void testSampleServiceA() {
        AttrAccess aa = new AttrAccess.Impl();
        aa.setAttr("movie", MOVIEKEY.toString());
        Object resp = new SoaBinding(SampleAsset.class, "").invoke(PLAYERKEY, "play", aa);
        assertTrue(MOVIEKEY.getId().equals(resp));
    }

    // test non-annotated asset class
    public void testSampleServiceANA() {
        AttrAccess aa = new AttrAccess.Impl();
        aa.setAttr("movie", MOVIEKEY.toString());
        Object resp = new SoaBinding(SampleAsset.class, "").invoke(new AssetKey("bibi", "cu"), "play", aa);
        assertTrue(MOVIEKEY.getId().equals(resp));
    }

    public void testSampleService2A() {
        Object resp = new SoaBinding(SampleAsset.class, "").invoke(PLAYERKEY, "play", new AttrAccess.Impl(
                "movie", MOVIEKEY.toString()));
        assertTrue(MOVIEKEY.getId().equals(resp));
    }

    public void testSampleService() {
        AttrAccess aa = new AttrAccess.Impl();
        aa.setAttr("parm1", "a");
        aa.setAttr("parm2", "b");
        SoaResponse resp = (SoaResponse) new SoaBinding(SampleAsset.class, "").invoke(PLAYERKEY,
                "concatenate", aa);
        assertTrue("ab".equals(resp.getAttr("Result")));
    }

    public void testSampleService2() {
        SoaResponse resp = (SoaResponse) new SoaBinding(SampleAsset.class, "").invoke(PLAYERKEY,
                "concatenate", new AttrAccess.Impl("parm1", "a", "parm2", "b"));
        assertTrue("ab".equals(resp.getAttr("Result")));
    }

    public void testSampleService3() {
        Properties aa = new Properties();
        aa.setProperty("parm1", "a");
        aa.setProperty("parm2", "b");
        SoaResponse resp = (SoaResponse) new SoaBinding(SampleAsset.class, "").invoke(PLAYERKEY,
                "concatenate", new AttrAccess.Impl(aa));
        assertTrue("ab".equals(resp.getAttr("Result")));
    }

    public void testSampleService4() {
        Map<String, String> aa = new HashMap<String, String>();
        aa.put("parm1", "a");
        aa.put("parm2", "b");
        SoaResponse resp = (SoaResponse) new SoaBinding(SampleAsset.class, "").invoke(PLAYERKEY,
                "concatenate", new AttrAccess.Impl(aa));
        assertTrue("ab".equals(resp.getAttr("Result")));
    }

    public void testStreamable() throws IOException {
        Map<String, String> aa = new HashMap<String, String>();
        aa.put("parm1", "a");
        aa.put("parm2", "b");
        DrawStream out = new SimpleDrawStream();
        new SoaBinding(SampleAsset.class, "").invokeStreamable(PLAYERKEY, "concatenateStream", out,
                new AttrAccess.Impl(aa));
        assertTrue("ab".equals(out.toString()));
    }

    public void testVoids() {
        SoaResponse resp = (SoaResponse) new SoaBinding(SampleAsset.class, "").invoke(PLAYERKEY, "doNothing",
                new AttrAccess.Impl("parm1", "a", "parm2", "b"));
    }

    static final Log logger = Log.Factory.create(TestLocalSoaAssets.class.getName());
}
