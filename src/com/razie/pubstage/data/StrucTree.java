package com.razie.pubstage.data;

import java.util.ArrayList;
import java.util.List;


/**
 * a tree structure, with nodes and leafs etc
 * 
 * TODO detailed docs
 * 
 * @author razvanc
 * @param <T>
 */
public interface StrucTree<T> extends Structure<T> {
    List<StrucTree<T>> getChildren();

    boolean isLeaf();

    public static class ImplNode<T> extends Structure.Impl<T> implements StrucTree<T> {
        protected List<StrucTree<T>> children = new ArrayList<StrucTree<T>>();

        public ImplNode(T contents) {
            super(contents);
        }

        @Override
        public List<StrucTree<T>> getChildren() {
            return children;
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        public void addLeaf (T t) {
           this.children.add(new StrucTree.ImplLeaf<T>(t));
        }

        public StrucTree<T> addNode (T t) {
           StrucTree newT = new StrucTree.ImplNode<T>(t);
           this.children.add(newT);
           return newT;
        }
    }
    
    public static class ImplLeaf<T> extends Structure.Impl<T> implements StrucTree<T> {
        public ImplLeaf(T contents) {
            super(contents);
        }

        @Override
        public List<StrucTree<T>> getChildren() {
            return null;
        }

        @Override
        public boolean isLeaf() {
            return true;
        }
    }
}
