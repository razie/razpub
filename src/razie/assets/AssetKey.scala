/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie.assets

import com.razie.pub.base.AttrAccess
import scala.collection._

/**
 * Each entity/asset has a unique key, which identifies the asset's type, id and location. Borrowed
 * from OSS/J's ManagedEntityKey (type, key, location), this is lighter and designed to pass through
 * URLs and be easily managed as a string form.
 * 
 * <p>
 * asset-URI has this format: <code>"razie.uri:entityType:entityKey@location"</code>
 * <p>
 * asset-context URI has this format: <code>"razie.puri:entityType:entityKey@location&context"</code>
 * 
 * <ul>
 * <li>type is the type of entity, should be unique among all other types. HINT: do not keep
 * defining "Movie" etc - always assume someone else did...use "AlBundy.Movie" for instance :D
 * <li>key is the unique key of the given entity, unique in this location and for this type. The key
 * could be anything that doesn't have an '@' un-escaped. it could contain ':' itself like an
 * XCAP/XPATH etc, which is rather cool...?
 * <li>location identifies the location of the entity: either URL or folder or a combination. 
 * A folder implies it's on the localhost. An URL implies it's in that specific server.
 * </ul>
 * 
 * <p>
 * Keys must have at least type. By convention, if the key is missing, the key refers to all
 * entities of the given type.
 * 
 * @author razvanc99
 */
class AssetKey (_meta:String, _id:String, _loc:AssetLocation) {
// TODO optimize - don't need the internal _xxx vars above...   
   val meta:String = _meta
   val id:String = if (_id == null) AssetKey.uid() else _id
   val loc:AssetLocation = if(_loc == null) AssetLocation.LOCAL else _loc
   // TODO optimize: constant for empty AssetLocation?
   
   def this (_meta:String, _id:String) = this (_meta, _id, null)
   def this (_meta:String) = this (_meta, AssetKey.uid, null)

   // these have to be functions rather than vars for existing java code...
   def getMeta() = meta
   def getId() = id
   def getLoc() = loc
   
   // TODO 1-1 inline
   def getType() = meta
   def getLocation() = loc

    override def equals(o:Any):Boolean = o match {
       case r : AssetKey =>  meta.equals(r.meta) && id.equals(r.id)
       case _ => false
    }

    override def hashCode() : Int = meta.hashCode() + (if(id != null ) id.hashCode() else 0)

    /** short descriptive string */
    override def toString = 
       AssetKey.PREFIX+":" + meta + ":" + (if(id == null ) "" else java.net.URLEncoder.encode(id, "UTF-8")) + (if (loc == null || AssetLocation.LOCAL.equals(loc)) "" else ("@" + loc.toString()))
//       AssetKey.PREFIX+":" + meta + ":" + (if(id == null ) "" else HttpUtils.toUrlEncodedString(id)) + (if (loc == null) "" else ("@" + loc.toString()))

    /**
     * Use this method to get a string that is safe to use in a URL. Note that whenever the string
     * is encoded when you want to use it it must be decoded with fromUrlEncodedString(String).
     */
    def toUrlEncodedString : String = //HttpUtils.toUrlEncodedString(this.toString());
            java.net.URLEncoder.encode(toString, "UTF-8")

}

object AssetKey {
   def PREFIX = "razie.uri"
   def PREFIXP = "razie.puri"
   
    /**
     * just a simple UID implementation, to fake IDs for objects that don't have them.
     */
    def uid() =  "Uid-" + {seqNum+=1; seqNum} + "-" + String.valueOf(System.currentTimeMillis());

    /** to allocate next UID...this should be done better */
    private var seqNum : Int = 1;

    /**
     * make up from an entity-URI. see class javadocs for details on URI
     * 
     * TODO it's not efficient - creates too many objects to parse the string
     * 
     * @return the entity-URI
     */
    def fromString(inurl:String) :AssetKey = {
        var url = inurl;

        var news:String="";
        if (url.startsWith(PREFIX)) {
            news = url.replace(PREFIX+":", "");
        } else {
            val map1 = url.split("://", 2);

            // with the following, i support also a missing PREFIX, i.e. a simplified KEY with just
            // type:key@loc
            news = (if(map1.length > 1 ) map1(1) else (if (map1.length == 1 ) map1(0) else null))
        }

        if (news != null) {
            // i have a class nm
            val map2 = news.split(":", 2);
            if (map2.length > 1 && map2(1) != null) {
                // i have a key/id
                val map3 = map2(1).split("@", 2);
                if (map3.length > 1) {
                   // i have an appEnv
                   if (map3(1).contains ("?")) {
                      // i have a context
                      val map4 = map3(1).split("?", 2);
                       return new AssetCtxKey(map2(0), decode(map3(0)), new AssetLocation(
                               map4(0)), new AssetContext (razie.AA(map4(1))));
                   } else 
                    return new AssetKey(map2(0), decode(map3(0)), new AssetLocation(
                            map3(1)));
                } else {
                    // no appEnv
                    return new AssetKey(map2(0), decode(map3(0)), null);
                }
            } else {
                // no key/id
                return new AssetKey(map2(0), null, null);
            }
        }
        return null;
    }

    val ROLE = "role."
       
    def ctx (s:String) = {
       val ac = new AssetContext (razie.AA(s))
       for (x <- razie.RJS apply ac.attrs.getPopulatedAttr)
          if (x.startsWith(ROLE))
             ac.env.put(x.replaceFirst(ROLE, ""), fromString(ac.attrs.sa(x)))
//       ac.attrs.foreach ((x,y) => if (x.startsWith(ROLE)) ac.env.put(x.replaceFirst(ROLE, ""), fromString(y)))
    }
    
   def decode (s:String) = java.net.URLDecoder.decode(s, "UTF-8")
            
    implicit def toac (a : AttrAccess) : AssetContext = new AssetContext (a)
}

/**TODO blurb about contexts */
class AssetContext (val attrs : AttrAccess) {
//   val assocs : List[AssetAssoc]   
   val env : mutable.Map[String, AssetKey] = new mutable.HashMap[String,AssetKey]()   // (role, who)
   
   def this () = this (null) // maybe EMPTY?
   
   def sa (name:String) : String = attrs.sa(name)
   def role(name:String) : AssetKey = env(name)
   def role(name:String, who:AssetKey) : AssetContext = {env.put(name, who); this}

   override def toString : String = 
      (for (k <- env.keySet) yield "role."+k+env(k).toString).mkString("&")+attrs.toString
}

/**TODO blurb about contexts */
class AssetCtxKey (_meta:String, _id:String, _loc:AssetLocation, val ctx:AssetContext) extends AssetKey (_meta, _id, _loc) {
    /** short descriptive string */
    override def toString = 
       AssetKey.PREFIXP+":" + meta + ":" + (if(id == null ) "" else java.net.URLEncoder.encode(id, "UTF-8")) + (if (loc == null) "" else ("@" + loc.toString())) + "&"
}
