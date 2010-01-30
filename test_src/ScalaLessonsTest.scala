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
class ScalaLessonsTest extends JUnit3Suite {

   def testNullMatch = expect ("-null") {
      null match {
         case o:AnyRef => "-anyref"
         case null => "-null"
         case _ => "-default"
      }
   }
   
   def testSomeFlatMap = expect (None) {
      Map("Tokyo" -> "Japan").get ("Toronto")
   }
   
}

class MyF1 { def name: String = "1" }
class MyF2 { def getName: String = "2" }
class MyF12 { 
   def name: String = "1" 
  def getName: String = "2" }

object MyF12 extends Application {
   test (new MyF1)
//   test (new MyF2)
//   test (new MyF12)
   
   def test (f: {def name:String} ) = println (f.name)
//   def test (f: {def getName:String} ) = println (f.getName)
}
