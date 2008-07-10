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

import java.util.Enumeration;
import java.util.Vector;

import com.javadude.antxr.Token;

/**
 * Represents a grammar in the outline view
 */
public class Grammar extends AbstractModel {

    private Block fPreamble = null;
    private Block fDocComment = null;
    private Block fOptions = null;
    private Block fTokens = null;
    private Block fMemberAction = null;
    private Vector<IModel> fRules = new Vector<IModel>();

    /**
     * Create the grammar node
     * @param aHierarchy the hiearchy definition
     * @param aName the name of the node
     * @param aSuperClass the superclass
     * @param aStartLine the start line
     */
    public Grammar(Hierarchy aHierarchy, String aName, String aSuperClass,
                    int aStartLine) {
        super(aName, aHierarchy);
        // superclass not used
        setStartLine(aStartLine);
        setEndLine(aStartLine);
    }

    /**
     * @see IModel#hasChildren()
     */
    public boolean hasChildren() {
        return fPreamble != null || fDocComment != null || fOptions != null ||
               fTokens != null || fMemberAction != null || !fRules.isEmpty();
    }

    /**
     * @see IModel#getChildren()
     */
    public Object[] getChildren() {
        Vector<IModel> children = new Vector<IModel>();
        if (fPreamble != null) {
            children.add(fPreamble);
        }
        if (fDocComment != null) {
            children.add(fDocComment);
        }
        if (fOptions != null) {
            children.add(fOptions);
        }
        if (fTokens != null) {
            children.add(fTokens);
        }
        if (fMemberAction != null) {
            children.add(fMemberAction);
        }
        children.addAll(fRules);
        return children.toArray();
    }

    /**
     * @see ISegment#getUniqueID()
     */
    public String getUniqueID() {
        return ((ISegment)getParent()).getUniqueID() + "/Grammar:" + getName();
    }

    /**
     * @see ISegment#accept(ISegmentVisitor)
     */
    public boolean accept(ISegmentVisitor aVisitor) {
        boolean more = true;

        // At first visit all rules of this grammar
        Enumeration rules = fRules.elements();
        while (rules.hasMoreElements() && more) {
            more = ((ISegment)rules.nextElement()).accept(aVisitor);
        }

        // Now visit this grammar's preamble, doc comment, options, tokens and
        // member action
        if (more && fPreamble != null) {
            more = aVisitor.visit(fPreamble);
        }
        if (more && fDocComment != null) {
            more = aVisitor.visit(fDocComment);
        }
        if (more && fOptions != null) {
            more = aVisitor.visit(fOptions);
        }
        if (more && fTokens != null) {
            more = aVisitor.visit(fTokens);
        }
        if (more && fMemberAction != null) {
            more = aVisitor.visit(fMemberAction);
        }

        // Finally visit this grammar
        if (more) {
            more = aVisitor.visit(this);
        }
        return more;
    }

    /**
     * Set the preamble for the grammar
     * @param aToken the preamble
     */
    public	void setPreamble(Token aToken) {
        fPreamble = new Block(this, Block.PREAMBLE, aToken.getLine(),
                              aToken.getColumn());
    }

    /**
     * get the reamble for the grammar
     * @return the reamble
     */
    public Block getPreamble() {
        return fPreamble;
    }

    /**
     * set the doc comment for the grammar
     * @param aToken the doc comment
     */
    public	void setDocComment(Token aToken) {
        fDocComment = new Block(this, Block.COMMENT, aToken.getLine(),
                                aToken.getColumn());
    }

    /**
     * Get the doc comment for the grammar
     * @return the doc comment
     */
    public Block getDocComment() {
        return fDocComment;
    }

    /**
     * Set the options for the grammar
     * @param aToken the options
     */
    public void setOptions(Token aToken) {
        fOptions = new Block(this, Block.OPTIONS, aToken.getLine(),
                             aToken.getColumn());
    }

    /**
     * Get the options for the grammar
     * @return the options
     */
    public Block getOptions() {
        return fOptions;
    }

    /**
     * Set the tokens for the grammar
     * @param aToken the tokens
     */
    public void setTokens(Token aToken) {
        fTokens = new Block(this, Block.TOKENS, aToken.getLine(),
                            aToken.getColumn());
    }

    /**
     * Get the tokens
     * @return the tokens
     */
    public Block getTokens() {
        return fTokens;
    }

    /**
     * Set the member action
     * @param aToken the member action
     */
    public void setMemberAction(Token aToken) {
        fMemberAction = new Block(this, Block.ACTION, aToken.getLine(),
                                  aToken.getColumn());
    }

    /**
     * Get the member action
     * @return member action
     */
    public Block getMemberAction() {
        return fMemberAction;
    }

    /**
     * Add a rule to the grammar
     * @param aRule the rule to add
     */
    public void addRule(Rule aRule) {
        fRules.add(aRule);
    }

    /**
     * Get all rules in the grammar
     * @return the rules
     */
    public Enumeration getRules() {
        return fRules.elements();
    }

    /**
     * Get the last rule in the grammar
     * @return the last rule
     */
    public Rule getLastRule() {
        return (fRules.isEmpty() ? null : (Rule)fRules.lastElement());
    }

    /** {@inheritDoc} */
    public String toString() {
        return getUniqueID() + " [" + getStartLine() + ":" + getEndLine() +
                "] with rule(s) " + fRules;
    }
}
