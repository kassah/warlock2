/*
 * Created on Feb 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.stormfront;

import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.xml.IStormFrontXMLHandler;

/**
 * @author sproctor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IStormFrontProtocolHandler extends IStormFrontXMLHandler {
	
	public void registerHandler(IStormFrontTagHandler tagHandler);
	
	public void removeHandler(IStormFrontTagHandler tagHandler);
	public IStormFrontClient getClient();
	public void pushStream(String name);
	public void popStream();
	public IStream getCurrentStream();
	
	public void addStyle(IWarlockStyle style);
	public void removeStyle(IWarlockStyle style);
	public void clearStyles();
	
	public void startSavingRawXML(StringBuffer buffer, String endOnTag);
	public void stopSavingRawXML();
	
	public IStormFrontTagHandler getTagHandler(Class<? extends IStormFrontTagHandler> handlerType);
}
