/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.draw;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.razie.pub.base.data.HtmlRenderUtils;
import com.razie.pub.draw.Renderer.Technology;
import com.razie.pub.http.HttpHelper;
import com.razie.pub.http.MyServerSocket;

/**
 * a drawing stream to an http client (plain old web). Will render objects in the html and wrap in
 * http header/footer
 * 
 * @author razvanc99
 * @version $Id$
 * 
 */
public class HttpDrawStream extends com.razie.pub.draw.DrawStream.DrawStreamWrapper {
    private boolean            wroteHeader    = false;
    private List<String>       metas          = null;
    private List<String>       httptags          = null;
    public static final String MIME_TEXT_HTML = "text/html";

    public HttpDrawStream(MyServerSocket socket) throws IOException {
        super(new SimpleDrawStream(Technology.HTML, socket.getOutputStream()));
        this.setEndPoint(socket);
    }

    public HttpDrawStream(OutputStream socket) throws IOException {
        super(new SimpleDrawStream(Technology.HTML, socket));
        ((SimpleDrawStream) proxied).writeBytes(HttpHelper.httpHeader(HttpHelper.OK).getBytes());
        ((SimpleDrawStream) proxied).writeBytes(HtmlRenderUtils.htmlHeader().getBytes());
    }

    /** add a completed object to the stream */
    @Override
    public void write(Object d) {
        header();
        proxied.write(d);
    }

    /** add an object to the stream */
    @Override
    public void open(Object d) {
        header();
        proxied.open(d);
    }

    private void header() {
        // this is to allow clients to switch tech to json before the first write
        if (!wroteHeader) {
            wroteHeader = true;
            if (this.technology.equals(Technology.JSON)) {
                ((SimpleDrawStream) proxied).writeBytes(HttpHelper.httpHeader(HttpHelper.OK,
                        "application/json").getBytes());
            } else if (this.technology.equals(Technology.HTML)) {
                ((SimpleDrawStream) proxied).writeBytes(HttpHelper.httpHeader(HttpHelper.OK).getBytes());
                if (this.metas == null)
                    ((SimpleDrawStream) proxied).writeBytes(HtmlRenderUtils.htmlHeader().getBytes());
                else
                    ((SimpleDrawStream) proxied).writeBytes(HtmlRenderUtils.htmlHeader(
                            this.metas.toArray(new String[0])).getBytes());
            }
        }
    }

    @Override
    public void close() {
        // TODO not correct, since BG threads may still produce stuff...
        // ((SimpleDrawStream) proxied).writeBytes("<p> END OF STREAM </p>".getBytes());
        if (this.technology.equals(Technology.HTML)) {
            ((SimpleDrawStream) proxied).writeBytes(HtmlRenderUtils.htmlFooter().getBytes());
        }
        super.close();
    }

    public void addMeta(String string) {
        if (this.metas == null)
            this.metas = new ArrayList<String>();
        this.metas.add(string);
    }
    
    public void addHttpTag(String string) {
        if (this.httptags == null)
            this.httptags = new ArrayList<String>();
        this.httptags.add(string);
    }
}
