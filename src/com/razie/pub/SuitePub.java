/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.razie.pub.draw.test.SuiteDraw;
import com.razie.pub.http.test.SuiteHttp;
import com.razie.pub.lightsoa.test.SuiteLightsoa;

/**
 * suite to run all pub tests
 * 
 * @author razvanc99
 */
public class SuitePub extends TestSuite {
    public static Test suite() {
        TestSuite result = new TestSuite(SuitePub.class.getName());

        result.addTest(SuiteDraw.suite());
        result.addTest(SuiteHttp.suite());
        result.addTest(SuiteLightsoa.suite());


        return result;
    }

}
