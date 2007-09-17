package cc.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IPropertyListener;

public class SWTPropertyListener<T> implements IPropertyListener<T> {

	private IPropertyListener<T> listener;
	private ListenerWrapper wrapper;
	
	public SWTPropertyListener (IPropertyListener<T> listener)
	{
		this.listener = listener;
		this.wrapper = new ListenerWrapper();
	}
	
	private static enum EventType {
		Activated, Changed, Cleared
	};
	
	private class ListenerWrapper implements Runnable
	{
		public EventType eventType;
		public IProperty<T> property;
		public T oldValue;
		
		public void run ()
		{
			switch (eventType)
			{
				case Activated: listener.propertyActivated(property); break;
				case Changed: listener.propertyChanged(property, oldValue); break;
				case Cleared: listener.propertyCleared(property, oldValue); break;
			}
			
			oldValue = null;
			property = null;
		}
	}
	
	public void propertyActivated (IProperty<T> property) {
		wrapper.eventType = EventType.Activated;
		wrapper.property = property;
		Display.getDefault().syncExec(wrapper);
	}

	public void propertyChanged (IProperty<T> property, T oldValue) {
		wrapper.eventType = EventType.Changed;
		wrapper.property = property;
		wrapper.oldValue = oldValue;
		Display.getDefault().syncExec(wrapper);
	}

	public void propertyCleared (IProperty<T> property, T oldValue) {
		wrapper.eventType = EventType.Cleared;
		wrapper.property = property;
		wrapper.oldValue = oldValue;
		Display.getDefault().syncExec(wrapper);
	}

}
