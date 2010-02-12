package razie.assets.pres;

import razie.assets._
import razie.draw._
import razie.draw.widgets.NavButton;
import razie.draw.widgets.NavLink;

/** basic asset management page */
class PageAssets extends DrawableSource {

    override def makeDrawable() : Drawable = {
        val table = new DrawTable()
        table.prefCols = 4
        table.packed = true

        AssetMgr.metas().map(AssetMgr.meta(_)).foreach (
              meta => table.write(razie.Draw.button(meta.orNull.id, "/mutant/asset/" + meta.orNull.id.name))
        )
        table;
    }

}
