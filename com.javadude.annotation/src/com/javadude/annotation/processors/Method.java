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
		@Property(name="throwsClause"),
		@Property(name="access"),
		@Property(name="abstract", type=boolean.class)
})
public class Method extends MethodGen {
    private static final Set<String> NUMBER_TYPES = new HashSet<String>();
    static {
        Method.NUMBER_TYPES.add("byte");
        Method.NUMBER_TYPES.add("short");
        Method.NUMBER_TYPES.add("int");
        Method.NUMBER_TYPES.add("long");
        Method.NUMBER_TYPES.add("float");
        Method.NUMBER_TYPES.add("double");
    }
    @Override
    public void setName(String name) {
        super.setName(name);
        super.setUpperName(Utils.upperFirstChar(name));
    }
    public String getSymbolAfterDecl() {
    	if (isAbstract())
    		return ";";
    	return " {";
    }
    public String getQualifiers() {
    	if (isAbstract())
    		return "abstract ";
    	return "";
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
        if (Method.NUMBER_TYPES.contains(getReturnType())) {
            return "return 0; // null object implementation";
        }
        return "return null; // null object implementation";
    }
}
