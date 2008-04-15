/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.hframe.base.data;

/**
 * a bunch of utilities related to rendering html content, including minimal support for themes via
 * stylesheets, if so supported by the server.
 * 
 * <p>
 * To customize this, derive your own theme and set it before starting the server.
 * 
 * TODO i only support one theme per JVM which maybe it's what i want :) to ensure consistency of
 * all presentation but maybe we should support a them per server/service?
 * 
 * @author razvanc99
 * 
 */
public class HtmlRenderUtils {
    static HtmlTheme theme = new HtmlTheme();

    /** wrap contents as an html document */
    public static String htmlWrap(String s) {
        String r = s;// GRef.toUrlEncodedString(s);
        return htmlHeader() + r + htmlFooter();
    }

    /** wrap contents as an html document */
    public static String htmlHeader(String... metas) {
        String s = theme.get(HtmlTheme.HEADSTART);
        if (metas.length > 0) {
            s += "<head>";
            for (String m : metas)
                s += m;
            s += "</head>";
        }
        return s + theme.get(HtmlTheme.BODYSTART);
    }

    /** wrap contents as an html document */
    public static String htmlFooter() {
        return theme.get(HtmlTheme.BODYEND) + theme.get(HtmlTheme.HEADSTART);
    }

    /** wrap contents as an html document */
    public static String textToHtml(String s) {
        String r = s;// GRef.toUrlEncodedString(s);
        r = r.replaceAll("\n", "<br>");
        return r;
    }

    public static class HtmlTheme {
        public static final int BODYSTART = 0;
        public static final int BODYEND   = 1;
        public static final int HEADSTART = 2;
        public static final int HEADEND   = 3;
        public static final int LAST      = 3;

        static String[]         patterns  = { "<body.*>", "</body>", "<html.*>", "</html>" };
        static String[]         tags      = { "<body>", "</body>", "<html>", "</html>" };

        public String get(int what) {
            return tags[what];
        }
    }

    public static String replace(String input) {
        String output = input;
        for (int i = 0; i <= HtmlTheme.LAST; i++) {
            output = output.replace(theme.patterns[i], theme.get(i));
        }
        return output;
    }

    public static void setTheme(HtmlTheme theTheme) {
        theme = theTheme;
    }
}
