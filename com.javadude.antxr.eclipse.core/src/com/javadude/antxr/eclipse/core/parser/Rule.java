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
package com.javadude.antxr.eclipse.core.parser;

import java.util.Enumeration;
import java.util.Vector;

import com.javadude.antxr.Token;

/**
 * Represents a rule in the outline view
 */
public class Rule extends AbstractModel {
    /** The rule is private */
    public static final int PRIVATE = 1;
    /** The rule is protected */
    public static final int PROTECTED = 2;
    /** The rule is public */
    public static final int PUBLIC = 3;

    private int fVisibility;
    private Block fDocComment = null;
    private Block fOptions = null;
    private Block fMemberAction = null;
    private Vector<IModel> fExceptions = new Vector<IModel>();

    /** Indicates if rules is excluded from transformation (suffix "!") */
    private boolean fIsExcluded;

    /**
     * create a rule node
     * @param aGrammar the grammar containing the rule
     * @param aName the name of the rule
     * @param aVisibility the visibility of the rule
     * @param aStartLine the start line of the rule
     */
    public Rule(Grammar aGrammar, String aName, int aVisibility,
                 int aStartLine) {
        super(aName, aGrammar);
        fVisibility = aVisibility;
        setStartLine(aStartLine);
        setEndLine(aStartLine);
    }

    /**
     * @see IModel#hasChildren()
     */
    public boolean hasChildren() {
        return fDocComment != null || fOptions != null ||
               fMemberAction != null || !fExceptions.isEmpty();
    }

    /**
     * @see IModel#getChildren()
     */
    public Object[] getChildren() {
        Vector<IModel> children = new Vector<IModel>();
        if (fDocComment != null) {
            children.add(fDocComment);
        }
        if (fOptions != null) {
            children.add(fOptions);
        }
        if (fMemberAction != null) {
            children.add(fMemberAction);
        }
        children.addAll(fExceptions);
        return children.toArray();
    }

    /**
     * @see ISegment#getUniqueID()
     */
    public String getUniqueID() {
        return ((ISegment)getParent()).getUniqueID() + "/Rule:" + getName();
    }

    /**
     * @see ISegment#accept(ISegmentVisitor)
     */
    public boolean accept(ISegmentVisitor aVisitor) {
        boolean more = true;

        // At first visit all exceptions of this rule
        Enumeration exceptions = fExceptions.elements();
        while (exceptions.hasMoreElements() && more) {
            more = ((ISegment)exceptions.nextElement()).accept(aVisitor);
        }

        // Now visit this rule's doc comment, options and member action
        if (more && fDocComment != null) {
            more = aVisitor.visit(fDocComment);
        }
        if (more && fOptions != null) {
            more = aVisitor.visit(fOptions);
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
     * get the visibility of the rule
     * @return the rule visibility
     */
    public int getVisibility() {
        return fVisibility;
    }

    /**
     * set the rule as excluded
     * @param anIsExcluded true to exclude the rule
     */
    public void setIsExcluded(boolean anIsExcluded) {
        fIsExcluded = anIsExcluded;
    }

    /**
     * is the rule excluded from view
     * @return true if excluded
     */
    public boolean isExcluded() {
        return fIsExcluded;
    }

    /**
     * set the doc comment for the rule
     * @param aToken the doc comment
     */
    public	void setDocComment(Token aToken) {
        fDocComment = new Block(this, Block.COMMENT, aToken.getLine(),
                                aToken.getColumn());
    }

    /**
     * get the doc comment for the rule
     * @return the doc comment
     */
    public Block getDocComment() {
        return fDocComment;
    }

    /**
     * set the rule options
     * @param aToken the rule options
     */
    public void setOptions(Token aToken) {
        fOptions = new Block(this, Block.OPTIONS, aToken.getLine(),
                             aToken.getColumn());
    }

    /**
     * get the rule options
     * @return the rule options
     */
    public Block getOptions() {
        return fOptions;
    }

    /**
     * add a member action to the rule
     * @param aToken the action
     */
    public void setMemberAction(Token aToken) {
        fMemberAction = new Block(this, Block.ACTION, aToken.getLine(),
                                  aToken.getColumn());
    }

    /**
     * get the member action for the rule
     * @return the action
     */
    public Block getMemberAction() {
        return fMemberAction;
    }

    /**
     * Add an exception to the rule
     * @param aToken the exception spec
     */
    public void addException(Token aToken) {
        fExceptions.add(new Block(this, Block.EXCEPTION, aToken.getLine(),
                                  getEndLine()));
    }

    /** {@inheritDoc} */
    public String toString() {
        return getUniqueID() + " [" + getStartLine() + ":" +
                getEndLine() + "]";
    }
}
