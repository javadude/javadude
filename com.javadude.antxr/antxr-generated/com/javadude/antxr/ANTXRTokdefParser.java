// $ANTXR : "tokdef.antxr" -> "ANTXRTokdefParser.java"$
// GENERATED CODE - DO NOT EDIT!
 package com.javadude.antxr; 
import com.javadude.antxr.TokenBuffer;
import com.javadude.antxr.TokenStreamException;
import com.javadude.antxr.TokenStreamIOException;
import com.javadude.antxr.ANTXRException;
import com.javadude.antxr.LLkParser;
import com.javadude.antxr.Token;
import com.javadude.antxr.TokenStream;
import com.javadude.antxr.RecognitionException;
import com.javadude.antxr.NoViableAltException;
import com.javadude.antxr.MismatchedTokenException;
import com.javadude.antxr.SemanticException;
import com.javadude.antxr.ParserSharedInputState;
import com.javadude.antxr.collections.impl.BitSet;

// ANTXR XML Mode Support
import com.javadude.antxr.scanner.XMLToken;
import com.javadude.antxr.scanner.Attribute;
import java.util.Map;
import java.util.HashMap;


/** Simple lexer/parser for reading token definition files
  in support of the import/export vocab option for grammars.
 */
@SuppressWarnings("all")
public class ANTXRTokdefParser extends com.javadude.antxr.LLkParser       implements ANTXRTokdefParserTokenTypes
 {
	// ANTXR XML Mode Support
	private static Map<String, String> __xml_namespaceMap = new HashMap<String, String>();
	public static Map<String, String> getNamespaceMap() {return __xml_namespaceMap;}
	public static String resolveNamespace(String prefix) {
		if (prefix == null || "".equals(prefix))
			return "";
		return __xml_namespaceMap.get(prefix);
	}


    // This chunk of error reporting code provided by Brian Smith

    private com.javadude.antxr.Tool antxrTool;

    /** In order to make it so existing subclasses don't break, we won't require
     * that the com.javadude.antxr.Tool instance be passed as a constructor element. Instead,
     * the com.javadude.antxr.Tool instance should register itself via {@link #initTool(com.javadude.antxr.Tool)}
     * @throws IllegalStateException if a tool has already been registered
     * @since 2.7.2
     */
    public void setTool(com.javadude.antxr.Tool tool) {
        if (antxrTool == null) {
            antxrTool = tool;
        }
        else {
            throw new IllegalStateException("com.javadude.antxr.Tool already registered");
        }
    }

    /** @since 2.7.2 */
    protected com.javadude.antxr.Tool getTool() {
        return antxrTool;
    }

    /** Delegates the error message to the tool if any was registered via
     *  {@link #initTool(com.javadude.antxr.Tool)}
     *  @since 2.7.2
     */
    public void reportError(String s) {
        if (getTool() != null) {
            getTool().error(s, getFilename(), -1, -1);
        }
        else {
            super.reportError(s);
        }
    }

    /** Delegates the error message to the tool if any was registered via
     *  {@link #initTool(com.javadude.antxr.Tool)}
     *  @since 2.7.2
     */
    public void reportError(RecognitionException e) {
        if (getTool() != null) {
            getTool().error(e.getErrorMessage(), e.getFilename(), e.getLine(), e.getColumn());
        }
        else {
            super.reportError(e);
        }
    }

    /** Delegates the warning message to the tool if any was registered via
     *  {@link #initTool(com.javadude.antxr.Tool)}
     *  @since 2.7.2
     */
    public void reportWarning(String s) {
        if (getTool() != null) {
            getTool().warning(s, getFilename(), -1, -1);
        }
        else {
            super.reportWarning(s);
        }
    }

protected ANTXRTokdefParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public ANTXRTokdefParser(TokenBuffer tokenBuf) {
  this(tokenBuf,3);
}

protected ANTXRTokdefParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public ANTXRTokdefParser(TokenStream lexer) {
  this(lexer,3);
}

public ANTXRTokdefParser(ParserSharedInputState state) {
  super(state,3);
  tokenNames = _tokenNames;
}

	public final void file(
		ImportVocabTokenManager tm
	) throws RecognitionException, TokenStreamException {
		
		Token  name = null;
		
		try {      // for error handling
			name = LT(1);
			match(ID);
			{
			_loop21:
			do {
				if ((LA(1)==ID||LA(1)==STRING)) {
					line(tm);
				}
				else {
					break _loop21;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
	}
	
	public final void line(
		ImportVocabTokenManager tm
	) throws RecognitionException, TokenStreamException {
		
		Token  s1 = null;
		Token  lab = null;
		Token  s2 = null;
		Token  id = null;
		Token  para = null;
		Token  id2 = null;
		Token  i = null;
		Token t=null; Token s=null;
		
		try {      // for error handling
			{
			if ((LA(1)==STRING)) {
				s1 = LT(1);
				match(STRING);
				s = s1;
			}
			else if ((LA(1)==ID) && (LA(2)==ASSIGN) && (LA(3)==STRING)) {
				lab = LT(1);
				match(ID);
				t = lab;
				match(ASSIGN);
				s2 = LT(1);
				match(STRING);
				s = s2;
			}
			else if ((LA(1)==ID) && (LA(2)==LPAREN)) {
				id = LT(1);
				match(ID);
				t=id;
				match(LPAREN);
				para = LT(1);
				match(STRING);
				match(RPAREN);
			}
			else if ((LA(1)==ID) && (LA(2)==ASSIGN) && (LA(3)==INT)) {
				id2 = LT(1);
				match(ID);
				t=id2;
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			match(ASSIGN);
			i = LT(1);
			match(INT);
			
			Integer value = Integer.valueOf(i.getText());
			// if literal found, define as a string literal
			if ( s!=null ) {
			tm.define(s.getText(), value.intValue());
			// if label, then label the string and map label to token symbol also
			if ( t!=null ) {
			StringLiteralSymbol sl =
			(StringLiteralSymbol) tm.getTokenSymbol(s.getText());
			sl.setLabel(t.getText());
			tm.mapToTokenSymbol(t.getText(), sl);
			}
			}
			// define token (not a literal)
			else if ( t!=null ) {
			tm.define(t.getText(), value.intValue());
			if ( para!=null ) {
			TokenSymbol ts = tm.getTokenSymbol(t.getText());
			ts.setParaphrase(
			para.getText()
			);
			}
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"ID",
		"STRING",
		"ASSIGN",
		"LPAREN",
		"RPAREN",
		"INT",
		"WS",
		"SL_COMMENT",
		"ML_COMMENT",
		"ESC",
		"DIGIT",
		"XDIGIT"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 50L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	
	}
