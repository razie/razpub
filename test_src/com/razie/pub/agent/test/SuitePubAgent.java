/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liabity assumed for this code.
 */
package com.razie.pub.agent.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * suite to run all agents tests
 * 
 * @author razvanc99
 */
public class SuitePubAgent extends TestSuite {
    public static Test suite() {
        TestSuite result = new TestSuite(SuitePubAgent.class.getName());

        result.addTestSuite(TestSimpleAgent.class);

        return result;
    }

}
