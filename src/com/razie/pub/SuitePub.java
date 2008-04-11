/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.razie.pub.hframe.SuiteHframe;
import com.razie.pub.hframe.http.test.TestLightServer;
import com.razie.pub.hframe.lightsoa.test.TestLightServerSoaAssets;
import com.razie.pub.hframe.lightsoa.test.TestLocalSoa;
import com.razie.pub.hframe.lightsoa.test.TestLocalSoaAssets;

/**
 * suite to run all pub tests
 * 
 * @author razvanc99
 */
public class SuitePub extends TestSuite {
    public static Test suite() {
        TestSuite result = new TestSuite(SuitePub.class.getName());

        result.addTest(SuiteHframe.suite());

        result.addTestSuite(TestLightServer.class);

        result.addTestSuite(TestLocalSoa.class);
        result.addTestSuite(TestLocalSoaAssets.class);
        result.addTestSuite(TestLightServerSoaAssets.class);

        return result;
    }

}
