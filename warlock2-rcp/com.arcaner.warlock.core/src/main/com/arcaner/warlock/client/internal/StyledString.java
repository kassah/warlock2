package com.arcaner.warlock.client.internal;

import java.util.ArrayList;
import java.util.Collection;

import com.arcaner.warlock.client.IStyledString;
import com.arcaner.warlock.client.IWarlockStyle;

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
	
	public boolean readyToFlush() {
		return buffer.indexOf("\n") > -1;
	}
}
