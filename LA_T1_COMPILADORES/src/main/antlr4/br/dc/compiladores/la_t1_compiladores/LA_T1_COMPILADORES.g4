lexer grammar LA_T1_COMPILADORES;
//Palavras Chaves
PALAVRA_CHAVE 
	:	'algoritmo' | 'fim_algoritmo' | 'declare' | 'literal' | 'inteiro' | 'real' | 'leia' | 'escreva' 
                | 'se' | 'entao' | 'fim_se'  | 'senao' | 'registro' | 'fim_registro' | 'falso' | 'logico' 
                | 'verdadeiro' | 'nao'  | 'enquanto' | 'fim_enquanto'
                | 'retorne' | 'caso' | 'fim_caso' | 'seja'
                | 'var' | 'para' | 'ate' | 'faca' | 'fim_para' | 'registro' | 'fim_registro'
                | 'procedimento' | 'fim_procedimento' | 'funcao' | 'fim_funcao'
                | 'tipo' | 'constante' | 
        ; 


//Numeros
NUM_INT	: ('0'..'9')+
	;
NUM_REAL	:  NUM_INT + ('.' NUM_INT+)?
	;

OP_BOOLEANO :   'e' | 'ou' | 'nao' |   
        ;

//Identificadores
IDENT   : ('a'..'z'|'A'..'Z')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
	;

// Cadeias
CADEIA  : '"' ('\\"' | ~('"' | '\\' | '\n'))* '"'
        ;
CADEIA_ABERTA: '"' ('\\"' | ~('"' | '\\' | '\n'))* '\n'
        ;

//Operadores
OP_REL	:	'>' | '>=' | '<' | '<=' | '<>' | '='
	;
OP_ARIT	:	'+' | '-' | '*' | '/' | '%'
	;

//Simbolos
SIMBOLOS:       ':' | ',' | '[' | ']' | '^' | '(' | ')' | '-' | '&' | '..' | '.' | '<-' 
        ; 
        

//Comentarios/Espaços em branco
COMENTARIO: '{' ~('\n' | '\r' | '}')* '}' -> skip;
COMENTARIO_ABERTO: '{' ~('}')* '\n';
WS: (' ' | '\t' | '\r' | '\n') -> skip;

ERRO    : .
        ;
