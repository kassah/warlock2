package cc.warlock.core.client.internal;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;

public abstract class WarlockClientListener implements IWarlockClientListener {

	public abstract void clientActivated(IWarlockClient client);

	public abstract void clientConnected(IWarlockClient client);

	public abstract void clientDisconnected(IWarlockClient client);

	public abstract void clientRemoved(IWarlockClient client);

	public abstract void clientSettingsLoaded(IWarlockClient client);

}
