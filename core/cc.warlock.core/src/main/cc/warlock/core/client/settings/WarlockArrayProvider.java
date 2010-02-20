package cc.warlock.core.client.settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockHighlight;

public abstract class WarlockArrayProvider<T> extends WarlockPreferenceProvider<T> {
	
	public Collection<T> getAll(WarlockClientPreferences prefs) {
		ArrayList<T> results =
			new ArrayList<T>();

		Preferences hnode = prefs.getNode().node(this.getNodeName());
		try {
			for (String name : hnode.childrenNames()) {
				Preferences node = hnode.node(name);
				results.add(get(node));
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}

		return results;
	}
	
	public Collection<WarlockPreference<T>> getAllPreferences(
			WarlockClientPreferences prefs) {
		ArrayList<WarlockPreference<T>> results =
			new ArrayList<WarlockPreference<T>>();

		Preferences hnode = prefs.getNode().node(this.getNodeName());
		try {
			for (String name : hnode.childrenNames()) {
				Preferences node = hnode.node(name);
				results.add(getPreference(node));
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}

		return results;
	}
	
	public Map<String, T> getMap(WarlockClientPreferences prefs) {
		HashMap<String, T> results = new HashMap<String, T>();

		Preferences hnode = prefs.getNode().node(getNodeName());
		try {
			for (String name : hnode.childrenNames()) {
				Preferences node = hnode.node(name);
				results.put(name, get(node));
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}

		return results;
	}
	
	public Map<String, WarlockPreference<T>> getPreferenceMap(WarlockClientPreferences prefs) {
		HashMap<String, WarlockPreference<T>> results =
			new HashMap<String, WarlockPreference<T>>();

		Preferences hnode = prefs.getNode().node(getNodeName());
		try {
			for (String name : hnode.childrenNames()) {
				Preferences node = hnode.node(name);
				results.put(name, getPreference(node));
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}

		return results;
	}
	
	public String add(WarlockClientPreferences prefs, T highlight) {
		Preferences hnode = prefs.getNode().node(getNodeName());
		
		int max = 0;
		try {
			for (String name : hnode.childrenNames()) {
				int cur = Integer.parseInt(name);
				if(cur > max)
					max = cur;
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
		String name = Integer.toString(max + 1);
		put(hnode.node(name), highlight);
		return name;
	}
}
