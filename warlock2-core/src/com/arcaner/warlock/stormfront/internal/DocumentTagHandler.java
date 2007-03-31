package com.arcaner.warlock.stormfront.internal;

import java.util.Hashtable;

import org.xml.sax.Attributes;

import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.stormfront.internal.StormFrontStyle;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

/**
 * This is a special tag handler that buffers incoming characters (upon request) so they can be used in a more fine-grained fashion.
 * @author Marshall
 */
public class DocumentTagHandler extends DefaultTagHandler {

	private static Hashtable<String, StringBuffer> clientBuffers = new Hashtable <String, StringBuffer>();
	
	public DocumentTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String getName() {
		return "document";
	}
	
	@Override
	public boolean handleCharacters(char[] ch, int start, int length) {
		String str = new String(ch);
		String toAppend = str.substring(start, start + length);

		boolean handled = false;
		for (StringBuffer buffer : clientBuffers.values())
		{
			buffer.append(toAppend);
			handled = true;
		}
		
		if (!handled)
		{
			handler.getClient().append(IWarlockClient.DEFAULT_VIEW, toAppend, StormFrontStyle.EMPTY_STYLE);
		}
		
		return true;
	}
	
	public static void startCollecting (String id)
	{
		clientBuffers.put(id, new StringBuffer());
	}
	
	public static StringBuffer getBuffer (String id)
	{
		return clientBuffers.get(id);
	}
	
	public static void stopCollecting (String id)
	{
		clientBuffers.remove(id);
	}
}
