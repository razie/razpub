/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.comms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.razie.pub.base.data.ByteArray;
import com.razie.pub.base.log.CommRtException;
import com.razie.pub.base.log.Log;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;

/**
 * communications utils
 * 
 * TODO detailed docs
 * 
 * @author razvanc99
 * 
 */
public class Comms {

    /**
     * Stream the response of a URL.
     * 
     * @param url can be local or remote
     * @return a string containing the text read from the URL. can be the result of a servlet, a web
     *         page or the contents of a local file. It's null if i couldn't read the file.
     */
    public static InputStream streamUrl(String url) {
        try {
            InputStream in = null;
            if (url.startsWith("file:")) {
                in = (new URL(url)).openStream();
            } else if (url.startsWith("http:")) {
                URLConnection uc = (new URL(url)).openConnection();
                logger.trace(3, "hdr: ", uc.getHeaderFields());
                String resCode = uc.getHeaderField(0);
                in = uc.getInputStream();
                if (!resCode.endsWith("200 OK")) {
                    String msg = "Could not fetch data from url " + url + ", resCode=" + resCode;
                    logger.trace(3, msg);
                    CommRtException rte = new CommRtException(msg);
                    if (uc.getContentType().endsWith("xml")) {
                        DOMParser parser = new DOMParser();
                        try {
                            parser.parse(new InputSource(in));
                        } catch (SAXException e) {
                            RuntimeException iex = new CommRtException("Error while processing document at "
                                    + url);
                            iex.initCause(e);
                            throw iex;
                        }
                    }
                    throw rte;
                }
            } else {
                File file = new File(url);
                in = file.toURL().openStream();
            }
            return in;
        } catch (MalformedURLException e) {
            RuntimeException iex = new IllegalArgumentException();
            iex.initCause(e);
            throw iex;
        } catch (IOException e1) {
            // server/node down
            CommRtException rte = new CommRtException("Connection exception");
            rte.initCause(e1);
            throw rte;
        }
    }

    /**
     * read the given stream into a String and return the string. It will read and concatenate
     * chunks of 100 bytes.
     * 
     * @param fis an input stream
     * @return a string containing the text read from the stream. It's null if i couldn't read the
     *         file.
     */
    public static String readStream(InputStream fis) {
        try {
            byte[] buff = new byte[ByteArray.BUFF_QUOTA];
            int n = 0;
            ByteArray xml = new ByteArray();
            while ((n = fis.read(buff, 0, ByteArray.BUFF_QUOTA)) > 0) {
                xml.append(buff, n);
            }
            return xml.toString();
        } catch (Exception e) { // an error occurs ...
            throw new RuntimeException("Cannot read from input stream ...", e);
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                // do nothing here ...
            }
        }
    }

    /**
     * Serialize to string the response of a URL.
     * 
     * @param url can be local or remote
     * @return a string containing the text read from the URL. can be the result of a servlet, a web
     *         page or the contents of a local file. It's null if i couldn't read the file.
     */
    public static String readUrl(String url) {
        InputStream s = streamUrl(url);
        if (s == null) {
            return null;
        }
        return readStream(s);
    }

    static Log logger = Log.Factory.create(Comms.class.getName());
}
