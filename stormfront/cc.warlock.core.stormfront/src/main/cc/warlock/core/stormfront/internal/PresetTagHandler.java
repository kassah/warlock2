package cc.warlock.core.stormfront.internal;

import java.util.Stack;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.serversettings.server.Preset;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class PresetTagHandler extends DefaultTagHandler {

	private Stack<IWarlockStyle> styles = new Stack<IWarlockStyle>();
	
	public PresetTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "preset" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
		String id = attributes.getValue("id");
		Preset preset = handler.getClient().getServerSettings().getPreset(id);
		
		IWarlockStyle style;
		if(preset != null)
			style = preset.getStyle();
		else
			style = new WarlockStyle();

		styles.push(style);
		handler.addStyle(style);
		
		if(newLine != null && newLine.length() > 0) {
			char[] chars = new char[newLine.length()];
			newLine.getChars(0, newLine.length(), chars, 0);
			handler.characters(chars, 0, newLine.length());
		}
	}
	
	@Override
	public void handleEnd(String newLine) {
		IWarlockStyle style = styles.pop();
		handler.removeStyle(style);
		
		if(newLine != null && newLine.length() > 0) {
			char[] chars = new char[newLine.length()];
			newLine.getChars(0, newLine.length(), chars, 0);
			handler.characters(chars, 0, newLine.length());
		}
	}

}
