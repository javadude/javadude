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
package com.javadude.antxr.debug;

import java.util.Stack;

import com.javadude.antxr.CommonToken;
import com.javadude.antxr.LLkParser;
import com.javadude.antxr.MismatchedTokenException;
import com.javadude.antxr.ParseTree;
import com.javadude.antxr.ParseTreeRule;
import com.javadude.antxr.ParseTreeToken;
import com.javadude.antxr.ParserSharedInputState;
import com.javadude.antxr.Token;
import com.javadude.antxr.TokenBuffer;
import com.javadude.antxr.TokenStream;
import com.javadude.antxr.TokenStreamException;
import com.javadude.antxr.collections.impl.BitSet;

/** Override the standard matching and rule entry/exit routines
 *  to build parse trees.  This class is useful for 2.7.3 where
 *  you can specify a superclass like
 *
 *   class TinyCParser extends Parser(ParseTreeDebugParser);
 */
public class ParseTreeDebugParser extends LLkParser {
    /** Each new rule invocation must have it's own subtree.  Tokens
     *  are added to the current root so we must have a stack of subtree roots.
     */
    protected Stack<ParseTreeRule> currentParseTreeRoot = new Stack<ParseTreeRule>();

    /** Track most recently created parse subtree so that when parsing
     *  is finished, we can get to the root.
     */
    protected ParseTreeRule mostRecentParseTreeRoot = null;

    /** For every rule replacement with a production, we bump up count. */
    protected int numberOfDerivationSteps = 1; // n replacements plus step 0

    public ParseTreeDebugParser(int k_) {
        super(k_);
    }

    public ParseTreeDebugParser(ParserSharedInputState state, int k_) {
        super(state,k_);
    }

    public ParseTreeDebugParser(TokenBuffer tokenBuf, int k_) {
        super(tokenBuf, k_);
    }

    public ParseTreeDebugParser(TokenStream lexer, int k_) {
        super(lexer,k_);
    }

    public ParseTree getParseTree() {
        return mostRecentParseTreeRoot;
    }

    public int getNumberOfDerivationSteps() {
        return numberOfDerivationSteps;
    }

    @Override
    public void match(int i) throws MismatchedTokenException, TokenStreamException {
        addCurrentTokenToParseTree();
        super.match(i);
    }

    @Override
    public void match(BitSet bitSet) throws MismatchedTokenException, TokenStreamException {
        addCurrentTokenToParseTree();
        super.match(bitSet);
    }

    @Override
    public void matchNot(int i) throws MismatchedTokenException, TokenStreamException {
        addCurrentTokenToParseTree();
        super.matchNot(i);
    }

    /** This adds LT(1) to the current parse subtree.  Note that the match()
     *  routines add the node before checking for correct match.  This means
     *  that, upon mismatched token, there will a token node in the tree
     *  corresponding to where that token was expected.  For no viable
     *  alternative errors, no node will be in the tree as nothing was
     *  matched() (the lookahead failed to predict an alternative).
     */
    protected void addCurrentTokenToParseTree() throws TokenStreamException {
        if (inputState.guessing>0) {
            return;
        }
        ParseTreeRule root = currentParseTreeRoot.peek();
        ParseTreeToken tokenNode = null;
        if ( LA(1)==Token.EOF_TYPE ) {
            tokenNode = new ParseTreeToken(new CommonToken("EOF"));
        }
        else {
            tokenNode = new ParseTreeToken(LT(1));
        }
        root.addChild(tokenNode);
    }

    /** Create a rule node, add to current tree, and make it current root */
    @Override
    public void traceIn(String s) throws TokenStreamException {
        if (inputState.guessing>0) {
            return;
        }
        ParseTreeRule subRoot = new ParseTreeRule(s);
        if ( currentParseTreeRoot.size()>0 ) {
            ParseTreeRule oldRoot = currentParseTreeRoot.peek();
            oldRoot.addChild(subRoot);
        }
        currentParseTreeRoot.push(subRoot);
        numberOfDerivationSteps++;
    }

    /** Pop current root; back to adding to old root */
    @Override
    public void traceOut(String s) throws TokenStreamException {
        if (inputState.guessing>0) {
            return;
        }
        mostRecentParseTreeRoot = currentParseTreeRoot.pop();
    }

}
