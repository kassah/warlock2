/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.client;

/**
 * @author Sean Proctor
 */
public interface IPropertyListener {
	
	/**
	 * this method is called when the value of the property has changed
	 */
	public void propertyChanged();
	
	/**
	 * this method is called when the property is cleared
	 */
	public void propertyCleared();
	
	/**
	 * this method is called when the property is activated
	 */
	public void propertyActivated();
	
}
