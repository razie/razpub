package com.razie.pub.webui;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import razie.base.ActionItem;
import razie.base.AttrAccess;
import razie.base.AttrAccessImpl;
import razie.base.ScriptContext;
import razie.draw.DrawSequence;
import razie.draw.DrawTable;
import razie.draw.Drawable;
import razie.draw.DrawableSource;
import razie.draw.Renderer;
import razie.draw.Technology;

import com.razie.pub.base.data.RiXmlUtils;
import com.razie.pub.base.data.XmlDoc;
import com.razie.pub.base.data.XmlDoc.Reg;
import com.razie.pub.base.log.Log;
import com.razie.pub.comms.ServiceActionToInvoke;
import com.razie.pub.draw.widgets.DrawScript;

/**
 * loads the presentation configuration - basically each page can be customized via this helper.
 * 
 * TODO presentation must be plugged in as a component into the page server and other components.
 * Currently it's hardcoded in the CmdGET or something...
 * 
 * @author razvanc
 * @version $Id$
 * 
 */
public class MutantPresentation extends PageMaker {

    private static MutantPresentation singleton;
    private long                      lastModified;
    private AttrAccess                pages     = new AttrAccessImpl();
    // name,toRefresh
    private Map<String, Long>         filenames = new HashMap<String, Long>();

    public static final String        XMLDOC    = "user.xml";

    private MutantPresentation(ScriptContext parent) {
        super(parent);
    }

    public static void addPresentation(String filename) {
        if (singleton == null) {
            singleton = new MutantPresentation(ScriptContext.Impl.global());
        }

        refresh(filename);
    }

    public static Object paintPageCode(String clsname) {
        Object d;
        try {
            d = (DrawableSource) Class.forName(clsname).newInstance();
        } catch (Exception e1) {
            d = e1;
        }
        
        return d;
    }

    protected static void refresh(String filename) {
       if (Reg.doc(filename) != null) {
        for (Element page : XmlDoc.listEntities(Reg.doc(filename).getEntity("/config/presentation"))) {
            Object d;
            if (page.hasAttribute("code")) {
                d = paintPageCode (page.getAttribute("code"));
            } else {
                d = singleton.makeElement(page);
            }
            singleton.pages.setAttr(page.getNodeName(), d);
            singleton.pages.setAttr(page.getNodeName().replace(".", "_"), d);
        }
        singleton.setAttr("presentation", singleton);

        singleton.filenames.put(filename, Reg.doc(filename).fileLastModified);
       } else Log.alarmThis ("ERROR_XMLDOCNOTFOUND: "+filename);
    }

    public static MutantPresentation getInstance() {
        if (singleton == null) {
            singleton = new MutantPresentation(ScriptContext.Impl.global());
        }

        // trigger refresh
        // TODO 3 PERF don't trigger eery time - less often
        for (String s : singleton.filenames.keySet()) {
            Reg.doc(s);
            if (singleton.filenames.get(s) != Reg.doc(s).fileLastModified) {
                refresh(s);
            }
        }

        return singleton;
    }

    private Drawable makeElement(Element e) {
        DrawSequence page = new DrawSequence();

        for (Element c : XmlDoc.listEntities(e)) {
            Object d = readDrawable(c);
            page.write(d);
        }

        return page;
    }

    public Object getAttr(String name) {
        Object o = super.getAttr(name);
        if (o instanceof DrawableSource) {
            return ((DrawableSource) o).makeDrawable();
        } else {
            return o;
        }
    }

    private Object readDrawable(Element e) {
        if (e.getNodeName().equals("action")) {
            ActionItem ai = new ActionItem(e.getAttribute("cmd"), e.getAttribute("icon"));
            ai.label = e.getAttribute("label");
            if (e.hasAttribute("svc"))
                return new ServiceActionToInvoke(e.getAttribute("svc"), ai);
            else
                return new ServiceActionToInvoke("cmd", ai);
        } else if (e.getNodeName().equals("browse")) {
            ActionItem ai = new ActionItem(e.getAttribute("cmd"), e.getAttribute("icon"));
            ai.label = e.getAttribute("label");
            BrowseToPage ati = new BrowseToPage(ai, e.getAttribute("page"));
            return ati;
        } else if (e.getNodeName().equals("element")) {
            return getAttr(e.getAttribute("ref"));
        } else if (e.getNodeName().equals("script")) {
            return new DrawScript(e.getAttribute("lang"), RiXmlUtils.getOptNodeVal(e), this);
        } else if (e.getNodeName().equals("table")) {
            DrawTable table = new DrawTable();
            if (e.hasAttribute("packed"))
                table.packed = Boolean.valueOf(e.getAttribute("packed"));
            if (e.hasAttribute("col"))
                table.prefCols = Integer.valueOf(e.getAttribute("col"));

            for (Element c : XmlDoc.listEntities(e)) {
                Object d = readDrawable(c);
                table.write(d);
            }
            return table;
        }
        return "UNKNOWN pres element: " + e.getNodeName();
    }

    public String page(String name) {
        Object o = pages.isPopulated(name) ? pages.getAttr(name) : pages.getAttr(name.replace(".", "_"));
        String line = (String) Renderer.Helper.draw(o, Technology.HTML, null);
        return line;
    }
}
