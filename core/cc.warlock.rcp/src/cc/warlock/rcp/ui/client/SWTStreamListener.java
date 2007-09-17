package cc.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IStyledString;
import cc.warlock.core.client.IWarlockStyle;

public class SWTStreamListener implements IStreamListener {
	
	private IStreamListener listener;
	private ListenerWrapper wrapper = new ListenerWrapper();
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
		Cleared, ReceivedText, Echoed, Prompted
	};
	
	private class ListenerWrapper implements Runnable
	{
		public IStream stream;
		public String text;
		public IStyledString string;
		public IWarlockStyle style;
		public EventType eventType;
		
		public void run() {
			switch (eventType)
			{
			case Cleared: listener.streamCleared(stream); break;
			case ReceivedText: listener.streamReceivedText(stream, string); break;
			case Echoed: listener.streamEchoed(stream, text); break;
			case Prompted: listener.streamPrompted(stream, text); break;
			}
			
			text = null;
			style = null;
			stream = null;
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
		wrapper.eventType = EventType.Cleared;
		wrapper.stream = stream;
		run(wrapper);
	}

	public void streamReceivedText(IStream stream, IStyledString string) {
		wrapper.stream = stream;
		wrapper.eventType = EventType.ReceivedText;
		wrapper.string = string;
		run(wrapper);
	}
	
	public void streamEchoed(IStream stream, String text) {
		wrapper.stream = stream;
		wrapper.text = text;
		wrapper.eventType = EventType.Echoed;
		run(wrapper);
	}
	
	public void streamPrompted(IStream stream, String prompt) {
		wrapper.stream = stream;
		wrapper.text = prompt;
		wrapper.eventType = EventType.Prompted;
		run(wrapper);
	}

	public void streamDonePrompting (IStream stream) { }
}
