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

import java.util.List;
import java.util.Stack;

public class MakeGrammar extends DefineGrammarSymbols {
    protected Stack<BlockContext> blocks = new Stack<BlockContext>(); // track subrules--Stack<BlockContext>
    protected RuleRefElement lastRuleRef;

    protected RuleEndElement ruleEnd;   // used if not nested
    protected RuleBlock ruleBlock;		// points to block of current rule.
    protected int nested = 0;			// nesting inside a subrule
    protected boolean grammarError = false;

    ExceptionSpec currentExceptionSpec = null;

    public MakeGrammar(Tool tool_, String[] args_, LLkAnalyzer analyzer_) {
        super(tool_, args_, analyzer_);
    }

    /** Abort the processing of a grammar (due to syntax errors) */
    @Override
    public void abortGrammar() {
        String s = "unknown grammar";
        if (grammar != null) {
            s = grammar.getClassName();
        }
        tool.error("aborting grammar '" + s + "' due to errors");
        super.abortGrammar();
    }

    protected void addElementToCurrentAlt(AlternativeElement e) {
        e.enclosingRuleName = ruleBlock.ruleName;
        context().addAlternativeElement(e);
    }

    @Override
    public void beginAlt(boolean doAutoGen_) {
        super.beginAlt(doAutoGen_);
        Alternative alt = new Alternative();
        alt.setAutoGen(doAutoGen_);
        context().block.addAlternative(alt);
    }

    @Override
    public void beginChildList() {
        super.beginChildList();
        context().block.addAlternative(new Alternative());
    }

    /** Add an exception group to a rule (currently a no-op) */
    @Override
    public void beginExceptionGroup() {
        super.beginExceptionGroup();
        if (!(context().block instanceof RuleBlock)) {
            tool.fatalError("panic: beginExceptionGroup called outside of rule block");
        }
    }

    /** Add an exception spec to an exception group or rule block */
    @Override
    public void beginExceptionSpec(Token label) {
        // Hack the label string a bit to remove leading/trailing space.
        if (label != null) {
            label.setText(StringUtils.stripFront(StringUtils.stripBack(label.getText(), " \n\r\t"), " \n\r\t"));
        }
        super.beginExceptionSpec(label);
        // Don't check for currentExceptionSpec!=null because syntax errors
        // may leave it set to something.
        currentExceptionSpec = new ExceptionSpec(label);
    }

    @Override
    public void beginSubRule(Token label, Token start, boolean not) {
        super.beginSubRule(label, start, not);
        // we don't know what kind of subrule it is yet.
        // push a dummy one that will allow us to collect the
        // alternatives.  Later, we'll switch to real object.
        blocks.push(new BlockContext());
        context().block = new AlternativeBlock(grammar, start, not);
        context().altNum = 0; // reset alternative number
        nested++;
        // create a final node to which the last elememt of each
        // alternative will point.
        context().blockEnd = new BlockEndElement(grammar);
        // make sure end node points to start of block
        context().blockEnd.block = context().block;
        labelElement(context().block, label);
    }

    @Override
    public void beginTree(Token tok) throws SemanticException {
        if (!(grammar instanceof TreeWalkerGrammar)) {
            tool.error("Trees only allowed in TreeParser", grammar.getFilename(), tok.getLine(), tok.getColumn());
            throw new SemanticException("Trees only allowed in TreeParser");
        }
        super.beginTree(tok);
        blocks.push(new TreeBlockContext());
        context().block = new TreeElement(grammar, tok);
        context().altNum = 0; // reset alternative number
    }

    public BlockContext context() {
        if (blocks.isEmpty()) {
            return null;
        }
        return blocks.peek();
    }

    /**Used to build nextToken() for the lexer.
     * This builds a rule which has every "public" rule in the given Vector of
     * rules as it's alternate.  Each rule ref generates a Token object.
     * @param g  The Grammar that is being processed
     * @param lexRules A vector of lexer rules that will be used to create an alternate block.
     * @param rname The name of the resulting rule.
     */
    public static RuleBlock createNextTokenRule(Grammar g, List<RuleSymbol> lexRules, String rname) {
        // create actual rule data structure
        RuleBlock rb = new RuleBlock(g, rname);
        rb.setDefaultErrorHandler(g.getDefaultErrorHandler());
        RuleEndElement ruleEnd = new RuleEndElement(g);
        rb.setEndElement(ruleEnd);
        ruleEnd.block = rb;
        // Add an alternative for each element of the rules vector.
        for (RuleSymbol r : lexRules) {
            if (!r.isDefined()) {
                g.antxrTool.error("Lexer rule " + r.id.substring(1) + " is not defined");
            }
            else {
                if (r.access.equals("public")) {
                    Alternative alt = new Alternative(); // create alt we'll add to ref rule
                    RuleBlock targetRuleBlock = r.getBlock();
                    List<Alternative> targetRuleAlts = targetRuleBlock.getAlternatives();
                    // collect a sem pred if only one alt and it's at the start;
                    // simple, but faster to implement until real hoisting
                    if ( targetRuleAlts!=null && targetRuleAlts.size()==1 ) {
                        Alternative onlyAlt = targetRuleAlts.get(0);
                        if ( onlyAlt.semPred!=null ) {
                            // ok, has sem pred, make this rule ref alt have a pred
                            alt.semPred = onlyAlt.semPred;
                            // REMOVE predicate from target rule???  NOPE, another
                            // rule other than nextToken() might invoke it.
                        }
                    }

                    // create a rule ref to lexer rule
                    // the Token is a RULE_REF not a TOKEN_REF since the
                    // conversion to mRulename has already taken place
                    RuleRefElement rr =
                        new RuleRefElement(g,
                                           new CommonToken(ANTXRTokenTypes.RULE_REF, r.getId()),
                                           GrammarElement.AUTO_GEN_NONE);
                    rr.setLabel("theRetToken");
                    rr.enclosingRuleName = "nextToken";
                    rr.next = ruleEnd;
                    alt.addElement(rr);  		// add rule ref to alt
                    alt.setAutoGen(true);		// keep text of elements
                    rb.addAlternative(alt);		// add alt to rule block
                    r.addReference(rr);			// track ref to this rule in rule blk
                }
            }
        }

        rb.setAutoGen(true);		// keep text of elements
        rb.prepareForAnalysis();
        //System.out.println(rb);
        return rb;
    }

    /** Return block as if they had typed: "( rule )?" */
    private AlternativeBlock createOptionalRuleRef(String rule, Token start) {
        // Make the subrule
        AlternativeBlock blk = new AlternativeBlock(grammar, start, false);

        // Make sure rule is defined
        String mrule = CodeGenerator.encodeLexerRuleName(rule); // can only be a lexer rule!
        if (!grammar.isDefined(mrule)) {
            grammar.define(new RuleSymbol(mrule));
        }

        // Make the rule ref element
        // RK: fixme probably easier to abuse start token..
        Token t = new CommonToken(ANTXRTokenTypes.TOKEN_REF, rule);
        t.setLine(start.getLine());
        t.setLine(start.getColumn());
        RuleRefElement rref =
            new RuleRefElement(grammar, t, GrammarElement.AUTO_GEN_NONE);

        rref.enclosingRuleName = ruleBlock.ruleName;

        // Make the end of block element
        BlockEndElement end = new BlockEndElement(grammar);
        end.block = blk;		// end block points back to start of blk

        // Make an alternative, putting the rule ref into it
        Alternative alt = new Alternative(rref);
        alt.addElement(end); // last element in alt points to end of block

        // Add the alternative to this block
        blk.addAlternative(alt);

        // create an empty (optional) alt and add to blk
        Alternative optAlt = new Alternative();
        optAlt.addElement(end);	// points immediately to end of block

        blk.addAlternative(optAlt);

        blk.prepareForAnalysis();
        return blk;
    }

    @Override
    public void defineRuleName(Token r,
                               String access,
                               boolean ruleAutoGen,
                               String docComment)
        throws SemanticException {
        //		if ( Character.isUpperCase(r.getText().charAt(0)) ) {
        if (r.type == ANTXRTokenTypes.TOKEN_REF) {
            if (!(grammar instanceof LexerGrammar)) {
                tool.error("Lexical rule " + r.getText() +
                           " defined outside of lexer",
                           grammar.getFilename(), r.getLine(), r.getColumn());
                r.setText(r.getText().toLowerCase());
            }
        }
        else {
            if (grammar instanceof LexerGrammar) {
                tool.error("Lexical rule names must be upper case, '" + r.getText() +
                           "' is not",
                           grammar.getFilename(), r.getLine(), r.getColumn());
                r.setText(r.getText().toUpperCase());
            }
        }

        super.defineRuleName(r, access, ruleAutoGen, docComment);
        String id = r.getText();
        //		if ( Character.isUpperCase(id.charAt(0)) ) { // lexer rule?
        if (r.type == ANTXRTokenTypes.TOKEN_REF) { // lexer rule?
            id = CodeGenerator.encodeLexerRuleName(id);
        }
        RuleSymbol rs = (RuleSymbol)grammar.getSymbol(id);
        RuleBlock rb = new RuleBlock(grammar, r.getText(), r.getLine(), ruleAutoGen);

        // Lexer rules do not generate default error handling
        rb.setDefaultErrorHandler(grammar.getDefaultErrorHandler());

        ruleBlock = rb;
        blocks.push(new BlockContext()); // enter new context
        context().block = rb;
        rs.setBlock(rb);
        ruleEnd = new RuleEndElement(grammar);
        rb.setEndElement(ruleEnd);
        nested = 0;
    }

    @Override
    public void beginRule(Token ruleId, Token lookahead, boolean ruleAutoGen) {
        RuleBlock rb = (RuleBlock)context().block;
//		tool.getPlugin().beginRule(this, grammar, rb, ruleId, lookahead, ruleAutoGen);
        String tag = grammar.xmlRuleTagMap.get(rb);
        if (tag != null) {
            // create a token for the tag
            String startTag = "\"<" + tag + ">\"";
            Token label = new CommonToken(ANTXRTokenTypes.RULE_REF, "__xml_startTag");
            label.setLine(rb.getLine());
            // SAS: what to use for autogen type? GrammarElement.AUTO_GEN_NONE is ok for now, but not
            //      a a good long-term...
            beginAlt(ruleAutoGen);
            Token litToken = new CommonToken(ANTXRTokenTypes.STRING_LITERAL, startTag);
            litToken.setLine(rb.getLine());
            refStringLiteral(litToken, label, GrammarElement.AUTO_GEN_NONE, false);

            // treat the rest of the rule like a subrule in case there are
            //   alternatives
            beginSubRule(null, lookahead, false);

            // keep track that we created a dummy context for the tag
            //   and which tag it was
            grammar.xmlTagImplicitSubrules.put(context(),tag);
        }
    }

    @Override
    public void endAlt() {
        super.endAlt();
        if (nested == 0) {	// all rule-level alts link to ruleEnd node
            addElementToCurrentAlt(ruleEnd);
        }
        else {
            addElementToCurrentAlt(context().blockEnd);
        }
        context().altNum++;
    }

    @Override
    public void endChildList() {
        super.endChildList();
        // create a final node to which the last elememt of the single
        // alternative will point.  Done for compatibility with analyzer.
        // Does NOT point to any block like alternative blocks because the
        // TreeElement is not a block.  This is used only as a placeholder.
        BlockEndElement be = new BlockEndElement(grammar);
        be.block = context().block;
        addElementToCurrentAlt(be);
    }

    @Override
    public void endExceptionGroup() {
        super.endExceptionGroup();
    }

    @Override
    public void endExceptionSpec() {
        super.endExceptionSpec();
        if (currentExceptionSpec == null) {
            tool.fatalError("panic: exception processing internal error -- no active exception spec");
        }
        if (context().block instanceof RuleBlock) {
            // Named rule
            ((RuleBlock)context().block).addExceptionSpec(currentExceptionSpec);
        }
        else {
            // It must be a plain-old alternative block
            if (context().currentAlt().exceptionSpec != null) {
                tool.error("Alternative already has an exception specification", grammar.getFilename(), context().block.getLine(), context().block.getColumn());
            }
            else {
                context().currentAlt().exceptionSpec = currentExceptionSpec;
            }
        }
        currentExceptionSpec = null;
    }

    /** Called at the end of processing a grammar */
    @Override
    public void endGrammar() {
        if (grammarError) {
            abortGrammar();
        }
        else {
            super.endGrammar();
        }
    }

    @Override
    public void endRule(String rule) {
        // if xmltag was specified, add a ref to XML_END_TAG and treat the rest of
        //   the rule as a subrule. We can determine this if the current top
        //   context is in the xmlTagImplicitSubrules set
        if (grammar.xmlTagImplicitSubrules.containsKey(context())) {
            grammar.xmlTagImplicitSubrules.remove(context());

            // end the dummy subrule
            endSubRule();

            // SAS: what to use for autogen type? GrammarElement.AUTO_GEN_NONE is ok for now, but not
            //      a a good long-term...
            // SAS: what about last in rule -- is this determined elsewhere via lookahead? if so, this won't work right -- we'd need to add a dummy XML_END_TAG in the scanner!
            // don't need a label on the end tag
            Token endToken = new CommonToken(ANTXRTokenTypes.TOKEN_REF, "XML_END_TAG");
            endToken.setLine(context().block.getLine());
            refToken(null, endToken, null, null, false, GrammarElement.AUTO_GEN_NONE, true);

            endAlt();
        }

        super.endRule(rule);
        BlockContext ctx = blocks.pop();	// remove scope
        // record the start of this block in the ending node
        ruleEnd.block = ctx.block;
        ruleEnd.block.prepareForAnalysis();
        //System.out.println(ctx.block);
    }

    @Override
    public void endSubRule() {
        super.endSubRule();
        nested--;
        // remove subrule context from scope stack
        BlockContext ctx = blocks.pop();
        AlternativeBlock block = ctx.block;

        // If the subrule is marked with ~, check that it is
        // a valid candidate for analysis
        if (
            block.not &&
            !(block instanceof SynPredBlock) &&
            !(block instanceof ZeroOrMoreBlock) &&
            !(block instanceof OneOrMoreBlock)
        ) {
            if (!analyzer.subruleCanBeInverted(block, grammar instanceof LexerGrammar)) {
                String newline = System.getProperty("line.separator");
                tool.error(
                    "This subrule cannot be inverted.  Only subrules of the form:" + newline +
                    "    (T1|T2|T3...) or" + newline +
                    "    ('c1'|'c2'|'c3'...)" + newline +
                    "may be inverted (ranges are also allowed).",
                    grammar.getFilename(),
                    block.getLine(), block.getColumn()
                );
            }
        }

        // add the subrule as element if not a syn pred
        if (block instanceof SynPredBlock) {
            // record a reference to the recently-recognized syn pred in the
            // enclosing block.
            SynPredBlock synpred = (SynPredBlock)block;
            context().block.hasASynPred = true;
            context().currentAlt().synPred = synpred;
            grammar.hasSyntacticPredicate = true;
            synpred.removeTrackingOfRuleRefs(grammar);
        }
        else {
            addElementToCurrentAlt(block);
        }
        ctx.blockEnd.block.prepareForAnalysis();
    }

    @Override
    public void endTree() {
        super.endTree();
        BlockContext ctx = blocks.pop();
        addElementToCurrentAlt(ctx.block);		// add new TreeElement to enclosing alt.
    }

    /** Remember that a major error occured in the grammar */
    @Override
    public void hasError() {
        grammarError = true;
    }

    private void labelElement(AlternativeElement el, Token label) {
        if (label != null) {
            // Does this label already exist?
            for (AlternativeElement altEl : ruleBlock.labeledElements) {
                String l = altEl.getLabel();
                if (l != null && l.equals(label.getText())) {
                    tool.error("Label '" + label.getText() + "' has already been defined", grammar.getFilename(), label.getLine(), label.getColumn());
                    return;
                }
            }
            // add this node to the list of labeled elements
            el.setLabel(label.getText());
            ruleBlock.labeledElements.add(el);
        }
    }

    public void noAutoGenSubRule() {
        context().block.setAutoGen(false);
    }

    @Override
    public void oneOrMoreSubRule() {
        if (context().block.not) {
            tool.error("'~' cannot be applied to (...)* subrule", grammar.getFilename(), context().block.getLine(), context().block.getColumn());
        }
        // create the right kind of object now that we know what that is
        // and switch the list of alternatives.  Adjust the stack of blocks.
        // copy any init action also.
        OneOrMoreBlock b = new OneOrMoreBlock(grammar);
        MakeGrammar.setBlock(b, context().block);
        BlockContext old = blocks.pop(); // remove old scope; we want new type of subrule
        blocks.push(new BlockContext());
        context().block = b;
        context().blockEnd = old.blockEnd;
        context().blockEnd.block = b;
    }

    @Override
    public void optionalSubRule() {
        if (context().block.not) {
            tool.error("'~' cannot be applied to (...)? subrule", grammar.getFilename(), context().block.getLine(), context().block.getColumn());
        }
        // convert (X)? -> (X|) so that we can ignore optional blocks altogether!
        // It already thinks that we have a simple subrule, just add option block.
        beginAlt(false);
        endAlt();
    }

    @Override
    public void refAction(Token action) {
        super.refAction(action);
        context().block.hasAnAction = true;
        addElementToCurrentAlt(new ActionElement(grammar, action));
    }

    @Override
    public void setUserExceptions(String thr) {
        ((RuleBlock)context().block).throwsSpec = thr;
    }

    // Only called for rule blocks
    @Override
    public void refArgAction(Token action) {
        ((RuleBlock)context().block).argAction = action.getText();
    }

    @Override
    public void refCharLiteral(Token lit, Token label, boolean inverted, int autoGenType, boolean lastInRule) {
        if (!(grammar instanceof LexerGrammar)) {
            tool.error("Character literal only valid in lexer", grammar.getFilename(), lit.getLine(), lit.getColumn());
            return;
        }
        super.refCharLiteral(lit, label, inverted, autoGenType, lastInRule);
        CharLiteralElement cl = new CharLiteralElement((LexerGrammar)grammar, lit, inverted, autoGenType);

        // Generate a warning for non-lowercase ASCII when case-insensitive
        if (
            !((LexerGrammar)grammar).caseSensitive && cl.getType() < 128 &&
            Character.toLowerCase((char)cl.getType()) != (char)cl.getType()
        ) {
            tool.warning("Character literal must be lowercase when caseSensitive=false", grammar.getFilename(), lit.getLine(), lit.getColumn());
        }

        addElementToCurrentAlt(cl);
        labelElement(cl, label);

        // if ignore option is set, must add an optional call to the specified rule.
        String ignore = ruleBlock.getIgnoreRule();
        if (!lastInRule && ignore != null) {
            addElementToCurrentAlt(createOptionalRuleRef(ignore, lit));
        }
    }

    @Override
    public void refCharRange(Token t1, Token t2, Token label, int autoGenType, boolean lastInRule) {
        if (!(grammar instanceof LexerGrammar)) {
            tool.error("Character range only valid in lexer", grammar.getFilename(), t1.getLine(), t1.getColumn());
            return;
        }
        int rangeMin = ANTXRLexer.tokenTypeForCharLiteral(t1.getText());
        int rangeMax = ANTXRLexer.tokenTypeForCharLiteral(t2.getText());
        if (rangeMax < rangeMin) {
            tool.error("Malformed range.", grammar.getFilename(), t1.getLine(), t1.getColumn());
            return;
        }

        // Generate a warning for non-lowercase ASCII when case-insensitive
        if (!((LexerGrammar)grammar).caseSensitive) {
            if (rangeMin < 128 && Character.toLowerCase((char)rangeMin) != (char)rangeMin) {
                tool.warning("Character literal must be lowercase when caseSensitive=false", grammar.getFilename(), t1.getLine(), t1.getColumn());
            }
            if (rangeMax < 128 && Character.toLowerCase((char)rangeMax) != (char)rangeMax) {
                tool.warning("Character literal must be lowercase when caseSensitive=false", grammar.getFilename(), t2.getLine(), t2.getColumn());
            }
        }

        super.refCharRange(t1, t2, label, autoGenType, lastInRule);
        CharRangeElement cr = new CharRangeElement((LexerGrammar)grammar, t1, t2, autoGenType);
        addElementToCurrentAlt(cr);
        labelElement(cr, label);

        // if ignore option is set, must add an optional call to the specified rule.
        String ignore = ruleBlock.getIgnoreRule();
        if (!lastInRule && ignore != null) {
            addElementToCurrentAlt(createOptionalRuleRef(ignore, t1));
        }
    }

    @Override
    public void refTokensSpecElementOption(Token tok,
                                           Token option,
                                           Token value) {
        /*
        System.out.println("setting tokens spec option for "+tok.getText());
        System.out.println(option.getText()+","+value.getText());
        */
        TokenSymbol ts = grammar.tokenManager.getTokenSymbol(tok.getText());
        if (ts == null) {
            tool.fatalError("panic: cannot find " + tok.getText() + "in tokens {...}");
            return;
        }
        if (option.getText().equals("AST")) {
            ts.setASTNodeType(value.getText());
        }
        else {
            grammar.antxrTool.error("invalid tokens {...} element option:" +
                               option.getText(),
                               grammar.getFilename(),
                               option.getLine(), option.getColumn());
        }
    }

    @Override
    public void refElementOption(Token option, Token value) {
        /*
        System.out.println("setting option for "+context().currentElement());
        System.out.println(option.getText()+","+value.getText());
        */
        AlternativeElement e = context().currentElement();
        if (e instanceof StringLiteralElement ||
            e instanceof TokenRefElement ||
            e instanceof WildcardElement) {
            ((GrammarAtom)e).setOption(option, value);
        }
        else {
            tool.error("cannot use element option (" + option.getText() +
                       ") for this kind of element",
                       grammar.getFilename(), option.getLine(), option.getColumn());
        }
    }

    /** Add an exception handler to an exception spec */
    @Override
    public void refExceptionHandler(Token exTypeAndName, Token action) {
        super.refExceptionHandler(exTypeAndName, action);
        if (currentExceptionSpec == null) {
            tool.fatalError("panic: exception handler processing internal error");
        }
        currentExceptionSpec.addHandler(new ExceptionHandler(exTypeAndName, action));
    }

    @Override
    public void refInitAction(Token action) {
        super.refAction(action);
        context().block.setInitAction(action.getText());
    }

    @Override
    public void refMemberAction(Token act) {
        grammar.classMemberAction = act;
    }

    @Override
    public void refPreambleAction(Token act) {
        super.refPreambleAction(act);
    }

    // Only called for rule blocks
    @Override
    public void refReturnAction(Token returnAction) {
        if (grammar instanceof LexerGrammar) {
            String name = CodeGenerator.encodeLexerRuleName(((RuleBlock)context().block).getRuleName());
            RuleSymbol rs = (RuleSymbol)grammar.getSymbol(name);
            if (rs.access.equals("public")) {
                tool.warning("public Lexical rules cannot specify return type", grammar.getFilename(), returnAction.getLine(), returnAction.getColumn());
                return;
            }
        }
        ((RuleBlock)context().block).returnAction = returnAction.getText();
    }

    @Override
    public void refRule(Token idAssign,
                        Token r,
                        Token label,
                        Token arguments,
                        int autoGenType) {
        // Disallow parser rule references in the lexer
        if (grammar instanceof LexerGrammar) {
            //			if (!Character.isUpperCase(r.getText().charAt(0))) {
            if (r.type != ANTXRTokenTypes.TOKEN_REF) {
                tool.error("Parser rule " + r.getText() + " referenced in lexer");
                return;
            }
            if (autoGenType == GrammarElement.AUTO_GEN_CARET) {
                tool.error("AST specification ^ not allowed in lexer", grammar.getFilename(), r.getLine(), r.getColumn());
            }
        }

        super.refRule(idAssign, r, label, arguments, autoGenType);
        lastRuleRef = new RuleRefElement(grammar, r, autoGenType);
        if (arguments != null) {
            lastRuleRef.setArgs(arguments.getText());
        }
        if (idAssign != null) {
            lastRuleRef.setIdAssign(idAssign.getText());
        }
        addElementToCurrentAlt(lastRuleRef);

        String id = r.getText();
        //		if ( Character.isUpperCase(id.charAt(0)) ) { // lexer rule?
        if (r.type == ANTXRTokenTypes.TOKEN_REF) { // lexer rule?
            id = CodeGenerator.encodeLexerRuleName(id);
        }
        // update symbol table so it knows what nodes reference the rule.
        RuleSymbol rs = (RuleSymbol)grammar.getSymbol(id);
        rs.addReference(lastRuleRef);
        labelElement(lastRuleRef, label);
    }

    @Override
    public void refSemPred(Token pred) {
        //System.out.println("refSemPred "+pred.getText());
        super.refSemPred(pred);
        //System.out.println("context().block: "+context().block);
        if (context().currentAlt().atStart()) {
            context().currentAlt().semPred = pred.getText();
        }
        else {
            ActionElement a = new ActionElement(grammar, pred);
            a.isSemPred = true;
            addElementToCurrentAlt(a);
        }
        //System.out.println("DONE refSemPred "+pred.getText());
    }

    @Override
    public void refStringLiteral(Token lit, Token label, int autoGenType, boolean lastInRule) {
        super.refStringLiteral(lit, label, autoGenType, lastInRule);
        if (grammar instanceof TreeWalkerGrammar && autoGenType == GrammarElement.AUTO_GEN_CARET) {
            tool.error("^ not allowed in here for tree-walker", grammar.getFilename(), lit.getLine(), lit.getColumn());
        }
        StringLiteralElement sl = new StringLiteralElement(grammar, lit, autoGenType);

        // If case-insensitive, then check each char of the stirng literal
        if (grammar instanceof LexerGrammar && !((LexerGrammar)grammar).caseSensitive) {
            for (int i = 1; i < lit.getText().length() - 1; i++) {
                char c = lit.getText().charAt(i);
                if (c < 128 && Character.toLowerCase(c) != c) {
                    tool.warning("Characters of string literal must be lowercase when caseSensitive=false", grammar.getFilename(), lit.getLine(), lit.getColumn());
                    break;
                }
            }
        }

        addElementToCurrentAlt(sl);
        labelElement(sl, label);

        // if ignore option is set, must add an optional call to the specified rule.
        String ignore = ruleBlock.getIgnoreRule();
        if (!lastInRule && ignore != null) {
            addElementToCurrentAlt(createOptionalRuleRef(ignore, lit));
        }
    }

    @Override
    public void refToken(Token idAssign, Token t, Token label, Token arguments,
                         boolean inverted, int autoGenType, boolean lastInRule) {
        if (grammar instanceof LexerGrammar) {
            // In lexer, token references are really rule references
            if (autoGenType == GrammarElement.AUTO_GEN_CARET) {
                tool.error("AST specification ^ not allowed in lexer", grammar.getFilename(), t.getLine(), t.getColumn());
            }
            if (inverted) {
                tool.error("~TOKEN is not allowed in lexer", grammar.getFilename(), t.getLine(), t.getColumn());
            }
            refRule(idAssign, t, label, arguments, autoGenType);

            // if ignore option is set, must add an optional call to the specified token rule.
            String ignore = ruleBlock.getIgnoreRule();
            if (!lastInRule && ignore != null) {
                addElementToCurrentAlt(createOptionalRuleRef(ignore, t));
            }
        }
        else {
            // Cannot have token ref args or assignment outside of lexer
            if (idAssign != null) {
                tool.error("Assignment from token reference only allowed in lexer", grammar.getFilename(), idAssign.getLine(), idAssign.getColumn());
            }
            if (arguments != null) {
                tool.error("Token reference arguments only allowed in lexer", grammar.getFilename(), arguments.getLine(), arguments.getColumn());
            }
            super.refToken(idAssign, t, label, arguments, inverted, autoGenType, lastInRule);
            TokenRefElement te = new TokenRefElement(grammar, t, inverted, autoGenType);
            addElementToCurrentAlt(te);
            labelElement(te, label);
        }
    }

    @Override
    public void refTokenRange(Token t1, Token t2, Token label, int autoGenType, boolean lastInRule) {
        if (grammar instanceof LexerGrammar) {
            tool.error("Token range not allowed in lexer", grammar.getFilename(), t1.getLine(), t1.getColumn());
            return;
        }
        super.refTokenRange(t1, t2, label, autoGenType, lastInRule);
        TokenRangeElement tr = new TokenRangeElement(grammar, t1, t2, autoGenType);
        if (tr.end < tr.begin) {
            tool.error("Malformed range.", grammar.getFilename(), t1.getLine(), t1.getColumn());
            return;
        }
        addElementToCurrentAlt(tr);
        labelElement(tr, label);
    }

    @Override
    public void refTreeSpecifier(Token treeSpec) {
        context().currentAlt().treeSpecifier = treeSpec;
    }

    @Override
    public void refWildcard(Token t, Token label, int autoGenType) {
        super.refWildcard(t, label, autoGenType);
        WildcardElement wc = new WildcardElement(grammar, t, autoGenType);
        addElementToCurrentAlt(wc);
        labelElement(wc, label);
    }

    /** Get ready to process a new grammar */
    @Override
    public void reset() {
        super.reset();
        blocks = new Stack<BlockContext>();
        lastRuleRef = null;
        ruleEnd = null;
        ruleBlock = null;
        nested = 0;
        currentExceptionSpec = null;
        grammarError = false;
    }

    @Override
    public void setArgOfRuleRef(Token argAction) {
        super.setArgOfRuleRef(argAction);
        lastRuleRef.setArgs(argAction.getText());
    }

    public static void setBlock(AlternativeBlock b, AlternativeBlock src) {
        b.setAlternatives(src.getAlternatives());
        b.initAction = src.initAction;
        //b.lookaheadDepth = src.lookaheadDepth;
        b.label = src.label;
        b.hasASynPred = src.hasASynPred;
        b.hasAnAction = src.hasAnAction;
        b.warnWhenFollowAmbig = src.warnWhenFollowAmbig;
        b.generateAmbigWarnings = src.generateAmbigWarnings;
        b.line = src.line;
        b.greedy = src.greedy;
        b.greedySet = src.greedySet;
    }

    @Override
    public void setRuleOption(Token key, Token value) {
        //((RuleBlock)context().block).setOption(key, value);
        ruleBlock.setOption(key, value);
    }

    @Override
    public void setSubruleOption(Token key, Token value) {
        context().block.setOption(key, value);
    }

    @Override
    public void synPred() {
        if (context().block.not) {
            tool.error("'~' cannot be applied to syntactic predicate", grammar.getFilename(), context().block.getLine(), context().block.getColumn());
        }
        // create the right kind of object now that we know what that is
        // and switch the list of alternatives.  Adjust the stack of blocks.
        // copy any init action also.
        SynPredBlock b = new SynPredBlock(grammar);
        MakeGrammar.setBlock(b, context().block);
        BlockContext old = blocks.pop(); // remove old scope; we want new type of subrule
        blocks.push(new BlockContext());
        context().block = b;
        context().blockEnd = old.blockEnd;
        context().blockEnd.block = b;
    }

    @Override
    public void zeroOrMoreSubRule() {
        if (context().block.not) {
            tool.error("'~' cannot be applied to (...)+ subrule", grammar.getFilename(), context().block.getLine(), context().block.getColumn());
        }
        // create the right kind of object now that we know what that is
        // and switch the list of alternatives.  Adjust the stack of blocks.
        // copy any init action also.
        ZeroOrMoreBlock b = new ZeroOrMoreBlock(grammar);
        MakeGrammar.setBlock(b, context().block);
        BlockContext old = blocks.pop(); // remove old scope; we want new type of subrule
        blocks.push(new BlockContext());
        context().block = b;
        context().blockEnd = old.blockEnd;
        context().blockEnd.block = b;
    }
}
