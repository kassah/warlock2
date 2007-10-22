package cc.warlock.rcp.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import cc.warlock.rcp.util.RCPUtil;

public class VisitWarlockWebpageHandler extends SimpleCommandHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		RCPUtil.openURL("http://warlock.cc");
		
		return null;
	}

}
