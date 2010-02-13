package cc.warlock.core.client.settings;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.internal.WarlockMacro;

public class WarlockMacroProvider implements WarlockPreferenceProvider<WarlockMacro> {
	private static WarlockMacroProvider instance;
	
	private WarlockMacroProvider() { }
	
	protected static synchronized WarlockMacroProvider getInstance() {
		if(instance == null)
			instance = new WarlockMacroProvider();
		return instance;
	}
	
	public static Collection<WarlockPreference<WarlockMacro>> getAll(
			WarlockClientPreferences prefs) {
		ArrayList<WarlockPreference<WarlockMacro>> results =
			new ArrayList<WarlockPreference<WarlockMacro>>();

		Preferences inode = prefs.getNode().node("macro");
		try {
			for (String name : inode.childrenNames()) {
				Preferences node = inode.node(name);
				results.add(new WarlockPreference<WarlockMacro>(getInstance(),
						node.absolutePath(), getMacro(node)));
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}

		return results;
	}
	
	protected static WarlockMacro getMacro(Preferences node) {
		int keycode = node.getInt("keycode", 0);
		int modifiers = node.getInt("modifiers", 0);
		String command = node.get("command", null);
		
		return new WarlockMacro(keycode, modifiers, command);
	}
	
	protected static void saveMacro(Preferences node, WarlockMacro macro) {
		node.putInt("keycode", macro.getKeycode());
		node.putInt("modifiers", macro.getModifiers());
		node.put("command", macro.getCommand());
	}
	
	public void save(String path, WarlockMacro value) {
		saveMacro(WarlockPreferences.getScope().getNode(path), value);
	}
}
