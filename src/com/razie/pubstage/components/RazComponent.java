// ==========================================================================
// $Id: Svc.java,v 1.63 2005/04/01 16:22:12 davidx Exp $
// (@) Copyright Sigma Systems (Canada) Inc.
// ==========================================================================
package com.razie.pubstage.components;

import java.util.List;

/**
 * a component is totally manageable and reconfigurable. This is the basic interface to allow that
 * 
 * @version $Revision: 1.63 $
 * @author $Author: davidx $
 * @since $Date: 2005/04/01 16:22:12 $
 */
public interface RazComponent {
    List<RazFunction> getFunctions();
}