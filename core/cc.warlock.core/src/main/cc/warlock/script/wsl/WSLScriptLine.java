package cc.warlock.script.wsl;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.warlock.script.wsl.WSLScript.WSLCommand;

public class WSLScriptLine {
	private WSLScript script;
	private int lineNumber;
	private WSLScriptLine next;
	private ArrayList<IWSLValue> args = new ArrayList<IWSLValue>();
	private IWSLValue condition;
	
	private Pattern commandPattern;
	
	public WSLScriptLine(WSLScript script, int lineNumber) {
		this.script = script;
		this.lineNumber = lineNumber;
		commandPattern = Pattern.compile("^([\\w_]+)(\\s+(.*))?");
	}
	
	
	public void execute() {
		if(args == null) return; // in a label
		
		// Skip the line if the condition is false
		if(condition != null && !condition.getBoolean()) return;
		
		StringBuffer buffer = new StringBuffer();
		
		for(IWSLValue arg : args) {
			// System.out.print("appending arg \"" + arg.getString(vars) + "\"\n");
			
			buffer.append(arg.getString());
		}
		
		String line = buffer.toString();
		System.out.print("script line: " + line + "\n");
		
		Matcher m = commandPattern.matcher(line);
		
		if (!m.find()) {
			System.out.println("Couldn't find the command");
			return;
		}
		
		String commandName = m.group(1).toLowerCase();
		// System.out.print("command \"" + commandName + "\"\n");
		String arguments = m.group(3);
		if(arguments == null) arguments = "";
		// System.out.print("arguments \"" + arguments + "\"\n");
		
		WSLCommand command = script.getCommands().get(commandName);
		if(command != null) command.execute(arguments);
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
	
	public void addArg(IWSLValue arg) {
		args.add(arg);
	}
	
	public void addArgs(ArrayList<IWSLValue> args) {
		for(IWSLValue arg : args) {
			addArg(arg);
		}
	}
	
	public void setCondition(IWSLValue cond) {
		condition = cond;
	}
}
