package razie.assetsj;

import com.razie.pub.base.ActionItem;
import com.razie.pub.base.ScriptContext;
import com.razie.pub.comms.ActionToInvoke;
import com.razie.pub.comms.LightAuth;
import com.razie.pub.draw.Drawable;

import razie.assets.AssetKey;

/**
 * the OLD asset action URL invocation...
 * 
 * @deprecated - use AssetActionToInvoke instead...
 */
public class EntityAction extends AssetActionToInvoke implements Drawable {

	public EntityAction(ActionItem item, AssetKey ref, Object... pairs) {
		super(ref, item, pairs);
		this.setAttr("ref", ref);
	}

	public EntityAction clone() {
		return new EntityAction(this.actionItem.clone(), this.ref.clone(), this
				.toPairs());
	}

	public String makeActionUrl() {
		String url = this.target + "/cmd/invcmd?cmd=" + actionItem.name + "&ref="
				+ ref.toUrlEncodedString();

		for (String nm : getPopulatedAttr()) {
			if (!"cmd".equals(nm) && !"ref".equals(nm)) {
				url += "&" + nm + "=" + getAttr(nm);
			}
		}
		return LightAuth.wrapUrl(url);
	}
}
