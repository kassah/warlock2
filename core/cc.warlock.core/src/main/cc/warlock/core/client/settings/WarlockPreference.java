package cc.warlock.core.client.settings;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;


public class WarlockPreference<T> {
	private WarlockPreferenceProvider<T> provider;
	private T value;
	private String path;
	
	public WarlockPreference(WarlockPreferenceProvider<T> provider, String path, T value) {
		this.provider = provider;
		this.path = path;
		this.value = value;
	}
	
	public T get() {
		return value;
	}
	
	public String getPath() {
		return path;
	}
	
	public void save() {
		provider.save(path, value);
	}
	
	public void addNodeChangeListener(INodeChangeListener listener) {
		WarlockPreferences.addNodeChangeListener(path, listener);
	}
	
	public void addPreferenceChangeListener(IPreferenceChangeListener listener) {
		WarlockPreferences.addPreferenceChangeListener(path, listener);
	}
}
