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
		@Property(name="readable", type=boolean.class),
		@Property(name="writeable", type=boolean.class),
		@Property(name="bound", type=boolean.class),
		@Property(name="primitive", type=boolean.class),
		@Property(name="pluralName"),
		@Property(name="upperPluralName"),
		@Property(name="baseType"),
		@Property(name="keyType"),
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
