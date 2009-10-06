/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.razie.pub.UnknownRtException;

/**
 * simple log proxy - log4j is dominant but then there's the JDK's log... this gives you the freedom
 * to use one or the other...or simply recode to use your own - if you hapen to use another
 * one...doesn't it suck when you use a library which writes to stdout?
 * 
 * create logs per class with the Factory. then invoke log() trace() or alarm().
 * 
 * if you're lazy, use the static Log.logThis()
 * 
 * $
 * 
 * @author razvanc99
 * 
 */
public class Log {

   private String         category;
   private String         component;
   public static String   program    = "dflt";
   public static int      MAXLOGS    = 1000;
   public static String[] lastLogs   = new String[MAXLOGS];
   public static int      curLogLine = 0;
   public static boolean  DEBUGGING  = false;

   public Log(String componentNm, String categoryNm) {
      this.category = categoryNm;
      this.component = componentNm;
   }

   public static void addLogLine(String line) {
      synchronized (lastLogs) {
         lastLogs[curLogLine] = line;
         curLogLine = (curLogLine + 1) % MAXLOGS;
      }
   }

   public static String[] getLastLogs(int howMany) {
      synchronized (lastLogs) {
         int theseMany = howMany;
         String[] ret;

         // find out how many we have
         if (lastLogs[MAXLOGS - 1] == null) {
            theseMany = howMany > curLogLine ? curLogLine : howMany;
            ret = new String[theseMany];
            int k = 0;
            for (int i = curLogLine - theseMany; k < theseMany; i++) {
               ret[k++] = lastLogs[i];
            }
         } else {
            // bounced
            theseMany = howMany > MAXLOGS ? MAXLOGS : howMany;
            ret = new String[theseMany];
            int k = 0;
            for (int i = theseMany - curLogLine; i >= 0 && i < MAXLOGS && k < theseMany; i++) {
               ret[k++] = lastLogs[i];
            }
            for (int i = curLogLine - (theseMany - k); i < curLogLine && k < theseMany; i++) {
               ret[k++] = lastLogs[i];
            }
         }
         return ret;
      }
   }

   public void log(String m, Throwable t) {
      log(m + " Exception: " + Exceptions.getStackTraceAsString(t));
   }

   public void log(Object... o) {
      String m = "";
      for (int i = 0; i < o.length; i++) {
         m += o[i].toString();
      }

      String msg = "LOG-" + program + "-" + component + "-" + category + ": " + m;
      System.out.println(msg);
      try {
         File f = new File("c:\\video\\raz\\mutant.log");
         FileOutputStream fos = new FileOutputStream(f);
         fos.write(msg.getBytes());
         fos.write('\n');
         fos.flush();
         fos.close();
      } catch (FileNotFoundException e) {
         // can't recurse to log...
         e.printStackTrace();
      } catch (IOException e) {
         // can't recurse to log...
         e.printStackTrace();
      }
      addLogLine(msg);
   }

   public void alarm(String m, Throwable... e) {
      log(m + (e.length <= 0 ? "" : Exceptions.getStackTraceAsString(e[0])));
   }

   /**
    * trace by concatenating the sequence of objects to String - this is the most efficient trace
    * since the strings will only be evaluated and concatenated if the trace is actually turned on
    */
   public void trace(int l, Object... o) {
      if (isTraceLevel(l)) {
         String m = "";
         for (int i = 0; i < o.length; i++) {
            m += (o[i] == null ? "null" : o[i].toString());
         }
         log(m);
      }
   }

   public boolean isTraceLevel(int l) {
      return DEBUGGING;
   }

   public static boolean isTraceOn() {
      return DEBUGGING;
   }

   public static void logThis(String m) {
      Factory.logger.log(m);
   }

   public static void traceThis(String m) {
      if (Factory.logger.isTraceLevel(1)) Factory.logger.log(m);
   }

   public static void traceThis(String m, Throwable t) {
      if (Factory.logger.isTraceLevel(1)) Factory.logger.log(m, t);
   }

   public static void logThis(String m, Throwable t) {
      Factory.logger.log(m + " Exception: " + Exceptions.getStackTraceAsString(t));
   }

   public static void alarmThis(String m, Throwable... e) {
      Factory.logger.alarm(m, e);
   }

   public static void alarmThisAndThrow(String m, Throwable... e) {
      // TODO i don't think this should log again...since it throws it, eh?
      Factory.logger.alarm(m, e);
      if (e.length > 0)
         throw new UnknownRtException(m, e[0]);
      else
         throw new UnknownRtException(m);
   }

   /**
    * helper to turn lists/arrays/maps into strings for nice logging
    * 
    * @param ret object to toString
    * @return either the new String or the original object if not recognized
    */
   @SuppressWarnings("unchecked")
   public static Object tryToString(String indent, Object ret) {
      return Log4j.tryToString(indent, ret);
   }

   /**
    * This is how you can use any underlying logging package: simply overwrite this with your own
    * factory before the thing starts (first thing in main())
    * 
    * TODO implement proper factory pattern
    */
   public static class Factory {

      public static Log logger = create("?", "DFLTLOG");

      public static Log create(String component, String categoryName) {
         return new Log4j(component, categoryName);
      }

      public static Log create(String categoryName) {
         return new Log4j("?", categoryName);
      }
   }
}
