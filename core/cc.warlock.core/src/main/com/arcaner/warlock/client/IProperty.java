package com.arcaner.warlock.client;

public interface IProperty<T> {
	
	/**
	 * @return The name of this property
	 */
	public String getName();
	
	/**
	 * For properties with a simple string interface, this will suffice
	 * @param data The property may do whatever it likes with this data
	 */
	public void set(T data);
	
	/**
	 * For properties with a simple string interface, this will suffice
	 * @return the data of the property
	 */
	public T get();
	
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
	public void addListener(IPropertyListener<T> listener);
	
	/**
	 * removes the listener
	 * @param listener Listener to remove
	 * @throws IllegalAgumentException if the listener is not listening
	 */
	public boolean removeListener(IPropertyListener<T> listener);
}
