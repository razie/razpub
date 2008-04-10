/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.draw;

import com.razie.pub.hframe.draw.Renderer.Technology;

/**
 * just wrap a container and present it as a stream - this is to separate model logic from
 * presentation: the model logic knows to stream and you just just stream it into a presentation
 * container (i.e. table)
 * 
 * note again, that the objects are not rendered yet, in either this stream or its container...
 * 
 * @author razvanc99
 * 
 */
public class ContainerStream extends DrawStream {
    StreamableContainer c;

    public ContainerStream(StreamableContainer c) {
        super(Technology.ANY);
        this.c=c;
    }

    @Override
    public void close() {
        c.close();
    }

    @Override
    protected void renderObjectToStream(Object d) {
        // just forward to my target - it will actually render whenever however it pleases
        this.c.write(d);
    }

    @Override
    public void renderElement(StreamableContainer container, Object element) {
        // nothing to do - that element container has already been forwarded to my target
    }

    @Override
    protected void renderFooter(StreamableContainer container) {
        // nothing to do - that element container has already been forwarded to my target
    }

    @Override
    protected void renderHeader(StreamableContainer container) {
        // nothing to do - that element container has already been forwarded to my target
    }

}
