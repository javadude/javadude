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
 */
public @interface Property {
	/**
	 * The name of the property. This will be used as the basis for the generated field name,
	 *   and accessor methods.
	 */
    String name();

    /**
     * The plural name of the property. This is only used for properties that have kind set to LIST, MAP, SET,
     *   UNMODIFIABLE_LIST, UNMODIFIABLE_MAP or UNMODIFIABLE_SET. If the plural name has not been set and
     *   the kind is one of these, plural will default to the value of name suffixed with an 's'.
     * @return
     */
    String plural() default "";

    /**
     * The type of the property. This type is used as the property type for simple properties, the
     *   element type for list/set properties, and the value type for map properties.
     * Exactly one of type or typeString must be specified.
     */
    Class<?> type() default Void.class;

    /**
     * The type of the property. If the type is in the same package as the annotated bean, you don't need to
     *   package-qualify it. Otherwise, prepend with the package name. This type is used as the property
     *   type for simple properties, element type for list/set properties, and the value type for map
     *   properties.
     * Exactly one of type or typeString must be specified.
     */
    String typeString() default "";

    /**
     * The type of keys for map properties. This attribute is ignored for non-map properties.
     * Exactly one of keyType or keyTypeString must be specified.
     */
    Class<?> keyType() default Void.class;

    /**
     * The type of keys for map properties. This attribute is ignored for non-map properties.
     * If the type is in the same package as the annotated bean, you don't need to
     *   package-qualify it. Otherwise, prepend with the package name.
     * Exactly one of keyType or keyTypeString must be specified.
     */
    String keyTypeString() default "";

    /**
     * The access level of the generated reader methods. If set to Access.NONE, the property will not be readable.
     */
    Access reader() default Access.NOT_SPECIFIED;

    /**
     * The access level of the generated writer methods. If set to Access.NONE, the property will not be writeable.
     */
    Access writer() default Access.NOT_SPECIFIED;

    /**
     * Set to true if the property should be bound. Bound properties will fire events to registered
     *   PropertyChangeListeners. If at least one property in a Bean is marked as bound, the generated
     *   superclass will contain add/remove methods for PropertyChangeListeners as well as
     *   firePropertyChange methods.
     */
    boolean bound() default false;

    /**
     * The general kind of the property. This can have the following values:
     *   <ul>
     *   	<li>{@link PropertyKind.SIMPLE}: A standard, single-value JavaBean property</li>
     * 		<li>{@link PropertyKind.LIST}: A multi-valued JavaBean property, represented as a java.util.List</li>
     * 		<li>{@link PropertyKind.MAP}: A multi-valued JavaBean property, represented as a java.util.Map</li>
     * 		<li>{@link PropertyKind.SET}: A multi-valued JavaBean property, represented as a java.util.Set</li>
     * 		<li>{@link PropertyKind.UNMODIFIABLE_LIST}: A LIST property, but the generated reader that returns the entire list will wrap it in Collections.unmodifiableList</li>
     * 		<li>{@link PropertyKind.UNMODIFIABLE_MAP}: A MAP property, but the generated reader that returns the entire list will wrap it in Collections.unmodifiableMap</li>
     * 		<li>{@link PropertyKind.UNMODIFIABLE_SET}: A SET property, but the generated reader that returns the entire list will wrap it in Collections.unmodifiableMap</li>
     * 	</ul>
     */
    PropertyKind kind() default PropertyKind.SIMPLE;

    /**
     * If true, this property will not be included in the generated toString() method. This is useful
     *   to avoid cyclic toString() definitions.
     */
    boolean omitFromToString() default false;
}
