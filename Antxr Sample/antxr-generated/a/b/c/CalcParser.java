// $ANTXR : "Calc.antxr" -> "CalcParser.java"$
// GENERATED CODE - DO NOT EDIT!

	package a.b.c;

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


@SuppressWarnings("all")
public class CalcParser extends com.javadude.antxr.LLkParser       implements CalcParserTokenTypes
 {
	// ANTXR XML Mode Support
	private static Map<String, String> __xml_namespaceMap = new HashMap<String, String>();
	public static Map<String, String> getNamespaceMap() {return __xml_namespaceMap;}
	public static String resolveNamespace(String prefix) {
		if (prefix == null || "".equals(prefix))
			return "";
		return __xml_namespaceMap.get(prefix);
	}


protected CalcParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public CalcParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected CalcParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public CalcParser(TokenStream lexer) {
  this(lexer,1);
}

public CalcParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final int  expression() throws RecognitionException, TokenStreamException {
		int value=0;
		
		int other;
		
		try {      // for error handling
			value=mult();
			{
			_loop184:
			do {
				switch ( LA(1)) {
				case PLUS:
				{
					match(PLUS);
					other=mult();
					value += other;
					break;
				}
				case MINUS:
				{
					match(MINUS);
					other=mult();
					value -= other;
					break;
				}
				default:
				{
					break _loop184;
				}
				}
			} while (true);
			}
			match(Token.EOF_TYPE);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return value;
	}
	
	public final int  mult() throws RecognitionException, TokenStreamException {
		int value=0;
		
		int other;
		
		try {      // for error handling
			value=term();
			{
			_loop187:
			do {
				switch ( LA(1)) {
				case TIMES:
				{
					match(TIMES);
					other=term();
					value *= other;
					break;
				}
				case DIVIDE:
				{
					match(DIVIDE);
					other=term();
					value /= other;
					break;
				}
				default:
				{
					break _loop187;
				}
				}
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return value;
	}
	
	public final int  term() throws RecognitionException, TokenStreamException {
		int value=0;
		
		Token  i = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case INT:
			{
				i = LT(1);
				match(INT);
				value = Integer.parseInt(i.getText());
				break;
			}
			case LPAREN:
			{
				match(LPAREN);
				value=expression();
				match(RPAREN);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		return value;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"PLUS",
		"MINUS",
		"TIMES",
		"DIVIDE",
		"INT",
		"LPAREN",
		"RPAREN",
		"WS"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 1024L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 50L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 242L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	
	}
