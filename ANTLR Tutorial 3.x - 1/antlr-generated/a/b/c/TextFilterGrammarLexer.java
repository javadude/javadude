// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g 2010-03-18 18:08:10

  package a.b.c;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class TextFilterGrammarLexer extends Lexer {
    public static final int WORD=7;
    public static final int WS=8;
    public static final int OR=4;
    public static final int NOT=6;
    public static final int AND=5;
    public static final int EOF=-1;

    // delegates
    // delegators

    public TextFilterGrammarLexer() {;} 
    public TextFilterGrammarLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public TextFilterGrammarLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g"; }

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:28:9: ( 'not' )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:28:17: 'not'
            {
            match("not"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOT"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:30:9: ( 'and' )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:30:17: 'and'
            {
            match("and"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AND"

    // $ANTLR start "OR"
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:32:9: ( 'or' )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:32:17: 'or'
            {
            match("or"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OR"

    // $ANTLR start "WORD"
    public final void mWORD() throws RecognitionException {
        try {
            int _type = WORD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:34:9: ( ( 'a' .. 'z' | '0' .. '9' | '%' | '_' )+ )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:34:17: ( 'a' .. 'z' | '0' .. '9' | '%' | '_' )+
            {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:34:17: ( 'a' .. 'z' | '0' .. '9' | '%' | '_' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='%'||(LA1_0>='0' && LA1_0<='9')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:
            	    {
            	    if ( input.LA(1)=='%'||(input.LA(1)>='0' && input.LA(1)<='9')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WORD"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:36:9: ( ( ' ' | '\\r' | '\\n' | '\\t' ) )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:36:17: ( ' ' | '\\r' | '\\n' | '\\t' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

             skip(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:1:8: ( NOT | AND | OR | WORD | WS )
        int alt2=5;
        alt2 = dfa2.predict(input);
        switch (alt2) {
            case 1 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:1:10: NOT
                {
                mNOT(); 

                }
                break;
            case 2 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:1:14: AND
                {
                mAND(); 

                }
                break;
            case 3 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:1:18: OR
                {
                mOR(); 

                }
                break;
            case 4 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:1:21: WORD
                {
                mWORD(); 

                }
                break;
            case 5 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\TextFilterGrammar.g:1:26: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA2 dfa2 = new DFA2(this);
    static final String DFA2_eotS =
        "\1\uffff\3\4\2\uffff\2\4\1\13\1\14\1\15\3\uffff";
    static final String DFA2_eofS =
        "\16\uffff";
    static final String DFA2_minS =
        "\1\11\1\157\1\156\1\162\2\uffff\1\164\1\144\3\45\3\uffff";
    static final String DFA2_maxS =
        "\1\172\1\157\1\156\1\162\2\uffff\1\164\1\144\3\172\3\uffff";
    static final String DFA2_acceptS =
        "\4\uffff\1\4\1\5\5\uffff\1\3\1\1\1\2";
    static final String DFA2_specialS =
        "\16\uffff}>";
    static final String[] DFA2_transitionS = {
            "\2\5\2\uffff\1\5\22\uffff\1\5\4\uffff\1\4\12\uffff\12\4\45"+
            "\uffff\1\4\1\uffff\1\2\14\4\1\1\1\3\13\4",
            "\1\6",
            "\1\7",
            "\1\10",
            "",
            "",
            "\1\11",
            "\1\12",
            "\1\4\12\uffff\12\4\45\uffff\1\4\1\uffff\32\4",
            "\1\4\12\uffff\12\4\45\uffff\1\4\1\uffff\32\4",
            "\1\4\12\uffff\12\4\45\uffff\1\4\1\uffff\32\4",
            "",
            "",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( NOT | AND | OR | WORD | WS );";
        }
    }
 

}