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

import java.io.File;
import java.io.IOException;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;

import cc.warlock.core.script.IFilesystemScriptProvider;
import cc.warlock.core.script.ScriptEngineRegistry;

public class NewScriptWizard extends Wizard {
	
	protected NewScriptWizardPage page1;
	protected IWorkbenchPage page;
	
	
	public NewScriptWizard (IWorkbenchPage page)
	{
		this.page = page;
	}
	
	@Override
	public void addPages() {
		page1 = new NewScriptWizardPage();
		addPage(page1);
	}
	
	@Override
	public boolean performFinish() {
		File scriptFile = new File(page1.getScriptDir(), page1.getScriptName() + "." + page1.getScriptExt());
		
		try {
			File dir = scriptFile.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			scriptFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IFilesystemScriptProvider provider = (IFilesystemScriptProvider)
			ScriptEngineRegistry.getScriptProvider(IFilesystemScriptProvider.class);
		
		if (provider != null)
		{
			provider.forceScan();
		}
		
		
		
		IFileStore fileStore = EFS.getLocalFileSystem().fromLocalFile(scriptFile);
		try {
			page.openEditor(new FileStoreEditorInput(fileStore), "org.eclipse.ui.DefaultTextEditor");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
