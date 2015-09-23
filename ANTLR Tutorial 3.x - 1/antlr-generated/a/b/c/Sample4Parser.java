// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g 2010-03-18 18:08:14

  package a.b.c;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

/*******************************************************************************
 * Copyright (c) 2009 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
public class Sample4Parser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NEGATION", "IDENT", "INTEGER", "LETTER", "DIGIT", "WS", "':='", "';'", "'('", "')'", "'+'", "'-'", "'*'", "'/'", "'mod'"
    };
    public static final int INTEGER=6;
    public static final int WS=9;
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int NEGATION=4;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int LETTER=7;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int T__10=10;
    public static final int IDENT=5;
    public static final int DIGIT=8;
    public static final int EOF=-1;

    // delegates
    // delegators


        public Sample4Parser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public Sample4Parser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return Sample4Parser.tokenNames; }
    public String getGrammarFileName() { return "C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g"; }


    public static class evaluator_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "evaluator"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:27:1: evaluator : ( assignment )* expression EOF ;
    public final Sample4Parser.evaluator_return evaluator() throws RecognitionException {
        Sample4Parser.evaluator_return retval = new Sample4Parser.evaluator_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token EOF3=null;
        Sample4Parser.assignment_return assignment1 = null;

        Sample4Parser.expression_return expression2 = null;


        CommonTree EOF3_tree=null;

        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:28:2: ( ( assignment )* expression EOF )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:28:4: ( assignment )* expression EOF
            {
            root_0 = (CommonTree)adaptor.nil();

            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:28:4: ( assignment )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==IDENT) ) {
                    int LA1_2 = input.LA(2);

                    if ( (LA1_2==10) ) {
                        alt1=1;
                    }


                }


                switch (alt1) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:28:4: assignment
            	    {
            	    pushFollow(FOLLOW_assignment_in_evaluator64);
            	    assignment1=assignment();

            	    state._fsp--;

            	    adaptor.addChild(root_0, assignment1.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            pushFollow(FOLLOW_expression_in_evaluator67);
            expression2=expression();

            state._fsp--;

            adaptor.addChild(root_0, expression2.getTree());
            EOF3=(Token)match(input,EOF,FOLLOW_EOF_in_evaluator69); 

            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "evaluator"

    public static class assignment_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "assignment"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:31:1: assignment : IDENT ':=' expression ';' ;
    public final Sample4Parser.assignment_return assignment() throws RecognitionException {
        Sample4Parser.assignment_return retval = new Sample4Parser.assignment_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token IDENT4=null;
        Token string_literal5=null;
        Token char_literal7=null;
        Sample4Parser.expression_return expression6 = null;


        CommonTree IDENT4_tree=null;
        CommonTree string_literal5_tree=null;
        CommonTree char_literal7_tree=null;

        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:32:2: ( IDENT ':=' expression ';' )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:32:4: IDENT ':=' expression ';'
            {
            root_0 = (CommonTree)adaptor.nil();

            IDENT4=(Token)match(input,IDENT,FOLLOW_IDENT_in_assignment82); 
            IDENT4_tree = (CommonTree)adaptor.create(IDENT4);
            adaptor.addChild(root_0, IDENT4_tree);

            string_literal5=(Token)match(input,10,FOLLOW_10_in_assignment84); 
            string_literal5_tree = (CommonTree)adaptor.create(string_literal5);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal5_tree, root_0);

            pushFollow(FOLLOW_expression_in_assignment87);
            expression6=expression();

            state._fsp--;

            adaptor.addChild(root_0, expression6.getTree());
            char_literal7=(Token)match(input,11,FOLLOW_11_in_assignment89); 

            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "assignment"

    public static class term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "term"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:37:1: term : ( IDENT | '(' expression ')' | INTEGER );
    public final Sample4Parser.term_return term() throws RecognitionException {
        Sample4Parser.term_return retval = new Sample4Parser.term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token IDENT8=null;
        Token char_literal9=null;
        Token char_literal11=null;
        Token INTEGER12=null;
        Sample4Parser.expression_return expression10 = null;


        CommonTree IDENT8_tree=null;
        CommonTree char_literal9_tree=null;
        CommonTree char_literal11_tree=null;
        CommonTree INTEGER12_tree=null;

        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:38:2: ( IDENT | '(' expression ')' | INTEGER )
            int alt2=3;
            switch ( input.LA(1) ) {
            case IDENT:
                {
                alt2=1;
                }
                break;
            case 12:
                {
                alt2=2;
                }
                break;
            case INTEGER:
                {
                alt2=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:38:4: IDENT
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    IDENT8=(Token)match(input,IDENT,FOLLOW_IDENT_in_term104); 
                    IDENT8_tree = (CommonTree)adaptor.create(IDENT8);
                    adaptor.addChild(root_0, IDENT8_tree);


                    }
                    break;
                case 2 :
                    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:39:4: '(' expression ')'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    char_literal9=(Token)match(input,12,FOLLOW_12_in_term109); 
                    pushFollow(FOLLOW_expression_in_term112);
                    expression10=expression();

                    state._fsp--;

                    adaptor.addChild(root_0, expression10.getTree());
                    char_literal11=(Token)match(input,13,FOLLOW_13_in_term114); 

                    }
                    break;
                case 3 :
                    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:40:4: INTEGER
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    INTEGER12=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_term120); 
                    INTEGER12_tree = (CommonTree)adaptor.create(INTEGER12);
                    adaptor.addChild(root_0, INTEGER12_tree);


                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "term"

    public static class unary_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unary"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:43:1: unary : ( '+' | negation )* term ;
    public final Sample4Parser.unary_return unary() throws RecognitionException {
        Sample4Parser.unary_return retval = new Sample4Parser.unary_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal13=null;
        Sample4Parser.negation_return negation14 = null;

        Sample4Parser.term_return term15 = null;


        CommonTree char_literal13_tree=null;

        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:44:2: ( ( '+' | negation )* term )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:44:4: ( '+' | negation )* term
            {
            root_0 = (CommonTree)adaptor.nil();

            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:44:4: ( '+' | negation )*
            loop3:
            do {
                int alt3=3;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==14) ) {
                    alt3=1;
                }
                else if ( (LA3_0==15) ) {
                    alt3=2;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:44:5: '+'
            	    {
            	    char_literal13=(Token)match(input,14,FOLLOW_14_in_unary133); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:44:12: negation
            	    {
            	    pushFollow(FOLLOW_negation_in_unary138);
            	    negation14=negation();

            	    state._fsp--;

            	    root_0 = (CommonTree)adaptor.becomeRoot(negation14.getTree(), root_0);

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            pushFollow(FOLLOW_term_in_unary143);
            term15=term();

            state._fsp--;

            adaptor.addChild(root_0, term15.getTree());

            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "unary"

    public static class negation_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "negation"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:47:1: negation : '-' -> NEGATION ;
    public final Sample4Parser.negation_return negation() throws RecognitionException {
        Sample4Parser.negation_return retval = new Sample4Parser.negation_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal16=null;

        CommonTree char_literal16_tree=null;
        RewriteRuleTokenStream stream_15=new RewriteRuleTokenStream(adaptor,"token 15");

        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:48:2: ( '-' -> NEGATION )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:48:4: '-'
            {
            char_literal16=(Token)match(input,15,FOLLOW_15_in_negation154);  
            stream_15.add(char_literal16);



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 48:8: -> NEGATION
            {
                adaptor.addChild(root_0, (CommonTree)adaptor.create(NEGATION, "NEGATION"));

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "negation"

    public static class mult_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mult"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:51:1: mult : unary ( ( '*' | '/' | 'mod' ) unary )* ;
    public final Sample4Parser.mult_return mult() throws RecognitionException {
        Sample4Parser.mult_return retval = new Sample4Parser.mult_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal18=null;
        Token char_literal19=null;
        Token string_literal20=null;
        Sample4Parser.unary_return unary17 = null;

        Sample4Parser.unary_return unary21 = null;


        CommonTree char_literal18_tree=null;
        CommonTree char_literal19_tree=null;
        CommonTree string_literal20_tree=null;

        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:52:2: ( unary ( ( '*' | '/' | 'mod' ) unary )* )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:52:4: unary ( ( '*' | '/' | 'mod' ) unary )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_unary_in_mult169);
            unary17=unary();

            state._fsp--;

            adaptor.addChild(root_0, unary17.getTree());
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:52:10: ( ( '*' | '/' | 'mod' ) unary )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>=16 && LA5_0<=18)) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:52:11: ( '*' | '/' | 'mod' ) unary
            	    {
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:52:11: ( '*' | '/' | 'mod' )
            	    int alt4=3;
            	    switch ( input.LA(1) ) {
            	    case 16:
            	        {
            	        alt4=1;
            	        }
            	        break;
            	    case 17:
            	        {
            	        alt4=2;
            	        }
            	        break;
            	    case 18:
            	        {
            	        alt4=3;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 4, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt4) {
            	        case 1 :
            	            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:52:12: '*'
            	            {
            	            char_literal18=(Token)match(input,16,FOLLOW_16_in_mult173); 
            	            char_literal18_tree = (CommonTree)adaptor.create(char_literal18);
            	            root_0 = (CommonTree)adaptor.becomeRoot(char_literal18_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:52:19: '/'
            	            {
            	            char_literal19=(Token)match(input,17,FOLLOW_17_in_mult178); 
            	            char_literal19_tree = (CommonTree)adaptor.create(char_literal19);
            	            root_0 = (CommonTree)adaptor.becomeRoot(char_literal19_tree, root_0);


            	            }
            	            break;
            	        case 3 :
            	            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:52:26: 'mod'
            	            {
            	            string_literal20=(Token)match(input,18,FOLLOW_18_in_mult183); 
            	            string_literal20_tree = (CommonTree)adaptor.create(string_literal20);
            	            root_0 = (CommonTree)adaptor.becomeRoot(string_literal20_tree, root_0);


            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_unary_in_mult187);
            	    unary21=unary();

            	    state._fsp--;

            	    adaptor.addChild(root_0, unary21.getTree());

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "mult"

    public static class expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expression"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:55:1: expression : mult ( ( '+' | '-' ) mult )* ;
    public final Sample4Parser.expression_return expression() throws RecognitionException {
        Sample4Parser.expression_return retval = new Sample4Parser.expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal23=null;
        Token char_literal24=null;
        Sample4Parser.mult_return mult22 = null;

        Sample4Parser.mult_return mult25 = null;


        CommonTree char_literal23_tree=null;
        CommonTree char_literal24_tree=null;

        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:56:2: ( mult ( ( '+' | '-' ) mult )* )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:56:4: mult ( ( '+' | '-' ) mult )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_mult_in_expression201);
            mult22=mult();

            state._fsp--;

            adaptor.addChild(root_0, mult22.getTree());
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:56:9: ( ( '+' | '-' ) mult )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>=14 && LA7_0<=15)) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:56:10: ( '+' | '-' ) mult
            	    {
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:56:10: ( '+' | '-' )
            	    int alt6=2;
            	    int LA6_0 = input.LA(1);

            	    if ( (LA6_0==14) ) {
            	        alt6=1;
            	    }
            	    else if ( (LA6_0==15) ) {
            	        alt6=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 6, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt6) {
            	        case 1 :
            	            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:56:11: '+'
            	            {
            	            char_literal23=(Token)match(input,14,FOLLOW_14_in_expression205); 
            	            char_literal23_tree = (CommonTree)adaptor.create(char_literal23);
            	            root_0 = (CommonTree)adaptor.becomeRoot(char_literal23_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample4.g:56:18: '-'
            	            {
            	            char_literal24=(Token)match(input,15,FOLLOW_15_in_expression210); 
            	            char_literal24_tree = (CommonTree)adaptor.create(char_literal24);
            	            root_0 = (CommonTree)adaptor.becomeRoot(char_literal24_tree, root_0);


            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_mult_in_expression214);
            	    mult25=mult();

            	    state._fsp--;

            	    adaptor.addChild(root_0, mult25.getTree());

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "expression"

    // Delegated rules


 

    public static final BitSet FOLLOW_assignment_in_evaluator64 = new BitSet(new long[]{0x000000000000D060L});
    public static final BitSet FOLLOW_expression_in_evaluator67 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_evaluator69 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_assignment82 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_assignment84 = new BitSet(new long[]{0x000000000000D060L});
    public static final BitSet FOLLOW_expression_in_assignment87 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_assignment89 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_term104 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_term109 = new BitSet(new long[]{0x000000000000D060L});
    public static final BitSet FOLLOW_expression_in_term112 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_term114 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_in_term120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_unary133 = new BitSet(new long[]{0x000000000000D060L});
    public static final BitSet FOLLOW_negation_in_unary138 = new BitSet(new long[]{0x000000000000D060L});
    public static final BitSet FOLLOW_term_in_unary143 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_negation154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unary_in_mult169 = new BitSet(new long[]{0x0000000000070002L});
    public static final BitSet FOLLOW_16_in_mult173 = new BitSet(new long[]{0x000000000000D060L});
    public static final BitSet FOLLOW_17_in_mult178 = new BitSet(new long[]{0x000000000000D060L});
    public static final BitSet FOLLOW_18_in_mult183 = new BitSet(new long[]{0x000000000000D060L});
    public static final BitSet FOLLOW_unary_in_mult187 = new BitSet(new long[]{0x0000000000070002L});
    public static final BitSet FOLLOW_mult_in_expression201 = new BitSet(new long[]{0x000000000000C002L});
    public static final BitSet FOLLOW_14_in_expression205 = new BitSet(new long[]{0x000000000000D060L});
    public static final BitSet FOLLOW_15_in_expression210 = new BitSet(new long[]{0x000000000000D060L});
    public static final BitSet FOLLOW_mult_in_expression214 = new BitSet(new long[]{0x000000000000C002L});

}