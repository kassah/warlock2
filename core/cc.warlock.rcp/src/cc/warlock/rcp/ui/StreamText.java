package cc.warlock.rcp.ui;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockHighlight;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockFont;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.WarlockStringMarker;
import cc.warlock.core.client.internal.Property;
import cc.warlock.core.client.settings.WarlockFontProvider;
import cc.warlock.core.client.settings.WarlockWindowProvider;
import cc.warlock.rcp.ui.style.StyleProviders;
import cc.warlock.rcp.util.ColorUtil;
import cc.warlock.rcp.util.FontUtil;
import cc.warlock.rcp.util.SoundPlayer;
import cc.warlock.rcp.views.GameView;

public class StreamText extends WarlockText implements IStreamListener {

	protected IWarlockClient client;
	protected String streamName;
	protected IStream stream;
	protected boolean isPrompting = false;
	protected String prompt = null;
	protected Property<String> title = new Property<String>();
	
	private WarlockString textBuffer;
	
	public StreamText(Composite parent, String streamName) {
		super(parent);
		this.streamName = streamName;
	}
	
	public void setFocus() {
		// Nothing to do
	}
	
	protected synchronized void appendText(WarlockString string) {
		highlightText(string);
		append(string);
	}
	
	protected void highlightText (WarlockString text)
	{
		// TODO: Highlights should be cached
		for (IWarlockHighlight highlight : client.getHighlights())
		{
			Pattern p;
			try {
				p = highlight.getPattern();
			} catch(PatternSyntaxException e) {
				continue;
			}
			if(p == null)
				continue;
			Matcher matcher = p.matcher(text.toString());
			
			while (matcher.find())
			{
				MatchResult result = matcher.toMatchResult();
				
				IWarlockStyle style = highlight.getStyle();
				text.addStyle(WarlockStringMarker.Type.BEGIN, style, result.start());
				text.addStyle(WarlockStringMarker.Type.END, style, result.end());
				
				try{
					if (style.getSound() != null && !style.getSound().equals("")){
						//System.out.println("Playing sound " + style.getSound());
						SoundPlayer.play(style.getSound());
						//InputStream soundStream = new FileInputStream(style.getSound());
						//RCPUtil.playSound(soundStream);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	protected synchronized void bufferText (WarlockString string)
	{
		if(textBuffer == null) {
			textBuffer = new WarlockString();
		}

		textBuffer.append(string);
	}
	
	public Property<String> getTitle() {
		return title;
	}
	
	public void streamCreated(IStream stream) {
		this.stream = stream;
		this.title.set(stream.getFullTitle());
	}
	
	public void componentUpdated(IStream stream, String id, WarlockString value) {
		replaceMarker(id, value);
	}

	public void streamCleared(IStream stream) {
		clearText();
	}

	public void streamEchoed(IStream stream, String text) {
		IWarlockClient client = stream.getClient();
		WarlockString string = new WarlockString();
		if(isPrompting) {
			string.append("\n");
			isPrompting = false;
		}
		IWarlockStyle style = client.getCommandStyle();
		string.addStyle(WarlockStringMarker.Type.BEGIN, style);
		string.append(text);
		// TODO: make a different style for client messages
		string.addStyle(WarlockStringMarker.Type.END, style);

		appendText(string);
	}

	public void streamFlush(IStream stream) {
		flushBuffer();
	}

	private void flushBuffer() {
		if(textBuffer != null) {
			appendText(textBuffer);
			textBuffer.clear();
			textBuffer = null;
		}
	}
	
	private void showPrompt(String prompt) {
		if(prompt != null
				&& client.hasSuppressPrompt()) {
			WarlockString text = new WarlockString();
			text.append(prompt);
			appendText(text);
		}
	}
	
	public void streamPrompted(IStream stream, String prompt) {
		if(!isPrompting)
		{
			isPrompting = true;
			
			flushBuffer();
			
			showPrompt(prompt);
		} else {
			// if the new prompt is the same as the old one, do nothing.
			// if the new prompt is null, just print the newline.
			if(prompt == null) {
				if(this.prompt != null)
					appendText(new WarlockString("\n"));
			} else if(this.prompt == null || !this.prompt.equals(prompt)) {
				appendText(new WarlockString("\n" + prompt));
			}	
		}
		this.prompt = prompt;
	}

	public void streamReceivedCommand(IStream stream, ICommand command) {
		IWarlockClient client = stream.getClient();
		WarlockString string = new WarlockString(command.getText());
		
		string.addStyle(client.getCommandStyle());
		
		if(!isPrompting && prompt != null)
			appendText(new WarlockString(prompt));
		
		appendText(string);
	}

	public void streamReceivedText(IStream stream, WarlockString text) {
		WarlockString string = new WarlockString();
		
		if (isPrompting) {
			string.append("\n");
			isPrompting = false;
		}
			
		string.append(text);
			
		bufferText(string);
	}
	
	public void streamTitleChanged(IStream stream, String title) {
		this.title.set(stream.getFullTitle());
	}

	public void setClient(IWarlockClient client) {
		this.client = client;
		
		GameView game = GameView.getGameViewForClient(client);
		if (game == null) {
			System.out.println("Couldn't find a gameview for this client! This view won't be setup to send keys over.");
		} else {
			this.getTextWidget().addVerifyKeyListener(game.getWarlockEntry().new KeyVerifier());
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().addKeyListener(game.getWarlockEntry().new KeyEventListener());
		}

		stream = client.getStream(streamName);
		
		if(stream != null)
			this.title.set(stream.getFullTitle());
		
		IStyleProvider styleProvider = StyleProviders.getStyleProvider(client);
		if(styleProvider != null)
			setStyleProvider(styleProvider);

		// FIXME: make the following lines read from a default config
		Color background = new Color(Display.getDefault(), 0, 0, 0);
		Color foreground = new Color(Display.getDefault(), 192, 192, 192);
		
		WarlockFont font = WarlockFontProvider.getInstance().get(client.getClientPreferences(), "default");
		
		// TODO: add a preference change listener here
		IWarlockStyle wsettings = WarlockWindowProvider.getInstance().get(client.getClientPreferences(), streamName);
		if (wsettings != null) {
			if (!wsettings.getBackgroundColor().isDefault())
				background = ColorUtil.warlockColorToColor(wsettings.getBackgroundColor());
			if (!wsettings.getForegroundColor().isDefault())
				foreground = ColorUtil.warlockColorToColor(wsettings.getForegroundColor());
			if (!wsettings.getFont().isDefaultFont())
				font = wsettings.getFont();
		}
		
		this.setBackground(background);
		this.setForeground(foreground);

		this.setFont(FontUtil.warlockFontToFont(font));
	}
}
