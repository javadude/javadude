/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield, based on ANTLR-Eclipse plugin
 *   by Torsten Juergeleit.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors
 *    Torsten Juergeleit - original ANTLR Eclipse plugin
 *    Scott Stanchfield - modifications for ANTXR
 *******************************************************************************/
package com.javadude.antxr;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.javadude.antxr.eclipse.core.AntxrCorePlugin;

/**
 * Replacement ANTXR tool (main entry point) for Eclipse integration
 */
public class AntxrTool extends Tool {
    private Map sourceMaps;
    private String[] fPreprocessedArgs = null;
    private LLkAnalyzer fAnalyzer = null;
    private MakeGrammar fBehavior = null;
    private ANTXRParser fParser = null;
    private Vector<String> fFiles = new Vector<String>();
    private Vector<PrintWriter> fWriters = new Vector<PrintWriter>();

    /**
     * Create the antxr tool instance
     */
    public AntxrTool() {
        setFileLineFormatter(new MarkerFormatter());
    }

    /**
     * Returns iterator for all files created during last code generation.
     * @return an iterator of all generated files
     */
    public Iterator files() {
        return fFiles.iterator();
    }

    /** {@inheritDoc} */
    public void fatalError(String aMessage) {
        System.err.println(FileLineFormatter.getFormatter().
                           getFormatString(null, -1, -1) + aMessage);
        throw new IllegalStateException();
    }

    /** {@inheritDoc} */
    public void error(String aMessage) {
        System.err.println(FileLineFormatter.getFormatter().
                           getFormatString(null, -1, -1) + aMessage);
        hasError = true;
    }

    /** {@inheritDoc} */
    public void warning(String aMessage) {
        System.err.println(FileLineFormatter.getFormatter().
                           getFormatString(null, -1, -1) + "warning: " +
                           aMessage);
    }

    /** {@inheritDoc} */
    public void warning(String[] aMessageLines, String aFile, int aLine,
                         int aColumn) {
        if (aMessageLines != null && aMessageLines.length != 0) {
            StringBuffer message = new StringBuffer(aMessageLines[0]);
            for (int i = 1; i < aMessageLines.length; i++) {
                String line = aMessageLines[i];
                int pos = 0;
                while (Character.isWhitespace(line.charAt(pos))) {
                    pos++;
                }
                message.append(" ");
                message.append(line.substring(pos));
            }
            System.err.println(FileLineFormatter.getFormatter().
                               getFormatString(aFile, aLine, aColumn) +
                               "warning: " + message.toString());
        }
    }

    /** {@inheritDoc} */
    public void toolError(String aMessage) {
        System.err.println(FileLineFormatter.getFormatter().
                           getFormatString(null, -1, -1) + aMessage);
    }

    /** {@inheritDoc} */
    public PrintWriter openOutputFile(String aFileName) throws IOException {
        if (!fFiles.contains(aFileName)) {
            fFiles.add(aFileName);
        }
        PrintWriter writer = new PrintWriter(new FileWriter(outputDir +
                            System.getProperty("file.separator") + aFileName));
        fWriters.add(writer);
        return writer;
    }

    /**
     * Perform preprocessing on the grammar file.
     * Can only be called from main().
     * @param anArgs  the command-line arguments passed to main()
     * @return true if there was an error, false otherwise
     */
    public boolean preprocess(String[] anArgs) {

        // run the preprocessor to handle inheritance first.
        com.javadude.antxr.preprocessor.Tool preTool = new com.javadude.antxr.preprocessor.Tool(this,
                                                                      anArgs);
        if (preTool.preprocess()) {
            fPreprocessedArgs = preTool.preprocessedArgList();
        } else {
            fPreprocessedArgs = null;
            hasError = true;
        }
        return hasError;
    }

    /**
     * Parse the grammar file.
     * Can only be called after calling preprocess().
     * @return true if there was an error; false otherwise
     * @throws CoreException If eclipse got upset
     */
    public boolean parse() throws CoreException {
        if (fPreprocessedArgs == null) {
            throw createException("AntxrTool.error.missingPreprocess");
        }

        // process arguments for the Tool
        processArguments(fPreprocessedArgs);
        if (!hasError) {
            Reader reader = getGrammarReader();
            ANTXRLexer lexer = new ANTXRLexer(reader);
            fAnalyzer = new LLkAnalyzer(this);
            fBehavior = new MakeGrammar(this, fPreprocessedArgs, fAnalyzer);
            fParser = new ANTXRParser(new TokenBuffer(lexer), fBehavior, this);
            fParser.setFilename(grammarFile);
            try {
                fParser.grammar();
            } catch (IllegalStateException e) {
                // Thrown by AntxrTool.fatalError()
                System.err.println(e.toString());
                hasError = true;
            } catch (RecognitionException e) {
                System.err.println(
                              FileLineFormatter.getFormatter().getFormatString(
                              null, e.getLine(), e.getColumn())
                              + e.getMessage());
                hasError = true;
            } catch (TokenStreamException e) {
                if (e instanceof TokenStreamRecognitionException) {
                System.err.println(e.toString());
                } else if (e.getMessage() != null) {
                    System.err.println(
                              FileLineFormatter.getFormatter().getFormatString(
                              null, -1, -1)
                              + e.getMessage());
                }
                hasError = true;
            } finally {

                // Close all writers opened during grammar inheritance
                // (expanded grammar files)
                Iterator writers = fWriters.iterator();
                while (writers.hasNext()) {
                    PrintWriter writer = (PrintWriter)writers.next();
                    writer.close();
                }

                // Close reader
                try {
                    reader.close();
                } catch (IOException e) {
                    throw createException("AntxrTool.error.canNotCloseFile",
                                          grammarFile, e);
                }
            }
        }
        return hasError;
    }

    /**
     * Run the code generation
     * @return true if there was an error; false otherwise
     * @throws CoreException If eclipse got upset
     */
    public boolean generate() throws CoreException {
        if (fParser == null) {
            throw createException("AntxrTool.error.missingParse");
        } else if (!hasError) {
            fFiles.clear();
            fWriters.clear();

            // Create the right code generator according to the
            // "language" option
            String codeGenClassName = "com.javadude.antxr." + getLanguage(fBehavior) +
                                      "CodeGenerator";
            try {
                CodeGenerator codeGen = (CodeGenerator)
                                 Class.forName(codeGenClassName).newInstance();
                codeGen.setBehavior(fBehavior);
                codeGen.setAnalyzer(fAnalyzer);
                codeGen.setTool(this);
                codeGen.gen();
                if (codeGen instanceof JavaCodeGenerator) {
                    sourceMaps = ((JavaCodeGenerator) codeGen).getPrintWriterManager().getSourceMaps();
                }
            } catch (ClassNotFoundException e) {
                throw createException("AntxrTool.error.noCodeGenerator",
                                       codeGenClassName, e);
            } catch (InstantiationException e) {
                throw createException("AntxrTool.error.noCodeGenerator",
                                       codeGenClassName, e);
            } catch (IllegalArgumentException e) {
                throw createException("AntxrTool.error.noCodeGenerator",
                                       codeGenClassName, e);
            } catch (IllegalAccessException e) {
                throw createException("AntxrTool.error.noCodeGenerator",
                                       codeGenClassName, e);
            } catch (IllegalStateException e) {

                // Thrown in fatalError() - ignore
                hasError = true;
            } finally {

                // Close all writers opened during code generation
                Iterator writers = fWriters.iterator();
                while (writers.hasNext()) {
                    PrintWriter writer = (PrintWriter)writers.next();
                    writer.close();
                }
            }
        }
        return hasError;
    }

    private CoreException createException(String aKey) {
        return createException(aKey, (String[])null, null);
    }

    private CoreException createException(String aKey, String anArg,
                                           Throwable aThrowable) {
        return createException(aKey, new String[] { anArg }, aThrowable);
    }

    private CoreException createException(String aKey, String[] anArgs,
                                           Throwable aThrowable) {
        String msg = (anArgs == null ? AntxrCorePlugin.getMessage(aKey) :
                            AntxrCorePlugin.getFormattedMessage(aKey, anArgs));
        return new CoreException(new Status(IStatus.ERROR,
                       AntxrCorePlugin.PLUGIN_ID, IStatus.OK, msg, aThrowable));
    }

    private class MarkerFormatter extends FileLineFormatter {

        /**
         * Returns given information separated by a '|'.
         *
         * @param aFileName  the file that should appear in the prefix (or null)
         * @param aLine  the line (or -1)
         * @param aColumn  the column (or -1)
         * @see com.javadude.antxr.FileLineFormatter#getFormatString(java.lang.String, int, int)
         */
        public String getFormatString(String aFileName, int aLine,
                                      int aColumn) {
            StringBuffer buf = new StringBuffer();

            if (aFileName != null) {
                buf.append(aFileName);
            } else {
                buf.append("<noname>");
            }
            buf.append('|').append(aLine).append('|').append(aColumn).
                                                                    append('|');
            return buf.toString();
        }
    }

    /**
     * @return source maps
     */
    public Map getSourceMaps() {
        return sourceMaps;
    }
}
