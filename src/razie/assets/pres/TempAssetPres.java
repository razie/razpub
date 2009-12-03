/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie.assets.pres;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import razie.assets.AssetBrief;
import razie.assets.AssetMgr;
import razie.assets.Meta;
import razie.assets.AssetActionToInvoke;

//import com.razie.assets.EntityAction;
import razie.agent.pres.PageServices;
import com.razie.pub.assets.AssetPres;
import com.razie.pub.assets.ContextActionFactory;
import com.razie.pub.base.ActionItem;
import com.razie.pub.comms.ActionToInvoke;
import com.razie.pub.comms.Agents;
import com.razie.pub.draw.DrawList;
import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.DrawTable;
import com.razie.pub.draw.Drawable;
import com.razie.pub.draw.widgets.NavButton;
import com.razie.pub.lightsoa.SoaAsset;
import com.razie.pub.lightsoa.SoaMethod;
import com.razie.pub.resources.RazIcons;
import com.razie.pubstage.UserPrefs;
//import com.razie.sdk.assets.SdkAssetBrief;

/**
 * asset presentation implementation
 * 
 * @author razvanc
 */
public class TempAssetPres extends AssetPres {

   /**
    * put a list of assets into drawable
    * 
    * @param movies
    *           a collection of assets
    * @param stream
    *           the stream to draw on
    * @param context
    *           current context for actions
    * @param visuals
    *           visual preferences
    * @return the resulting drawable - note that it has already been drawn on the stream, if any stream was
    *         passed in
    */
   @Override
   public Drawable toDrawable(Collection<AssetBrief> movies, DrawStream stream, ContextActionFactory context,
         UserPrefs.AssetListVisual... visuals) {
      UserPrefs.AssetListVisual visual = UserPrefs.AssetListVisual.LIST;

      if (visuals != null && visuals.length > 0) {
         visual = visuals[0];
      } else {
         String cname = "listvisual";
         if (movies.size() > 0) {
            cname += "." + movies.iterator().next().getKey().getType();
         }
         visual = UserPrefs.AssetListVisual.valueOf(UserPrefs.getInstance().getPref(cname,
               UserPrefs.AssetListVisual.LIST.toString()));
      }

      DrawTable list = new DrawTable();

      if (visual.equals(UserPrefs.AssetListVisual.LIST) || visual.equals(UserPrefs.AssetListVisual.BRIEFLIST)) {
         list.htmlWidth = " width=600 ";
      }

      if (stream != null) {
         stream.open(list);
      }
      // list.isVertical = true;
      list.rowColor = "#292929";

      // sort the files...
      // TODO 1-3 USER - this is a big bottleneck: i have to wait for the end of a search to sort the
      // results...DO I?
      List<AssetBrief> sortedMovies = new ArrayList<AssetBrief>();
      sortedMovies.addAll(movies);
      Collections.sort(sortedMovies, new Comparator<AssetBrief>() {
         public int compare(AssetBrief o1, AssetBrief o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
         }
      });

      if (visual.equals(UserPrefs.AssetListVisual.LIST) || visual.equals(UserPrefs.AssetListVisual.BRIEFLIST)) {
         for (AssetBrief movie : sortedMovies) {
            DrawList entry = new DrawList();
            if (visual.equals(UserPrefs.AssetListVisual.BRIEFLIST)) {
               entry.write(new ABDrawable (movie, DetailLevel.BRIEFLIST));
            } else
               entry.write(new ABDrawable (movie, DetailLevel.LIST));


            try {
               for (Drawable a : instance().makeAllButtons(movie,
                     visual.equals(UserPrefs.AssetListVisual.BRIEFLIST)))
                  entry.write(a);

               if (context != null) {
                  List<ActionToInvoke> atil = context.make(movie.getKey());
                  for (ActionToInvoke ati : atil) {
                     ati.drawTiny = visual.equals(UserPrefs.AssetListVisual.BRIEFLIST);
                     entry.write(ati);
                  }
               }
            } catch (Exception e) {
               list.writeRow(e.toString());
            }
            list.writeRow(entry);
         }
      } else if (visual.equals(UserPrefs.AssetListVisual.DETAILS)) {
         list.prefCols = 3;

         for (AssetBrief movie : sortedMovies) {
            movie.detailLevel = AssetBrief.DetailLevel.LARGE;
            list.write(movie);
         }
      }

      if (stream != null) {
         stream.close(list);
      }

      return list;
   }

   /**
    * make all the buttons for a given asset
    * 
    * @param movie the asset to make buttons for
    * @param drawTiny if true then the buttons are small for table-like list of assets. If false,
    *        then the buttons are large for a details page.
    * @return
    */
   @Override
   public List<Drawable> makeAllButtons(AssetBrief movie, boolean drawTiny) {
      List<Drawable> l = new ArrayList<Drawable>();

      for (ActionItem ai : AssetMgr.supportedActions(movie.getKey())) {
         if ("play".equals(ai.name)) {
            l.add(SdkAssetBrief.makePlayButton(movie, drawTiny));
         } else if ("stream".equals(ai.name)) {
            l.add(SdkAssetBrief.makePlayButtonStreamed(movie, drawTiny));
         } else {
            ActionToInvoke ati = new EntityAction(ai, movie.getKey());
            ati.drawTiny = drawTiny;
            l.add(ati);
         }
      }

      // now add reflected soas
      Meta meta = AssetMgr.meta(movie.getKey().getType());
      if (meta != null && meta.assetCls != null && meta.assetCls.length() > 0) {
         try {
            Class<?> ac = Class.forName(meta.assetCls);
            if (ac.getAnnotation(SoaAsset.class) != null) {
               for (Method m : ac.getDeclaredMethods()) {
                  if (m.getAnnotation(SoaMethod.class) != null) {
                     SoaMethod ma = (SoaMethod) m.getAnnotation(SoaMethod.class);
                     ActionItem mitem = new ActionItem(m.getName(), RazIcons.UNKNOWN);
                     mitem.tooltip = ma.descr();

                     NavButton ati = null;

                     if (ma.args() != null && ma.args().length > 0) {
                        // prepare invocation page...
                        ati = PageServices$.MODULE$.methodButton(movie.getKey(), m);
                     } else {
                        ati = new NavButton(new AssetActionToInvoke(Agents.me().url, movie.getKey(), mitem));
                     }
                     ati.setTiny(drawTiny);
                     // ati.drawTiny = drawTiny;
                     l.add(ati);
                  }
               }
            }
         } catch (ClassNotFoundException e) {
            // TODO handle this
            e.printStackTrace();
         }
      }

      return l;
   }
}
