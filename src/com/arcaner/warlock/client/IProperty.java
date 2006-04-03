package com.arcaner.warlock.client;

public interface IProperty {
	
	/**
	 * For properties with a simple string interface, this will suffice
	 * @param data The property may do whatever it likes with this data
	 */
	public void set(String data);
	
	/**
	 * For properties with a simple string interface, this will suffice
	 * @return the data of the property
	 */
	public String get();
	
	/**
	 * FIXME I don't know if this method is appropriately generic, maybe it should be moved
	 */
	public void clear();
	
	/**
	 * FIXME I don't know if this function is needed either
	 */
	public void activate();
	
	/**
	 * adds a listener to this property
	 * @param listener
	 */
	public void addListener(IPropertyListener listener);
	
	/**
	 * removes the listener
	 * @param listener Listener to remove
	 * @throws IllegalAgumentException if the listener is not listening
	 */
	public boolean removeListener(IPropertyListener listener);
	
	/**
	 * gets the client that this property is a part of
	 * @return the client
	 */
	public IWarlockClient getClient();
}
