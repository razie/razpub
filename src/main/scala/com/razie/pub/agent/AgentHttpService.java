/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.agent;

import java.util.List;

import razie.assets.Meta;

import com.razie.pub.base.NoStaticSafe;
import com.razie.pub.base.NoStatics;
import com.razie.pub.base.log.Log;
import com.razie.pub.http.LightCmdGET;
import com.razie.pub.http.LightServer;
import com.razie.pub.http.SocketCmdHandler;
import com.razie.pub.lightsoa.HttpAssetSoaBinding;
import com.razie.pub.lightsoa.HttpSoaBinding;
import com.razie.pub.lightsoa.ISoaBinding;

/**
 * this is the main web server, basically - required by the Agent itself. Note that you have to
 * create an actual server and initialize it before mounting this service...
 * 
 * As you can see from the code, it simply abstracts the soa registration to an agent service level.
 * It also includes lifecycle management for the server.
 * 
 * provides: web server (http implementation) with lightsoa,
 * 
 * @author razvanc
 * @version $Id$
 */
@NoStaticSafe
public class AgentHttpService extends AgentService {
   public Thread              serverThread;
   LightServer                server;
   LightCmdGET                cmdGET       = null;
   StatusReport               status       = new StatusReport();
   public HttpAssetSoaBinding assetBinding = null;              // TODO 3 CODE encapsulate properly

   /**
    * create a simple http service. NOTE that the serverToUse must have a cmdGet handler, where we
    * hookup the bindings later
    * 
    * @param agent - need to
    * @param serverToUse
    */
   public AgentHttpService(Agent agent, LightServer serverToUse) {
      this.agent = agent;
      this.server = serverToUse;

      if (NoStatics.get(AgentHttpService.class) == null)
         NoStatics.put(AgentHttpService.class, this);
      else {
         status.lastError = new IllegalStateException("ERR_SVC_INIT AgentWebSrevice already initialized!");
         throw (IllegalStateException) status.lastError;
      }

      // the server must have a GET command for thise service...
      for (SocketCmdHandler l : server.getHandlers())
         if (l instanceof LightCmdGET) {
            cmdGET = (LightCmdGET) l;
            break;
         }

      if (cmdGET == null) {
         status.lastError = new IllegalStateException(
               "AgentHttpService needs a LightCmdGET listener initialized with the server");
         throw (IllegalStateException) status.lastError;
      }

      // what this is is asset access via AssetMgr - that's what an empty AssetSoaBinding does :)
      cmdGET.registerSoa(assetBinding = new HttpAssetSoaBinding());
      status.ok();
   }

   /** NoStatic singleton */
   public static AgentHttpService instance() {
      return (AgentHttpService) NoStatics.get(AgentHttpService.class);
   }

   /*
    *  NOTE: you may want to check if there's on in your context: instance()!=null first
    */
   public static void registerSoa(HttpSoaBinding c) {
      instance().cmdGET.registerSoa(c);
   }

   /** if meta not passed in, must be annotated with SoaAsset and have the meta set.
    * 
    *  NOTE: you may want to check if there's on in your context: instance()!=null first
    */
   public static void registerSoaAsset(Class<?> c, Meta...meta) {
      instance().assetBinding.register(c, meta);
   }

   /*
    *  NOTE: you may want to check if there's on in your context: instance()!=null first
    */
   public static void registerHandler(SocketCmdHandler h) {
      instance().server.registerHandler(h);
   }

   public static List<SocketCmdHandler> getHandlers() {
      return instance().server.getHandlers();
   }

   public static Iterable<ISoaBinding> getBindings() {
      return instance().cmdGET.getBindings();
   }

   public void todoEncapsulateSomehowJoin() {
      try {
         serverThread.join();
      } catch (InterruptedException e) {
         Log.alarmThis("WARN_MUTANT_INTERRUPTED", e);
      } catch (Throwable t) {
         Log.alarmThis("ERROR_MUTANT_INTERRUPTED", t);
      }
      Log.alarmThis("MUTANT_DONE");
   }

   @Override
   protected void onShutdown() {
      Log.logThis("AGENTWEB_SHUTDOWN");
      server.shutdown();
      // serverThread.interrupt();
      todoEncapsulateSomehowJoin();
   }

   @Override
   protected void onStartup() {
      Log.logThis("AGENTWEB_STARTUP");
      serverThread = new Thread(server, "AgentServerThread");
      serverThread.start();
   }

   /** get status and report */
   @Override
   public StatusReport status() {
      StatusReport res = new StatusReport(StatusReport.Status.GREEN);
      return res;
   }

}
