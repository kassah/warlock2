package cc.warlock.rcp.stormfront.ui.script;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.serversettings.server.IServerScriptInfo;
import cc.warlock.core.stormfront.serversettings.server.IServerScriptProvider;
import cc.warlock.rcp.stormfront.ui.StormFrontSharedImages;

public class StormfrontScriptLabelProvider implements ILabelProvider {

	public Image getImage(Object element) {
		if (element instanceof IServerScriptInfo)
		{
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
		}
		if (element instanceof IServerScriptProvider)
		{
			return StormFrontSharedImages.getImage(StormFrontSharedImages.IMG_STORMFRONT_SCRIPTS);
		}
		return null;
	}

	public String getText(Object element) {
		if (element instanceof IServerScriptInfo)
		{
			return ((IServerScriptInfo)element).getScriptName();
		}
		else if (element instanceof IServerScriptProvider)
		{
			IStormFrontClient client = ((IServerScriptProvider)element).getClient();
			
			return client.getCharacterName().get() + "'s Scripts";
		}
		return element.toString();
	}

	public void addListener(ILabelProviderListener listener) {	}

	public void dispose() {	}

	public boolean isLabelProperty(Object element, String property) {
		return true;
	}

	public void removeListener(ILabelProviderListener listener) {	}
}
