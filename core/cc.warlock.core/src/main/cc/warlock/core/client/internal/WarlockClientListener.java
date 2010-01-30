package cc.warlock.core.client.internal;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;

public abstract class WarlockClientListener implements IWarlockClientListener {

	@Override
	public abstract void clientActivated(IWarlockClient client);

	@Override
	public abstract void clientConnected(IWarlockClient client);

	@Override
	public abstract void clientDisconnected(IWarlockClient client);

	@Override
	public abstract void clientRemoved(IWarlockClient client);

	@Override
	public abstract void clientSettingsLoaded(IWarlockClient client);

}
