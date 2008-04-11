package com.razie.pub.hframe.resources;

import java.io.IOException;
import java.util.Properties;

/**
 * just to proxy/wrap icons functionality, in case i introduce "themes" later - a theme would be a
 * different icons.properties file for now
 * 
 * if you want to use this, have an icons.properties at the root in the classpath
 * 
 * TODO implement registry of cascaded property files etc
 * 
 * @author razvanc99
 * 
 */
public class RazIconRes {
    public static String            curTheme = "icons.properties";
    public static String            getPictureService = "/classpath/public/pics/";
    static Properties props    = new Properties();

    public static void init() throws IOException {
        props.load(RazIconRes.class.getClassLoader().getResource(curTheme).openStream());
    }

    public static String getIconFile(RazIcons icon) {
        return getIconFile(icon.name());
    }

    /** the actual url to pic or empty */
    public static String getIconFile(String icon) {
        if (icon == null || icon.length() <= 0)
            icon = RazIcons.UNKNOWN.toString();
        String f = props.getProperty(icon.toLowerCase());
        return f == null ? icon : getPictureService + f;
    }

    Properties p;
}
