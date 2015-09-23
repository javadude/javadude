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
