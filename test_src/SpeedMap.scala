
import scala.collection._

object SpeedMap {
  def main(args : Array[String]) : Unit = {
     val T = 100000
    
     for (laps <- 0 to 1800) {
     val im = immutable.HashMap[Int,String](1 -> "1", 2->"2", 0->"0")
     val mm =   mutable.HashMap[Int,String](1 -> "1", 2->"2", 0->"0")
     val jm =   new java.util.HashMap[Int,String]()
     jm.put(1, "1")
     jm.put(2, "r")
     jm.put(3, "3")
     
     val t1 = System.currentTimeMillis()
     for (i <- 0 to T) im.get(i % 3)
     val t2 = System.currentTimeMillis()
     for (i <- 0 to T) mm.get(i % 3)
     val t3 = System.currentTimeMillis()
     for (i <- 0 to T) jm.get(i % 3)
     val t4 = System.currentTimeMillis()
     
     println (t2-t1, t3-t2, t4-t3)
     Thread.sleep(100)
     }
  }
}
