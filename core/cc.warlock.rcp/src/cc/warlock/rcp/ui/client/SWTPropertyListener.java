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
