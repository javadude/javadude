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

import java.util.Map;

import com.javadude.antxr.collections.AST;
import com.javadude.antxr.collections.impl.BitSet;

/**A generic ANTXR parser (LL(k) for k>=1) containing a bunch of
 * utility routines useful at any lookahead depth.  We distinguish between
 * the LL(1) and LL(k) parsers because of efficiency.  This may not be
 * necessary in the near future.
 *
 * Each parser object contains the state of the parse including a lookahead
 * cache (the form of which is determined by the subclass), whether or
 * not the parser is in guess mode, where tokens come from, etc...
 *
 * <p>
 * During <b>guess</b> mode, the current lookahead token(s) and token type(s)
 * cache must be saved because the token stream may not have been informed
 * to save the token (via <tt>mark</tt>) before the <tt>try</tt> block.
 * Guessing is started by:
 * <ol>
 * <li>saving the lookahead cache.
 * <li>marking the current position in the TokenBuffer.
 * <li>increasing the guessing level.
 * </ol>
 *
 * After guessing, the parser state is restored by:
 * <ol>
 * <li>restoring the lookahead cache.
 * <li>rewinding the TokenBuffer.
 * <li>decreasing the guessing level.
 * </ol>
 *
 * @see com.javadude.antxr.Token
 * @see com.javadude.antxr.TokenBuffer
 * @see com.javadude.antxr.LLkParser
 */

public abstract class Parser {
    protected ParserSharedInputState inputState;

    /** Nesting level of registered handlers */
    // protected int exceptionLevel = 0;

    /** Table of token type to token names */
    protected String[] tokenNames;

    /** AST return value for a rule is squirreled away here */
    protected AST returnAST;

    /** AST support code; parser delegates to this object.
     *  This is set during parser construction by default
     *  to either "new ASTFactory()" or a ctor that
     *  has a token type to class map for hetero nodes.
     */
    protected ASTFactory astFactory = null;

    /** Constructed if any AST types specified in tokens{..}.
     *  Maps an Integer->Class object.
     */
    protected Map<Integer, Class<?>> tokenTypeToASTClassMap = null;

    /** Used to keep track of indentdepth for traceIn/Out */
    protected int traceDepth = 0;

    public Parser() {
        this(new ParserSharedInputState());
    }

    public Parser(ParserSharedInputState state) {
        inputState = state;
    }

    /** If the user specifies a tokens{} section with heterogeneous
     *  AST node types, then ANTXR generates code to fill
     *  this mapping.
     */
    public Map<Integer, Class<?>> getTokenTypeToASTClassMap() {
        return tokenTypeToASTClassMap;
    }

    /**Get another token object from the token stream */
    public abstract void consume() throws TokenStreamException;

    /** Consume tokens until one matches the given token */
    public void consumeUntil(int tokenType) throws TokenStreamException {
        while (LA(1) != Token.EOF_TYPE && LA(1) != tokenType) {
            consume();
        }
    }

    /** Consume tokens until one matches the given token set */
    public void consumeUntil(BitSet set) throws TokenStreamException {
        while (LA(1) != Token.EOF_TYPE && !set.member(LA(1))) {
            consume();
        }
    }

    /** Get the AST return value squirreled away in the parser */
    public AST getAST() {
        return returnAST;
    }

    public ASTFactory getASTFactory() {
        return astFactory;
    }

    public String getFilename() {
        return inputState.filename;
    }

    public ParserSharedInputState getInputState() {
        return inputState;
    }

    public void setInputState(ParserSharedInputState state) {
        inputState = state;
    }

    public String getTokenName(int num) {
        return tokenNames[num];
    }

    public String[] getTokenNames() {
        return tokenNames;
    }

    public boolean isDebugMode() {
        return false;
    }

    /** Return the token type of the ith token of lookahead where i=1
     * is the current token being examined by the parser (i.e., it
     * has not been matched yet).
     */
    public abstract int LA(int i) throws TokenStreamException;

    /**Return the ith token of lookahead */
    public abstract Token LT(int i) throws TokenStreamException;

    // Forwarded to TokenBuffer
    public int mark() {
        return inputState.input.mark();
    }

    /**Make sure current lookahead symbol matches token type <tt>t</tt>.
     * Throw an exception upon mismatch, which is catch by either the
     * error handler or by the syntactic predicate.
     */
    public void match(int t) throws MismatchedTokenException, TokenStreamException {
        if (LA(1) != t) {
            throw new MismatchedTokenException(tokenNames, LT(1), t, false, getFilename());
        }
        // mark token as consumed -- fetch next token deferred until LA/LT
        consume();
    }

    /**Make sure current lookahead symbol matches the given set
     * Throw an exception upon mismatch, which is catch by either the
     * error handler or by the syntactic predicate.
     */
    public void match(BitSet b) throws MismatchedTokenException, TokenStreamException {
        if (!b.member(LA(1))) {
            throw new MismatchedTokenException(tokenNames, LT(1), b, false, getFilename());
        }
        // mark token as consumed -- fetch next token deferred until LA/LT
        consume();
    }

    public void matchNot(int t) throws MismatchedTokenException, TokenStreamException {
        if (LA(1) == t) {
            // Throws inverted-sense exception
            throw new MismatchedTokenException(tokenNames, LT(1), t, true, getFilename());
        }
        // mark token as consumed -- fetch next token deferred until LA/LT
        consume();
    }

    /** @deprecated as of 2.7.2. This method calls System.exit() and writes
     *  directly to stderr, which is usually not appropriate when
     *  a parser is embedded into a larger application. Since the method is
     *  <code>static</code>, it cannot be overridden to avoid these problems.
     *  ANTXR no longer uses this method internally or in generated code.
     */
    @Deprecated
    public static void panic() {
        System.err.println("Parser: panic");
        Utils.error("");
    }

    /** Parser error-reporting function can be overridden in subclass */
    public void reportError(RecognitionException ex) {
        System.err.println(ex);
    }

    /** Parser error-reporting function can be overridden in subclass */
    public void reportError(String s) {
        if (getFilename() == null) {
            System.err.println("error: " + s);
        }
        else {
            System.err.println(getFilename() + ": error: " + s);
        }
    }

    /** Parser warning-reporting function can be overridden in subclass */
    public void reportWarning(String s) {
        if (getFilename() == null) {
            System.err.println("warning: " + s);
        }
        else {
            System.err.println(getFilename() + ": warning: " + s);
        }
    }

    public void recover(RecognitionException ex,
                        BitSet tokenSet) throws TokenStreamException {
        consume();
        consumeUntil(tokenSet);
    }

    public void rewind(int pos) {
        inputState.input.rewind(pos);
    }

    /** Specify an object with support code (shared by
     *  Parser and TreeParser.  Normally, the programmer
     *  does not play with this, using setASTNodeType instead.
     */
    public void setASTFactory(ASTFactory f) {
        astFactory = f;
    }

    public void setASTNodeClass(String cl) {
        astFactory.setASTNodeClass(cl);
    }

    /** Specify the type of node to create during tree building; use setASTNodeClass now
     *  to be consistent with Token Object Type accessor.
     *  @deprecated since 2.7.1
     */
    @Deprecated
    public void setASTNodeType(String nodeType) {
        setASTNodeClass(nodeType);
    }

    public void setFilename(String f) {
        inputState.filename = f;
    }

    /** Set or change the input token buffer */
    public void setTokenBuffer(TokenBuffer t) {
        inputState.input = t;
    }

    public void traceIndent() {
        for (int i = 0; i < traceDepth; i++) {
            System.out.print(" ");
        }
    }

    public void traceIn(String rname) throws TokenStreamException {
        traceDepth += 1;
        traceIndent();
        System.out.println("> " + rname + "; LA(1)==" + LT(1).getText() +
                           ((inputState.guessing > 0)?" [guessing]":""));
    }

    public void traceOut(String rname) throws TokenStreamException {
        traceIndent();
        System.out.println("< " + rname + "; LA(1)==" + LT(1).getText() +
                           ((inputState.guessing > 0)?" [guessing]":""));
        traceDepth -= 1;
    }
}
