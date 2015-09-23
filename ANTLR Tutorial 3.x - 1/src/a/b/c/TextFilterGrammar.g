grammar TextFilterGrammar;
options {
       output=AST;
  	ASTLabelType=CommonTree;
}
@header {
  package a.b.c;
}

@lexer::header {
  package a.b.c;
}

content :       orexpression
       ;
orexpression
       :       andexpression (OR^ andexpression)*
       ;
andexpression
       :       expression (AND^ expression)*
       ;
expression
       :       (NOT^)? term
       ;
term    :       WORD | NOT | AND | OR
       ;

NOT     :       'not'
       ;
AND     :       'and'
       ;
OR      :       'or'
       ;
WORD    :       ('a'..'z' | '0'..'9' | '%' | '_')+
       ;
WS      :       (' ' | '\r' | '\n' | '\t')  { skip(); }
       ;