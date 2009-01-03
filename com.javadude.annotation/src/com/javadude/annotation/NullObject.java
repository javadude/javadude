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
}
