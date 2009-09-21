/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.base.test;


import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * suite to run all pub tests
 * 
 * @author razvanc99
 */
public class SuitePubBase extends TestSuite {
    public static Test suite() {
        TestSuite result = new TestSuite(SuitePubBase.class.getName());

        result.addTestSuite(com.razie.pub.base.test.TestAttr.class);
        result.addTestSuite(com.razie.pub.base.test.TestJason.class);
        result.addTestSuite(com.razie.pub.base.test.TestLastLog.class);
        result.addTestSuite(com.razie.pub.base.test.TestNoStatic.class);
        
        return result;
    }

}