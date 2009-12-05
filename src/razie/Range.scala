/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie

/** there, I fixed it! http://thereifixedit.com/ */
object Range {
   def excl (min:Int, max:Int) = new Range (min,max-1)
   def incl (min:Int, max:Int) = new Range (min,max)
}

/** just for fun - before 2.8 Range was not strict - well, this one is! */
class Range (min:Int, val max:Int) extends Iterator[Int] {
   var curr = min
   def hasNext:Boolean =  curr <= max
   def next:Int = { val oldcurr=curr; curr.+=(1); oldcurr}
}
