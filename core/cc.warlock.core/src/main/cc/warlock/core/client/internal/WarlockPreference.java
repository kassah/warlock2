package cc.warlock.core.client.internal;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;

import cc.warlock.core.client.settings.internal.WarlockClientPreferences;

public class WarlockPreference<T> {
	private T value;
	private WarlockClientPreferences prefs;
	private String path;
	
	public WarlockPreference(WarlockClientPreferences prefs, String path, T value) {
		this.prefs = prefs;
		this.path = path;
		this.value = value;
	}
	
	public T get() {
		return value;
	}
	
	public void addNodeChangeListener(INodeChangeListener listener) {
		prefs.addNodeChangeListener(path, listener);
	}
	
	public void addPrefenceChangeListener(IPreferenceChangeListener listener) {
		prefs.addPreferenceChangeListener(path, listener);
	}
}
