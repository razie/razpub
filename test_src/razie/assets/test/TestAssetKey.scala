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
import razie.assets.AssetLocation._

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
   val k5 = new AssetCtxKey ("Meta", "id", new AssetLocation ("http://gigi.com::c:\\Video"), new AssetContext(razie.AA("someattr=somevalue")))
   val k6 = new AssetCtxKey ("Meta", "id", new AssetLocation ("http://gigi.com::c:\\Video"), new AssetContext())
   k6.ctx.role("k4", k4)
   
   def s1 = "razie.uri:Meta:id"
   def s2 = "razie.uri:Meta:id@c:\\Video"
   def ss2 = "razie.uri:Meta:id@c:/Video/"
   def s3 = "razie.uri:Meta:id@http://gigi.com"
   def s4 = "razie.uri:Meta:id@http://gigi.com::c:\\Video"
      
   def s5 = "razie.uri:Meta:id@http://gigi.com::c:\\Video@@someattr=somevalue&ctx.name="

   def testk1() = expect (k1) { AssetKey.fromString(s1) }
   def testk2() = expect (k2) { AssetKey.fromString(s2) }
   def testk3() = expect (k3) { AssetKey.fromString(s3) }
   def testk4() = expect (k4) { AssetKey.fromString(s4) }

   def testk1s() = expect (s1) { k1.toString }
   def testk2s() = expect (ss2) { k2.toString }
   def testk3s() = expect (s3) { k3.toString }
   def testk4s() = expect (s4) { k4.toString }
   
   def testk4l() = expect ("c:\\Video/") { k4.loc.localPath  }
   def testk4ll() = expect ("http://gigi.com:8080") { k4.loc.toHttp } // this is really a mutant url so...

   // TODO 3-1 complete the asset context key implementation and test
   def testk5s() = expect (s5) { k5.toString } 
   def testk5() = expect (s5) { AssetKey.fromString(s5).toString }
   
   def testk6() = expect(k4){  k6.asInstanceOf[AssetCtxKey].ctx.role("k4") } 
}
