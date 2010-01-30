package razie.naked

class EntityKey (val pk:String) {
   override def toString = pk
}

object EntityKey {
   def fromString (s:String) = new EntityKey (s)
}