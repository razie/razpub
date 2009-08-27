/**
 * Razvan's code. 
 * Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.http.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.razie.pub.base.data.HtmlRenderUtils;
import com.razie.pub.base.log.Log;
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
 * a simple no threads web server with a few echo commands and a nice stylesheet, serving fils from
 * classpath only (the stylesheet)
 * 
 * TODO decouple from lightsoa - the basic server shouldn't know about lightsoa directly
 * 
 * @author razvanc99
 * 
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

    static LightServer     server;
    static SampleWebServer singleton;
    static LightCmdGET     cmdGET = new SimpleClasspathServer();

    public static void main(String[] argv) throws IOException {
        RazIconRes.init();
        start(TestLightBase.PORT);
    }

    public static void start(int port) {
        // stuff to set before you start the server
        HtmlRenderUtils.setTheme(new HtmlRenderUtils.DarkTheme());

        singleton = new SampleWebServer();

        server = new SampleNoThreadsServer(port);
        server.registerCmdListener(cmdGET);

        HttpSoaBinding soa = new HttpSoaBinding(singleton, "service");
        cmdGET.registerSoa(soa);
        
        Log.logThis("Starting simple web server at port: " + port);
        Log.logThis(" - try simple url like http://localhost:"+port+"/");

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

            if (path.equals("/") || path.equals("")) {
                path = "/classpath/com/razie/pub/http/test/index.html";
            }

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
