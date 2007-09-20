package cc.warlock.scribe.ui.views;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

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
import cc.warlock.core.client.WarlockClientAdapter;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.IWarlockSkin.ColorType;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptProvider;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.serversettings.server.IServerScriptInfo;
import cc.warlock.core.stormfront.serversettings.server.IServerSettingsListener;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;
import cc.warlock.rcp.util.ColorUtil;
import cc.warlock.scribe.ui.ScribeSharedImages;

public class ScriptsView extends ViewPart implements IServerSettingsListener {

	public static final String VIEW_ID = "cc.warlock.scribe.ui.views.ScriptsView";
	
	protected TableViewer scriptList;
	
	protected void addListener (IWarlockClient client)
	{
		if (client instanceof IStormFrontClient)
		{
			((IStormFrontClient)client).getServerSettings().addServerSettingsListener(ScriptsView.this);
		}
	}
	
	@Override
	public void createPartControl(Composite parent) {
		scriptList = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		scriptList.setContentProvider(new ArrayContentProvider());
		scriptList.setLabelProvider(new ITableLabelProvider () {
			public Image getColumnImage(Object element, int columnIndex) {
				if (columnIndex == 0) return ScribeSharedImages.getImage(ScribeSharedImages.IMG_SCRIPT);
				
				return null;
			}
			
			public String getColumnText(Object element, int columnIndex) {
				if (columnIndex == 1) {
					return ((IScript)element).getName();
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
				return ((IScript)e1).getName().compareTo(((IScript)e2).getName());
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
		
		for (IWarlockClient client : WarlockClientRegistry.getActiveClients())
		{
			if (client instanceof IStormFrontClient)
			{
				serverSettingsLoaded(((IStormFrontClient)client).getServerSettings());
			}
		}
		WarlockClientRegistry.addWarlockClientListener(new WarlockClientAdapter() {
			public void clientConnected(IWarlockClient client) {
				addListener(client);
			}
		});
	}
	
	public void serverSettingsLoaded(final ServerSettings settings) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				ArrayList<IScript> scripts = new ArrayList<IScript>();
				for (IScriptProvider provider : ScriptEngineRegistry.getScriptProviders())
				{
					scripts.addAll(provider.getScripts());
				}
				scriptList.setInput(scripts);
			}
		});
	}

	protected String readerToString (Reader reader)
	{
		try {
			StringBuffer string = new StringBuffer();
			
			char[] bytes = new char[1024];
			int size = 0;
			
			while (size != -1)
			{	
				size = reader.read(bytes);
				if (size != -1)
					string.append(bytes, 0, size);
			}
			reader.close();
			
			return string.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	protected void openScript (IStructuredSelection selection)
	{
		IScript script = (IScript) selection.getFirstElement();
		
		Shell shell = new Shell();
		shell.setText(script.getName());
		shell.setImage(ScribeSharedImages.getImage(ScribeSharedImages.IMG_SCRIPT));
		shell.setLayout(new GridLayout());
		
		Composite main = new Composite(shell, SWT.NONE);
		
		StyledText text = new StyledText(main, SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		
		if (script.getScriptInfo() instanceof IServerScriptInfo)
		{
			IServerScriptInfo info = (IServerScriptInfo) script.getScriptInfo();
			IStormFrontClient client = info.getClient();

			text.setBackground(ColorUtil.warlockColorToColor(client.getServerSettings().getColorSetting(ColorType.MainWindow_Background)));
			text.setForeground(ColorUtil.warlockColorToColor(client.getServerSettings().getColorSetting(ColorType.MainWindow_Foreground)));
		}
		
		text.setText(readerToString(script.getScriptInfo().openReader()));
		text.setSize(640, 480);
		
		shell.pack();
		shell.layout();
		shell.open();
	}
	
	@Override
	public void setFocus() {
	}

}
