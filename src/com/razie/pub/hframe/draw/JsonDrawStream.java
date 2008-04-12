/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.draw;

import java.io.IOException;
import java.io.OutputStream;

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
public class JsonDrawStream extends com.razie.pub.hframe.draw.DrawStream.DrawStreamWrapper {

    public static final String MIME_APPLICATION_JSON = "application/json";

    public JsonDrawStream(MyServerSocket socket) throws IOException {
        super(new SimpleDrawStream(Technology.JSON, socket.getOutputStream()));
        this.setEndPoint(socket);
        ((SimpleDrawStream)proxied).writeBytes(HttpHelper.httpHeader(HttpHelper.OK, MIME_APPLICATION_JSON).getBytes());
    }

    public JsonDrawStream(OutputStream stream) throws IOException {
        super (new SimpleDrawStream(Technology.JSON, stream));
        ((SimpleDrawStream)proxied).writeBytes(HttpHelper.httpHeader(HttpHelper.OK, MIME_APPLICATION_JSON).getBytes());
    }

    @Override
    public void close() {
    }
}
