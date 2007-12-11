package cc.warlock.rcp.ui.script;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import cc.warlock.core.script.IFilesystemScriptProvider;
import cc.warlock.core.script.IScriptInfo;
import cc.warlock.core.script.IScriptProvider;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.rcp.application.WarlockApplication;

public class ScriptFilesystemContentProvider implements ITreeContentProvider
{
	public static final String LOCAL_SCRIPTS = "localScripts";
	
	protected IFilesystemScriptProvider getFilesystemScriptProvider ()
	{
		IFilesystemScriptProvider provider = null;
		for (IScriptProvider p : ScriptEngineRegistry.getScriptProviders())
		{
			if (p instanceof IFilesystemScriptProvider)
			{
				provider = (IFilesystemScriptProvider) p;
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
			IFilesystemScriptProvider provider = getFilesystemScriptProvider();
			
			List<IScriptInfo> scriptInfos = provider.getScriptInfos();
			
			return scriptInfos.toArray(new IScriptInfo[scriptInfos.size()]);
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
			IFilesystemScriptProvider provider = getFilesystemScriptProvider();
			if (provider != null)
			{
				return true;
			}
		}
		
		return false;
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public void dispose() {	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {	}
	
}