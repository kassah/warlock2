/**
 * 
 */
package cc.warlock.rcp.stormfront.ui.menu;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import cc.warlock.rcp.menu.SimpleCommandHandler;
import cc.warlock.rcp.stormfront.ui.util.LoginUtil;

/**
 * @author kassah
 *
 * Connects to a test server on xibar.warlock.cc used for testing dumps provided from users against warlock.
 */
public class TestConnectonHandler extends SimpleCommandHandler {

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
