/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.WarlockFont;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.client.settings.internal.WindowSettings;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.settings.internal.StormFrontClientSettings;
import cc.warlock.core.stormfront.settings.skin.IStormFrontSkin;
import cc.warlock.rcp.stormfront.ui.views.StormFrontGameView;
import cc.warlock.rcp.util.ColorUtil;
import cc.warlock.rcp.util.FontSelector;
import cc.warlock.rcp.util.RCPUtil;

/**
 * 
 * @author marshall
 */
public class PresetsPreferencePage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public static final String PAGE_ID = "cc.warlock.rcp.stormfront.ui.prefs.presets";
	
	private ColorSelector mainBGSelector, mainFGSelector;
	private FontSelector mainFontSelector;
	private FontSelector columnFontSelector;
	
	private ColorSelector bgSelector, fgSelector;
	private StyledText preview;
	private TableViewer stylesTable;
	
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
	
	private StormFrontClientSettings settings;
	private IStormFrontSkin skin;
	private WindowSettings mainWindow;
	private HashMap<String, WarlockStyle> styles = new HashMap<String, WarlockStyle>();
	
	protected static final HashMap<String, String> presetDescriptions = new HashMap<String, String>();
	static {
		presetDescriptions.put(StormFrontClientSettings.PRESET_BOLD, "Bold text");
		presetDescriptions.put(StormFrontClientSettings.PRESET_COMMAND, "Sent commands");
		presetDescriptions.put(StormFrontClientSettings.PRESET_LINK, "Hyperlinks");
		presetDescriptions.put(StormFrontClientSettings.PRESET_ROOM_NAME, "Room names");
		presetDescriptions.put(StormFrontClientSettings.PRESET_SELECTED_LINK, "Selected Hyperlinks");
		presetDescriptions.put(StormFrontClientSettings.PRESET_SPEECH, "Speech");
		presetDescriptions.put(StormFrontClientSettings.PRESET_THOUGHT, "Thoughts");
		presetDescriptions.put(StormFrontClientSettings.PRESET_WATCHING, "Watching");
		presetDescriptions.put(StormFrontClientSettings.PRESET_WHISPER, "Whispers");
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
	
	protected Color getWorkingBackgroundColor (IWarlockStyle style)
	{
		WarlockColor color = style.getBackgroundColor();
		if (color.isDefault())
		{
			if (style.getName() != null) {
				color = settings.getStormFrontClient().getStormFrontSkin().getDefaultBackgroundColor(style.getName());
			}
		}
		if (color.isDefault()) {
			color = ColorUtil.rgbToWarlockColor(mainBGSelector.getColorValue());
		}
		
		return ColorUtil.warlockColorToColor(color);
	}
	
	protected Color getWorkingForegroundColor (IWarlockStyle style)
	{
		WarlockColor color = style.getForegroundColor();
		if (color.isDefault())
		{
			if (style.getName() != null) {
				color = settings.getStormFrontClient().getStormFrontSkin().getDefaultForegroundColor(style.getName());
			}
		}
		if (color.isDefault()) {
			color = ColorUtil.rgbToWarlockColor(mainFGSelector.getColorValue());
		}
		
		return ColorUtil.warlockColorToColor(color);
	}
	
	protected class PresetsLabelProvider implements ITableLabelProvider, ITableColorProvider
	{
		public Color getBackground(Object element, int columnIndex) {
			IWarlockStyle style = (IWarlockStyle) element;
			return getWorkingBackgroundColor(style);
		}

		public Color getForeground(Object element, int columnIndex) {
			IWarlockStyle style = (IWarlockStyle) element;
			return getWorkingForegroundColor(style);
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			IWarlockStyle style = (IWarlockStyle) element;
			if (presetDescriptions.containsKey(style.getName()))
				return presetDescriptions.get(style.getName());
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
		
		stylesTable = new TableViewer(presetsGroup, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		TableColumn column = new TableColumn(stylesTable.getTable(), SWT.NONE, 0);
		column.setWidth(400);
		
		stylesTable.setUseHashlookup(true);
		stylesTable.setColumnProperties(new String[] { "preset" });
		stylesTable.setContentProvider(new ArrayContentProvider());
		stylesTable.setLabelProvider(new PresetsLabelProvider());
		data = new GridData(GridData.FILL, GridData.FILL, true, true);
		data.horizontalSpan = 6;
		stylesTable.getTable().setLayoutData(data);
		
		stylesTable.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				presetSelected((IStructuredSelection)stylesTable.getSelection());
			}
		});
	}
	
	protected IWarlockStyle currentStyle;

	private StyleRange columnStyleRange;
	protected void presetSelected (IStructuredSelection selection)
	{
		currentStyle = (IWarlockStyle) selection.getFirstElement();
		
		bgSelector.setColorValue(getWorkingBackgroundColor(currentStyle).getRGB());
		fgSelector.setColorValue(getWorkingForegroundColor(currentStyle).getRGB());
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

		WarlockColor color = new WarlockColor(ColorUtil.rgbToWarlockColor(newColor));
		
		if (source == bgSelector)
		{
			if (currentStyle != null)
				currentStyle.setBackgroundColor(color);
		}
		else if (source == fgSelector)
		{
			if (currentStyle != null)
				currentStyle.setForegroundColor(color);
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
		WarlockFont font = new WarlockFont();
		font.setFamilyName(fontData.getName());
		font.setSize(RCPUtil.getPointSizeInPixels(fontData.getHeight()));
		
		if (source == mainFontSelector)
		{
			mainWindow.setFont(font);
		}
		else if (source == columnFontSelector)
		{
			mainWindow.setColumnFont(font);
		}
		
		updatePreview();
	}
	
	@Override
	public void setElement(IAdaptable element) {
		IStormFrontClient client = (IStormFrontClient) element.getAdapter(IStormFrontClient.class);
		if (client != null)
		{
			this.settings = (StormFrontClientSettings) client.getStormFrontClientSettings();
			this.skin = this.settings.getStormFrontClient().getStormFrontSkin();
		}
	}
	
	private FontData getDefaultFont ()
	{
		String fontFace = settings.getMainWindowSettings().getFont().getFamilyName();
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
		String fontFace = settings.getMainWindowSettings().getColumnFont().getFamilyName();
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
			mainWindow = new WindowSettings((WindowSettings)settings.getMainWindowSettings());
			
			for (IWarlockStyle style: settings.getHighlightConfigurationProvider().getNamedStyles())
			{
				styles.put(style.getName(), new WarlockStyle(style));
			}
			
			mainBGSelector.setColorValue(
				ColorUtil.warlockColorToRGB(skin.getMainBackground()));
			
			mainFGSelector.setColorValue(
				ColorUtil.warlockColorToRGB(skin.getMainForeground()));
			
//			mainFontSelector.setFontData(getDefaultFont());
//			columnFontSelector.setFontData(getDefaultColumnFont());
			
			stylesTable.setInput(styles.values());
			stylesTable.getTable().setBackground(new Color(getShell().getDisplay(), getColor(mainBGSelector)));
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
		styleRange.background = getWorkingBackgroundColor(styles.get(presetName));
		styleRange.foreground = getWorkingForegroundColor(styles.get(presetName));
	}
	
	private void updatePreview ()
	{
		Color mainBG = new Color(getShell().getDisplay(), mainBGSelector.getColorValue());
		Color mainFG = new Color(getShell().getDisplay(), mainFGSelector.getColorValue());
		
		stylesTable.getTable().setBackground(mainBG);
		stylesTable.setInput(styles.values());
		
		preview.setBackground(mainBG);
		preview.setForeground(mainFG);
//		preview.setFont(new Font(getShell().getDisplay(), mainFontSelector.getFontData()));
		
		updatePresetColors(StormFrontClientSettings.PRESET_ROOM_NAME, roomNameStyleRange);
		updatePresetColors(StormFrontClientSettings.PRESET_BOLD, boldStyleRange);
		updatePresetColors(StormFrontClientSettings.PRESET_COMMAND, commandStyleRange);
		updatePresetColors(StormFrontClientSettings.PRESET_SPEECH, speechStyleRange);
		updatePresetColors(StormFrontClientSettings.PRESET_WHISPER, whisperStyleRange);
		updatePresetColors(StormFrontClientSettings.PRESET_THOUGHT, thoughtStyleRange);
		
//		roomNameStyleRange.background = ColorUtil.warlockColorToColor(styles.get(StormFrontClientSettings.PRESET_ROOM_NAME).getBackgroundColor());
//		roomNameStyleRange.foreground = ColorUtil.warlockColorToColor(styles.get(StormFrontClientSettings.PRESET_ROOM_NAME).getForegroundColor());
		
		columnStyleRange.background = mainBG;
		columnStyleRange.foreground = mainFG;
//		columnStyleRange.font = new Font(getShell().getDisplay(), columnFontSelector.getFontData());
		
		preview.setStyleRanges(new StyleRange[] { roomNameStyleRange, boldStyleRange, commandStyleRange, speechStyleRange, whisperStyleRange, thoughtStyleRange, columnStyleRange });
		preview.update();
		
	}
	
	@Override
	public boolean performOk() {
		
		boolean updateView = false;
		
		for (WarlockStyle style: styles.values())
		{
			if (style.needsUpdate())
			{
				updateView = true;
				settings.getHighlightConfigurationProvider().removeNamedStyle(style.getOriginalStyle().getName());
				settings.getHighlightConfigurationProvider().addNamedStyle(style.getName(), style);
			}
		}
		
		if (mainWindow.needsUpdate())
		{
			updateView = true;
			settings.getWindowSettingsProvider().removeWindowSettings(mainWindow.getOriginalWindowSettings());
			settings.getWindowSettingsProvider().addWindowSettings(mainWindow);
		}
		
		if (updateView) {
			StormFrontGameView view = (StormFrontGameView) StormFrontGameView.getGameViewForClient(settings.getClient());
			view.loadStormFrontClientSettings(settings);
		}
		
		return true;
	}
}
