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


@Bean(properties = {
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
		@Property(name="extraMethodKeywords"),
		@Property(name="extraFieldKeywords"),
		@Property(name="omitFromToString", type=boolean.class),
		@Property(name="kind", type=PropertyKind.class)
})
public class PropertySpec extends PropertySpecGen {
    @Override
    public void setName(String name) {
        super.setName(name);
        setUpperName(Utils.upperFirstChar(name));
    }
    public String isGet() {
        if ("boolean".equals(getType())) {
            return "is";
        }
        return "get";
    }
    public String getUnmodPrefix() {
        return getKind().getPrefix();
    }
    public String getUnmodSuffix() {
    	return getKind().getSuffix();
    }
    @Override
    public void setPluralName(String pluralName) {
        super.setPluralName(pluralName);
        setUpperPluralName(Utils.upperFirstChar(pluralName));
    }
    @Override
    public boolean isPrimitive() { return false; }

}
