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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.javadude.antxr.Parser;
import com.javadude.antxr.Token;
import com.javadude.antxr.TokenStream;
import com.javadude.antxr.TokenStreamException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * A Simple version of an XML token stream that uses the Xerces SAX parser.
 * This parser can validate your XML against a schema.
 *
 * You can use this version if:
 * <ul>
 *   <li>You want to use Xerces to parse your XML</li>
 *   <li>You have Xerces in your classpath</li>
 *   <li>You only want to configure the following Xerces options:
 *     <ul>
 *       <li>namespace awareness (are there namespaces in the XML to parse?)</li>
 *       <li>validation (are there namespaces in the XML to parse?)</li>
 *       <li>you want to specify schema or non-schema validation</li>
 *     </ul>
 *   </li>
 * </ul>
 * @author scott
 *
 */
public class BasicXercesXMLTokenStream implements TokenStream {
    private XMLTokenStream xmlTokenStream;
    private static final Class<?>[] NO_PARAMETERS = new Class[] {};
    private static final Object[] NO_ARGUMENTS = new Object[] {};

    /**
     * Create the xml token stream. This version does not gate the number of
     * tokens read by the SAX parser. <i>Note that this can cause the entire
     * XML to be read into memory!</i> If you have a small XML document to
     * parse, this is more efficient, but large XML documents can cause memory
     * problems. If you want to use a large XML file, call the other constructor
     * and pass it a maximumQueueSize and resumeQueueSize.
     * @param xmlToParse The XML input to parse
     * @param parserClass Your parser class. The parser must have been generated
     *                    with the xmlMode=true option specified
     * @param namespaceAware true if the XML (and your grammar) uses namespaces
     * @param validating true if you want SAX to validate your XML
     * @param validateWithSchema  true if you want to validate using an XML schema
     * @throws IllegalArgumentException if you pass in an invalid parser
     */
    public BasicXercesXMLTokenStream(Reader xmlToParse,
             Class<? extends Parser> parserClass,
             boolean namespaceAware,
             boolean validating,
             boolean validateWithSchema) {
        this(xmlToParse, parserClass, namespaceAware, validating, validateWithSchema, -1, -1);
    }
    /**
     * Create the xml token stream. This version does not gate the number of
     * tokens read by the SAX parser. <i>Note that this can cause the entire
     * XML to be read into memory!</i> If you have a small XML document to
     * parse, this is more efficient, but large XML documents can cause memory
     * problems. If you want to use a large XML file, call the other constructor
     * and pass it a maximumQueueSize and resumeQueueSize.
     * @param xmlToParse The XML input to parse
     * @param parserClass Your parser class. The parser must have been generated
     *                    with the xmlMode=true option specified
     * @param namespaceAware true if the XML (and your grammar) uses namespaces
     * @param validating true if you want SAX to validate your XML
     * @param validateWithSchema  true if you want to validate using an XML schema
     * @param maximumQueueSize the maximum number of tokens you want to place
     *                         in the blocking queue ready for the ANTXR parser
     *                         to fetch. This will put the SAX parse on hold
     *                         until resumeQueue size is reached.
     * @param resumeQueueSize The number of buffered tokens at which you will
     *                        resume the SAX parse
     * @throws IllegalArgumentException if you pass in an invalid parser
     */
    public BasicXercesXMLTokenStream(Reader xmlToParse,
                                     Class<? extends Parser> parserClass,
                                     boolean namespaceAware,
                                     boolean validating,
                                     boolean validateWithSchema,
                                     int maximumQueueSize,
                                     int resumeQueueSize) {
        try {
            // Create the SAX parser (really part of the scanner)
            System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                               "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
            System.setProperty("javax.xml.parsers.SAXParserFactory",
                               "org.apache.xerces.jaxp.SAXParserFactoryImpl");

            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(namespaceAware);
            factory.setValidating(validating);
            factory.setFeature("http://apache.org/xml/features/validation/schema", validateWithSchema);

            SAXParser parser= factory.newSAXParser();

            Field field = parserClass.getField("_tokenNames");
            String[] tokenNames = (String[])field.get(null);

            Method getNameSpaceMapMethod = parserClass.getMethod("getNamespaceMap", NO_PARAMETERS);
            @SuppressWarnings("unchecked")
            Map<String, String> namespaceMap = (Map<String, String>) getNameSpaceMapMethod.invoke(null, NO_ARGUMENTS);

            // Create our scanner (using the SAX parser)
            xmlTokenStream =
                new XMLTokenStream(tokenNames, namespaceMap,
                                   new InputSource(xmlToParse), parser, null, null);
        }
        catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Cannot find _tokenNames in the parser class -- is it an XML parser?");
        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Cannot find getNamespaceMap() in the parser class -- is it an XML parser?");
        }
        catch (ParserConfigurationException e) {
            throw new RuntimeException("Cannot configure the SAX parser. See nested exception.", e);
        }
        catch (SAXException e) {
            throw new RuntimeException("Error building SAX parser. See nested exception.", e);
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

    /**
     * State whether the given token is an XML start tag
     * @param token the token to check
     * @return true if it's a start tag, false otherwise
     */
    public boolean isStartTag(Token token) {
        return xmlTokenStream.isStartTag(token);
    }

    /** {@inheritDoc} */
    public Token nextToken() throws TokenStreamException {
        return xmlTokenStream.nextToken();
    }
}
