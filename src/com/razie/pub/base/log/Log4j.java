/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.base.log;

import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;

/**
 * the log4j implementation
 */
public class Log4j extends Log {
    /**
     * Build a local instance for an object of the specified class.
     * 
     * @param classNm name of the class to which the object for which the Log instance is created
     */
    public Log4j(String cn, String classNm) {
        super(cn, classNm);
        if (!Log4j.initialized) {
            init();
        }

        this.log4jLogger = Logger.getLogger(classNm);
    }

    /** Initialize with default logPattern and print on screen */
    public static void init() {
        URL aUrl = Log4j.class.getClassLoader().getResource("log4j.properties");
        if (aUrl != null) {
            Properties props = new Properties();
            try {
                PropertyConfigurator.configure(aUrl);
                props.load(aUrl.openStream());
            } catch (Exception ex) {
                System.out.println("Error: cannot open log file. Msg: " + ex.getMessage());
            }

            System.out.println("Log initialized from: " + (aUrl == null ? "null" : aUrl.toString()));
            Log4j.initialized = true;
        } else {
            Layout layout = new PatternLayout("DFLTLOG> %p %d{HH:mm:ss} %c> %m%n");
            try {
                Appender fileAppender = new ConsoleAppender(layout);
                BasicConfigurator.configure(fileAppender);
            } catch (Exception ex) {
                System.out.println("Error: cannot open log file. Msg: " + ex.getMessage());
            }
            System.out.println("Log initialized from default pattern - no properties files");
            Log4j.initialized = true;
        }
    }

    public void alarm(String o, Throwable... e) {
       String m = o + (e.length <= 0 ? "" : Log.getStackTraceAsString(e[0]));
       log4jLogger.fatal(m);
       addLogLine(m);
    }

    public void log(Object... o) {
        String m = "";
        for (int i = 0; i < o.length; i++)
            m += o[i] != null ? o[i].toString() : "<NULL>";
        log4jLogger.info(m);
        addLogLine(m);
    }

    public void log(String msg, Throwable e) {
        String m = msg + " Exception: " + e.toString();
        log4jLogger.info(m, e);
        addLogLine(m);
    }

    public boolean isTraceLevel(int level) {
        return log4jLogger.isDebugEnabled();
    }

    /**
     * @param level is the log level
     * @param msg is the message to be logged
     * @param e is the exception's whose stack trace is logged
     */
    public void trace(int level, String msg, Throwable e) {
        if (isTraceLevel(level)) {
            String m = msg + " Exception: " + e.toString();
            log4jLogger.debug(m, e);
            addLogLine(m);
        }
    }

    public void trace(int l, Object... o) {
        if (isTraceLevel(l)) {
            String m = "";
            for (int i = 0; i < o.length; i++)
                m += (o[i] == null ? "null" : o[i].toString());
            log4jLogger.debug(m);
            addLogLine(m);
        }
    }

    /**
     * @param msg is the message to be logged
     * @param e is the exception to be logged...
     */
    public void alarm(String msg, Throwable e) {
        if (e != null) {
            msg += " Exception: " + e.toString();
        }

        Priority log4jPriority = Priority.FATAL;
        // log4jPriority = Priority.ERROR;
        // log4jPriority = Priority.WARN;

        if (e != null) {
            log4jLogger.log(log4jPriority, msg, e);
        } else {
            log4jLogger.log(log4jPriority, msg);
        }

        addLogLine(msg);
    }

    private static boolean initialized = false;

    /** The LOG4J category. Contains the classname of the logging class. */
    private Logger         log4jLogger = null;
}
