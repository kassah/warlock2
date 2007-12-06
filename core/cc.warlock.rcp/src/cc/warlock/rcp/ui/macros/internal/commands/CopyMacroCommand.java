package cc.warlock.rcp.ui.macros.internal.commands;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.rcp.ui.macros.IMacroCommand;

public class CopyMacroCommand implements IMacroCommand {

	public void execute(IWarlockClientViewer context) {
		context.copy();
	}

	public String getIdentifier() {
		return "Copy";
	}

}
