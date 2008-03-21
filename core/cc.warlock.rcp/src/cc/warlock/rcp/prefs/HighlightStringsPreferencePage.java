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
package cc.warlock.rcp.prefs;

import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.ColorSelector;
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
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockSkin;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.client.settings.IHighlightString;
import cc.warlock.core.client.settings.internal.ClientSettings;
import cc.warlock.core.client.settings.internal.HighlightString;
import cc.warlock.rcp.ui.WarlockSharedImages;
import cc.warlock.rcp.util.ColorUtil;


public class HighlightStringsPreferencePage extends WarlockPreferencePage implements
		IWorkbenchPropertyPage {

	public static final String PAGE_ID = "cc.warlock.rcp.prefs.highlightStrings";
	
	protected TableViewer stringTable;
	protected Button fillLineButton, regexButton, fullWordMatchButton, caseSensitiveButton;
	protected ColorSelector customBGSelector, customFGSelector;
	protected Button defaultBG, customBG, defaultFG, customFG;
	protected Button addString, removeString;
	protected Text filterText;
	protected IWarlockClient client;
	protected IWarlockSkin skin;
	protected ClientSettings settings;
	protected HighlightString selectedString;
	protected ArrayList<HighlightString> addedStrings = new ArrayList<HighlightString>();
	protected ArrayList<HighlightString> removedStrings = new ArrayList<HighlightString>();
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
		
		return main;
	}
	
	private void createStringsTable (Composite main)
	{
		Group stringsGroup = new Group(main, SWT.NONE);
		stringsGroup.setLayout(new GridLayout(2, false));
		stringsGroup.setText(getDisplayName());
		stringsGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		Composite filterComposite = new Composite(stringsGroup, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = layout.marginHeight = 0;
		filterComposite.setLayout(layout);
		filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		new Label(filterComposite, SWT.NONE).setText("Filter: ");
		filterText = new Text(filterComposite, SWT.BORDER);
		filterText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				stringTable.refresh();
			}
		});
		filterText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		new Label(stringsGroup, SWT.NONE);
		
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
				return ((HighlightString)element).getText();
			}

			public void modify(Object element, String property, Object value) {
				TableItem item = (TableItem)element;
				HighlightString string = (HighlightString)item.getData();
				String pattern = ((String)value).trim();
				
				try {
					string.setText(pattern);
				} catch(PatternSyntaxException e) {
					e.printStackTrace();
				}
				stringTable.refresh(string);
			}
		});
		
		stringTable.addFilter(new ViewerFilter () {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				
				HighlightString string = (HighlightString) element;
				String str = string.getText();
				
				if (str.equals("")) return true;
				
				return (str.toLowerCase().contains(filterText.getText().toLowerCase()));
			}
		});
		
		stringTable.setLabelProvider(new StringsLabelProvider());
		stringTable.setContentProvider(new ArrayContentProvider());
		stringTable.setInput(highlightStrings);
		
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
		
		addString = createButton(buttonsComposite, "Add", SWT.LEFT | SWT.PUSH);
		addString.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_ADD));
		addString.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		removeString = createButton(buttonsComposite, "Remove", SWT.LEFT | SWT.PUSH);
		removeString.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_REMOVE));
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
		defaultFG = createRadio(fgColorComposite, "Default");
		defaultFG.setEnabled(false);
		customFG = createRadio(fgColorComposite);
		customFG.setEnabled(false);
		customFGSelector = createColorSelector(fgColorComposite);
		customFGSelector.setEnabled(false);
		
		new Label(optionsGroup, SWT.NONE).setText("Background Color: ");
		Composite bgColorComposite = new Composite(optionsGroup, SWT.NONE);
		bgColorComposite.setLayout(new GridLayout(3, false));
		defaultBG = createRadio(bgColorComposite, "Default");
		defaultBG.setEnabled(false);
		customBG = createRadio(bgColorComposite);
		customBG.setEnabled(false);
		customBGSelector = createColorSelector(bgColorComposite);
		customBGSelector.setEnabled(false);
		
		regexButton = createCheckbox(optionsGroup, "Regular expression");
		regexButton.setEnabled(true);
		fillLineButton = createCheckbox(optionsGroup, "Highlight Entire Line");
		fillLineButton.setEnabled(false);
		caseSensitiveButton = createCheckbox(optionsGroup, "Case Sensitive");
		caseSensitiveButton.setEnabled(false);
		fullWordMatchButton = createCheckbox(optionsGroup, "Match full word (word boundary)");
		fullWordMatchButton.setEnabled(false);
	}
	
	protected String getDisplayName ()
	{
		return "Highlight Strings";
	}
	
	protected HighlightString createHighlightString ()
	{
		return new HighlightString(
			settings.getHighlightConfigurationProvider(), "<Highlight Text>", true, true, false, new WarlockStyle());
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
		defaultFG.setEnabled(true);
		customFG.setSelection(!fgIsDefault);
		customFG.setEnabled(true);
		customFGSelector.setEnabled(!fgIsDefault);
		
		defaultBG.setSelection(bgIsDefault);
		defaultBG.setEnabled(true);
		customBG.setSelection(!bgIsDefault);
		customBG.setEnabled(true);
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
		
		regexButton.setSelection(!string.isLiteral());
		regexButton.setEnabled(true);
		
		fillLineButton.setSelection(string.getStyle().isFullLine());
		fillLineButton.setEnabled(true);
		
		caseSensitiveButton.setSelection(string.isCaseSensitive());
		caseSensitiveButton.setEnabled(true);
		
		fullWordMatchButton.setSelection(string.isFullWordMatch());
		fullWordMatchButton.setEnabled(true);
	}
	
	@Override
	protected void buttonPressed(Button button) {
		if (button == removeString) {
			removeStringSelected();
		} else if (button == addString) {
			addStringSelected();
		} else if (button == defaultBG) {
			defaultBackgroundSelected();
		} else if (button == defaultFG) {
			defaultForegroundSelected();
		} else if (button == customBG) {
			customBGSelector.setEnabled(true);
		} else if (button == customFG) {
			customFGSelector.setEnabled(true);
		} else if (button == fillLineButton) {
			fillLineSelected();
		} else if (button == fullWordMatchButton) {
			fullWordMatchSelected();
		} else if (button == caseSensitiveButton) {
			caseSensitiveSelected();
		} else if (button == regexButton) {
			regexSelected();
		}
	}
	
	@Override
	protected void colorSelectorChanged(ColorSelector selector) {
		if (selector == customBGSelector) {
			customBackgroundChanged();
		} else if (selector == customFGSelector) {
			customForegroundChanged();
		}
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
		RGB selectedColor = customFGSelector.getColorValue();
		WarlockColor newColor = ColorUtil.rgbToWarlockColor(selectedColor);
		selectedString.getStyle().setForegroundColor(newColor);
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
	
	private void removeStringSelected() {
		HighlightString string = selectedString;
		if (addedStrings.contains(string)) {
			addedStrings.remove(string);
		}
		else {
			removedStrings.add(string);
		}
		
		highlightStrings.remove(string);
		stringTable.remove(string);
	}

	private void addStringSelected() {
		HighlightString newString = createHighlightString();
		highlightStrings.add(newString);
		addedStrings.add(newString);
		
		selectedString = newString;
		
		stringTable.add(newString);
		stringTable.editElement(newString, 0);
	}
	
	private void fillLineSelected () {
		selectedString.getStyle().setFullLine(fillLineButton.getSelection());
	}
	
	private void fullWordMatchSelected () {
		selectedString.setFullWordMatch(fullWordMatchButton.getSelection());
	}
	
	private void caseSensitiveSelected () {
		selectedString.setCaseSensitive(caseSensitiveButton.getSelection());
	}
	
	private void regexSelected () {
		selectedString.setLiteral(!regexButton.getSelection());
	}
	
	protected class StringsLabelProvider implements ITableLabelProvider, ITableColorProvider
	{
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			return ((HighlightString)element).getText();
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
		client = (IWarlockClient)element.getAdapter(IWarlockClient.class);
		settings = (ClientSettings) client.getClientSettings();
		skin = settings.getClient().getSkin();
		
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
		for (HighlightString string : highlightStrings) {
			WarlockStyle style = (WarlockStyle) string.getStyle();
			
			if (addedStrings.contains(string)) {
				settings.getHighlightConfigurationProvider().addHighlightString(string);
				addedStrings.remove(string);
			}
			else if (string.needsUpdate() || style.needsUpdate()) {
				settings.getHighlightConfigurationProvider().replaceHighlightString(string.getOriginalHighlightString(), string);
			}
		}
		
		for (HighlightString string : removedStrings)
		{
			if (string.getOriginalHighlightString() != null) {
				settings.getHighlightConfigurationProvider().removeHighlightString(string.getOriginalHighlightString());
			} else {
				settings.getHighlightConfigurationProvider().removeHighlightString(string);
			}
		}
		
		return true;
	}
}
