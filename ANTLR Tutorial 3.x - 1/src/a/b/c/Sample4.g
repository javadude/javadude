/*******************************************************************************
 * Copyright (c) 2009 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
grammar Sample4;

options {
  language = Java;
  output=AST;
  ASTLabelType=CommonTree;
}
tokens {
	NEGATION;
}

@header {
  package a.b.c;
}

@lexer::header {
  package a.b.c;
}

evaluator
	:	assignment* expression EOF!
	;
	
assignment
	:	IDENT ':='^ expression ';'!
	;
	
// expressions -- fun time!

term
	:	IDENT
	|	'('! expression ')'!
	|	INTEGER
	;
	
unary
	:	('+'! | negation^)* term
	;

negation
	:	'-' -> NEGATION
	;

mult
	:	unary (('*'^ | '/'^ | 'mod'^) unary)*
	;
	
expression
	:	mult (('+'^ | '-'^) mult)*
	;


fragment LETTER : ('a'..'z' | 'A'..'Z') ;
fragment DIGIT : '0'..'9';
INTEGER : DIGIT+ ;
IDENT : LETTER (LETTER | DIGIT)*;
WS : (' ' | '\t' | '\n' | '\r' | '\f')+ {$channel = HIDDEN;};
