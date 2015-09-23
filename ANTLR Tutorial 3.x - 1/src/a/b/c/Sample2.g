/*******************************************************************************
 * Copyright (c) 2009 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
grammar Sample2;

options {
  language = Java;
}

@header {
  package a.b.c;
}

@lexer::header {
  package a.b.c;
}

evaluator returns [int result]
	:	expression EOF { $result = $expression.result; }
	;
	
// expressions -- fun time!

term returns [int result]
	:	IDENT {$result = 0;}
	|	'(' expression ')' {$result = $expression.result;}
	|	INTEGER {$result = Integer.parseInt($INTEGER.text);}
	;
	
unary returns [int result]
	:	{ boolean positive = true; }
		('+' | '-' { positive = !positive; })* term
		{
			$result = $term.result;
			if (!positive)
				$result = -$result;
		}
	;

mult returns [int result]
	:	op1=unary { $result = $op1.result; }
		(	'*' op2=unary { $result = $result * $op2.result; }
		|	'/' op2=unary { $result = $result / $op2.result; }
		|	'mod' op2=unary { $result = $result \% $op2.result; }
		)*
	;
	
expression returns [int result]
	:	op1=mult { $result = $op1.result; }
		(	'+' op2=mult { $result = $result + $op2.result; }
		|	'-' op2=mult { $result = $result - $op2.result; }
		)*
	;


MULTILINE_COMMENT : '/*' .* '*/' {$channel = HIDDEN;} ;

STRING_LITERAL
	:	'"'
		{ StringBuilder b = new StringBuilder(); }
		(	'"' '"'				{ b.appendCodePoint('"');}
		|	c=~('"'|'\r'|'\n')	{ b.appendCodePoint(c);}
		)*
		'"'
		{ setText(b.toString()); }
	;
	
CHAR_LITERAL
	:	'\'' . '\'' {setText(getText().substring(1,2));}
	;

fragment LETTER : ('a'..'z' | 'A'..'Z') ;
fragment DIGIT : '0'..'9';
INTEGER : DIGIT+ ;
IDENT : LETTER (LETTER | DIGIT)*;
WS : (' ' | '\t' | '\n' | '\r' | '\f')+ {$channel = HIDDEN;};
COMMENT : '//' .* ('\n'|'\r') {$channel = HIDDEN;};
