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
 * Describes the access level of generated getter and setter methods.
 * <p>You can specify the access level in the Bean or Property annotations.</p>
 * <p>In the {@link Bean} annotation, the value you specify for reader or writer will be
 *   used as the default for all properties defined in the bean.</p>
 * <p>If the {@link Property} annotation, the value you specify for reader or writer will
 *   be used as the value for that property only.</p>
 * <p>Note that there is no "PRIVATE" access value. Because the annotations generate
 *   code in a superclass, there would be no way to use the generated getter or setter,
 *   so PRIVATE is unnecessary.</p>
 * <p>Some examples:</p>
 * <pre>@{@link Bean}(writer=Access.PROTECTED, reader=Access.NONE, ...)</pre>
 * <p>defines that by default, properties in this bean will be given a PROTECTED setter method
 * and no getter method.</p>
 * <pre>@{@link Bean}(...)</pre>
 * <p>defines that by default, all properties will have PUBLIC getters and setters.</p>
 * <pre>@{@link Property}(name="x", writer=Access.PROTECTED)</pre>
 * <p>defines a property named "x" with a protected setter. Its getter will default to the
 * access defined in the Bean annotation containing the {@link Property}.</p>
 *
 * @see Bean
 * @see Property
 */
public enum Access {
    /** <p>Indicates that you do not want a getter or setter. For example:</p>
     *     <pre>@{@link Property}(name="name", writer = Access.NONE)</pre>
     *  <p>would create a read-only property called "name"; it would have a getName() method, but not a setName(String name) method.</p>
     */
    NONE,

    /** <p>Indicates that you want the reader or writer to be declared PUBLIC.</p>
     *  <p>This is the default value for reader and writer access in the {@link Bean} annotation.</p>
     */
    PUBLIC,

    /** Indicates that you want the reader or writer to be declared PROTECTED.</p>
     *  This is the default value for reader and writer access in the {@link Bean} annotation.</p>
     */
    PROTECTED,

    /** Indicates that you want the reader or writer to be declared without an access modified (package-level access).</p>
     *  This is the default value for reader and writer access in the {@link Bean} annotation.</p>
     */
    PACKAGE;

    /**
     * Converts the specified access into the access modifier to put in the generated code.
     * @return an access modifier string (public, protected, <none>)
     */
    public String getModifier() {
        switch (this) {
            case PUBLIC:
                return "public ";
            case PROTECTED:
                return "protected ";
            default:
                return "";
        }
    }
}
