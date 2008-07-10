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
package com.javadude.antxr.eclipse.core.parser;

/**
 * General model interface for nodes in the outline view
 */
public interface IModel {
	/** A constant array of zero children (many nodes have no kids */
    public static final Object[] NO_CHILDREN = new Object[0];
	
	/**
	 * Get the name of the current node
	 * @return the name of the current node
	 */
	String getName();
	
	/**
	 * Get the parent node
	 * @return the parent node
	 */
	Object getParent();
	
	/**
	 * Do we have children
	 * @return true if child nodes exist
	 */
	boolean hasChildren();
	
	/**
	 * Get the child nodes
	 * @return the child nodes
	 */
	Object[] getChildren();
}
