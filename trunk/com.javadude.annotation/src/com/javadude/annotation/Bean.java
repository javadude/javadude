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
 * <p>Allowed attributes and their effects are:</p>
 * <dl>
 *   <dt>superclass</dt>
 *     <dd>Defines the class you would like to extend. If this superclass is in
 *     the same package as the annotated class, you only need to specify its
 *     name. If the superclass is in a different package, you must fully-qualify it.</dd>
 *   <dt>superclassConstructorArgs</dt>
 *     <dd>TBD</dd>
 *   <dt>superclassConstructorSuperCall</dt>
 *     <dd>TBD</dd>
 *   <dt>cloneable</dt>
 *     <dd>specify <code>cloneable=true</code> if you want the generated superclass to define a clone() method and implement the Cloneable interface.</dd>
 *   <dt>overrideParamString</dt>
 *     <dd>TBD</dd>
 *   <dt>defineSimpleEqualsAndHashCode</dt>
 *     <dd>TBD</dd>
 *   <dt>equalsShouldCheckSuperEquals</dt>
 *     <dd>TBD</dd>
 *   <dt>createPropertyMap</dt>
 *     <dd>TBD</dd>
 *   <dt>properties</dt>
 *     <dd>TBD</dd>
 *   <dt>observers</dt>
 *     <dd>TBD</dd>
 *   <dt>delegates</dt>
 *     <dd>TBD</dd>
 *   <dt>nullObjectImplementations</dt>
 *     <dd>TBD</dd>
 *   <dt>reader</dt>
 *     <dd>specifies the default access level of the getter methods generated for any defined properties</dd>
 *   <dt>writer</dt>
 *     <dd>specifies the default access level of the setter methods generated for any defined properties</dd>
 * </dl>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Bean {
    Class<?> superclass() default Void.class;
    String superclassString() default "";
    String superConstructorArgs() default "";
    String superConstructorSuperCall() default "";
    boolean cloneable() default false;
    boolean overrideParamString() default false;
    boolean defineSimpleEqualsAndHashCode() default false;
    boolean equalsShouldCheckSuperEquals() default false;
    boolean createPropertyMap() default false;
    Property[] properties() default { };
    Observer[] observers() default { };
    Delegate[] delegates() default { };
    NullObject[] nullObjectImplementations() default { };
    Access reader() default Access.PUBLIC;
    Access writer() default Access.PUBLIC;
}
