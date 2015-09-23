// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g 2010-03-18 18:08:09

  package a.b.c;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class TextFilterGrammarParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "OR", "AND", "NOT", "WORD", "WS"
    };
    public static final int WORD=7;
    public static final int WS=8;
    public static final int OR=4;
    public static final int NOT=6;
    public static final int AND=5;
    public static final int EOF=-1;

    // delegates
    // delegators


        public TextFilterGrammarParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public TextFilterGrammarParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return TextFilterGrammarParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g"; }


    public static class content_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "content"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:14:1: content : orexpression ;
    public final TextFilterGrammarParser.content_return content() throws RecognitionException {
        TextFilterGrammarParser.content_return retval = new TextFilterGrammarParser.content_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        TextFilterGrammarParser.orexpression_return orexpression1 = null;



        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:14:9: ( orexpression )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:14:17: orexpression
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_orexpression_in_content54);
            orexpression1=orexpression();

            state._fsp--;

            adaptor.addChild(root_0, orexpression1.getTree());

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
    // $ANTLR end "content"

    public static class orexpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "orexpression"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:16:1: orexpression : andexpression ( OR andexpression )* ;
    public final TextFilterGrammarParser.orexpression_return orexpression() throws RecognitionException {
        TextFilterGrammarParser.orexpression_return retval = new TextFilterGrammarParser.orexpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token OR3=null;
        TextFilterGrammarParser.andexpression_return andexpression2 = null;

        TextFilterGrammarParser.andexpression_return andexpression4 = null;


        CommonTree OR3_tree=null;

        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:17:8: ( andexpression ( OR andexpression )* )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:17:16: andexpression ( OR andexpression )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_andexpression_in_orexpression82);
            andexpression2=andexpression();

            state._fsp--;

            adaptor.addChild(root_0, andexpression2.getTree());
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:17:30: ( OR andexpression )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==OR) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:17:31: OR andexpression
            	    {
            	    OR3=(Token)match(input,OR,FOLLOW_OR_in_orexpression85); 
            	    OR3_tree = (CommonTree)adaptor.create(OR3);
            	    root_0 = (CommonTree)adaptor.becomeRoot(OR3_tree, root_0);

            	    pushFollow(FOLLOW_andexpression_in_orexpression88);
            	    andexpression4=andexpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, andexpression4.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
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
    // $ANTLR end "orexpression"

    public static class andexpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "andexpression"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:19:1: andexpression : expression ( AND expression )* ;
    public final TextFilterGrammarParser.andexpression_return andexpression() throws RecognitionException {
        TextFilterGrammarParser.andexpression_return retval = new TextFilterGrammarParser.andexpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token AND6=null;
        TextFilterGrammarParser.expression_return expression5 = null;

        TextFilterGrammarParser.expression_return expression7 = null;


        CommonTree AND6_tree=null;

        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:20:8: ( expression ( AND expression )* )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:20:16: expression ( AND expression )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_expression_in_andexpression118);
            expression5=expression();

            state._fsp--;

            adaptor.addChild(root_0, expression5.getTree());
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:20:27: ( AND expression )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==AND) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:20:28: AND expression
            	    {
            	    AND6=(Token)match(input,AND,FOLLOW_AND_in_andexpression121); 
            	    AND6_tree = (CommonTree)adaptor.create(AND6);
            	    root_0 = (CommonTree)adaptor.becomeRoot(AND6_tree, root_0);

            	    pushFollow(FOLLOW_expression_in_andexpression124);
            	    expression7=expression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, expression7.getTree());

            	    }
            	    break;

            	default :
            	    break loop2;
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
    // $ANTLR end "andexpression"

    public static class expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expression"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:22:1: expression : ( NOT )? term ;
    public final TextFilterGrammarParser.expression_return expression() throws RecognitionException {
        TextFilterGrammarParser.expression_return retval = new TextFilterGrammarParser.expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NOT8=null;
        TextFilterGrammarParser.term_return term9 = null;


        CommonTree NOT8_tree=null;

        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:23:8: ( ( NOT )? term )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:23:16: ( NOT )? term
            {
            root_0 = (CommonTree)adaptor.nil();

            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:23:16: ( NOT )?
            int alt3=2;
            alt3 = dfa3.predict(input);
            switch (alt3) {
                case 1 :
                    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:23:17: NOT
                    {
                    NOT8=(Token)match(input,NOT,FOLLOW_NOT_in_expression155); 
                    NOT8_tree = (CommonTree)adaptor.create(NOT8);
                    root_0 = (CommonTree)adaptor.becomeRoot(NOT8_tree, root_0);


                    }
                    break;

            }

            pushFollow(FOLLOW_term_in_expression160);
            term9=term();

            state._fsp--;

            adaptor.addChild(root_0, term9.getTree());

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

    public static class term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "term"
    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:25:1: term : ( WORD | NOT | AND | OR );
    public final TextFilterGrammarParser.term_return term() throws RecognitionException {
        TextFilterGrammarParser.term_return retval = new TextFilterGrammarParser.term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set10=null;

        CommonTree set10_tree=null;

        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:25:9: ( WORD | NOT | AND | OR )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set10=(Token)input.LT(1);
            if ( (input.LA(1)>=OR && input.LA(1)<=WORD) ) {
                input.consume();
                adaptor.addChild(root_0, (CommonTree)adaptor.create(set10));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


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

    // Delegated rules


    protected DFA3 dfa3 = new DFA3(this);
    static final String DFA3_eotS =
        "\30\uffff";
    static final String DFA3_eofS =
        "\1\uffff\1\2\1\uffff\2\5\1\uffff\4\2\6\5\5\2\3\5";
    static final String DFA3_minS =
        "\2\4\1\uffff\2\4\1\uffff\22\4";
    static final String DFA3_maxS =
        "\2\7\1\uffff\2\7\1\uffff\22\7";
    static final String DFA3_acceptS =
        "\2\uffff\1\2\2\uffff\1\1\22\uffff";
    static final String DFA3_specialS =
        "\30\uffff}>";
    static final String[] DFA3_transitionS = {
            "\2\2\1\1\1\2",
            "\1\4\1\3\2\5",
            "",
            "\1\7\1\6\2\2",
            "\1\11\1\10\2\2",
            "",
            "\1\12\1\13\2\5",
            "\1\14\1\15\2\5",
            "\1\12\1\16\2\5",
            "\1\14\1\17\2\5",
            "\1\11\1\10\2\2",
            "\1\7\1\6\2\2",
            "\1\11\1\20\2\2",
            "\1\7\1\21\2\2",
            "\1\22\1\23\2\2",
            "\1\22\1\24\2\2",
            "\1\26\1\25\2\5",
            "\1\26\1\27\2\5",
            "\1\14\1\17\2\5",
            "\1\12\1\16\2\5",
            "\1\26\1\25\2\5",
            "\1\22\1\24\2\2",
            "\1\11\1\20\2\2",
            "\1\7\1\21\2\2"
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "23:16: ( NOT )?";
        }
    }
 

    public static final BitSet FOLLOW_orexpression_in_content54 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andexpression_in_orexpression82 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_OR_in_orexpression85 = new BitSet(new long[]{0x00000000000000F0L});
    public static final BitSet FOLLOW_andexpression_in_orexpression88 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_expression_in_andexpression118 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_AND_in_andexpression121 = new BitSet(new long[]{0x00000000000000F0L});
    public static final BitSet FOLLOW_expression_in_andexpression124 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_NOT_in_expression155 = new BitSet(new long[]{0x00000000000000F0L});
    public static final BitSet FOLLOW_term_in_expression160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_term0 = new BitSet(new long[]{0x0000000000000002L});

}