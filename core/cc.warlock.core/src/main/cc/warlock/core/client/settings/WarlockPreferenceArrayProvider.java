package cc.warlock.core.client.settings;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public abstract class WarlockPreferenceArrayProvider<T> extends WarlockPreferenceProvider<T> {
	
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
		set(hnode.node(name), highlight);
		return name;
	}
}
