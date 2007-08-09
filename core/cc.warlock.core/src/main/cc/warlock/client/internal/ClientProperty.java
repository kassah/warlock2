package cc.warlock.client.internal;

import cc.warlock.client.IWarlockClient;


public class ClientProperty<T> extends Property<T> {

	protected IWarlockClient client;
	
	public ClientProperty(IWarlockClient client, String name) {
		super(name);
		this.client = client;
	}
	
	public IWarlockClient getClient() {
		return client;
	}
}
