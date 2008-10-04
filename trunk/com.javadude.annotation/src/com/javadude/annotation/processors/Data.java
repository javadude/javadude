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

@Bean(createPropertyMap=true,
	properties = {
		@Property(name="packageName"),
		@Property(name="className"),
		@Property(name="date", type=Date.class),
		@Property(name="classAccess"),
		@Property(name="extendsClause"),
		@Property(name="cloneable", type=boolean.class),
		@Property(name="spacesForLeadingTabs", type=int.class),
		@Property(name="cloneableClause"),
		@Property(name="atLeastOneBound", type=boolean.class),
		@Property(name="equalsShouldCheckSuperEquals", type=boolean.class),
		@Property(name="superConstructorSuperCall"),
		@Property(name="superConstructorArgs"),
		@Property(name="year", type=int.class),
		@Property(name="paramStringOverridden", type=boolean.class),
		@Property(name="defineSimpleEqualsAndHashCode", type=boolean.class),
		@Property(name="createPropertyMap", type=boolean.class),
		@Property(name="property", plural="properties", type=PropertySpec.class, kind=PropertyKind.LIST),
		@Property(name="listener", type=Listener.class, kind=PropertyKind.LIST),
		@Property(name="defaultMethod", type=Method.class, kind=PropertyKind.LIST),
		@Property(name="delegate", type=DelegateSpec.class, kind=PropertyKind.LIST),
		@Property(name="nullImplementation", type=Listener.class, kind=PropertyKind.LIST)
})
public class Data extends DataGen {
}
