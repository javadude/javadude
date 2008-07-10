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

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.javadude.antxr.Parser;
import com.javadude.antxr.Token;
import com.javadude.antxr.TokenStream;
import com.javadude.antxr.TokenStreamException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * A simple implementation of XMLPullTokenStream that uses kxml as its
 * implementation
 */
public class BasicKXml2XMLPullTokenStream implements TokenStream {
    private XMLPullTokenStream tokenStream;

    private static final Class<?>[] NO_PARAMETERS = new Class[] {};

    private static final Object[] NO_ARGUMENTS = new Object[] {};

    /**
     * Creates an instance of the KXml token stream
     * @param xmlToParse the xml stream to parse
     * @param parserClass the generated parser class
     * @param namespaceAware do we want a namespace aware parse
     *
     */
    public BasicKXml2XMLPullTokenStream(Reader xmlToParse, Class<? extends Parser> parserClass,
                                        boolean namespaceAware) {
        // Create the XmlPull parser (really part of the scanner)

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance("org.kxml2.io.KXmlParser", null);
            factory.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, namespaceAware);

            XmlPullParser parser = factory.newPullParser();
            parser.setInput(xmlToParse);

            Field field = parserClass.getField("_tokenNames");
            String[] tokenNames = (String[])field.get(null);

            Method getNameSpaceMapMethod = parserClass.getMethod("getNamespaceMap", NO_PARAMETERS);
            @SuppressWarnings("unchecked")
            Map<String, String> namespaceMap = (Map<String, String>)getNameSpaceMapMethod.invoke(null, NO_ARGUMENTS);

            // Create our scanner (using the xml pull parser)
            tokenStream = new XMLPullTokenStream(tokenNames, namespaceMap, parser);
        }
        catch (XmlPullParserException e) {
            throw new RuntimeException("Exception thrown setting up KXml parser. See nested exception.",e);
        }
        catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Cannot find _tokenNames in the parser class -- is it an XML parser?");
        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Cannot find getNamespaceMap() in the parser class -- is it an XML parser?");
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot access _tokenNames or getNamespaceMap() in the parser class (they should be static)");
        }
        catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot access _tokenNames or getNamespaceMap() in the parser class (they should be public)");
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException("Exception thrown when running getNamespaceMap(). See nested exception.", e);
        }


    }

    /** {@inheritDoc} */
    public Token nextToken() throws TokenStreamException {
        return tokenStream.nextToken();
    }

}
