package cc.warlock.script.wsl;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.warlock.script.wsl.WSLScript.WSLCommand;

public class WSLScriptLine {
	private WSLScript script;
	private int lineNumber;
	private WSLScriptLine next;
	private IWSLValue value;
	private IWSLValue condition;
	
	public WSLScriptLine(WSLScript script, int lineNumber) {
		this.script = script;
		this.lineNumber = lineNumber;
	}
	
	
	public void execute() {
		if(value == null) return; // in a label
		
		// Skip the line if the condition is false
		if(condition != null && !condition.getBoolean()) return;
		
		String line = value.getString();
		System.out.print("script line: " + line + "\n");
		script.execute(line);
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public void setNext(WSLScriptLine next) {
		this.next = next;
	}
	
	public WSLScriptLine getNext() {
		return next;
	}
	
	public void setValue(IWSLValue value) {
		this.value = value;
	}
	
	public void setCondition(IWSLValue cond) {
		condition = cond;
	}
}
