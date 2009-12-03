
// section 6.3 from scala book
class Rational63(n: Int, d: Int) {
  override def toString = n +"/"+ d
}

// section 6.6 from scala book
class Rational66(n: Int, d: Int) {
  require(d != 0)
  val numer: Int = n
  val denom: Int = d
  override def toString = numer +"/"+ denom
}

object LearnScala1 {
   def main (argv:Array[String]) {
      val b1=new Rational63(3,4)
      val b2=new Rational66(3,4)

      println
      println (b1, b2)
//      println "puzzly"
      Console println "puzzly"
      println ("puzzly")
      println ("puzzly", "puzzle")
      
      val s = "a" + "b"
      println (s)
   }
}