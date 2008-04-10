/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.draw;

import java.io.IOException;
import java.io.OutputStream;

import com.razie.pub.hframe.base.data.HtmlRenderUtils;
import com.razie.pub.hframe.draw.Renderer.Technology;
import com.razie.pub.hframe.http.HttpHelper;
import com.razie.pub.hframe.http.MyServerSocket;

/**
 * a drawing stream to an http client (plain old web). Will render objects in the html and wrap in
 * http header/footer
 * 
 * @author razvanc99
 * @version $Id$
 * 
 */
public class HttpDrawStream extends com.razie.pub.hframe.draw.DrawStream.DrawStreamWrapper {
    private MyServerSocket socket;
    private boolean        wroteHeader = false;

    public MyServerSocket getSocket() {
        return socket;
    }

    public HttpDrawStream(MyServerSocket socket) throws IOException {
        super(new SimpleDrawStream(Technology.HTML, socket.getOutputStream()));
        this.socket = socket;
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
                ((SimpleDrawStream) proxied).writeBytes(HtmlRenderUtils.htmlHeader().getBytes());
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
}
