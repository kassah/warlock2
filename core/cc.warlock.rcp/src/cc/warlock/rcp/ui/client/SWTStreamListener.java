package cc.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockStyle;

public class SWTStreamListener implements IStreamListener {
	
	private IStreamListener listener;
	protected boolean asynch;
	
	public SWTStreamListener (IStreamListener listener)
	{
		this(listener, false);
	}
	
	public SWTStreamListener (IStreamListener listener, boolean asynch)
	{
		this.listener = listener;
		this.asynch = asynch;
	}
	
	private static enum EventType {
		Cleared, ReceivedText, ReceivedStyle, Echoed, Prompted
	};
	
	private class ListenerWrapper implements Runnable
	{
		public IStream stream;
		public String text;
		public IWarlockStyle style;
		public EventType eventType;
		
		public void run() {
			switch (eventType)
			{
			case Cleared: listener.streamCleared(stream); break;
			case ReceivedText: listener.streamReceivedText(stream, text); break;
			case ReceivedStyle: listener.streamReceivedStyle(stream, style); break;
			case Echoed: listener.streamEchoed(stream, text); break;
			case Prompted: listener.streamPrompted(stream, text); break;
			}
		}
	}
	
	protected void run(Runnable runnable)
	{
		if (asynch)
		{
			Display.getDefault().asyncExec(runnable);
		} else {
			Display.getDefault().syncExec(runnable);
		}
	}
	
	public void streamCleared(IStream stream) {
		ListenerWrapper wrapper = new ListenerWrapper();
		wrapper.eventType = EventType.Cleared;
		wrapper.stream = stream;
		run(wrapper);
	}

	public void streamReceivedText(IStream stream, String text) {
		ListenerWrapper wrapper = new ListenerWrapper();
		wrapper.stream = stream;
		wrapper.eventType = EventType.ReceivedText;
		wrapper.text = text;
		run(wrapper);
	}
	
	public void streamReceivedStyle(IStream stream, IWarlockStyle style) {
		ListenerWrapper wrapper = new ListenerWrapper();
		wrapper.stream = stream;
		wrapper.eventType = EventType.ReceivedStyle;
		wrapper.style = style;
		run(wrapper);
	}
	
	public void streamEchoed(IStream stream, String text) {
		ListenerWrapper wrapper = new ListenerWrapper();
		wrapper.stream = stream;
		wrapper.text = text;
		wrapper.eventType = EventType.Echoed;
		run(wrapper);
	}
	
	public void streamPrompted(IStream stream, String prompt) {
		ListenerWrapper wrapper = new ListenerWrapper();
		wrapper.stream = stream;
		wrapper.text = prompt;
		wrapper.eventType = EventType.Prompted;
		run(wrapper);
	}

	public void streamDonePrompting (IStream stream) { }
}
