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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.sun.mirror.apt.AnnotationProcessorFactory;

public abstract class BaseAnnotationProcessorFactory implements AnnotationProcessorFactory {
    private final List<String> annotations_ = new ArrayList<String>();

    public BaseAnnotationProcessorFactory(Class<?>... annotations) {
    	for (Class<?> annotation : annotations) {
			annotations_.add(annotation.getName());
		}
    }

    public Collection<String> supportedAnnotationTypes() {
        return annotations_;
    }

    public Collection<String> supportedOptions() {
        return Collections.emptyList();
    }
}