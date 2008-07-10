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

import java.util.HashSet;
import java.util.Set;

import com.javadude.annotation.Bean;
import com.javadude.annotation.Property;

@Bean(properties = {
		@Property(name="name"),
		@Property(name="upperName"),
		@Property(name="args"),
		@Property(name="argDecls"),
		@Property(name="returnType"),
		@Property(name="returnOrNot"),
		@Property(name="throwsClause")
})
public class Method extends MethodGen {
    private static final Set<String> NUMBER_TYPES = new HashSet<String>();
    static {
        NUMBER_TYPES.add("byte");
        NUMBER_TYPES.add("short");
        NUMBER_TYPES.add("int");
        NUMBER_TYPES.add("long");
        NUMBER_TYPES.add("float");
        NUMBER_TYPES.add("double");
    }
    @Override
    public void setName(String name) {
        super.setName(name);
        super.setUpperName(Utils.upperFirstChar(name));
    }
    @Override
    public void setReturnType(String returnType) {
        super.setReturnType(returnType);
        if ("void".equals(returnType)) {
            setReturnOrNot("");
        } else {
            setReturnOrNot("return ");
        }
    }
    public String getNullBody() {
        if ("void".equals(getReturnType())) {
            return "// null object implementation; do nothing";
        }
        if ("boolean".equals(getReturnType())) {
            return "return false; // null object implementation";
        }
        if ("char".equals(getReturnType())) {
            return "return ' '; // null object implementation";
        }
        if (NUMBER_TYPES.contains(getReturnType())) {
            return "return 0; // null object implementation";
        }
        return "return null; // null object implementation";
    }
}
