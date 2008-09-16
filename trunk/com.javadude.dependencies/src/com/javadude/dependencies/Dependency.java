/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.dependencies;

import org.eclipse.jdt.core.IJavaProject;

import com.javadude.annotation.Bean;
import com.javadude.annotation.Property;

@Bean(defineSimpleEqualsAndHashCode=true,
	  properties = {
		@Property(name="source", type=IJavaProject.class),
		@Property(name="target", type=IJavaProject.class),
		@Property(name="exported", type=boolean.class),
})
public class Dependency extends DependencyGen {
    public Dependency(IJavaProject source, IJavaProject target, boolean exported) {
    	setSource(source);
    	setTarget(target);
    	setExported(exported);
    }
}
