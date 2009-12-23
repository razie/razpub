/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */

import org.scalatest.junit._
import org.scalatest.SuperSuite

/**
 * testing the assets
 * 
 * @author razvanc99
 */
class TestScalaLessons extends JUnit3Suite {

   def testNullMatch = expect ("-null") {
      null match {
         case null => "-null"
         case o:AnyRef => "-anyref"
         case _ => "-default"
      }
   }
}
