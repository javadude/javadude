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
 * <p>An annotation that requests to generate Bean code for the annotated class.</p>
 * <p>This annotation marks a class as a JavaBean and allows generation of
 * code based on the specified attributes.
 *
 * <p>If you annotate a class with @Bean, the annotation processor will generate
 * a superclass containing the requested generated code. The name of the
 * generated class is the same as the annotated class with a suffix of "Gen".
 * To use this annotation, you <b>must</b> define your class to extend
 * the generated superclass. For example:</p>
 * <pre>@Bean(...)
 * public class Foo extends FooGen { ...}</pre>
 *
 * <p>If you need to extend another class, you can ask the Bean annotation
 * processor to add an extends clause to the generated class using the
 * "superclass" attribute. For example, if you wanted class Foo to extend
 * class Fee, you would write:</p>
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
	 * Defines the class you would like to extend.
	 * Only one of superclass or superclassString maybe specified
	 */
    Class<?> superclass() default Void.class;

    /**
     * Defines the class you would like to extend. If this superclass is in
     *   the same package as the annotated class, you only need to specify its
     *   name. If the superclass is in a different package, you must fully-qualify it.
     * Only one of superclass or superclassString maybe specified
     */
    String superclassString() default "";

    /**
     * Parameter definition for the constructor in the generated superclass.
     */
    String superConstructorArgs() default "";

    /**
     * Arguments to the super(...) call that will be generated in the superclass' constructor. Usually used
     *   with superConstructorArgs.
     */
    String superConstructorSuperCall() default "";

    /**
     * If true, the generated superclass will implement Cloneable and appropriately override the clone()
     *   method.
     */
    boolean cloneable() default false;

    /**
     * If non-zero, use this many spaces for each leading tab.
     */
    int spacesForLeadingTabs() default 0;

    /**
     * If true, the superclass will add {@link Override} to the generated paramString method and include
     *   the contents of its superclass' paramString() method.
     */
    boolean overrideParamString() default false;

    /**
     * If true, the generated superclass will include a simple equals() and hashCode() method.
     */
    boolean defineSimpleEqualsAndHashCode() default false;

    /**
     * If true, the generated superclass' equals() method will first check super.equals(), and return false
     *   if its result is false.
     */
    boolean equalsShouldCheckSuperEquals() default false;

    /**
     * If true, generate a createPropertyMap() method in the superclass. This method will generate a
     *   Map<String, Object> that contains property name/values for properties defined via an
     *   {@link Property} annotation inside this bean.
     */
    boolean createPropertyMap() default false;

    /**
     * A list of {@link Property} annotations that define properties to be generated in the generated
     *   superclass.
     */
    Property[] properties() default { };

    /**
     * A list of {@link Observer} annotations that specify observer pattern creation in the generated
     *   superclass.
     */
    Observer[] observers() default { };

    /**
     * A list of {@link Delegate} annotations that specify creation of delegate methods in the generated
     *   superclass.
     */
    Delegate[] delegates() default { };

    /**
     * A list of {@link NullObject} annotations that specify creation of null stub methods in the generated
     *   superclass.
     */
    NullObject[] nullObjectImplementations() default { };

    /**
     * Specifies the default access level of the setter methods generated for any defined properties
     */
    Access reader() default Access.PUBLIC;

    /**
     * Specifies the default access level of the getter methods generated for any defined properties
     */
    Access writer() default Access.PUBLIC;
}
