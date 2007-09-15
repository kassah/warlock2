package cc.warlock.client;

public interface IWarlockClientListener {

	public void clientActivated (IWarlockClient client);
	public void clientConnected (IWarlockClient client);
	
	public void clientDisconnected (IWarlockClient client);
	public void clientRemoved (IWarlockClient client);
}
