/**
 * 
 */
package cc.warlock.rcp.views;

import java.util.Vector;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.core.client.settings.internal.HighlightString;

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
		scriptsTable.setColumnProperties(new String[] { "name", "play", "pause", "stop" });
		
		CellEditor editors[] = new CellEditor[] { 
				new TextCellEditor(scriptsTable.getTable()),
				new TextCellEditor(scriptsTable.getTable()),
				new TextCellEditor(scriptsTable.getTable()),
				new TextCellEditor(scriptsTable.getTable())
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
		playColumn = new TableColumn(scriptsTable.getTable(), SWT.NONE, 1);
		playColumn.setWidth(30);
		playColumn.setText("Play");
		pauseColumn = new TableColumn(scriptsTable.getTable(), SWT.NONE, 2);
		pauseColumn.setWidth(30);
		pauseColumn.setText("Pause");
		stopColumn = new TableColumn(scriptsTable.getTable(), SWT.NONE, 3);
		stopColumn.setWidth(30);
		stopColumn.setText("Stop");
		scriptsTable.setLabelProvider(new ScriptsLabelProvider());
		scriptsTable.setContentProvider(new ArrayContentProvider());
		scriptsTable.setInput(new ScriptRow[] { 
				new ScriptRow("sfhunter.cmd",true,false), 
				new ScriptRow("train.cmd", false,false), 
				new ScriptRow("do.wiz", true, false), 
				new ScriptRow("go.wsl", true, true) 
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
					nameColumn.setWidth(width - 90);
					table.setSize(area.width, area.height);
				} else {
					// table is getting bigger so make the table 
					// bigger first and then make the columns wider
					// to match the client area width
					table.setSize(area.width, area.height);
					nameColumn.setWidth(width - 90);
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
	
	/**
	 * InnerClass that acts as a proxy for the ExampleTaskList 
	 * providing content for the Table. It implements the ITaskListViewer 
	 * interface since it must register changeListeners with the 
	 * ExampleTaskList 
	 */
	class ScriptContentProvider implements IStructuredContentProvider {
		private final int COUNT = 10;
		private Vector<ScriptRow> scripts = new Vector(COUNT);
		// Combo box choices
		final String[] script_stuff = { "sfhunter.cmd", "train.cmd", "do.wiz", "go.wsl" };
		
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
		}

		public void dispose() {
			// TODO Auto-generated method stub	
		}

		// Return the tasks as an array of Objects
		public Object[] getElements(Object parent) {
			// TODO Auto-generated method stub
			ScriptRow script;
			for (int i = 0; i < 4; i++) {
				//script = new ScriptRow(script_stuff[i]);
				//script.setPaused(false);
				//script.setRunning(true);
				//scripts.add(script);
			}
			return scripts.toArray();
		}
	}
	
	class ScriptsLabelProvider 
	extends LabelProvider
	implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			String result = "";
			ScriptRow script = (ScriptRow) element;
			switch (columnIndex) {
				case 0:
					result = script.getName();
					break;
				case 1 :
					result = script.getRunning()?"":">";
					break;
				case 2 :
					result = (script.getRunning() && !script.getPaused())?"||":"";
					break;
				case 3 :
					result = script.getRunning()?"[]":"";
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
		
		public ScriptRow(String name, Boolean running, Boolean paused) {
			
			super();
			setName(name);
			setRunning(running);
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
		
		public void setRunning(Boolean running) {
			this.running = running;
		}
		
		public void setPaused(Boolean paused) {
			this.paused = paused;
		}
	}
}
