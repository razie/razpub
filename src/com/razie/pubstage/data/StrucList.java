package com.razie.pubstage.data;

import java.util.ArrayList;
import java.util.List;


/**
 * a list structure
 * 
 * TODO detailed docs
 * 
 * @author razvanc
 * @param <T>
 */
public interface StrucList<T> extends Structure<T> {
    List<T> elements();

    public static class Impl<T> extends Structure.Impl<T> implements StrucList<T> {
        protected List<T> children = new ArrayList<T>();

        public Impl(T contents) {
            super(contents);
        }

        @Override
        public List<T> elements() {
            return children;
        }
        
        public void add(T t) {
            children.add(t);
        }
    }
}
