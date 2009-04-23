package com.razie.pubstage.actions;

import java.util.Collections;
import java.util.List;

public interface ActionableObjectSpec {

    public List<ActionableSpec> getActionables();
    
    public static class Impl implements ActionableObjectSpec {

        @Override
        public List<ActionableSpec> getActionables() {
            return Collections.EMPTY_LIST;
        }
        
    }
}
