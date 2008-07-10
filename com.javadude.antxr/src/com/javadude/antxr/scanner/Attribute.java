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
 *
 * Contributors:
 *   Based on the ANTLR parser generator by Terence Parr, http://antlr.org
 *   Ric Klaren <klaren@cs.utwente.nl>
 *******************************************************************************/
package com.javadude.antxr.scanner;

/**
 * An attribute in an XML tag.
 */
public class Attribute {
	private String namespace;
	private String localName;
	private String value;
	private String type; // should we keep this?
	
	/**
	 * Create an attribute
	 * @param namespace The attribute's namespace
	 * @param localName The local name of the attribute
	 * @param value The value of the attribute
	 * @param type The type of the attribute
	 */
	public Attribute(String namespace, String localName, String value,
			String type) {
		super();
		this.namespace = namespace;
		this.localName = localName;
		this.value = value;
		this.type = type;
	}

	/**
	 * @return Returns the attribute's localName.
	 */
	public String getLocalName() {
		return localName;
	}

	/**
	 * @return Returns the attribute's namespace.
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @return Returns the attribute's type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return Returns the attribute's value.
	 */
	public String getValue() {
		return value;
	}
}
