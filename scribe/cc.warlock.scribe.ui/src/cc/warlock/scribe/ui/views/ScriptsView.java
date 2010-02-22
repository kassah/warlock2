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
import cc.warlock.core.script.IScriptInfo;
import cc.warlock.core.script.IScriptProvider;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.rcp.views.GameView;
import cc.warlock.scribe.ui.ScribeSharedImages;

public class ScriptsView extends ViewPart {

	public static final String VIEW_ID = "cc.warlock.scribe.ui.views.ScriptsView";
	
	protected TableViewer scriptList;
	
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
					return ((IScriptInfo)element).getScriptName();
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
				return ((IScriptInfo)e1).getScriptName().compareTo(((IScriptInfo)e2).getScriptName());
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
		
//		for (IWarlockClient client : WarlockClientRegistry.getActiveClients())
//		{
//			if (client instanceof IStormFrontClient)
//			{
//				serverSettingsLoaded(((IStormFrontClient)client).getServerSettings());
//			}
//		}
//		WarlockClientRegistry.addWarlockClientListener(new WarlockClientAdapter() {
//			public void clientConnected(IWarlockClient client) {
//				addListener(client);
//			}
//		});
	}
	
	public void serverSettingsLoaded() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				ArrayList<IScriptInfo> scripts = new ArrayList<IScriptInfo>();
				for (IScriptProvider provider : ScriptEngineRegistry.getScriptProviders())
				{
					for (GameView gv: GameView.getOpenGameViews()) {
						scripts.addAll(provider.getScriptInfos(gv.getWarlockClient().getClientPreferences()));
					}
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
		IScriptInfo scriptInfo = (IScriptInfo) selection.getFirstElement();
		
		Shell shell = new Shell();
		shell.setText(scriptInfo.getScriptName());
		shell.setImage(ScribeSharedImages.getImage(ScribeSharedImages.IMG_SCRIPT));
		shell.setLayout(new GridLayout());
		
		Composite main = new Composite(shell, SWT.NONE);
		
		StyledText text = new StyledText(main, SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		
//		if (scriptInfo instanceof IServerScriptInfo)
//		{
//			IServerScriptInfo info = (IServerScriptInfo) scriptInfo;
//			IStormFrontClient client = info.getClient();
//
//			text.setBackground(ColorUtil.warlockColorToColor(client.getServerSettings().getMainWindowSettings().getBackgroundColor()));
//			text.setForeground(ColorUtil.warlockColorToColor(client.getServerSettings().getMainWindowSettings().getForegroundColor()));
//		}
		
		Reader reader = scriptInfo.openReader();
		text.setText(readerToString(reader));
		
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		text.setSize(640, 480);
		
		shell.pack();
		shell.layout();
		shell.open();
	}
	
	@Override
	public void setFocus() {
	}

}
