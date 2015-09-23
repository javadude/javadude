// $ANTXR : "antlr.antxr" -> "AntxrParser.java"$
// GENERATED CODE - DO NOT EDIT!

package com.javadude.antxr.eclipse.core.parser;

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


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

@SuppressWarnings("all")
public class AntxrParser extends com.javadude.antxr.LLkParser       implements AntxrParserTokenTypes
 {
	// ANTXR XML Mode Support
	private static Map<String, String> __xml_namespaceMap = new HashMap<String, String>();
	public static Map<String, String> getNamespaceMap() {return __xml_namespaceMap;}
	public static String resolveNamespace(String prefix) {
		if (prefix == null || "".equals(prefix))
			return "";
		return __xml_namespaceMap.get(prefix);
	}


  // Define a main
  public static void main(String[] anArgs) {
    try {
      if (anArgs.length > 0 ) {
          System.out.println("Start lexing...");
        for (int i = 0; i < anArgs.length; i++) {
          doFile(new File(anArgs[i]));
        }
      } else {
        System.err.println("Usage: java AntxrParser <file/directory name>");
      }
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public static void doFile(File aFile) throws IOException {
    if (aFile.isDirectory()) {
      String files[] = aFile.list();
      for (int i = 0; i < files.length; i++) {
        doFile(new File(aFile, files[i]));
      }
    } else if (aFile.getName().length() > 2 &&
             aFile.getName().substring(aFile.getName().length() - 2).equals(".antxr")) {
      System.out.println("   " + aFile.getAbsolutePath());
      parseFile(aFile);
    }
  }

  public static void parseFile(File aFile) throws IOException {
    try {
      AntxrLexer lexer = new AntxrLexer(new FileInputStream(aFile));
      AntxrParser parser = new AntxrParser(lexer);
      Hierarchy h = parser.grammarFile(aFile.getName());
      System.out.println("Header: " + h.getHeader());
      System.out.println("Options: " + h.getOptions());
      System.out.println(h);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

protected AntxrParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public AntxrParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected AntxrParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public AntxrParser(TokenStream lexer) {
  this(lexer,1);
}

public AntxrParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final Hierarchy  grammarFile(
		String name
	) throws RecognitionException, TokenStreamException {
		Hierarchy h;
		
		Token  hdr = null;
		Token  opt = null;
		Token  end = null;
		
		h = new Hierarchy(name);
		
		
		try {      // for error handling
			{
			_loop570:
			do {
				if ((LA(1)==HEADER)) {
					hdr = LT(1);
					match(HEADER);
					h.setHeader(hdr);
				}
				else {
					break _loop570;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case OPTIONS:
			{
				opt = LT(1);
				match(OPTIONS);
				h.setOptions(opt);
				break;
			}
			case EOF:
			case ACTION:
			case DOC_COMMENT:
			case LITERAL_class:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop573:
			do {
				if (((LA(1) >= ACTION && LA(1) <= LITERAL_class))) {
					classDef(h);
				}
				else {
					break _loop573;
				}
				
			} while (true);
			}
			end = LT(1);
			match(Token.EOF_TYPE);
			h.setEndLine(end.getColumn());
		}
		catch (Exception e) {
			
			h.setException(e);
			
		}
		return h;
	}
	
	public final void classDef(
		Hierarchy h
	) throws RecognitionException, TokenStreamException {
		
		Token  preamble = null;
		Token  doc = null;
		Token  cls = null;
		Token  sub = null;
		Token  sup = null;
		Token  opt = null;
		Token  tk = null;
		Token  memberA = null;
		
		Grammar g = null;
		
		
		{
		switch ( LA(1)) {
		case ACTION:
		{
			preamble = LT(1);
			match(ACTION);
			break;
		}
		case DOC_COMMENT:
		case LITERAL_class:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case DOC_COMMENT:
		{
			doc = LT(1);
			match(DOC_COMMENT);
			break;
		}
		case LITERAL_class:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		cls = LT(1);
		match(LITERAL_class);
		sub = LT(1);
		match(ID);
		match(LITERAL_extends);
		sup = LT(1);
		match(ID);
		match(SEMI);
		g = new Grammar(h, sub.getText(), sup.getText(), cls.getLine());
		if (preamble != null) {
		g.setPreamble(preamble);
		}
		if (doc != null) {
		g.setDocComment(doc);
		}
		h.addGrammar(g);
		
		}
		{
		switch ( LA(1)) {
		case OPTIONS:
		{
			opt = LT(1);
			match(OPTIONS);
			g.setOptions(opt);
			break;
		}
		case ACTION:
		case DOC_COMMENT:
		case ID:
		case TOKENS:
		case LITERAL_protected:
		case LITERAL_private:
		case LITERAL_public:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case TOKENS:
		{
			tk = LT(1);
			match(TOKENS);
			g.setTokens(tk);
			break;
		}
		case ACTION:
		case DOC_COMMENT:
		case ID:
		case LITERAL_protected:
		case LITERAL_private:
		case LITERAL_public:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case ACTION:
		{
			memberA = LT(1);
			match(ACTION);
			g.setMemberAction(memberA);
			break;
		}
		case DOC_COMMENT:
		case ID:
		case LITERAL_protected:
		case LITERAL_private:
		case LITERAL_public:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		int _cnt582=0;
		_loop582:
		do {
			if ((_tokenSet_0.member(LA(1)))) {
				rule(g);
			}
			else {
				if ( _cnt582>=1 ) { break _loop582; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt582++;
		} while (true);
		}
		Rule r = g.getLastRule();
		if (r == null) {
		g.setEndLine(g.getStartLine());
		} else {
		g.setEndLine(r.getEndLine());
		}
		
	}
	
	public final void rule(
		Grammar g
	) throws RecognitionException, TokenStreamException {
		
		Token  doc = null;
		Token  p1 = null;
		Token  p2 = null;
		Token  p3 = null;
		Token  n1 = null;
		Token  opt = null;
		Token  memberA = null;
		Token  rb = null;
		Token  e = null;
		Token  a = null;
		
		Rule r;
		int vis = Rule.PUBLIC, line = -1;
		
		
		{
		switch ( LA(1)) {
		case DOC_COMMENT:
		{
			doc = LT(1);
			match(DOC_COMMENT);
			break;
		}
		case ID:
		case LITERAL_protected:
		case LITERAL_private:
		case LITERAL_public:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case LITERAL_protected:
		{
			p1 = LT(1);
			match(LITERAL_protected);
			vis = Rule.PROTECTED; line = p1.getLine();
			break;
		}
		case LITERAL_private:
		{
			p2 = LT(1);
			match(LITERAL_private);
			vis = Rule.PRIVATE;   line = p2.getLine();
			break;
		}
		case LITERAL_public:
		{
			p3 = LT(1);
			match(LITERAL_public);
			vis = Rule.PUBLIC;    line = p3.getLine();
			break;
		}
		case ID:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		n1 = LT(1);
		match(ID);
		
		if (line == -1) {
		vis = Rule.PUBLIC;
		line = n1.getLine();
		}
		r = new Rule(g, n1.getText(), vis, line);
		if (doc != null)
		r.setDocComment(doc);
		g.addRule(r);
		
		{
		switch ( LA(1)) {
		case BANG:
		{
			match(BANG);
			r.setIsExcluded(true);
			break;
		}
		case OPTIONS:
		case ACTION:
		case ARG_ACTION:
		case LITERAL_returns:
		case LITERAL_throws:
		case RULE_BLOCK:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case ARG_ACTION:
		{
			match(ARG_ACTION);
			break;
		}
		case OPTIONS:
		case ACTION:
		case LITERAL_returns:
		case LITERAL_throws:
		case RULE_BLOCK:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case LITERAL_returns:
		{
			match(LITERAL_returns);
			match(ARG_ACTION);
			break;
		}
		case OPTIONS:
		case ACTION:
		case LITERAL_throws:
		case RULE_BLOCK:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case LITERAL_throws:
		{
			match(LITERAL_throws);
			match(ID);
			{
			_loop591:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					match(ID);
				}
				else {
					break _loop591;
				}
				
			} while (true);
			}
			break;
		}
		case OPTIONS:
		case ACTION:
		case RULE_BLOCK:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case OPTIONS:
		{
			opt = LT(1);
			match(OPTIONS);
			r.setOptions(opt);
			break;
		}
		case ACTION:
		case RULE_BLOCK:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case ACTION:
		{
			memberA = LT(1);
			match(ACTION);
			r.setMemberAction(memberA);
			break;
		}
		case RULE_BLOCK:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		rb = LT(1);
		match(RULE_BLOCK);
		r.setEndLine(rb.getColumn());
		{
		_loop598:
		do {
			if ((LA(1)==LITERAL_exception)) {
				e = LT(1);
				match(LITERAL_exception);
				{
				switch ( LA(1)) {
				case ARG_ACTION:
				{
					match(ARG_ACTION);
					break;
				}
				case EOF:
				case ACTION:
				case DOC_COMMENT:
				case LITERAL_class:
				case ID:
				case LITERAL_protected:
				case LITERAL_private:
				case LITERAL_public:
				case LITERAL_exception:
				case LITERAL_catch:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				_loop597:
				do {
					if ((LA(1)==LITERAL_catch)) {
						match(LITERAL_catch);
						match(ARG_ACTION);
						a = LT(1);
						match(ACTION);
						r.setEndLine(a.getColumn());
						r.addException(e);
					}
					else {
						break _loop597;
					}
					
				} while (true);
				}
			}
			else {
				break _loop598;
			}
			
		} while (true);
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"HEADER",
		"OPTIONS",
		"ACTION",
		"DOC_COMMENT",
		"\"class\"",
		"ID",
		"\"extends\"",
		"SEMI",
		"TOKENS",
		"\"protected\"",
		"\"private\"",
		"\"public\"",
		"BANG",
		"ARG_ACTION",
		"\"returns\"",
		"\"throws\"",
		"COMMA",
		"RULE_BLOCK",
		"\"exception\"",
		"\"catch\"",
		"SUBRULE_BLOCK",
		"ALT",
		"ELEMENT",
		"RCURLY",
		"ID_OR_KEYWORD",
		"CURLY_BLOCK_SCARF",
		"WS",
		"NEWLINE",
		"COMMENT",
		"SL_COMMENT",
		"ML_COMMENT",
		"CHAR_LITERAL",
		"STRING_LITERAL",
		"ESC",
		"DIGIT",
		"XDIGIT"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 57984L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	
	}
