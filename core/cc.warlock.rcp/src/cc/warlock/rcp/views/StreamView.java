package cc.warlock.rcp.views;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IStyledString;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.PropertyListener;
import cc.warlock.rcp.ui.IStyleProvider;
import cc.warlock.rcp.ui.StyleRangeWithData;
import cc.warlock.rcp.ui.WarlockText;
import cc.warlock.rcp.ui.client.SWTPropertyListener;
import cc.warlock.rcp.ui.client.SWTStreamListener;
import cc.warlock.rcp.ui.style.DefaultStyleProvider;

public class StreamView extends ViewPart implements IStreamListener {
	
	public static final String STREAM_VIEW_PREFIX = "cc.warlock.rcp.views.stream.";
	
	protected static ArrayList<StreamView> openViews = new ArrayList<StreamView>();
	
	protected IStream mainStream;
	protected ArrayList<IStream> streams;
	protected IWarlockClient client;
	protected WarlockText text;
	protected Composite mainComposite;
	// This name is the 'suffix' part of the stream... so we will install listeners for each client
	protected String mainStreamName;
	protected SWTStreamListener streamListenerWrapper;
	protected SWTPropertyListener<String> propertyListenerWrapper;
	protected boolean appendNewlines = false;
	protected boolean isPrompting = false;
	protected IStyleProvider styleProvider;
	
	public StreamView() {
		openViews.add(this);
		streamListenerWrapper = new SWTStreamListener(this);
		styleProvider = new DefaultStyleProvider();
		streams = new ArrayList<IStream>();
	}

	public static StreamView getViewForStream (String streamName) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		for (StreamView view : openViews)
		{
			if (view.getStreamName().equals(streamName))
			{
				page.activate(view);
				return view;
			}
		}
		
		// none of the already created views match, create a new one
		try {
			StreamView nextInstance = (StreamView) page.showView(STREAM_VIEW_PREFIX + streamName);
			nextInstance.setStreamName(streamName);
			
			return nextInstance;
		} catch (PartInitException e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		mainComposite = new Composite (parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		mainComposite.setLayout(layout);
		
		text = new WarlockText(mainComposite, SWT.V_SCROLL);
		text.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		text.setEditable(false);
		text.setWordWrap(true);
		text.setBackground(new Color(text.getDisplay(), 25, 25, 50));
		text.setForeground(new Color(text.getDisplay(), 240, 240, 255));
		text.setScrollDirection(SWT.DOWN);
		
//		GameView currentGameView = GameView.getViewInFocus();
//		if (currentGameView != null && currentGameView.getWarlockClient() != null)
//		{
//			setPartName(currentGameView.getWarlockClient().getStream(mainStreamName).getTitle().get());
//		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public IStream getMainStream() {
		return mainStream;
	}

	public void setMainStream(IStream stream) {
		this.mainStream = stream;
		
		stream.addStreamListener(streamListenerWrapper);
		propertyListenerWrapper = new SWTPropertyListener<String>(new PropertyListener<String>() {
			@Override
			public void propertyChanged(IProperty<String> property, String oldValue) {
				if (property.getName().equals("streamTitle"))
				{
					setPartName(property.get());
				}
			}
		});
		stream.getTitle().addListener(propertyListenerWrapper);
	}
	
	public void addStream (IStream stream) {
		stream.addStreamListener(streamListenerWrapper);
		streams.add(stream);
	}
	
	public void streamCleared(IStream stream) {
		if (this.mainStream.equals(stream))
			text.setText("");
	}
	
	public void streamReceivedText(IStream stream, IStyledString string) {
		if (this.mainStream.equals(stream) || this.streams.contains(stream))
		{
			if (isPrompting) {
				this.text.append("\n");
				isPrompting = false;
			}
			
			String streamText = string.getBuffer().toString();
			
			if (appendNewlines)
				streamText += "\n";
			
			this.text.append(streamText);
			
			int charCount = this.text.getCharCount() - streamText.length();
			StyleRangeWithData ranges[] = new StyleRangeWithData[string.getStyles().size()];
			int i = 0;
			for (IWarlockStyle style : string.getStyles())
			{
				ranges[i] = styleProvider.getStyleRange(style, charCount + style.getStart(), style.getLength());
				if (style.getStyleTypes().contains(IWarlockStyle.StyleType.LINK))
				{
					ranges[i].data.put("link.url", style.getLinkAddress().toString());
					// skip any leading spaces
					int j = 0;
					while (string.getBuffer().charAt(j) == ' ') j++;
					ranges[i].start = ranges[i].start + j;
				}
				i++;
			}
			
			boolean userHighlightsApplied = false;
			
			for (StyleRangeWithData range : ranges)
			{
				if (range != null) {
					int lineIndex = this.text.getLineAtOffset(range.start);
					this.text.setStyleRange(range);
					
					styleProvider.applyStyles(this.text, range, streamText, range.start, lineIndex);
					userHighlightsApplied = true;
				}
			}
			
			if (!userHighlightsApplied) {
				styleProvider.applyStyles(this.text, null, streamText, charCount, this.text.getLineAtOffset(charCount));
			}
		}
	}
		
	public void streamEchoed(IStream stream, String text) {
		if (this.mainStream.equals(stream) || this.streams.contains(stream))
		{
			isPrompting = false;
			
			this.text.append(text + "\n");
			
			StyleRange echoStyle = styleProvider.getEchoStyle(this.text.getCharCount() - text.length() - 1, text.length());
			this.text.setStyleRange(echoStyle);
		}
	}
	
	public void streamPrompted(IStream stream, String prompt) {
		if (!isPrompting && (this.mainStream.equals(stream) || this.streams.contains(stream)))
		{
			isPrompting = true;
			this.text.append(prompt);
		}
	}
	
	public void streamDonePrompting (IStream stream) {
		isPrompting = false;
	}

	public static Collection<StreamView> getOpenViews ()
	{
		return openViews;
	}
	
	public void setClient (IWarlockClient client)
	{
		this.client = client;
		
		setMainStream(client.getStream(mainStreamName));
	}
	
	@Override
	public void dispose() {
		if (mainStream != null) {
			mainStream.removeStreamListener(streamListenerWrapper);
			mainStream.getTitle().removeListener(propertyListenerWrapper);
		}
		
		for (IStream stream : streams)
		{
			stream.removeStreamListener(streamListenerWrapper);
		}
		
		super.dispose();
	}
	
	public String getStreamName() {
		return mainStreamName;
	}

	public void setStreamName(String streamName) {
		this.mainStreamName = streamName;
	}

	public void setAppendNewlines(boolean appendNewlines) {
		this.appendNewlines = appendNewlines;
	}
	
	public void setViewTitle (String title)
	{
		setPartName(title);
	}
	
	public void setStyleProvider (IStyleProvider provider)
	{
		this.styleProvider = provider;
	}
}
