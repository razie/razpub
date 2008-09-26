/**
 * Razvan's code. 
 * Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.agent.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * suite to run all agents tests
 * 
 * @author razvanc99
 */
public class SuiteAgents extends TestSuite {
    public static Test suite() {
        TestSuite result = new TestSuite(SuiteAgents.class.getName());

        result.addTestSuite(TestSimpleAgent.class);

        return result;
    }

}
