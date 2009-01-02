/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.assets;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import com.razie.pub.assets.AssetMgr.Meta;
import com.razie.pub.base.ActionItem;
import com.razie.pub.base.AttrAccess;
import com.razie.pub.base.data.HtmlRenderUtils;
import com.razie.pub.base.data.HttpUtils;
import com.razie.pub.base.data.MimeUtils;
import com.razie.pub.comms.ActionToInvoke;
import com.razie.pub.comms.Agents;
import com.razie.pub.comms.LightAuth;
import com.razie.pub.comms.ServiceActionToInvoke;
import com.razie.pub.draw.DrawList;
import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.Drawable;
import com.razie.pub.draw.Renderer;
import com.razie.pub.draw.Renderer.Technology;
import com.razie.pub.resources.RazIconRes;
import com.razie.pub.resources.RazIcons;

/**
 * a brief description of an asset. It includes enough information to allow users to initiate
 * actions on this (i.e. play or viewDetails). It includes a favorite player (optional, though).
 * 
 * It does not necessarily identify a file or an URL that links to the asset. But rather enough
 * information that an inventory based command back to the server of the append with this REF can
 * identify the asset and for instance start streaming it.
 * 
 * It normally includes an icon, an image, a name, small and large descriptions, plus whatever other
 * information is needed.
 * 
 * TODO AssetBrief should be final and immutable - for now i still use the SdkAssetBrief in the
 * mutant...
 * 
 * TODO split into a generic assetbrief and a fileassetbrief - for files, with directories etc.
 * these are not applicable to say web assets...
 * 
 * @author razvanc99
 * 
 */
public class AssetBrief extends AttrAccess.Impl implements AttrAccess, Drawable, JSONString {

    protected String               name;
    protected String               fileName;
    protected String               icon;
    protected String               image;
    protected String               briefDesc;
    protected String               largeDesc;
    private String                 localDir;
    private AssetKey               ref;
    public String                  player;
    public String                  parentID    = "";
    protected AssetKey             series      = null;
    public long             size      = -1;

    public DetailLevel             detailLevel = DetailLevel.LIST;

    /** standard actions on assets */
    public static final ActionItem DETAILS     = new ActionItem("details", RazIcons.UNKNOWN);
    public static final ActionItem PLAY        = new ActionItem("play", RazIcons.PLAY);
    public static final ActionItem STREAM      = new ActionItem("stream", RazIcons.PLAY);

    /**
     * detail levels are:
     * 
     * <ul>
     * <li>BRIEFLIST -
     * <li>LIST -
     * <li>LARGE -
     * <li>FULL -
     *</ul>
     * 
     * @author razvanc
     * 
     */
    public static enum DetailLevel {
        BRIEFLIST, LIST, LARGE, FULL
    }

    public AssetBrief() {
    }

    public AssetBrief(String name) {
        this.setName(name);
    }

    /** shortcut to render self - don't like controllers that much */
    public Object render(Technology t, DrawStream stream) {
        return Renderer.Helper.draw(this, t, stream);
    }

    @Override
    public String toString() {
        String s = "EntityBrief{";
        s += "\n   name=" + getName();
        s += "\n   fileName=" + getFileName();
        s += "\n   icon=" + getIcon();
        s += "\n   image=" + getImage();
        s += "\n   briefDesc=" + getBriefDesc();
        s += "\n   largeDesc=" + getLargeDesc();
        s += "\n   urlForDetails=" + getUrlForDetails().makeActionUrl();
        s += "\n   localDir=" + getLocalDir();
        s += "\n   ref=" + getKey();
        s += "\n   series=" + (series == null ? "" : getSeries());
        return s + "\n}";
    }

    public String getMimeType() {
        String fname = getLocalDir() + getFileName();
        String mimeType = MimeUtils.getMimeType(fname);
        return mimeType;
    }

    public String toUpnpItem(String parentID) {
        String s = "\n<item id=\"" + getKey().toUrlEncodedString() + "\" parentID=\"" + parentID
                + "\" restricted=\"false\"" + ">\n";

        AttrAccess a = new AttrAccess.Impl();

        a.setAttr("dc\\:title", getName());
        a.setAttr("upnp\\:class", upnptypes.get(getKey().getType()));
        // if (series != null)
        // a.setAttr("series", getSeries().toUrlEncodedString());

       // a.setAttr("upnp\\:genre", "");
       // a.setAttr("upnp\\:longDescription", getLargeDesc());
        a.setAttr("dc\\:description", getBriefDesc());

        //a.setAttr("upnp\\:storageMedium", "");
        //a.setAttr("upnp\\:channelName", "");

        s += a.toXml();

        String p = "\n<res protocolInfo=\"http-get:*:" + getMimeType() + ":*\" size=\"200000\">"
                + getUrlForStreaming().makeActionUrl() + "</res>";

        return s + p + "\n</item>";
    }

    // TODO
    public String toRssMediaItem() {
        String s = "\n<item>\n";

        AttrAccess a = new AttrAccess.Impl();

        a.setAttr("title", getName());
        a.setAttr("link", "?");

        a.setAttr("upnp:class", "object.item.videoItem.movie");
        // if (series != null)
        // a.setAttr("series", getSeries().toUrlEncodedString());

        a.setAttr("upnp:genre", "");
        a.setAttr("upnp:longDescription", getLargeDesc());
        a.setAttr("dc:description", getBriefDesc());

        a.setAttr("upnp:storageMedium", "");
        a.setAttr("upnp:channelName", "");

        s += a.toXml();

        // String p = "\n<res protocolInfo=\"http-get:*:" + getMimeType() + ":*\" size=\"200000\">"
        // + "http://"
        // + Devices.getMyUrl() + "/mutant/stream?ref=" + getRef().toUrlEncodedString() + "</res>";
        String p = "\n<res protocolInfo=\"http-get:*:" + getMimeType() + ":*\" size=\"200000\">"
                + getUrlForStreaming().makeActionUrl() + "</res>";

        return s + p + "\n</item>";
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param briefDesc the briefDesc to set
     */
    public void setBriefDesc(String briefDesc) {
        this.briefDesc = briefDesc;
    }

    /**
     * @return the briefDesc
     */
    public String getBriefDesc() {
        return briefDesc;
    }

    /**
     * @param largeDesc the largeDesc to set
     */
    public void setLargeDesc(String largeDesc) {
        this.largeDesc = largeDesc;
    }

    /**
     * @return the largeDesc
     */
    public String getLargeDesc() {
        return largeDesc;
    }

    /**
     * an URL to a place where you can find more details about this asset...
     * 
     * TODO define the protocol to serve/get/use the description
     * 
     * @return the urlForDetails
     */
    public ActionToInvoke getUrlForDetails() {
        if (this.getSeries() != null) {
            // FIXME do i incorporate series into the baseline assets?
            return new ServiceActionToInvoke("assets", DETAILS, "ref", getKey(), "series", this.getSeries()
                    .toString());
        } else {
            // return new AssetActionToInvoke(getKey(), DETAILS);
            return new ServiceActionToInvoke("assets", DETAILS, "ref", getKey());
            // FIXME must use only asset stuff, no mutant specifics...
        }
    }

    /**
     * for streamable assets (movie, music, photo) an URL you can use to stream the asset
     * 
     * TODO define the protocol to serve/get/use the description
     * 
     * @return a url you can use to stream the asset or null if streaming is not supported. the url
     *         you can use to stream the file - it includes the filename just so IE can figure out
     *         what to do with it (open the right player etc)...
     */
    public ActionToInvoke getUrlForStreaming() {
        if (this.getSeries() != null) {
            // FIXME do i incorporate series into the baseline assets?
            return new ActionToInvoke(new ActionItem(AssetBrief.STREAM.name + "/" + this.getFileName(),
                    RazIcons.DOWNLOAD), "ref", getKey().toUrlEncodedString(), "series", this.getSeries()
                    .toString());
        } else {
            return new ActionToInvoke(new ActionItem(AssetBrief.STREAM.name + "/" + this.getFileName(),
                    RazIcons.DOWNLOAD), "ref", getKey().toUrlEncodedString());
        }
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param localDir the localDir to set
     */
    public void setLocalDir(String localDir) {
        this.localDir = localDir;
    }

    /**
     * @return the localDir
     */
    public String getLocalDir() {
        return localDir;
    }

    /**
     * @param ref the ref to set
     */
    public void setKey(AssetKey ref) {
        this.ref = ref;
    }

    /**
     * @return the ref
     */
    public AssetKey getKey() {
        return this.ref;
    }

    /**
     * @param series the series to set
     */
    public void setSeries(AssetKey series) {
        this.series = series;
    }

    /**
     * @return the series
     */
    public AssetKey getSeries() {
        return series;
    }

    /**
     * @param b
     * @return
     */
    @Override
    public String toXml() {
        String s = "<SdkAssetBrief>";
        s += toAA().toXml();
        return s + "</SdkAssetBrief>";
    }

    /**
     * @param b
     * @return
     */
    protected AttrAccess toAA() {
        AttrAccess a = this;

        a.setAttr("name", getName());
        a.setAttr("icon", icon);
        a.setAttr("image", getImage());
        a.setAttr("briefDesc", getBriefDesc());
        a.setAttr("largeDesc", getLargeDesc());
        a.setAttr("urlForDetails", getUrlForDetails().makeActionUrl());
        a.setAttr("ref", getKey().toUrlEncodedString());

        a.setAttr("localDir", getLocalDir());
        a.setAttr("fileName", getFileName());

        if (series != null)
            a.setAttr("series", getSeries().toUrlEncodedString());

        return a;
    }

    /**
     * @param b
     * @return
     */
    public String toJSONString() {
        AttrAccess a = toAA();
        return a.toJson(new JSONObject()).toString();
        // return new JSONObject (this).toString();
        // TODO add the rest of the attributes, i.e. url for details or streaming
    }

    /**
     * @param b
     * @return
     */
    public static AssetBrief fromJson(JSONObject a) {
        AssetBrief brief = new AssetBrief();
        try {
            brief.setName(a.getString("name"));
            brief.setIcon(a.optString("icon"));
            brief.setImage(a.optString("image"));
            brief.setFileName(a.getString("fileName"));
            brief.setBriefDesc(a.getString("briefDesc"));
            brief.setLargeDesc(a.optString("largeDesc"));
            // brief.setUrlForDetails(a.getString("urlForDetails"),
            // getUrlForDetails().makeActionUrl());
            brief.setLocalDir(a.optString("localDir"));
            brief.setKey(AssetKey.fromString(HttpUtils.fromUrlEncodedString(a.getString("ref"))));
            if (a.has("series"))
                brief.setSeries(AssetKey.fromString(HttpUtils.fromUrlEncodedString(a.getString("series"))));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return brief;
    }

    public Renderer<?> getRenderer(Technology technology) {
        return new MyRender();
    }

    /**
     * make a url for the image/icon representing this asset...gives you something to display for
     * this asset
     */
    public String getIconImgUrl() {
        // 1. it may be the image
        // 2. it may be the icon
        // 3. default to the meta
        String img = icon;

        if (image != null && image.length() > 0) {
            img = image;
        }

        if (img == null || img.length() <= 0) { // default to the meta
            Meta meta = AssetMgr.meta(getKey().getType());
            if (meta != null)
                img = AssetMgr.meta(getKey().getType()).id.getIconProp();
        }

        String i = RazIconRes.getIconFile(img);
        // img = (img.startsWith("/mutant") ? img : "/mutant/getPic/" + img);
        i = (i.startsWith("/") ? i : "/getPic/" + img);
        i = Agents.instance().me().url + i;
        i = LightAuth.wrapUrl(i);

        return i;
    }

    public static class MyRender implements Renderer<AssetBrief> {

        public boolean canRender(AssetBrief o, Technology technology) {
            return true;
        }

        public Object render(AssetBrief o, Technology technology, DrawStream stream) {
            AssetBrief b = o;
            if (Technology.HTML.equals(technology)) {
                return toHtml(b);
            } else if (Technology.XML.equals(technology)) {
                return b.toXml();
            } else if (Technology.JSON.equals(technology)) {
                return b.toJSONString();
            } else if (Technology.UPNP.equals(technology)) {
                return b.toUpnpItem(b.parentID);
            }

            // default rendering
            return b.toString();
        }

        /**
         * @param b
         * @return
         */
        protected Object toHtml(AssetBrief b) {
            String s = "<table align=middle><tr>";
            String img = b.getIconImgUrl();

            // NavButton button = new NavButton(DETAILS, "");

            String width = b.detailLevel.equals(DetailLevel.LIST) ? "80" : "300";
            width = b.detailLevel.equals(DetailLevel.LARGE) ? "150" : width;
            width = b.detailLevel.equals(DetailLevel.BRIEFLIST) ? "30" : width;
            width = b.detailLevel.equals(DetailLevel.FULL) ? "400" : width;

            // THIS IS FUCKED - the nokia770 tablet browser has a problem if only height is
            // specified. it accepts width however !!!!

            if (b.detailLevel.equals(DetailLevel.LARGE)) {
                s += "<td align=center>" + "<a href=\"" + b.getUrlForDetails().makeActionUrl() + "\">"
                        + "<img border=0 " + " width=\"" + width + "\" " + " src=\"" + img + "\"/>" + "</a>";
                s += "<br><b>" + b.getName() + "</b>";
            } else {
                s += "<td>" + "<a href=\"" + b.getUrlForDetails().makeActionUrl() + "\">" + "<img border=0 "
                        + " width=\"" + width + "\" " + " src=\"" + img + "\"/>" + "</a>";
                s += "</td>";
                s += "<td><b>" + b.getName() + "</b><br>";
                s += b.getBriefDesc() + "<br>";
                if (b.detailLevel.equals(DetailLevel.LIST)) {
                    s += b.getLargeDesc() + "<br>";
                } else if (b.detailLevel.equals(DetailLevel.FULL)) {
                    s += b.getLargeDesc() + "<br>";

                    if (b.getLocalDir() != null)
                        s += b.getLocalDir().replace("/", " /") + "<br>";
                }
            }
            // s += "<a href=\"" + b.getUrlForDetails() + "\">details</a>" + "</td>";

            // IF full details, then print all buttons inside:
            if (b.detailLevel.equals(DetailLevel.FULL)) {
                DrawList l = new DrawList();
                for (Drawable a : AssetPres.instance().makeAllButtons(b, false))
                    l.write(a);

                s += l.render(Technology.HTML, null);
            }

            s += "</td>";

            s += "</tr>";
            s += "</table>";

            return HtmlRenderUtils.textToHtml(s);
        }

        /**
         * @param b
         * @return
         */
        protected Object OLDtoHtml(AssetBrief b) {
            String s = "<table align=middle><tr>";
            String img = b.getIconImgUrl();

            // NavButton button = new NavButton(DETAILS, "");

            String width = b.detailLevel.equals(DetailLevel.LIST) ? "80" : "300";
            width = b.detailLevel.equals(DetailLevel.LARGE) ? "150" : width;
            width = b.detailLevel.equals(DetailLevel.BRIEFLIST) ? "30" : width;
            width = b.detailLevel.equals(DetailLevel.FULL) ? "400" : width;

            // THIS IS FUCKED - the nokia770 tablet browser has a problem if only height is
            // specified. it accepts width however !!!!

            if (b.detailLevel.equals(DetailLevel.LARGE)) {
                s += "<td align=center>" + "<a href=\"" + b.getUrlForDetails().makeActionUrl() + "\">"
                        + "<img border=0 " + " width=\"" + width + "\" " + " src=\"" + img + "\"/>" + "</a>";
                s += "<br><b>" + b.getName() + "</b>";
            } else {
                s += "<td>" + "<a href=\"" + b.getUrlForDetails().makeActionUrl() + "\">" + "<img border=0 "
                        + " width=\"" + width + "\" " + " src=\"" + img + "\"/>" + "</a>";
                s += "</td>";
                s += "<td><b>" + b.getName() + "</b><br>";
                s += b.getBriefDesc() + "<br>";
                if (b.detailLevel.equals(DetailLevel.LIST)) {
                    s += b.getLargeDesc() + "<br>";
                } else if (b.detailLevel.equals(DetailLevel.FULL)) {
                    s += b.getLargeDesc() + "<br>";

                    if (b.getLocalDir() != null)
                        s += b.getLocalDir().replace("/", " /") + "<br>";
                }
            }
            // s += "<a href=\"" + b.getUrlForDetails() + "\">details</a>" + "</td>";
            s += "</td>";

            s += "</tr>";
            s += "</table>";

            return HtmlRenderUtils.textToHtml(s);
        }
    }
        protected static Map<String,String> upnptypes = new HashMap<String,String>();
        static {
            upnptypes.put ("Movie", "object.item.videoItem.movie");
            upnptypes.put ("Music", "object.item.audioItem.musicTrack");
        }
}
