lexer grammar AlgumaLexer;
//Palavras Chaves
PALAVRA_CHAVE 
	:	'algoritmo' | 'fim_algoritmo' | 'declare' | 'literal' | 'inteiro' | 'leia' | 'escreva' 
                | 'tem' | 'anos' | 'se' | 'fim_se'  | 'registro' | 'fim_registro' | 'falso'
                | 'verdadeiro' | 'nao' | 'fim_se'   | 'procedimento' | 'fim_procedimento' | 'fim_se' 

        ; 


//Numeros
NUMINT	: ('0'..'9')+
	;
NUMREAL	: ('+'|'-')?('0'..'9')+ ('.' ('0'..'9')+)?
	;

//Identificadores
IDENT   : ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9')*
	;

// Cadeias
CADEIA  : '"' ('\\"' | ~('"' | '\\' | '\n'))* '"'
        ;
CADEIA_NAO_FECHADA: '"' ('\\"' | ~('"' | '\\' | '\n'))* '\n'
        ;

//Operadores
OP_REL	:	'>' | '>=' | '<' | '<=' | '<>' | '='
	;
OP_ARIT	:	'+' | '-' | '*' | '/'
	;

//Simbolos
DELIM	:	':'
	;
ABREPAR :	'('
	;
FECHAPAR:	')'
	;
ABRECOL :       '['
        ;
FECHACOL:       ']'
        ;
VIRGULA :       ','
        ;
PONTO   :       '.'
        ;

//Comentarios/Espaços em branco
COMENTARIO: '{' ~('\n' | '\r' | '}')* '}' -> skip;
WS: (' ' | '\t' | '\r' | '\n') -> skip;

ERRO    : .
        ;