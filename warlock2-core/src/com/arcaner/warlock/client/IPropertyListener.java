/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.client;

/**
 * @author Sean Proctor
 */
public interface IPropertyListener<T> {
	
	/**
	 * this method is called when the value of the property has changed
	 */
	public void propertyChanged(IProperty<T> property, T oldValue);
	
	/**
	 * this method is called when the property is cleared
	 */
	public void propertyCleared(IProperty<T> property, T oldValue);
	
	/**
	 * this method is called when the property is activated
	 */
	public void propertyActivated(IProperty<T> property);
	
}
