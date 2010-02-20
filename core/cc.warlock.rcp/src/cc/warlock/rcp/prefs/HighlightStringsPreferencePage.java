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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockHighlight;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.internal.WarlockClient;
import cc.warlock.core.client.internal.WarlockHighlight;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.rcp.prefs.BackupHighlightStringsPreferencePageBak.StringsLabelProvider;
import cc.warlock.rcp.ui.WarlockSharedImages;
import cc.warlock.rcp.util.ColorUtil;
import cc.warlock.rcp.views.GameView;


public class HighlightStringsPreferencePage extends PreferencePageUtils implements
		IWorkbenchPropertyPage {
	
	class HighlightItem {
		public IWarlockHighlight item;
		public boolean needsUpdate;
		public boolean isNew;
		
		public HighlightItem(IWarlockHighlight highlight) {
			this.item = highlight;
		}
	}
	
	protected class StringsLabelProvider implements ITableLabelProvider, ITableColorProvider
	{
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			return ((HighlightItem)element).item.getText();
		}

		public void addListener(ILabelProviderListener listener) {	}

		public void dispose() {	}

		public boolean isLabelProperty(Object element, String property) {
			return true;
		}

		public void removeListener(ILabelProviderListener listener) {}

		public Color getBackground(Object element, int columnIndex) {
			HighlightItem string = (HighlightItem)element;
			Color c = new Color(HighlightStringsPreferencePage.this.getShell().getDisplay(),
					ColorUtil.warlockColorToRGB(skin.getBackgroundColor(string)));
			return c;
		}

		public Color getForeground(Object element, int columnIndex) {
			HighlightItem string = (HighlightItem)element;
			Color c = new Color(HighlightStringsPreferencePage.this.getShell().getDisplay(), 
					ColorUtil.warlockColorToRGB(skin.getForegroundColor(string)));
			return c;
		}
	}

	public static final String PAGE_ID = "cc.warlock.rcp.prefs.highlightStrings";
	protected Text filterText;
	protected TableViewer highlightTable;
	protected HighlightItem selectedHighlight;
	private ColorSelector customFGSelector, customBGSelector;
	private Button defaultFG, customFG, defaultBG, customBG, fillLineButton;
	private Button caseInsensitiveButton, regexButton, fullWordMatchButton, soundButton;
	private Button addHighlight, removeHighlight;
	private WarlockClient client;
	
	private Text soundText;
	protected ArrayList<HighlightItem> highlightArray = new ArrayList<HighlightItem>();
	protected ArrayList<HighlightItem> removedHighlights = new ArrayList<HighlightItem>();
	
	protected void browseForSound(){
		FileDialog fd = new FileDialog(HighlightStringsPreferencePage.this.getShell(), SWT.OPEN);
		fd.setFilterNames(new String[]{"Wave File (*.wav)"});
		fd.setFilterExtensions(new String[]{"*.wav"});
		String filename = fd.open();
		if (filename != null){
			soundText.setText(filename);
			try{
				if (selectedHighlight != null && selectedHighlight.item.getStyle() != null){
					selectedHighlight.item.getStyle().setSound(filename);
					selectedHighlight.needsUpdate = true;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
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
		} else if (button == caseInsensitiveButton) {
			caseInsensitiveSelected();
		} else if (button == regexButton) {
			regexSelected();
		}
	}
	
	private void fillLineSelected () {
		selectedHighlight.item.getStyle().setFullLine(fillLineButton.getSelection());
	}
	
	private void fullWordMatchSelected () {
		selectedHighlight.item.setWholeWord(fullWordMatchButton.getSelection());
	}
	
	private void caseInsensitiveSelected () {
		selectedHighlight.item.setCaseInsensitive(caseInsensitiveButton.getSelection());
	}
	
	private void defaultForegroundSelected ()
	{
		selectedHighlight.item.getStyle().setForegroundColor(skin.getMainForeground());
		customFGSelector.setEnabled(false);
		customFGSelector.setColorValue(ColorUtil.warlockColorToRGB(selectedHighlight.item.getStyle().getForegroundColor()));
		highlightTable.update(selectedHighlight, null);
		setValid(true);
	}
	
	private void customForegroundChanged ()
	{
		RGB selectedColor = customFGSelector.getColorValue();
		WarlockColor newColor = ColorUtil.rgbToWarlockColor(selectedColor);
		selectedHighlight.item.getStyle().setForegroundColor(newColor);
		highlightTable.update(selectedHighlight, null);
		setValid(true);
	}
	
	private void defaultBackgroundSelected ()
	{
		selectedHighlight.item.getStyle().setBackgroundColor(skin.getMainBackground());
		customBGSelector.setEnabled(false);
		customBGSelector.setColorValue(ColorUtil.warlockColorToRGB(selectedHighlight.item.getStyle().getBackgroundColor()));
		highlightTable.update(selectedHighlight, null);	
		setValid(true);
	}
	
	private void customBackgroundChanged ()
	{
		selectedHighlight.item.getStyle().setBackgroundColor(ColorUtil.rgbToWarlockColor(customBGSelector.getColorValue()));
		highlightTable.update(selectedHighlight, null);
		setValid(true);
	}
	
	private void removeStringSelected() {
		// Grab selected string.
		HighlightItem highlight = selectedHighlight;
		
		// Select Next (or Previous if last) Highlight in line
		int index = highlightTable.getTable().getSelectionIndex();
		if (highlightTable.getElementAt(index + 1) != null) {
			highlightTable.getTable().setSelection(index + 1);
			highlightSelected((HighlightItem) highlightTable.getElementAt(index + 1));
		} else if (highlightTable.getElementAt(index - 1) != null) {
			highlightTable.getTable().setSelection(index - 1);
			highlightSelected((HighlightItem) highlightTable.getElementAt(index - 1));
		}
		
		// Mark string removed in our changelog to commit to prefs
		// If it was added since the last commit, just remove it from the changelog
		if (!highlight.isNew)
			removedHighlights.add(highlight);
		
		// Remove string from our display list and notify the table
		highlightArray.remove(highlight);
		highlightTable.remove(highlight);
	}
	
	private void regexSelected () {
		selectedHighlight.item.setListeral(!regexButton.getSelection());
		selectedHighlight.needsUpdate = true;
	}
	
	private void addStringSelected() {
		HighlightItem newHighlight = new HighlightItem(createHighlightString());
		newHighlight.isNew = true;
		highlightArray.add(newHighlight);
		selectedHighlight = newHighlight;
		highlightTable.add(newHighlight);
		highlightTable.editElement(newHighlight, 0);
	}
	
	private IWarlockHighlight createHighlightString() {
		return new WarlockHighlight("<Match Text>", false, false, true, new WarlockStyle());
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		
		createStringsTable(main);
		createOptions(main);
		
		return main;
	}
	
	private void createOptions (Composite main)
	{
		Group optionsGroup = new Group(main, SWT.NONE);
		optionsGroup.setLayout(new GridLayout(1, false));
		optionsGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		optionsGroup.setText("Options");
		Composite mainGroup = new Composite(optionsGroup, SWT.NONE);
		
		mainGroup.setLayout(new GridLayout(2, false));
		mainGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		
		new Label(mainGroup, SWT.NONE).setText("Foreground Color: ");
		Composite fgColorComposite = new Composite(mainGroup, SWT.NONE);
		fgColorComposite.setLayout(new GridLayout(3, false));
		fgColorComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		defaultFG = createRadio(fgColorComposite, "Default");
		defaultFG.setEnabled(false);
		customFG = createRadio(fgColorComposite);
		customFG.setEnabled(false);
		customFGSelector = createColorSelector(fgColorComposite);
		customFGSelector.setEnabled(false);
		customFGSelector.setColorValue(new RGB(0, 0, 0));
		
		new Label(mainGroup, SWT.NONE).setText("Background Color: ");
		Composite bgColorComposite = new Composite(mainGroup, SWT.NONE);
		bgColorComposite.setLayout(new GridLayout(3, false));
		bgColorComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		defaultBG = createRadio(bgColorComposite, "Default");
		defaultBG.setEnabled(false);
		customBG = createRadio(bgColorComposite);
		customBG.setEnabled(false);
		customBGSelector = createColorSelector(bgColorComposite);
		customBGSelector.setEnabled(false);
		customBGSelector.setColorValue(new RGB(0, 0, 0));
		
		regexButton = createCheckbox(mainGroup, "Regular expression");
		regexButton.setEnabled(false);
		fillLineButton = createCheckbox(mainGroup, "Highlight Entire Line");
		fillLineButton.setEnabled(false);
		caseInsensitiveButton = createCheckbox(mainGroup, "Case Insensitive");
		caseInsensitiveButton.setEnabled(false);
		fullWordMatchButton = createCheckbox(mainGroup, "Match full word (word boundary)");
		fullWordMatchButton.setEnabled(false);
		
		Group soundGroup = new Group(optionsGroup, SWT.NONE);
		soundGroup.setLayout(new GridLayout(3, false));
		soundGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		new Label(soundGroup, SWT.NONE).setText("Sound:");
		soundText = new Text(soundGroup, SWT.BORDER | SWT.SINGLE);
		soundText.setEnabled(false);
		GridData soundTextData = new GridData(GridData.FILL_HORIZONTAL);
		soundText.setLayoutData(soundTextData);
		soundText.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				selectedHighlight.item.getStyle().setSound(soundText.getText());
			}
		});
		
		
		soundButton = createButton(soundGroup, "Browse", SWT.PUSH);
		soundButton.setEnabled(false);
		soundButton.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e){}
			
			public void widgetSelected(SelectionEvent e){
				browseForSound();
			}
		});
	}
	
	private void createStringsTable(Composite main) {
		// TODO Auto-generated method stub
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
				highlightTable.refresh();
			}
		});
		filterText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		new Label(stringsGroup, SWT.NONE);
		
		highlightTable = new TableViewer(stringsGroup, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL );
		TableColumn column = new TableColumn(highlightTable.getTable(), SWT.NONE, 0);
		column.setWidth(400);
		
		highlightTable.setUseHashlookup(true);
		highlightTable.setColumnProperties(new String[] { "highlightString" });
		
		CellEditor editors[] = new CellEditor[] { 
				new TextCellEditor(highlightTable.getTable())
		};
		
		highlightTable.setCellEditors(editors);
		highlightTable.setCellModifier(new ICellModifier () {
			public boolean canModify(Object element, String property) {
				return true;
			}

			public Object getValue(Object element, String property) {
				return ((HighlightItem)element).item.getText();
			}

			public void modify(Object element, String property, Object value) {
				TableItem item = (TableItem)element;
				HighlightItem highlight = (HighlightItem)item.getData();
				String pattern = ((String)value).trim();
				highlight.item.setText(pattern);
				highlightTable.refresh(highlight);
			}
		});
		
		highlightTable.addFilter(new ViewerFilter () {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				
				HighlightItem string = (HighlightItem) element;
				String str = string.item.getText();
				
				if (str.equals("")) return true;
				
				return (str.toLowerCase().contains(filterText.getText().toLowerCase()));
			}
		});
		
		highlightTable.setLabelProvider(new StringsLabelProvider());
		highlightTable.setContentProvider(new ArrayContentProvider());
		highlightTable.setInput(highlightArray);
		
		int listHeight = highlightTable.getTable().getItemHeight() * 8;
		Rectangle trim = highlightTable.getTable().computeTrim(0, 0, 0, listHeight);
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
		data.heightHint = trim.height;
		highlightTable.getTable().setLayoutData(data);

		highlightTable.addSelectionChangedListener(new ISelectionChangedListener () {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				HighlightItem string = (HighlightItem) selection.getFirstElement();
				
				if (string != selectedHighlight)
				{
					highlightTable.cancelEditing();
				}
				
				highlightSelected(string);
			}
		});
		
		Composite buttonsComposite = new Composite(stringsGroup, SWT.NONE);
		buttonsComposite.setLayout(new GridLayout(1, true));
		buttonsComposite.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, true, true));
		
		addHighlight = createButton(buttonsComposite, "Add", SWT.LEFT | SWT.PUSH);
		addHighlight.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_ADD));
		addHighlight.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		removeHighlight = createButton(buttonsComposite, "Remove", SWT.LEFT | SWT.PUSH);
		removeHighlight.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_REMOVE));
		removeHighlight.setEnabled(false);
	}

	protected String getDisplayName ()
	{
		return "Highlights";
	}
	
	private void highlightSelected (HighlightItem highlight)
	{
		if (highlight == null) {
			// No string selected, disable all fields
			removeHighlight.setEnabled(false);
			defaultFG.setEnabled(false);
			defaultFG.setSelection(true);
			customFG.setEnabled(false);
			customFG.setSelection(true);
			customFGSelector.setEnabled(false);
			customFGSelector.setColorValue(new RGB(0, 0, 0));
			defaultBG.setEnabled(false);
			customBG.setEnabled(false);
			customBG.setSelection(true);
			customBGSelector.setEnabled(false);
			customBGSelector.setColorValue(new RGB(0, 0, 0));
			regexButton.setEnabled(false);
			regexButton.setSelection(false);
			fillLineButton.setEnabled(false);
			fillLineButton.setSelection(false);
			caseInsensitiveButton.setEnabled(false);
			caseInsensitiveButton.setSelection(false);
			fullWordMatchButton.setEnabled(false);
			fullWordMatchButton.setSelection(false);
			soundText.setEnabled(false);
			soundText.setText("");
			soundButton.setEnabled(false);
			return;
		}
		selectedHighlight = highlight;
		
		removeHighlight.setEnabled(true);
		
		WarlockColor fgColor = highlight.item.getStyle().getForegroundColor();
		WarlockColor bgColor = highlight.item.getStyle().getBackgroundColor();

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
		
		regexButton.setSelection(!highlight.item.isLiteral());
		regexButton.setEnabled(true);
		
		fillLineButton.setSelection(highlight.item.getStyle().isFullLine());
		fillLineButton.setEnabled(true);
		
		caseInsensitiveButton.setSelection(highlight.item.isCaseInsensitive());
		caseInsensitiveButton.setEnabled(true);
		
		fullWordMatchButton.setSelection(highlight.item.isWholeWord());
		fullWordMatchButton.setEnabled(true);
		

		if (highlight.item.getStyle().getSound() != null){
			soundText.setText(highlight.item.getStyle().getSound());
		}else{
			soundText.setText("");
		}
		
		soundText.setEnabled(true);
		soundButton.setEnabled(true);
	}
	
	@Override
	public void setElement(IAdaptable element) {
		client = GameView.getGameViewInFocus().getWarlockClient();
		
		client = (IWarlockClient)element.getAdapter(IWarlockClient.class);
		settings = (ClientSettings) client.getClientSettings();
		skin = settings.getClient().getSkin();
		
		if (highlightStrings.isEmpty())
			copyHighlightStrings();
	}
	
	@Override
	protected void performDefaults() {
		// TODO: Bare
	}
	
	@Override
	public boolean performOk() {
		return false;
	}
}
