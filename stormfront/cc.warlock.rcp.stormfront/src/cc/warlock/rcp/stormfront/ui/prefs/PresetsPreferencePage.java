package cc.warlock.rcp.stormfront.ui.prefs;


import java.util.HashMap;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.resource.JFaceResources;
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

import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.client.StormFrontColor;
import cc.warlock.core.stormfront.serversettings.server.Preset;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;
import cc.warlock.core.stormfront.serversettings.server.WindowSettings;
import cc.warlock.rcp.util.ColorUtil;
import cc.warlock.rcp.util.FontSelector;


public class PresetsPreferencePage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public static final String PAGE_ID = "cc.warlock.rcp.stormfront.ui.prefs.presets";
	
	private ColorSelector mainBGSelector, mainFGSelector;
	private FontSelector mainFontSelector;
	private FontSelector columnFontSelector;
	
	private ColorSelector bgSelector, fgSelector;
	private StyledText preview;
	private TableViewer presetsTable;
	
	private static String roomNamePreview = "[Riverhaven, Crescent Way]";
	private static String boldPreview = "You also see a Sir Robyn.";
	private static String commandPreview = "say Hello.";
	private static String speechPreview = "You say, \"Hello.\"";
	private static String whisperPreview = "Someone whispers, \"Hi\"";
	private static String thoughtPreview = "Your mind hears Someone thinking, \"hello everyone\"";
	private static String columnPreview =
		"    Strength : 20           Reflex : 20\n"+
		"     Agility : 20         Charisma : 20\n"+
		"  Discipline : 20           Wisdom : 20\n"+
		"Intelligence : 20          Stamina : 20\n"+
		"       Favors: 10";
	
	private static String previewText = 
		roomNamePreview + "\n" +
		boldPreview + "\n" +
		"Obvious paths: southeast, west, northwest.\n" +
		">" + commandPreview + "\n"+
		speechPreview + "\n"+
		">\n"+
		whisperPreview +
		">\n" +
		thoughtPreview +
		"\n" +
		columnPreview;

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
//		mainFontSelector = fontSelectorWithLabel(main, "Main window font:");
//		columnFontSelector = fontSelectorWithLabel(main, "Column font:");
		
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
	
	protected Color getWorkingBackgroundColor (Preset preset)
	{
		StormFrontColor color = preset.getBackgroundColor();
		if (color.equals(settings.getMainWindowSettings().getBackgroundColor()))
		{
			color = mainWindow.getBackgroundColor();
		}
		return ColorUtil.warlockColorToColor(color);
	}
	
	protected Color getWorkingForegroundColor (Preset preset)
	{
		StormFrontColor color = preset.getForegroundColor();
		if (color.equals(settings.getMainWindowSettings().getForegroundColor()))
		{
			color = mainWindow.getForegroundColor();
		}
		return ColorUtil.warlockColorToColor(color);
	}
	
	protected class PresetsLabelProvider implements ITableLabelProvider, ITableColorProvider
	{
		public Color getBackground(Object element, int columnIndex) {
			Preset preset = (Preset) element;
			return getWorkingBackgroundColor(preset);
		}

		public Color getForeground(Object element, int columnIndex) {
			Preset preset = (Preset) element;
			return getWorkingForegroundColor(preset);
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

	private StyleRange columnStyleRange;
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

		updatePreview();
	}
	
	private void fontChanged (FontSelector source, FontData fontData)
	{
		String fontFace = fontData.getName();
		int fontSize = fontData.getHeight();
		
		if (source == mainFontSelector)
		{
			mainWindow.setFontFace(fontFace);
			mainWindow.setFontSizeInPoints(fontSize);
		}
		else if (source == columnFontSelector)
		{
			mainWindow.setColumnFontFace(fontFace);
			mainWindow.setColumnFontSizeInPoints(fontSize);
		}
		
		updatePreview();
	}
	
	@Override
	public void setElement(IAdaptable element) {
		IStormFrontClient client = (IStormFrontClient) element.getAdapter(IStormFrontClient.class);
		if (client != null)
		{
			this.settings = client.getServerSettings();
		}
	}
	
	private FontData getDefaultFont ()
	{
		String fontFace = settings.getMainWindowSettings().getFontFace();
		FontData datas[] = new FontData[0];
		
		if (fontFace != null)
			datas = getShell().getDisplay().getFontList(fontFace, true);
		
		if (datas.length == 0)
		{
			return JFaceResources.getDefaultFont().getFontData()[0];
		}
		else
		{
			return datas[0];
		}
	}
	
	private FontData getDefaultColumnFont ()
	{
		String fontFace = settings.getMainWindowSettings().getColumnFontFace();
		FontData datas[] = new FontData[0];
		
		if (fontFace != null)
			datas = getShell().getDisplay().getFontList(fontFace, true);
		
		if (datas.length == 0)
		{
			return JFaceResources.getTextFont().getFontData()[0];
		}
		else
		{
			return datas[0];
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
			
//			mainFontSelector.setFontData(getDefaultFont());
//			columnFontSelector.setFontData(getDefaultColumnFont());
			
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
		columnStyleRange = new StyleRange();
		
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
		
		columnStyleRange.start = previewText.indexOf(columnPreview);
		columnStyleRange.length = columnPreview.length();
		
		updatePreview();
	}
	
	private RGB getColor (ColorSelector selector) 	
	{
		return selector.getColorValue() == null ? mainBGSelector.getColorValue() : selector.getColorValue();
	}
	
	private void updatePresetColors (String presetName, StyleRange styleRange)
	{
		styleRange.background = getWorkingBackgroundColor(presets.get(presetName));
		styleRange.foreground = getWorkingForegroundColor(presets.get(presetName));
	}
	
	private void updatePreview ()
	{
		Color mainBG = new Color(getShell().getDisplay(), mainBGSelector.getColorValue());
		Color mainFG = new Color(getShell().getDisplay(), mainFGSelector.getColorValue());
		
		presetsTable.getTable().setBackground(mainBG);
		presetsTable.setInput(presets.values());
		
		preview.setBackground(mainBG);
		preview.setForeground(mainFG);
//		preview.setFont(new Font(getShell().getDisplay(), mainFontSelector.getFontData()));
		
		updatePresetColors(Preset.PRESET_ROOM_NAME, roomNameStyleRange);
		updatePresetColors(Preset.PRESET_BOLD, boldStyleRange);
		updatePresetColors(Preset.PRESET_COMMAND, commandStyleRange);
		updatePresetColors(Preset.PRESET_SPEECH, speechStyleRange);
		updatePresetColors(Preset.PRESET_WHISPER, whisperStyleRange);
		updatePresetColors(Preset.PRESET_THOUGHT, thoughtStyleRange);
		
		roomNameStyleRange.background = ColorUtil.warlockColorToColor(presets.get(Preset.PRESET_ROOM_NAME).getBackgroundColor());
		roomNameStyleRange.foreground = ColorUtil.warlockColorToColor(presets.get(Preset.PRESET_ROOM_NAME).getForegroundColor());
		
		columnStyleRange.background = mainBG;
		columnStyleRange.foreground = mainFG;
//		columnStyleRange.font = new Font(getShell().getDisplay(), columnFontSelector.getFontData());
		
		preview.setStyleRanges(new StyleRange[] { roomNameStyleRange, boldStyleRange, commandStyleRange, speechStyleRange, whisperStyleRange, thoughtStyleRange, columnStyleRange });
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
