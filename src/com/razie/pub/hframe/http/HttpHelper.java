/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.http;

import com.razie.pub.hframe.base.data.HtmlRenderUtils;

public class HttpHelper {
    public static final String OK       = "200 OK";
    public static final String EXC      = "500 Exception";
    public static final String NOTFOUND = "404 Not Found";

    public static String httpWrap(String code, String s, long len) {
        String temp = (s == null ? "" : s);
        temp = HtmlRenderUtils.htmlWrap(temp);
        long finallen = len == 0 ? temp.getBytes().length : len;
        String r = httpHeader(code) + temp;
        return r;
    }

    public static String httpHeader(String code){
        String ctype = "text/html";
        String r = "HTTP/1.1 " + code + "\r\nContent-Type: " + ctype + "\r\n\r\n";
        return r;
    }

    public static String httpHeader(String code, String contentType, String...tags) {
        String r = "HTTP/1.1 " + code + "\r\nContent-Type: " + contentType;
        for (String s : tags)
        r += "\r\n" + s;
        return r + "\r\n\r\n";
    }

    public static String httpWrapPic(String fname, long len) {
        String type = "image/gif";
        String ext = fname.toUpperCase();
        if (ext.endsWith(".GIF")) {
            type = "image/gif";
        } else if (ext.endsWith(".ICO")) {
            type = "image/x-icon";
        } else if (ext.endsWith(".JPG")) {
            type = "image/jpeg";
        }
        return httpWrapMimeType (type, len, "Expires: Thu, 15 Apr 2010 20:00:00 GMT");
    }

    public static String httpWrapOtherFile(String fname, long len) {
        String type = "text/html";
        String ext = fname.toUpperCase();
        if (ext.endsWith(".JS")) {
            type = "text/javascript";
        } else if (ext.endsWith(".XML")) {
            type = "application/xml";
        } else if (ext.endsWith(".CSS")) {
            type = "text/css";
        } else if (ext.endsWith(".WMV")) {
            type = "video/x-ms-wmv";
        } else {
        }
        return httpWrapMimeType (type, len);
    }

    public static String httpWrapMimeType(String type, long len, String...fields) {
//        return "HTTP/1.1 200 OK\r\nContent-Type: " + type + "\r\n\r\n";
        return httpHeader(OK, type, fields);
    }

    public static boolean isOtherFile(String fname) {
        String ext = fname.toUpperCase();
        return ext.endsWith(".JS") || ext.endsWith(".JS") || ext.endsWith(".XML") || ext.endsWith(".CSS")|| ext.endsWith(".WMV");
    }

    public static boolean isImage(String fname) {
        String ext = fname.toUpperCase();
        return ext.endsWith(".GIF") || ext.endsWith(".JPG")
                || ext.endsWith(".PNG") || ext.endsWith(".ICO");
    }

}