// $ANTXR : "antlrSettings.antxr" -> "AntxrSettingsParser.java"$
// GENERATED CODE - DO NOT EDIT!

package com.javadude.antxr.eclipse.core.properties;
import java.util.Map;
import java.util.HashMap;

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
public class AntxrSettingsParser extends com.javadude.antxr.LLkParser       implements AntxrSettingsParserTokenTypes
 {
	// ANTXR XML Mode Support
	private static Map<String, String> __xml_namespaceMap = new HashMap<String, String>();
	public static Map<String, String> getNamespaceMap() {return __xml_namespaceMap;}
	public static String resolveNamespace(String prefix) {
		if (prefix == null || "".equals(prefix))
			return "";
		return __xml_namespaceMap.get(prefix);
	}


protected AntxrSettingsParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public AntxrSettingsParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected AntxrSettingsParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public AntxrSettingsParser(TokenStream lexer) {
  this(lexer,1);
}

public AntxrSettingsParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final Map<String, Map<String, String>>  document() throws RecognitionException, TokenStreamException {
		Map<String, Map<String, String>> projectSettings=null;
		
		
		try {      // for error handling
			projectSettings=__xml_settings();
			match(Token.EOF_TYPE);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return projectSettings;
	}
	
	public final Map<String, Map<String, String>>  __xml_settings() throws RecognitionException, TokenStreamException {
		Map<String, Map<String, String>> projectSettings=new HashMap();
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(4);
			{
			{
			_loop561:
			do {
				if ((LA(1)==6)) {
					__xml_resource(projectSettings);
				}
				else {
					break _loop561;
				}
				
			} while (true);
			}
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return projectSettings;
	}
	
	public final void __xml_resource(
		Map<String, Map<String, String>> projectSettings
	) throws RecognitionException, TokenStreamException {
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(6);
			{
			
			Map resourceSettings = new HashMap();
			projectSettings.put(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"name"), resourceSettings);
			
			{
			_loop565:
			do {
				if ((LA(1)==7)) {
					__xml_property(resourceSettings);
				}
				else {
					break _loop565;
				}
				
			} while (true);
			}
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
	}
	
	public final void __xml_property(
		Map<String, String> settings
	) throws RecognitionException, TokenStreamException {
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(7);
			{
			settings.put(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"name"), ((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"value"));
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"<settings>\"",
		"XML_END_TAG",
		"\"<resource>\"",
		"\"<property>\""
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 96L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 160L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	
	}
