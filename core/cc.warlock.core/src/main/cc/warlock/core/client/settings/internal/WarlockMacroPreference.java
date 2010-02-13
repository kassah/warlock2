package cc.warlock.core.client.settings.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.internal.WarlockMacro;
import cc.warlock.core.client.internal.WarlockPreference;

public class WarlockMacroPreference {
	public static WarlockPreference<Collection<WarlockMacro>> getAll(
			WarlockClientPreferences prefs) {
		ArrayList<WarlockMacro> results = new ArrayList<WarlockMacro>();

		Preferences inode = prefs.getClientPreferences().node("macro");
		try {
			for (String name : inode.childrenNames()) {
				results.add(getMacro(inode.node(name)));
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}

		return new WarlockPreference<Collection<WarlockMacro>>(prefs, inode.absolutePath(), results);
	}
	
	protected static WarlockMacro getMacro(Preferences node) {
		int keycode = node.getInt("keycode", 0);
		int modifiers = node.getInt("modifiers", 0);
		String command = node.get("command", null);
		
		return new WarlockMacro(keycode, modifiers, command);
	}
}
