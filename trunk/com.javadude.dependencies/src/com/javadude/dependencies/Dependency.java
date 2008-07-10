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
