package com.arcaner.warlock.client;

import java.util.Collection;

public interface IStyledString {

	public StringBuffer getBuffer();
	
	public Collection<IWarlockStyle> getStyles();

	public void addStyle (IWarlockStyle style);
	
	public boolean readyToFlush();
}
