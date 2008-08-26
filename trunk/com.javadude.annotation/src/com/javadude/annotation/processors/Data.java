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

import java.util.Calendar;

import com.javadude.annotation.Access;
import com.javadude.annotation.Bean;
import com.javadude.annotation.Property;
import com.javadude.annotation.PropertyKind;

@Bean(properties = {
		@Property(name="bean", type=Bean.class, reader=Access.PROTECTED),
		@Property(name="packageName"),
		@Property(name="className"),
		@Property(name="classAccess"),
		@Property(name="atLeastOneBound", type=boolean.class),
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
	private String superClass_;

	public void setSuperClass(String superClass) {
    	superClass_ = superClass;
    }
	public int getYear() { return Calendar.getInstance().get(Calendar.YEAR); }
    public String getExtendsClause() {
        if (superClass_ == null) {
            return "";
        }
        return "extends " + superClass_;
    }
    public boolean isCloneable() {
        return getBean().cloneable();
    }
    public String getCloneableClause() {
        if (isCloneable()) {
            return " implements java.lang.Cloneable";
        }
        return "";
    }
    public boolean isEqualsShouldCheckSuperEquals() { return getBean().equalsShouldCheckSuperEquals(); }
    public String getSuperConstructorSuperCall() { return getBean().superConstructorSuperCall(); }
    public String getSuperConstructorArgs() { return getBean().superConstructorArgs(); }
}
