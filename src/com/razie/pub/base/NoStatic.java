/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base;


/**
 * thread local static object, see NoStatics - not sure why i don't like ThreadLocal yet :)
 * 
 * TODO this implementation is soooooo stupid...
 * 
 * TODO make mtsafe
 * 
 * @author razvanc99
 */
public class NoStatic<T> {
    private T     value;
    private T     initialValue;
    public String id;

    public NoStatic(String id, T initialValue) {
        this.value = initialValue;
        this.initialValue = initialValue;
        this.id = id;
    }

    public T value() {
        return find().value;
    }

    /** here's the tricky part... will set only on the particular thread ... IF it has a context... */
    public void setThreadValue(T newValue) {
        NoStatic<T> inst = this;

        ThreadContext tx = ThreadContext.instance();
        if (tx != ThreadContext.MAIN) {
            if (tx.isPopulated(id)) {
                inst = (NoStatic<T>) (ThreadContext.instance().getAttr(id));
            } else {
                // here's the magic: clone itself for new context with new value
                NoStatic<T> newInst = new NoStatic<T>(id, initialValue);
                tx.setAttr(id, newInst);
                inst = newInst;
            }
        }

        inst.value = newValue;
    }

    private NoStatic<T> find() {
        Object o = (ThreadContext.instance().getAttr(id));
        if (o != null)
            return (NoStatic<T>) o;
        return this;
    }
}
