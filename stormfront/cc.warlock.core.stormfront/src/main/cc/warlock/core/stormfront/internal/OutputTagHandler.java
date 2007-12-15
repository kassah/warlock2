package cc.warlock.core.stormfront.internal;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class OutputTagHandler extends DefaultTagHandler {
	private IWarlockStyle currentStyle;
	
	/**
	 * @param handler
	 */
	public OutputTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] {"output"};
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
		if(currentStyle != null) {
			handler.removeStyle(currentStyle);
		}
	
		String className = attributes.getValue("class");
		
		if (className != null) {
			currentStyle = new WarlockStyle();
		
			if(className.equals("mono")) {
				currentStyle.addStyleType(IWarlockStyle.StyleType.MONOSPACE);
			}
			currentStyle.setName(className);
			handler.addStyle(currentStyle);
		}
	}
}
