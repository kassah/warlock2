package cc.warlock.core.client.settings;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public abstract class WarlockPreferenceArrayProvider<T> extends WarlockPreferenceProvider<T> {
	
	public String add(WarlockPreferences prefs, T value) {
		Preferences hnode = prefs.getNode().node(getNodeName());
		
		int max = 0;
		try {
			for (String name : hnode.childrenNames()) {
				// Wrap this, so if we encounter non-numeric keys, we'll just let them pass through
				try {
					int cur = Integer.parseInt(name);
					if(cur > max)
						max = cur;
				} catch (NumberFormatException e) {
					// Nothing
				}
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
		String name = Integer.toString(max + 1);
		set(hnode.node(name), value);
		return name;
	}
	
	@Override
	public Collection<T> getAll(WarlockPreferences prefs) {
		Preferences hnode = prefs.getNode().node(getNodeName());
		ArrayList<T> arr = new ArrayList<T>();
		try {
			for (String name : hnode.childrenNames()) {
				// Wrap this, so if we encounter non-numeric keys, we'll just let them pass through
				try {
					Integer.parseInt(name); // This will throw an exception if it's a not numeric.
					arr.add(this.get(hnode.node(name)));
				} catch (NumberFormatException e) {
					// Nothing
				}
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
		return arr;
	}
}
