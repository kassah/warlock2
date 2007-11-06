package cc.warlock.rcp.ui.macros.internal.commands;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.rcp.ui.macros.IMacroCommand;

public class HistoryPrevMacroCommand implements IMacroCommand {
	
	public String getIdentifier() {
		return "HistoryPrev";
	}
	
	public void execute(IWarlockClientViewer viewer) {
		viewer.prevCommand();
	}
}
