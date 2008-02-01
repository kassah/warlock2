/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.core.client.internal;

import java.util.ArrayList;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IPropertyListener;

public class Property<T> implements IProperty<T> {

	protected String name;
	protected T data;
	protected ArrayList<IPropertyListener<T>> listeners = new ArrayList<IPropertyListener<T>>();
	
	public Property(String name, T value) {
		this.name = name;
		this.data = value;
	}
	
	public void set(T data) {
		T oldData = this.data;
		
		this.data = data;
		for(IPropertyListener<T> listener : listeners) {
			listener.propertyChanged(this, oldData);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public T get() {
		return data;
	}
	
	public void clear() {
		T oldData = this.data;
		data = null;
		
		for(IPropertyListener<T> listener : listeners) {
			listener.propertyCleared(this, oldData);
		}
	}
	
	public void activate() {
		for (IPropertyListener<T> listener : listeners) {
			listener.propertyActivated(this);
		}
	}
	
	public void addListener(IPropertyListener<T> listener) {
		listeners.add(listener);
	}
	
	public boolean removeListener(IPropertyListener<T> listener) {
		return listeners.remove(listener);
	}
	
	@Override
	public String toString() {
		return getName() + "=" + get();
	}
}
