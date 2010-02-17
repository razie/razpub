package razie.assets.pres;

import razie.assets.AssetBrief;
import razie.assets.AssetBrief$;
import razie.assets.AssetCtxKey;
import razie.draw.widgets.NavButton;
import razie.draw.widgets.NavLink;

import com.razie.pub.comms.Agents;

/**
 * a brief description of an asset. It includes enough information to allow users to initiate
 * actions on this (i.e. play or viewDetails). It includes a favorite player (optional, though).
 * 
 * It does not neccessarily identify a file or an URL that links to the asset. But rather enough
 * information that an inventory based command back to the server of the append with this REF can
 * identify the asset and for instance start streaming it.
 * 
 * It normally includes an icon, an image, a name, small and large descriptions, plus whatever other
 * information is needed.
 * 
 * @author razvanc
 */
public class SdkAssetBrief {

    public static NavLink makePlayButtonStreamed(AssetBrief movie, boolean tiny) {
        NavLink b = new NavButton(AssetBrief$.MODULE$.STREAM(), movie.getUrlForStreaming());
        b.setTiny(tiny);
        return b;
    }

    public static NavLink makePlayButton(AssetBrief movie, boolean tiny) {
       if (movie.key() instanceof AssetCtxKey) {
        String cmd = "/playepisode?player=";
        String sref = "&series=" + ((AssetCtxKey)movie.key()).ctx().role("series").toUrlEncodedString();
        NavLink b = new NavButton(AssetBrief$.MODULE$.PLAY(), Agents.instance().me().url + "/mutant/cmd" + cmd + movie.getPlayer()
                + "&ref=" + movie.getKey().toUrlEncodedString() + sref);
        b.setTiny(tiny);
        return b;
       } else {
          String cmd = "/play?player=";
          String sref = "";
          NavLink b = new NavButton(AssetBrief$.MODULE$.PLAY(), Agents.instance().me().url + "/mutant/cmd" + cmd + movie.getPlayer()
                  + "&ref=" + movie.getKey().toUrlEncodedString() + sref);
          b.setTiny(tiny);
        return b;
       }
    }
}
