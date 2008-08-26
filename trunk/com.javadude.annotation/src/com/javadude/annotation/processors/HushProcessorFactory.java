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

import java.util.Set;
import java.util.StringTokenizer;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

public class HushProcessorFactory extends BaseAnnotationProcessorFactory {
	public HushProcessorFactory() {
		String annotationsToHush = System.getProperty("annotations.to.hush.during.apt");
		if (annotationsToHush == null)
			return;
		StringTokenizer tokenizer = new StringTokenizer(annotationsToHush, " ,");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			Class<?> annotationClass;
			try {
				annotationClass = Class.forName(token);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Bad annotation class namd in annotations.to.hush: " + e.getMessage());
			}
			add(annotationClass);
		}
    }
    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> atds, AnnotationProcessorEnvironment env) {
        return new HushProcessor(env);
    }
}
