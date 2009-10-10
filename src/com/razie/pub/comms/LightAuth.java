/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.comms;

import java.net.Socket;

import com.razie.pub.base.AttrAccess;

/**
 * auth providers have, for me, several responsibilities - see the static methods below
 * 
 * this default will do nothing
 * 
 * NOTE that authentication providers are supposed to understand different remote targets and
 * properly massage the urls accordingly. for instance, one remote could be a lightsoa server while
 * another could be a bank that needs a session token appended etc. That mechanism is implemented in
 * the razagents framework, not here. Feel free to write your own...
 * 
 * <p>
 * This simple implementation uses a prefix which precedes all URLs. This is used so that, if you
 * open a port in the firewall, I hope it would confuse any would-be attacker which just scanned
 * your port, since the mutant wouldn't say a word without the prefix.
 * 
 * TODO AA should be custom per endpoint (same agent can use other agents with different AA
 * capabilities). Also, the AA can be negociated with the endpoints. Not sure how this will hookup
 * with the comms channel framework.
 * 
 * TODO make nostaticsafe
 * 
 * @author razvanc99
 */
public class LightAuth {
   /**
    * permissions that can be assigned to objects, actions and methods. These are decoupled from the
    * authentications below.
    */
   public static enum PermType {
      /** highest permission: includes upgrades and code changes, define new assets */
      ADMIN,
      /** allows control of play/preferences, turn on lights etc */
      CONTROL,
      /** since we have VIEW/READ, let's have write as well, eh? */
      WRITE,
      /** just query and view */
      VIEW,
      /** what you want anybody to be able to do */
      PUBLIC
   }

   /** auth types */
   public static enum AuthType {
      /** only in-house...i.e. from the same subnetwork */
      INHOUSE,
      /**
       * shared same secret anywhere. this is basically you (or your delegate) from anywhere in the
       * world
       */
      SHAREDSECRET,
      /** in-cloud - members of the same cloud */
      INCLOUD,
      /** friends - their public ID is listed in your friends list */
      FRIEND,
      /** anybody */
      ANYBODY,
   }

   protected static LightAuth impl;
   protected String           prefix = "";
   private static boolean           locked = false;

   public LightAuth(String prefix) {
      // be nice
      if (prefix.startsWith("/"))
         this.prefix = prefix.substring(1);
      else
         this.prefix = prefix;
   }

   /** @deprecated use instance() */
   protected static LightAuth singleton() {
      if (impl == null)
         impl = new LightAuth("");
      return impl;
   }

   public static LightAuth instance () {
      if (impl == null)
         impl = new LightAuth("");
      return impl;
   }

   /** initialize the (for now static) AA used in this server/client */
   public static void init(LightAuth impl) {
      if (!locked)
         LightAuth.impl = impl;
   }

   /** lock it - can't reset after this */
   public static void lock() {
      locked=true;
   }

   /**
    * used by client to prepare the URL just before using it.
    * 
    * in basic HTTP communication, the only way to AA is by messing with the URL - add/remove, add
    * tokens parameters etc.
    * 
    * TODO We could mess with the header parms...but...ok...in the future :)
    * 
    * @parm url the url to prepare, full form possibly including server:port
    * @return the URL to use, with AA info inserted
    */
   public static String wrapUrl(String url) {
      return singleton().wrapUrlImpl(url);
   }

   public AttrAccess httpSecParms(java.net.URL url) {
      return null;
   }

   /**
    * used by server to the URL just before using it.
    * <p>
    * in basic HTTP communication, the only way to AA is by messing with the URL - add/remove, add
    * tokens parameters etc
    * <p>
    * THIS is tricky. If there is any AA information to save in a "session" on the server to be used
    * later when processing the URL, you do that any way you see fit. The only thing I will help
    * with right now is to make sure that the request is processed on the same thread that made this
    * call. Go think!
    * <p>
    * NOTE you don't have to authorize right now, since auth is supposed to be done per
    * service/asset/resource type. Just save whatever you need (token) in a session for this
    * request.
    * <p>
    * If you simply don't like this URL at all, then just throw AuthException right here
    * 
    * @parm url the url to prepare, starting with "/" and NOT including server:port
    * @return the URL to process, with AA info removed
    */
   public static String unwrapUrl(String url) throws AuthException {
      return singleton().unwrapUrlImpl(url);
   }

   /** default impl will just prefix the url */
   protected String wrapUrlImpl(String url) {
      return prefixUrl(url, prefix);
   }

   /** default impl will just prefix the url */
   protected String prefixUrl(String url, String prefix) {
      String[] ss = url.split("://", 2);

      // be nice if the path is local
      if (ss.length == 1) {
         // if the paht was alreadey prepared, don't do it again...
         if (url != null && (url.startsWith(prefix) || url.startsWith("/" + prefix))) {
            return url;
         }

         return "/" + (prefix.length() > 0 ? (prefix) : "") + (url.startsWith("/") ? url : ("/" + url));
      }

      String[] ss2 = ss[1].split("/", 2);

      // if the paht was alreadey prepared, don't do it again...
      if (ss2[1] != null && ss2[1].startsWith(prefix)) {
         return url;
      }

      return ss[0] + "://" + ss2[0] + "/" + (prefix.length() > 0 ? (prefix + "/") : "") + ss2[1];
   }

   /**
    * default impl will just prefix the url
    * <p>
    * NOTE - derived classes should clear any session context info from previous calls here
    * <p>
    * If you simply don't like this URL at all, then just throw AuthException right here
    * <p>
    * NOTE one thing to remember is the path "/favicon.ico" - this is requested by the browsers
    * directly without asking for auth preparation or whatever, so...you must make sure you serve
    * that if you want to
    */
   protected String unwrapUrlImpl(String url) throws AuthException {
      if (url.equals("/favicon.ico"))
         return url;
      else {
         // TODO remove prefix
         return url;
      }
   }

   public LightAuth.AuthType iauthorize(Socket socket, String url, AttrAccess httpArgs) {
      String clientip = socket.getInetAddress().getHostAddress();

      // if Agents doesn't know myself, this should succeed, it's not a proper server but maybe
      // some sort of a test???

      // TODO this auth is really weak anyways...
      Object debug1 = Agents.getMyHostName();

      // TODO is this correct in linux?
      if (clientip.startsWith(Agents.getHomeNetPrefix()) || Agents.agent(Agents.getMyHostName()) == null
            || clientip.equals(Agents.agent(Agents.getMyHostName()).ip)) {
         return LightAuth.AuthType.INHOUSE;
      } else if (Comms.isLocalhost(clientip)) {
         return LightAuth.AuthType.INHOUSE;
      } else if (false) {
         // TODO identify shared secret
         return LightAuth.AuthType.SHAREDSECRET;
      } else if (false) {
         // TODO identify friends
         return LightAuth.AuthType.FRIEND;
      } else
         return LightAuth.AuthType.ANYBODY;
   }

   public static LightAuth.AuthType mapAuth(LightAuth.PermType perm) {
      switch (perm) {
      case ADMIN:
         return LightAuth.AuthType.INHOUSE;
      case CONTROL:
         return LightAuth.AuthType.SHAREDSECRET;
      case WRITE:
      case VIEW:
         return LightAuth.AuthType.FRIEND;
      case PUBLIC:
         return LightAuth.AuthType.ANYBODY;
      }
      return LightAuth.AuthType.SHAREDSECRET;
   }
  
   // TODO security - make these final somehow - plugns can attack by wrapping the lightauth...
   public String resetSecurity (String pwd) {return "NOT IMPLEMENTED";}
   public String accept (String pwd, AgentHandle who, String pk) {return "NOT IMPLEMENTED";}
   public String pubkey (AgentHandle who) {return "NOT IMPLEMENTED";}
}
