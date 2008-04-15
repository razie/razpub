// ==========================================================================
// $Id: Svc.java,v 1.63 2005/04/01 16:22:12 davidx Exp $
// (@) Copyright Sigma Systems (Canada) Inc.
// ==========================================================================
package com.razie.pub.hframe.draw.widgets;
/** i want to abstract a window
 * 
 * TODO i don't know how to abstract nicely the layouts...
 * 
 * @version $Revision: 1.63 $
 * @author $Author: davidx $
 * @since $Date: 2005/04/01 16:22:12 $
 */
public class CoolWindow {
    public enum Style {
        POPUP, BROWSED
    };

    Style style;

    public CoolWindow style(Style s) {
        this.style = s;
        return this;
    }
}
