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
package com.javadude.antxr;

public class StringUtils {
    /** General-purpose utility function for removing
     * characters from back of string
     * @param s The string to process
     * @param c The character to remove
     * @return The resulting string
     */
    static public String stripBack(String s, char c) {
        while (s.length() > 0 && s.charAt(s.length() - 1) == c) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /** General-purpose utility function for removing
     * characters from back of string
     * @param s The string to process
     * @param remove A string containing the set of characters to remove
     * @return The resulting string
     */
    static public String stripBack(String s, String remove) {
        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < remove.length(); i++) {
                char c = remove.charAt(i);
                while (s.length() > 0 && s.charAt(s.length() - 1) == c) {
                    changed = true;
                    s = s.substring(0, s.length() - 1);
                }
            }
        } while (changed);
        return s;
    }

    /** General-purpose utility function for removing
     * characters from front of string
     * @param s The string to process
     * @param c The character to remove
     * @return The resulting string
     */
    static public String stripFront(String s, char c) {
        while (s.length() > 0 && s.charAt(0) == c) {
            s = s.substring(1);
        }
        return s;
    }

    /** General-purpose utility function for removing
     * characters from front of string
     * @param s The string to process
     * @param remove A string containing the set of characters to remove
     * @return The resulting string
     */
    static public String stripFront(String s, String remove) {
        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < remove.length(); i++) {
                char c = remove.charAt(i);
                while (s.length() > 0 && s.charAt(0) == c) {
                    changed = true;
                    s = s.substring(1);
                }
            }
        } while (changed);
        return s;
    }

    /** General-purpose utility function for removing
     * characters from the front and back of string
     * @param s The string to process
     * @param head exact string to strip from head
     * @param tail exact string to strip from tail
     * @return The resulting string
     */
    public static String stripFrontBack(String src, String head, String tail) {
        int h = src.indexOf(head);
        int t = src.lastIndexOf(tail);
        if (h == -1 || t == -1) {
	        return src;
        }
        return src.substring(h + 1, t);
    }
}
