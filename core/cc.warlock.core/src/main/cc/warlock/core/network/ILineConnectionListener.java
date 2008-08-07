package cc.warlock.core.network;

public interface ILineConnectionListener extends IConnectionListener {

	public void lineReady (IConnection connection, String line);
}
