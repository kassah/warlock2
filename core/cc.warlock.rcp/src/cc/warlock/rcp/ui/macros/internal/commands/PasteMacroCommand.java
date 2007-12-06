package cc.warlock.rcp.ui.macros.internal.commands;

import cc.warlock.core.client.IWarlockClientViewer;

public class PasteMacroCommand {
	public void execute(IWarlockClientViewer context) {
		context.paste();
	}

	public String getIdentifier() {
		return "Paste";
	}
}
