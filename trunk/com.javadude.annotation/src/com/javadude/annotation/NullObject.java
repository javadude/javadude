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
package com.javadude.annotation;

/**
 * <p>Specifies a java interface that will be implemented using Null Object methods. If the methods have a return value, they will
 * 	return null, false or 0. If they do not have a return value, they will do nothing.</p>
 */
public @interface NullObject {
	/**
	 * The interface that will be implemented using null-object methods.
	 * Exactly one of type or typeString must be specified.
	 */
    Class<?> type() default Void.class;

    /**
     * The interface that will be implemented using null-object methods.
     * Exactly one of type or typeString must be specified.
     */
    String typeString() default "";

    /**
     * If true, @Override will be added to all generated methods.
     */
    boolean addOverrides() default false;
}
