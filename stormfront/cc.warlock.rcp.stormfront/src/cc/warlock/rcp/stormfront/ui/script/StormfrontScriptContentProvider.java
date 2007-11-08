package cc.warlock.rcp.stormfront.ui.script;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import cc.warlock.core.script.IScriptInfo;
import cc.warlock.core.script.IScriptProvider;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.stormfront.serversettings.server.IServerScriptInfo;
import cc.warlock.core.stormfront.serversettings.server.IServerScriptProvider;
import cc.warlock.rcp.application.WarlockApplication;

public class StormfrontScriptContentProvider implements ITreeContentProvider {

	protected IServerScriptProvider[] getServerScriptProviders ()
	{
		ArrayList<IServerScriptProvider> providers = new ArrayList<IServerScriptProvider>();
		for (IScriptProvider p : ScriptEngineRegistry.getScriptProviders())
		{
			if (p instanceof IServerScriptProvider)
			{
				IServerScriptProvider provider = (IServerScriptProvider) p;
				
				if (provider.getClient().getPlayerId().get() != null)
				{
					providers.add(provider);
				}
			}
		}
		
		return providers.toArray(new IServerScriptProvider[providers.size()]);
	}
	
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof WarlockApplication)
		{
			return getServerScriptProviders();
		}
		else if (parentElement instanceof IServerScriptProvider)
		{
			IServerScriptProvider provider = (IServerScriptProvider) parentElement;
			List<IScriptInfo> scriptInfos = provider.getScriptInfos();
			
			return scriptInfos.toArray(new IScriptInfo[scriptInfos.size()]);
		}
		
		return new Object[0];
	}

	public Object getParent(Object element) {
		if (element instanceof IServerScriptInfo)
		{
			IServerScriptInfo info = (IServerScriptInfo)element;
			return info.getProvider();
		}
		else if (element instanceof IServerScriptProvider)
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
		else if (element instanceof IServerScriptProvider)
		{
			IServerScriptProvider provider = (IServerScriptProvider) element;
			return !provider.getScriptInfos().isEmpty();
		}
		return false;
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public void dispose() {	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {	}
}
