package cc.warlock.rcp.stormfront.ui.script;

import java.text.Collator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import cc.warlock.core.stormfront.serversettings.server.IServerScriptInfo;

public class ServerScriptSorter extends ViewerSorter {

	public ServerScriptSorter() {
		// TODO Auto-generated constructor stub
	}

	public ServerScriptSorter(Collator collator) {
		super(collator);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		int comparison = super.compare(viewer, e1, e2);
		
		if (e1 instanceof IServerScriptInfo && e2 instanceof IServerScriptInfo)
		{
			IServerScriptInfo s1 = (IServerScriptInfo)e1;
			IServerScriptInfo s2 = (IServerScriptInfo)e2;
			
			return s1.getScriptName().compareTo(s2.getScriptName());
		}
		
		return comparison;
	}
}
