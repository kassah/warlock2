package com.arcaner.warlock.configuration.skin;

/**
 * Eventually this will allow for pluggable skins, but for now we just provide an entry point for the default skin
 * @author marshall
 */
public class WarlockSkins {

	protected static DefaultSkin defaultSkin = new DefaultSkin();
	
	public static DefaultSkin getDefault() {
		return defaultSkin;
	}
}
