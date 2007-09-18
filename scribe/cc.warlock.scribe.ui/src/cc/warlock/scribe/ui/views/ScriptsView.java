package cc.warlock.scribe.ui.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.IWarlockSkin.ColorType;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.serversettings.server.IServerSettingsListener;
import cc.warlock.core.stormfront.serversettings.server.ServerScript;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;
import cc.warlock.rcp.ui.client.SWTWarlockClientListener;
import cc.warlock.rcp.util.ColorUtil;
import cc.warlock.scribe.ui.ScribeSharedImages;

public class ScriptsView extends ViewPart implements IServerSettingsListener {

	public static final String VIEW_ID = "cc.warlock.scribe.ui.views.ScriptsView";
	
	protected IStormFrontClient client;
	protected TableViewer scriptList;
	
	protected void setClient (IStormFrontClient client)
	{
		this.client = client;
		client.getServerSettings().addServerSettingsListener(this);
	}
	
	protected void updateCurrentClient ()
	{
		if (WarlockClientRegistry.getActiveClients().size() > 0)
		{
			IWarlockClient client = WarlockClientRegistry.getActiveClients().get(0);
			if (client instanceof IStormFrontClient)
			{
				setClient((IStormFrontClient) client);
			}
		} else {
			WarlockClientRegistry.addWarlockClientListener(new SWTWarlockClientListener(
			new IWarlockClientListener() {
				public void clientActivated(IWarlockClient client) {
					if (client instanceof IStormFrontClient)
					{
						setClient((IStormFrontClient) client);
					}
				}
				public void clientConnected(IWarlockClient client) {}
				public void clientDisconnected(IWarlockClient client) {}
				public void clientRemoved(IWarlockClient client) {}
			}));
		}
	}
	
	@Override
	public void createPartControl(Composite parent) {
		updateCurrentClient();
		
		scriptList = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		scriptList.setContentProvider(new ArrayContentProvider());
		scriptList.setLabelProvider(new ITableLabelProvider () {
			public Image getColumnImage(Object element, int columnIndex) {
				if (columnIndex == 0) return ScribeSharedImages.getImage(ScribeSharedImages.IMG_SCRIPT);
				
				return null;
			}
			
			public String getColumnText(Object element, int columnIndex) {
				if (columnIndex == 1) {
					return ((ServerScript)element).getName();
				}
				return "";
			}

			public void addListener(ILabelProviderListener listener) {}
			public void dispose() {	}
			public boolean isLabelProperty(Object element, String property) {
				return true;
			}
			public void removeListener(ILabelProviderListener listener) {}
		});
		
		scriptList.setSorter(new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				return ((ServerScript)e1).getName().compareTo(((ServerScript)e2).getName());
			}
		});
		
		scriptList.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				openScript((IStructuredSelection) scriptList.getSelection());
			}
		});
		
		TableColumn image = new TableColumn(scriptList.getTable(), SWT.NONE, 0);
		image.setWidth(16);
		
		TableColumn name = new TableColumn(scriptList.getTable(), SWT.NONE, 1);
		name.setWidth(200);
		
		
	}
	
	public void serverSettingsLoaded(final ServerSettings settings) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				scriptList.setInput(settings.getAllServerScripts());
			}
		});
	}

	protected void openScript (IStructuredSelection selection)
	{
		ServerScript script = (ServerScript) selection.getFirstElement();
		
		Shell shell = new Shell();
		shell.setText(script.getName());
		shell.setImage(ScribeSharedImages.getImage(ScribeSharedImages.IMG_SCRIPT));
		shell.setLayout(new GridLayout());
		
		Composite main = new Composite(shell, SWT.NONE);
		
		StyledText text = new StyledText(main, SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		text.setBackground(ColorUtil.warlockColorToColor(client.getServerSettings().getColorSetting(ColorType.MainWindow_Background)));
		text.setForeground(ColorUtil.warlockColorToColor(client.getServerSettings().getColorSetting(ColorType.MainWindow_Foreground)));
		text.setText(script.getScriptContents());
		text.setSize(640, 480);
		
		shell.pack();
		shell.layout();
		shell.open();
		
	}
	
	@Override
	public void setFocus() {
		scriptList.setInput(client.getServerSettings().getAllServerScripts());
	}

}
