/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.base.data;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A bunch of utilities for XML parsing.
 * 
 * @Id $Id: 1.3 $
 */
public class BaseXmlUtils {
    /**
     * missing required attrs cause a RuntimeException
     */
    public static String getReqAttr(Element element, String attrNm) {
        if (!element.hasAttribute(attrNm)) {
            String err = "XML Error: " + element.getNodeName() + " missing attr : " + attrNm;
            throw new RuntimeException(err);
        }
        return element.getAttribute(attrNm);
    }

    /**
     * helper to get an optional attr
     */
    public static String getOptAttr(Element parent, String attrNm) {
        String val = null;
        if (parent.hasAttribute(attrNm)) {
            val = parent.getAttribute(attrNm);
        }
        return val;
    }

    public static String getOptNodeVal(Element elem) {
        String ret = null;
        if (elem != null) {
            for (Node child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
                if (child.getNodeType() == Node.CDATA_SECTION_NODE || child.getNodeType() == Node.TEXT_NODE) {
                    ret = ret == null ? child.getNodeValue() : ret + child.getNodeValue();
                }
            }
        }
        return ret;
    }

    /**
     * will concatenate all CDATA nodes under the element
     * 
     * @param elem
     * @return null or concatenate all CDATA nodes under the element
     */
    public static String getOptCData(Element elem) {
        String val = null;
        if (elem != null) {
            for (Node child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
                if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
                    val = val == null ? child.getNodeValue() : val + child.getNodeValue();
                }
            }
        }
        return val;
    }

    /** same as setOptAttr, but the value is that of a child node */
    public static Element setOptNodeVal(Element parent, String nodeNm, String ns, String val) {
        Element childElem = null;
        if (val != null && val.length() > 0) {
            childElem = setOptCData(parent.getOwnerDocument(), nodeNm, ns, val);
            parent.appendChild(childElem);
        }
        return childElem;
    }

    /** same as setOptAttr, but the value is that of a child node */
    public static Element setOptCData(Document document, String nodeNm, String ns, String val) {
        Element childElem = null;
        if (val != null && val.length() > 0) {
            childElem = document.createElementNS(ns, nodeNm);
            CDATASection cdata = document.createCDATASection(val);
            childElem.appendChild(cdata);
        }
        return childElem;
    }

    /**
     * set an optional attribute...
     */
    public static void setOptAttr(Element element, String attrNm, String attrVal) {
        if (attrVal != null) {
            element.setAttribute(attrNm, attrVal);
        }
    }

    /**
     * required attributes must be there
     */
    public static void setReqAttr(Element element, String attrNm, String attrVal) {
        if (attrVal != null) {
            element.setAttribute(attrNm, attrVal);
        } else {
            element.setAttribute(attrNm, "");
        }
    }
}
