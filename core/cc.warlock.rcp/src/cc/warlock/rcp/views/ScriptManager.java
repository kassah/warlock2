/**
 * 
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
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * @author kassah
 *
 */
public class ScriptManager extends ViewPart implements IGameViewFocusListener {
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
		//parent.setLayout
		//main = new Composite(parent, SWT.NONE);
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
		pauseColumn.setWidth(16);
		pauseColumn.setText("Pause");
		stopColumn = new TableColumn(scriptsTable.getTable(), SWT.RIGHT, 2);
		stopColumn.setWidth(16);
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
					nameColumn.setWidth(width - 32);
					stopColumn.setWidth(16);
					pauseColumn.setWidth(16);
					table.setSize(area.width, area.height);
				} else {
					// table is getting bigger so make the table 
					// bigger first and then make the columns wider
					// to match the client area width
					table.setSize(area.width, area.height);
					nameColumn.setWidth(width - 32);
					stopColumn.setWidth(16);
					pauseColumn.setWidth(16);
				}
			}
		});

		//column.setWidth(400);
		
		//entry = new Text(main, SWT.BORDER);
		//entry.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
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
	
	class ScriptsLabelProvider 
	extends LabelProvider
	implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			Image result = null;
			ScriptRow script = (ScriptRow) element;
			switch (columnIndex) {
				case 0 :
					break;
				case 1 :
					if (!script.getRunning())
						result = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_FORWARD);
					else
						result = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_FORWARD_DISABLED);
				case 2:
					if (script.getRunning() && !script.getPaused())
						result = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ELCL_STOP);
					else
						result = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ELCL_STOP_DISABLED);
			}
			return result;
		}

		public String getColumnText(Object element, int columnIndex) {
			String result = "";
			ScriptRow script = (ScriptRow) element;
			switch (columnIndex) {
				case 0:
					result = script.getName();
					break;
				case 1 :
					result = script.getPaused()?"|>":"||";
					break;
				case 2 :
					result = "[]";
					break;
				default :
					break;
			}
			return result;
		}
	}
	
	class ScriptRow {
		private String name = "";
		private Boolean running = false;
		private Boolean paused = false;
		
		public ScriptRow(String name, Boolean paused) {
			
			super();
			setName(name);
			setPaused(paused);
		}
		
		public String getName() {
			return name;
		}
		
		public Boolean getRunning() {
			return running;
		}
		
		public Boolean getPaused() {
			return paused;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public void setPaused(Boolean paused) {
			this.paused = paused;
		}
	}
}
