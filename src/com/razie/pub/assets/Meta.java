/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.assets;

import com.razie.pub.base.ActionItem;

/**
 * a meta-description of an asset type
 * 
 * The inventory and asset class specs are used elsewhere...didn't bother extending...you don't have
 * to use them
 * 
 * TODO 1-1 metas need a namespace
 */
public class Meta {
   public String       inventory;
   public String       assetCls;
   public String       baseMetaname;
   public ActionItem   id;
   public ActionItem[] supportedActions;

   /** basic constructor - the inventory/class are set automatically when registered */
   public Meta(ActionItem id, String base) {
      this.baseMetaname = base;
      this.id = id;
   }

   /** a meta for assets with an inventroy */
   public Meta(ActionItem id, String base, String inventory) {
      this.baseMetaname = base;
      this.id = id;
      this.inventory = inventory;
   }

   /** a meta for assets with their own class */
   public Meta(ActionItem id, String base, String assetCls, String inventory) {
      this.baseMetaname = base;
      this.id = id;
      this.inventory = inventory;
      this.assetCls = assetCls;
   }

   public String toBriefXml() {
      StringBuilder b = new StringBuilder();
      b.append("<metaspec name=\"" + id.name + "\"");
      b.append(" inventory=\"" + inventory + "\"");
      if (baseMetaname != null && baseMetaname.length() > 0)
         b.append(" base=\"" + baseMetaname + "\"");
      b.append(">");

      b.append("</metaspec>");

      return b.toString();
   }

   public String toDetailedXml() {
      StringBuilder b = new StringBuilder();
      b.append("<metaspec name=\"" + id.name + "\"");
      b.append(" inventory=\"" + inventory + "\"");
      if (baseMetaname != null && baseMetaname.length() > 0)
         b.append(" base=\"" + baseMetaname + "\"");
      b.append(">");

      for (ActionItem ai : supportedActions) {
         b.append("<action name=\"" + ai.name + "\"");
         b.append("/>");
      }
      // TODO 1-1 add reflected actions
      // TODO 1-1 add assocs
      
      b.append("</metaspec>");

      return b.toString();
   }
   
   public String toString () {
      return toBriefXml();
   }
}