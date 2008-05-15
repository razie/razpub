/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.draw;

import java.io.IOException;
import java.io.OutputStream;

import com.razie.pub.draw.Renderer.Technology;
import com.razie.pub.http.HttpHelper;

/**
 * a drawing stream that is plain text, but you can specify the mime type sent to the client
 * 
 * @author razvanc99
 * @version $Id$
 * 
 */
public class MimeDrawStream extends com.razie.pub.draw.DrawStream.DrawStreamWrapper {

    public MimeDrawStream(OutputStream stream, String mime) throws IOException {
        super (new SimpleDrawStream(Technology.TEXT, stream));
        ((SimpleDrawStream)proxied).writeBytes(HttpHelper.httpHeader(HttpHelper.OK, mime).getBytes());
    }

    @Override
    public void close() {
    }
}
