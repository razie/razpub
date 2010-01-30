// exercise from Tony Morris: http://blog.tmorris.net/debut-with-a-catamorphism/

trait MyOption[+A] {
  // single abstract method
  def cata[X](some: A => X, none: => X): X

  import MyOption._
  
  def map[B](f: A => B): MyOption[B] = cata (f andThen some/*x=>MyOption.some(f(x))*/, MyOption.none)
 
  def flatMap[B](f: A => MyOption[B]): MyOption[B] = error("todo")
 
  def getOrElse[AA >: A](e: => AA): AA = cata (x=>x, e)
 
  def filter(p: A => Boolean): MyOption[A] = cata (x=>if (p(x))MyOption.some(x) else MyOption.none, MyOption.none)
 
  def foreach(f: A => Unit): Unit = cata (x=>f(x), null)
 
  def isDefined: Boolean = cata(x=>true,false)
 
  def isEmpty: Boolean = cata (x=>false,true)
 
  // WARNING: not defined for None
  def get: A = cata(x=>x, throw new IllegalStateException())
 
  def orElse[AA >: A](o: MyOption[AA]): MyOption[AA] = cata (x=>MyOption.some(x),o)
 
  def toLeft[X](right: => X): Either[A, X] = error("todo")
 
  def toRight[X](left: => X): Either[X, A] = error("todo")
 
  def toList: List[A] = error("todo")
 
  def iterator: Iterator[A] = error("todo")
}
 
object MyOption {
  def none[A] = new MyOption[A] {
    def cata[X](s: A => X, n: => X) = n
  }
 
  def some[A](a: A) = new MyOption[A] {
    def cata[X](s: A => X, n: => X) = s(a)
  }
}