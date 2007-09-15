package cc.warlock.client.internal;

import java.util.ArrayList;
import java.util.Collection;

import cc.warlock.client.IStyledString;
import cc.warlock.client.IWarlockStyle;

public class StyledString implements IStyledString {

	protected StringBuffer buffer = new StringBuffer();
	protected ArrayList<IWarlockStyle> styles = new ArrayList<IWarlockStyle>();
	
	public StringBuffer getBuffer() {
		return buffer;
	}

	public Collection<IWarlockStyle> getStyles() {
		return styles;
	}

	public void addStyle(IWarlockStyle style) {
		styles.add(style);
	}
	
	public void addStyle(IWarlockStyle style, int position) {
		styles.add(position, style);
	}
	
	public void append(IStyledString string) {
		if (!string.equals(this))
		{
			int offset = buffer.length();
			buffer.append(string.getBuffer());
			
			for (IWarlockStyle style : string.getStyles())
			{
				style.setStart(style.getStart() + offset);
				addStyle(style);
			}
		}
	}
	
	public boolean readyToFlush() {
		return buffer.indexOf("\n") > -1;
	}
}
