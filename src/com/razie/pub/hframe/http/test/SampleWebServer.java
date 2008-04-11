package com.razie.pub.hframe.http.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.razie.pub.hframe.base.data.HtmlRenderUtils;
import com.razie.pub.hframe.base.data.HtmlRenderUtils.HtmlTheme;
import com.razie.pub.hframe.http.AuthException;
import com.razie.pub.hframe.http.LightCmdGET;
import com.razie.pub.hframe.http.LightServer;
import com.razie.pub.hframe.http.MyServerSocket;
import com.razie.pub.hframe.lightsoa.HttpSoaBinding;
import com.razie.pub.hframe.lightsoa.SoaMethod;

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

    @SoaMethod(descr = "die")
    public String die() {
        server.shutdown();
        return "dying...";
    }

    static LightServer     server;
    static SampleWebServer singleton;
    static LightCmdGET     cmdGET = new SimpleClasspathServer();

    public static void main(String[] argv) {
        start(TestLightServer.PORT);
    }

    public static void start(int port) {
        // stuff to set before you start the server
        HtmlRenderUtils.setTheme(new DarkTheme());

        singleton = new SampleWebServer();

        server = new SampleNoThreadsServer(port);
        server.registerCmdListener(cmdGET);

        HttpSoaBinding soa = new HttpSoaBinding(singleton, "service");
        cmdGET.registerSoa(soa);

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
                path = "/classpath/com/razie/pub/hframe/http/test/index.html";
            }

            if (path.startsWith("/classpath") || path.equals("/favicon.ico")) {
                String filenm = path;
                if (path.equals("/favicon.ico")) {
                    filenm = "/public/favicon.ico";
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
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

    /** a simple, css-based theme to be used by the sample server */
    static class DarkTheme extends HtmlTheme {
        static String[] tags = {
                                     "<head><link rel=\"stylesheet\" type=\"text/css\" href=\"/classpath/com/razie/pub/hframe/http/test/style.css\" /></head><body link=\"yellow\" vlink=\"yellow\">",
                                     "</body>", "<html>", "</html>" };

        public String get(int what) {
            return tags[what];
        }
    }

}
