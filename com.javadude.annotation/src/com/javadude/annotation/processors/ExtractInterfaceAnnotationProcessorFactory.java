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

import com.javadude.annotation.Expose;
import com.javadude.annotation.ExtractInterface;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

public class ExtractInterfaceAnnotationProcessorFactory extends BaseAnnotationProcessorFactory  {
	public ExtractInterfaceAnnotationProcessorFactory() {
		super(ExtractInterface.class, Expose.class);
    }

    @Override
	public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> atds, AnnotationProcessorEnvironment env) {
        return new ExtractInterfaceAnnotationProcessor(env);
    }
}
