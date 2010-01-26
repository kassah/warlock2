package cc.warlock.rcp.ui;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockFont;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.settings.IHighlightString;
import cc.warlock.core.client.settings.IWindowSettings;
import cc.warlock.rcp.configuration.GameViewConfiguration;
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
	
	private WarlockString textBuffer;
	
	public StreamText(Composite parent, String streamName) {
		super(parent, SWT.V_SCROLL);
		this.streamName = streamName;

		// TODO move this section into WarlockText
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
		this.setLayoutData(data);
		this.setEditable(false);
		this.setWordWrap(true);
		this.getTextWidget().setIndent(1);

		this.setScrollDirection(SWT.DOWN);
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
		for (IHighlightString highlight : client.getClientSettings().getAllHighlightStrings())
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
				int start = result.start();
				int length = result.end() - start;
				
				IWarlockStyle style = highlight.getStyle();
				text.addStyle(start, length, style);
				
				try{
					if (style.getSound() != null && !style.getSound().equals("")){
						//System.out.println("Playing sound " + style.getSound());
						SoundPlayer.getInstance().play(style.getSound(), false);
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
			int styleStart = string.length();
			string.append(text);
			// TODO: make a different style for client messages
			string.addStyle(styleStart, text.length(), client.getCommandStyle());
			
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
		if(!GameViewConfiguration.instance().getSuppressPrompt()) {
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
			
			if(prompt != null)
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
		
		string.addStyle(0, string.length(), client.getCommandStyle());
		
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

	public void setClient(IWarlockClient client) {
		this.client = client;
		
		GameView game = GameView.getGameViewForClient(client);
		if (game == null) {
			System.out.println("Couldn't find a gameview for this client! This view won't be setup to send keys over.");
		} else {
			this.getTextWidget().addVerifyKeyListener(game.getWarlockEntry().new KeyVerifier());
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().addKeyListener(game.getWarlockEntry().new KeyEventListener());
		}

		setStyleProvider(StyleProviders.getStyleProvider(client));
		Color background = ColorUtil.warlockColorToColor(client.getSkin().getMainBackground());
		Color foreground = ColorUtil.warlockColorToColor(client.getSkin().getMainForeground());
		WarlockFont font = client.getClientSettings().getMainWindowSettings().getFont();
		if (!streamName.equals(IWarlockClient.DEFAULT_STREAM_NAME)) {
			IWindowSettings settings = client.getClientSettings().getWindowSettings(streamName);
			if (settings != null) {
				if (!settings.getBackgroundColor().isDefault())
					background = ColorUtil.warlockColorToColor(settings.getBackgroundColor());
				if (!settings.getForegroundColor().isDefault())
					foreground = ColorUtil.warlockColorToColor(settings.getForegroundColor());
				if (!settings.getFont().isDefaultFont())
					font = settings.getFont();
			}
		}
		this.setBackground(background);
		this.setForeground(foreground);
		
		String defaultFontFace = GameViewConfiguration.instance().getDefaultFontFace();
		int defaultFontSize = GameViewConfiguration.instance().getDefaultFontSize();

		if (font.isDefaultFont()) {
			this.setFont(new Font(Display.getDefault(), defaultFontFace, defaultFontSize, SWT.NORMAL));
		} else {
			this.setFont(FontUtil.warlockFontToFont(font));
		}
	}
}