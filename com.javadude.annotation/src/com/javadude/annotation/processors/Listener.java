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
		@Property(name="nameWithoutPackage"),
		@Property(name="lowerName"),
		@Property(name="method", type=Method.class, kind=PropertyKind.LIST),
		@Property(name="overriding", type=boolean.class)
})
public class Listener extends ListenerGen {
}
