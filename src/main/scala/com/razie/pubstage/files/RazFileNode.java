package com.razie.pubstage.files;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class RazFileNode {
    public String location;
    public String name;
    public String localPath;
    public URL    localUrl;

    public RazFileNode(File f) {
        location = "?";
        name = f.getName();
        try {
            localPath = f.getCanonicalPath();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        try {
            localUrl = f.toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
