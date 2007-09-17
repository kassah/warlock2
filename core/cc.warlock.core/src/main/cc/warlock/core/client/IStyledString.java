package cc.warlock.core.client;

import java.util.Collection;

public interface IStyledString {

	public StringBuffer getBuffer();
	
	public Collection<IWarlockStyle> getStyles();

	public void addStyle (IWarlockStyle style);
	public void addStyle (IWarlockStyle style, int position);
	
	public void append (IStyledString string);
	
	public boolean readyToFlush();
}
