/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base;

/**
 * simple typed wrapper over ThreadLocal
 * 
 * Use like
 * <code> public static NoStatic<MyClass> myStatic = new NoStatic<MyClass>(new MyClass(...))</code>
 * .
 * 
 * On a thread, reset the default value if needed:
 * <code>myStatic.set(newValue)</code>
 * 
 * In code, access like a static, don't worry about thread:
 * <code>myStatic.get()</code>
 * 
 * @see com.razie.pub.base.test.TestNoStatic
 * @author razvanc99
 * @deprecated use NoStatic instead - it's tied to the ExecutionContext instead of the local thread...more powerful
 */
public class TLNoStatic<T> extends ThreadLocal<Object> {
	private T initialValue;

	/** initialize with initial value - inherited on all threads */
	public TLNoStatic(T initialValue) {
		this.initialValue = initialValue;
	}

	 protected synchronized Object initialValue() {
         return initialValue;
     }
	 
	 @SuppressWarnings("unchecked")
   public T get() {
		return (T) super.get();
	}
}
