package com.arcaner.warlock.configuration.skin;

import com.arcaner.warlock.client.stormfront.WarlockColor;

public interface IWarlockSkin {

	public static enum FontFaceType {
		MainWindow_FontFace, MainWindow_MonoFontFace, CommandLine_FontFace
	};
	
	public static enum ColorType {
		MainWindow_Background, MainWindow_Foreground, CommandLine_Background, CommandLine_Foreground
	};
	
	public static enum FontSizeType {
		MainWindow_FontSize, MainWindow_MonoFontSize, CommandLine_FontSize
	};
	
	public int getFontSize (FontSizeType type);
	
	public WarlockColor getColor (ColorType type);
	
	public String getFontFace (FontFaceType type);
}
