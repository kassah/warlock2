package cc.warlock.core.stormfront.internal;

import java.net.MalformedURLException;
import java.net.URL;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.IStormFrontClientViewer;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class ResourceTagHandler extends DefaultTagHandler {

	public ResourceTagHandler (IStormFrontProtocolHandler handler)
	{
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "resource" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
		String pictureId = null;
		
		if (attributes.getValue("picture") != null)
			pictureId = attributes.getValue("picture");
		else if (attributes.getValue("id") != null)
			pictureId = attributes.getValue("id");
		
		if (pictureId != null)
		{
			try {
				URL url = new URL("http://www.play.net/bfe/DR-art/" + pictureId + "_t.jpg");
				
				for (IWarlockClientViewer viewer : handler.getClient().getViewers())
				{
					if (viewer instanceof IStormFrontClientViewer)
					{
						IStormFrontClientViewer sfViewer = (IStormFrontClientViewer) viewer;
						sfViewer.appendImage(url);
					}
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
