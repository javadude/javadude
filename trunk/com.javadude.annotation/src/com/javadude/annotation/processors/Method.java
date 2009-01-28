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

//@Bean(createPropertyMap=true,
//	properties = {
//		@Property(name="name"),
//		@Property(name="args"),
//		@Property(name="argDecls"),
//		@Property(name="genericDecls"),
//		@Property(name="returnType"),
//		@Property(name="throwsClause"),
//		@Property(name="modifiers"),
//		@Property(name="nullBody"),
//		@Property(name="abstract", type=boolean.class),
//})
public class Method extends MethodGen implements Pushable {
	//	public boolean isReturns() {
	//		return !"void".equals(getReturnType());
	//	}
	//	@Override
	//	public Map<String, Object> createPropertyMap() {
	//		Map<String, Object> map = super.createPropertyMap();
	//		map.put("returns", isReturns());
	//		return map;
	//	}
}
