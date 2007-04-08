package com.arcaner.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import com.arcaner.warlock.client.stormfront.IStormFrontStyle;
import com.arcaner.warlock.stormfront.IStreamListener;

public class SWTStreamListener implements IStreamListener {
	private IStreamListener listener;
	private ListenerWrapper wrapper = new ListenerWrapper();
	
	public SWTStreamListener (IStreamListener listener)
	{
		this.listener = listener;
	}
	
	private static enum EventType {
		Cleared, ReceivedText
	};
	
	private class ListenerWrapper implements Runnable
	{
		public String text;
		public IStormFrontStyle style;
		public EventType eventType;
		
		public void run() {
			switch (eventType)
			{
			case Cleared: listener.streamCleared(); break;
			case ReceivedText: listener.streamReceivedText(text, style); break;
			}
			
			text = null;
			style = null;
		}
	}
	
	public void streamCleared() {
		wrapper.eventType = EventType.Cleared;
		Display.getDefault().syncExec(wrapper);
	}

	public void streamReceivedText(String text, IStormFrontStyle style) {
		wrapper.eventType = EventType.ReceivedText;
		wrapper.text = text;
		wrapper.style = style;
		Display.getDefault().syncExec(wrapper);
	}

}
