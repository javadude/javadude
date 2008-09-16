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
