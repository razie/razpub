/**
 * Razvan's code. 
 * Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.http.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * suite to run all pub.hframe tests
 * 
 * @author razvanc99
 */
public class SuiteHttp extends TestSuite {
    public static Test suite() {
        TestSuite result = new TestSuite(SuiteHttp.class.getName());

        result.addTestSuite(TestLightServer.class);

        return result;
    }

}
