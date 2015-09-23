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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.sun.mirror.apt.AnnotationProcessorFactory;

public abstract class BaseAnnotationProcessorFactory implements AnnotationProcessorFactory {
    private final List<String> annotations_ = new ArrayList<String>();

    public BaseAnnotationProcessorFactory() {}
   	public BaseAnnotationProcessorFactory(Class<?>... annotations) {
   		add(annotations);
    }

   	protected void add(Class<?>... annotations) {
   		for (Class<?> annotation : annotations) {
   			annotations_.add(annotation.getName());
   		}
   	}
    @Override
	public Collection<String> supportedAnnotationTypes() {
        return annotations_;
    }

    @Override
	public Collection<String> supportedOptions() {
        return Collections.emptyList();
    }
}
