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
package com.javadude.antxr.eclipse.ui.editor;

import com.javadude.antxr.eclipse.ui.AntxrColorProvider;
import com.javadude.antxr.eclipse.ui.editor.outline.AntxrOutlineLabelProvider;
import com.javadude.antxr.eclipse.ui.editor.text.AntxrCodeScanner;

import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.viewers.ILabelProvider;

/**
 * The EditorEnvironment maintains singletons used by the ANTXR editor.
 *
 * @author Torsten Juergeleit
 */
public class EditorEnvironment {

	private static AntxrColorProvider fColorProvider;
	private static ITokenScanner fCodeScanner;
	private static ILabelProvider fLabelProvider;

	private static int fRefCount = 0;

	/**
	 * A connection has occured - initialize the receiver if it is the first
	 * activation.
	 */
	public static void connect() {
		if (++fRefCount == 1) {
			fColorProvider = new AntxrColorProvider();
			fCodeScanner = new AntxrCodeScanner(fColorProvider);
			fLabelProvider = new AntxrOutlineLabelProvider();
		}
	}
	
	/**
	 * A disconnection has occured - clear the receiver if it is the last
	 * deactivation.
	 */
	public static void disconnect() {
		if (--fRefCount == 0) {
			fColorProvider.dispose();
			fColorProvider = null;
			fCodeScanner = null;
			fLabelProvider.dispose();
			fLabelProvider = null;
		}
	}
	
	/**
	 * Returns the singleton color provider.
	 * @return the color provider
	 */
	public static AntxrColorProvider getColorProvider() {
		return fColorProvider;
	}
	
	/**
	 * Returns the singleton scanner.
	 * @return the token scanner
	 */
	public static ITokenScanner getCodeScanner() {
		return fCodeScanner;
	}
	
	/**
	 * Returns the singleton label provider.
	 * @return the label provider
	 */
	public static ILabelProvider getLabelProvider() {
		return fLabelProvider;
	}
}
