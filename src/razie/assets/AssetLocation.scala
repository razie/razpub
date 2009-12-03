/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie.assets

import java.io.File;
import java.io.Serializable;
import java.net.URL;

import com.razie.pub.FileUtils;
//import com.razie.pub.comms.AgentHandle;
import com.razie.pub.comms.Agents;
import com.razie.pub.comms.Comms;

/**
 * the location of an asset, either a remote url like below or a directory.
 * 
 * a location is always on the form: protocol://host:portPATH
 * 
 * for mutant, the format is mutant://host:port::PATH
 * 
 * for others it's http://host:port/url
 * 
 * <p>
 * inspired from OSS/J's application environment, highly simplified (arguably...).
 * 
 * @author razvanc99
 */
class AssetLocation (o:String) {
   
   val (remoteUrl, localPath) = setURL(o)

   def getHost() = host // TODO inine
   def getPort() = port // TODO inine
   
   def host = hostport._1
   def port = hostport._2
  
   /** points to a mutant location */
   def isMutant = this.remoteUrl != null && this.remoteUrl.startsWith("mutant://")

   /** returns true if this NewAppEnv points to a local directory */
   def isLocal () = 
      if (localPath==null && remoteUrl==null)
         true
      else if (isMutant) {
         val h = this.host
         if (h.equals(Agents.getMyHostName()) || "local".equals(h)
               || Comms.isLocalhost(h)) true else false
      } else {
         this.localPath != null && this.localPath.length() > 0;
      }

   /** returns true if this points to a remote server */
   def isRemote () = 
      if (isMutant) {
         !isLocal;
      } else {
         this.remoteUrl != null && this.remoteUrl.length() > 0;
      }

   override def toString = 
      if (this.remoteUrl != null ) this.remoteUrl else this.localPath;

   /**
    * make an http URL, if remote. Normally the remote reference is in Weblogic's t3:// format. This
    * will convert it to http://
    */
   def toHttp () = 
      if (isMutant) {
         "http://" + this.host + ":" + this.port;
      } else
      this.remoteUrl;

   /** smart setting of the actual URL */
   private[this] def setURL(url:String ) : (String,String) = {
      var ru : String = null
      var lp : String = null
      
      if (url == null) {
         ru = null
      } else {
         // it's an URL. factory is then the default, I take it?
         if (url.indexOf("mutant:") >= 0) {
            // format: mutant://computer:port::localpath
            ru = url;
            lp = null;
         } else if (url.indexOf("http:") >= 0) {
            ru = url;
            lp = null;
         } else {
            lp = setLocalPath(url);
            // this is a local PATH, need to make sure it ends with a "/" - all other code will
            // simply concatenate file names to it
            if (lp != null && !"".equals(lp) && !lp.endsWith("/")
                  && !lp.endsWith("\\")) {
               lp += "/";
            }
         }

         // TODO not sure why i do this
         if (!isMutant && ru != null && ru.endsWith("/")) {
            ru = ru.substring(0, ru.length() - 1);
         }
      }
      (ru, lp)
   }

   def getLocalPath () : String = 
      if (this.remoteUrl != null) {
         if (this.remoteUrl.contains("::")) {
            val sp = this.remoteUrl.split("::");
            if (sp.length > 1 ) sp(1) else null
         }
         null;
      } else
         this.localPath;

   /** will get canonic path unless the path is in the classpath */
   private[this] def setLocalPath(lp:String ) : String = {
      if (lp != null) {
         if(lp.startsWith("jar:") ) lp else FileUtils.toCanonicalPath(lp);
      } else null
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   override def hashCode() : Int = {
//      final int PRIME = 31;
      // int result = super.hashCode();
      var result = 1;
      result = 31 * result + (if(localPath == null)  0 else localPath.hashCode());
      result = 31 * result + (if(remoteUrl == null)  0 else remoteUrl.hashCode());
      result;
   }

   /** turn into a URL. If looking for files local or classpath, use toUrl (fileName) */
   def toURL ={
      var url : URL = null;

      try {
         if (this.isLocal) {
            val f = new File(this.localPath);
            if (f.exists()) {
               url = f.getCanonicalFile().toURL();
            }
         } else {
            url = new URL(this.remoteUrl);
         }
      } catch {
         case e:Exception => throw new IllegalStateException("can't turn into URL, NewAppEnv=" + this.toString(), e);
      }
      url;
   }

   /**
    * toUrl when this NewAppEnv localpath contains the directory and the parameter is the actual
    * filename. For remote appoEnv, the fileName is ignored
    */
   def toURL(fileName:String) = {
      var url:URL = null;

      try {
         if (this.isLocal) {
            // treat classpath url's differently
            if (this.localPath.startsWith("jar:")) {
               url = new URL(this.localPath + fileName);
            } else {
               val f = new File(this.localPath + fileName);
               if (f.exists()) {
                  url = f.getCanonicalFile().toURL();
               }
            }
         } else {
            url = new URL(this.remoteUrl);
         }
      } catch {
         case e:Exception =>
         throw new IllegalStateException("can't turn into URL, NewAppEnv=" + this.toString() + " fileName="
               + fileName, e);
      }
      url;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   override def equals(obj:Any):Boolean = {
      if (obj == null)
         return false;
      val other = obj.asInstanceOf[AssetLocation]
      if (localPath == null) {
         if (other.localPath != null)
            return false;
      } else if (!localPath.equals(other.localPath))
         return false;
      if (remoteUrl == null) {
         if (other.remoteUrl != null)
            return false;
      } else if (!remoteUrl.equals(other.remoteUrl))
         return false;
      true;
   }

   private def hostport : (String,String) = {
      if (this.remoteUrl != null && this.remoteUrl.contains("://")) {
         val l = this.remoteUrl.split("://")(1);
         var ipport = l.split("/", 2)(0); // remove any uninteresting path in a URL
         ipport = ipport.split("::", 2)(0); // mutant://host:port::localpath
         
         // TODO is there always a port? I guess nobody would run at the default 8080...
         val colon = ipport.lastIndexOf(":");

         if (colon > 0) {
            val port = ipport.substring(colon + 1);
            val srcIp = ipport.substring(0, colon);
            (srcIp, port)
         }
         else 
            (Agents.getMyHostName(), Agents.me().port) // default to my port
      }
      else 
            (Agents.getMyHostName(), Agents.me().port) // default to my port

   }

   def protocol :String = 
      if (this.remoteUrl != null && this.remoteUrl.contains("://")) {
         this.remoteUrl.split("://")(0);
      } else
      null;

}

object AssetLocation {
   
   implicit def fs (s:String) : AssetLocation = new AssetLocation (s)

   def mutantEnv(host:String , dir:String ) = {
      var d = Agents.agent(host);
      if (d == null) {
         // try by ip
         d = Agents.agentByIp(host);
      }

      if (d == null) {
         throw new IllegalArgumentException("Unknown host/ip: " + host);
      }

      new AssetLocation("mutant://" + host + ":" + d.port + "::" + prepLocalDir(dir));
   }

   def mutantEnv(dir:String ) ={
      val me = Agents.agent(Agents.getMyHostName());
      // NOTE mutant URLs must contain hostname not IP !!!
      new AssetLocation("mutant://" + me.hostname + ":" + me.port + "::" + prepLocalDir(dir));
   }

   private def prepLocalDir(d:String ) = {
      val dir = if(d.startsWith("jar:") ) d else FileUtils.toCanonicalPath(d)

      // this is a local PATH, need to make sure it ends with a "/" - all other code will
      // simply concatenate file names to it
      if (dir != null && !"".equals(dir) && !dir.endsWith("/") && !dir.endsWith("\\")) {
         dir + "/";
      }
      else dir;
   }

   val LOCAL = new AssetLocation (null)
}