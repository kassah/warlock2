package cc.warlock.rcp.ui.macros.internal.commands;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.rcp.ui.macros.IMacroCommand;

public class HistorySearchMacroCommand implements IMacroCommand {
	
	public String getIdentifier() {
		return "HistorySearch";
	}
	
	public void execute(IWarlockClientViewer viewer) {
		viewer.searchHistory();
	}
}
