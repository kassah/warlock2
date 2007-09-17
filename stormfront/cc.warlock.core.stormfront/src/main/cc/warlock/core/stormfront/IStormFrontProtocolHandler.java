/*
 * Created on Feb 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.stormfront;

import java.util.Map;

import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStyledString;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.stormfront.client.IStormFrontClient;

/**
 * @author sproctor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IStormFrontProtocolHandler {
	
	public void registerHandler(IStormFrontTagHandler tagHandler);
	
	public void removeHandler(IStormFrontTagHandler tagHandler);
	public IStormFrontClient getClient();
	public void pushStream(String name);
	public void popStream();
	public IStream getCurrentStream();
	public IWarlockStyle getCurrentStyle();
	
	public void characters(char[] ch, int start, int length);
	public void startElement(String name, Map<String,String> attributes);
	public void endElement(String name);
	
	public void pushBuffer();
	public IStyledString popBuffer();
	public IStyledString peekBuffer();
	public void sendAndPopBuffer();
	
	public void startSavingRawXML(StringBuffer buffer, String endOnTag);
	public void stopSavingRawXML();
	
	public void setCurrentStyle (IWarlockStyle style);
	public void clearCurrentStyle ();
}
