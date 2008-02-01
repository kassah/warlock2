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

import org.eclipse.core.runtime.IAdaptable;
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
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
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

import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.client.StormFrontColor;
import cc.warlock.core.stormfront.serversettings.server.HighlightPreset;
import cc.warlock.rcp.stormfront.ui.PaletteColorSelector;
import cc.warlock.rcp.ui.WarlockSharedImages;
import cc.warlock.rcp.util.ColorUtil;


public class HighlightStringsPreferencePage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public static final String PAGE_ID = "cc.warlock.rcp.prefs.highlightStrings";
	
	protected TableViewer stringTable;
	protected Button fillLineButton;
	protected PaletteColorSelector customBGSelector, customFGSelector;
	protected Button defaultBG, customBG, defaultFG, customFG;
	protected Button addString, removeString;
	protected IStormFrontClient client;
	protected HighlightPreset selectedString;
	protected ArrayList<HighlightPreset> highlightStrings = new ArrayList<HighlightPreset>();
	
	private void copyHighlightStrings ()
	{
		highlightStrings.clear();
		for (HighlightPreset string : client.getServerSettings().getHighlightPresets())
		{
			highlightStrings.add(new HighlightPreset(string));
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
				return ((HighlightPreset)element).getText();
			}

			public void modify(Object element, String property, Object value) {
				TableItem item = (TableItem)element;
				HighlightPreset string = (HighlightPreset)item.getData();
				
				string.setText(((String)value).trim());
				stringTable.refresh(string);
			}
		});
		
		stringTable.setLabelProvider(new StringsLabelProvider());
		stringTable.setContentProvider(new ArrayContentProvider());
		stringTable.setInput(highlightStrings);
		stringTable.addFilter(getViewerFilter());
		
		int listHeight = stringTable.getTable().getItemHeight() * 8;
		Rectangle trim = stringTable.getTable().computeTrim(0, 0, 0, listHeight);
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
		data.heightHint = trim.height;
		stringTable.getTable().setLayoutData(data);

		stringTable.addSelectionChangedListener(new ISelectionChangedListener () {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				HighlightPreset string = (HighlightPreset) selection.getFirstElement();
				
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
		customFGSelector = new PaletteColorSelector(fgColorComposite, client.getServerSettings().getPalette());
		
		
		new Label(optionsGroup, SWT.NONE).setText("Background Color: ");
		Composite bgColorComposite = new Composite(optionsGroup, SWT.NONE);
		bgColorComposite.setLayout(new GridLayout(3, false));
		defaultBG = new Button(bgColorComposite, SWT.RADIO);
		defaultBG.setText("Default");
		customBG = new Button(bgColorComposite, SWT.RADIO);
		customBGSelector = new PaletteColorSelector(bgColorComposite, client.getServerSettings().getPalette());
		
		fillLineButton = new Button(optionsGroup, SWT.CHECK);
		fillLineButton.setText("Fill Entire Line");
	}
	
	protected String getDisplayName ()
	{
		return "Highlight Strings";
	}
	
	protected ViewerFilter getViewerFilter ()
	{
		return new ViewerFilter ()
		{
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				return !((HighlightPreset)element).isName();
			}
		};
	}
	
	protected HighlightPreset createHighlightString ()
	{
		 HighlightPreset newString = client.getServerSettings().createHighlightString(false);
		 newString.setText("<Highlight Text>");
		 return newString;
	}
	
	private void highlightStringSelected (HighlightPreset string)
	{
		selectedString = string;
		if (string == null) return;
		
		StormFrontColor fgColor = string.getForegroundColor();
		StormFrontColor bgColor = string.getBackgroundColor();

		boolean fgIsDefault = fgColor.isSkinColor() || fgColor.equals(string.getDefaultForegroundColor());
		boolean bgIsDefault = bgColor.isSkinColor() || bgColor.equals(string.getDefaultBackgroundColor());
		
		defaultFG.setSelection(fgIsDefault);
		customFG.setSelection(!fgIsDefault);
		customFGSelector.setEnabled(!fgIsDefault);
		
		defaultBG.setSelection(bgIsDefault);
		customBG.setSelection(!bgIsDefault);
		customBGSelector.setEnabled(!bgIsDefault);
		
		customFGSelector.setColorValue(ColorUtil.warlockColorToRGB(fgColor));
		customBGSelector.setColorValue(ColorUtil.warlockColorToRGB(bgColor));
		fillLineButton.setSelection(string.isFillEntireLine());
	}
	
	private void defaultForegroundSelected ()
	{
		selectedString.setDefaultForegroundColor();
		customFGSelector.setEnabled(false);
		customFGSelector.setColorValue(ColorUtil.warlockColorToRGB(selectedString.getForegroundColor()));
		stringTable.update(selectedString, null);
		setValid(true);
	}
	
	private void customForegroundChanged ()
	{
		selectedString.setForegroundColor(customFGSelector.getStormFrontColor());
		stringTable.update(selectedString, null);
		setValid(true);
	}
	
	private void defaultBackgroundSelected ()
	{
		selectedString.setDefaultBackgroundColor();
		customBGSelector.setEnabled(false);
		customBGSelector.setColorValue(ColorUtil.warlockColorToRGB(selectedString.getBackgroundColor()));
		stringTable.update(selectedString, null);	
		setValid(true);
	}
	
	private void customBackgroundChanged ()
	{
		selectedString.setBackgroundColor(customBGSelector.getStormFrontColor());
		stringTable.update(selectedString, null);
		setValid(true);
	}
	
	private void removeStringClicked() {
		HighlightPreset string = selectedString;
		
		client.getServerSettings().deleteHighlightString(string);
		
		highlightStrings.remove(string);
		stringTable.remove(string);
	}

	private void addStringClicked() {
		HighlightPreset newString = createHighlightString();
		highlightStrings.add(newString);
		
		selectedString = newString;
		
		stringTable.add(newString);
		stringTable.editElement(newString, 0);
	}
	
	private void fillLineClicked () {
		selectedString.setFillEntireLine(fillLineButton.getSelection());
	}
	
	protected class StringsLabelProvider implements ITableLabelProvider, ITableColorProvider
	{
		
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			return ((HighlightPreset)element).getText();
		}

		public void addListener(ILabelProviderListener listener) {	}

		public void dispose() {	}

		public boolean isLabelProperty(Object element, String property) {
			return true;
		}

		public void removeListener(ILabelProviderListener listener) {}

		public Color getBackground(Object element, int columnIndex) {
			Color c = new Color(HighlightStringsPreferencePage.this.getShell().getDisplay(),
					ColorUtil.warlockColorToRGB(((HighlightPreset)element).getBackgroundColor()));
			
			return c;
		}

		public Color getForeground(Object element, int columnIndex) {
			Color c = new Color(HighlightStringsPreferencePage.this.getShell().getDisplay(),
					ColorUtil.warlockColorToRGB(((HighlightPreset)element).getForegroundColor()));
			
			return c;
		}
	}
	
	@Override
	public void setElement(IAdaptable element) {
		client = (IStormFrontClient)element.getAdapter(IStormFrontClient.class);
		
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
		
		for (HighlightPreset string : highlightStrings) {
			if (string.needsUpdate()) {
				client.getServerSettings().updateHighlightString(string);
			}
		}
		
		// God save us all
		saveSettings();
		
		return true;
	}
	
	protected void saveSettings ()
	{
		client.getServerSettings().saveHighlightStrings();
	}
}
