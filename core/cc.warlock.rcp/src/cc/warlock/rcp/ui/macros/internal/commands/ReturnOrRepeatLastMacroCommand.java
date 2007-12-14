package cc.warlock.rcp.ui.macros.internal.commands;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.rcp.ui.macros.IMacroCommand;

public class ReturnOrRepeatLastMacroCommand implements IMacroCommand {
	
	public String getIdentifier() {
		return "ReturnOrRepeatLast";
	}
	
	public void execute(IWarlockClientViewer viewer) {
		if(viewer.getCurrentCommand().length() > 0)
			viewer.submit();
		else
			viewer.repeatLastCommand();
	}
}
