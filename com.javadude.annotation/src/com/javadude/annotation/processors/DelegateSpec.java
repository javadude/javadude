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

import com.javadude.annotation.Bean;
import com.javadude.annotation.Property;


@Bean(overrideParamString=true,
	  superclass=Listener.class,
	  properties = {
		@Property(name="accessor"),
		@Property(name="instantiateType"),
		@Property(name="needToDefine", type=boolean.class)
})
public class DelegateSpec extends DelegateSpecGen {
	// nothing to add
}
