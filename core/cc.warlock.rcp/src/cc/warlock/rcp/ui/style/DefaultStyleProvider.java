package cc.warlock.rcp.ui.style;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.rcp.ui.IStyleProvider;
import cc.warlock.rcp.ui.StyleRangeWithData;
import cc.warlock.rcp.ui.WarlockText;

public class DefaultStyleProvider implements IStyleProvider {
	
	protected static DefaultStyleProvider _instance;
	protected DefaultStyleProvider () { }
	
	public static DefaultStyleProvider instance()
	{
		if (_instance == null) {
			_instance = new DefaultStyleProvider();
		}
		return _instance;
	}
	
	public StyleRangeWithData getStyleRange (IWarlockStyle style, int start)
	{
		if (style.getStyleTypes().contains(IWarlockStyle.StyleType.EMPTY))
			return null;
		
		StyleRangeWithData range = new StyleRangeWithData();
		range.start = start;
		range.fontStyle = 0;
		
		for (IWarlockStyle.StyleType styleType : style.getStyleTypes())
		{
			if (styleType.equals(IWarlockStyle.StyleType.BOLD))
				range.fontStyle |= SWT.BOLD;
			else if (styleType.equals(IWarlockStyle.StyleType.ITALIC))
				range.fontStyle |= SWT.ITALIC;
			else if (styleType.equals(IWarlockStyle.StyleType.UNDERLINE) || styleType.equals(IWarlockStyle.StyleType.LINK))
				range.underline = true;
			else if (styleType.equals(IWarlockStyle.StyleType.MONOSPACE))
			{
				range.font = JFaceResources.getTextFont();
			}
		}
		
		return range;
	}
	
	public StyleRangeWithData getEchoStyle (int start)
	{
		return getStyleRange(WarlockStyle.createCustomStyle("command"), start);
	}
}
