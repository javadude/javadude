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

import java.util.Map;

import com.javadude.annotation.Bean;
import com.javadude.annotation.Property;
import com.javadude.annotation.PropertyKind;

@Bean(superclass=BasePushable.class,defineCreatePropertyMap=true,
	properties = {
		@Property(name="name"),
		@Property(name="writerAccess"),
		@Property(name="readerAccess"),
		@Property(name="type"),
		@Property(name="notNull", type=boolean.class),
		@Property(name="readable", type=boolean.class),
		@Property(name="writeable", type=boolean.class),
		@Property(name="bound", type=boolean.class),
		@Property(name="primitive", type=boolean.class),
		@Property(name="pluralName"),
		@Property(name="keyType"),
		@Property(name="extraMethodKeywords"),
		@Property(name="extraFieldKeywords"),
		@Property(name="omitFromToString", type=boolean.class),
		@Property(name="kind", type=PropertyKind.class)
	}
)
public class PropertySpec extends PropertySpecGen implements Pushable {
		public boolean isSimple() {
			return getKind().isSimple();
		}
		public boolean isList() {
			return getKind().isList();
		}
		public boolean isSet() {
			return getKind().isSet();
		}
		public boolean isMap() {
			return getKind().isMap();
		}
		public boolean isBoolean() {
			return "boolean".equals(getType());
		}
		public boolean isShort() {
			return "short".equals(getType());
		}
		public boolean isDouble() {
			return "double".equals(getType());
		}
		public boolean isLong() {
			return "long".equals(getType());
		}
		public boolean isInt() {
			return "int".equals(getType());
		}
		public boolean isChar() {
			return "char".equals(getType());
		}
		public boolean isByte() {
			return "byte".equals(getType());
		}
		public boolean isFloat() {
			return "float".equals(getType());
		}
		@Override
		public Map<String, Object> createPropertyMap() {
			Map<String, Object> map = super.createPropertyMap();
			map.put("simple", isSimple());
			map.put("list", isList());
			map.put("set", isSet());
			map.put("map", isMap());
			map.put("boolean", isBoolean());
			map.put("short", isShort());
			map.put("double", isDouble());
			map.put("long", isLong());
			map.put("int", isInt());
			map.put("char", isChar());
			map.put("byte", isByte());
			map.put("float", isFloat());
			return map;
		}
}
