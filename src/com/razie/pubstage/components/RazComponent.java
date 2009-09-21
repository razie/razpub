package com.razie.pubstage.components;

import java.util.List;

/**
 * a component is totally manageable and reconfigurable. This is the basic interface to allow that
 * 
 */
public interface RazComponent {
    List<RazFunction> getFunctions();
}
