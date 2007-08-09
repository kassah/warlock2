package com.arcaner.warlock.rcp.prefs;

import java.util.ArrayList;

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

import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.client.stormfront.WarlockColor;
import com.arcaner.warlock.configuration.server.HighlightString;
import com.arcaner.warlock.rcp.ui.WarlockSharedImages;
import com.arcaner.warlock.rcp.util.ColorUtil;

public class HighlightStringsPreferencePage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public static final String PAGE_ID = "com.arcaner.warlock.rcp.prefs.highlightStrings";
	
	protected TableViewer stringTable;
	protected Button fillLineButton;
	protected ColorSelector customBGSelector, customFGSelector;
	protected Button defaultBG, customBG, defaultFG, customFG;
	protected Button addString, removeString;
	protected IStormFrontClient client;
	protected HighlightString selectedString;
	protected ArrayList<HighlightString> highlightStrings = new ArrayList<HighlightString>();
	
	private void copyHighlightStrings ()
	{
		highlightStrings.clear();
		for (HighlightString string : client.getServerSettings().getHighlightStrings())
		{
			highlightStrings.add(new HighlightString(string));
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
				return ((HighlightString)element).getText();
			}

			public void modify(Object element, String property, Object value) {
				TableItem item = (TableItem)element;
				HighlightString string = (HighlightString)item.getData();
				
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
	
	protected ViewerFilter getViewerFilter ()
	{
		return new ViewerFilter ()
		{
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				return !((HighlightString)element).isName();
			}
		};
	}
	
	protected HighlightString createHighlightString ()
	{
		 HighlightString newString = client.getServerSettings().createHighlightString(false);
		 newString.setText("<Highlight Text>");
		 return newString;
	}
	
	private void highlightStringSelected (HighlightString string)
	{
		selectedString = string;
		
		WarlockColor fgColor = string.getForegroundColor();
		WarlockColor bgColor = string.getBackgroundColor();

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
		selectedString.setForegroundColor(ColorUtil.rgbToWarlockColor(customFGSelector.getColorValue()));
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
		selectedString.setBackgroundColor(ColorUtil.rgbToWarlockColor(customBGSelector.getColorValue()));
		stringTable.update(selectedString, null);
		setValid(true);
	}
	
	private void removeStringClicked() {
		highlightStrings.remove(selectedString);
		stringTable.remove(selectedString);
	}

	private void addStringClicked() {
		HighlightString newString = createHighlightString();
		highlightStrings.add(newString);
		
		selectedString = newString;
		
		stringTable.add(newString);
		stringTable.editElement(newString, 0);
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
			Color c = new Color(HighlightStringsPreferencePage.this.getShell().getDisplay(),
					ColorUtil.warlockColorToRGB(((HighlightString)element).getBackgroundColor()));
			
			return c;
		}

		public Color getForeground(Object element, int columnIndex) {
			Color c = new Color(HighlightStringsPreferencePage.this.getShell().getDisplay(),
					ColorUtil.warlockColorToRGB(((HighlightString)element).getForegroundColor()));
			
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
		client.getServerSettings().clearHighlightStrings();
		for (HighlightString string : highlightStrings)
			client.getServerSettings().addHighlightString(string);
		
		// God save us all
		saveSettings();
		
		return true;
	}
	
	protected void saveSettings ()
	{
		client.getServerSettings().saveHighlightStrings();
	}
}
