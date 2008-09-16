/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Based on the ANTLR parser generator by Terence Parr, http://antlr.org
 *   Ric Klaren <klaren@cs.utwente.nl>
 *   Scott Stanchfield - Modifications for XML Parsing
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
