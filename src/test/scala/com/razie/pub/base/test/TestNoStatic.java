/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.base.test;

import junit.framework.TestCase;

import com.razie.pub.base.NoStatic;
import com.razie.pub.base.TLNoStatic;
import com.razie.pub.base.ExecutionContext;

/**
 * test the light server
 * 
 * @author razvanc99
 */
public class TestNoStatic extends TestCase {
	ExecutionContext t = new ExecutionContext(null);
	static String failed = null;

	// setup a static context - we test that in other threads i get differnet
	// values
	static NoStatic<Boolean> STATIC = new NoStatic<Boolean>("testing", Boolean.TRUE);
	static TLNoStatic<Boolean> STATICTL = new TLNoStatic<Boolean>(Boolean.TRUE);

	// setup two statics in different threads and make sure they work
	public void testNoStatics() throws InterruptedException {
		// we test this context in the second thread
		t.enter();
		failed = null;

		// the value is set in a differnet context than default...for t2
		STATIC.set(Boolean.FALSE);
		STATICTL.set(Boolean.FALSE);

		// t1 uses the default
		Thread t1 = new Thread() {
			@Override
			public void run() {
				if (!STATIC.get())
					failed = "value changed!!!";
				if (!STATICTL.get())
					failed = "TL value changed!!!";
			}
		};

		// t2 overwrites default
		Thread t2 = new Thread() {
			@Override
			public void run() {
				t.enter();
				// we reuse the same thread context above - value will be FALSE
				if (STATIC.get())
					failed = "value DIDNT change!!!";

				// threadLocal created a new one for this new thread - still TRUE...
				if (!STATICTL.get())
					failed = "TL value DIDNT change!!!";
				ExecutionContext.exit();
			}
		};

		t1.start();
		t2.start();

		t1.join();
		t2.join();

		assertFalse(failed, failed != null);
	}

}
