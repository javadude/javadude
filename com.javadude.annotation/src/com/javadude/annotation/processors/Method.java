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

@Bean(createPropertyMap=true,
	properties = {
		@Property(name="name"),
		@Property(name="upperName"),
		@Property(name="args"),
		@Property(name="argDecls"),
		@Property(name="returnType"),
		@Property(name="returnOrNot"),
		@Property(name="throwsClause"),
		@Property(name="access"),
		@Property(name="qualifiers"),
		@Property(name="nullBody"),
		@Property(name="symbolAfterDecl"),
		@Property(name="abstract", type=boolean.class)
})
public class Method extends MethodGen {
}
