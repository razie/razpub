/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.hframe.base.data;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import eu.medsea.util.MimeUtil;

/**
 * a bunch of http utils ?
 * 
 * @author razvanc99
 * 
 */
public class HttpUtils {

    public static String toUrlEncodedString(String ref) {
        try {
            return URLEncoder.encode(ref, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return ref;
        }
    }

    public static String fromUrlEncodedString(String ref) {
        try {
            return URLDecoder.decode(ref, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return ref;
        }
    }

    /**
     * Using JMimeMagic Checking the file extension is not a very strong way to determine the file
     * type. A more robust solution is possible with the JMimeMagic library. JMimeMagic is a Java
     * library (LGLP licence) that retrieves file and stream mime types by checking magic headers. //
     * snippet for JMimeMagic lib // http://sourceforge.net/projects/jmimemagic/
     * 
     * Magic parser = new Magic() ; // getMagicMatch accepts Files or byte[], // which is nice if
     * you want to test streams MagicMatch match = parser.getMagicMatch(new File("gumby.gif"));
     * System.out.println(match.getMimeType()) ;
     * 
     * Thanks to Jean-Marc Autexier and sygsix for the tip! Using mime-util Another tool is
     * mime-util. This tool can detect using the file extension or the magic header technique. //
     * snippet for mime-util lib // http://sourceforge.net/projects/mime-util
     * 
     * public static final String UNKNOWN_MIME_TYPE="application/x-unknown-mime-type"; ... String
     * mimeType = MimeUtil.getMagicMimeType(file); if(mimeType == null) mimeType =
     * UNKNOWN_MIME_TYPE;
     */
    public static String getMimeType(String file) {
        // snippet for mime-util lib
        // http://sourceforge.net/projects/mime-util
        String mimeType = MimeUtil.getMimeType(file);
        if (mimeType == null)
            mimeType = UNKNOWN_MIME_TYPE;
        return mimeType;
    }

    public static final String UNKNOWN_MIME_TYPE = "application/x-unknown-mime-type";

}
