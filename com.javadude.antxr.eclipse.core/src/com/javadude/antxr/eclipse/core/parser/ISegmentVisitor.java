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
 * Visitor design pattern.
 * @see ISegment#accept(ISegmentVisitor)
 */
public interface ISegmentVisitor {
	
	/**
	 * Visit a specific segment
	 * @param aSegment the segment to visit
	 * @return true if we should visit children
	 */
	boolean visit(ISegment aSegment);
}
