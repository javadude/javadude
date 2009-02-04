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
package com.javadude.annotation.processors;

import java.util.Date;

import com.javadude.annotation.Bean;
import com.javadude.annotation.Property;
import com.javadude.annotation.PropertyKind;


@Bean(superclass=BasePushable.class,
	defineCreatePropertyMap=true,
	properties = {
		@Property(name="packageName"),
		@Property(name="firstPropertyName"),
		@Property(name="className"),
		@Property(name="date", type=Date.class),
		@Property(name="classModifiers"),
		@Property(name="superclass"),
		@Property(name="genericDecls"),
		@Property(name="paramStringModifiers"),
		@Property(name="cloneable", type=boolean.class),
		@Property(name="atLeastOneObject", type=boolean.class),
		@Property(name="atLeastOneDouble", type=boolean.class),
		@Property(name="definePropertyNameConstants", type=boolean.class),
		@Property(name="extendPropertyNameConstants", type=boolean.class),
		@Property(name="equalsShouldCheckSuperEquals", type=boolean.class),
		@Property(name="year", type=int.class),
		@Property(name="defineEqualsAndHashCode", type=boolean.class),
		@Property(name="definePropertyChangeSupport", type=boolean.class),
		@Property(name="extendParamString", type=boolean.class),
		@Property(name="defineCreatePropertyMap", type=boolean.class),
		@Property(name="createPropertyMapCallsSuper", type=boolean.class),
		@Property(name="property", plural="properties", type=PropertySpec.class, kind=PropertyKind.LIST),
		@Property(name="superclassConstructor", type=Method.class, kind=PropertyKind.LIST),
		@Property(name="observer", type=Type.class, kind=PropertyKind.LIST),
		@Property(name="defaultMethod", type=Method.class, kind=PropertyKind.LIST),
		@Property(name="delegate", type=DelegateSpec.class, kind=PropertyKind.LIST),
		@Property(name="nullObject", type=Type.class, kind=PropertyKind.LIST)
})
public class Data extends DataGen {
}
