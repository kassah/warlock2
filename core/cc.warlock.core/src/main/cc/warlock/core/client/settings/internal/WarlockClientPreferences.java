package cc.warlock.core.client.settings.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockHighlight;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockStyle;

public class WarlockClientPreferences {
	protected String clientID;
	protected ConfigurationScope scope;
	protected Preferences clientPrefs;
	
	public WarlockClientPreferences(String clientID) {
		this.clientID = clientID;
		
		scope = new ConfigurationScope();
		IEclipsePreferences toplevel = scope.getNode("cc.warlock");
		clientPrefs = toplevel.node("client/" + clientID);
	}
	
	public void addNodeChangeListener(String path, INodeChangeListener listener) {
		try {
			scope.getNode(path).addNodeChangeListener(listener);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addPreferenceChangeListener(String path, IPreferenceChangeListener listener) {
		try {
			scope.getNode(path).addPreferenceChangeListener(listener);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Preferences getClientPreferences() {
		return clientPrefs;
	}
}
