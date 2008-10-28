/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.base.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * suite to run all base
 * 
 * @author razvanc99
 */
public class SuiteBase extends TestSuite {
    public static Test suite() {
        TestSuite result = new TestSuite(SuiteBase.class.getName());

        result.addTestSuite(TestAttr.class);
        result.addTestSuite(TestJason.class);
        result.addTestSuite(TestLastLog.class);
        result.addTestSuite(TestNoStatics.class);

        return result;
    }

}
