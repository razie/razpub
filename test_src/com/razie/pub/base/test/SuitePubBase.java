/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
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

      result.addTestSuite(com.razie.pub.base.test.AttrAccessTest.class);
      result.addTestSuite(com.razie.pub.base.test.ScalaAttrAccessTest.class);
      result.addTestSuite(com.razie.pub.base.test.JasonTest.class);
      result.addTestSuite(com.razie.pub.base.test.LastLogTest.class);
      result.addTestSuite(com.razie.pub.base.test.TestNoStatic.class);
      result.addTestSuite(com.razie.pub.base.test.ScriptingTest.class);
      
      return result;
   }
}
