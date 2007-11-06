package cc.warlock.rcp.ui.macros.internal.commands;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.rcp.ui.macros.IMacroCommand;

public class RepeatSecondToLastMacroCommand implements IMacroCommand {
	
	public String getIdentifier() {
		return "RepeatSecondToLast";
	}
	
	public void execute(IWarlockClientViewer viewer) {
		viewer.repeatSecondToLastCommand();
	}
}
