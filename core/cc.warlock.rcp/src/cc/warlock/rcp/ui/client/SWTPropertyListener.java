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
package cc.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IPropertyListener;

public class SWTPropertyListener<T> implements IPropertyListener<T> {

	private IPropertyListener<T> listener;
	
	public SWTPropertyListener (IPropertyListener<T> listener)
	{
		this.listener = listener;
	}
	
	private class ChangedWrapper implements Runnable
	{
		private IProperty<T> property;
		private T oldValue;
		
		public ChangedWrapper(IProperty<T> p, T o) {
			property = p;
			oldValue = o;
		}
		
		public void run ()
		{
			listener.propertyChanged(property, oldValue);
		}
	}
	
	private class ClearedWrapper implements Runnable
	{
		private IProperty<T> property;
		private T oldValue;
		
		public ClearedWrapper(IProperty<T> p, T o) {
			property = p;
			oldValue = o;
		}
		
		public void run ()
		{
			listener.propertyCleared(property, oldValue);
		}
	}
	
	private class ActivatedWrapper implements Runnable
	{
		private IProperty<T> property;
		
		public ActivatedWrapper(IProperty<T> p) {
			property = p;
		}
		
		public void run ()
		{
			listener.propertyActivated(property);
		}
	}
	
	public void propertyActivated (IProperty<T> property) {
		Display.getDefault().asyncExec(new ActivatedWrapper(property));
	}

	public void propertyChanged (IProperty<T> property, T oldValue) {
		Display.getDefault().asyncExec(new ChangedWrapper(property, oldValue));
	}

	public void propertyCleared (IProperty<T> property, T oldValue) {
		Display.getDefault().asyncExec(new ClearedWrapper(property, oldValue));
	}

}
