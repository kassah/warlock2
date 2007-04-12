package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.client.IWarlockStyle;
import com.arcaner.warlock.client.internal.WarlockStyle;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class OutputTagHandler extends DefaultTagHandler {
	private WarlockStyle currentStyle;
	
	public OutputTagHandler (IStormFrontProtocolHandler handler) {
		super (handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "output" };
	}
	
	@Override
	public void handleStart(Attributes attributes) {
		String clazz = attributes.getValue("class");
		
		if (clazz == null || clazz.length() == 0)
		{
			StringBuffer buffer = handler.popBuffer();
			handler.getClient().getDefaultStream().send(buffer.toString(), currentStyle);	
		}
		else
		{
			handler.pushBuffer();
			currentStyle = WarlockStyle.createCustomStyle(clazz);
			
			if (clazz.equals("mono")) {
				currentStyle.addStyleType(IWarlockStyle.StyleType.MONOSPACE);
			}
		}
	}

}
