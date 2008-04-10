/**
 * Razvan's code. 
 * Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.razie.pub.hframe.draw.test.TestContainers;
import com.razie.pub.hframe.draw.test.TestDrawables;
import com.razie.pub.hframe.draw.test.TestRendering;
import com.razie.pub.hframe.draw.test.TestStreams;

/**
 * suite to run all pub.hframe tests
 * 
 * @author razvanc99
 */
public class SuiteHframe extends TestSuite {
    public static Test suite() {
        TestSuite result = new TestSuite(SuiteHframe.class.getName());

        result.addTestSuite(TestDrawables.class);
        result.addTestSuite(TestRendering.class);
        result.addTestSuite(TestContainers.class);
        result.addTestSuite(TestStreams.class);

        return result;
    }

}
