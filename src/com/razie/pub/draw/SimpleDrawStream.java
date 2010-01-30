/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.draw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.razie.pub.draw.Renderer.ContainerRenderer;

/**
 * simple stream to write in any technology to an output stream. Just translates from Draw to java
 * streams...
 * 
 * wrap this for instance in the http stream to wrap in http
 * 
 * @author razvanc99
 * @version $Id$
 * 
 */
public class SimpleDrawStream extends DrawStream {

    /** the actual output socket */
    protected OutputStream out;

    public SimpleDrawStream(Technology tech, OutputStream out) throws IOException {
        super(tech);
        this.out = out;
    }

    public SimpleDrawStream(Technology tech) throws IOException {
        super(tech);
        this.out = new ByteArrayOutputStream();
    }

    public SimpleDrawStream() throws IOException {
        super(Technology.TEXT);
        this.out = new ByteArrayOutputStream();
    }

    protected void writeBytes(byte[] b) {
        try {
            out.write(b);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void renderObjectToStream(Object d) {
        Object result = Renderer.Helper.draw(d, technology, this);
        writeBytes(result.toString().getBytes());
    }

    @Override
    public void renderElement(StreamableContainer container, Object element) {
        Object result = ((ContainerRenderer) ((Drawable) container).getRenderer(technology)).renderElement(
                container, element, technology, this);
        writeBytes(result.toString().getBytes());
    }

    @Override
    protected void renderFooter(StreamableContainer container) {
        Object result = ((ContainerRenderer) ((Drawable) container).getRenderer(technology)).renderFooter(
                container, technology, this);
        writeBytes(result.toString().getBytes());
    }

    @Override
    protected void renderHeader(StreamableContainer container) {
        Object result = ((ContainerRenderer) ((Drawable) container).getRenderer(technology)).renderHeader(
                container, technology, this);
        writeBytes(result.toString().getBytes());
    }

    public String toString() {
        return out.toString();
    }
}
