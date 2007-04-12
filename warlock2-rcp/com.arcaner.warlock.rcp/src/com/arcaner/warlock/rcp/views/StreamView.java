package com.arcaner.warlock.rcp.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineBackgroundEvent;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.arcaner.warlock.client.IProperty;
import com.arcaner.warlock.client.IStream;
import com.arcaner.warlock.client.IStreamListener;
import com.arcaner.warlock.client.IWarlockStyle;
import com.arcaner.warlock.client.PropertyListener;
import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.client.stormfront.WarlockColor;
import com.arcaner.warlock.configuration.ServerSettings;
import com.arcaner.warlock.rcp.ui.StyleRangeWithData;
import com.arcaner.warlock.rcp.ui.WarlockText;
import com.arcaner.warlock.rcp.ui.client.SWTPropertyListener;
import com.arcaner.warlock.rcp.ui.client.SWTStreamListener;
import com.arcaner.warlock.rcp.ui.style.StyleMappings;

public class StreamView extends ViewPart implements IStreamListener, LineBackgroundListener {
	public static final String VIEW_ID = "com.arcaner.warlock.rcp.views.StreamView";
	protected static ArrayList<StreamView> openViews = new ArrayList<StreamView>();
	
	protected IStream stream;
	protected IStormFrontClient client;
	protected WarlockText text;
	protected Hashtable<Integer, Color> lineBackgrounds = new Hashtable<Integer,Color>();
	protected Hashtable<Integer, Color> lineForegrounds = new Hashtable<Integer,Color>();
	protected Composite mainComposite;
	// This name is the 'suffix' part of the stream... so we will install listeners for each client
	protected String streamName;
	protected SWTStreamListener streamListenerWrapper;
	protected SWTPropertyListener<String> propertyListenerWrapper;
	
	public StreamView() {
		openViews.add(this);
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
			StreamView nextInstance = (StreamView) page.showView(VIEW_ID, streamName, IWorkbenchPage.VIEW_ACTIVATE);
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
		text.addLineBackgroundListener(this);
		
		GameView currentGameView = GameView.getViewInFocus();
		if (currentGameView != null && currentGameView.getStormFrontClient() != null)
		{
			setPartName(currentGameView.getStormFrontClient().getStream(streamName).getTitle().get());
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public IStream getStream() {
		return stream;
	}

	public void setStream(IStream stream) {
		this.stream = stream;
		
		streamListenerWrapper = new SWTStreamListener(this);
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
	
	
	public void streamCleared(IStream stream) {
		if (this.stream.equals(stream))
			text.setText("");
	}
	
	private void scrollToBottom ()
	{
		int length = this.text.getContent().getCharCount();		
		if (this.text.getCaretOffset() < length) {
			this.text.setCaretOffset(length);
			this.text.showSelection();
		}
	}
	
	private void applyUserHighlights (StyleRange parentStyle, String text, int start, int length)
	{
		int lineIndex = this.text.getLineAtOffset(start);
		Font font = this.text.getFont();
		if (parentStyle != null)
		{
			if (parentStyle.font != null)
				font = parentStyle.font;
		}
		
		for (String highlight : client.getServerSettings().getHighlightStrings())
		{
			int highlightLength = highlight.length();
			int index = text.indexOf(highlight);
			while (index > -1)
			{
				StyleRangeWithData range = new StyleRangeWithData();
				range.background = createColor(client.getServerSettings().getHighlightBackgroundColor(highlight));
				range.foreground = createColor(client.getServerSettings().getHighlightForegroundColor(highlight));
				range.start = start + index;
				range.length = highlightLength;
				range.font = font;
				
				if (client.getServerSettings().getHighlightFillEntireLine(highlight))
				{
					lineBackgrounds.put(lineIndex, range.background);
					lineForegrounds.put(lineIndex, range.foreground);
				}
				
				this.text.setStyleRange(range);
				index = text.indexOf(highlight, index+1);
			}
		}
	}
	
	public void streamReceivedText(IStream stream, String text, IWarlockStyle style) {
		if (this.stream.equals(stream))
		{
			StyleRangeWithData range = null;
			int start = this.text.getCharCount();
			int length = text.length();
			
			try {
				range = StyleMappings.getStyle(client.getServerSettings(), style, start, length);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.text.append(text);
			int lineIndex = this.text.getLineAtOffset(start);
			
			if (range != null) {
				this.text.setStyleRange(range);
				if (range.data.containsKey(StyleMappings.FILL_ENTIRE_LINE))
				{
					lineBackgrounds.put(lineIndex, range.background);
					lineForegrounds.put(lineIndex, range.foreground);
				}
			}
			
			applyUserHighlights(range, text, start, length);
			scrollToBottom();
		}
	}
	
	public void lineGetBackground(LineBackgroundEvent event) {
		int lineIndex = this.text.getLineAtOffset(event.lineOffset);
		boolean hasBackground = lineBackgrounds.containsKey(lineIndex);
		boolean hasForeground = lineForegrounds.containsKey(lineIndex);
		
		if (hasBackground)
		{
			event.lineBackground = lineBackgrounds.get(lineIndex);
		}
		
//		if (hasForeground)
//		{
//			StyleRange lineRange = new StyleRange();
//			lineRange.start = event.lineOffset;
//			lineRange.length = event.lineText.length();
//			lineRange.foreground = lineForegrounds.get(lineIndex);
//			
//			this.text.setStyleRange(lineRange);
//		}
	}
	
	public void streamEchoed(IStream stream, String text) {
		if (this.stream.equals(stream))
		{
			StyleRange echoStyle = StyleMappings.getEchoStyle(client.getServerSettings(), this.text.getCharCount(), text.length());
			this.text.append(text + "\n");
			this.text.setStyleRange(echoStyle);
			
			scrollToBottom();
		}
	}
	
	public void streamPrompted(IStream stream, String prompt) {
		if (this.stream.equals(stream))
		{
			this.text.append(prompt);
			scrollToBottom();
		}
	}

	public static Collection<StreamView> getOpenViews ()
	{
		return openViews;
	}
	
	private Color createColor (WarlockColor color)
	{
		return new Color(getSite().getShell().getDisplay(), color.getRed(), color.getGreen(), color.getBlue());
	}
	
	public void setClient (IStormFrontClient client)
	{
		this.client = client;
		
		setStream(client.getStream(streamName));
	}
	
	public void loadServerSettings (ServerSettings settings)
	{
		// just inherit from the main window for now
		WarlockColor bg = settings.getColorSetting(ServerSettings.ColorType.MainWindow_Background);
		WarlockColor fg = settings.getColorSetting(ServerSettings.ColorType.MainWindow_Foreground);
		String fontFace = settings.getStringSetting(ServerSettings.StringType.MainWindow_FontFace);
		int fontSize = settings.getIntSetting(ServerSettings.IntType.MainWindow_FontSize);
		
		text.setBackground(createColor(bg));
		text.setForeground(createColor(fg));
		
		Font normalFont = new Font(getSite().getShell().getDisplay(), fontFace, fontSize, SWT.NONE);
		text.setFont(normalFont);
	}
	
	@Override
	public void dispose() {
		stream.removeStreamListener(streamListenerWrapper);
		stream.getTitle().removeListener(propertyListenerWrapper);
		super.dispose();
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}
}
