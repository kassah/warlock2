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
