package cc.warlock.core.client.internal;

import java.util.ArrayList;
import java.util.Collection;

import cc.warlock.core.client.IStyledString;
import cc.warlock.core.client.IWarlockStyle;

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
	
	public void append (String string) {
		StyledString styledString = new StyledString();
		styledString.getBuffer().append(string);
		
		append(styledString);
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
	
	public void clear()
	{
		buffer.setLength(0);
		styles.clear();
	}
}
