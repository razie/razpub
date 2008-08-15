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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * represents an xml document - generally for configuration.
 * 
 * @author razvanc
 */
public class XmlDoc {
    protected Document                   document;
    protected String                     name;
    protected Element                    root;

    protected static Map<String, XmlDoc> allDocs = new HashMap<String, XmlDoc>();

    /** TEMP */
    public static void docAdd(String s, XmlDoc d) {
        allDocs.put(s, d);
    }

    /** TEMP */
    public static XmlDoc doc(String s) {
        return allDocs.get(s);
    }

    public void load(String name, URL url) {
        this.name = name;
        this.document = RiXmlUtils.readXml(url);
        this.root = this.document.getDocumentElement();
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
        return RiXmlUtils.getNodeList(root, path, null);
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
        return (Element) RiXmlUtils.getNode(root, path, null);
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
}