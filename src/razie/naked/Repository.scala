package razie.naked

trait Repository {
   def get : Entity
//   def list : List[Entity]
   def query : List[Entity]
   def update : Entity
   def remove : Entity
}
