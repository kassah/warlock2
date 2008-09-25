grammar WSL;

options { backtrack=true; memoize=true; }

@parser::header {
	package cc.warlock.core.stormfront.script.wsl;
	import java.util.ArrayList;
	import cc.warlock.core.stormfront.script.wsl.WSLEqualityCondition.EqualityOperator;
	import cc.warlock.core.stormfront.script.wsl.WSLRelationalCondition.RelationalOperator;
}

@lexer::header {
	package cc.warlock.core.stormfront.script.wsl;
}

@parser::members {
	private WSLScript script;
	private int lineNum = 1;
	private int actionDepth = 0;
	public void setScript(WSLScript s) { script = s; }
	private boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	private boolean followingWhitespace() {
		return input.get(input.index() - 1).getChannel() != Token.DEFAULT_CHANNEL;
	}
	@Override
	public void reportError(RecognitionException ex) {
		script.getCommands().echo("Line: " + ex.line + ":" + ex.charPositionInLine + ": " + ex.toString());
		script.stop();
	}
}

@lexer::members {
	private boolean atStart = true;	
}

script
	: line (EOL line)* EOF
	;

line
	: (label=LABEL)? (c=expr)?
		{
			script.addCommand(c);
			if(label != null) {
				int existingLine = script.labelLineNumber($label.text);
				if(existingLine != -1)
					script.echo("Redefinition of label \"" + $label.text + "\" on line " + lineNum + ", originally defined on line " + existingLine);
				script.addLabel($label.text, lineNum);
			}
			lineNum++;
		}
	;

expr returns [WSLAbstractCommand command]
	: (IF)=> IF cond=conditionalExpression THEN c=expr
		{
			command = new WSLCondition(lineNum, script, cond, c);
		}
	| (ACTION)=> ACTION { actionDepth++; } (c=expr WHEN { actionDepth--; } args=string_list
			{
				command = new WSLAction(lineNum, script, c, args);
			}
		| (REMOVE)=> REMOVE { actionDepth--; } args=string_list
			{
				command = new WSLActionRemove(lineNum, script, args);
			}
		| (CLEAR)=> CLEAR
			{
				command = new WSLActionClear(lineNum, script);
				actionDepth--;
			})
	| (INSTANT)=> INSTANT c=expr
		{
			command = c;
			command.setInstant(true);
		}
	| args=string_list
		{
			command = new WSLCommand(lineNum, script, args);
		}
	;

string_list returns [IWSLValue value]
	: l=string_list_helper
			{
				if(l.size() > 1)
					value = new WSLList(l);
				else
					value = l.get(0);
			}
	;

string_list_helper returns [ArrayList<IWSLValue> list] @init { String whitespace = null; }
	: data=string_value
		{
			whitespace = "";
			for(int i = input.index() - 1; i >= 0 && input.get(i).getChannel() != Token.DEFAULT_CHANNEL; i--) {
				whitespace = input.get(i).getText() + whitespace;
			}
		}
	(l=string_list_helper)?
		{
			if(l == null) {
				list = new ArrayList<IWSLValue>();
				list.add(data);
			} else {
				list = l;
				if(whitespace != null && whitespace.length() > 0)
					list.add(0, new WSLString(whitespace));
				list.add(0, data);
			}
		}
	;

string_value returns [IWSLValue value]
	: str=string				{ value = new WSLString(str); }
	| v=variable				{ value = new WSLVariable(v, script); }
	| v=local_variable			{ value = new WSLLocalVariable(v, script); }
	;
	
conditionalExpression returns [IWSLValue cond] @init { ArrayList<IWSLValue> args = null; }
	: arg=conditionalAndExpression { args = null; }
		(OR argNext=conditionalAndExpression
			{
				if(args == null) {
					args = new ArrayList<IWSLValue>();
					args.add(arg);
				}
				args.add(argNext);
			}
		)*
			{
				if(args == null)
					cond = arg;
				else
					cond = new WSLOrCondition(args);
			}
	;

conditionalAndExpression returns [IWSLValue cond] @init { ArrayList<IWSLValue> args = null; }
	: arg=equalityExpression
		(AND argNext=equalityExpression
			{
				if(args == null) {
					args = new ArrayList<IWSLValue>();
					args.add(arg);
				}
				args.add(argNext);
			}
		)*
		{
			if(args == null)
				cond = arg;
			else
				cond = new WSLAndCondition(args);
		}
	;

equalityExpression returns [IWSLValue cond]
	@init {
			ArrayList<IWSLValue> args = null;
			ArrayList<EqualityOperator> ops = null;
		}
	: arg=relationalExpression { args = null; ops = null; }
		(op=equalityOp argNext=relationalExpression
			{
				if(args == null) {
					args = new ArrayList<IWSLValue>();
					args.add(arg);
				}
				args.add(argNext);
				if(ops == null) {
					ops = new ArrayList<EqualityOperator>();
				}
				ops.add(op);
			}
		)*
			{
				if(args == null)
					cond = arg;
				else
					cond = new WSLEqualityCondition(args, ops);
			}
	;

equalityOp returns [EqualityOperator op]
	: EQUAL			{ op = EqualityOperator.equals; }
	| NOTEQUAL		{ op = EqualityOperator.notequals; }
	;
	
relationalExpression returns [IWSLValue cond]
	@init {
			ArrayList<IWSLValue> args = null;
			ArrayList<RelationalOperator> ops = null;
		}
	: arg=unaryExpression { args = null; ops = null; }
		(op=relationalOp argNext=unaryExpression
			{
				if(args == null) {
					args = new ArrayList<IWSLValue>();
					args.add(arg);
				}
				args.add(argNext);
				if(ops == null) {
					ops = new ArrayList<RelationalOperator>();
				}
				ops.add(op);
			}
		)*
			{
				if(args == null)
					cond = arg;
				else
					cond = new WSLRelationalCondition(args, ops);
			}
	;

relationalOp returns [RelationalOperator op]
	: GT		{ op = RelationalOperator.GreaterThan; }
	| LT		{ op = RelationalOperator.LessThan; }
	| GTE		{ op = RelationalOperator.GreaterThanEqualTo; }
	| LTE		{ op = RelationalOperator.LessThanEqualTo; }
	| CONTAINS	{ op = RelationalOperator.Contains; }
	;

unaryExpression returns [IWSLValue cond]
	: NOT arg=unaryExpression		{ cond = new WSLNotCondition(arg); }
	| EXISTS arg=unaryExpression	{ cond = new WSLExistsCondition(arg); }
	| arg=primaryExpression			{ cond = arg; }
	;

parenExpression returns [IWSLValue cond]
	: LPAREN arg=conditionalExpression RPAREN		{ cond = arg; }
	;

primaryExpression returns [IWSLValue cond]
	: arg=parenExpression	{ cond = arg; }
	| (v=cond_value)			{ cond = v; }
	;
	
cond_value returns [IWSLValue value]
	: PERCENT v=STRING	{ value = new WSLVariable($v.text, script); }
	| DOLLAR v=STRING	{ value = new WSLLocalVariable($v.text, script); }
	| val=number		{ value = val; }
	| TRUE				{ value = new WSLBoolean(true); }
	| FALSE				{ value = new WSLBoolean(false); }
	| val=quoted_string	{ value = val; }
	;

number returns [IWSLValue value]
	: { isNumber(input.LT(1).getText()) }? v=STRING	{ value = new WSLNumber($v.text); }
	;

quoted_string returns [IWSLValue value]
	: QUOTE l=quoted_string_helper QUOTE
		{
			// FIXME: get preceding and following whitespace
			if(l.size() > 1)
				value = new WSLList(l);
			else
				value = l.get(0);
		}
	;

quoted_string_helper returns [ArrayList<IWSLValue> list] @init { String whitespace = null; }
	: data=quoted_string_value
		{
			whitespace = "";
			for(int i = input.index() - 1; i >= 0 && input.get(i).getChannel() != Token.DEFAULT_CHANNEL; i--) {
				whitespace = input.get(i).getText() + whitespace;
			}
		}
	(l=quoted_string_helper)?
		{
			if(l == null) {
				list = new ArrayList<IWSLValue>();
				list.add(data);
			} else {
				list = l;
				if(whitespace != null && whitespace.length() > 0)
					list.add(0, new WSLString(whitespace));
				list.add(0, data);
			}
		}
	;

quoted_string_value returns [IWSLValue value]
	: str=qstring				{ value = str; }
	| v=qvariable				{ value = new WSLVariable(v, script); }
	| v=qlocal_variable			{ value = new WSLLocalVariable(v, script); }
	;

common_string returns [String value]
	: (str=STRING | str=IF | str=THEN | str=OR | str=AND | str=NOTEQUAL
		| str=NOT | str=EQUAL | str=GTE | str=LTE | str=GT | str=LT
		| str=RPAREN | str=LPAREN | str=EXISTS | str=CONTAINS | str=ACTION
		| { actionDepth == 0 }? str=WHEN | str=REMOVE | str=CLEAR | str=TRUE
		| str=FALSE | str=INSTANT | str=BACKSLASH | str=AMP | str=VERT)
		{ value = $str.text; }
	;

qstring returns [IWSLValue value]
	: str=common_string { value = new WSLString(str); }
	| (PERCENT t=PERCENT | DOLLAR t=DOLLAR) { value = new WSLString($t.text); }
	;
	
string returns [String value]
	: str=common_string { value = str; }
	| (t=QUOTE | (PERCENT PERCENT)=> PERCENT t=PERCENT
		| (DOLLAR DOLLAR)=> DOLLAR t=DOLLAR
		| t=PERCENT { followingWhitespace() }?
		| t=DOLLAR { followingWhitespace() }?
	) { value = $t.text; }
	;

variable returns [String value]
	: PERCENT str=variable_string_helper ({ !followingWhitespace() }? PERCENT)? { value = str; }
	;
	
local_variable returns [String value]
	: DOLLAR str=local_variable_string_helper ({ !followingWhitespace() }? DOLLAR)? { value = str; }
	;

variable_string_helper returns [String value]
	: str=variable_string ({ !followingWhitespace() }? rest=variable_string_helper)?
		{
			if(rest == null) value = str;
			else value = str + rest;
		}
	;

local_variable_string_helper returns [String value]
	: str=local_variable_string ({ !followingWhitespace() }? rest=local_variable_string_helper)?
		{
			if(rest == null) value = str;
			else value = str + rest;
		}
	;
	
variable_string returns [String value]
	: str=common_string { value = str; }
	| (t=QUOTE | t=DOLLAR) { value = $t.text; }
	;

local_variable_string returns [String value]
	: str=common_string { value = str; }
	| (t=QUOTE | t=PERCENT) { value = $t.text; }
	;

qvariable returns [String value]
	: PERCENT str=qvariable_string_helper ({ !followingWhitespace() }? PERCENT)? { value = str; }
	;
	
qlocal_variable returns [String value]
	: DOLLAR str=qlocal_variable_string_helper ({ !followingWhitespace() }? DOLLAR)? { value = str; }
	;

qvariable_string returns [String value]
	: str=common_string { value = str; }
	| t=DOLLAR { value = $t.text; }
	;

qvariable_string_helper returns [String value]
	: str=qvariable_string ({ !followingWhitespace() }? rest=qvariable_string_helper)?
		{
			if(rest == null) value = str;
			else value = str + rest;
		}
	;

qlocal_variable_string_helper returns [String value]
	: str=qlocal_variable_string ({ !followingWhitespace() }? rest=qlocal_variable_string_helper)?
		{
			if(rest == null) value = str;
			else value = str + rest;
		}
	;

qlocal_variable_string returns [String value]
	: str=common_string { value = str; }
	| t=PERCENT { value = $t.text; }
	;


IF
	: 'if' { atStart = false; }
	;
THEN
	: 'then' { atStart = false; }
	;
OR
	: ('or' | '||') { atStart = false; }
	;
AND
	: ('and' | '&&') { atStart = false; }
	;
NOTEQUAL
	: ('!=' | '<>') { atStart = false; }
	;
NOT
	: ('not' | '!') { atStart = false; }
	;
EQUAL
	: ('==' | '=') { atStart = false; }
	;
GTE
	: '>=' { atStart = false; }
	;
LTE
	: '<=' { atStart = false; }
	;
GT
	: '>' { atStart = false; }
	;
LT
	: '<' { atStart = false; }
	;
LPAREN
	: '(' { atStart = false; }
	;
RPAREN
	: ')' { atStart = false; }
	;
EXISTS
	: 'exists' { atStart = false; }
	;
CONTAINS
	: ('contains' | 'indexof') { atStart = false; }
	;
ACTION
	: 'action' { atStart = false; }
	;
WHEN
	: 'when' { atStart = false; }
	;
REMOVE
	: 'remove' { atStart = false; }
	;
CLEAR
	: 'clear' { atStart = false; }
	;
TRUE
	: 'true' { atStart = false; }
	;
FALSE
	: 'false' { atStart = false; }
	;
INSTANT
	: 'instant' {atStart = false; }
	;
QUOTE
	: '"' { atStart = false; }
	;
BLANK
	: (' ' | '\t')+			{ $channel = HIDDEN; }
	;
EOL
	: ('\r'? '\n' | '\r')	{ atStart = true; }
	;
PERCENT
	: '%'					{ atStart = false; }
	;
DOLLAR
	: '$'					{ atStart = false; }
	;
AMP
	: '&'					{ atStart = false; }
	;
VERT
	: '|'					{ atStart = false; }
	;

STRING
	: (~('%'|'$'|'\\'|'"'|'!'|'='|'>'|'<'|'('|')'|'&'|'|'|WS))+  { atStart = false; }
	;
BACKSLASH
    : '\\' c=ANY { setText($c.text); atStart = false; }
	;
LABEL
	: { atStart }?=> (LABEL_STRING ':')=> label=LABEL_STRING ':' { setText($label.text); atStart = false; }
	;
COMMENT
	: { atStart }?=> (~(WORD_CHAR|WS|'$'|'%'|'\\'))=> ~(WORD_CHAR|WS|'$'|'%'|'\\') (~('\n'|'\r'))* { $channel = HIDDEN; }
	;

fragment WS
	: ' ' | '\t' | '\n' | '\r'
	;
fragment WORD_CHAR
	: ('a'..'z'|'0'..'9'|'_')
	;
fragment LABEL_STRING
	: (~(WS|':'))+
	;
fragment ANY
	: .
	;
