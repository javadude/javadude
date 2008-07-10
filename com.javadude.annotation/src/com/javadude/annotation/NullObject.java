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
 * 
 * <p>This annotation supports the following attributes:</p>
 * <dl>
 * 		<dt>type</dt>
 * 			<dd>The interface that will be implemented using null-object methods</dd>
 * 		<dt>addOverrides</dt>
 * 			<dd>If set to true, an &#064;Override annotation will be added to generated methods</dd>
 * </dl>
 */
public @interface NullObject {
    Class<?> type() default Void.class;
    String typeString() default "";
    boolean addOverrides() default false;
}
