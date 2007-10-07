package cc.warlock.rcp.stormfront.ui.prefs;


import java.util.HashMap;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import cc.warlock.core.client.IWarlockSkin;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.client.StormFrontColor;
import cc.warlock.core.stormfront.serversettings.server.Preset;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;
import cc.warlock.core.stormfront.serversettings.server.WindowSettings;
import cc.warlock.rcp.ui.style.SavedStyles;
import cc.warlock.rcp.ui.style.Style;
import cc.warlock.rcp.util.ColorUtil;
import cc.warlock.rcp.util.FontSelector;


public class PresetsPreferencePage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public static final String PAGE_ID = "cc.warlock.rcp.stormfront.ui.prefs.presets";
	
	private ColorSelector mainBGSelector, mainFGSelector;
	private FontSelector mainFontSelector;
	private ColorSelector bgSelector, fgSelector;
	private StyledText preview;
	private TableViewer presetsTable;
	
	private static String roomNamePreview = "[Riverhaven, Crescent Way]";
	private static String boldPreview = "You also see a Sir Robyn.";
	private static String commandPreview = "say Hello.";
	private static String speechPreview = "You say, \"Hello.\"";
	private static String whisperPreview = "Someone whispers, \"Hi\"";
	private static String thoughtPreview = "Your mind hears Someone thinking, \"hello everyone\"";
	private static String previewText = 
		roomNamePreview + "\n" +
		boldPreview + "\n" +
		"Obvious paths: southeast, west, northwest.\n" +
		">" + commandPreview + "\n"+
		speechPreview + "\n"+
		">\n"+
		whisperPreview +
		">\n" +
		thoughtPreview;

	private StyleRange roomNameStyleRange, boldStyleRange;
	private StyleRange commandStyleRange, speechStyleRange;
	private StyleRange whisperStyleRange, thoughtStyleRange;
	
	private ServerSettings settings;
	private WindowSettings mainWindow;
	private HashMap<String, Preset> presets = new HashMap<String, Preset>();
	
	protected static final HashMap<String, String> presetDescriptions = new HashMap<String, String>();
	static {
		presetDescriptions.put(Preset.PRESET_BOLD, "Bold text");
		presetDescriptions.put(Preset.PRESET_COMMAND, "Sent commands");
		presetDescriptions.put(Preset.PRESET_LINK, "Hyperlinks");
		presetDescriptions.put(Preset.PRESET_ROOM_NAME, "Room names");
		presetDescriptions.put(Preset.PRESET_SELECTED_LINK, "Selected Hyperlinks");
		presetDescriptions.put(Preset.PRESET_SPEECH, "Speech");
		presetDescriptions.put(Preset.PRESET_THOUGHT, "Thoughts");
		presetDescriptions.put(Preset.PRESET_WATCHING, "Watching");
		presetDescriptions.put(Preset.PRESET_WHISPER, "Whispers");
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Composite main = new Composite (parent, SWT.NONE);
		main.setLayout(new GridLayout(3, false));
		main.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		mainBGSelector = colorSelectorWithLabel(main, "Main window background color:");
		mainFGSelector = colorSelectorWithLabel(main, "Main window foreground color:");
		mainFontSelector = fontSelectorWithLabel(main, "Main window font:");
		
		createPresetsTable(main);
		
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
	
	protected class PresetsLabelProvider implements ITableLabelProvider, ITableColorProvider
	{
		public Color getBackground(Object element, int columnIndex) {
			Preset preset = (Preset) element;
			return ColorUtil.warlockColorToColor(preset.getBackgroundColor());
		}

		public Color getForeground(Object element, int columnIndex) {
			Preset preset = (Preset) element;
			return ColorUtil.warlockColorToColor(preset.getForegroundColor());
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			Preset preset = (Preset) element;
			if (presetDescriptions.containsKey(preset.getName()))
				return presetDescriptions.get(preset.getName());
			return "";
		}
		public void addListener(ILabelProviderListener listener) {}
		public void dispose() {}
		public boolean isLabelProperty(Object element, String property) {
			return true;
		}
		public void removeListener(ILabelProviderListener listener) {}
	}
	
	protected void createPresetsTable (Composite main)
	{
		Group presetsGroup = new Group(main, SWT.NONE);
		presetsGroup.setLayout(new GridLayout(6, false));
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
		data.horizontalSpan = 3;
		presetsGroup.setLayoutData(data);
		presetsGroup.setText("Presets");
		
		bgSelector = colorSelectorWithLabel(presetsGroup, "Background color:");
		fgSelector = colorSelectorWithLabel(presetsGroup, "Foreground color:");
		
		presetsTable = new TableViewer(presetsGroup, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		TableColumn column = new TableColumn(presetsTable.getTable(), SWT.NONE, 0);
		column.setWidth(400);
		
		presetsTable.setUseHashlookup(true);
		presetsTable.setColumnProperties(new String[] { "preset" });
		presetsTable.setContentProvider(new ArrayContentProvider());
		presetsTable.setLabelProvider(new PresetsLabelProvider());
		data = new GridData(GridData.FILL, GridData.FILL, true, true);
		data.horizontalSpan = 6;
		presetsTable.getTable().setLayoutData(data);
		
		presetsTable.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				presetSelected((IStructuredSelection)presetsTable.getSelection());
			}
		});
	}
	
	protected Preset currentPreset;
	protected void presetSelected (IStructuredSelection selection)
	{
		currentPreset = (Preset) selection.getFirstElement();
		
		bgSelector.setColorValue(ColorUtil.warlockColorToRGB(currentPreset.getBackgroundColor()));
		fgSelector.setColorValue(ColorUtil.warlockColorToRGB(currentPreset.getForegroundColor()));
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

		StormFrontColor color = new StormFrontColor(ColorUtil.rgbToWarlockColor(newColor));
		
		if (source == bgSelector)
		{
			if (currentPreset != null)
				currentPreset.setBackgroundColor(color);
		}
		else if (source == fgSelector)
		{
			if (currentPreset != null)
				currentPreset.setForegroundColor(color);
		}
		else if (source == mainBGSelector)
		{
			mainWindow.setBackgroundColor(color);
		}
		else if (source == mainFGSelector)
		{
			mainWindow.setForegroundColor(color);
		}
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
			this.settings = client.getServerSettings();
		}
	}
	
	private void initValues ()
	{
		if (settings != null)
		{
			mainWindow = new WindowSettings(settings.getMainWindowSettings());
			
			for (Preset preset : settings.getPresets())
			{
				presets.put(preset.getName(), new Preset(preset));
			}
			
			mainBGSelector.setColorValue(
				ColorUtil.warlockColorToRGB(settings.getMainWindowSettings().getBackgroundColor()));
			
			mainFGSelector.setColorValue(
				ColorUtil.warlockColorToRGB(settings.getMainWindowSettings().getForegroundColor()));
			
			mainFontSelector.setFontData(
				new FontData(
					settings.getMainWindowSettings().getFontFace(),
					settings.getMainWindowSettings().getFontSizeInPoints(),
					0));
			
			presetsTable.setInput(presets.values());
			presetsTable.getTable().setBackground(new Color(getShell().getDisplay(), getColor(mainBGSelector)));
		}
	}
	
	private void initPreview ()
	{
		preview.setText(previewText);
		
		roomNameStyleRange = new StyleRange();
		speechStyleRange = new StyleRange();
		boldStyleRange = new StyleRange();
		commandStyleRange = new StyleRange();
		whisperStyleRange = new StyleRange();
		thoughtStyleRange = new StyleRange();
		
		roomNameStyleRange.start = 0;
		roomNameStyleRange.length = roomNamePreview.length();
		
		speechStyleRange.start = previewText.indexOf(speechPreview);
		speechStyleRange.length = 7;
		
		boldStyleRange.start = previewText.indexOf(boldPreview) + 15;
		boldStyleRange.length = 9;
		
		commandStyleRange.start = previewText.indexOf(commandPreview);
		commandStyleRange.length = commandPreview.length();
		
		whisperStyleRange.start = previewText.indexOf(whisperPreview);
		whisperStyleRange.length = 16;
		
		thoughtStyleRange.start = previewText.indexOf(thoughtPreview);
		thoughtStyleRange.length = thoughtPreview.length();
		
		updatePreview();
	}
	
	private RGB getColor (ColorSelector selector) 	
	{
		return selector.getColorValue() == null ? mainBGSelector.getColorValue() : selector.getColorValue();
	}
	
	private void updatePresetColors (String presetName, StyleRange styleRange)
	{
		styleRange.background = ColorUtil.warlockColorToColor(presets.get(presetName).getBackgroundColor());
		styleRange.foreground = ColorUtil.warlockColorToColor(presets.get(presetName).getForegroundColor());
	}
	
	private void updatePreview ()
	{
		presetsTable.getTable().setBackground(new Color(getShell().getDisplay(), getColor(mainBGSelector)));
		preview.setBackground(new Color(getShell().getDisplay(), getColor(mainBGSelector)));
		preview.setForeground(new Color(getShell().getDisplay(), mainFGSelector.getColorValue()));
		preview.setFont(mainFontSelector.getFont());
		
		updatePresetColors(Preset.PRESET_ROOM_NAME, roomNameStyleRange);
		updatePresetColors(Preset.PRESET_BOLD, boldStyleRange);
		updatePresetColors(Preset.PRESET_COMMAND, commandStyleRange);
		updatePresetColors(Preset.PRESET_SPEECH, speechStyleRange);
		updatePresetColors(Preset.PRESET_WHISPER, whisperStyleRange);
		updatePresetColors(Preset.PRESET_THOUGHT, thoughtStyleRange);
		
		roomNameStyleRange.background = ColorUtil.warlockColorToColor(presets.get(Preset.PRESET_ROOM_NAME).getBackgroundColor());
		roomNameStyleRange.foreground = ColorUtil.warlockColorToColor(presets.get(Preset.PRESET_ROOM_NAME).getForegroundColor());
		
		preview.setStyleRanges(new StyleRange[] { roomNameStyleRange, boldStyleRange, commandStyleRange, speechStyleRange, whisperStyleRange, thoughtStyleRange });
		preview.update();
	}
	
	@Override
	public boolean performOk() {
		
		for (Preset preset : presets.values())
		{
			settings.updatePreset(preset);
		}
		
		settings.savePresets();
		
		if (mainWindow.needsUpdate())
		{
			settings.updateWindowSettings(mainWindow);
			settings.saveWindowSettings();
		}
		
		return true;
	}
}
