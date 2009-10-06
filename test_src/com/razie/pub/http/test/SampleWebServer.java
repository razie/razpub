/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.http.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.razie.pub.base.NoStatics;
import com.razie.pub.base.data.HtmlRenderUtils;
import com.razie.pub.base.log.Log;
import com.razie.pub.comms.AgentCloud;
import com.razie.pub.comms.AgentHandle;
import com.razie.pub.comms.Agents;
import com.razie.pub.comms.AuthException;
import com.razie.pub.comms.MyServerSocket;
import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.test.SampleDrawable;
import com.razie.pub.http.LightCmdGET;
import com.razie.pub.http.LightServer;
import com.razie.pub.lightsoa.HttpSoaBinding;
import com.razie.pub.lightsoa.SoaMethod;
import com.razie.pub.lightsoa.SoaStreamable;
import com.razie.pub.resources.RazIconRes;

/**
 * a simple no threads web server with a few echo commands and a nice stylesheet, serving file0s
 * from classpath only (the stylesheet)
 * 
 * NOTE: there is no security whatsoever
 * 
 * TODO decouple from lightsoa - the basic server shouldn't know about lightsoa directly
 * 
 * @author razvanc99
 */
public class SampleWebServer {

   @SoaMethod(descr = "echo", args = { "msg" })
   public String echo(String msg) {
      return "echo: " + msg;
   }

   @SoaMethod(descr = "ask the server to die")
   public String die() {
      server.shutdown();
      return "dying...";
   }

   @SoaMethod(descr = "demo for drawing models")
   @SoaStreamable
   public void sampleDrawable(DrawStream out) {
      SampleDrawable d = new SampleDrawable();
      Object ret = d.render(out.getTechnology(), out);
      if (ret != null)
         out.write(ret);
   }

   public static void main(String[] argv) throws IOException {
      RazIconRes.init();
      new SampleWebServer().start(TestLightServer.ME, new AgentCloud(TestLightServer.ME));
   }

      LightServer server = null;// setup in start
      
   public void start(AgentHandle me, AgentCloud cloud) {
      // stuff to set before you start the server
      HtmlRenderUtils.setTheme(new HtmlRenderUtils.DarkTheme());

      NoStatics.put(Agents.class, new Agents(cloud, me));
      
      LightCmdGET cmdGET = new SimpleClasspathServer();
      server = new SimpleNoThreadsServer(me, cmdGET);

      cmdGET.registerSoa(new HttpSoaBinding(this, "service"));

      Log.logThis("Starting simple web server at: " + me);
      Log.logThis(" - try simple url like: " + me.url + "/");

      // start the server thread...
      server.run();
   }

   /** the default cmdGET does not serve files - this one will serve from classpath */
   static class SimpleClasspathServer extends LightCmdGET {
      /**
       * serve only from classpath if the path is "/classpath/..."
       */
      @Override
      protected URL findUrlToServe(MyServerSocket socket, String path, Properties parms)
            throws MalformedURLException, AuthException {

         // the main page
         if (path.equals("/") || path.equals("")) {
            path = "/classpath/com/razie/pub/http/test/index.html";
         }

         // serve files
         if (path.startsWith("/classpath") || path.equals("/favicon.ico")) {
            String filenm = path;
            if (path.equals("/favicon.ico")) {
               filenm = "/public/favicon.ico";
               try {
                  socket.close();
               } catch (IOException e) {
                  Log.logThis("IGNORING: ", e);
               }
               return null;
            } else {
               filenm = path.replaceFirst("/classpath", "");
            }

            URL url = null;

            url = this.getClass().getResource(filenm);

            return url;
         }

         return null;
      }
   }

}
