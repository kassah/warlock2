package com.arcaner.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import com.arcaner.warlock.client.IPropertyListener;

public class SWTPropertyListener implements IPropertyListener {

	private IPropertyListener listener;
	private ListenerWrapper wrapper;
	
	public SWTPropertyListener (IPropertyListener listener)
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
		
		public void run ()
		{
			switch (eventType)
			{
				case Activated: listener.propertyActivated(); break;
				case Changed: listener.propertyChanged(); break;
				case Cleared: listener.propertyCleared(); break;
			}
		}
	}
	
	public void propertyActivated() {
		wrapper.eventType = EventType.Activated;
		Display.getDefault().syncExec(wrapper);
	}

	public void propertyChanged() {
		wrapper.eventType = EventType.Changed;
		Display.getDefault().syncExec(wrapper);
	}

	public void propertyCleared() {
		wrapper.eventType = EventType.Cleared;
		Display.getDefault().syncExec(wrapper);
	}

}
