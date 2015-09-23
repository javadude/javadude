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
 * Describes the kinds of properties that you can define using JavaDude Annotations.
 *
 * As a user of this class, you should not call its methods; you should only use
 *   the enumeration values when specifying properties. For example:
 * <pre>
 * &#064;Bean(properties = {
 *     &#064;Property(name="name"),   // defaults to PropertyKind.SIMPLE
 *     &#064;Property(name="nickNames", kind=PropertyKind.LIST),
 * })
 *
 * There are four basic kinds of properties:
 *   SIMPLE: a single-valued property
 *   LIST: a property with values that are stored and accessed as a list
 *   SET: a property with values that are stored and accessed as a list
 *   MAP: a property with mapped values that are stored and accessed as a map
 *
 * Each kind of property generates several methods. For each example, we assume a property
 *   named "thing" of type "Type".
 *
 * SIMPLE properties generate the following:
 *   private Type thing_;
 *   [writer-access] void setThing(Type thing);
 *   [reader-access] Type getThing();
 *
 * LIST properties generate the following methods:
 *   private List&lt;Type&gt; things_ = new ArrayList&lt;Type&gt;();
 *   [reader-access] Type getThing(int i);
 *   [reader-access] List&lt;Type&gt; getThings();
 *   [reader-access] boolean thingsContains(Type value);
 *   [writer-access] void addThing(Type value);
 *   [writer-access] void addThing(int i, Type value);
 *   [writer-access] void clearThings();
 *
 * SET properties generate the following methods:
 *   private Set&lt;Type&gt; things_ = new HashSet&lt;Type&gt;();
 *   [reader-access] Set&lt;Type&gt; getThings();
 *   [reader-access] boolean thingsContains(Type value);
 *   [writer-access] void addThing(Type value);
 *   [writer-access] void clearThings();
 *
 * MAP properties generate the following (KeyType is specified as the type of key to use)
 *   private Map&lt;KeyType, Type&gt; things_ = new HashMap&lt;KeyType, Type&gt;();
 *   [reader-access] Type getThing(KeyType key);
 *   [reader-access] Map&lt;KeyType, Type&gt; getThings();
 *   [reader-access] boolean thingsContainsKey(KeyType key);
 *   [reader-access] boolean thingsContainsValue(Thing value);
 *   [writer-access] void putThing(KeyType key, Type value);
 *   [writer-access] void clearThings();
 *
 */
public enum PropertyKind {
    SIMPLE, LIST, /* TODO: INDEXED, */ SET, MAP;
    public boolean isSimple() {
        return this == SIMPLE;
    }
    public boolean isList() {
        return this == LIST;
    }
    public boolean isSet() {
    	return this == SET;
    }
    public boolean isMap() {
        return this == MAP;
    }
}
