// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g 2010-03-18 18:08:13

  package a.b.c;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class Sample2Lexer extends Lexer {
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

    public Sample2Lexer() {;} 
    public Sample2Lexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public Sample2Lexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g"; }

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:11:7: ( '(' )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:11:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:12:7: ( ')' )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:12:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:13:7: ( '+' )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:13:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:14:7: ( '-' )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:14:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:15:7: ( '*' )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:15:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:16:7: ( '/' )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:16:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:17:7: ( 'mod' )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:17:9: 'mod'
            {
            match("mod"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "MULTILINE_COMMENT"
    public final void mMULTILINE_COMMENT() throws RecognitionException {
        try {
            int _type = MULTILINE_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:60:19: ( '/*' ( . )* '*/' )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:60:21: '/*' ( . )* '*/'
            {
            match("/*"); 

            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:60:26: ( . )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='*') ) {
                    int LA1_1 = input.LA(2);

                    if ( (LA1_1=='/') ) {
                        alt1=2;
                    }
                    else if ( ((LA1_1>='\u0000' && LA1_1<='.')||(LA1_1>='0' && LA1_1<='\uFFFF')) ) {
                        alt1=1;
                    }


                }
                else if ( ((LA1_0>='\u0000' && LA1_0<=')')||(LA1_0>='+' && LA1_0<='\uFFFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:60:26: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match("*/"); 

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MULTILINE_COMMENT"

    // $ANTLR start "STRING_LITERAL"
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int c;

            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:63:2: ( '\"' ( '\"' '\"' | c=~ ( '\"' | '\\r' | '\\n' ) )* '\"' )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:63:4: '\"' ( '\"' '\"' | c=~ ( '\"' | '\\r' | '\\n' ) )* '\"'
            {
            match('\"'); 
             StringBuilder b = new StringBuilder(); 
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:65:3: ( '\"' '\"' | c=~ ( '\"' | '\\r' | '\\n' ) )*
            loop2:
            do {
                int alt2=3;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='\"') ) {
                    int LA2_1 = input.LA(2);

                    if ( (LA2_1=='\"') ) {
                        alt2=1;
                    }


                }
                else if ( ((LA2_0>='\u0000' && LA2_0<='\t')||(LA2_0>='\u000B' && LA2_0<='\f')||(LA2_0>='\u000E' && LA2_0<='!')||(LA2_0>='#' && LA2_0<='\uFFFF')) ) {
                    alt2=2;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:65:5: '\"' '\"'
            	    {
            	    match('\"'); 
            	    match('\"'); 
            	     b.appendCodePoint('"');

            	    }
            	    break;
            	case 2 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:66:5: c=~ ( '\"' | '\\r' | '\\n' )
            	    {
            	    c= input.LA(1);
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}

            	     b.appendCodePoint(c);

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match('\"'); 
             setText(b.toString()); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING_LITERAL"

    // $ANTLR start "CHAR_LITERAL"
    public final void mCHAR_LITERAL() throws RecognitionException {
        try {
            int _type = CHAR_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:73:2: ( '\\'' . '\\'' )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:73:4: '\\'' . '\\''
            {
            match('\''); 
            matchAny(); 
            match('\''); 
            setText(getText().substring(1,2));

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CHAR_LITERAL"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:76:17: ( ( 'a' .. 'z' | 'A' .. 'Z' ) )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:76:19: ( 'a' .. 'z' | 'A' .. 'Z' )
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:77:16: ( '0' .. '9' )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:77:18: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "INTEGER"
    public final void mINTEGER() throws RecognitionException {
        try {
            int _type = INTEGER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:78:9: ( ( DIGIT )+ )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:78:11: ( DIGIT )+
            {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:78:11: ( DIGIT )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:78:11: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INTEGER"

    // $ANTLR start "IDENT"
    public final void mIDENT() throws RecognitionException {
        try {
            int _type = IDENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:79:7: ( LETTER ( LETTER | DIGIT )* )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:79:9: LETTER ( LETTER | DIGIT )*
            {
            mLETTER(); 
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:79:16: ( LETTER | DIGIT )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')||(LA4_0>='A' && LA4_0<='Z')||(LA4_0>='a' && LA4_0<='z')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IDENT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:80:4: ( ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+ )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:80:6: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+
            {
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:80:6: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='\t' && LA5_0<='\n')||(LA5_0>='\f' && LA5_0<='\r')||LA5_0==' ') ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:81:9: ( '//' ( . )* ( '\\n' | '\\r' ) )
            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:81:11: '//' ( . )* ( '\\n' | '\\r' )
            {
            match("//"); 

            // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:81:16: ( . )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='\n'||LA6_0=='\r') ) {
                    alt6=2;
                }
                else if ( ((LA6_0>='\u0000' && LA6_0<='\t')||(LA6_0>='\u000B' && LA6_0<='\f')||(LA6_0>='\u000E' && LA6_0<='\uFFFF')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:81:16: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            if ( input.LA(1)=='\n'||input.LA(1)=='\r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    public void mTokens() throws RecognitionException {
        // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:1:8: ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | MULTILINE_COMMENT | STRING_LITERAL | CHAR_LITERAL | INTEGER | IDENT | WS | COMMENT )
        int alt7=14;
        alt7 = dfa7.predict(input);
        switch (alt7) {
            case 1 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:1:10: T__13
                {
                mT__13(); 

                }
                break;
            case 2 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:1:16: T__14
                {
                mT__14(); 

                }
                break;
            case 3 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:1:22: T__15
                {
                mT__15(); 

                }
                break;
            case 4 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:1:28: T__16
                {
                mT__16(); 

                }
                break;
            case 5 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:1:34: T__17
                {
                mT__17(); 

                }
                break;
            case 6 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:1:40: T__18
                {
                mT__18(); 

                }
                break;
            case 7 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:1:46: T__19
                {
                mT__19(); 

                }
                break;
            case 8 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:1:52: MULTILINE_COMMENT
                {
                mMULTILINE_COMMENT(); 

                }
                break;
            case 9 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:1:70: STRING_LITERAL
                {
                mSTRING_LITERAL(); 

                }
                break;
            case 10 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:1:85: CHAR_LITERAL
                {
                mCHAR_LITERAL(); 

                }
                break;
            case 11 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:1:98: INTEGER
                {
                mINTEGER(); 

                }
                break;
            case 12 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:1:106: IDENT
                {
                mIDENT(); 

                }
                break;
            case 13 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:1:112: WS
                {
                mWS(); 

                }
                break;
            case 14 :
                // C:\\eclipse352\\workspace\\ANTLR Tutorial 3.x - 1\\src\\a\\b\\c\\Sample2.g:1:115: COMMENT
                {
                mCOMMENT(); 

                }
                break;

        }

    }


    protected DFA7 dfa7 = new DFA7(this);
    static final String DFA7_eotS =
        "\6\uffff\1\17\1\13\10\uffff\1\13\1\22\1\uffff";
    static final String DFA7_eofS =
        "\23\uffff";
    static final String DFA7_minS =
        "\1\11\5\uffff\1\52\1\157\10\uffff\1\144\1\60\1\uffff";
    static final String DFA7_maxS =
        "\1\172\5\uffff\1\57\1\157\10\uffff\1\144\1\172\1\uffff";
    static final String DFA7_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\2\uffff\1\11\1\12\1\13\1\14\1\15\1"+
        "\10\1\16\1\6\2\uffff\1\7";
    static final String DFA7_specialS =
        "\23\uffff}>";
    static final String[] DFA7_transitionS = {
            "\2\14\1\uffff\2\14\22\uffff\1\14\1\uffff\1\10\4\uffff\1\11"+
            "\1\1\1\2\1\5\1\3\1\uffff\1\4\1\uffff\1\6\12\12\7\uffff\32\13"+
            "\6\uffff\14\13\1\7\15\13",
            "",
            "",
            "",
            "",
            "",
            "\1\15\4\uffff\1\16",
            "\1\20",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\21",
            "\12\13\7\uffff\32\13\6\uffff\32\13",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | MULTILINE_COMMENT | STRING_LITERAL | CHAR_LITERAL | INTEGER | IDENT | WS | COMMENT );";
        }
    }
 

}