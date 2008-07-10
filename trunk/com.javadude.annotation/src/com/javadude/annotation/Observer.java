/*******************************************************************************
 *  Copyright 2008 Scott Stanchfield.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package com.javadude.annotation;

/**
 * <p>Defines an event set to generate code for.</p>
 * 
 * <p>An event set is an interface that is used as an observer. The Bean containing the event set will
 * 		allow instances of that interface to be registered as listeners, and will fire events to them by calling
 * 		their methods.</p>
 * 
 * <p>This annotation supports the following attributes:</p>
 * <dl>
 * 		<dt>type</dt>
 * 			<dd>The listener interface that represents the event set</dd>
 * 		<dt>addOverrides</dt>
 * 			<dd>If set to true, an &#064;Override annotation will be added to generated methods</dd>
 * </dl>
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
    Class<?> type() default Void.class;
    String typeString() default "";
    boolean addOverrides() default false;
}
