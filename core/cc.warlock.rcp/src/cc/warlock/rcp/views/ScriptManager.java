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
package cc.warlock.rcp.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptEngine;
import cc.warlock.core.script.IScriptInfo;
import cc.warlock.core.script.IScriptListener;
import cc.warlock.rcp.ui.WarlockSharedImages;

/**
 * @author kassah
 *
 */
public class ScriptManager extends ViewPart implements IGameViewFocusListener, IScriptListener {
	protected Text entry;
	protected TableViewer scriptsTable;
	protected Composite main;
	protected TableColumn nameColumn, playColumn, pauseColumn, stopColumn;
	
	public static final String VIEW_ID = "cc.warlock.rcp.views.ScriptManager";
	
	public ScriptManager()
	{
		// Listen for switch of Focus
		GameView.addGameViewFocusListener(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		main = parent;
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing=0;
		layout.marginBottom=0;
		layout.marginHeight =0;
		layout.marginLeft =0;
		layout.marginRight=0;
		layout.marginTop =0;
		layout.marginWidth=0;
		layout.verticalSpacing = 0;
		
		parent.setLayout(layout);

		
		scriptsTable = new TableViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.FILL );
		scriptsTable.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		scriptsTable.setUseHashlookup(true);
		scriptsTable.setColumnProperties(new String[] { "name", "pause", "stop" });
		scriptsTable.getTable().setLinesVisible(true);
		//scriptsTable.getTable().setHeaderVisible(true);
		
		CellEditor editors[] = new CellEditor[] { 
				new TextCellEditor(scriptsTable.getTable()),
				new CheckboxCellEditor(scriptsTable.getTable()),
				new CheckboxCellEditor(scriptsTable.getTable())
		};
		scriptsTable.setCellEditors(editors);
		
		scriptsTable.setCellModifier(new ICellModifier () {
			public boolean canModify(Object element, String property) {
				return false;
			}

			public Object getValue(Object element, String property) {
				return ((ScriptRow)element).getName();
			}

			public void modify(Object element, String property, Object value) {
				return;
			}
		});
		
		nameColumn = new TableColumn(scriptsTable.getTable(), SWT.LEFT, 0);
		nameColumn.setText("Name");
		pauseColumn = new TableColumn(scriptsTable.getTable(), SWT.RIGHT, 1);
		pauseColumn.setWidth(30);
		pauseColumn.setText("Pause");
		stopColumn = new TableColumn(scriptsTable.getTable(), SWT.RIGHT, 2);
		stopColumn.setWidth(30);
		stopColumn.setText("Stop");
		scriptsTable.setLabelProvider(new ScriptsLabelProvider());
		scriptsTable.setContentProvider(new ArrayContentProvider());
		scriptsTable.setInput(new ScriptRow[] { 
				new ScriptRow("sfhunter.cmd",false), 
				new ScriptRow("train.cmd", true), 
				new ScriptRow("do.wiz", false), 
				new ScriptRow("go.wsl", true) 
			});
		
		
		main.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle area = main.getClientArea();
				Table table = scriptsTable.getTable();
				Point size = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				ScrollBar vBar = table.getVerticalBar();
				int width = area.width - table.computeTrim(0,0,0,0).width - vBar.getSize().x;
				if (size.y > area.height + table.getHeaderHeight()) {
					// Subtract the scrollbar width from the total column width
					// if a vertical scrollbar will be required
					Point vBarSize = vBar.getSize();
					width -= vBarSize.x;
				}
				Point oldSize = table.getSize();
				if (oldSize.x > area.width) {
					// table is getting smaller so make the columns 
					// smaller first and then resize the table to
					// match the client area width
					nameColumn.setWidth(width - 60);
					stopColumn.setWidth(30);
					pauseColumn.setWidth(30);
					table.setSize(area.width, area.height);
				} else {
					// table is getting bigger so make the table 
					// bigger first and then make the columns wider
					// to match the client area width
					table.setSize(area.width, area.height);
					nameColumn.setWidth(width - 60);
					stopColumn.setWidth(30);
					pauseColumn.setWidth(30);
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see cc.warlock.rcp.views.IGameViewFocusListener#gameViewFocused(cc.warlock.rcp.views.GameView)
	 */
	public void gameViewFocused(GameView gameView) {
		// TODO Auto-generated method stub

	}
	
	public void scriptPaused(IScript script) {
		// TODO Auto-generated method stub
		
	}

	public void scriptResumed(IScript script) {
		// TODO Auto-generated method stub
		
	}

	public void scriptStarted(IScript script) {
		// TODO Auto-generated method stub
		
	}

	public void scriptStopped(IScript script, boolean userStopped) {
		// TODO Auto-generated method stub
		
	}
	
	class ScriptsLabelProvider 
	extends LabelProvider
	implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			Image result = null;
			IScript script = (IScript) element;
			switch (columnIndex) {
				case 0 :
					break;
				case 1 :
					if (!script.isSuspended())
						result = WarlockSharedImages.getImage(WarlockSharedImages.IMG_SCRIPT_SUSPEND);
					else
						result = WarlockSharedImages.getImage(WarlockSharedImages.IMG_SCRIPT_RESUME);
					break;
				case 2:
					result = WarlockSharedImages.getImage(WarlockSharedImages.IMG_SCRIPT_STOP);
					break;
			}
			return result;
		}

		public String getColumnText(Object element, int columnIndex) {
			String result = "";
			IScript script = (IScript) element;
			switch (columnIndex) {
				case 0:
					result = script.getName();
					break;
				case 1 :
					// result = script.getPaused()?"|>":"||";
					break;
				case 2 :
					// result = "[]";
					break;
				default :
					break;
			}
			return result;
		}
	}
	
	
	class ScriptRow implements IScript {
		protected String name;
		protected Boolean suspended;
		
		public ScriptRow(String name, Boolean suspended) {
			this.name = name;
			this.suspended = suspended;
		}

		public void addScriptListener(IScriptListener listener) {
			// TODO Auto-generated method stub
			
		}

		public IWarlockClient getClient() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}

		public IScriptEngine getScriptEngine() {
			// TODO Auto-generated method stub
			return null;
		}

		public IScriptInfo getScriptInfo() {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isRunning() {
			// TODO Auto-generated method stub
			return true;
		}

		public boolean isSuspended() {
			// TODO Auto-generated method stub
			return suspended;
		}

		public void removeScriptListener(IScriptListener listener) {
			// TODO Auto-generated method stub
			
		}

		public void resume() {
			suspended = false;
		}

		public void stop() {
			// TODO Auto-generated method stub
			suspended = true;
		}

		public void suspend() {
			// TODO Auto-generated method stub
			suspended = true;
		}
	}
}
