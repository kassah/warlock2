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
package cc.warlock.rcp.ui.script;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import cc.warlock.core.script.IScriptInfo;
import cc.warlock.core.script.IScriptProvider;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.script.internal.FilesystemScriptProvider;
import cc.warlock.rcp.application.WarlockApplication;
import cc.warlock.rcp.views.GameView;

public class ScriptFilesystemContentProvider implements ITreeContentProvider
{
	public static final String LOCAL_SCRIPTS = "localScripts";
	
	protected FilesystemScriptProvider getFilesystemScriptProvider ()
	{
		FilesystemScriptProvider provider = null;
		for (IScriptProvider p : ScriptEngineRegistry.getScriptProviders())
		{
			if (p instanceof FilesystemScriptProvider)
			{
				provider = (FilesystemScriptProvider) p;
			}
		}
		
		return provider;
	}
	
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof WarlockApplication)
		{
			return new Object[] { LOCAL_SCRIPTS };
		}
		else if (parentElement.equals(LOCAL_SCRIPTS))
		{
			FilesystemScriptProvider provider = getFilesystemScriptProvider();
			
			return provider.getScriptInfos(GameView.getGameViewInFocus().getWarlockClient().getClientPreferences());
		}
		
		return new Object[0];
	}

	public Object getParent(Object element) {
		if (element instanceof IScriptInfo)
		{
			return LOCAL_SCRIPTS;
		}
		else if (element.equals(LOCAL_SCRIPTS))
		{
			return WarlockApplication.instance();
		}
		
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof WarlockApplication)
		{
			return true;
		}
		else if (element.equals(LOCAL_SCRIPTS))
		{
			return (getFilesystemScriptProvider() != null);
		}
		
		return false;
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public void dispose() {	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {	}
	
}