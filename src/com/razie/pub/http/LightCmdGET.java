/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.razie.pub.agent.AgentFileService;
import com.razie.pub.base.data.HttpUtils;
import com.razie.pub.base.data.MimeUtils;
import com.razie.pub.base.log.Log;
import com.razie.pub.comms.AuthException;
import com.razie.pub.comms.LightAuth;
import com.razie.pub.comms.MyServerSocket;
import com.razie.pub.draw.Renderer.Technology;
import com.razie.pub.draw.widgets.DrawError;
import com.razie.pub.lightsoa.HttpSoaBinding;

/**
 * WARNING this is a no-security whatsoever web server...will serve whatever it finds and it can, so
 * DO NOT enable this towards the internet...it's intended as a sample class...see the
 * SampleWebServer.
 * 
 * very simple http server implementation, with lightsoa hookup. This is a good demonstration of how
 * http is built on top of a plain socket :)
 * 
 * It will:
 * <ul>
 * <li>call lightsoa methods, i.e. "echo" if the path is "/lightsoa/echo"</li>
 * <li>serve files from the classpath if the path is "/classpath/com/razie/..."
 * 
 * @author razvanc99
 * 
 */
public class LightCmdGET extends SocketCmdListener.Impl {
    /**
     * overload this to implement authentication/authorization etc
     * 
     * @param path is the path the user wants: the entire url after GET
     * @return null to disable serving or the path to serve. normally you remove auth tokens
     * @throws AuthException
     */
    protected String shouldServe(MyServerSocket socket, String path) throws AuthException {
        return LightAuth.unwrapUrl(path);
    }

    /**
     * overload this if you want to serve lightsoa bindings at certain urls (for isntance servlets
     * can have a name "myservlet1/dothis" vs "myservlet2/dothis" different than serving the file
     * "myservlet1.file"
     * 
     * @param path is the path the user wants: the entire url after GET, with all ?& parms removed
     * @return null if this doesn't map to a known soa path or non-null containing the new path to
     *         use for a soa call in the form SVCNAME/METHODNAME...
     */
    protected String findSoaToCall(MyServerSocket socket, String path, Properties parms) {
        return path.startsWith("/lightsoa/") ? path.replaceFirst("/lightsoa/", "") : null;
    }

    /**
     * overload this to serve only what you want so you only serve those files...MAKE sure you avoid
     * stupid attacks like "/../../windows/windows.exe" or etc. THIS class is not intended for
     * connections open to the internet...
     * 
     * @param path is the path the user wants: the entire url after GET, with all ?& parms removed
     * @return null to disable serving or the URL of the file to serve. This is where you resolve
     *         mappings and disable access to folders. for instance, map "/" to "c:/myserver/public"
     *         so you only serve those files...MAKE sure you avoid stupid attacks like
     *         "/../../windows/windows.exe" or etc.
     * @throws MalformedURLException
     * @throws AuthException
     */
    protected URL findUrlToServe(MyServerSocket socket, String path, Properties parms)
            throws MalformedURLException, AuthException {
        // disable serving by default
        return null;
    }

    /**
     * call the actual soa service with the command you just identified. At this point the path
     * 
     * @param socket
     * @param originalPath
     * @param svc
     * @param cmd
     * @param cmdargs
     * @param parms
     * @return
     */
    protected Object callSoa(MyServerSocket socket, String originalPath, String svc, String cmd,
            String cmdargs, Properties parms) {
        String p = "http";

        Object reply = "";

        // try the new soa bridges
        for (HttpSoaBinding c : getBindings()) {
            if (c.getServiceName().equals(svc)) {
                boolean callThisOne = false;

                if ("asset".equals(svc)) {
                    // the asset soa binding doesn't have soamethods - must hack here
                    callThisOne = true;
                } else
                    for (String s : c.getSoaMethods()) {
                        if (cmd.equals(s)) {
                            callThisOne = true;
                        }
                    }

                if (callThisOne) {
                    logger.log("HTTP_FOUND_SOA_BRIDGE: " + c.getClass().getName());
                    try {
                        reply = c.executeCmdServer(cmd, p, cmdargs, parms, socket);
                    } catch (Throwable e) {
                        logger.log("HTTP_ERR_INVOKING_SOA: ", e);
                        reply = new DrawError(e);
                    }

                    break;
                }
            }

        }
        return reply;
    }

    /** main entry point, called from the Server's Receiver, when cmd is GET */
    public Object executeCmdServer(String cmdName, String protocol, String args, Properties parms,
            MyServerSocket socket) throws AuthException {
        logger.trace(3, "execute cmdName=", cmdName, ", protocol=", protocol, ", args=", args);

        // find path to serve
        String path;

        if (args.contains(" ")) {
            // standard HTTP call: "GET PATH HTTP1.1"
            path = args.substring(0, args.indexOf(' '));
            // String http = args.substring(args.indexOf(' ') + 1);
            // logger.trace(3, "GET path=", path, ", http=", http);
        } else {
            path = args;
            logger.log("WARN_HTTP_NOTSTANDARD GET request...missing HTTP ver suffix");
        }

        path = shouldServe(socket, path);
        if (path == null) {
            logger.log("WARN_HTTP_PATHNOTTOSERVE " + path);
            return HttpHelper.httpWrap(HttpHelper.OK, "Echo service: " + cmdName + " " + args, 0);
        }

        // standard URL parsing of parms - the path will be replaced by the first part (without
        // parms)
        if (path.contains("?")) {
            String[] sparms = path.split("\\?", 2);
            path = sparms[0];
            String[] pairs = sparms[1].split("&");
            for (String pair : pairs) {
                String[] split = pair.split("=", 2);
                parms.put(split[0], HttpUtils.fromUrlEncodedString(split[1]));
            }
        }

        // is this a known soa binding?
        String cmd = findSoaToCall(socket, path, parms);
        if (cmd != null) {
            String[] ss = cmd.split("/", 3);
            String svc = ss[0];

            if (ss.length < 2) {
                throw new IllegalArgumentException(
                        "ERR_ Path must be http://host:port/PREFIX/SERVICE/METHOD - you're missing something");
            }

            cmd = ss[1];
            String cmdargs = ss.length > 2 ? ss[2] : "";

            Object reply = callSoa(socket, path, svc, cmd, cmdargs, parms);

            if (reply != null) {
                if (reply instanceof StreamConsumedReply) {
                    return reply;
                } else if (reply instanceof DrawError) {
                    return HttpHelper.httpWrap(HttpHelper.EXC, ((DrawError) reply).render(Technology.HTML,
                            null).toString(), 0);
                }

                return path.startsWith("/mutant/cmd") ? HttpHelper.httpWrap(HttpHelper.OK, reply.toString(),
                        0) : reply;
            }

            return HttpHelper.httpWrap(HttpHelper.OK, "<NULL>", 0);
        }

        // should i serve a file?
        try {
            URL file = findUrlToServe(socket, path, parms);
            if (file != null) {
                return serveFile(socket, file.toExternalForm(), file);
            } else {
                return HttpHelper.httpWrap(HttpHelper.NOTFOUND, "File not found: " + path, 0);
            }
        } catch (MalformedURLException e) {
            logger.log("can't serve the file: ", e);
            DrawError reply = new DrawError("ERR_HTTP_SOA_?: ", e);
            return HttpHelper.httpWrap(HttpHelper.EXC, ((DrawError) reply).render(Technology.HTML, null)
                    .toString(), 0);
        }
    }

    /**
     * @param socket
     * @param path
     * @return
     */
    protected Object serveFile(MyServerSocket socket, String filenm, URL url) {
        // serve files

        PrintStream out = null;
        InputStream in = null;

        long len = 0;

        try {
            out = new PrintStream(socket.getOutputStream());
            in = url.openStream();
            len = in.available();
        } catch (IOException e) {
            HttpHelper.httpWrap(HttpHelper.EXC, e.toString(), 0);
        }

        if (HttpHelper.isImage(filenm)) {
            out.print(HttpHelper.httpWrapPic(filenm, len));
        } else if (HttpHelper.isOtherFile(filenm)) {
            out.print(HttpHelper.httpWrapOtherFile(filenm, len));
        } else {
            String type = MimeUtils.getMimeType(filenm);
            if (type.equals(MimeUtils.UNKNOWN_MIME_TYPE)) {
                out.print(HttpHelper.httpWrapMimeType(type, len));
                // out.print(HttpHelper.httpWrap(HttpHelper.OK, null, len));
            } else {
                out.print(HttpHelper.httpWrapMimeType(type, len));
            }
        }

        if (in == null) {
            Log.logThis("THE INPUT STREAM IS NULL...url=" + url);
        } else
            AgentFileService.copyStream(in, out);
        return null;
    }

    /** add an http binding to be called when the method/service matches a url */
    public void registerSoa(HttpSoaBinding c) {
        bindings.add(c);
        Log.logThis("HTTP_INIT_LISTENER " + c.getClass().getName());
    }

    /** remove an existing soa binding */
    public void removeSoa(HttpSoaBinding c) {
        bindings.remove(c);
        Log.logThis("HTTP_REMOVE_LISTENER " + c.getClass().getName());
    }

    /**
     * @return the soa bindings in use
     */
    public List<HttpSoaBinding> getBindings() {
        return bindings;
    }

    public String[] getSupportedCommands() {
        return COMMANDS;
    }

    static final String[]        COMMANDS = { "GET"}; // TODO "POST", "PUT", "DELETE" };
    static final Log             logger   = Log.Factory.create("", LightCmdGET.class.getName());
    private List<HttpSoaBinding> bindings = new ArrayList<HttpSoaBinding>();
}
