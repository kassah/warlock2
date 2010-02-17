package cc.warlock.core.client.settings;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.internal.WarlockMacro;

public class WarlockMacroProvider implements WarlockPreferenceProvider<WarlockMacro> {
	private static final String NODE_NAME = "macro";
	
	private static final WarlockMacroProvider instance = new WarlockMacroProvider();
	
	private WarlockMacroProvider() { }
	
	protected static WarlockMacroProvider getInstance() {
		return instance;
	}
	
	public static Collection<WarlockPreference<WarlockMacro>> getAll(
			WarlockClientPreferences prefs) {
		ArrayList<WarlockPreference<WarlockMacro>> results =
			new ArrayList<WarlockPreference<WarlockMacro>>();

		Preferences inode = prefs.getNode().node(NODE_NAME);
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
	
	public static void addNodeChangeListener(WarlockClientPreferences prefs,
			INodeChangeListener listener) {
		String path = prefs.getNode().node(NODE_NAME).absolutePath();
		WarlockPreferences.getScope().getNode(path).addNodeChangeListener(listener);
	}
	
	public static void addPreferenceChangeListener(WarlockClientPreferences prefs,
			IPreferenceChangeListener listener) {
		String path = prefs.getNode().node(NODE_NAME).absolutePath();
		WarlockPreferences.getScope().getNode(path).addPreferenceChangeListener(listener);
	}
	
	public static void removeNodeChangeListener(WarlockClientPreferences prefs,
			INodeChangeListener listener) {
		String path = prefs.getNode().node(NODE_NAME).absolutePath();
		WarlockPreferences.getScope().getNode(path).removeNodeChangeListener(listener);
	}
	
	public static void removePreferenceChangeListener(WarlockClientPreferences prefs,
			IPreferenceChangeListener listener) {
		String path = prefs.getNode().node(NODE_NAME).absolutePath();
		WarlockPreferences.getScope().getNode(path).removePreferenceChangeListener(listener);
	}
}
