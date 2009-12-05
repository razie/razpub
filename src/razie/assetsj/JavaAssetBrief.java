/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie.assetsj;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONString;

import razie.assets.AssetBriefImpl;
import razie.assets.AssetKey;

import com.razie.pub.base.ActionItem;
import com.razie.pub.draw.Drawable;
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
 * Think that this is the equivalent of an item in a Media RSS. 
 * 
 * Uses for the asset brief: represent RSS items, UPNP items, etc.
 * 
 * TODO OldAssetBrief should be final and immutable - for now i still use the SdkOldAssetBrief in the
 * mutant...
 * 
 * TODO split into a generic assetbrief and a fileassetbrief - for files, with directories etc.
 * these are not applicable to say web assets...
 * 
 * @author razvanc99
 */
public class JavaAssetBrief extends AssetBriefImpl implements JSONString {

    public static final ActionItem DELETE = new ActionItem("delete", RazIcons.DELETE);

    /** standard actions on assets */
    public static final ActionItem DETAILS     = new ActionItem("details", RazIcons.UNKNOWN);
    public static final ActionItem PLAY        = new ActionItem("play", RazIcons.PLAY);
    public static final ActionItem STREAM      = new ActionItem("stream", RazIcons.PLAY);

    public JavaAssetBrief() {
    }

    public JavaAssetBrief(String name) {
        this.setName(name);
    }

    public JavaAssetBrief(AssetKey ref) {
        this.setKey(ref);
        this.setName(ref.getId());
    }


//   // TODO 1-2 setup the entire rss feed thing. implies format specs
//   public String toRssMediaItem() {
//       String s = "\n<item>\n";
//
//       AttrAccess a = new AttrAccessImpl();
//
//       a.setAttr("title", getName());
//       a.setAttr("media:title", getName());
//       a.setAttr("link", "?");// TODO build page to view the item
//       a.setAttr("media:player", "?");
//
////       a.setAttr("upnp:class", "object.item.videoItem.movie");
//       // if (series != null)
//       // a.setAttr("series", getSeries().toUrlEncodedString());
//
////       a.setAttr("upnp:genre", "");
//       a.setAttr("description", getLargeDesc());
//       a.setAttr("media:description", getBriefDesc());
//
//       a.setAttr("media:category", "?");
//
////       a.setAttr("upnp:storageMedium", "");
////       a.setAttr("upnp:channelName", "");
//
//       s += a.toXml();
//
//       // String p = "\n<res protocolInfo=\"http-get:*:" + getMimeType() + ":*\" size=\"200000\">"
//       // + "http://"
//       // + Devices.getMyUrl() + "/mutant/stream?ref=" + getRef().toUrlEncodedString() + "</res>";
//       String p = "\n<res protocolInfo=\"http-get:*:" + getMimeType() + ":*\" size=\"200000\">"
//               + getUrlForStreaming().makeActionUrl() + "</res>";
//
//       return s + p + "\n</item>";
//   }

//    @Override 
//    public String toJSONString () {
//       toAA.toJson(new JSONObject()).toString();
//    }
}
