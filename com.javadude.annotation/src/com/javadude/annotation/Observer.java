/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Scott Stanchfield - initial API and implementation
 *******************************************************************************/
package com.javadude.annotation;

/**
 * <p>Defines an event set to generate code for.</p>
 *
 * <p>An event set is an interface that is used as an observer. The Bean containing the event set will
 * 		allow instances of that interface to be registered as listeners, and will fire events to them by calling
 * 		their methods.</p>
 *
 * <p>This annotation will generate the following methods in the generated superclass (assuming SomeListener is the listener interface,
 *     and someMethod is a method in that interface):</p>
 *
 * <pre>
 * private java.util.List&lt;SomeListener&gt; someListeners_ = new java.util.ArrayList&lt;SomeListener&gt;();
 * public void add&lt;SomeListener&gt;(SomeListener listener) {
 *     synchronized(someListeners_) {
 *         someListeners_.add(listener);
 *     }
 * }
 * public void removeSomeListener(SomeListener listener) {
 *     synchronized(someListeners_) {
 *         someListeners_.remove(listener);
 *     }
 * }
 * protected void fireSomeMethod(Type1 arg1, Type2 arg2, ...) {
 *     java.util.List&lt;SomeListener&gt; targets = null;
 *     synchronized(someListeners_) {
 *         targets = new java.util.ArrayList&lt;SomeListener&gt;(someListeners_);
 *     }
 *     for (SomeListener listener : targets) {
 *         listener.someMethod(arg1, arg2, ...);
 *     }
 * }
 * </pre>
 */
public @interface Observer {
	/**
	 * The listener interface that represents the observer.
	 * Exactly one of type or typeString must be specified.
	 */
    Class<?> type() default Void.class;

    /**
     * The listener interface that represents the observer.
     * Exactly one of type or typeString must be specified.
     */
    String typeString() default "";

    /**
     * If true, @Override will be added to the generated methods.
     */
    boolean addOverrides() default false;
}
