/*
 * Created on Feb 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.stormfront;

import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.client.stormfront.IStormFrontStyle;

/**
 * @author sproctor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IStormFrontProtocolHandler {
	
	public void registerHandler(IStormFrontTagHandler tagHandler);
	public IStormFrontClient getClient();
	public void pushStream(String name);
	public void popStream();
	public IStream getCurrentStream();
	
	public void pushBuffer();
	public StringBuffer popBuffer();
	
	public void startSavingRawXML(StringBuffer buffer, String endOnTag);
	public void stopSavingRawXML();
	
	public void setCurrentStyle (IStormFrontStyle style);
	public void clearCurrentStyle ();
}
