/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.assets;

/**
 * an asset has a brief and is referenceable...this is the deepest base for an asset
 * 
 * @author razvanc 
 * @stereotype thing
 */
public interface AssetBase extends Referenceable {
    AssetBrief getBrief();

    AssetKey getKey();

    public class Impl implements AssetBase {

        protected AssetBrief brief;

        public Impl(AssetBrief brief) {
            this.brief = brief;
        }

        /** used only in derived before they figure out and set the brief */
        protected Impl() {
        }

        public AssetBrief getBrief() {
            return this.brief;
        }

        public void setBrief(AssetBrief brief2) {
            this.brief = brief2;
        }

        public AssetKey getKey() {
            return this.brief.getKey();
        }

        public void setKey(AssetKey ref) {
            this.brief.setKey(ref);
        }
    }
}
