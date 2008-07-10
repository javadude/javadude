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

import java.util.ArrayList;
import java.util.List;

import com.javadude.annotation.Bean;
import com.javadude.annotation.Property;

@Bean(properties = {
		@Property(name="name"),
		@Property(name="lowerName"),
		@Property(name="overriding", type=boolean.class)
})
public class Listener extends ListenerGen {
    private List<Method> methods_ = new ArrayList<Method>();
    @Override
    public void setName(String name) {
    	super.setName(name);
        setLowerName(Utils.lowerFirstChar(getNameWithoutPackage()));
    }

    public List<Method> getMethods() {
        return methods_;
    }
    public String getNameWithoutPackage() {
        int i = getName().lastIndexOf('.');
        if (i == -1) {
            return getName();
        }
        return getName().substring(i + 1);
    }
}
