// $ANTXR : "antlr.antxr" -> "ANTXRParser.java"$
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


import java.util.Enumeration;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

@SuppressWarnings("all")
public class ANTXRParser extends com.javadude.antxr.LLkParser       implements ANTXRTokenTypes
 {
	// ANTXR XML Mode Support
	private static Map<String, String> __xml_namespaceMap = new HashMap<String, String>();
	public static Map<String, String> getNamespaceMap() {return __xml_namespaceMap;}
	public static String resolveNamespace(String prefix) {
		if (prefix == null || "".equals(prefix))
			return "";
		return __xml_namespaceMap.get(prefix);
	}


        private static final boolean DEBUG_PARSER = false;

        ANTXRGrammarParseBehavior behavior;
        Tool antxrTool;
        protected int blockNesting= -1;

        public ANTXRParser(
                TokenBuffer tokenBuf,
                ANTXRGrammarParseBehavior behavior_,
                Tool tool_
        ) {
                super(tokenBuf, 1);
                tokenNames = _tokenNames;
                behavior = behavior_;
                antxrTool = tool_;
        }

        public void reportError(String s) {
            antxrTool.error(s, getFilename(), -1, -1);
        }

        public void reportError(RecognitionException e) {
            reportError(e, e.getErrorMessage());
        }

        public void reportError(RecognitionException e, String s) {
            antxrTool.error(s, e.getFilename(), e.getLine(), e.getColumn());
        }

        public void reportWarning(String s) {
            antxrTool.warning(s, getFilename(), -1, -1);
        }

        private boolean lastInRule() throws TokenStreamException {
                if ( blockNesting==0 && (LA(1)==SEMI || LA(1)==LITERAL_exception || LA(1)==OR) ) {
                        return true;
                }
                return false;
        }

        private void checkForMissingEndRule(Token label) {
                if ( label.getColumn()==1 ) {
                        antxrTool.warning("did you forget to terminate previous rule?", getFilename(), label.getLine(), label.getColumn());
                }
        }

        private static final Token XML_TAG_TOKEN = new CommonToken(RULE_REF, "xmlTag");

protected ANTXRParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public ANTXRParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected ANTXRParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public ANTXRParser(TokenStream lexer) {
  this(lexer,2);
}

public ANTXRParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	public final void grammar() throws RecognitionException, TokenStreamException {
		
		Token  n = null;
		Token  h = null;
		
		try {      // for error handling
			{
			_loop188:
			do {
				if ((LA(1)==LITERAL_header)) {
					if ( inputState.guessing==0 ) {
						
						n = null;	// RK: prevent certain orders of header actions
						// overwriting eachother.
						
					}
					match(LITERAL_header);
					{
					switch ( LA(1)) {
					case STRING_LITERAL:
					{
						n = LT(1);
						match(STRING_LITERAL);
						break;
					}
					case ACTION:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					h = LT(1);
					match(ACTION);
					if ( inputState.guessing==0 ) {
						
						// store the header action
						// FIXME: 'n' should be checked for validity
						behavior.refHeaderAction(n,h);
						
					}
				}
				else {
					break _loop188;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case OPTIONS:
			{
				fileOptionsSpec();
				break;
			}
			case EOF:
			case ACTION:
			case DOC_COMMENT:
			case LITERAL_lexclass:
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
			_loop191:
			do {
				if (((LA(1) >= ACTION && LA(1) <= LITERAL_class))) {
					classDef();
				}
				else {
					break _loop191;
				}
				
			} while (true);
			}
			match(Token.EOF_TYPE);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
				reportError(ex, "rule grammar trapped:\n"+ex.toString());
				consumeUntil(EOF);
				
			} else {
				throw ex;
			}
		}
	}
	
	public final void fileOptionsSpec() throws RecognitionException, TokenStreamException {
		
		Token idTok; Token value;
		
		match(OPTIONS);
		{
		_loop202:
		do {
			if ((LA(1)==TOKEN_REF||LA(1)==RULE_REF)) {
				idTok=id();
				match(ASSIGN);
				value=optionValue();
				if ( inputState.guessing==0 ) {
					behavior.setFileOption(idTok, value,getInputState().filename);
				}
				match(SEMI);
			}
			else {
				break _loop202;
			}
			
		} while (true);
		}
		match(RCURLY);
	}
	
	public final void classDef() throws RecognitionException, TokenStreamException {
		
		Token  a = null;
		Token  d = null;
		String doc=null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case ACTION:
			{
				a = LT(1);
				match(ACTION);
				if ( inputState.guessing==0 ) {
					behavior.refPreambleAction(a);
				}
				break;
			}
			case DOC_COMMENT:
			case LITERAL_lexclass:
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
				d = LT(1);
				match(DOC_COMMENT);
				if ( inputState.guessing==0 ) {
					doc=d.getText();
				}
				break;
			}
			case LITERAL_lexclass:
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
			boolean synPredMatched197 = false;
			if (((LA(1)==LITERAL_lexclass||LA(1)==LITERAL_class) && (LA(2)==TOKEN_REF||LA(2)==RULE_REF))) {
				int _m197 = mark();
				synPredMatched197 = true;
				inputState.guessing++;
				try {
					{
					switch ( LA(1)) {
					case LITERAL_lexclass:
					{
						match(LITERAL_lexclass);
						break;
					}
					case LITERAL_class:
					{
						match(LITERAL_class);
						id();
						match(LITERAL_extends);
						match(LITERAL_Lexer);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
				}
				catch (RecognitionException pe) {
					synPredMatched197 = false;
				}
				rewind(_m197);
inputState.guessing--;
			}
			if ( synPredMatched197 ) {
				lexerSpec(doc);
			}
			else {
				boolean synPredMatched199 = false;
				if (((LA(1)==LITERAL_class) && (LA(2)==TOKEN_REF||LA(2)==RULE_REF))) {
					int _m199 = mark();
					synPredMatched199 = true;
					inputState.guessing++;
					try {
						{
						match(LITERAL_class);
						id();
						match(LITERAL_extends);
						match(LITERAL_TreeParser);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched199 = false;
					}
					rewind(_m199);
inputState.guessing--;
				}
				if ( synPredMatched199 ) {
					treeParserSpec(doc);
				}
				else if ((LA(1)==LITERAL_class) && (LA(2)==TOKEN_REF||LA(2)==RULE_REF)) {
					parserSpec(doc);
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				rules();
				if ( inputState.guessing==0 ) {
					behavior.endGrammar();
				}
			}
			catch (RecognitionException ex) {
				if (inputState.guessing==0) {
					
					if ( ex instanceof NoViableAltException ) {
					NoViableAltException e = (NoViableAltException)ex;
					// RK: These probably generate inconsequent error messages...
					// have to see how this comes out..
					if ( e.token.getType()==DOC_COMMENT ) {
					reportError(ex, "JAVADOC comments may only prefix rules and grammars");
					}
					else {
					reportError(ex, "rule classDef trapped:\n"+ex.toString());
					}
					}
					else {
					reportError(ex, "rule classDef trapped:\n"+ex.toString());
					}
					behavior.abortGrammar();
					boolean consuming = true;
					// consume everything until the next class definition or EOF
					while (consuming) {
					consume();
					switch(LA(1)) {
					case LITERAL_class:
					case LITERAL_lexclass:
					case EOF:
					consuming = false;
					break;
					}
					}
					
				} else {
					throw ex;
				}
			}
		}
		
	public final  Token  id() throws RecognitionException, TokenStreamException {
		 Token idTok ;
		
		Token  a = null;
		Token  b = null;
		idTok = null;
		
		switch ( LA(1)) {
		case TOKEN_REF:
		{
			a = LT(1);
			match(TOKEN_REF);
			if ( inputState.guessing==0 ) {
				idTok = a;
			}
			break;
		}
		case RULE_REF:
		{
			b = LT(1);
			match(RULE_REF);
			if ( inputState.guessing==0 ) {
				idTok = b;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return idTok ;
	}
	
	public final void lexerSpec(
		String doc
	) throws RecognitionException, TokenStreamException {
		
		Token  lc = null;
		Token  a = null;
		
		Token idTok;
		String sup=null;
		
		
		{
		switch ( LA(1)) {
		case LITERAL_lexclass:
		{
			lc = LT(1);
			match(LITERAL_lexclass);
			idTok=id();
			if ( inputState.guessing==0 ) {
				
				antxrTool.warning("lexclass' is deprecated; use 'class X extends Lexer'",
				getFilename(), lc.getLine(), lc.getColumn());
				//				System.out.println("warning: line " + lc.getLine() + ": 'lexclass' is deprecated; use 'class X extends Lexer'");
				
			}
			break;
		}
		case LITERAL_class:
		{
			match(LITERAL_class);
			idTok=id();
			match(LITERAL_extends);
			match(LITERAL_Lexer);
			{
			switch ( LA(1)) {
			case LPAREN:
			{
				sup=superClass();
				break;
			}
			case SEMI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			behavior.startLexer(getFilename(), idTok,sup,doc);
		}
		match(SEMI);
		{
		switch ( LA(1)) {
		case OPTIONS:
		{
			lexerOptionsSpec();
			break;
		}
		case ACTION:
		case DOC_COMMENT:
		case TOKENS:
		case TOKEN_REF:
		case LITERAL_protected:
		case LITERAL_public:
		case LITERAL_private:
		case RULE_REF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			behavior.endOptions();
		}
		{
		switch ( LA(1)) {
		case TOKENS:
		{
			tokensSpec();
			break;
		}
		case ACTION:
		case DOC_COMMENT:
		case TOKEN_REF:
		case LITERAL_protected:
		case LITERAL_public:
		case LITERAL_private:
		case RULE_REF:
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
			a = LT(1);
			match(ACTION);
			if ( inputState.guessing==0 ) {
				behavior.refMemberAction(a);
			}
			break;
		}
		case DOC_COMMENT:
		case TOKEN_REF:
		case LITERAL_protected:
		case LITERAL_public:
		case LITERAL_private:
		case RULE_REF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void treeParserSpec(
		String doc
	) throws RecognitionException, TokenStreamException {
		
		Token  a = null;
		
		Token idTok;
		String sup=null;
		
		
		match(LITERAL_class);
		idTok=id();
		match(LITERAL_extends);
		match(LITERAL_TreeParser);
		{
		switch ( LA(1)) {
		case LPAREN:
		{
			sup=superClass();
			break;
		}
		case SEMI:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			behavior.startTreeWalker(getFilename(), idTok,sup,doc);
		}
		match(SEMI);
		{
		switch ( LA(1)) {
		case OPTIONS:
		{
			treeParserOptionsSpec();
			break;
		}
		case ACTION:
		case DOC_COMMENT:
		case TOKENS:
		case TOKEN_REF:
		case LITERAL_protected:
		case LITERAL_public:
		case LITERAL_private:
		case RULE_REF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			behavior.endOptions();
		}
		{
		switch ( LA(1)) {
		case TOKENS:
		{
			tokensSpec();
			break;
		}
		case ACTION:
		case DOC_COMMENT:
		case TOKEN_REF:
		case LITERAL_protected:
		case LITERAL_public:
		case LITERAL_private:
		case RULE_REF:
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
			a = LT(1);
			match(ACTION);
			if ( inputState.guessing==0 ) {
				behavior.refMemberAction(a);
			}
			break;
		}
		case DOC_COMMENT:
		case TOKEN_REF:
		case LITERAL_protected:
		case LITERAL_public:
		case LITERAL_private:
		case RULE_REF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void parserSpec(
		String doc
	) throws RecognitionException, TokenStreamException {
		
		Token  a = null;
		
		Token idTok;
		String sup=null;
		
		
		match(LITERAL_class);
		idTok=id();
		{
		switch ( LA(1)) {
		case LITERAL_extends:
		{
			match(LITERAL_extends);
			match(LITERAL_Parser);
			{
			switch ( LA(1)) {
			case LPAREN:
			{
				sup=superClass();
				break;
			}
			case SEMI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case SEMI:
		{
			if ( inputState.guessing==0 ) {
				
				antxrTool.warning("use 'class X extends Parser'", getFilename(), idTok.getLine(), idTok.getColumn());
				//			System.out.println("warning: line " +
				//				idTok.getLine() + ": use 'class X extends Parser'");
				
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			behavior.startParser(getFilename(), idTok, sup, doc);
		}
		match(SEMI);
		{
		switch ( LA(1)) {
		case OPTIONS:
		{
			parserOptionsSpec();
			break;
		}
		case ACTION:
		case DOC_COMMENT:
		case TOKENS:
		case TOKEN_REF:
		case LITERAL_protected:
		case LITERAL_public:
		case LITERAL_private:
		case RULE_REF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			behavior.endOptions();
		}
		{
		switch ( LA(1)) {
		case TOKENS:
		{
			tokensSpec();
			break;
		}
		case ACTION:
		case DOC_COMMENT:
		case TOKEN_REF:
		case LITERAL_protected:
		case LITERAL_public:
		case LITERAL_private:
		case RULE_REF:
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
			a = LT(1);
			match(ACTION);
			if ( inputState.guessing==0 ) {
				behavior.refMemberAction(a);
			}
			break;
		}
		case DOC_COMMENT:
		case TOKEN_REF:
		case LITERAL_protected:
		case LITERAL_public:
		case LITERAL_private:
		case RULE_REF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void rules() throws RecognitionException, TokenStreamException {
		
		
		{
		int _cnt255=0;
		_loop255:
		do {
			if ((_tokenSet_0.member(LA(1))) && (_tokenSet_1.member(LA(2)))) {
				rule();
			}
			else {
				if ( _cnt255>=1 ) { break _loop255; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt255++;
		} while (true);
		}
	}
	
	public final  Token  optionValue() throws RecognitionException, TokenStreamException {
		 Token retval ;
		
		Token  sl = null;
		Token  cl = null;
		Token  il = null;
		retval = null;
		
		switch ( LA(1)) {
		case TOKEN_REF:
		case RULE_REF:
		{
			retval=qualifiedID();
			break;
		}
		case STRING_LITERAL:
		{
			sl = LT(1);
			match(STRING_LITERAL);
			if ( inputState.guessing==0 ) {
				retval = sl;
			}
			break;
		}
		case CHAR_LITERAL:
		{
			cl = LT(1);
			match(CHAR_LITERAL);
			if ( inputState.guessing==0 ) {
				retval = cl;
			}
			break;
		}
		case INT:
		{
			il = LT(1);
			match(INT);
			if ( inputState.guessing==0 ) {
				retval = il;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return retval ;
	}
	
	public final void parserOptionsSpec() throws RecognitionException, TokenStreamException {
		
		Token nsTok=null; Token idTok; Token value; String optBase=""; Token next=null;
		
		match(OPTIONS);
		{
		_loop208:
		do {
			if ((LA(1)==TOKEN_REF||LA(1)==RULE_REF)) {
				{
				if ((LA(1)==TOKEN_REF||LA(1)==RULE_REF) && (LA(2)==COLON)) {
					nsTok=id();
					match(COLON);
				}
				else if ((LA(1)==TOKEN_REF||LA(1)==RULE_REF) && (LA(2)==ASSIGN||LA(2)==DASH)) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				idTok=id();
				if ( inputState.guessing==0 ) {
					optBase = idTok.getText();
				}
				{
				_loop207:
				do {
					if ((LA(1)==DASH)) {
						match(DASH);
						next=id();
						if ( inputState.guessing==0 ) {
							optBase += '-' + next.getText();
						}
					}
					else {
						break _loop207;
					}
					
				} while (true);
				}
				match(ASSIGN);
				value=optionValue();
				if ( inputState.guessing==0 ) {
					
					if (nsTok != null)
					idTok.setText(nsTok.getText() + ':' + optBase);
					
				}
				if ( inputState.guessing==0 ) {
					behavior.setGrammarOption(idTok, value);
				}
				match(SEMI);
			}
			else {
				break _loop208;
			}
			
		} while (true);
		}
		match(RCURLY);
	}
	
	public final void treeParserOptionsSpec() throws RecognitionException, TokenStreamException {
		
		Token idTok; Token value;
		
		match(OPTIONS);
		{
		_loop211:
		do {
			if ((LA(1)==TOKEN_REF||LA(1)==RULE_REF)) {
				idTok=id();
				match(ASSIGN);
				value=optionValue();
				if ( inputState.guessing==0 ) {
					behavior.setGrammarOption(idTok, value);
				}
				match(SEMI);
			}
			else {
				break _loop211;
			}
			
		} while (true);
		}
		match(RCURLY);
	}
	
	public final void lexerOptionsSpec() throws RecognitionException, TokenStreamException {
		
		Token idTok; Token value; BitSet b;
		
		match(OPTIONS);
		{
		_loop214:
		do {
			switch ( LA(1)) {
			case LITERAL_charVocabulary:
			{
				match(LITERAL_charVocabulary);
				match(ASSIGN);
				b=charSet();
				match(SEMI);
				if ( inputState.guessing==0 ) {
					behavior.setCharVocabulary(b);
				}
				break;
			}
			case TOKEN_REF:
			case RULE_REF:
			{
				idTok=id();
				match(ASSIGN);
				value=optionValue();
				if ( inputState.guessing==0 ) {
					behavior.setGrammarOption(idTok, value);
				}
				match(SEMI);
				break;
			}
			default:
			{
				break _loop214;
			}
			}
		} while (true);
		}
		match(RCURLY);
	}
	
	public final  BitSet  charSet() throws RecognitionException, TokenStreamException {
		 BitSet b ;
		
		
		b = null;
		BitSet tmpSet = null;
		
		
		b=setBlockElement();
		{
		_loop221:
		do {
			if ((LA(1)==OR)) {
				match(OR);
				tmpSet=setBlockElement();
				if ( inputState.guessing==0 ) {
					b.orInPlace(tmpSet);
				}
			}
			else {
				break _loop221;
			}
			
		} while (true);
		}
		return b ;
	}
	
	public final void subruleOptionsSpec() throws RecognitionException, TokenStreamException {
		
		Token idTok; Token value;
		
		match(OPTIONS);
		{
		_loop217:
		do {
			if ((LA(1)==TOKEN_REF||LA(1)==RULE_REF)) {
				idTok=id();
				match(ASSIGN);
				value=optionValue();
				if ( inputState.guessing==0 ) {
					behavior.setSubruleOption(idTok, value);
				}
				match(SEMI);
			}
			else {
				break _loop217;
			}
			
		} while (true);
		}
		match(RCURLY);
	}
	
/** Match a.b.c.d qualified ids; WILDCARD here is overloaded as
 *  id separator; that is, I need a reference to the '.' token.
 */
	public final Token  qualifiedID() throws RecognitionException, TokenStreamException {
		Token qidTok=null;
		
		
		StringBuffer buf = new StringBuffer(30);
		Token a;
		
		
		a=id();
		if ( inputState.guessing==0 ) {
			buf.append(a.getText());
		}
		{
		_loop331:
		do {
			if ((LA(1)==WILDCARD)) {
				match(WILDCARD);
				a=id();
				if ( inputState.guessing==0 ) {
					buf.append('.'); buf.append(a.getText());
				}
			}
			else {
				break _loop331;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			
			// can use either TOKEN_REF or RULE_REF; should
			// really create a QID or something instead.
			qidTok = new CommonToken(TOKEN_REF, buf.toString());
			qidTok.setLine(a.getLine());
			
		}
		return qidTok;
	}
	
	public final  BitSet  setBlockElement() throws RecognitionException, TokenStreamException {
		 BitSet b ;
		
		Token  c1 = null;
		Token  c2 = null;
		
		b = null;
		int rangeMin = 0;
		
		
		c1 = LT(1);
		match(CHAR_LITERAL);
		if ( inputState.guessing==0 ) {
			
			rangeMin = ANTXRLexer.tokenTypeForCharLiteral(c1.getText());
			b = BitSet.of(rangeMin);
			
		}
		{
		switch ( LA(1)) {
		case RANGE:
		{
			match(RANGE);
			c2 = LT(1);
			match(CHAR_LITERAL);
			if ( inputState.guessing==0 ) {
				
				int rangeMax = ANTXRLexer.tokenTypeForCharLiteral(c2.getText());
				if (rangeMax < rangeMin) {
				antxrTool.error("Malformed range line ", getFilename(), c1.getLine(), c1.getColumn());
				}
				for (int i = rangeMin+1; i <= rangeMax; i++) {
				b.add(i);
				}
				
			}
			break;
		}
		case SEMI:
		case OR:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return b ;
	}
	
	public final void tokensSpec() throws RecognitionException, TokenStreamException {
		
		Token  t1 = null;
		Token  s1 = null;
		Token  s3 = null;
		
		match(TOKENS);
		{
		int _cnt230=0;
		_loop230:
		do {
			if ((LA(1)==STRING_LITERAL||LA(1)==TOKEN_REF)) {
				{
				switch ( LA(1)) {
				case TOKEN_REF:
				{
					if ( inputState.guessing==0 ) {
						s1=null;
					}
					t1 = LT(1);
					match(TOKEN_REF);
					{
					switch ( LA(1)) {
					case ASSIGN:
					{
						match(ASSIGN);
						s1 = LT(1);
						match(STRING_LITERAL);
						break;
					}
					case SEMI:
					case OPEN_ELEMENT_OPTION:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						behavior.defineToken(t1, s1);
					}
					{
					switch ( LA(1)) {
					case OPEN_ELEMENT_OPTION:
					{
						tokensSpecOptions(t1);
						break;
					}
					case SEMI:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					break;
				}
				case STRING_LITERAL:
				{
					s3 = LT(1);
					match(STRING_LITERAL);
					if ( inputState.guessing==0 ) {
						behavior.defineToken(null, s3);
					}
					{
					switch ( LA(1)) {
					case OPEN_ELEMENT_OPTION:
					{
						tokensSpecOptions(s3);
						break;
					}
					case SEMI:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(SEMI);
			}
			else {
				if ( _cnt230>=1 ) { break _loop230; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt230++;
		} while (true);
		}
		match(RCURLY);
	}
	
	public final void tokensSpecOptions(
		Token t
	) throws RecognitionException, TokenStreamException {
		
		
		Token o=null, v=null;
		
		
		match(OPEN_ELEMENT_OPTION);
		o=id();
		match(ASSIGN);
		v=optionValue();
		if ( inputState.guessing==0 ) {
			behavior.refTokensSpecElementOption(t,o,v);
		}
		{
		_loop233:
		do {
			if ((LA(1)==SEMI)) {
				match(SEMI);
				o=id();
				match(ASSIGN);
				v=optionValue();
				if ( inputState.guessing==0 ) {
					behavior.refTokensSpecElementOption(t,o,v);
				}
			}
			else {
				break _loop233;
			}
			
		} while (true);
		}
		match(CLOSE_ELEMENT_OPTION);
	}
	
	public final String  superClass() throws RecognitionException, TokenStreamException {
		String sup;
		
		sup=null;
		
		match(LPAREN);
		if ( inputState.guessing==0 ) {
			
			sup = LT(1).getText();
			sup = StringUtils.stripFrontBack(sup, "\"", "\"");
			
		}
		{
		match(STRING_LITERAL);
		}
		match(RPAREN);
		return sup;
	}
	
	public final void rule() throws RecognitionException, TokenStreamException {
		
		Token  d = null;
		Token  p1 = null;
		Token  p2 = null;
		Token  p3 = null;
		Token  aa = null;
		Token  rt = null;
		Token  a = null;
		
		String access="public";
		Token idTok;
		String doc=null;
		boolean ruleAutoGen = true;
		Token xmlTag = null;
		blockNesting = -1;	// block increments, so -1 to make rule at level 0
		
		
		{
		switch ( LA(1)) {
		case DOC_COMMENT:
		{
			d = LT(1);
			match(DOC_COMMENT);
			if ( inputState.guessing==0 ) {
				doc=d.getText();
			}
			break;
		}
		case TOKEN_REF:
		case LITERAL_protected:
		case LITERAL_public:
		case LITERAL_private:
		case RULE_REF:
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
			if ( inputState.guessing==0 ) {
				access=p1.getText();
			}
			break;
		}
		case LITERAL_public:
		{
			p2 = LT(1);
			match(LITERAL_public);
			if ( inputState.guessing==0 ) {
				access=p2.getText();
			}
			break;
		}
		case LITERAL_private:
		{
			p3 = LT(1);
			match(LITERAL_private);
			if ( inputState.guessing==0 ) {
				access=p3.getText();
			}
			break;
		}
		case TOKEN_REF:
		case RULE_REF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		idTok=id();
		if ( inputState.guessing==0 ) {
			
			String text = idTok.getText();
			if (text.startsWith("<")) {
			xmlTag = new CommonToken(RULE_REF, text.substring(0, text.length()-1));
			behavior.fixRuleName(idTok);
			}
			
		}
		{
		switch ( LA(1)) {
		case BANG:
		{
			match(BANG);
			if ( inputState.guessing==0 ) {
				ruleAutoGen = false;
			}
			break;
		}
		case ACTION:
		case OPTIONS:
		case COLON:
		case ARG_ACTION:
		case LITERAL_returns:
		case LITERAL_throws:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			
			behavior.defineRuleName(idTok, access, ruleAutoGen, doc);
			if (xmlTag != null)
			behavior.setRuleOption(XML_TAG_TOKEN, xmlTag);
			
		}
		{
		switch ( LA(1)) {
		case ARG_ACTION:
		{
			aa = LT(1);
			match(ARG_ACTION);
			if ( inputState.guessing==0 ) {
				behavior.refArgAction(aa);
			}
			break;
		}
		case ACTION:
		case OPTIONS:
		case COLON:
		case LITERAL_returns:
		case LITERAL_throws:
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
			rt = LT(1);
			match(ARG_ACTION);
			if ( inputState.guessing==0 ) {
				behavior.refReturnAction(rt);
			}
			break;
		}
		case ACTION:
		case OPTIONS:
		case COLON:
		case LITERAL_throws:
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
			throwsSpec();
			break;
		}
		case ACTION:
		case OPTIONS:
		case COLON:
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
			ruleOptionsSpec();
			break;
		}
		case ACTION:
		case COLON:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			behavior.beginRule(idTok, LT(1), ruleAutoGen);
		}
		{
		switch ( LA(1)) {
		case ACTION:
		{
			a = LT(1);
			match(ACTION);
			if ( inputState.guessing==0 ) {
				behavior.refInitAction(a);
			}
			break;
		}
		case COLON:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(COLON);
		block();
		match(SEMI);
		{
		switch ( LA(1)) {
		case LITERAL_exception:
		{
			exceptionGroup();
			break;
		}
		case EOF:
		case ACTION:
		case DOC_COMMENT:
		case LITERAL_lexclass:
		case LITERAL_class:
		case TOKEN_REF:
		case LITERAL_protected:
		case LITERAL_public:
		case LITERAL_private:
		case RULE_REF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			behavior.endRule(idTok.getText());
		}
	}
	
	public final void throwsSpec() throws RecognitionException, TokenStreamException {
		
		
		String t=null;
		Token a,b;
		
		
		match(LITERAL_throws);
		a=id();
		if ( inputState.guessing==0 ) {
			t=a.getText();
		}
		{
		_loop271:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				b=id();
				if ( inputState.guessing==0 ) {
					t+=","+b.getText();
				}
			}
			else {
				break _loop271;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			behavior.setUserExceptions(t);	
		}
	}
	
	public final void ruleOptionsSpec() throws RecognitionException, TokenStreamException {
		
		Token idTok; Token value;
		
		match(OPTIONS);
		{
		_loop268:
		do {
			if ((LA(1)==TOKEN_REF||LA(1)==RULE_REF)) {
				idTok=id();
				match(ASSIGN);
				value=optionValue();
				if ( inputState.guessing==0 ) {
					behavior.setRuleOption(idTok, value);
				}
				match(SEMI);
			}
			else {
				break _loop268;
			}
			
		} while (true);
		}
		match(RCURLY);
	}
	
	public final void block() throws RecognitionException, TokenStreamException {
		
		
		if ( inputState.guessing==0 ) {
			blockNesting++;
		}
		alternative();
		{
		_loop274:
		do {
			if ((LA(1)==OR)) {
				match(OR);
				alternative();
			}
			else {
				break _loop274;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			blockNesting--;
		}
	}
	
	public final void exceptionGroup() throws RecognitionException, TokenStreamException {
		
		
		if ( inputState.guessing==0 ) {
			behavior.beginExceptionGroup();
		}
		{
		int _cnt282=0;
		_loop282:
		do {
			if ((LA(1)==LITERAL_exception)) {
				exceptionSpec();
			}
			else {
				if ( _cnt282>=1 ) { break _loop282; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt282++;
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			behavior.endExceptionGroup();
		}
	}
	
	public final void alternative() throws RecognitionException, TokenStreamException {
		
		boolean altAutoGen = true;
		
		{
		switch ( LA(1)) {
		case BANG:
		{
			match(BANG);
			if ( inputState.guessing==0 ) {
				altAutoGen=false;
			}
			break;
		}
		case STRING_LITERAL:
		case ACTION:
		case SEMI:
		case CHAR_LITERAL:
		case OR:
		case TOKEN_REF:
		case LPAREN:
		case RPAREN:
		case LITERAL_exception:
		case RULE_REF:
		case NOT_OP:
		case SEMPRED:
		case TREE_BEGIN:
		case WILDCARD:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			behavior.beginAlt(altAutoGen);
		}
		{
		_loop278:
		do {
			if ((_tokenSet_2.member(LA(1)))) {
				element();
			}
			else {
				break _loop278;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case LITERAL_exception:
		{
			exceptionSpecNoLabel();
			break;
		}
		case SEMI:
		case OR:
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			behavior.endAlt();
		}
	}
	
	public final void element() throws RecognitionException, TokenStreamException {
		
		
		elementNoOptionSpec();
		{
		switch ( LA(1)) {
		case OPEN_ELEMENT_OPTION:
		{
			elementOptionSpec();
			break;
		}
		case STRING_LITERAL:
		case ACTION:
		case SEMI:
		case CHAR_LITERAL:
		case OR:
		case TOKEN_REF:
		case LPAREN:
		case RPAREN:
		case LITERAL_exception:
		case RULE_REF:
		case NOT_OP:
		case SEMPRED:
		case TREE_BEGIN:
		case WILDCARD:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void exceptionSpecNoLabel() throws RecognitionException, TokenStreamException {
		
		
		match(LITERAL_exception);
		if ( inputState.guessing==0 ) {
			behavior.beginExceptionSpec(null);
		}
		{
		_loop289:
		do {
			if ((LA(1)==LITERAL_catch)) {
				exceptionHandler();
			}
			else {
				break _loop289;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			behavior.endExceptionSpec();
		}
	}
	
	public final void exceptionSpec() throws RecognitionException, TokenStreamException {
		
		Token  aa = null;
		Token labelAction = null;
		
		match(LITERAL_exception);
		{
		switch ( LA(1)) {
		case ARG_ACTION:
		{
			aa = LT(1);
			match(ARG_ACTION);
			if ( inputState.guessing==0 ) {
				labelAction = aa;
			}
			break;
		}
		case EOF:
		case ACTION:
		case DOC_COMMENT:
		case LITERAL_lexclass:
		case LITERAL_class:
		case TOKEN_REF:
		case LITERAL_protected:
		case LITERAL_public:
		case LITERAL_private:
		case LITERAL_exception:
		case LITERAL_catch:
		case RULE_REF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			behavior.beginExceptionSpec(labelAction);
		}
		{
		_loop286:
		do {
			if ((LA(1)==LITERAL_catch)) {
				exceptionHandler();
			}
			else {
				break _loop286;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			behavior.endExceptionSpec();
		}
	}
	
	public final void exceptionHandler() throws RecognitionException, TokenStreamException {
		
		Token  a1 = null;
		Token  a2 = null;
		Token exType; Token exName;
		
		match(LITERAL_catch);
		a1 = LT(1);
		match(ARG_ACTION);
		a2 = LT(1);
		match(ACTION);
		if ( inputState.guessing==0 ) {
			behavior.refExceptionHandler(a1, a2);
		}
	}
	
	public final void elementNoOptionSpec() throws RecognitionException, TokenStreamException {
		
		Token  rr = null;
		Token  aa = null;
		Token  tr = null;
		Token  aa2 = null;
		Token  r2 = null;
		Token  aa3 = null;
		Token  a = null;
		Token  p = null;
		
		Token label = null;
		Token assignId = null;
		Token args = null;
		int autoGen = GrammarElement.AUTO_GEN_NONE;
		
		
		switch ( LA(1)) {
		case ACTION:
		{
			a = LT(1);
			match(ACTION);
			if ( inputState.guessing==0 ) {
				behavior.refAction(a);
			}
			break;
		}
		case SEMPRED:
		{
			p = LT(1);
			match(SEMPRED);
			if ( inputState.guessing==0 ) {
				behavior.refSemPred(p);
			}
			break;
		}
		case TREE_BEGIN:
		{
			tree();
			break;
		}
		default:
			if ((LA(1)==TOKEN_REF||LA(1)==RULE_REF) && (LA(2)==ASSIGN)) {
				assignId=id();
				match(ASSIGN);
				{
				if ((LA(1)==TOKEN_REF||LA(1)==RULE_REF) && (LA(2)==COLON)) {
					label=id();
					match(COLON);
					if ( inputState.guessing==0 ) {
						checkForMissingEndRule(label);
					}
				}
				else if ((LA(1)==TOKEN_REF||LA(1)==RULE_REF) && (_tokenSet_3.member(LA(2)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				{
				switch ( LA(1)) {
				case RULE_REF:
				{
					rr = LT(1);
					match(RULE_REF);
					if ( inputState.guessing==0 ) {
						behavior.fixRuleName(rr);
					}
					{
					switch ( LA(1)) {
					case ARG_ACTION:
					{
						aa = LT(1);
						match(ARG_ACTION);
						if ( inputState.guessing==0 ) {
							args=aa;
						}
						break;
					}
					case STRING_LITERAL:
					case ACTION:
					case SEMI:
					case CHAR_LITERAL:
					case OR:
					case TOKEN_REF:
					case OPEN_ELEMENT_OPTION:
					case LPAREN:
					case RPAREN:
					case BANG:
					case LITERAL_exception:
					case RULE_REF:
					case NOT_OP:
					case SEMPRED:
					case TREE_BEGIN:
					case WILDCARD:
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
					case BANG:
					{
						match(BANG);
						if ( inputState.guessing==0 ) {
							autoGen = GrammarElement.AUTO_GEN_BANG;
						}
						break;
					}
					case STRING_LITERAL:
					case ACTION:
					case SEMI:
					case CHAR_LITERAL:
					case OR:
					case TOKEN_REF:
					case OPEN_ELEMENT_OPTION:
					case LPAREN:
					case RPAREN:
					case LITERAL_exception:
					case RULE_REF:
					case NOT_OP:
					case SEMPRED:
					case TREE_BEGIN:
					case WILDCARD:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						behavior.refRule(assignId, rr, label, args, autoGen);
					}
					break;
				}
				case TOKEN_REF:
				{
					tr = LT(1);
					match(TOKEN_REF);
					{
					switch ( LA(1)) {
					case ARG_ACTION:
					{
						aa2 = LT(1);
						match(ARG_ACTION);
						if ( inputState.guessing==0 ) {
							args=aa2;
						}
						break;
					}
					case STRING_LITERAL:
					case ACTION:
					case SEMI:
					case CHAR_LITERAL:
					case OR:
					case TOKEN_REF:
					case OPEN_ELEMENT_OPTION:
					case LPAREN:
					case RPAREN:
					case LITERAL_exception:
					case RULE_REF:
					case NOT_OP:
					case SEMPRED:
					case TREE_BEGIN:
					case WILDCARD:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						behavior.refToken(assignId, tr, label, args, false, autoGen, lastInRule());
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else if ((_tokenSet_4.member(LA(1))) && (_tokenSet_5.member(LA(2)))) {
				{
				if ((LA(1)==TOKEN_REF||LA(1)==RULE_REF) && (LA(2)==COLON)) {
					label=id();
					match(COLON);
					if ( inputState.guessing==0 ) {
						checkForMissingEndRule(label);
					}
				}
				else if ((_tokenSet_4.member(LA(1))) && (_tokenSet_6.member(LA(2)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				{
				switch ( LA(1)) {
				case RULE_REF:
				{
					r2 = LT(1);
					match(RULE_REF);
					if ( inputState.guessing==0 ) {
						behavior.fixRuleName(r2);
					}
					{
					switch ( LA(1)) {
					case ARG_ACTION:
					{
						aa3 = LT(1);
						match(ARG_ACTION);
						if ( inputState.guessing==0 ) {
							args=aa3;
						}
						break;
					}
					case STRING_LITERAL:
					case ACTION:
					case SEMI:
					case CHAR_LITERAL:
					case OR:
					case TOKEN_REF:
					case OPEN_ELEMENT_OPTION:
					case LPAREN:
					case RPAREN:
					case BANG:
					case LITERAL_exception:
					case RULE_REF:
					case NOT_OP:
					case SEMPRED:
					case TREE_BEGIN:
					case WILDCARD:
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
					case BANG:
					{
						match(BANG);
						if ( inputState.guessing==0 ) {
							autoGen = GrammarElement.AUTO_GEN_BANG;
						}
						break;
					}
					case STRING_LITERAL:
					case ACTION:
					case SEMI:
					case CHAR_LITERAL:
					case OR:
					case TOKEN_REF:
					case OPEN_ELEMENT_OPTION:
					case LPAREN:
					case RPAREN:
					case LITERAL_exception:
					case RULE_REF:
					case NOT_OP:
					case SEMPRED:
					case TREE_BEGIN:
					case WILDCARD:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						behavior.refRule(assignId, r2, label, args, autoGen);
					}
					break;
				}
				case NOT_OP:
				{
					match(NOT_OP);
					{
					switch ( LA(1)) {
					case CHAR_LITERAL:
					case TOKEN_REF:
					{
						notTerminal(label);
						break;
					}
					case LPAREN:
					{
						ebnf(label,true);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					break;
				}
				case LPAREN:
				{
					ebnf(label,false);
					break;
				}
				default:
					if ((LA(1)==STRING_LITERAL||LA(1)==CHAR_LITERAL||LA(1)==TOKEN_REF) && (LA(2)==RANGE)) {
						range(label);
					}
					else if ((_tokenSet_7.member(LA(1))) && (_tokenSet_8.member(LA(2)))) {
						terminal(label);
					}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}
	
	public final void elementOptionSpec() throws RecognitionException, TokenStreamException {
		
		
		Token o=null, v=null;
		
		
		match(OPEN_ELEMENT_OPTION);
		o=id();
		match(ASSIGN);
		v=optionValue();
		if ( inputState.guessing==0 ) {
			behavior.refElementOption(o,v);
		}
		{
		_loop295:
		do {
			if ((LA(1)==SEMI)) {
				match(SEMI);
				o=id();
				match(ASSIGN);
				v=optionValue();
				if ( inputState.guessing==0 ) {
					behavior.refElementOption(o,v);
				}
			}
			else {
				break _loop295;
			}
			
		} while (true);
		}
		match(CLOSE_ELEMENT_OPTION);
	}
	
	public final void range(
		 Token label 
	) throws RecognitionException, TokenStreamException {
		
		Token  crLeft = null;
		Token  crRight = null;
		Token  t = null;
		Token  u = null;
		Token  v = null;
		Token  w = null;
		
		Token trLeft=null;
		Token trRight=null;
		int autoGen=GrammarElement.AUTO_GEN_NONE;
		
		
		switch ( LA(1)) {
		case CHAR_LITERAL:
		{
			crLeft = LT(1);
			match(CHAR_LITERAL);
			match(RANGE);
			crRight = LT(1);
			match(CHAR_LITERAL);
			{
			switch ( LA(1)) {
			case BANG:
			{
				match(BANG);
				if ( inputState.guessing==0 ) {
					autoGen = GrammarElement.AUTO_GEN_BANG;
				}
				break;
			}
			case STRING_LITERAL:
			case ACTION:
			case SEMI:
			case CHAR_LITERAL:
			case OR:
			case TOKEN_REF:
			case OPEN_ELEMENT_OPTION:
			case LPAREN:
			case RPAREN:
			case LITERAL_exception:
			case RULE_REF:
			case NOT_OP:
			case SEMPRED:
			case TREE_BEGIN:
			case WILDCARD:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				behavior.refCharRange(crLeft, crRight, label, autoGen, lastInRule());
			}
			break;
		}
		case STRING_LITERAL:
		case TOKEN_REF:
		{
			{
			switch ( LA(1)) {
			case TOKEN_REF:
			{
				t = LT(1);
				match(TOKEN_REF);
				if ( inputState.guessing==0 ) {
					trLeft=t;
				}
				break;
			}
			case STRING_LITERAL:
			{
				u = LT(1);
				match(STRING_LITERAL);
				if ( inputState.guessing==0 ) {
					trLeft=u;
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RANGE);
			{
			switch ( LA(1)) {
			case TOKEN_REF:
			{
				v = LT(1);
				match(TOKEN_REF);
				if ( inputState.guessing==0 ) {
					trRight=v;
				}
				break;
			}
			case STRING_LITERAL:
			{
				w = LT(1);
				match(STRING_LITERAL);
				if ( inputState.guessing==0 ) {
					trRight=w;
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			autoGen=ast_type_spec();
			if ( inputState.guessing==0 ) {
				behavior.refTokenRange(trLeft, trRight, label, autoGen, lastInRule());
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}
	
	public final void terminal(
		 Token label 
	) throws RecognitionException, TokenStreamException {
		
		Token  cl = null;
		Token  tr = null;
		Token  aa = null;
		Token  sl = null;
		Token  wi = null;
		
		int autoGen=GrammarElement.AUTO_GEN_NONE;
		Token args=null;
		
		
		switch ( LA(1)) {
		case CHAR_LITERAL:
		{
			cl = LT(1);
			match(CHAR_LITERAL);
			{
			switch ( LA(1)) {
			case BANG:
			{
				match(BANG);
				if ( inputState.guessing==0 ) {
					autoGen = GrammarElement.AUTO_GEN_BANG;
				}
				break;
			}
			case STRING_LITERAL:
			case ACTION:
			case SEMI:
			case CHAR_LITERAL:
			case OR:
			case TOKEN_REF:
			case OPEN_ELEMENT_OPTION:
			case LPAREN:
			case RPAREN:
			case LITERAL_exception:
			case RULE_REF:
			case NOT_OP:
			case SEMPRED:
			case TREE_BEGIN:
			case WILDCARD:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				behavior.refCharLiteral(cl, label, false, autoGen, lastInRule());
			}
			break;
		}
		case TOKEN_REF:
		{
			tr = LT(1);
			match(TOKEN_REF);
			autoGen=ast_type_spec();
			{
			switch ( LA(1)) {
			case ARG_ACTION:
			{
				aa = LT(1);
				match(ARG_ACTION);
				if ( inputState.guessing==0 ) {
					args=aa;
				}
				break;
			}
			case STRING_LITERAL:
			case ACTION:
			case SEMI:
			case CHAR_LITERAL:
			case OR:
			case TOKEN_REF:
			case OPEN_ELEMENT_OPTION:
			case LPAREN:
			case RPAREN:
			case LITERAL_exception:
			case RULE_REF:
			case NOT_OP:
			case SEMPRED:
			case TREE_BEGIN:
			case WILDCARD:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				behavior.refToken(null, tr, label, args, false, autoGen, lastInRule());
			}
			break;
		}
		case STRING_LITERAL:
		{
			sl = LT(1);
			match(STRING_LITERAL);
			autoGen=ast_type_spec();
			if ( inputState.guessing==0 ) {
				behavior.refStringLiteral(sl, label, autoGen, lastInRule());
			}
			break;
		}
		case WILDCARD:
		{
			wi = LT(1);
			match(WILDCARD);
			autoGen=ast_type_spec();
			if ( inputState.guessing==0 ) {
				behavior.refWildcard(wi, label, autoGen);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}
	
	public final void notTerminal(
		 Token label 
	) throws RecognitionException, TokenStreamException {
		
		Token  cl = null;
		Token  tr = null;
		int autoGen=GrammarElement.AUTO_GEN_NONE;
		
		switch ( LA(1)) {
		case CHAR_LITERAL:
		{
			cl = LT(1);
			match(CHAR_LITERAL);
			{
			switch ( LA(1)) {
			case BANG:
			{
				match(BANG);
				if ( inputState.guessing==0 ) {
					autoGen = GrammarElement.AUTO_GEN_BANG;
				}
				break;
			}
			case STRING_LITERAL:
			case ACTION:
			case SEMI:
			case CHAR_LITERAL:
			case OR:
			case TOKEN_REF:
			case OPEN_ELEMENT_OPTION:
			case LPAREN:
			case RPAREN:
			case LITERAL_exception:
			case RULE_REF:
			case NOT_OP:
			case SEMPRED:
			case TREE_BEGIN:
			case WILDCARD:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				behavior.refCharLiteral(cl, label, true, autoGen, lastInRule());
			}
			break;
		}
		case TOKEN_REF:
		{
			tr = LT(1);
			match(TOKEN_REF);
			autoGen=ast_type_spec();
			if ( inputState.guessing==0 ) {
				behavior.refToken(null, tr, label, null, true, autoGen, lastInRule());
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}
	
	public final void ebnf(
		 Token label, boolean not 
	) throws RecognitionException, TokenStreamException {
		
		Token  lp = null;
		Token  aa = null;
		Token  ab = null;
		
		lp = LT(1);
		match(LPAREN);
		if ( inputState.guessing==0 ) {
			behavior.beginSubRule(label, lp, not);
		}
		{
		if ((LA(1)==OPTIONS)) {
			subruleOptionsSpec();
			{
			switch ( LA(1)) {
			case ACTION:
			{
				aa = LT(1);
				match(ACTION);
				if ( inputState.guessing==0 ) {
					behavior.refInitAction(aa);
				}
				break;
			}
			case COLON:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(COLON);
		}
		else if ((LA(1)==ACTION) && (LA(2)==COLON)) {
			ab = LT(1);
			match(ACTION);
			if ( inputState.guessing==0 ) {
				behavior.refInitAction(ab);
			}
			match(COLON);
		}
		else if ((_tokenSet_9.member(LA(1))) && (_tokenSet_10.member(LA(2)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		block();
		match(RPAREN);
		{
		switch ( LA(1)) {
		case STRING_LITERAL:
		case ACTION:
		case SEMI:
		case CHAR_LITERAL:
		case OR:
		case TOKEN_REF:
		case OPEN_ELEMENT_OPTION:
		case LPAREN:
		case RPAREN:
		case BANG:
		case LITERAL_exception:
		case RULE_REF:
		case NOT_OP:
		case SEMPRED:
		case TREE_BEGIN:
		case QUESTION:
		case STAR:
		case PLUS:
		case WILDCARD:
		{
			{
			switch ( LA(1)) {
			case QUESTION:
			{
				match(QUESTION);
				if ( inputState.guessing==0 ) {
					behavior.optionalSubRule();
				}
				break;
			}
			case STAR:
			{
				match(STAR);
				if ( inputState.guessing==0 ) {
					behavior.zeroOrMoreSubRule();
				}
				break;
			}
			case PLUS:
			{
				match(PLUS);
				if ( inputState.guessing==0 ) {
					behavior.oneOrMoreSubRule();
				}
				break;
			}
			case STRING_LITERAL:
			case ACTION:
			case SEMI:
			case CHAR_LITERAL:
			case OR:
			case TOKEN_REF:
			case OPEN_ELEMENT_OPTION:
			case LPAREN:
			case RPAREN:
			case BANG:
			case LITERAL_exception:
			case RULE_REF:
			case NOT_OP:
			case SEMPRED:
			case TREE_BEGIN:
			case WILDCARD:
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
			case BANG:
			{
				match(BANG);
				if ( inputState.guessing==0 ) {
					behavior.noASTSubRule();
				}
				break;
			}
			case STRING_LITERAL:
			case ACTION:
			case SEMI:
			case CHAR_LITERAL:
			case OR:
			case TOKEN_REF:
			case OPEN_ELEMENT_OPTION:
			case LPAREN:
			case RPAREN:
			case LITERAL_exception:
			case RULE_REF:
			case NOT_OP:
			case SEMPRED:
			case TREE_BEGIN:
			case WILDCARD:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			break;
		}
		case IMPLIES:
		{
			match(IMPLIES);
			if ( inputState.guessing==0 ) {
				behavior.synPred();
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			behavior.endSubRule();
		}
	}
	
	public final void tree() throws RecognitionException, TokenStreamException {
		
		Token  lp = null;
		
		lp = LT(1);
		match(TREE_BEGIN);
		if ( inputState.guessing==0 ) {
			behavior.beginTree(lp);
		}
		rootNode();
		if ( inputState.guessing==0 ) {
			behavior.beginChildList();
		}
		{
		int _cnt309=0;
		_loop309:
		do {
			if ((_tokenSet_2.member(LA(1)))) {
				element();
			}
			else {
				if ( _cnt309>=1 ) { break _loop309; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt309++;
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			behavior.endChildList();
		}
		match(RPAREN);
		if ( inputState.guessing==0 ) {
			behavior.endTree();
		}
	}
	
	public final void rootNode() throws RecognitionException, TokenStreamException {
		
		Token label = null;
		
		{
		if ((LA(1)==TOKEN_REF||LA(1)==RULE_REF) && (LA(2)==COLON)) {
			label=id();
			match(COLON);
			if ( inputState.guessing==0 ) {
				checkForMissingEndRule(label);
			}
		}
		else if ((_tokenSet_7.member(LA(1))) && (_tokenSet_11.member(LA(2)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		terminal(label);
	}
	
	public final  int  ast_type_spec() throws RecognitionException, TokenStreamException {
		 int autoGen ;
		
		autoGen = GrammarElement.AUTO_GEN_NONE;
		
		{
		switch ( LA(1)) {
		case CARET:
		{
			match(CARET);
			if ( inputState.guessing==0 ) {
				autoGen = GrammarElement.AUTO_GEN_CARET;
			}
			break;
		}
		case BANG:
		{
			match(BANG);
			if ( inputState.guessing==0 ) {
				autoGen = GrammarElement.AUTO_GEN_BANG;
			}
			break;
		}
		case STRING_LITERAL:
		case ACTION:
		case SEMI:
		case CHAR_LITERAL:
		case OR:
		case TOKEN_REF:
		case OPEN_ELEMENT_OPTION:
		case LPAREN:
		case RPAREN:
		case ARG_ACTION:
		case LITERAL_exception:
		case RULE_REF:
		case NOT_OP:
		case SEMPRED:
		case TREE_BEGIN:
		case WILDCARD:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		return autoGen ;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"tokens\"",
		"\"header\"",
		"STRING_LITERAL",
		"ACTION",
		"DOC_COMMENT",
		"\"lexclass\"",
		"\"class\"",
		"\"extends\"",
		"\"Lexer\"",
		"\"TreeParser\"",
		"OPTIONS",
		"ASSIGN",
		"SEMI",
		"RCURLY",
		"COLON",
		"DASH",
		"\"charVocabulary\"",
		"CHAR_LITERAL",
		"INT",
		"OR",
		"RANGE",
		"TOKENS",
		"TOKEN_REF",
		"OPEN_ELEMENT_OPTION",
		"CLOSE_ELEMENT_OPTION",
		"LPAREN",
		"RPAREN",
		"\"Parser\"",
		"\"protected\"",
		"\"public\"",
		"\"private\"",
		"BANG",
		"ARG_ACTION",
		"\"returns\"",
		"\"throws\"",
		"COMMA",
		"\"exception\"",
		"\"catch\"",
		"RULE_REF",
		"NOT_OP",
		"SEMPRED",
		"TREE_BEGIN",
		"QUESTION",
		"STAR",
		"PLUS",
		"IMPLIES",
		"CARET",
		"WILDCARD",
		"\"options\"",
		"WS",
		"COMMENT",
		"SL_COMMENT",
		"ML_COMMENT",
		"ESC",
		"DIGIT",
		"XDIGIT",
		"NESTED_ARG_ACTION",
		"NESTED_ACTION",
		"WS_LOOP",
		"INTERNAL_RULE_REF",
		"WS_OPT"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 4428178391296L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 4943574745216L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 2317771117428928L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 2318974924685504L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 2264994559295552L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 3444874848583872L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 3444874848321728L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 2251799882891328L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 3444874831528128L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 2318906070925504L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 4502605034537152L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 3443774103486656L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	
	}
