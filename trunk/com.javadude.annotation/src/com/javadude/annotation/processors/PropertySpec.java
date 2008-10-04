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

import com.javadude.annotation.Bean;
import com.javadude.annotation.Property;
import com.javadude.annotation.PropertyKind;


@Bean(createPropertyMap=true,
	properties = {
		@Property(name="name"),
		@Property(name="upperName"),
		@Property(name="writerAccess"),
		@Property(name="readerAccess"),
		@Property(name="type"),
		@Property(name="intConversion"),
		@Property(name="notNull", type=boolean.class),
		@Property(name="readable", type=boolean.class),
		@Property(name="writeable", type=boolean.class),
		@Property(name="bound", type=boolean.class),
		@Property(name="primitive", type=boolean.class),
		@Property(name="pluralName"),
		@Property(name="upperPluralName"),
		@Property(name="baseType"),
		@Property(name="keyType"),
		@Property(name="isOrGet"),
		@Property(name="extraMethodKeywords"),
		@Property(name="unmodPrefix"),
		@Property(name="unmodSuffix"),
		@Property(name="extraFieldKeywords"),
		@Property(name="omitFromToString", type=boolean.class),
		@Property(name="kind", type=PropertyKind.class)
})
public class PropertySpec extends PropertySpecGen {
}
