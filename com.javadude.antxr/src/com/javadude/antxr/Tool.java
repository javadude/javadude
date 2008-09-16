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
package com.javadude.antxr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.javadude.antxr.collections.impl.BitSet;

public class Tool {
    public static String version = "";

    /** Object that handles analysis errors */
    ToolErrorHandler errorHandler;

    /** Was there an error during parsing or analysis? */
    protected boolean hasError = false;

    /** Generate diagnostics? (vs code) */
    boolean genDiagnostics = false;

    /** Generate DocBook vs code? */
    boolean genDocBook = false;

    /** Generate HTML vs code? */
    boolean genHTML = false;

    /** Current output directory for generated files */
    protected String outputDir = ".";

    // Grammar input
    protected String grammarFile;
    transient Reader f = new InputStreamReader(System.in);
    // SAS: changed for proper text io
    //  transient DataInputStream in = null;

    protected String literalsPrefix = "LITERAL_";
    protected boolean upperCaseMangledLiterals = false;

    protected String namespaceAntxr = null;
    protected String namespaceStd = null;
    protected boolean genHashLines = true;
    protected boolean noConstructors = false;

    private BitSet cmdLineArgValid = new BitSet();

    /** Construct a new Tool. */
    public Tool() {
        errorHandler = new DefaultToolErrorHandler(this);
    }

    public String getGrammarFile() {
        return grammarFile;
    }

    public boolean hasError() {
        return hasError;
    }

    public String getNamespaceStd() {
        return namespaceStd;
    }

    public String getNamespaceAntxr() {
        return namespaceAntxr;
    }

    public boolean getGenHashLines() {
        return genHashLines;
    }

    public String getLiteralsPrefix() {
        return literalsPrefix;
    }

    public boolean getUpperCaseMangledLiterals() {
        return upperCaseMangledLiterals;
    }

    public void setFileLineFormatter(FileLineFormatter formatter) {
        FileLineFormatter.setFormatter(formatter);
    }

    protected void checkForInvalidArguments(String[] args, BitSet cmdLineArgsValid) {
        // check for invalid command line args
        for (int a = 0; a < args.length; a++) {
            if (!cmdLineArgsValid.member(a)) {
                warning("invalid command-line argument: " + args[a] + "; ignored");
            }
        }
    }

    /** This example is from the book _Java in a Nutshell_ by David
     * Flanagan.  Written by David Flanagan.  Copyright (c) 1996
     * O'Reilly & Associates.  You may study, use, modify, and
     * distribute this example for any purpose.  This example is
     * provided WITHOUT WARRANTY either expressed or implied.  */
    public void copyFile(String source_name, String dest_name)
        throws IOException {
        File source_file = new File(source_name);
        File destination_file = new File(dest_name);
        Reader source = null;
        Writer destination = null;
        char[] buffer;
        int bytes_read;

        try {
            // First make sure the specified source file
            // exists, is a file, and is readable.
            if (!source_file.exists() || !source_file.isFile()) {
                throw new FileCopyException("FileCopy: no such source file: " +
                                            source_name);
            }
            if (!source_file.canRead()) {
                throw new FileCopyException("FileCopy: source file " +
                                            "is unreadable: " + source_name);
            }

            // If the destination exists, make sure it is a writeable file
            // and ask before overwriting it.  If the destination doesn't
            // exist, make sure the directory exists and is writeable.
            if (destination_file.exists()) {
                if (destination_file.isFile()) {

                    if (!destination_file.canWrite()) {
                        throw new FileCopyException("FileCopy: destination " +
                                                    "file is unwriteable: " + dest_name);
                    /*
                      System.out.print("File " + dest_name +
                      " already exists.  Overwrite? (Y/N): ");
                      System.out.flush();
                      response = in.readLine();
                      if (!response.equals("Y") && !response.equals("y"))
                      throw new FileCopyException("FileCopy: copy cancelled.");
                    */
                    }
                }
                else {
                    throw new FileCopyException("FileCopy: destination "
                                                + "is not a file: " + dest_name);
                }
            }
            else {
                File parentdir = parent(destination_file);
                if (!parentdir.exists()) {
                    throw new FileCopyException("FileCopy: destination "
                                                + "directory doesn't exist: " + dest_name);
                }
                if (!parentdir.canWrite()) {
                    throw new FileCopyException("FileCopy: destination "
                                                + "directory is unwriteable: " + dest_name);
                }
            }

            // If we've gotten this far, then everything is okay; we can
            // copy the file.
            source = new BufferedReader(new FileReader(source_file));
            destination = new BufferedWriter(new FileWriter(destination_file));

            buffer = new char[1024];
            while (true) {
                bytes_read = source.read(buffer, 0, 1024);
                if (bytes_read == -1) {
                    break;
                }
                destination.write(buffer, 0, bytes_read);
            }
        }
            // No matter what happens, always close any streams we've opened.
        finally {
            if (source != null) {
                try {
                    source.close();
                }
                catch (IOException e) {
                    /* do nothing */
                }
            }
            if (destination != null) {
                try {
                    destination.close();
                }
                catch (IOException e) {
                    /* do nothing */
                }
            }
        }
    }

    /** Process args and have ANTXR do it's stuff without calling System.exit.
     *  Just return the result code.  Makes it easy for ANT build tool.
     */
    public int doEverything(String[] args) {
        // run the preprocessor to handle inheritance first.

        // Start preprocessor. This strips generates an argument list
        // without -glib options (inside preTool)
        com.javadude.antxr.preprocessor.Tool preTool = new com.javadude.antxr.preprocessor.Tool(this, args);

        boolean preprocess_ok = preTool.preprocess();
        String[] modifiedArgs = preTool.preprocessedArgList();

        // process arguments for the Tool
        processArguments(modifiedArgs);
        if (!preprocess_ok) {
            return 1;
        }

        f = getGrammarReader();

        ANTXRLexer lexer = new ANTXRLexer(f);
        TokenBuffer tokenBuf = new TokenBuffer(lexer);
        LLkAnalyzer analyzer = new LLkAnalyzer(this);
        MakeGrammar behavior = new MakeGrammar(this, args, analyzer);

        try {
            ANTXRParser p = new ANTXRParser(tokenBuf, behavior, this);
            p.setFilename(grammarFile);
            p.grammar();
            if (hasError()) {
                fatalError("Exiting due to errors.");
            }
            checkForInvalidArguments(modifiedArgs, cmdLineArgValid);

            // Create the right code generator according to the "language" option
            CodeGenerator codeGen;

            // SAS: created getLanguage() method so subclass can override
            //      (necessary for VAJ interface)
            String codeGenClassName = "com.javadude.antxr." + getLanguage(behavior) + "CodeGenerator";
            try {
                codeGen = (CodeGenerator)Utils.createInstanceOf(codeGenClassName);
                codeGen.setBehavior(behavior);
                codeGen.setAnalyzer(analyzer);
                codeGen.setTool(this);
                codeGen.gen();
            }
            catch (ClassNotFoundException cnfe) {
                fatalError("panic: Cannot instantiate code-generator: " + codeGenClassName);
            }
            catch (InstantiationException ie) {
                fatalError("panic: Cannot instantiate code-generator: " + codeGenClassName);
            }
            catch (IllegalArgumentException ie) {
                fatalError("panic: Cannot instantiate code-generator: " + codeGenClassName);
            }
            catch (IllegalAccessException iae) {
                fatalError("panic: code-generator class '" + codeGenClassName + "' is not accessible");
            }
        }
        catch (RecognitionException pe) {
            fatalError("Unhandled parser error: " + pe.getMessage());
        }
        catch (TokenStreamException io) {
            fatalError("TokenStreamException: " + io.getMessage());
        }
        return 0;
    }

    /** Issue an error
     * @param s The message
     */
    public void error(String s) {
        hasError = true;
        System.err.println("error: " + s);
    }

    /** Issue an error with line number information
     * @param s The message
     * @param file The file that has the error (or null)
     * @param line The grammar file line number on which the error occured (or -1)
     * @param column The grammar file column number on which the error occured (or -1)
     */
    public void error(String s, String file, int line, int column) {
        hasError = true;
        System.err.println(FileLineFormatter.getFormatter().
                           getFormatString(file, line, column) + s);
    }

    public Object factory(String p) {
        Object o = null;
        try {
            o = Utils.createInstanceOf(p);// get class def
        }
        catch (Exception e) {
            // either class not found,
            // class is interface/abstract, or
            // class or initializer is not accessible.
            warning("Can't create an object of type " + p);
            return null;
        }
        return o;
    }

    public String fileMinusPath(String fileName) {
        String separator = System.getProperty("file.separator");
        int endOfPath = fileName.lastIndexOf(separator);
        if (endOfPath == -1) {
            return fileName;   // no path found
        }
        return fileName.substring(endOfPath + 1);
    }

    /** Determine the language used for this run of ANTXR
     *  This was made a method so the subclass can override it
     */
    public String getLanguage(MakeGrammar behavior) {
        if (genDiagnostics) {
            return "Diagnostic";
        }
        if (genHTML) {
            return "HTML";
        }
        if (genDocBook) {
            return "DocBook";
        }
        return behavior.language;
    }

    public String getOutputDirectory() {
        return outputDir;
    }

    private static void help() {
        System.err.println("usage: java com.javadude.antxr.Tool [args] file.g");
        System.err.println("  -o outputDir       specify output directory where all output generated.");
        System.err.println("  -glib superGrammar specify location of supergrammar file.");
        System.err.println("  -debug             add ParseView support to your generated parser (Parseview will when you run your application).");
        System.err.println("  -html              generate a html file from your grammar.");
        System.err.println("  -docbook           generate a docbook sgml file from your grammar.");
        System.err.println("  -diagnostic        generate a textfile with diagnostics.");
        System.err.println("  -trace             have all rules call traceIn/traceOut.");
        System.err.println("  -traceLexer        have lexer rules call traceIn/traceOut.");
        System.err.println("  -traceParser       have parser rules call traceIn/traceOut.");
        System.err.println("  -traceTreeParser   have tree parser rules call traceIn/traceOut.");
        System.err.println("  -h|-help|--help    this message");
    }

    public static void main(String[] args) {
        System.err.println("ANTXR Parser Generator   Version 0.9.0   Copyright (c) 2005, Scott Stanchfield, JavaDude.com");

        try {
            boolean showHelp = false;

            if (args.length == 0) {
                showHelp = true;
            }
            else {
                for (int i = 0; i < args.length; ++i) {
                    if (args[i].equals("-h")
                        || args[i].equals("-help")
                        || args[i].equals("--help")
                    ) {
                        showHelp = true;
                        break;
                    }
                }
            }

            if (showHelp) {
                Tool.help();
            }
            else {
                Tool theTool = new Tool();
                theTool.doEverything(args);
                theTool = null;
            }
        }
        catch (Exception e) {
            System.err.println(System.getProperty("line.separator") +
                               System.getProperty("line.separator"));
            System.err.println("#$%%*&@# internal error: " + e.toString());
            System.err.println("[please send stack trace with report to scott@javadude.com.]" +
                               System.getProperty("line.separator"));
            e.printStackTrace();
        }
    }

    /** This method is used by all code generators to create new output
     * files. If the outputDir set by -o is not present it will be created here.
     */
    public PrintWriter openOutputFile(String fileName) throws IOException {
        if( outputDir != "." ) {
            File out_dir = new File(outputDir);
            if( ! out_dir.exists() ) {
                out_dir.mkdirs();
            }
        }
        return new PrintWriter(new PreservingFileWriter(outputDir + System.getProperty("file.separator") + fileName));
    }

    public Reader getGrammarReader() {
        Reader reader = null;
        try {
            if (grammarFile != null) {
                reader = new BufferedReader(new FileReader(grammarFile));
            }
        }
        catch (IOException e) {
            fatalError("cannot open grammar file " + grammarFile);
        }
        return reader;
    }

    /** @since 2.7.2
     */
    public void reportException(Exception e, String message) {
        System.err.println(message == null ? e.getMessage()
                                           : message + ": " + e.getMessage());
    }

    /** @since 2.7.2
     */
    public void reportProgress(String message) {
        System.out.println(message);
    }

    /** An error occured that should stop the Tool from doing any work.
     *  The default implementation currently exits (via
     *  {@link java.lang.System.exit(int)} after printing an error message to
     *  <var>stderr</var>. However, the tools should expect that a subclass
     *  will override this to throw an unchecked exception such as
     *  {@link java.lang.IllegalStateException} or another subclass of
     *  {@link java.lang.RuntimeException}. <em>If this method is overriden,
     *  <strong>it must never return normally</strong>; i.e. it must always
     *  throw an exception or call System.exit</em>.
     *  @since 2.7.2
     *  @param s The message
     */
    public void fatalError(String message) {
        System.err.println(message);
        Utils.error(message);
    }

    /** Issue an unknown fatal error. <em>If this method is overriden,
     *  <strong>it must never return normally</strong>; i.e. it must always
     *  throw an exception or call System.exit</em>.
     *  @deprecated as of 2.7.2 use {@link #fatalError(String)}. By default
     *              this method executes <code>fatalError("panic");</code>.
     */
    @Deprecated
    public void panic() {
        fatalError("panic");
    }

    /** Issue a fatal error message. <em>If this method is overriden,
     *  <strong>it must never return normally</strong>; i.e. it must always
     *  throw an exception or call System.exit</em>.
     *  @deprecated as of 2.7.2 use {@link #fatalError(String)}. By defaykt
     *              this method executes <code>fatalError("panic: " + s);</code>.
     * @param s The message
     */
    @Deprecated
    public void panic(String s) {
        fatalError("panic: " + s);
    }

    // File.getParent() can return null when the file is specified without
    // a directory or is in the root directory.
    // This method handles those cases.
    public File parent(File file) {
        String dirname = file.getParent();
        if (dirname == null) {
            if (file.isAbsolute()) {
                return new File(File.separator);
            }
            return new File(System.getProperty("user.dir"));
        }
        return new File(dirname);
    }

    /** Parse a list such as "f1.g;f2.g;..." and return a Vector
     *  of the elements.
     */
    public static List<String> parseSeparatedList(String list, char separator) {
        java.util.StringTokenizer st =
        new java.util.StringTokenizer(list, String.valueOf(separator));
        List<String> v = new ArrayList<String>();
        while ( st.hasMoreTokens() ) {
             v.add(st.nextToken());
        }
        if (v.size() == 0) {
            return null;
        }
        return v;
    }

    /** given a filename, strip off the directory prefix (if any)
     *  and return it.  Return "./" if f has no dir prefix.
     */
    public String pathToFile(String fileName) {
        String separator = System.getProperty("file.separator");
        int endOfPath = fileName.lastIndexOf(separator);
        if (endOfPath == -1) {
            // no path, use current directory
            return "." + System.getProperty("file.separator");
        }
        return fileName.substring(0, endOfPath + 1);
    }

    /** <p>Process the command-line arguments.  Can only be called by Tool.
     * A bitset is collected of all correct arguments via setArgOk.</p>
     * @param args The command-line arguments passed to main()
     *
     */
    protected void processArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-diagnostic")) {
                genDiagnostics = true;
                genHTML = false;
                setArgOK(i);
            }
            else if (args[i].equals("-o")) {
                setArgOK(i);
                if (i + 1 >= args.length) {
                    error("missing output directory with -o option; ignoring");
                }
                else {
                    i++;
                    setOutputDirectory(args[i]);
                    setArgOK(i);
                }
            }
            else if (args[i].equals("-html")) {
                genHTML = true;
                genDiagnostics = false;
                setArgOK(i);
            }
            else if (args[i].equals("-docbook")) {
                genDocBook = true;
                genDiagnostics = false;
                setArgOK(i);
            }
            else {
                if (args[i].charAt(0) != '-') {
                    // Must be the grammar file
                    grammarFile = args[i];
                    setArgOK(i);
                }
            }
        }
    }

    public void setArgOK(int i) {
        cmdLineArgValid.add(i);
    }

    public void setOutputDirectory(String o) {
        outputDir = o;
    }

    /** Issue an error; used for general tool errors not for grammar stuff
     * @param s The message
     */
    public void toolError(String s) {
        System.err.println("error: " + s);
    }

    /** Issue a warning
     * @param s the message
     */
    public void warning(String s) {
        System.err.println("warning: " + s);
    }

    /** Issue a warning with line number information
     * @param s The message
     * @param file The file that has the warning (or null)
     * @param line The grammar file line number on which the warning occured (or -1)
     * @param column The grammar file line number on which the warning occured (or -1)
     */
    public void warning(String s, String file, int line, int column) {
        System.err.println(FileLineFormatter.getFormatter().
                           getFormatString(file, line, column) + "warning:" + s);
    }

    /** Issue a warning with line number information
     * @param s The lines of the message
     * @param file The file that has the warning
     * @param line The grammar file line number on which the warning occured
     */
    public void warning(String[] s, String file, int line, int column) {
        if (s == null || s.length == 0) {
            fatalError("panic: bad multi-line message to Tool.warning");
            return;
        }
        System.err.println(FileLineFormatter.getFormatter().
                           getFormatString(file, line, column) + "warning:" + s[0]);
        for (int i = 1; i < s.length; i++) {
            System.err.println(FileLineFormatter.getFormatter().
                               getFormatString(file, line, column) + "    " + s[i]);
        }
    }
}
