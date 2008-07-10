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
 * <p>Defines a property for a JavaBean.</p>
 * 
 * <p>You can set the following attributes for a property:</p>
 * <dl>
 * 		<dt>name</dt>
 * 			<dd>The name of the property. This will be used as the basis for the generated field name,
 *              and accessor methods.</dd>
 *      <dt>plural</dt>
 *          <dd>The plural name of the property. This is only used for properties that have kind set
 *              to LIST, MAP, UNMODIFIABLE_LIST or UNMODIFIABLE_MAP. If the plural name has not been
 *              set and the kind is one of these, plural will default to the value of name suffixed with
 *              an 's'.</dd>
 *      <dt>type</dt>
 *      	<dd>The type of the property. If the type is in the same package as the annotated bean,
 *              you don't need to package-qualify it. Otherwise, prepend with the package name.
 *              This type is used as the property type for simple properties, and the element type for
 *              list properties.</dt>
 *     	<dt>keyType</dt>
 *     		<dd>The type of keys for map properties. This attribute is ignored for non-map properties.</dd>
 *    	<dt>reader</dt>
 *    		<dd>The access level of the generated reader methods. If set to Access.NONE, the property will not be readable.</dd>
 *    	<dt>writer</dt>
 *    		<dd>The access level of the generated writer methods. If set to Access.NONE, the property will not be writeable.</dd>
 *   	<dt>bound</dt>
 *   		<dd>Set to true if the property should be bound. Bound properties will fire events to registered PropertyChangeListeners. If at least
 *   			one property in a Bean is marked as bound, the generated superclass will contain add/remove methods for PropertyChangeListeners as well
 *   			as firePropertyChange methods.</dd>
 *   	<dt>kind</dt>
 *   		<dd>The general kind of the property. This can have the following values:
 *   			<ul>
 *   				<li>SIMPLE: A standard, single-value JavaBean property</li>
 *   				<li>LIST: A multi-valued JavaBean property, represented as a java.util.List</li>
 *   				<li>MAP: A multi-valued JavaBean property, represented as a java.util.Map</li>
 *   				<li>UNMODIFIABLE_LIST: A LIST property, but the generated reader that returns the entire list will wrap it in Collections.unmodifiableList</li>
 *   				<li>UNMODIFIABLE_MAP: A MAP property, but the generated reader that returns the entire list will wrap it in Collections.unmodifiableMap</li>
 *   			</ul>
 * </dl>
 */
public @interface Property {
    String name();
    String plural() default "";
    Class<?> type() default Void.class;
    String typeString() default "";
    Class<?> keyType() default Void.class;
    String keyTypeString() default "";
    Access reader() default Access.NOT_SPECIFIED;
    Access writer() default Access.NOT_SPECIFIED;
    boolean bound() default false;
    PropertyKind kind() default PropertyKind.SIMPLE;
    boolean omitFromToString() default false;
}
