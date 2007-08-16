header {
	package cc.warlock.script.wsl.internal;
}


{ import java.util.ArrayList; }
class WarlockWSLParser extends Parser;
options { k = 2; }

script returns [ArrayList<ArrayList<String>> scriptLines]
{ scriptLines = new ArrayList<ArrayList<String>>(); ArrayList<String> scriptLine, scriptLine2; }
: (
	scriptLine=line { if(!scriptLine.isEmpty())scriptLines.add(scriptLine); }
	(EOL scriptLine2=line { if(!scriptLine2.isEmpty())scriptLines.add(scriptLine2); })*
	(EOL)?
	EOF
);

line returns [ArrayList<String> scriptLine]
{ scriptLine = new ArrayList<String>(); }
: (token:STRING {scriptLine.add(token.getText()); })*;

class WarlockWSLLexer extends Lexer;

SPACE: (' ' | '\t' ) { $setType(Token.SKIP); };
EOL: ('\n' {newline();} | '\r');

STRING: (~(' '|'\t'|'\r'|'\n'))+;