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

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.client.settings.IHighlightString;
import cc.warlock.core.client.settings.internal.HighlightString;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.settings.internal.StormFrontClientSettings;
import cc.warlock.core.stormfront.settings.skin.IStormFrontSkin;
import cc.warlock.rcp.ui.WarlockSharedImages;
import cc.warlock.rcp.util.ColorUtil;


public class HighlightStringsPreferencePage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public static final String PAGE_ID = "cc.warlock.rcp.prefs.highlightStrings";
	
	protected TableViewer stringTable;
	protected Button fillLineButton;
	protected ColorSelector customBGSelector, customFGSelector;
	protected Button defaultBG, customBG, defaultFG, customFG;
	protected Button addString, removeString;
	protected IStormFrontClient client;
	protected IStormFrontSkin skin;
	protected StormFrontClientSettings settings;
	protected HighlightString selectedString;
	protected ArrayList<HighlightString> highlightStrings = new ArrayList<HighlightString>();

	
	private void copyHighlightStrings ()
	{
		highlightStrings.clear();
		for (IHighlightString string : client.getClientSettings().getAllHighlightStrings())
		{
			if (string instanceof HighlightString)
			{
				highlightStrings.add(new HighlightString((HighlightString)string));
			}
		}
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		
		createStringsTable(main);
		createOptions(main);
		
		defaultFG.addSelectionListener(new SelectionListener () {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				defaultForegroundSelected();
			}
		});
		
		customFG.addSelectionListener(new SelectionAdapter () {
			@Override
			public void widgetSelected(SelectionEvent e) {
				customFGSelector.setEnabled(true);
			}
		});
		
		customFGSelector.addListener(new IPropertyChangeListener () {
			public void propertyChange(PropertyChangeEvent event) {
				customForegroundChanged();
			}
		});
		
		defaultBG.addSelectionListener(new SelectionListener () {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				defaultBackgroundSelected();
			}
		});
		
		customBG.addSelectionListener(new SelectionAdapter () {
			@Override
			public void widgetSelected(SelectionEvent e) {
				customBGSelector.setEnabled(true);
			}
		});
		
		customBGSelector.addListener(new IPropertyChangeListener () {
			public void propertyChange(PropertyChangeEvent event) {
				customBackgroundChanged();
			}
		});
		
		fillLineButton.addSelectionListener(new SelectionAdapter () {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fillLineClicked();
			}
		});
		
		return main;
	}
	
	private void createStringsTable (Composite main)
	{
		Group stringsGroup = new Group(main, SWT.NONE);
		stringsGroup.setLayout(new GridLayout(2, false));
		stringsGroup.setText(getDisplayName());
		stringsGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		
		stringTable = new TableViewer(stringsGroup, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL );
		TableColumn column = new TableColumn(stringTable.getTable(), SWT.NONE, 0);
		column.setWidth(400);
		
		stringTable.setUseHashlookup(true);
		stringTable.setColumnProperties(new String[] { "highlightString" });
		
		CellEditor editors[] = new CellEditor[] { 
				new TextCellEditor(stringTable.getTable())
		};
		
		stringTable.setCellEditors(editors);
		stringTable.setCellModifier(new ICellModifier () {
			public boolean canModify(Object element, String property) {
				return true;
			}

			public Object getValue(Object element, String property) {
				return ((HighlightString)element).getPattern().pattern();
			}

			public void modify(Object element, String property, Object value) {
				TableItem item = (TableItem)element;
				HighlightString string = (HighlightString)item.getData();
				String pattern = ((String)value).trim();
				
				string.setPattern(Pattern.compile(pattern, Pattern.LITERAL | Pattern.CASE_INSENSITIVE));
				stringTable.refresh(string);
			}
		});
		
		stringTable.setLabelProvider(new StringsLabelProvider());
		stringTable.setContentProvider(new ArrayContentProvider());
		stringTable.setInput(highlightStrings);
//		stringTable.addFilter(getViewerFilter());
		
		int listHeight = stringTable.getTable().getItemHeight() * 8;
		Rectangle trim = stringTable.getTable().computeTrim(0, 0, 0, listHeight);
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
		data.heightHint = trim.height;
		stringTable.getTable().setLayoutData(data);

		stringTable.addSelectionChangedListener(new ISelectionChangedListener () {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				HighlightString string = (HighlightString) selection.getFirstElement();
				
				if (string != selectedString)
				{
					stringTable.cancelEditing();
				}
				
				highlightStringSelected(string);
			}
		});
		
		Composite buttonsComposite = new Composite(stringsGroup, SWT.NONE);
		buttonsComposite.setLayout(new GridLayout(1, true));
		buttonsComposite.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, true, true));
		
		addString = new Button(buttonsComposite, SWT.LEFT | SWT.PUSH);
		addString.setText("Add");
		addString.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_ADD));
		addString.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addStringClicked();
			}
		});
		addString.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		removeString = new Button(buttonsComposite, SWT.LEFT | SWT.PUSH);
		removeString.setText("Remove");
		removeString.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_REMOVE));
		removeString.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeStringClicked();
			}
		});
	}

	private void createOptions (Composite main)
	{
		Group optionsGroup = new Group(main, SWT.NONE);
		optionsGroup.setLayout(new GridLayout(2, false));
		optionsGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		optionsGroup.setText("Options");
		
		new Label(optionsGroup, SWT.NONE).setText("Foreground Color: ");
		Composite fgColorComposite = new Composite(optionsGroup, SWT.NONE);
		fgColorComposite.setLayout(new GridLayout(3, false));
		defaultFG = new Button(fgColorComposite, SWT.RADIO);
		defaultFG.setText("Default");
		customFG = new Button(fgColorComposite, SWT.RADIO);
		customFGSelector = new ColorSelector(fgColorComposite);
		
		
		new Label(optionsGroup, SWT.NONE).setText("Background Color: ");
		Composite bgColorComposite = new Composite(optionsGroup, SWT.NONE);
		bgColorComposite.setLayout(new GridLayout(3, false));
		defaultBG = new Button(bgColorComposite, SWT.RADIO);
		defaultBG.setText("Default");
		customBG = new Button(bgColorComposite, SWT.RADIO);
		customBGSelector = new ColorSelector(bgColorComposite);
		
		fillLineButton = new Button(optionsGroup, SWT.CHECK);
		fillLineButton.setText("Fill Entire Line");
	}
	
	protected String getDisplayName ()
	{
		return "Highlight Strings";
	}
	
	protected HighlightString createHighlightString ()
	{
		return new HighlightString(
			settings.getHighlightConfigurationProvider(), "<Highlight Text>", true, false, null);
	}
	
	private void highlightStringSelected (HighlightString string)
	{
		selectedString = string;
		if (string == null) return;
		
		WarlockColor fgColor = string.getStyle().getForegroundColor();
		WarlockColor bgColor = string.getStyle().getBackgroundColor();

		boolean fgIsDefault = fgColor.isDefault();
		boolean bgIsDefault = bgColor.isDefault();
		
		defaultFG.setSelection(fgIsDefault);
		customFG.setSelection(!fgIsDefault);
		customFGSelector.setEnabled(!fgIsDefault);
		
		defaultBG.setSelection(bgIsDefault);
		customBG.setSelection(!bgIsDefault);
		customBGSelector.setEnabled(!bgIsDefault);
		
		if (fgIsDefault) {
			customFGSelector.setColorValue(ColorUtil.warlockColorToRGB(skin.getMainForeground()));
		} else {
			customFGSelector.setColorValue(ColorUtil.warlockColorToRGB(fgColor));
		}
		
		if (bgIsDefault) {
			customBGSelector.setColorValue(ColorUtil.warlockColorToRGB(skin.getMainBackground()));
		} else {
			customBGSelector.setColorValue(ColorUtil.warlockColorToRGB(bgColor));
		}
		fillLineButton.setSelection(string.getStyle().isFullLine());
	}
	
	private void defaultForegroundSelected ()
	{
		selectedString.getStyle().setForegroundColor(skin.getMainForeground());
		customFGSelector.setEnabled(false);
		customFGSelector.setColorValue(ColorUtil.warlockColorToRGB(selectedString.getStyle().getForegroundColor()));
		stringTable.update(selectedString, null);
		setValid(true);
	}
	
	private void customForegroundChanged ()
	{
		selectedString.getStyle().setForegroundColor(ColorUtil.rgbToWarlockColor(customFGSelector.getColorValue()));
		stringTable.update(selectedString, null);
		setValid(true);
	}
	
	private void defaultBackgroundSelected ()
	{
		selectedString.getStyle().setBackgroundColor(skin.getMainBackground());
		customBGSelector.setEnabled(false);
		customBGSelector.setColorValue(ColorUtil.warlockColorToRGB(selectedString.getStyle().getBackgroundColor()));
		stringTable.update(selectedString, null);	
		setValid(true);
	}
	
	private void customBackgroundChanged ()
	{
		selectedString.getStyle().setBackgroundColor(ColorUtil.rgbToWarlockColor(customBGSelector.getColorValue()));
		stringTable.update(selectedString, null);
		setValid(true);
	}
	
	private void removeStringClicked() {
		HighlightString string = selectedString;
		
		settings.getHighlightConfigurationProvider().removeHighlightString(string.getOriginalHighlightString());
		
		highlightStrings.remove(string);
		stringTable.remove(string);
	}

	private void addStringClicked() {
		HighlightString newString = createHighlightString();
		highlightStrings.add(newString);
		
		selectedString = newString;
		
		stringTable.add(newString);
		stringTable.editElement(newString, 0);
	}
	
	private void fillLineClicked () {
		selectedString.getStyle().setFullLine(fillLineButton.getSelection());
	}
	
	protected class StringsLabelProvider implements ITableLabelProvider, ITableColorProvider
	{
		
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			return ((HighlightString)element).getPattern().pattern();
		}

		public void addListener(ILabelProviderListener listener) {	}

		public void dispose() {	}

		public boolean isLabelProperty(Object element, String property) {
			return true;
		}

		public void removeListener(ILabelProviderListener listener) {}

		public Color getBackground(Object element, int columnIndex) {
			HighlightString string = (HighlightString)element;
			Color c = new Color(HighlightStringsPreferencePage.this.getShell().getDisplay(),
					ColorUtil.warlockColorToRGB(skin.getBackgroundColor(string)));
			
			return c;
		}

		public Color getForeground(Object element, int columnIndex) {
			HighlightString string = (HighlightString)element;
			Color c = new Color(HighlightStringsPreferencePage.this.getShell().getDisplay(), 
					ColorUtil.warlockColorToRGB(skin.getForegroundColor(string)));
			
			return c;
		}
	}
	
	@Override
	public void setElement(IAdaptable element) {
		client = (IStormFrontClient)element.getAdapter(IStormFrontClient.class);
		settings = (StormFrontClientSettings) client.getStormFrontClientSettings();
		skin = settings.getStormFrontClient().getStormFrontSkin();
		
		if (highlightStrings.isEmpty())
			copyHighlightStrings();
	}
	
	@Override
	protected void performDefaults() {
		copyHighlightStrings();
		stringTable.refresh();
	}
	
	@Override
	public boolean performOk() {
//		client.getServerSettings().clearHighlightStrings();
		
		for (HighlightString string : highlightStrings) {
			WarlockStyle style = (WarlockStyle) string.getStyle();
			if (string.needsUpdate() || style.needsUpdate()) {
				settings.getHighlightConfigurationProvider().replaceHighlightString(string.getOriginalHighlightString(), string);
			}
		}
		
		// God save us all
//		saveSettings();
		
		return true;
	}
	
//	protected void saveSettings ()
//	{
//		client.getServerSettings().saveHighlightStrings();
//	}
}
