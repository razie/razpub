
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

class EmbedOne {
   case class EmbeddedOne ()
  val v4 = new EmbeddedOne() 
}

object ReallyWorks {
  val v1 = new EmbedOne()
//  val v2 = new EmbedOne.EmbeddedOne() 
//  val v3 = new EmbeddedOne() 
}

trait Monad[C[_]] {
   def pure[A] (a:A) : C[A]
   def bind[A,B] (a:C[A]) (f : A => C[B]) : C[B]
}

trait D { def draw():Unit = { println("1")} }
trait A extends D { override def draw () = { println("A"); super.draw()}}
trait B extends D { override def draw () = { println("B"); super.draw()}}
case class C extends D with A with B { override def draw () = { println("C"); super.draw()}}

object LearnScalaLinear extends Application {
   C().draw()
}

trait BaseF {
//   def brief:Any = "1"
}

abstract class FinalMe extends BaseF with Drawable {
   def brief:Any 
//   def brief:Any = "1"
}

trait Drawable {
   def brief:Any
   def draw() = DrawStuff_.draw(this)
}

object DrawStuff_ {
   def draw (s:{def brief:Any}) = println (s)
}
