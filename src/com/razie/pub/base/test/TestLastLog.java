package com.razie.pub.base.test;

import com.razie.pub.base.log.Log;

import junit.framework.TestCase;

public class TestLastLog extends TestCase {
    public static void testA1() {
        for (int i = 0; i < 50; i++)
            Log.logThis("log line " + i);
    }
    public static void testAGetLess() {
        String[] lines = Log.getLastLogs(20);
        System.out.println (Log.tryToString("   ", lines));
        assertTrue ("didn't get as many...", lines.length == 20);
    }
    public static void testBGetMore() {
        String[] lines = Log.getLastLogs(70);
        System.out.println (Log.tryToString("   ", lines));
        assertTrue ("didn't get as many...", lines.length == 50);
    }
    public static void testC1() {
        for (int i = 51; i < Log.MAXLOGS; i++)
            Log.logThis("log line M " + i);
    }
    public static void testCWrapped() {
        String[] lines = Log.getLastLogs(200);
        System.out.println (Log.tryToString("   ", lines));
        assertTrue ("didn't get as many...", lines.length == 200);
    }
}
