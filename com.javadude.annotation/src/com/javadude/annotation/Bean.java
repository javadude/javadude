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
 * <p>An annotation that requests to generate Bean code for the
 * 		annotated class.</p>
 * <p>This annotation marks a class as a JavaBean and allows generation
 * 		of code based on the specified attributes.
 *
 * <p>If you annotate a class with @Bean, the annotation processor
 * 		will generate a superclass containing the requested generated
 * 		code. The name of the generated class is the same as the
 * 		annotated class with a suffix of "Gen".
 * To use this annotation, you <b>must</b> define your class to extend
 * the generated superclass. For example:</p>
 * <pre>@Bean(...)
 * public class Foo extends FooGen { ...}</pre>
 *
 * <p>If you need to extend another class, you can ask the Bean
 * 		annotation processor to add an extends clause to the generated
 * 		class using the "superclass" attribute. For example, if you
 * 		wanted class Foo to extend class Fee, you would write:</p>
 * <pre>@Bean(superclass="Fee", ...)
 * public class Foo extends FooGen { ...}</pre>
 * <p>The generated superclass would look like</p>
 * <pre>public class FooGen extends Fee {...}</pre>
 * <p><i>If the required superclass is not in the same package as the
 * annotated class, you need to fully qualify it:</i></p>
 * <pre>@Bean(superclass="x.y.Fee",...)</pre>
 *
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Bean {
	/**
	 * <p>Defines the class you would like to extend. The generated
	 * 		superclass will be inserted between the class you are
	 *		annotating and this class.</p>
	 * <p>You must specify the class constant for the class
	 * 		(eg: Foo.class)</p>
	 */
    Class<?> superclass() default Void.class;

    /**
     * <p>If true, the generated superclass will implement
     * 		java.lang.Cloneable and	appropriately override the
     * 		clone() method.</p>
     */
    boolean cloneable() default false;

    /**
     * <p>If true, the generated superclass will define an inner
     * 		interface "PropertyNames" containing all the property
     * 		names in the class. Use of this interface will reduce
     * 		typos when setting up PropertyChangeListeners.</p>
     * <p>For example, to set up a PropertyChangeListener for property
     * 		"name", you would call</p>
     * <pre>Bean bean = ...;
     * bean.addPropertyChangeListener(Bean.PropertyNames.name, ...);
     * </pre>
     */
    boolean definePropertyNameConstants() default false;

    /**
     * <p>If non-zero, the generated code will use this many spaces
     * 		on each indented line instead of a tab.</p>
     */
    int spacesForLeadingTabs() default 0;

    /**
     * <p>If true, the generated superclass will include a simple
     * 		equals() and hashCode() method.</p>
     * @see #equalsShouldCheckSuperEquals()
     */
    boolean defineEqualsAndHashCode() default false;

    /**
     * <p>If true, the generated superclass' equals() method will
     * 		first check super.equals(), and return false if its
     * 		result is false. The generated hashCode() method
     * 		will call super.hashCode() and factor it into its calculation</p>
     * @see #defineSimpleEqualsAndHashCode()
     */
    boolean equalsAndHashCodeCallSuper() default false;

    /**
     * <p>If true, generate a createPropertyMap() method in the
     * 		superclass. This method will generate a
     * 		<code>Map<String, Object><code> that contains property
     * 		name/values for properties defined via an {@link Property}
     * 		annotation inside this bean.</p>
     */
    boolean defineCreatePropertyMap() default false;

    /**
     * <p>A list of {@link Property} annotations that define
     * 		properties to be generated in the generated superclass.</p>
     * @see Property
     */
    Property[] properties() default { };

    /**
     * <p>A list of {@link Observer} annotations that specify
     * 		observer pattern creation in the generated superclass.</p>
     * @see Observer
     */
    Observer[] observers() default { };

    /**
     * <p>A list of {@link Delegate} annotations that specify
     * 		creation of delegate methods in the generated
     * 		superclass.</p>
     * @see Delegate
     */
    Delegate[] delegates() default { };

    /**
     * <p>A list of {@link NullObject} annotations that specify creation
     * 		of null stub methods in the generated superclass.</p>
     * @see NullObject
     */
    NullObject[] nullObjects() default { };
}
