/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package razie.assets.test

import org.scalatest.junit._
import com.razie.pub.base.data._
import org.scalatest.SuperSuite
import razie.assets._
import razie.assets.AssetKey

/**
 * testing the assets
 * 
 * @author razvanc99
 */
class TestAssetKey extends JUnit3Suite {
   val k1 = new AssetKey ("Meta", "id")
//   val k2 = new AssetKey ("Meta", "id", new AssetLocation ("c:\\Video"))
   val k2 = new AssetKey ("Meta", "id", "c:\\Video")
   val k3 = new AssetKey ("Meta", "id", new AssetLocation ("http://gigi.com"))
   val k4 = new AssetKey ("Meta", "id", new AssetLocation ("http://gigi.com::c:\\Video"))
   val k5 = new AssetCtxKey ("Meta", "id", new AssetLocation ("http://gigi.com::c:\\Video"), new AssetContext(razie.AA("role.series=razie.uri://Meta:id")))
   
   def s1 = "razie.uri:Meta:id"
   def s2 = "razie.uri:Meta:id@c:\\Video"
   def ss2 = "razie.uri:Meta:id@c:/Video/"
   def s3 = "razie.uri:Meta:id@http://gigi.com"
   def s4 = "razie.uri:Meta:id@http://gigi.com::c:\\Video"

   def testk1() = expect (k1) { AssetKey.fromString(s1) }
   def testk2() = expect (k2) { AssetKey.fromString(s2) }
   def testk3() = expect (k3) { AssetKey.fromString(s3) }
   def testk4() = expect (k4) { AssetKey.fromString(s4) }

   def testk1s() = expect (s1) { k1.toString }
   def testk2s() = expect (ss2) { k2.toString }
   def testk3s() = expect (s3) { k3.toString }
   def testk4s() = expect (s4) { k4.toString }

}
