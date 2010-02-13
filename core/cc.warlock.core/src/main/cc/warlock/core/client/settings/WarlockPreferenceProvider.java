package cc.warlock.core.client.settings;

public interface WarlockPreferenceProvider<T> {
	public void save(String path, T value);
}
