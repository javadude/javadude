// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g 2010-03-18 18:08:13

  package a.b.c;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/*******************************************************************************
 * Copyright (c) 2009 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
public class Sample2Parser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "IDENT", "INTEGER", "MULTILINE_COMMENT", "STRING_LITERAL", "CHAR_LITERAL", "LETTER", "DIGIT", "WS", "COMMENT", "'('", "')'", "'+'", "'-'", "'*'", "'/'", "'mod'"
    };
    public static final int INTEGER=5;
    public static final int LETTER=9;
    public static final int EOF=-1;
    public static final int T__19=19;
    public static final int MULTILINE_COMMENT=6;
    public static final int T__16=16;
    public static final int STRING_LITERAL=7;
    public static final int WS=11;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int CHAR_LITERAL=8;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int IDENT=4;
    public static final int DIGIT=10;
    public static final int COMMENT=12;

    // delegates
    // delegators


        public Sample2Parser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public Sample2Parser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return Sample2Parser.tokenNames; }
    public String getGrammarFileName() { return "C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g"; }



    // $ANTLR start "evaluator"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:22:1: evaluator returns [int result] : expression EOF ;
    public final int evaluator() throws RecognitionException {
        int result = 0;

        int expression1 = 0;


        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:23:2: ( expression EOF )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:23:4: expression EOF
            {
            pushFollow(FOLLOW_expression_in_evaluator46);
            expression1=expression();

            state._fsp--;

            match(input,EOF,FOLLOW_EOF_in_evaluator48); 
             result = expression1; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "evaluator"


    // $ANTLR start "term"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:28:1: term returns [int result] : ( IDENT | '(' expression ')' | INTEGER );
    public final int term() throws RecognitionException {
        int result = 0;

        Token INTEGER3=null;
        int expression2 = 0;


        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:29:2: ( IDENT | '(' expression ')' | INTEGER )
            int alt1=3;
            switch ( input.LA(1) ) {
            case IDENT:
                {
                alt1=1;
                }
                break;
            case 13:
                {
                alt1=2;
                }
                break;
            case INTEGER:
                {
                alt1=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }

            switch (alt1) {
                case 1 :
                    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:29:4: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_term68); 
                    result = 0;

                    }
                    break;
                case 2 :
                    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:30:4: '(' expression ')'
                    {
                    match(input,13,FOLLOW_13_in_term75); 
                    pushFollow(FOLLOW_expression_in_term77);
                    expression2=expression();

                    state._fsp--;

                    match(input,14,FOLLOW_14_in_term79); 
                    result = expression2;

                    }
                    break;
                case 3 :
                    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:31:4: INTEGER
                    {
                    INTEGER3=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_term86); 
                    result = Integer.parseInt((INTEGER3!=null?INTEGER3.getText():null));

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "term"


    // $ANTLR start "unary"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:34:1: unary returns [int result] : ( '+' | '-' )* term ;
    public final int unary() throws RecognitionException {
        int result = 0;

        int term4 = 0;


        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:35:2: ( ( '+' | '-' )* term )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:35:4: ( '+' | '-' )* term
            {
             boolean positive = true; 
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:36:3: ( '+' | '-' )*
            loop2:
            do {
                int alt2=3;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==15) ) {
                    alt2=1;
                }
                else if ( (LA2_0==16) ) {
                    alt2=2;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:36:4: '+'
            	    {
            	    match(input,15,FOLLOW_15_in_unary109); 

            	    }
            	    break;
            	case 2 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:36:10: '-'
            	    {
            	    match(input,16,FOLLOW_16_in_unary113); 
            	     positive = !positive; 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            pushFollow(FOLLOW_term_in_unary119);
            term4=term();

            state._fsp--;


            			result = term4;
            			if (!positive)
            				result = -result;
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "unary"


    // $ANTLR start "mult"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:44:1: mult returns [int result] : op1= unary ( '*' op2= unary | '/' op2= unary | 'mod' op2= unary )* ;
    public final int mult() throws RecognitionException {
        int result = 0;

        int op1 = 0;

        int op2 = 0;


        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:45:2: (op1= unary ( '*' op2= unary | '/' op2= unary | 'mod' op2= unary )* )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:45:4: op1= unary ( '*' op2= unary | '/' op2= unary | 'mod' op2= unary )*
            {
            pushFollow(FOLLOW_unary_in_mult140);
            op1=unary();

            state._fsp--;

             result = op1; 
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:46:3: ( '*' op2= unary | '/' op2= unary | 'mod' op2= unary )*
            loop3:
            do {
                int alt3=4;
                switch ( input.LA(1) ) {
                case 17:
                    {
                    alt3=1;
                    }
                    break;
                case 18:
                    {
                    alt3=2;
                    }
                    break;
                case 19:
                    {
                    alt3=3;
                    }
                    break;

                }

                switch (alt3) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:46:5: '*' op2= unary
            	    {
            	    match(input,17,FOLLOW_17_in_mult148); 
            	    pushFollow(FOLLOW_unary_in_mult152);
            	    op2=unary();

            	    state._fsp--;

            	     result = result * op2; 

            	    }
            	    break;
            	case 2 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:47:5: '/' op2= unary
            	    {
            	    match(input,18,FOLLOW_18_in_mult160); 
            	    pushFollow(FOLLOW_unary_in_mult164);
            	    op2=unary();

            	    state._fsp--;

            	     result = result / op2; 

            	    }
            	    break;
            	case 3 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:48:5: 'mod' op2= unary
            	    {
            	    match(input,19,FOLLOW_19_in_mult172); 
            	    pushFollow(FOLLOW_unary_in_mult176);
            	    op2=unary();

            	    state._fsp--;

            	     result = result % op2; 

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "mult"


    // $ANTLR start "expression"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:52:1: expression returns [int result] : op1= mult ( '+' op2= mult | '-' op2= mult )* ;
    public final int expression() throws RecognitionException {
        int result = 0;

        int op1 = 0;

        int op2 = 0;


        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:53:2: (op1= mult ( '+' op2= mult | '-' op2= mult )* )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:53:4: op1= mult ( '+' op2= mult | '-' op2= mult )*
            {
            pushFollow(FOLLOW_mult_in_expression201);
            op1=mult();

            state._fsp--;

             result = op1; 
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:54:3: ( '+' op2= mult | '-' op2= mult )*
            loop4:
            do {
                int alt4=3;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==15) ) {
                    alt4=1;
                }
                else if ( (LA4_0==16) ) {
                    alt4=2;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:54:5: '+' op2= mult
            	    {
            	    match(input,15,FOLLOW_15_in_expression209); 
            	    pushFollow(FOLLOW_mult_in_expression213);
            	    op2=mult();

            	    state._fsp--;

            	     result = result + op2; 

            	    }
            	    break;
            	case 2 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:55:5: '-' op2= mult
            	    {
            	    match(input,16,FOLLOW_16_in_expression221); 
            	    pushFollow(FOLLOW_mult_in_expression225);
            	    op2=mult();

            	    state._fsp--;

            	     result = result - op2; 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "expression"

    // Delegated rules


 

    public static final BitSet FOLLOW_expression_in_evaluator46 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_evaluator48 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_term68 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_term75 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_expression_in_term77 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_term79 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_in_term86 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_unary109 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_16_in_unary113 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_term_in_unary119 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unary_in_mult140 = new BitSet(new long[]{0x00000000000E0002L});
    public static final BitSet FOLLOW_17_in_mult148 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_unary_in_mult152 = new BitSet(new long[]{0x00000000000E0002L});
    public static final BitSet FOLLOW_18_in_mult160 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_unary_in_mult164 = new BitSet(new long[]{0x00000000000E0002L});
    public static final BitSet FOLLOW_19_in_mult172 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_unary_in_mult176 = new BitSet(new long[]{0x00000000000E0002L});
    public static final BitSet FOLLOW_mult_in_expression201 = new BitSet(new long[]{0x0000000000018002L});
    public static final BitSet FOLLOW_15_in_expression209 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_mult_in_expression213 = new BitSet(new long[]{0x0000000000018002L});
    public static final BitSet FOLLOW_16_in_expression221 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_mult_in_expression225 = new BitSet(new long[]{0x0000000000018002L});

}