// $ANTXR : "preproc.antxr" -> "PreprocessorLexer.java"$
// GENERATED CODE - DO NOT EDIT!

package com.javadude.antxr.preprocessor;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.InputStream;
import com.javadude.antxr.TokenStreamException;
import com.javadude.antxr.TokenStreamIOException;
import com.javadude.antxr.TokenStreamRecognitionException;
import com.javadude.antxr.CharStreamException;
import com.javadude.antxr.CharStreamIOException;
import com.javadude.antxr.ANTXRException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import com.javadude.antxr.CharScanner;
import com.javadude.antxr.InputBuffer;
import com.javadude.antxr.ByteBuffer;
import com.javadude.antxr.CharBuffer;
import com.javadude.antxr.Token;
import com.javadude.antxr.CommonToken;
import com.javadude.antxr.RecognitionException;
import com.javadude.antxr.NoViableAltForCharException;
import com.javadude.antxr.MismatchedCharException;
import com.javadude.antxr.TokenStream;
import com.javadude.antxr.ANTXRHashString;
import com.javadude.antxr.LexerSharedInputState;
import com.javadude.antxr.collections.impl.BitSet;
import com.javadude.antxr.SemanticException;

@SuppressWarnings("all")
public class PreprocessorLexer extends com.javadude.antxr.CharScanner implements PreprocessorTokenTypes, TokenStream
 {
public PreprocessorLexer(InputStream in) {
	this(new ByteBuffer(in));
}
public PreprocessorLexer(Reader in) {
	this(new CharBuffer(in));
}
public PreprocessorLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
}
public PreprocessorLexer(LexerSharedInputState state) {
	super(state);
	caseSensitiveLiterals = true;
	setCaseSensitive(true);
	literals = new HashMap<ANTXRHashString, Integer>();
	literals.put(new ANTXRHashString("extends", this), new Integer(10));
	literals.put(new ANTXRHashString("public", this), new Integer(18));
	literals.put(new ANTXRHashString("private", this), new Integer(17));
	literals.put(new ANTXRHashString("exception", this), new Integer(25));
	literals.put(new ANTXRHashString("tokens", this), new Integer(4));
	literals.put(new ANTXRHashString("protected", this), new Integer(16));
	literals.put(new ANTXRHashString("returns", this), new Integer(21));
	literals.put(new ANTXRHashString("class", this), new Integer(8));
	literals.put(new ANTXRHashString("catch", this), new Integer(26));
	literals.put(new ANTXRHashString("throws", this), new Integer(23));
}

public Token nextToken() throws TokenStreamException {
	Token theRetToken=null;
tryAgain:
	for (;;) {
		Token _token = null;
		int _ttype = Token.INVALID_TYPE;
		resetText();
		try {   // for char stream error handling
			try {   // for lexical error handling
				switch ( LA(1)) {
				case ':':
				{
					mRULE_BLOCK(true);
					theRetToken=_returnToken;
					break;
				}
				case '\t':  case '\n':  case '\r':  case ' ':
				{
					mWS(true);
					theRetToken=_returnToken;
					break;
				}
				case '/':
				{
					mCOMMENT(true);
					theRetToken=_returnToken;
					break;
				}
				case '{':
				{
					mACTION(true);
					theRetToken=_returnToken;
					break;
				}
				case '"':
				{
					mSTRING_LITERAL(true);
					theRetToken=_returnToken;
					break;
				}
				case '\'':
				{
					mCHAR_LITERAL(true);
					theRetToken=_returnToken;
					break;
				}
				case '!':
				{
					mBANG(true);
					theRetToken=_returnToken;
					break;
				}
				case ';':
				{
					mSEMI(true);
					theRetToken=_returnToken;
					break;
				}
				case ',':
				{
					mCOMMA(true);
					theRetToken=_returnToken;
					break;
				}
				case '}':
				{
					mRCURLY(true);
					theRetToken=_returnToken;
					break;
				}
				case ')':
				{
					mRPAREN(true);
					theRetToken=_returnToken;
					break;
				}
				case '-':  case '<':  case 'A':  case 'B':
				case 'C':  case 'D':  case 'E':  case 'F':
				case 'G':  case 'H':  case 'I':  case 'J':
				case 'K':  case 'L':  case 'M':  case 'N':
				case 'O':  case 'P':  case 'Q':  case 'R':
				case 'S':  case 'T':  case 'U':  case 'V':
				case 'W':  case 'X':  case 'Y':  case 'Z':
				case '_':  case 'a':  case 'b':  case 'c':
				case 'd':  case 'e':  case 'f':  case 'g':
				case 'h':  case 'i':  case 'j':  case 'k':
				case 'l':  case 'm':  case 'n':  case 'o':
				case 'p':  case 'q':  case 'r':  case 's':
				case 't':  case 'u':  case 'v':  case 'w':
				case 'x':  case 'y':  case 'z':
				{
					mID_OR_KEYWORD(true);
					theRetToken=_returnToken;
					break;
				}
				case '=':
				{
					mASSIGN_RHS(true);
					theRetToken=_returnToken;
					break;
				}
				case '[':
				{
					mARG_ACTION(true);
					theRetToken=_returnToken;
					break;
				}
				default:
					if ((LA(1)=='(') && (_tokenSet_0.member(LA(2)))) {
						mSUBRULE_BLOCK(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='(') && (true)) {
						mLPAREN(true);
						theRetToken=_returnToken;
					}
				else {
					if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}
				else {throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());}
				}
				}
				if ( _returnToken==null ) continue tryAgain; // found SKIP token
				_ttype = _returnToken.getType();
				_ttype = testLiteralsTable(_ttype);
				_returnToken.setType(_ttype);
				return _returnToken;
			}
			catch (RecognitionException e) {
				throw new TokenStreamRecognitionException(e);
			}
		}
		catch (CharStreamException cse) {
			if ( cse instanceof CharStreamIOException ) {
				throw new TokenStreamIOException(((CharStreamIOException)cse).io);
			}
			else {
				throw new TokenStreamException(cse.getMessage());
			}
		}
	}
}

	public final void mRULE_BLOCK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RULE_BLOCK;
		int _saveIndex;
		
		match(':');
		{
		if ((_tokenSet_1.member(LA(1))) && (_tokenSet_2.member(LA(2)))) {
			_saveIndex=text.length();
			mWS(false);
			text.setLength(_saveIndex);
		}
		else if ((_tokenSet_2.member(LA(1))) && (true)) {
		}
		else {
			throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
		}
		
		}
		mALT(false);
		{
		switch ( LA(1)) {
		case '\t':  case '\n':  case '\r':  case ' ':
		{
			_saveIndex=text.length();
			mWS(false);
			text.setLength(_saveIndex);
			break;
		}
		case ';':  case '|':
		{
			break;
		}
		default:
		{
			throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		{
		_loop102:
		do {
			if ((LA(1)=='|')) {
				match('|');
				{
				if ((_tokenSet_1.member(LA(1))) && (_tokenSet_2.member(LA(2)))) {
					_saveIndex=text.length();
					mWS(false);
					text.setLength(_saveIndex);
				}
				else if ((_tokenSet_2.member(LA(1))) && (true)) {
				}
				else {
					throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
				}
				
				}
				mALT(false);
				{
				switch ( LA(1)) {
				case '\t':  case '\n':  case '\r':  case ' ':
				{
					_saveIndex=text.length();
					mWS(false);
					text.setLength(_saveIndex);
					break;
				}
				case ';':  case '|':
				{
					break;
				}
				default:
				{
					throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
				}
				}
				}
			}
			else {
				break _loop102;
			}
			
		} while (true);
		}
		match(';');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WS;
		int _saveIndex;
		
		{
		int _cnt152=0;
		_loop152:
		do {
			if ((LA(1)==' ') && (true)) {
				match(' ');
			}
			else if ((LA(1)=='\t') && (true)) {
				match('\t');
			}
			else if ((LA(1)=='\n'||LA(1)=='\r') && (true)) {
				mNEWLINE(false);
			}
			else {
				if ( _cnt152>=1 ) { break _loop152; } else {throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt152++;
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			_ttype = Token.SKIP;
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mALT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ALT;
		int _saveIndex;
		
		{
		_loop113:
		do {
			if ((_tokenSet_3.member(LA(1))) && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				mELEMENT(false);
			}
			else {
				break _loop113;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSUBRULE_BLOCK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SUBRULE_BLOCK;
		int _saveIndex;
		
		match('(');
		{
		if ((_tokenSet_1.member(LA(1))) && (_tokenSet_0.member(LA(2)))) {
			mWS(false);
		}
		else if ((_tokenSet_0.member(LA(1))) && (true)) {
		}
		else {
			throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
		}
		
		}
		mALT(false);
		{
		_loop108:
		do {
			if ((_tokenSet_4.member(LA(1))) && (_tokenSet_0.member(LA(2)))) {
				{
				switch ( LA(1)) {
				case '\t':  case '\n':  case '\r':  case ' ':
				{
					mWS(false);
					break;
				}
				case '|':
				{
					break;
				}
				default:
				{
					throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
				}
				}
				}
				match('|');
				{
				if ((_tokenSet_1.member(LA(1))) && (_tokenSet_0.member(LA(2)))) {
					mWS(false);
				}
				else if ((_tokenSet_0.member(LA(1))) && (true)) {
				}
				else {
					throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
				}
				
				}
				mALT(false);
			}
			else {
				break _loop108;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case '\t':  case '\n':  case '\r':  case ' ':
		{
			mWS(false);
			break;
		}
		case ')':
		{
			break;
		}
		default:
		{
			throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		match(')');
		{
		if ((LA(1)=='=') && (LA(2)=='>')) {
			match("=>");
		}
		else if ((LA(1)=='*') && (true)) {
			match('*');
		}
		else if ((LA(1)=='+') && (true)) {
			match('+');
		}
		else if ((LA(1)=='?') && (true)) {
			match('?');
		}
		else {
		}
		
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mELEMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ELEMENT;
		int _saveIndex;
		
		switch ( LA(1)) {
		case '/':
		{
			mCOMMENT(false);
			break;
		}
		case '{':
		{
			mACTION(false);
			break;
		}
		case '"':
		{
			mSTRING_LITERAL(false);
			break;
		}
		case '\'':
		{
			mCHAR_LITERAL(false);
			break;
		}
		case '(':
		{
			mSUBRULE_BLOCK(false);
			break;
		}
		case '\n':  case '\r':
		{
			mNEWLINE(false);
			break;
		}
		default:
			if ((_tokenSet_5.member(LA(1)))) {
				{
				match(_tokenSet_5);
				}
			}
		else {
			throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COMMENT;
		int _saveIndex;
		
		{
		if ((LA(1)=='/') && (LA(2)=='/')) {
			mSL_COMMENT(false);
		}
		else if ((LA(1)=='/') && (LA(2)=='*')) {
			mML_COMMENT(false);
		}
		else {
			throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
		}
		
		}
		if ( inputState.guessing==0 ) {
			_ttype = Token.SKIP;
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mACTION(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ACTION;
		int _saveIndex;
		
		match('{');
		{
		_loop183:
		do {
			// nongreedy exit test
			if ((LA(1)=='}') && (true)) break _loop183;
			if ((LA(1)=='\n'||LA(1)=='\r') && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				mNEWLINE(false);
			}
			else if ((LA(1)=='{') && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				mACTION(false);
			}
			else if ((LA(1)=='\'') && (_tokenSet_6.member(LA(2)))) {
				mCHAR_LITERAL(false);
			}
			else if ((LA(1)=='/') && (LA(2)=='*'||LA(2)=='/')) {
				mCOMMENT(false);
			}
			else if ((LA(1)=='"') && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				mSTRING_LITERAL(false);
			}
			else if (((LA(1) >= '\u0003' && LA(1) <= '\u00ff')) && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				matchNot(EOF_CHAR);
			}
			else {
				break _loop183;
			}
			
		} while (true);
		}
		match('}');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTRING_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRING_LITERAL;
		int _saveIndex;
		
		match('"');
		{
		_loop168:
		do {
			if ((LA(1)=='\\')) {
				mESC(false);
			}
			else if ((_tokenSet_7.member(LA(1)))) {
				matchNot('"');
			}
			else {
				break _loop168;
			}
			
		} while (true);
		}
		match('"');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCHAR_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CHAR_LITERAL;
		int _saveIndex;
		
		match('\'');
		{
		if ((LA(1)=='\\')) {
			mESC(false);
		}
		else if ((_tokenSet_8.member(LA(1)))) {
			matchNot('\'');
		}
		else {
			throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
		}
		
		}
		match('\'');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mNEWLINE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NEWLINE;
		int _saveIndex;
		
		{
		if ((LA(1)=='\r') && (LA(2)=='\n')) {
			match('\r');
			match('\n');
			if ( inputState.guessing==0 ) {
				newline();
			}
		}
		else if ((LA(1)=='\r') && (true)) {
			match('\r');
			if ( inputState.guessing==0 ) {
				newline();
			}
		}
		else if ((LA(1)=='\n')) {
			match('\n');
			if ( inputState.guessing==0 ) {
				newline();
			}
		}
		else {
			throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
		}
		
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBANG(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BANG;
		int _saveIndex;
		
		match('!');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSEMI(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SEMI;
		int _saveIndex;
		
		match(';');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOMMA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COMMA;
		int _saveIndex;
		
		match(',');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRCURLY(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RCURLY;
		int _saveIndex;
		
		match('}');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LPAREN;
		int _saveIndex;
		
		match('(');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RPAREN;
		int _saveIndex;
		
		match(')');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
/** This rule picks off keywords in the lexer that need to be
 *  handled specially.  For example, "header" is the start
 *  of the header action (used to distinguish between options
 *  block and an action).  We do not want "header" to go back
 *  to the parser as a simple keyword...it must pick off
 *  the action afterwards.
 */
	public final void mID_OR_KEYWORD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ID_OR_KEYWORD;
		int _saveIndex;
		Token id=null;
		
		mID(true);
		id=_returnToken;
		if ( inputState.guessing==0 ) {
			_ttype = id.getType();
		}
		{
		if (((_tokenSet_9.member(LA(1))) && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff')))&&(id.getText().equals("header"))) {
			{
			if ((_tokenSet_1.member(LA(1))) && (_tokenSet_9.member(LA(2)))) {
				mWS(false);
			}
			else if ((_tokenSet_9.member(LA(1))) && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
			}
			else {
				throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			{
			switch ( LA(1)) {
			case '"':
			{
				mSTRING_LITERAL(false);
				break;
			}
			case '\t':  case '\n':  case '\r':  case ' ':
			case '/':  case '{':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			{
			_loop127:
			do {
				switch ( LA(1)) {
				case '\t':  case '\n':  case '\r':  case ' ':
				{
					mWS(false);
					break;
				}
				case '/':
				{
					mCOMMENT(false);
					break;
				}
				default:
				{
					break _loop127;
				}
				}
			} while (true);
			}
			mACTION(false);
			if ( inputState.guessing==0 ) {
				_ttype = HEADER_ACTION;
			}
		}
		else if (((_tokenSet_10.member(LA(1))) && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff')))&&(id.getText().equals("tokens"))) {
			{
			_loop129:
			do {
				switch ( LA(1)) {
				case '\t':  case '\n':  case '\r':  case ' ':
				{
					mWS(false);
					break;
				}
				case '/':
				{
					mCOMMENT(false);
					break;
				}
				default:
				{
					break _loop129;
				}
				}
			} while (true);
			}
			mCURLY_BLOCK_SCARF(false);
			if ( inputState.guessing==0 ) {
				_ttype = TOKENS_SPEC;
			}
		}
		else if (((_tokenSet_10.member(LA(1))) && (true))&&(id.getText().equals("options"))) {
			{
			_loop131:
			do {
				switch ( LA(1)) {
				case '\t':  case '\n':  case '\r':  case ' ':
				{
					mWS(false);
					break;
				}
				case '/':
				{
					mCOMMENT(false);
					break;
				}
				default:
				{
					break _loop131;
				}
				}
			} while (true);
			}
			match('{');
			if ( inputState.guessing==0 ) {
				_ttype = OPTIONS_START;
			}
		}
		else {
		}
		
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mID(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ID;
		int _saveIndex;
		
		{
		{
		switch ( LA(1)) {
		case '<':
		{
			match('<');
			{
			switch ( LA(1)) {
			case 'a':  case 'b':  case 'c':  case 'd':
			case 'e':  case 'f':  case 'g':  case 'h':
			case 'i':  case 'j':  case 'k':  case 'l':
			case 'm':  case 'n':  case 'o':  case 'p':
			case 'q':  case 'r':  case 's':  case 't':
			case 'u':  case 'v':  case 'w':  case 'x':
			case 'y':  case 'z':
			{
				matchRange('a','z');
				break;
			}
			case 'A':  case 'B':  case 'C':  case 'D':
			case 'E':  case 'F':  case 'G':  case 'H':
			case 'I':  case 'J':  case 'K':  case 'L':
			case 'M':  case 'N':  case 'O':  case 'P':
			case 'Q':  case 'R':  case 'S':  case 'T':
			case 'U':  case 'V':  case 'W':  case 'X':
			case 'Y':  case 'Z':
			{
				matchRange('A','Z');
				break;
			}
			case '_':
			{
				match('_');
				break;
			}
			default:
			{
				throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			{
			_loop140:
			do {
				switch ( LA(1)) {
				case 'a':  case 'b':  case 'c':  case 'd':
				case 'e':  case 'f':  case 'g':  case 'h':
				case 'i':  case 'j':  case 'k':  case 'l':
				case 'm':  case 'n':  case 'o':  case 'p':
				case 'q':  case 'r':  case 's':  case 't':
				case 'u':  case 'v':  case 'w':  case 'x':
				case 'y':  case 'z':
				{
					matchRange('a','z');
					break;
				}
				case 'A':  case 'B':  case 'C':  case 'D':
				case 'E':  case 'F':  case 'G':  case 'H':
				case 'I':  case 'J':  case 'K':  case 'L':
				case 'M':  case 'N':  case 'O':  case 'P':
				case 'Q':  case 'R':  case 'S':  case 'T':
				case 'U':  case 'V':  case 'W':  case 'X':
				case 'Y':  case 'Z':
				{
					matchRange('A','Z');
					break;
				}
				case '_':
				{
					match('_');
					break;
				}
				case ':':
				{
					match(':');
					break;
				}
				case '-':
				{
					match('-');
					break;
				}
				case '0':  case '1':  case '2':  case '3':
				case '4':  case '5':  case '6':  case '7':
				case '8':  case '9':
				{
					matchRange('0','9');
					break;
				}
				default:
				{
					break _loop140;
				}
				}
			} while (true);
			}
			match('>');
			break;
		}
		case '-':  case 'A':  case 'B':  case 'C':
		case 'D':  case 'E':  case 'F':  case 'G':
		case 'H':  case 'I':  case 'J':  case 'K':
		case 'L':  case 'M':  case 'N':  case 'O':
		case 'P':  case 'Q':  case 'R':  case 'S':
		case 'T':  case 'U':  case 'V':  case 'W':
		case 'X':  case 'Y':  case 'Z':  case '_':
		case 'a':  case 'b':  case 'c':  case 'd':
		case 'e':  case 'f':  case 'g':  case 'h':
		case 'i':  case 'j':  case 'k':  case 'l':
		case 'm':  case 'n':  case 'o':  case 'p':
		case 'q':  case 'r':  case 's':  case 't':
		case 'u':  case 'v':  case 'w':  case 'x':
		case 'y':  case 'z':
		{
			{
			boolean synPredMatched143 = false;
			if (((LA(1)=='x') && (LA(2)=='m'))) {
				int _m143 = mark();
				synPredMatched143 = true;
				inputState.guessing++;
				try {
					{
					match("xmlns:");
					}
				}
				catch (RecognitionException pe) {
					synPredMatched143 = false;
				}
				rewind(_m143);
inputState.guessing--;
			}
			if ( synPredMatched143 ) {
				match("xmlns:");
			}
			else if ((_tokenSet_11.member(LA(1))) && (true)) {
			}
			else {
				throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			{
			switch ( LA(1)) {
			case 'a':  case 'b':  case 'c':  case 'd':
			case 'e':  case 'f':  case 'g':  case 'h':
			case 'i':  case 'j':  case 'k':  case 'l':
			case 'm':  case 'n':  case 'o':  case 'p':
			case 'q':  case 'r':  case 's':  case 't':
			case 'u':  case 'v':  case 'w':  case 'x':
			case 'y':  case 'z':
			{
				matchRange('a','z');
				break;
			}
			case 'A':  case 'B':  case 'C':  case 'D':
			case 'E':  case 'F':  case 'G':  case 'H':
			case 'I':  case 'J':  case 'K':  case 'L':
			case 'M':  case 'N':  case 'O':  case 'P':
			case 'Q':  case 'R':  case 'S':  case 'T':
			case 'U':  case 'V':  case 'W':  case 'X':
			case 'Y':  case 'Z':
			{
				matchRange('A','Z');
				break;
			}
			case '_':
			{
				match('_');
				break;
			}
			case '-':
			{
				match('-');
				break;
			}
			default:
			{
				throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			{
			_loop146:
			do {
				switch ( LA(1)) {
				case 'a':  case 'b':  case 'c':  case 'd':
				case 'e':  case 'f':  case 'g':  case 'h':
				case 'i':  case 'j':  case 'k':  case 'l':
				case 'm':  case 'n':  case 'o':  case 'p':
				case 'q':  case 'r':  case 's':  case 't':
				case 'u':  case 'v':  case 'w':  case 'x':
				case 'y':  case 'z':
				{
					matchRange('a','z');
					break;
				}
				case 'A':  case 'B':  case 'C':  case 'D':
				case 'E':  case 'F':  case 'G':  case 'H':
				case 'I':  case 'J':  case 'K':  case 'L':
				case 'M':  case 'N':  case 'O':  case 'P':
				case 'Q':  case 'R':  case 'S':  case 'T':
				case 'U':  case 'V':  case 'W':  case 'X':
				case 'Y':  case 'Z':
				{
					matchRange('A','Z');
					break;
				}
				case '_':
				{
					match('_');
					break;
				}
				case '-':
				{
					match('-');
					break;
				}
				case '0':  case '1':  case '2':  case '3':
				case '4':  case '5':  case '6':  case '7':
				case '8':  case '9':
				{
					matchRange('0','9');
					break;
				}
				default:
				{
					break _loop146;
				}
				}
			} while (true);
			}
			break;
		}
		default:
		{
			throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		}
		_ttype = testLiteralsTable(new String(text.getBuffer(),_begin,text.length()-_begin),_ttype);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mCURLY_BLOCK_SCARF(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CURLY_BLOCK_SCARF;
		int _saveIndex;
		
		match('{');
		{
		_loop134:
		do {
			// nongreedy exit test
			if ((LA(1)=='}') && (true)) break _loop134;
			if ((LA(1)=='\n'||LA(1)=='\r') && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				mNEWLINE(false);
			}
			else if ((LA(1)=='"') && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				mSTRING_LITERAL(false);
			}
			else if ((LA(1)=='\'') && (_tokenSet_6.member(LA(2)))) {
				mCHAR_LITERAL(false);
			}
			else if ((LA(1)=='/') && (LA(2)=='*'||LA(2)=='/')) {
				mCOMMENT(false);
			}
			else if (((LA(1) >= '\u0003' && LA(1) <= '\u00ff')) && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				matchNot(EOF_CHAR);
			}
			else {
				break _loop134;
			}
			
		} while (true);
		}
		match('}');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mASSIGN_RHS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ASSIGN_RHS;
		int _saveIndex;
		
		_saveIndex=text.length();
		match('=');
		text.setLength(_saveIndex);
		{
		_loop149:
		do {
			// nongreedy exit test
			if ((LA(1)==';') && (true)) break _loop149;
			if ((LA(1)=='"') && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				mSTRING_LITERAL(false);
			}
			else if ((LA(1)=='\'') && (_tokenSet_6.member(LA(2)))) {
				mCHAR_LITERAL(false);
			}
			else if ((LA(1)=='\n'||LA(1)=='\r') && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				mNEWLINE(false);
			}
			else if (((LA(1) >= '\u0003' && LA(1) <= '\u00ff')) && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				matchNot(EOF_CHAR);
			}
			else {
				break _loop149;
			}
			
		} while (true);
		}
		match(';');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mSL_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SL_COMMENT;
		int _saveIndex;
		
		match("//");
		{
		_loop160:
		do {
			if ((_tokenSet_12.member(LA(1)))) {
				{
				match(_tokenSet_12);
				}
			}
			else {
				break _loop160;
			}
			
		} while (true);
		}
		mNEWLINE(false);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mML_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ML_COMMENT;
		int _saveIndex;
		
		match("/*");
		{
		_loop163:
		do {
			// nongreedy exit test
			if ((LA(1)=='*') && (LA(2)=='/')) break _loop163;
			if ((LA(1)=='\n'||LA(1)=='\r') && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				mNEWLINE(false);
			}
			else if (((LA(1) >= '\u0003' && LA(1) <= '\u00ff')) && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				matchNot(EOF_CHAR);
			}
			else {
				break _loop163;
			}
			
		} while (true);
		}
		match("*/");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mESC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ESC;
		int _saveIndex;
		
		match('\\');
		{
		switch ( LA(1)) {
		case 'n':
		{
			match('n');
			break;
		}
		case 'r':
		{
			match('r');
			break;
		}
		case 't':
		{
			match('t');
			break;
		}
		case 'b':
		{
			match('b');
			break;
		}
		case 'f':
		{
			match('f');
			break;
		}
		case 'w':
		{
			match('w');
			break;
		}
		case 'a':
		{
			match('a');
			break;
		}
		case '"':
		{
			match('"');
			break;
		}
		case '\'':
		{
			match('\'');
			break;
		}
		case '\\':
		{
			match('\\');
			break;
		}
		case '0':  case '1':  case '2':  case '3':
		{
			{
			matchRange('0','3');
			}
			{
			if (((LA(1) >= '0' && LA(1) <= '9')) && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				mDIGIT(false);
				{
				if (((LA(1) >= '0' && LA(1) <= '9')) && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
					mDIGIT(false);
				}
				else if (((LA(1) >= '\u0003' && LA(1) <= '\u00ff')) && (true)) {
				}
				else {
					throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
				}
				
				}
			}
			else if (((LA(1) >= '\u0003' && LA(1) <= '\u00ff')) && (true)) {
			}
			else {
				throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			break;
		}
		case '4':  case '5':  case '6':  case '7':
		{
			{
			matchRange('4','7');
			}
			{
			if (((LA(1) >= '0' && LA(1) <= '9')) && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				mDIGIT(false);
			}
			else if (((LA(1) >= '\u0003' && LA(1) <= '\u00ff')) && (true)) {
			}
			else {
				throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			break;
		}
		case 'u':
		{
			match('u');
			mXDIGIT(false);
			mXDIGIT(false);
			mXDIGIT(false);
			mXDIGIT(false);
			break;
		}
		default:
		{
			throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mDIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DIGIT;
		int _saveIndex;
		
		matchRange('0','9');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mXDIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = XDIGIT;
		int _saveIndex;
		
		switch ( LA(1)) {
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			matchRange('0','9');
			break;
		}
		case 'a':  case 'b':  case 'c':  case 'd':
		case 'e':  case 'f':
		{
			matchRange('a','f');
			break;
		}
		case 'A':  case 'B':  case 'C':  case 'D':
		case 'E':  case 'F':
		{
			matchRange('A','F');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mARG_ACTION(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ARG_ACTION;
		int _saveIndex;
		
		match('[');
		{
		_loop180:
		do {
			// nongreedy exit test
			if ((LA(1)==']') && (true)) break _loop180;
			if ((LA(1)=='[') && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				mARG_ACTION(false);
			}
			else if ((LA(1)=='\n'||LA(1)=='\r') && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				mNEWLINE(false);
			}
			else if ((LA(1)=='\'') && (_tokenSet_6.member(LA(2)))) {
				mCHAR_LITERAL(false);
			}
			else if ((LA(1)=='"') && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				mSTRING_LITERAL(false);
			}
			else if (((LA(1) >= '\u0003' && LA(1) <= '\u00ff')) && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))) {
				matchNot(EOF_CHAR);
			}
			else {
				break _loop180;
			}
			
		} while (true);
		}
		match(']');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	
	private static final long[] mk_tokenSet_0() {
		long[] data = new long[8];
		data[0]=-576460752303423496L;
		for (int i = 1; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 4294977024L, 0L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = new long[8];
		data[0]=-2199023255560L;
		for (int i = 1; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = new long[8];
		data[0]=-576462951326679048L;
		for (int i = 1; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 4294977024L, 1152921504606846976L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = new long[8];
		data[0]=-576605355262354440L;
		data[1]=-576460752303423489L;
		for (int i = 2; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = new long[8];
		data[0]=-549755813896L;
		for (int i = 1; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = new long[8];
		data[0]=-17179869192L;
		data[1]=-268435457L;
		for (int i = 2; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = new long[8];
		data[0]=-549755813896L;
		data[1]=-268435457L;
		for (int i = 2; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 140758963201536L, 576460752303423488L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 140741783332352L, 576460752303423488L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 35184372088832L, 576460745995190270L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = new long[8];
		data[0]=-9224L;
		for (int i = 1; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	
	}
