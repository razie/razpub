package com.razie.pub.hframe.http.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.razie.pub.hframe.assets.AssetActionToInvoke;
import com.razie.pub.hframe.assets.AssetHandle;
import com.razie.pub.hframe.base.ActionItem;
import com.razie.pub.hframe.base.ActionToInvoke;
import com.razie.pub.hframe.base.log.Log;
import com.razie.pub.hframe.lightsoa.HttpAssetSoaBinding;
import com.razie.pub.hframe.lightsoa.test.SampleAsset;
import com.razie.pub.hframe.lightsoa.test.SampleAssetMgr;
import com.razie.pub.hframe.lightsoa.test.TestLightSoaAssets;

/**
 * test the light server
 * 
 * $
 * @author razvanc99
 * 
 */
public class TestLightServerAssets extends TestLightBase {
    static String PK = TestLightSoaAssets.PLAYERKEY.toUrlEncodedString();

    public void setUp() {
        super.setUp();
        SampleAssetMgr.init();

        HttpAssetSoaBinding.register(SampleAsset.class);
    }

    /**
     * test the SOA simple echo via the proper URL reader (check implementation as a proper http
     * server)
     */
    public void testSoaEchoUrl() throws IOException, InterruptedException {
        // start server with echo impl
        HttpAssetSoaBinding soa = new HttpAssetSoaBinding();
        cmdGET.registerSoa(soa);

        // send echo command
        URL url = new URL("http://localhost:" + PORT + "/lightsoa/asset/" + PK + "/play?movie="
                + TestLightSoaAssets.MOVIEKEY.toUrlEncodedString());
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String result = in.readLine();
        in.close();

        cmdGET.removeSoa(soa);
        assertTrue(result.contains(TestLightSoaAssets.MOVIEKEY.getId()));
    }

    /**
     */
    public void testSoaEchoAction() throws IOException, InterruptedException {
        // start server with echo impl
        HttpAssetSoaBinding soa = new HttpAssetSoaBinding();
        cmdGET.registerSoa(soa);

        // send echo command
        ActionToInvoke action = new AssetActionToInvoke("http://localhost:" + PORT,
                TestLightSoaAssets.PLAYERKEY, new ActionItem("play"), "movie", TestLightSoaAssets.MOVIEKEY);
        String result = (String) action.exec(null);

        cmdGET.removeSoa(soa);
        assertTrue(result.contains(TestLightSoaAssets.MOVIEKEY.getId()));
    }

    /**
     */
    public void testSoaEchoHandle() throws IOException, InterruptedException {
        // start server with echo impl
        HttpAssetSoaBinding soa = new HttpAssetSoaBinding();
        cmdGET.registerSoa(soa);

        // send echo command
        AssetHandle handle = new AssetHandle(TestLightSoaAssets.PLAYERKEY);
        String result = (String) handle.invoke("play", "movie", TestLightSoaAssets.MOVIEKEY.toString());

        cmdGET.removeSoa(soa);
        assertTrue(result.contains(TestLightSoaAssets.MOVIEKEY.getId()));
    }

    static final Log logger = Log.Factory.create(TestLightServerAssets.class.getName());
}
