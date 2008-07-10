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
 * <p>Flags a class to have an interface created for. Methods to expose are
 * marked with &#064;Expose. The interface will be generated in the same package as the class.</p>
 * 
 * <p><b><i>Note: This annotation currently does not work properly for interfaces whose methods use parameterized types for their arguments and return types.
 * 		A later release will correct this.</i></b></p>
 * 
 * <p>This annotation supports the following attributes:</p>
 * <dl>
 * 		<dt>name</dt>
 * 			<dd>The name of the interface to generate.</dd>
 * 		<dt>nonPublic</dt>
 * 			<dd>If set to true, the generated interface will not be public (it will not be accessible outside the package)</dd>
 * 		<dt>exposeAllPublicMethods</dt>
 * 			<dd>If set to true, all public methods will be extracted into the interface.</dd>
 * </dl>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ExtractInterface {
    String name();
    String[] superinterfaces() default {};
    boolean nonPublic() default false;
    boolean exposeAllPublicMethods() default false;
}
