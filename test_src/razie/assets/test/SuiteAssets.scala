package razie.assets.test

import org.scalatest.junit._
import org.scalatest.SuperSuite

/** TODO testing the RazElement */
//class SuiteAssets extends SuperSuite (
//  List (
//    new razie.assets.test.TestAssetKey,
//    new razie.assets.test.TestAssetHandle
//  )
//)

/** TODO this is sooooooooooooo messed up... */
class SuiteAssets () extends junit.framework.TestSuite(classOf[XNada]) {
  
  // this is where you list the tests...
   addTestSuite(classOf[razie.assets.test.TestAssetKey])
   addTestSuite(classOf[razie.assets.test.TestAssetHandle])
//   addTestSuite(classOf[razie.assets.test.TestMetas])
   addTestSuite(classOf[razie.assets.test.TestXpAssets])
   
   def test1() = 
     // don't touch this line
     addTest(new junit.framework.TestSuite(classOf[razie.assets.test.TestAssetKey]))
     
}

class XNada extends junit.framework.TestCase {
 def testNada : Unit =  {}
}