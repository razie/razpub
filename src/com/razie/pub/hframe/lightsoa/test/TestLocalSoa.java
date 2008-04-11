package com.razie.pub.hframe.lightsoa.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import com.razie.pub.hframe.base.AttrAccess;
import com.razie.pub.hframe.base.log.Log;
import com.razie.pub.hframe.draw.DrawStream;
import com.razie.pub.hframe.draw.SimpleDrawStream;
import com.razie.pub.hframe.http.LightAuth;
import com.razie.pub.hframe.lightsoa.SoaBinding;
import com.razie.pub.hframe.lightsoa.SoaResponse;

/** local junit tests for lightsoa services - for full server tests, see the test in the http server */
public class TestLocalSoa extends TestCase {

    public void setUp() {
        // simple AA based on prefix - this also adds a prefix
        LightAuth.init(new LightAuth("lightsoa"));
    }

    public void testSampleService() {
        AttrAccess aa = new AttrAccess.Impl();
        aa.setAttr("parm1", "a");
        aa.setAttr("parm2", "b");
        SoaResponse resp = (SoaResponse) new SoaBinding(new SampleService(), "").invoke("concatenate", aa);
        assertTrue("ab".equals(resp.getAttr("Result")));
    }

    public void testSampleService2() {
        SoaResponse resp = (SoaResponse) new SoaBinding(new SampleService(), "").invoke("concatenate",
                new AttrAccess.Impl("parm1", "a", "parm2", "b"));
        assertTrue("ab".equals(resp.getAttr("Result")));
    }

    public void testSampleService3() {
        Properties aa = new Properties();
        aa.setProperty("parm1", "a");
        aa.setProperty("parm2", "b");
        SoaResponse resp = (SoaResponse) new SoaBinding(new SampleService(), "").invoke("concatenate",
                new AttrAccess.Impl(aa));
        assertTrue("ab".equals(resp.getAttr("Result")));
    }

    public void testSampleService4() {
        Map<String, String> aa = new HashMap<String, String>();
        aa.put("parm1", "a");
        aa.put("parm2", "b");
        SoaResponse resp = (SoaResponse) new SoaBinding(new SampleService(), "").invoke("concatenate",
                new AttrAccess.Impl(aa));
        assertTrue("ab".equals(resp.getAttr("Result")));
    }

    public void testStreamable() throws IOException {
        Map<String, String> aa = new HashMap<String, String>();
        aa.put("parm1", "a");
        aa.put("parm2", "b");
        DrawStream out = new SimpleDrawStream();
        new SoaBinding(new SampleService(), "").invokeStreamable("concatenateStream", out,
                new AttrAccess.Impl(aa));
        assertTrue("ab".equals(out.toString()));
    }

    public void testVoids() {
        SoaResponse resp = (SoaResponse) new SoaBinding(new SampleService(), "").invoke("doNothing",
                new AttrAccess.Impl("parm1", "a", "parm2", "b"));
    }

    static final Log logger = Log.Factory.create(TestLocalSoa.class.getName());
}
