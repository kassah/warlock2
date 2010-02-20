package cc.warlock.core.client.settings;

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
	
	public void save(String path, T value) {
		save(WarlockPreferences.getScope().getNode(path), value);
	}
	
	protected abstract void save(Preferences node, T value);
	
	public void remove(WarlockClientPreferences prefs, String id) {
		try {
			prefs.getNode().node(getNodeName()).node(id).removeNode();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
