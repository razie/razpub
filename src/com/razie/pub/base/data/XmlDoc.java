/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 * 
 * Note that this is based on work I did elsewhere:
 * 
 * Copyright 2006 - 2007 The Members of the OSS through Java(TM) Initiative. All rights reserved.
 * Use is subject to license terms.
 */
package com.razie.pub.base.data;

import java.io.File;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.razie.pub.FileUtils;
import com.razie.pub.base.log.Log;

/**
 * represents an xml document - generally for configuration.
 * 
 * @author razvanc
 */
public class XmlDoc {
    protected Document                   document;
    public URL                        myUrl;
    protected String                     name;
    protected Element                    root;
    protected Map<String, String>        prefixes         = null;                         // lazy
    public long                          fileLastModified = -1;
    private long                         lastChecked      = 0;
    // TODO use a reasonable interval, make configurable - maybe per db
    public long                          reloadMilis      = 1000 * 30;

    protected static Map<String, XmlDoc> allDocs          = new HashMap<String, XmlDoc>();

    /** add prefix to be used in resolving xpath in this doc */
    public void addPrefix(String s, String d) {
        if (prefixes == null)
            prefixes = new HashMap<String, String>();
        prefixes.put(s, d);
    }

    /** TEMP */
    public static void docAdd(String s, XmlDoc d) {
        allDocs.put(s, d);
    }

    /** TEMP */
    public static XmlDoc doc(String s) {
        XmlDoc d = allDocs.get(s);
        if (d != null)
            d.checkFile();
        return allDocs.get(s);
    }

    protected void checkFile() {
        if (this.reloadMilis > 0 && System.currentTimeMillis() - this.lastChecked >= this.reloadMilis) {
            File f = FileUtils.fileFromUrl(this.myUrl);
            long ft = f != null ? f.lastModified() : -1;
            if (ft != this.fileLastModified) {
                load(name, myUrl);
            }
        }
    }

    public void load(String name, URL url) {
        Log.logThis("XmlDoc:loading from URL=" + url);
        this.myUrl = url;
        this.name = name;
        this.document = RiXmlUtils.readXml(url);
        this.root = this.document.getDocumentElement();
        if (this.root != null && this.root.hasAttribute("razieReloadMillis")) {
            this.reloadMilis = Long.parseLong(this.root.getAttribute("razieReloadMillis"));
        }

        try {
            File f = FileUtils.fileFromUrl(url);
            fileLastModified = f != null ? f.lastModified() : -1;
            this.lastChecked = System.currentTimeMillis();
        } catch (Exception e) {
            Log.logThis("XMLDOC won't be refreshed automatically: Can't get datetime for file URL=" + url);
            this.reloadMilis = 0;
        }
    }

    protected void load(String name, Document d) {
        this.name = name;
        this.document = d;
        this.root = d.getDocumentElement();
    }

    /**
     * return a list of all elements in specified path, just their "name" attribute
     * 
     * @return never null
     */
    public List<String> list(String path) {
        List<String> ret = new ArrayList<String>();
        for (Element e : RiXmlUtils.getNodeList(root, path, null)) {
            ret.add(e.getAttribute("name"));
        }
        return ret;
    }

    /**
     * return a list of all elements in specified path
     * 
     * @return never null
     */
    public List<Element> listEntities(String path) {
        return RiXmlUtils.getNodeList(root, path, prefixes);
    }

    /**
     * return a list of all elements in specified path
     * 
     * @return never null
     */
    public static List<Element> listEntities(Element node, String path) {
        return RiXmlUtils.getNodeList(node, path, null);
    }

    /**
     * return a list of all elements in specified path
     * 
     * @return never null
     */
    public static List<Element> listEntities(Element node) {
        List<Element> list = new ArrayList<Element>();
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element) {
                list.add((Element) n);
            }
        }
        return list;
    }

    /**
     * get a specific element, by "name"
     * 
     * @param path identifies the xpath
     * @name identifies the name attribute of the element - could also be part of xpath instead
     * @return never null
     */
    public Element getEntity(String path) {
        return (Element) RiXmlUtils.getNode(root, path, prefixes);
    }

    /**
     * i.e. "/config/mutant/@localdir"
     * 
     * @param path identifies the xpath
     * @name identifies the name attribute of the element - could also be part of xpath instead
     * @return never null
     */
    public String getAttr(String path) {
        return RiXmlUtils.getStringValue(root, path, null);
    }

    public Document getDocument() {
        return this.document;
    }

    public static XmlDoc createFromString(String name, String str) {
        XmlDoc doc = new XmlDoc();
        Document d = RiXmlUtils.readXml(new StringBufferInputStream(str), "");
        doc.load(name, d);
        return doc;
    }
}
