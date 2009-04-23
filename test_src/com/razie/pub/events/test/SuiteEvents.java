/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.events.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * suite to run all pub.hframe tests
 * 
 * @author razvanc99
 */
public class SuiteEvents extends TestSuite {
    public static Test suite() {
        TestSuite result = new TestSuite(SuiteEvents.class.getName());

        result.addTestSuite(TestEvents.class);

        return result;
    }

}
