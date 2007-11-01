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

	public void handleStart(StormFrontAttributeList attributes) {
		if(currentStyle != null) {
			handler.getCurrentStream().removeStyle(currentStyle);
		}
	
		String classId = attributes.getValue("class");
		
		if (classId != null) {
			currentStyle = new WarlockStyle();
		
			if(classId.equals("mono")) {
				currentStyle.addStyleType(IWarlockStyle.StyleType.MONOSPACE);
			}
			handler.getCurrentStream().addStyle(currentStyle);
		}
	}
}
