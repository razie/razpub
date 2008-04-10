/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.draw.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import com.razie.pub.hframe.base.log.Log;
import com.razie.pub.hframe.draw.HttpDrawStream;
import com.razie.pub.hframe.draw.JsonDrawStream;

public class TestStreams extends TestCase {

    public void setUp() {
    }

    public void testAllStreams() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        HttpDrawStream http = new HttpDrawStream (bytes);
        http.write("11");
        assertTrue (bytes.toString().contains("HTTP"));
        JsonDrawStream json = new JsonDrawStream (bytes);
        json.write("11");
        assertTrue (bytes.toString().contains("/json"));
    }

    static final Log logger = Log.Factory.create(TestStreams.class.getName());
}
