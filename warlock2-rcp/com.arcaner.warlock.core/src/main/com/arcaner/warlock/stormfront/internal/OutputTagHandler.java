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
			currentStyle.setLength(handler.peekBuffer().getBuffer().length());
			handler.peekBuffer().addStyle(currentStyle, 0);
			
			handler.sendAndPopBuffer();
			handler.clearCurrentStyle();
		}
		else
		{
			handler.pushBuffer();
			currentStyle = WarlockStyle.createCustomStyle(clazz, 0, -1);
			
			if (clazz.equals("mono")) {
				currentStyle.addStyleType(IWarlockStyle.StyleType.MONOSPACE);
			}
			handler.setCurrentStyle(currentStyle);
			
		}
	}

}
