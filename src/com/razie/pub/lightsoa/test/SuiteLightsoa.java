/**
 * Razvan's code. 
 * Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.lightsoa.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * suite to run all pub.lightsoa.test tests
 * 
 * @author razvanc99
 */
public class SuiteLightsoa extends TestSuite {
    public static Test suite() {
        TestSuite result = new TestSuite(SuiteLightsoa.class.getName());

        result.addTestSuite(TestLocalSoa.class);
        result.addTestSuite(TestLocalSoaAssets.class);
        result.addTestSuite(TestLightServerSoa.class);
        result.addTestSuite(TestLightServerSoaAssets.class);

        return result;
    }

}
