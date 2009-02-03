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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Flags a class to have an interface created for. Methods to expose are
 * marked with &#064;Expose. The interface will be generated in the same package as the class.</p>
 *
 * <p><b><i>Note: This annotation currently does not work properly for interfaces whose methods use parameterized types for their arguments and return types.
 * 		A later release will correct this.</i></b></p>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ExtractInterface {
	/**
	 * The name of the interface to generate.
	 */
    String name();

    /**
     * Any super-interfaces you wish to define in the generated interface.
     */
    String[] superinterfaces() default {};

    /**
     * If true, the generated interface will not be public.
     */
    boolean nonPublic() default false;

    /**
     * If true, all public methods in the annotated class will be extracted into the interface.
     */
    boolean exposeAllPublicMethods() default false;
}
