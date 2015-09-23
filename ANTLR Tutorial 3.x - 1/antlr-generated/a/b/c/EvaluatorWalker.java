// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g 2010-03-18 18:08:16

  package a.b.c;
  import java.util.Map;
  import java.util.HashMap;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class EvaluatorWalker extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NEGATION", "IDENT", "INTEGER", "LETTER", "DIGIT", "WS", "':='", "';'", "'('", "')'", "'+'", "'-'", "'*'", "'/'", "'mod'", "'%'"
    };
    public static final int INTEGER=6;
    public static final int LETTER=7;
    public static final int EOF=-1;
    public static final int T__19=19;
    public static final int T__16=16;
    public static final int WS=9;
    public static final int T__15=15;
    public static final int NEGATION=4;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int T__10=10;
    public static final int IDENT=5;
    public static final int DIGIT=8;

    // delegates
    // delegators


        public EvaluatorWalker(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public EvaluatorWalker(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return EvaluatorWalker.tokenNames; }
    public String getGrammarFileName() { return "C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g"; }


      private Map<String, Integer> variables = new HashMap<String, Integer>();



    // $ANTLR start "evaluator"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:19:1: evaluator returns [int result] : ( assignment )* e= expression EOF ;
    public final int evaluator() throws RecognitionException {
        int result = 0;

        int e = 0;


        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:20:2: ( ( assignment )* e= expression EOF )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:20:4: ( assignment )* e= expression EOF
            {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:20:4: ( assignment )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==10) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:20:4: assignment
            	    {
            	    pushFollow(FOLLOW_assignment_in_evaluator61);
            	    assignment();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            pushFollow(FOLLOW_expression_in_evaluator66);
            e=expression();

            state._fsp--;

            match(input,EOF,FOLLOW_EOF_in_evaluator68); 
             result = e; 

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


    // $ANTLR start "assignment"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:23:1: assignment : ^( ':=' IDENT e= expression ) ;
    public final void assignment() throws RecognitionException {
        CommonTree IDENT1=null;
        int e = 0;


        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:24:2: ( ^( ':=' IDENT e= expression ) )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:24:4: ^( ':=' IDENT e= expression )
            {
            match(input,10,FOLLOW_10_in_assignment83); 

            match(input, Token.DOWN, null); 
            IDENT1=(CommonTree)match(input,IDENT,FOLLOW_IDENT_in_assignment85); 
            pushFollow(FOLLOW_expression_in_assignment89);
            e=expression();

            state._fsp--;


            match(input, Token.UP, null); 
             variables.put((IDENT1!=null?IDENT1.getText():null), e); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "assignment"


    // $ANTLR start "expression"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:28:1: expression returns [int result] : ( ^( '+' op1= expression op2= expression ) | ^( '-' op1= expression op2= expression ) | ^( '*' op1= expression op2= expression ) | ^( '/' op1= expression op2= expression ) | ^( '%' op1= expression op2= expression ) | ^( NEGATION e= expression ) | IDENT | INTEGER );
    public final int expression() throws RecognitionException {
        int result = 0;

        CommonTree IDENT2=null;
        CommonTree INTEGER3=null;
        int op1 = 0;

        int op2 = 0;

        int e = 0;


        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:29:2: ( ^( '+' op1= expression op2= expression ) | ^( '-' op1= expression op2= expression ) | ^( '*' op1= expression op2= expression ) | ^( '/' op1= expression op2= expression ) | ^( '%' op1= expression op2= expression ) | ^( NEGATION e= expression ) | IDENT | INTEGER )
            int alt2=8;
            switch ( input.LA(1) ) {
            case 14:
                {
                alt2=1;
                }
                break;
            case 15:
                {
                alt2=2;
                }
                break;
            case 16:
                {
                alt2=3;
                }
                break;
            case 17:
                {
                alt2=4;
                }
                break;
            case 19:
                {
                alt2=5;
                }
                break;
            case NEGATION:
                {
                alt2=6;
                }
                break;
            case IDENT:
                {
                alt2=7;
                }
                break;
            case INTEGER:
                {
                alt2=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:29:4: ^( '+' op1= expression op2= expression )
                    {
                    match(input,14,FOLLOW_14_in_expression112); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression116);
                    op1=expression();

                    state._fsp--;

                    pushFollow(FOLLOW_expression_in_expression120);
                    op2=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     result = op1 + op2; 

                    }
                    break;
                case 2 :
                    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:30:4: ^( '-' op1= expression op2= expression )
                    {
                    match(input,15,FOLLOW_15_in_expression129); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression133);
                    op1=expression();

                    state._fsp--;

                    pushFollow(FOLLOW_expression_in_expression137);
                    op2=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     result = op1 - op2; 

                    }
                    break;
                case 3 :
                    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:31:4: ^( '*' op1= expression op2= expression )
                    {
                    match(input,16,FOLLOW_16_in_expression146); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression150);
                    op1=expression();

                    state._fsp--;

                    pushFollow(FOLLOW_expression_in_expression154);
                    op2=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     result = op1 * op2; 

                    }
                    break;
                case 4 :
                    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:32:4: ^( '/' op1= expression op2= expression )
                    {
                    match(input,17,FOLLOW_17_in_expression163); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression167);
                    op1=expression();

                    state._fsp--;

                    pushFollow(FOLLOW_expression_in_expression171);
                    op2=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     result = op1 / op2; 

                    }
                    break;
                case 5 :
                    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:33:4: ^( '%' op1= expression op2= expression )
                    {
                    match(input,19,FOLLOW_19_in_expression180); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression184);
                    op1=expression();

                    state._fsp--;

                    pushFollow(FOLLOW_expression_in_expression188);
                    op2=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     result = op1 % op2; 

                    }
                    break;
                case 6 :
                    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:34:4: ^( NEGATION e= expression )
                    {
                    match(input,NEGATION,FOLLOW_NEGATION_in_expression197); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_expression_in_expression201);
                    e=expression();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     result = -e; 

                    }
                    break;
                case 7 :
                    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:35:4: IDENT
                    {
                    IDENT2=(CommonTree)match(input,IDENT,FOLLOW_IDENT_in_expression210); 
                     result = variables.get((IDENT2!=null?IDENT2.getText():null)); 

                    }
                    break;
                case 8 :
                    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\EvaluatorWalker.g:36:4: INTEGER
                    {
                    INTEGER3=(CommonTree)match(input,INTEGER,FOLLOW_INTEGER_in_expression217); 
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
    // $ANTLR end "expression"

    // Delegated rules


 

    public static final BitSet FOLLOW_assignment_in_evaluator61 = new BitSet(new long[]{0x00000000000BC470L});
    public static final BitSet FOLLOW_expression_in_evaluator66 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_evaluator68 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_10_in_assignment83 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_assignment85 = new BitSet(new long[]{0x00000000000BC070L});
    public static final BitSet FOLLOW_expression_in_assignment89 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_14_in_expression112 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression116 = new BitSet(new long[]{0x00000000000BC070L});
    public static final BitSet FOLLOW_expression_in_expression120 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_15_in_expression129 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression133 = new BitSet(new long[]{0x00000000000BC070L});
    public static final BitSet FOLLOW_expression_in_expression137 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_16_in_expression146 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression150 = new BitSet(new long[]{0x00000000000BC070L});
    public static final BitSet FOLLOW_expression_in_expression154 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_17_in_expression163 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression167 = new BitSet(new long[]{0x00000000000BC070L});
    public static final BitSet FOLLOW_expression_in_expression171 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_19_in_expression180 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression184 = new BitSet(new long[]{0x00000000000BC070L});
    public static final BitSet FOLLOW_expression_in_expression188 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NEGATION_in_expression197 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_expression201 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IDENT_in_expression210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_in_expression217 = new BitSet(new long[]{0x0000000000000002L});

}