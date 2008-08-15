/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.lightsoa;

import java.io.IOException;
import java.util.Properties;

import com.razie.pub.assets.AssetKey;
import com.razie.pub.assets.AssetMgr;
import com.razie.pub.base.AttrAccess;
import com.razie.pub.base.ScriptContext;
import com.razie.pub.base.data.HttpUtils;
import com.razie.pub.base.log.Log;
import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.HttpDrawStream;
import com.razie.pub.draw.JsonDrawStream;
import com.razie.pub.draw.MimeDrawStream;
import com.razie.pub.draw.SimpleDrawStream;
import com.razie.pub.draw.Renderer.Technology;
import com.razie.pub.http.MyServerSocket;
import com.razie.pub.http.SoaNotHtml;
import com.razie.pub.http.StreamConsumedReply;

/**
 * call services or assets via simple http requests
 * 
 * can handle methods that take only String arguments, including SoaStreamables, which return one
 * of: void, String, Drawable, Object - the return value will be drawn on an HttpDrawStream
 * 
 * use SoaNotHtml to prevent the return String from being drawn on html...
 * 
 * as a rule of thumb in implementing these, the URLs are of the form /PREFIX/serviceName/METHODNAME
 * 
 * @author razvanc99
 * 
 */
public class HttpSoaBinding extends SoaBinding {
    /** lightsoa server is setup to forward here if the http request starts with this prefix */
    public static final String PREFIX = "/lightsoa/cmd/";

    /**
     * create a simple binding for a service instance - you then have to register it with the server
     * 
     * if the class is annotated, use the other constructor, please...
     * 
     * @param service the object implementing the service methods
     * @param serviceName the name to use - no funky characters, other than ".". Especially no
     *        spaces, eh?
     */
    public HttpSoaBinding(Object service, String serviceName) {
        this(service.getClass(), serviceName);
        this.service = service;
    }

    /**
     * create a simple binding - you then have to register it with the server
     * 
     * if the class is annotated, use the other constructor, please...
     * 
     * @param service the class of the callback implementing the service/asset methods
     * @param serviceName the name to use - no funky characters, other than ".". Especially no
     *        spaces, eh?
     */
    public HttpSoaBinding(Class serviceCls, String serviceName) {
        super(serviceCls, serviceName);

        // check service name matches annotation
        if (serviceCls != null && serviceCls.getAnnotation(SoaService.class) != null) {
            SoaService s = (SoaService) serviceCls.getAnnotation(SoaService.class);
            if (!s.name().equals(serviceName))
                throw new IllegalArgumentException(
                        "can't bind service not annotated with @SoaService/@SoaAsset: "
                                + service.getClass().getName());
        } else if (serviceCls != null && serviceCls.getAnnotation(SoaAsset.class) != null) {
            SoaAsset s = (SoaAsset) serviceCls.getAnnotation(SoaAsset.class);
            if (!s.type().equals(serviceName))
                throw new IllegalArgumentException(
                        "can't bind service not annotated with @SoaService/@SoaAsset: "
                                + service.getClass().getName());
        } else {
            Log.logThis("BOUND class which was not annotated...");
        }
    }

    /**
     * create a simple binding for and annotated SoaService - you then have to register it wiht the
     * server
     * 
     * @param service the object implementing the service methods
     * @param serviceName the name to use - no funky characters, other than ".". Especially no
     *        spaces, eh?
     */
    public HttpSoaBinding(Object service) {
        super(service, "");
        if (service.getClass().getAnnotation(SoaService.class) != null) {
            SoaService s = (SoaService) service.getClass().getAnnotation(SoaService.class);
            this.serviceName = s.name();
        } else {
            throw new IllegalArgumentException("can't bind service not annotated with @SoaService: "
                    + service.getClass().getName());
        }
    }

    /** make a suitable URL to invoke a lightsoa via http - just read the url */
    public String makeUrl(String httpUrl, String action, AttrAccess parms) {
        return parms.addToUrl(httpUrl + PREFIX + action);
    }

    /**
     * main entry point from the http server
     * 
     * @param actionName the command code == soa method name
     * @param protocol protocol normaly "http"
     * @param cmdargs args in the url following the method name with, think servlet entry point
     *        "mymethod/a/b"
     * @param parms all parms in the url decoded parms follow the url with ? and &
     * @param socket the server socket
     * @return
     */
    public Object executeCmdServer(String actionName, String protocol, String cmdargs, Properties parms,
            MyServerSocket socket) {

        Object otoi = this.service;
        AssetKey key = null;

        if (otoi == null) {
            // must be an asset instance
            key = AssetKey.fromEntityUrl(HttpUtils.fromUrlEncodedString(actionName));
            String[] ss = cmdargs.split("/", 2);

            actionName = ss[0];
            cmdargs = ss.length > 1 ? ss[1] : null;

            otoi = AssetMgr.getAsset(key);
        }

        if (otoi == null) {
            Log.logThis("HTTP_SOA_ASSETNOTFOUND: " + key);
            return "HTTP_SOA_ASSETNOTFOUND: " + key;
        }

        // maybe it's a dumb asset or injected functionality
        if (methods.size() > 0 && !methods.containsKey(actionName)) {
            Log.logThis("HTTP_SOA_UNKWNOWNACTION: " + actionName);
            return "HTTP_SOA_UNKNOWNACTION: " + actionName;
        }

        Object response = null;
        DrawStream out = null;

        if (methods.size() <= 0) {
            // didn't find it but there's no methods for this anyhow...
            Log.logThis("HTTP_SOA_injected: " + actionName + ": ");
            ScriptContext ctx = new ScriptContext.Impl(ScriptContext.Impl.global());
            ctx.setAttr(parms);
            response = AssetMgr.executeCmd(actionName, key, ctx);
        } else if (methods.containsKey(actionName)) {
            Log.logThis("HTTP_SOA_" + actionName + ": ");

            AttrAccess args = new AttrAccess.Impl(parms);

            // setup the parms
            SoaMethod mdesc = methods.get(actionName).getAnnotation(SoaMethod.class);

            if (mdesc.auth().length() > 0) {
                // TODO check auth
            }

            if (methods.get(actionName).getAnnotation(SoaStreamable.class) != null) {
                SoaStreamable nh = methods.get(actionName).getAnnotation(SoaStreamable.class);
                if (nh.streamMimeType().length() > 0) {
                    out = makeMimeDrawStream(socket, nh.streamMimeType());
                } else
                    out = makeDrawStream(socket, protocol);
                response = invokeStreamable(otoi, actionName, out, args);
            } else {
                response = invoke(otoi, actionName, args);
            }

            if (methods.get(actionName).getAnnotation(SoaNotHtml.class) != null) {
                if (methods.get(actionName).getAnnotation(SoaStreamable.class) != null) {
                    throw new IllegalArgumentException("Cannot have a streamable nothtml");
                }
                // no special formatting, probably defaults to toString()
                return response;
            }

        }
        if (response != null) {
            // maybe stream already created for a streamable that returned a Drawable?
            // unbelievable...
            out = out != null ? out : makeDrawStream(socket, protocol);
            out.write(response);
            out.close();
            return new StreamConsumedReply();
        } else if (response == null) {
            if (out != null)
                out.close();
            return new StreamConsumedReply();
        }

        return response;
    }

    private DrawStream makeDrawStream(MyServerSocket socket, String protocol) {
        DrawStream out;
        try {
            if ("http".equals(protocol))
                out = new HttpDrawStream(socket);
            else if ("json".equals(protocol))
                out = new JsonDrawStream(socket);
            else
                out = new SimpleDrawStream(Technology.TEXT, socket.getOutputStream());
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
        return out;
    }

    private DrawStream makeMimeDrawStream(MyServerSocket socket, String mime) {
        DrawStream out;
        try {
            if (HttpDrawStream.MIME_TEXT_HTML.equals(mime))
                out = new HttpDrawStream(socket);
            else if (JsonDrawStream.MIME_APPLICATION_JSON.equals(mime))
                out = new JsonDrawStream(socket);
            else
                out = new MimeDrawStream(socket.getOutputStream(), mime);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
        return out;
    }

    public String toString() {
        return this.serviceName + " : "
                + (this.service == null ? "NULL SERVICE" : this.service.getClass().getName());
    }
}
