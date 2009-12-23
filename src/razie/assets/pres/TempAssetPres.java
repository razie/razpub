/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package razie.assets.pres;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import razie.agent.pres.PageServices;
import razie.assets.ABDrawable;
import razie.assets.AssetActionToInvoke;
import razie.assets.AssetBrief;
import razie.assets.AssetPres;
import razie.assets.Meta;

import com.razie.pub.assets.ContextActionFactory;
import com.razie.pub.assets.JavaAssetMgr;
import com.razie.pub.base.ActionItem;
import com.razie.pub.comms.ActionToInvoke;
import com.razie.pub.comms.Agents;
import com.razie.pub.draw.DetailLevel;
import com.razie.pub.draw.DrawList;
import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.DrawTable;
import com.razie.pub.draw.Drawable;
import com.razie.pub.draw.widgets.NavButton;
import com.razie.pub.lightsoa.SoaAsset;
import com.razie.pub.lightsoa.SoaMethod;
import com.razie.pub.resources.RazIcons;
import com.razie.pubstage.AssetListVisual;
import com.razie.pubstage.UserPrefs;

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
   public Drawable toDrawable(Iterable<AssetBrief> movies, DrawStream stream, ContextActionFactory context,
         AssetListVisual... visuals) {
      AssetListVisual visual = AssetListVisual.LIST;

      if (visuals != null && visuals.length > 0) {
         visual = visuals[0];
      } else {
         String cname = "listvisual";
         Iterator<AssetBrief> i = movies.iterator();
         if (i.hasNext()) {
            cname += "." + i.next().getKey().getType();
         }
         visual = AssetListVisual.valueOf(UserPrefs.getInstance().getPref(cname,
               AssetListVisual.LIST.toString()));
      }

      DrawTable list = new DrawTable();

      if (visual.equals(AssetListVisual.LIST) || visual.equals(AssetListVisual.BRIEFLIST)) {
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
      for (AssetBrief m : movies) sortedMovies.add(m);
      Collections.sort(sortedMovies, new Comparator<AssetBrief>() {
         public int compare(AssetBrief o1, AssetBrief o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
         }
      });

      if (visual.equals(AssetListVisual.LIST) || visual.equals(AssetListVisual.BRIEFLIST)) {
         for (AssetBrief movie : sortedMovies) {
            DrawList entry = new DrawList();
            if (visual.equals(AssetListVisual.BRIEFLIST)) {
               entry.write(new ABDrawable (movie, DetailLevel.BRIEFLIST));
            } else
               entry.write(new ABDrawable (movie, DetailLevel.LIST));


            try {
               for (Drawable a : makeAllButtons(movie,
                     visual.equals(AssetListVisual.BRIEFLIST)))
                  entry.write(a);

               if (context != null) {
                  List<ActionToInvoke> atil = context.make(movie.getKey());
                  for (ActionToInvoke ati : atil) {
                     ati.drawTiny = visual.equals(AssetListVisual.BRIEFLIST);
                     entry.write(ati);
                  }
               }
            } catch (Exception e) {
               list.writeRow(e.toString());
            }
            list.writeRow(entry);
         }
      } else if (visual.equals(AssetListVisual.DETAILS)) {
         list.prefCols = 3;

         for (AssetBrief movie : sortedMovies) {
            list.write(new ABDrawable (movie, DetailLevel.LARGE));
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

      for (ActionItem ai : JavaAssetMgr.supportedActions(movie.getKey())) {
         if ("play".equals(ai.name)) {
            l.add(SdkAssetBrief.makePlayButton(movie, drawTiny));
         } else if ("stream".equals(ai.name)) {
            l.add(SdkAssetBrief.makePlayButtonStreamed(movie, drawTiny));
         } else {
//            ActionToInvoke ati = new EntityAction(ai, movie.getKey());
            ActionToInvoke ati = new AssetActionToInvoke(movie.getKey(), ai);
            ati.drawTiny = drawTiny;
            l.add(ati);
         }
      }

      // now add reflected soas
      Meta meta = JavaAssetMgr.meta(movie.key().getType());
      if (meta != null && meta.assetCls() != null && meta.assetCls().length() > 0) {
         try {
            Class<?> ac = Class.forName(meta.assetCls());
            if (ac.getAnnotation(SoaAsset.class) != null) {
               for (Method m : ac.getDeclaredMethods()) {
                  if (m.getAnnotation(SoaMethod.class) != null) {
                     SoaMethod ma = (SoaMethod) m.getAnnotation(SoaMethod.class);
                     ActionItem mitem = new ActionItem(m.getName(), RazIcons.UNKNOWN);
                     mitem.tooltip = ma.descr();

                     NavButton ati = null;

                     if (ma.args() != null && ma.args().length > 0) {
                        // prepare invocation page...
                        ati = razie.agent.pres.PageServices$.MODULE$.methodButton(movie.getKey(), m);
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
