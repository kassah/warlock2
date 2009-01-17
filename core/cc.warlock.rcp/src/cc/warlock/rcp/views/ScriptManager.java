/**
 * 
 */
package cc.warlock.rcp.views;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
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
		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		
		scriptsTable = new TableViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.FILL );
		nameColumn = new TableColumn(scriptsTable.getTable(), SWT.LEFT, 0);
		nameColumn.setWidth(100);
		playColumn = new TableColumn(scriptsTable.getTable(), SWT.NONE, 1);
		playColumn.setWidth(10);
		pauseColumn = new TableColumn(scriptsTable.getTable(), SWT.NONE, 2);
		pauseColumn.setWidth(10);
		stopColumn = new TableColumn(scriptsTable.getTable(), SWT.NONE, 3);
		stopColumn.setWidth(10);
		
		
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
					nameColumn.setWidth(width - 30);
					table.setSize(area.width, area.height);
				} else {
					// table is getting bigger so make the table 
					// bigger first and then make the columns wider
					// to match the client area width
					table.setSize(area.width, area.height);
					nameColumn.setWidth(width - 30);
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

}
