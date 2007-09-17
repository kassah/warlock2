package cc.warlock.rcp.stormfront.ui.prefs;


import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import cc.warlock.core.client.IWarlockSkin;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.serversettings.server.Preset;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;
import cc.warlock.rcp.ui.style.SavedStyles;
import cc.warlock.rcp.ui.style.Style;
import cc.warlock.rcp.util.ColorUtil;
import cc.warlock.rcp.util.FontSelector;


public class PresetsPreferencePage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public static final String PAGE_ID = "cc.warlock.rcp.prefs.standardPresets";
	
	private RGB mainBG, mainFG, roomNameBG, roomNameFG, speechBG, speechFG;
	private Font mainFont, roomNameFont, speechFont;
	private ColorSelector mainBGSelector, mainFGSelector, roomNameBGSelector, roomNameFGSelector, speechBGSelector, speechFGSelector;
	private FontSelector mainFontSelector, roomNameFontSelector, speechFontSelector;
	private StyledText preview;
	
	private static String roomNamePreview = "[Riverhaven, Crescent Way]";
	private static String speechPreview = "You say, \"Hello.\"";
	private static String previewText = 
		roomNamePreview + "\n" +
		"Obvious paths: southeast, west, northwest.\n" +
		">\n"+
		speechPreview;

	private StyleRange roomNameStyleRange;
	private StyleRange speechStyleRange;
	
	@Override
	protected Control createContents(Composite parent) {
		Composite main = new Composite (parent, SWT.NONE);
		main.setLayout(new GridLayout(3, false));
		main.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		mainBGSelector = colorSelectorWithLabel(main, "Main window background color:");
		mainFGSelector = colorSelectorWithLabel(main, "Main window foreground color:");
		mainFontSelector = fontSelectorWithLabel(main, "Main window font:");
		
		roomNameBGSelector = colorSelectorWithLabel(main, "Room name background color:");
		roomNameFGSelector = colorSelectorWithLabel(main, "Room name foreground color:");
		roomNameFontSelector = fontSelectorWithLabel(main, "Room name font:");
		
		speechBGSelector = colorSelectorWithLabel(main, "Speech background color:");
		speechFGSelector = colorSelectorWithLabel(main, "Speech foreground color:");
		speechFontSelector = fontSelectorWithLabel(main, "Speech font:");
		
		Group previewGroup = new Group(main, SWT.NONE);
		previewGroup.setText("Preview");
		GridData data = new GridData();
		data.horizontalSpan = 3;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		data.verticalAlignment = SWT.FILL;
		previewGroup.setLayoutData(data);
		previewGroup.setLayout(new GridLayout(1, false));
		
		preview = new StyledText(previewGroup, SWT.BORDER);
		preview.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		initValues();
		initPreview();
		return main;
	}

	private ColorSelector colorSelectorWithLabel (Composite parent, String text)
	{
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
		GridData data = new GridData();
		data.horizontalSpan = 2;
		label.setLayoutData(data);
		
		ColorSelector selector = new ColorSelector(parent);
		selector.addListener(new IPropertyChangeListener () {
			public void propertyChange(PropertyChangeEvent event) {
				colorChanged((ColorSelector)event.getSource(), (RGB)event.getNewValue());
			}
		});
		
		return selector;
	}
	
	private FontSelector fontSelectorWithLabel (Composite parent, String text)
	{
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
		GridData data = new GridData();
		data.horizontalSpan = 2;
		label.setLayoutData(data);
		
		FontSelector selector = new FontSelector(parent);
		selector.addListener(new IPropertyChangeListener () {
			public void propertyChange(PropertyChangeEvent event) {
				fontChanged((FontSelector)event.getSource(), (FontData)event.getNewValue());
			}
		});
		
		return selector;
	}
	
	private void colorChanged (ColorSelector source, RGB newColor)
	{
		updatePreview();
	}
	
	private static enum ColorType { BACKGROUND, FOREGROUND };
	
	private void setStyleColor (String styleName, ColorType colorType, RGB color)
	{
		Style style = SavedStyles.getStyleFromName(styleName);
		if (colorType == ColorType.BACKGROUND)
		{
			style.setBackground(new Color(getShell().getDisplay(), color));
		}
		else
		{
			style.setForeground(new Color(getShell().getDisplay(), color));
		}
	}
	
	private void fontChanged (FontSelector source, FontData fontData)
	{	
		updatePreview();
	}
	
	private void setStyleFont (String styleName, Font font)
	{
		SavedStyles.getStyleFromName(styleName).setFontName(font.toString());
	}
	
	@Override
	public void setElement(IAdaptable element) {
		IStormFrontClient client = (IStormFrontClient) element.getAdapter(IStormFrontClient.class);
		if (client != null)
		{
			ServerSettings settings = client.getServerSettings();
			
			mainBG = ColorUtil.warlockColorToRGB(settings.getColorSetting(IWarlockSkin.ColorType.MainWindow_Background));
			mainFG = ColorUtil.warlockColorToRGB(settings.getColorSetting(IWarlockSkin.ColorType.MainWindow_Foreground));
			mainFont = JFaceResources.getFont(settings.getFontFaceSetting(IWarlockSkin.FontFaceType.MainWindow_FontFace));
			
			Preset roomNamePreset = settings.getPreset(Preset.PRESET_ROOM_NAME);
			roomNameBG = ColorUtil.warlockColorToRGB(roomNamePreset.getBackgroundColor());
			roomNameFG = ColorUtil.warlockColorToRGB(roomNamePreset.getForegroundColor());
			roomNameFont = mainFont;
			
			Preset speechPreset = client.getServerSettings().getPreset(Preset.PRESET_SPEECH);
			speechBG = ColorUtil.warlockColorToRGB(speechPreset.getBackgroundColor());
			speechFG = ColorUtil.warlockColorToRGB(speechPreset.getForegroundColor());
			speechFont = mainFont;
		}
	}
	
	private void initValues ()
	{
		if (mainBG != null)
			mainBGSelector.setColorValue(mainBG);
		if (mainFG != null)
			mainFGSelector.setColorValue(mainFG);
		if (roomNameBG != null)
			roomNameBGSelector.setColorValue(roomNameBG);
		if (roomNameFG != null)
			roomNameFGSelector.setColorValue(roomNameFG);
		if (speechBG != null)
			speechBGSelector.setColorValue(speechBG);
		if (speechFG != null)
			speechFGSelector.setColorValue(speechFG);
	}
	
	private void initPreview ()
	{
		preview.setText(previewText);
		roomNameStyleRange = new StyleRange();
		speechStyleRange = new StyleRange();

		roomNameStyleRange.start = 0;
		roomNameStyleRange.length = roomNamePreview.length();
		speechStyleRange.start = previewText.length() - speechPreview.length();
		speechStyleRange.length = 7;
		
		updatePreview();
	}
	
	private RGB getColor (ColorSelector selector) 	
	{
		return selector.getColorValue() == null ? mainBGSelector.getColorValue() : selector.getColorValue();
	}
	
	private void updatePreview ()
	{
		preview.setBackground(new Color(getShell().getDisplay(), getColor(mainBGSelector)));
		preview.setForeground(new Color(getShell().getDisplay(), mainFGSelector.getColorValue()));
		
		roomNameStyleRange.background = new Color(getShell().getDisplay(), getColor(roomNameBGSelector));
		roomNameStyleRange.foreground = new Color(getShell().getDisplay(), getColor(roomNameFGSelector));
		roomNameStyleRange.font = roomNameFontSelector.getFont();
		
		speechStyleRange.background = new Color(getShell().getDisplay(), getColor(speechBGSelector));
		speechStyleRange.foreground = new Color(getShell().getDisplay(), getColor(speechFGSelector));
		speechStyleRange.font = speechFontSelector.getFont();
		
		preview.setStyleRanges(new StyleRange[] { roomNameStyleRange, speechStyleRange });
		preview.update();
	}
	
	@Override
	public boolean performOk() {
		setStyleFont(SavedStyles.STYLE_MAIN_WINDOW, mainFontSelector.getFont());
		setStyleFont(SavedStyles.STYLE_ROOM_NAME, roomNameFontSelector.getFont());
		setStyleFont(SavedStyles.STYLE_SPEECH, speechFontSelector.getFont());
		
		setStyleColor(SavedStyles.STYLE_MAIN_WINDOW, ColorType.BACKGROUND, mainBGSelector.getColorValue());
		setStyleColor(SavedStyles.STYLE_MAIN_WINDOW, ColorType.FOREGROUND, mainFGSelector.getColorValue());
		setStyleColor(SavedStyles.STYLE_ROOM_NAME, ColorType.BACKGROUND, roomNameBGSelector.getColorValue());
		setStyleColor(SavedStyles.STYLE_ROOM_NAME, ColorType.FOREGROUND, roomNameFGSelector.getColorValue());
		setStyleColor(SavedStyles.STYLE_SPEECH, ColorType.BACKGROUND, speechBGSelector.getColorValue());
		setStyleColor(SavedStyles.STYLE_SPEECH, ColorType.FOREGROUND, speechFGSelector.getColorValue());
		SavedStyles.save();
		
		return true;
	}
}
