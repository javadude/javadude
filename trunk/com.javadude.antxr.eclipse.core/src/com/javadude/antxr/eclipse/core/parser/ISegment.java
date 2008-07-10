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
 * A segment in the file to track in the outline
 */
public interface ISegment {

	/**
	 * Get the id of the segment
	 * @return the segment id
	 */
	String getUniqueID();
	
	/**
	 * Where does the segment start
	 * @return the start line of the segment
	 */
	int getStartLine();

	/**
	 * Where does the line end
	 * @return The end line of the segment
	 */
	int getEndLine();

	/**
	 * Visitor design pattern.
	 * @param aVisitor the visitor to accept
	 * @return true if we should visit children
	 * @see ISegmentVisitor#visit(ISegment)
	 */
	boolean accept(ISegmentVisitor aVisitor);
}
