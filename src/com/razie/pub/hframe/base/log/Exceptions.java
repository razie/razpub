/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.base.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

/**
 * Different utilities relating to Exception handling.
 * 
 * @author razvanc99
 */
public class Exceptions {
    /**
     * print exception to string
     */
    public static String getStackTraceAsString(Throwable aThrowable) {
        if (aThrowable != null) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            PrintWriter printWriter = new PrintWriter(byteStream, true);
            String stackTrace = null;

            aThrowable.printStackTrace(printWriter);

            printWriter.flush();
            stackTrace = byteStream.toString();
            printWriter.close();
            return stackTrace;
        } else {
            return null;
        }
    }

    /**
     * simply recurse to get the root cause
     */
    public static Throwable getRootCause(Throwable aThrowable) {
        Throwable root = aThrowable;

        while (root != null && root.getCause() != null) {
            root = root.getCause();
        }

        return root;
    }
}
