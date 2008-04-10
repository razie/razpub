/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.draw.test;

import java.io.IOException;

import junit.framework.TestCase;

import com.razie.pub.hframe.base.log.Log;
import com.razie.pub.hframe.draw.DrawList;
import com.razie.pub.hframe.draw.DrawStream;
import com.razie.pub.hframe.draw.DrawTable;
import com.razie.pub.hframe.draw.Renderer;
import com.razie.pub.hframe.draw.SimpleDrawStream;

public class TestContainers extends TestCase {

    public void setUp() {
    }

    public void testWriteList() throws IOException {
        DrawStream stream = new SimpleDrawStream(Renderer.Technology.HTML);
        DrawList list = new DrawList();
        list.write("11");
        list.write("22");
        list.write("33");
        stream.write(list);
        String s = stream.toString();
        assertTrue(s.contains("33"));
    }

    public void testWriteTable() throws IOException {
        DrawStream stream = new SimpleDrawStream(Renderer.Technology.HTML);
        DrawTable list = new DrawTable(0, 2);
        list.write("11");
        list.write("22");
        list.write("33");
        stream.write(list);
        String s = stream.toString();
        assertTrue(s.contains("33"));
    }

    public void testStreamList() throws IOException {
        DrawStream stream = new SimpleDrawStream(Renderer.Technology.HTML);
        DrawList list = new DrawList();
        stream.open(list);
        String s = stream.toString();
        list.write("11");
        list.write("22");
        list.write("33");
        s = stream.toString();
        assertTrue(s.contains("33"));
        list.close();
        s = stream.toString();
        assertTrue(s.contains("33"));
    }

    public void testStreamTable() throws IOException {
        DrawStream stream = new SimpleDrawStream(Renderer.Technology.HTML);
        DrawTable list = new DrawTable(0, 2);
        stream.open(list);
        String s = stream.toString();
        list.write("11");
        list.write("22");
        list.write("33");
        s = stream.toString();
        assertFalse(s.contains("33"));
        list.close();
        s = stream.toString();
        assertTrue(s.contains("33"));
    }

    public void testStreamTableWithExactCols() throws IOException {
        DrawStream stream = new SimpleDrawStream(Renderer.Technology.HTML);
        DrawTable list = new DrawTable(0, 2);
        stream.open(list);
        String s = stream.toString();
        list.write("11");
        list.write("22");
        list.write("33");
        list.write("44");
        s = stream.toString();
        assertFalse(s.contains("33"));
        list.close();
        s = stream.toString();
        assertTrue(s.contains("33"));
    }

    static final Log logger = Log.Factory.create(TestContainers.class.getName());
}
