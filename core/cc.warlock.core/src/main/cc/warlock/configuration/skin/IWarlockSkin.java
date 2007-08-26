package cc.warlock.configuration.skin;

import cc.warlock.client.stormfront.WarlockColor;

public interface IWarlockSkin {

	public static enum FontFaceType {
		MainWindow_FontFace, MainWindow_MonoFontFace, CommandLine_FontFace
	};
	
	public static enum ColorType {
		MainWindow_Background, MainWindow_Foreground, CommandLine_Background, CommandLine_Foreground, CommandLine_BarColor
	};
	
	public static enum FontSizeType {
		MainWindow_FontSize, MainWindow_MonoFontSize, CommandLine_FontSize
	};
	
	public int getFontSize (FontSizeType type);
	
	public WarlockColor getColor (ColorType type);
	
	public String getFontFace (FontFaceType type);
}
