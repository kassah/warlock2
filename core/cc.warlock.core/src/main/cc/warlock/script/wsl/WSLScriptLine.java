package cc.warlock.script.wsl;

public class WSLScriptLine {
	private int lineNumber;
	private WSLScriptLine next;
	private IWSLValue value;
	private IWSLValue condition;
	
	public WSLScriptLine(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	
	public String get() {
		if(value == null) return null; // in a label
		
		// Skip the line if the condition is false
		if(condition != null && !condition.getBoolean()) return null;
		
		return value.getString();
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
