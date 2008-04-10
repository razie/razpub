package com.razie.pub.hframe.lightsoa;

import java.io.IOException;
import java.util.Properties;

import com.razie.pub.hframe.assets.AssetKey;
import com.razie.pub.hframe.assets.BaseAssetMgr;
import com.razie.pub.hframe.base.AttrAccess;
import com.razie.pub.hframe.base.data.HttpUtils;
import com.razie.pub.hframe.base.log.Log;
import com.razie.pub.hframe.draw.DrawStream;
import com.razie.pub.hframe.draw.HttpDrawStream;
import com.razie.pub.hframe.draw.JsonDrawStream;
import com.razie.pub.hframe.draw.SimpleDrawStream;
import com.razie.pub.hframe.draw.Renderer.Technology;
import com.razie.pub.hframe.http.MyServerSocket;
import com.razie.pub.hframe.http.SoaNotHtml;
import com.razie.pub.hframe.http.StreamConsumedReply;

/**
 * call services via simple http requests
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
     * create a simple binding - you then have to register it with the server
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
     * @param service the object implementing the service methods
     * @param serviceName the name to use - no funky characters, other than ".". Especially no
     *        spaces, eh?
     */
    public HttpSoaBinding(Class serviceCls, String serviceName) {
        super(serviceCls, serviceName);

        // check service name matches annotation
        if (serviceCls.getAnnotation(SoaService.class) != null) {
            SoaService s = (SoaService) serviceCls.getAnnotation(SoaService.class);
            if (!s.name().equals(serviceName))
                throw new IllegalArgumentException("can't bind service not annotated with @SoaService: "
                        + service.getClass().getName());
        } else if (serviceCls.getAnnotation(SoaAsset.class) != null) {
            SoaAsset s = (SoaAsset) serviceCls.getAnnotation(SoaAsset.class);
            if (!s.type().equals(serviceName))
                throw new IllegalArgumentException("can't bind service not annotated with @SoaService: "
                        + service.getClass().getName());
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

        if (otoi == null) {
            // must be an asset instance
            AssetKey key = AssetKey.fromEntityUrl(HttpUtils.fromUrlEncodedString(actionName));
            String[] ss = cmdargs.split("/", 2);

            actionName = ss[0];
            cmdargs = ss.length > 1 ? ss[1] : null;
            
            otoi = BaseAssetMgr.getAsset(key);
        }

        if (methods.containsKey(actionName)) {
            Log.logThis("HTTP_SOA_" + actionName + ": ");

            AttrAccess args = new AttrAccess.Impl(parms);

            // setup the parms
            SoaMethod mdesc = methods.get(actionName).getAnnotation(SoaMethod.class);

            Object resp = null;
            DrawStream out = null;

            if (mdesc.auth().length() > 0) {

            }

            if (methods.get(actionName).getAnnotation(SoaStreamable.class) != null) {
                out = makeDrawStream(protocol, socket);
                resp = invokeStreamable(otoi, actionName, out, args);
            } else {
                resp = invoke(otoi, actionName, args);
            }

            if (methods.get(actionName).getAnnotation(SoaNotHtml.class) != null) {
                if (methods.get(actionName).getAnnotation(SoaStreamable.class) == null) {
                    throw new UnsupportedOperationException("can't have a streamable nothtml");
                }
                // no special formatting, probably defaults to toString()
                return resp;
            }

            if (resp != null) {
                // maybe stream already created for a streamable that returned a Drawable?
                // unbelievable...
                out = out != null ? out : makeDrawStream(protocol, socket);
                out.write(resp);
                out.close();
                return new StreamConsumedReply();
            } else if (resp == null) {
                out.close();
                return new StreamConsumedReply();
            }

            return resp;
        }

        Log.logThis("HTTP_SOA_UNKWNOWNACTION: " + actionName);
        return "HTTP_SOA_UNKNOWNACTION: " + actionName;
    }

    private DrawStream makeDrawStream(String protocol, MyServerSocket socket) {
        DrawStream out;
        try {
            if ("http".equals(protocol))
                out = new HttpDrawStream(socket);
            else if ("json".equals(protocol))
                out = new JsonDrawStream(socket.getOutputStream());
            else
                out = new SimpleDrawStream(Technology.TEXT, socket.getOutputStream());
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
        return out;
    }

    public String toString() {
        return this.serviceName + " : " + this.service.getClass().getName();
    }
}
