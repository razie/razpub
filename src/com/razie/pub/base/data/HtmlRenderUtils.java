/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base.data;

import com.razie.pub.base.NoStatic;

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
    static NoStatic<HtmlTheme> theme = new NoStatic<HtmlTheme>("razie.theme", new DarkTheme());
    
    /** wrap contents as an html document */
    public static String htmlWrap(String s) {
        String r = s;// GRef.toUrlEncodedString(s);
        return htmlHeader() + r + htmlFooter();
    }

    /** wrap contents as an html document */
    public static String htmlHeader(String... metas) {
        String s = theme.get().get(HtmlTheme.HEADSTART);
        if (metas.length > 0) {
            s += "<head>";
            for (String m : metas)
                s += m;
            s += "</head>";
        }
        return s + theme.get().get(HtmlTheme.BODYSTART);
    }

    /** wrap contents as an html document */
    public static String htmlFooter() {
        return theme.get().get(HtmlTheme.BODYEND) + theme.get().get(HtmlTheme.HEADSTART);
    }

    /** wrap contents as an html document */
    public static String textToHtml(String s) {
//        String r = s;// GRef.toUrlEncodedString(s);
//        r = r == null ? "null" : r.replaceAll("\n", "<br>");
//        return r == null ? "null" : r.replaceAll("\t", "&nbsp;&nbsp;&nbsp;");
        return stringToHTMLString (s);
    }

    // from http://www.rgagnon.com/javadetails/java-0306.html
    private static String stringToHTMLString(String string) {
        StringBuffer sb = new StringBuffer(string.length());
        // true if last char was blank
        boolean lastWasBlankChar = false;
        int len = string.length();
        char c;

        for (int i = 0; i < len; i++)
            {
            c = string.charAt(i);
            if (c == ' ') {
                // blank gets extra work,
                // this solves the problem you get if you replace all
                // blanks with &nbsp;, if you do that you loss 
                // word breaking
                if (lastWasBlankChar) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                    }
                else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                    }
                }
            else {
                lastWasBlankChar = false;
                //
                // HTML Special Chars
                if (c == '"')
                    sb.append("&quot;");
                else if (c == '&')
                    sb.append("&amp;");
                else if (c == '<')
                    sb.append("&lt;");
                else if (c == '>')
                    sb.append("&gt;");
                else if (c == '\n')
                    // Handle Newline
//                    sb.append("&lt;br/&gt;");
                    sb.append("<br/>");
                else if (c == '\t')
                    sb.append("&nbsp;&nbsp;&nbsp;");
                else {
                    int ci = 0xffff & c;
                    if (ci < 160 )
                        // nothing special only 7 Bit
                        sb.append(c);
                    else {
                        // Not 7 Bit use the unicode system
                        sb.append("&#");
                        sb.append(new Integer(ci).toString());
                        sb.append(';');
                        }
                    }
                }
            }
        return sb.toString();
    }
    
    public static class HtmlTheme {
        protected static final int BODYSTART = 0;
        protected static final int BODYEND   = 1;
        protected static final int HEADSTART = 2;
        protected static final int HEADEND   = 3;
        protected static final int LAST      = 3;

        String[]            patterns  = { "<body.*>", "</body>", "<html.*>", "</html>" };
        String[]            tags      = { "<body>", "</body>", "<html>", "</html>" };

        protected String get(int what) {
            return tags[what];
        }
    }

    /** a simple, css-based theme to be used by the sample server */
    public static class DarkTheme extends HtmlTheme {
        static String[] tags = {
                                     "<head><link rel=\"stylesheet\" type=\"text/css\" href=\"/classpath/com/razie/pub/http/test/style.css\" /></head><body link=\"yellow\" vlink=\"yellow\">",
                                     "</body>", "<html>", "</html>" };

        protected String get(int what) {
            return tags[what];
        }
    }

    public static String replace(String input) {
        String output = input;
        for (int i = 0; i <= HtmlTheme.LAST; i++) {
            output = output.replace(theme.get().patterns[i], theme.get().get(i));
        }
        return output;
    }

    public static void setTheme(HtmlTheme theTheme) {
        theme.set(theTheme);
    }
}
