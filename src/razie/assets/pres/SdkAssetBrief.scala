package razie.assets.pres;

import razie.assets.AssetBrief;
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
object SdkAssetBrief {

    def makePlayButtonStreamed(movie:AssetBrief , tiny:Boolean ) : NavLink = {
        val b = new NavButton(AssetBrief.STREAM, movie.getUrlForStreaming());
        b.setTiny(tiny);
        b
    }

    def makePlayButton(movie:AssetBrief , tiny:Boolean ) : NavLink = {
       if (movie.key.isInstanceOf[AssetCtxKey]) {
        val cmd = "/playepisode?player=";
        val sref:String = "&series=" + movie.key.asInstanceOf[AssetCtxKey].ctx.role("series").toUrlEncodedString
        val b = new NavButton(AssetBrief.PLAY, Agents.me().url + "/mutant/cmd" + cmd + movie.getPlayer()
                + "&ref=" + movie.key.toUrlEncodedString + sref);
        b.setTiny(tiny);
        b;
       } else {
          val cmd = "/play?player=";
          val sref = "";
          val b = new NavButton(AssetBrief.PLAY, Agents.me().url + "/mutant/cmd" + cmd + movie.getPlayer()
                  + "&ref=" + movie.key.toUrlEncodedString + sref);
          b.setTiny(tiny);
        b;
       }
    }
}
