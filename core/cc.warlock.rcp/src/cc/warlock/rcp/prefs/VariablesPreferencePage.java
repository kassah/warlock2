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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.settings.IVariable;
import cc.warlock.core.client.settings.IVariableProvider;
import cc.warlock.core.client.settings.internal.ClientSettings;
import cc.warlock.core.client.settings.internal.Variable;
import cc.warlock.rcp.ui.WarlockSharedImages;

/**
 * 
 * @author Marshall Culpepper
 *
 */
public class VariablesPreferencePage extends PreferencePageUtils {

	public static final String PAGE_ID = "cc.warlock.rcp.prefs.variables";
	
	protected IWarlockClient client;
	protected ClientSettings settings;
	
	protected Text filterText;
	protected TableViewer variableTable;
	protected ArrayList<Variable> variables = new ArrayList<Variable>();
	protected ArrayList<Variable> addedVariables = new ArrayList<Variable>();
	protected ArrayList<Variable> removedVariables = new ArrayList<Variable>();
	protected Variable currentVariable;
	protected Button removeButton;
	
	public VariablesPreferencePage() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(2, false));
		
		Composite filterComposite = new Composite(main, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = layout.marginHeight = 0;
		filterComposite.setLayout(layout);
		filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		new Label(filterComposite, SWT.NONE).setText("Filter: ");
		filterText = new Text(filterComposite, SWT.BORDER);
		filterText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				variableTable.refresh();
			}
		});
		filterText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		new Label(main, SWT.NONE);
		
		variableTable = new TableViewer(main, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		
		TableViewerColumn varName = new TableViewerColumn(variableTable, SWT.NONE);
		varName.getColumn().setWidth(200);
		varName.getColumn().setText("Variable Name");
		varName.setEditingSupport(new TextEditingSupport(variableTable) {
			protected Object getValue(Object element) {
				return ((Variable)element).getIdentifier();
			}
			protected void setValue(Object element, Object value) {
				((Variable)element).setIdentifier(value.toString());
				variableTable.refresh(element);
			}
		});
		
		TableViewerColumn value = new TableViewerColumn(variableTable, SWT.NONE);
		value.getColumn().setWidth(150);
		value.getColumn().setText("Value");
		value.setEditingSupport(new TextEditingSupport(variableTable) {
			protected Object getValue(Object element) {
				return ((Variable)element).getValue();
			}
			protected void setValue(Object element, Object value) {
				((Variable)element).setValue(value.toString());
				variableTable.refresh(element);
			}
		});
		variableTable.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		variableTable.getTable().setHeaderVisible(true);
		variableTable.setUseHashlookup(true);
		variableTable.setContentProvider(new ArrayContentProvider());
		variableTable.setLabelProvider(new VariableLabelProvider());
		variableTable.addFilter(new ViewerFilter () {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				Variable var = (Variable) element;
				
				if (filterText.getText().equals("")) return true;
				
				return (var.getIdentifier().toLowerCase().contains(filterText.getText().toLowerCase())
						|| var.getValue().toLowerCase().contains(filterText.getText().toLowerCase()));
			}
		});
		variableTable.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				Variable v1 = (Variable)e1;
				Variable v2 = (Variable)e2;
				return v1.getIdentifier().compareTo(v2.getIdentifier());
			}
		});
		
		variableTable.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (variableTable.getSelection().isEmpty()) {
					currentVariable = null;
					removeButton.setEnabled(false);
				} else {
					currentVariable = (Variable) ((IStructuredSelection)variableTable.getSelection()).getFirstElement();
					removeButton.setEnabled(true);
				}
			}
		});
		
		int listHeight = variableTable.getTable().getItemHeight() * 8;
		Rectangle trim = variableTable.getTable().computeTrim(0, 0, 0, listHeight);
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
		data.heightHint = trim.height;
		
		variableTable.getTable().setLayoutData(data);
		
		Composite buttonsComposite = new Composite(main, SWT.NONE);
		buttonsComposite.setLayout(new GridLayout(1, true));
		buttonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		
		Button addButton = new Button(buttonsComposite, SWT.PUSH);
		addButton.setText("Add Variable");
		addButton.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_ADD));
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addVariableSelected();
			}
		});
		addButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		removeButton = new Button(buttonsComposite, SWT.PUSH);
		removeButton.setText("Remove Variable");
		removeButton.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_REMOVE));
		removeButton.setEnabled(false);
		removeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeVariableSelected();
			}
		});
		removeButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		for (IVariable var : settings.getAllVariables())
		{
			if (var instanceof Variable) {
				variables.add(new Variable((Variable)var));
			}
		}
		
		variableTable.setInput(variables);
		
		return main;
	}
	
	protected void addVariableSelected ()
	{
		Variable var = new Variable(settings.getVariableConfigurationProvider(), "", "");
		addedVariables.add(var);
		variables.add(var);
		variableTable.add(var);
		
		variableTable.editElement(var, 0);
	}
	
	protected void removeVariableSelected ()
	{
		addedVariables.remove(currentVariable);
		
		if (variables.remove(currentVariable))
			removedVariables.add(currentVariable);
		
		variableTable.remove(currentVariable);
	}
	
	protected static class VariableLabelProvider implements ITableLabelProvider
	{
		public void addListener(ILabelProviderListener listener) {}
		public void dispose() {}
		
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		
		public String getColumnText(Object element, int columnIndex) {
			if (columnIndex == 0) { 
				return ((Variable)element).getIdentifier();
			} else {
				return ((Variable)element).getValue();
			}
		}
		
		public boolean isLabelProperty(Object element, String property) {
			return true;
		}
		public void removeListener(ILabelProviderListener listener) {}
	}

	protected abstract static class TextEditingSupport extends EditingSupport
	{
		protected TextCellEditor editor;
		public TextEditingSupport (TableViewer viewer) {
			super(viewer);
			
			editor = new TextCellEditor(viewer.getTable(), SWT.SINGLE);
		}
		
		@Override
		protected boolean canEdit(Object element) {
			return true;
		}
		
		@Override
		protected CellEditor getCellEditor(Object element) {
			return editor;
		}
	}
	
	@Override
	public void setElement(IAdaptable element) {
		client = (IWarlockClient)element.getAdapter(IWarlockClient.class);
		settings = (ClientSettings)client.getClientSettings();
	}
	
	@Override
	public boolean performOk() {
		for (Variable var : variables) {
			if (var.needsUpdate() && !addedVariables.contains(var)) {
				IVariableProvider provider = (IVariableProvider) var.getProvider();
				provider.removeVariable(var.getOriginalVariable().getIdentifier());
				provider.setVariable(var.getIdentifier(), var);
			}
		}
		
		for (Variable var : removedVariables) {
			IVariableProvider provider = (IVariableProvider) var.getProvider();
			provider.removeVariable(var.getIdentifier());
		}
		
		for (Variable var : addedVariables) {
			settings.getVariableConfigurationProvider().addVariable(var);
		}
		
		return true;
	}
}
