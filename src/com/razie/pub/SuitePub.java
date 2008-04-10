/**
 * Razvan's code. 
 * Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.razie.pub.hframe.SuiteHframe;
import com.razie.pub.hframe.http.test.TestLightServer;
import com.razie.pub.hframe.http.test.TestLightServerAssets;
import com.razie.pub.hframe.lightsoa.test.TestLightSoa;
import com.razie.pub.hframe.lightsoa.test.TestLightSoaAssets;

/**
 * suite to run all pub tests
 * 
 * @author razvanc99
 */
public class SuitePub extends TestSuite {
    public static Test suite() {
        TestSuite result = new TestSuite(SuitePub.class.getName());

        result.addTest(SuiteHframe.suite());
        
        result.addTestSuite(TestLightSoa.class);
        result.addTestSuite(TestLightSoaAssets.class);

        result.addTestSuite(TestLightServer.class);
        result.addTestSuite(TestLightServerAssets.class);

        return result;
    }

}
