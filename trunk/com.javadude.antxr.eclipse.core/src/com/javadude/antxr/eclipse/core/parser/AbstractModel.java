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
 * General model for outline view
 */
public abstract class AbstractModel implements IModel, ISegment {
	private String fName;
	private IModel fParent;
	private int fStartLine;
	private int fEndLine;
	
	protected AbstractModel(String aName, IModel aParent) {
	    fName = aName;
	    fParent = aParent;
	}

	/**
	 * @see IModel#getName()
	 */
	public String getName() {
	    return fName;
	}
	
	/**
	 * @see IModel#getParent()
	 */
	public Object getParent() {
	    return fParent;
	}

	/**
	 * Set the start line
	 * @param aLine the start line
	 */
	public void setStartLine(int aLine) {
	    fStartLine = aLine;
	}
	
    /**
     * @see ISegment#getStartLine()
     */
	public int getStartLine() {
	    return fStartLine;
	}

	/**
	 * Set the end line
	 * @param aLine the end line
	 */
	public void setEndLine(int aLine) {
	    fEndLine = aLine;
	}
	
    /**
     * @see ISegment#getEndLine()
     */
	public int getEndLine() {
	    return fEndLine;
	}
}
