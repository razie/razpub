/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie.assets

import com.razie.pub.base.ActionItem;
import com.razie.pub.base._;
import com.razie.pub.base.ScriptContext;
import com.razie.pub.comms.Agents;
import com.razie.pub.draw._
import com.razie.pub.draw.Renderer;
import com.razie.pub.draw.Renderer.Technology;
import com.razie.pub.draw.widgets.NavButton;

/**
 * not sure why i need this class on top of the SdkAsset...
 * 
 * @author razvanc
 */
class AssetImpl (b : AssetBrief) extends AssetBaseImpl(b) with Drawable {
   var nkey : AssetKey = super.key

   override def key = nkey
   def key_= (k:AssetKey) = setKey (k)

   def this (k : AssetKey) = {
      this (new FileAssetBriefImpl())
      setKey (k)
   }
   
   def this () = {
      this (new FileAssetBriefImpl())
   }

   /** be sure to set the either a key or a brief before using it, eh? */

   override def setKey(k:AssetKey ) {
      this.nkey = k;
      super.setKey(k);
      if (k != null) brief match {
         case b:FileAssetBriefImpl => {
              brief.asInstanceOf[FileAssetBriefImpl].setFileName(k.id);
              brief.asInstanceOf[FileAssetBriefImpl].setLocalDir(k.loc.getLocalPath());
         }
      }
   }

   override def getKey() : AssetKey = if (this.nkey  == null) super.getKey() else this.nkey

   override def getRenderer(t:Technology ) : Renderer[Drawable] = 
      Drawable.DefaultRenderer.singleton;

   override def render(t:Technology , stream:DrawStream ) : AnyRef = {
      val movie = getBrief();

//      if (ctx.isPopulated("series"))
//         movie.setSeries((AssetKey) ctx.getAttr("series"));

      // TODO the remote paths come here without a / - fix that!
      movie match {
         case m : FileAssetBrief => if (m.localDir != null && m.localDir.startsWith("/")) {
            m.localDir = "/" + m.localDir
         }
      }

      val vert = new DrawList();
      vert.isVertical = true;

      // DrawList vert2 = new DrawList();
      // vert2.isVertical = true;

      val horiz = new DrawList();
      val actions = new DrawList();

      horiz.write(new ABDrawable (movie, DetailLevel.FULL))

       for (a <- razie.RJS apply AssetMgr.pres().makeAllButtons(movie, false))
          actions.write(a)

      // add more links...
      val moreActions = new DrawList();
      moreActions.write(new NavButton(new ActionItem("google"), new AttrAccessImpl("q", "movie "
            + movie.getName()).addToUrl("http://images.google.com/images")));
      moreActions.write(new NavButton(new ActionItem("imdb"), new AttrAccessImpl("s", "all", "q", movie
            .getName()).addToUrl("http://imdb.com/find")));
      movie match {
         case m : FileAssetBrief => 
      moreActions.write(new NavButton(new ActionItem("savejpg"), Agents.me.url + "/mutant/cmd" + "/saveJpg/" + "Movie/" + m.localDir + movie.getKey().getId() + "&"));
      }

      // vert2.write(movie);
      // vert2.write(actions);
      horiz.write(actions);

      vert.write(horiz);
      vert.write(moreActions);

      val moreDetails = AssetMgr.getDetails(movie);
      new DrawSequence(vert, moreDetails);
   }

}
