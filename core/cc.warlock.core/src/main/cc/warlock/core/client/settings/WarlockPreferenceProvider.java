package cc.warlock.core.client.settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public abstract class WarlockPreferenceProvider<T> {
	WarlockPreference<T> getPreference(Preferences node) {
		return new WarlockPreference<T>(this, node.absolutePath(), get(node));
	}
	
	protected abstract T get(Preferences node);
	
	public T get(WarlockClientPreferences prefs, String id) {
		return get(getNode(prefs, id));
	}
	
	protected Preferences getNode(WarlockClientPreferences prefs, String id) {
		return prefs.getNode().node(getNodeName()).node(id);
	}
	
	public WarlockPreference<T> getPreference(WarlockClientPreferences prefs,
			String id) {
		return getPreference(getNode(prefs, id));
	}
	
	protected abstract String getNodeName();
	
	public void set(String path, T value) {
		set(WarlockPreferences.getScope().getNode(path), value);
	}
	
	public void set(WarlockClientPreferences prefs, String id, T value) {
		set(getNode(prefs, id), value);
	}
	
	protected abstract void set(Preferences node, T value);
	
	public void remove(WarlockClientPreferences prefs, String id) {
		try {
			prefs.getNode().node(getNodeName()).node(id).removeNode();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
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
	
	public void addNodeChangeListener(WarlockClientPreferences prefs,
			INodeChangeListener listener) {
		String path = prefs.getNode().node(getNodeName()).absolutePath();
		WarlockPreferences.getScope().getNode(path).addNodeChangeListener(listener);
	}
	
	public void addPreferenceChangeListener(WarlockClientPreferences prefs,
			IPreferenceChangeListener listener) {
		String path = prefs.getNode().node(getNodeName()).absolutePath();
		WarlockPreferences.getScope().getNode(path).addPreferenceChangeListener(listener);
	}
	
	public void removeNodeChangeListener(WarlockClientPreferences prefs,
			INodeChangeListener listener) {
		String path = prefs.getNode().node(getNodeName()).absolutePath();
		WarlockPreferences.getScope().getNode(path).removeNodeChangeListener(listener);
	}
	
	public void removePreferenceChangeListener(WarlockClientPreferences prefs,
			IPreferenceChangeListener listener) {
		String path = prefs.getNode().node(getNodeName()).absolutePath();
		WarlockPreferences.getScope().getNode(path).removePreferenceChangeListener(listener);
	}
}
