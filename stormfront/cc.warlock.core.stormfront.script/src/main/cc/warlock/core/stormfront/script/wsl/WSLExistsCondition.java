package cc.warlock.core.stormfront.script.wsl;

public class WSLExistsCondition extends WSLAbstractBoolean {

	private IWSLValue variable;
	
	public WSLExistsCondition(IWSLValue variable) {
		this.variable = variable;
		
	}
	
	@Override
	public boolean toBoolean() {
		if(variable instanceof WSLAbstractVariable) {
			return ((WSLAbstractVariable)variable).variableExists();
		}
		return false;
	}

}
