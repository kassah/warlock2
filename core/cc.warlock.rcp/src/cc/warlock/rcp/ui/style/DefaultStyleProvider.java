package cc.warlock.rcp.ui.style;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.rcp.ui.IStyleProvider;
import cc.warlock.rcp.ui.StyleRangeWithData;

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
	
	public StyleRangeWithData getStyleRange (IWarlockStyle style)
	{	
		StyleRangeWithData range = new StyleRangeWithData();
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
}
