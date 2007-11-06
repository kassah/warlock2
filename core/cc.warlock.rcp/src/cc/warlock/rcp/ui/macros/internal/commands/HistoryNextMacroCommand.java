package cc.warlock.rcp.ui.macros.internal.commands;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.rcp.ui.macros.IMacroCommand;

public class HistoryNextMacroCommand implements IMacroCommand {
	
	public String getIdentifier() {
		return "HistoryNext";
	}
	
	public void execute(IWarlockClientViewer viewer) {
		viewer.nextCommand();
	}
}
