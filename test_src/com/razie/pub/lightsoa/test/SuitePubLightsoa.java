/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liabity assumed for this code.
 */
package com.razie.pub.lightsoa.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * suite to run all pub.lightsoa.test tests
 * 
 * @author razvanc99
 */
public class SuitePubLightsoa extends TestSuite {
    public static Test suite() {
        TestSuite result = new TestSuite(SuitePubLightsoa.class.getName());

        result.addTestSuite(TestLocalSoa.class);
        result.addTestSuite(TestLocalSoaAssets.class);
        result.addTestSuite(TestLightServerSoa.class);
        result.addTestSuite(TestLightServerSoaAssets.class);

        return result;
    }

}
