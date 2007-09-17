package cc.warlock.core.client.internal;

import cc.warlock.core.client.IWarlockClient;


public class ClientProperty<T> extends Property<T> {

	protected IWarlockClient client;
	
	public ClientProperty(IWarlockClient client, String name, T value) {
		super(name, value);
		this.client = client;
	}
	
	public IWarlockClient getClient() {
		return client;
	}
}
