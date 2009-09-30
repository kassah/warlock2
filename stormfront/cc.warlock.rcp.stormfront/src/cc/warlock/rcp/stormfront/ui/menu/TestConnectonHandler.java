/**
 * 
 */
package cc.warlock.rcp.stormfront.ui.menu;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;

import cc.warlock.rcp.menu.SimpleCommandHandler;
import cc.warlock.rcp.stormfront.ui.util.LoginUtil;
import cc.warlock.rcp.stormfront.ui.wizards.SGEConnectWizard;
import cc.warlock.rcp.ui.WarlockWizardDialog;

/**
 * @author kassah
 *
 * Connects to a test server on xibar.warlock.cc used for testing dumps provided from users against warlock.
 */
public class TestConnectonHandler extends SimpleCommandHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		try {
			Map<String, String> loginProperties = new HashMap<String, String>();
			loginProperties.put("GAMEHOST", "xibar.warlock.cc");
			loginProperties.put("GAMEPORT", "8940");
			loginProperties.put("KEY", "blah");
			loginProperties.put("GAMECODE", "TS");
			LoginUtil.connectAndOpenGameView(loginProperties, "Test");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
