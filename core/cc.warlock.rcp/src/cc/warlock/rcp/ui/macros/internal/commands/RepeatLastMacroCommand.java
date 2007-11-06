package cc.warlock.rcp.ui.macros.internal.commands;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.rcp.ui.macros.IMacroCommand;

public class RepeatLastMacroCommand implements IMacroCommand {
	
	public String getIdentifier() {
		return "RepeatLast";
	}
	
	public void execute(IWarlockClientViewer viewer) {
		viewer.repeatLastCommand();
	}
}
