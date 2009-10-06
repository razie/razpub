/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base;

/**
 * thread local static object - ThreadLocal is easy to implement, not neccessarily to use
 * 
 * Use like
 * <code> public static NoStatic<MyClass> myStatic = new NoStatic<MyClass>("myStatic", new MyClass(...))</code>
 * .
 * 
 * On a thread, reset the default value if needed:
 * <code>myStatic.setThreadValue (newValue)</code>
 * 
 * In code, access like a static, don't worry about thread:
 * <code>myStatic.value()</code>
 * 
 * NOTE this is a fairly limited implementation - use NoStatics instead!
 * 
 * TODO make mtsafe - didn't need it so far
 * TODO i should use ThreadLocal to implement this
 * 
 * see TLNoStatic for why ThreadLocal is not usable
 * 
 * @see com.razie.pub.base.test.TestNoStatic
 * @author razvanc99
 */
public class NoStatic<T> {
	private T value;
	private T initialValue;
	public String id;

	/** initialize with initial value - inherited on all threads */
	public NoStatic(String id, T initialValue) {
		this.value = initialValue;
		this.initialValue = initialValue;
		this.id = id;
	}

	public T get() {
		return find().value;
	}

	/**
	 * here's the tricky part... will set only on the particular thread ... IF
	 * it has a context...
	 */
	public void set(T newValue) {
		NoStatic<T> inst = this;

		ThreadContext tx = ThreadContext.instance();
		if (tx != ThreadContext.DFLT_CTX) {
			if (tx.isPopulated(id)) {
				inst = (NoStatic<T>) (ThreadContext.instance().getAttr(id));
			} else {
				// here's the magic: clone itself for new context with new value
			   // TODO shouldn't i clone the initialValue?
			   // TODO explicit samples/tests for this
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
