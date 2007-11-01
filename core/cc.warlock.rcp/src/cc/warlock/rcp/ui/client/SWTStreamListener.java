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
	
	private class ClearedWrapper implements Runnable
	{
		private IStream stream;
		
		public ClearedWrapper(IStream stream) {
			this.stream = stream;
		}
		
		public void run() {
			listener.streamCleared(stream);
		}
	}
	
	private class ReceivedTextWrapper implements Runnable
	{
		private IStream stream;
		private String text;
		
		public ReceivedTextWrapper(IStream stream, String text) {
			this.stream = stream;
			this.text = text;
		}
		
		public void run() {
			listener.streamReceivedText(stream, text);
		}
	}
	
	private class AddedStyleWrapper implements Runnable
	{
		private IStream stream;
		private IWarlockStyle style;
		
		public AddedStyleWrapper(IStream stream, IWarlockStyle style) {
			this.stream = stream;
			this.style = style;
		}
		
		public void run() {
			listener.streamAddedStyle(stream, style);
		}
	}
	
	private class RemovedStyleWrapper implements Runnable
	{
		private IStream stream;
		private IWarlockStyle style;
		
		public RemovedStyleWrapper(IStream stream, IWarlockStyle style) {
			this.stream = stream;
			this.style = style;
		}
		
		public void run() {
			listener.streamRemovedStyle(stream, style);
		}
	}
	
	private class EchoedWrapper implements Runnable
	{
		private IStream stream;
		private String text;
		
		public EchoedWrapper(IStream stream, String text) {
			this.stream = stream;
			this.text = text;
		}
		
		public void run() {
			listener.streamEchoed(stream, text);
		}
	}
	
	private class PromptedWrapper implements Runnable
	{
		private IStream stream;
		private String text;
		
		public PromptedWrapper(IStream stream, String text) {
			this.stream = stream;
			this.text = text;
		}
		
		public void run() {
			listener.streamPrompted(stream, text);
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
		run(new ClearedWrapper(stream));
	}

	public void streamReceivedText(IStream stream, String text) {
		run(new ReceivedTextWrapper(stream, text));
	}
	
	public void streamAddedStyle(IStream stream, IWarlockStyle style) {
		run(new AddedStyleWrapper(stream, style));
	}
	
	public void streamRemovedStyle(IStream stream, IWarlockStyle style) {
		run(new RemovedStyleWrapper(stream, style));
	}
	
	public void streamEchoed(IStream stream, String text) {
		run(new EchoedWrapper(stream, text));
	}
	
	public void streamPrompted(IStream stream, String prompt) {
		run(new PromptedWrapper(stream, prompt));
	}

	public void streamDonePrompting (IStream stream) { }
}
