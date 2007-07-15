
grammar WarlockWSL;
options { k = 2; }

@parser::header {
package com.arcaner.warlock.script.wsl.internal;
}

@lexer::header {
package com.arcaner.warlock.script.wsl.internal;
}

primary_exp:	exp_list | '(' or_exp ')';
unary_exp:		primary_exp | '!' unary_exp | 'exists' unary_exp;
multiply_exp:	unary_exp (('*' | '/') unary_exp)*;
additive_exp:	multiply_exp (('+' | '-') multiply_exp)*;
relational_exp:	additive_exp (('=' | '==' | '<=' | '>=' | '!=' | '<>' | '<' | '>') additive_exp)*;
and_exp:		relational_exp ('&&' relational_exp)*;
or_exp:			and_exp ('||' and_exp)*;
if_var:			'if_' (alnum)+;
if_statement:	'if' or_exp 'then';
condition_statement:	if_var | if_statement;

label:			(alnum)+ ':';

command:		arg_list;

line:			space (label)? (comment | ((condition_statement)* command))? EOL;

exp_list:		exp_data | (exp_data exp_list);
exp_data:		(alnum (alnum | space)*) | script_variable;

arg_list:		script_data | (script_data arg_list);
script_data:	script_string | script_variable;
script_variable:	'%' (alnum)+;
script_string:	(~('%' | EOL))+;
space:			(' ' | '\t' ) { skip(); };
EOL:	('\n' | '\r' '\n') { };
alnum: 'a'..'z' | 'A'..'Z' | '0'..'9';

comment:		'#' (~('\n' | '\r'))* { skip(); };
