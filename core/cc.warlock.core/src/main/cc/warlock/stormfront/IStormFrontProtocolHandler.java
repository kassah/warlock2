/*
 * Created on Feb 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.stormfront;

import cc.warlock.client.IStream;
import cc.warlock.client.IStyledString;
import cc.warlock.client.IWarlockStyle;
import cc.warlock.client.stormfront.IStormFrontClient;

/**
 * @author sproctor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IStormFrontProtocolHandler {
	
	public void registerHandler(IStormFrontTagHandler tagHandler);
	public void registerHandler(IStormFrontTagHandler tagHandler, int priority);
	
	public void removeHandler(IStormFrontTagHandler tagHandler);
	public IStormFrontClient getClient();
	public void pushStream(String name);
	public void popStream();
	public IStream getCurrentStream();
	public IWarlockStyle getCurrentStyle();
	
	public void pushBuffer();
	public IStyledString popBuffer();
	public IStyledString peekBuffer();
	public void sendAndPopBuffer();
	
	public void startSavingRawXML(StringBuffer buffer, String endOnTag);
	public void stopSavingRawXML();
	
	public void setCurrentStyle (IWarlockStyle style);
	public void clearCurrentStyle ();
}
