package cc.warlock.rcp.ui.client;

import org.eclipse.core.runtime.IAdapterFactory;

import cc.warlock.core.client.IWarlockClient;

public class WarlockClientAdapterFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		return ((WarlockClientAdaptable)adaptableObject).getAdapter(adapterType);
	}

	public Class[] getAdapterList() {
		return new Class[] { IWarlockClient.class };
	}

}
