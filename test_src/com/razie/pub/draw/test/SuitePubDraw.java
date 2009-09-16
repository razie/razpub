/**
 * Razvan's code. 
 * Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.draw.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * suite to run all pub.hframe tests
 * 
 * @author razvanc99
 */
public class SuitePubDraw extends TestSuite {
    public static Test suite() {
        TestSuite result = new TestSuite(SuitePubDraw.class.getName());

        result.addTestSuite(TestDrawables.class);
        result.addTestSuite(TestContainers.class);
        result.addTestSuite(TestStreams.class);

        return result;
    }

}
