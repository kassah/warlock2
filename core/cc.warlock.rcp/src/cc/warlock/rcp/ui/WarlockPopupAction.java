package cc.warlock.rcp.ui;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class WarlockPopupAction extends Composite implements PaintListener {

	protected Composite parent;
	protected Color background;
	protected Font smallFont;
	
	public WarlockPopupAction (Composite parent, int style)
	{
		super(parent, style);
		
		this.parent = parent;
		parent.addPaintListener(this);
		addPaintListener(this);
		
		this.background = new Color(getDisplay(), 239, 243, 193);
		setBackground(background);
		setCursor(getDisplay().getSystemCursor(SWT.CURSOR_ARROW));

		FontData defaultFontData = JFaceResources.getDefaultFont().getFontData()[0];
		int height = Math.max(8, defaultFontData.getHeight() - 1);
		
		smallFont = new Font(getDisplay(), defaultFontData.getName(), height, defaultFontData.getStyle());
		setFont(smallFont);
	}
	
	protected boolean fontSet = false;
	protected void setChildrenFont (Composite composite)
	{
		for (Control child : composite.getChildren())
		{
			child.setFont(smallFont);
			if (child instanceof Composite)
			{
				setChildrenFont((Composite)child);
			}
		}
	}
	
	public void paintControl(PaintEvent e) {
		if (isVisible())
		{
			if (!fontSet) {
				setChildrenFont(this);
				fontSet = true;
			}
			
			setLocation(0, 0);
			GC gc = e.gc;
	
			pack();
			Rectangle parentBounds = parent.getBounds();
			int height = getBounds().height;
			
			gc.setForeground(new Color(getDisplay(), 0, 0, 0));
			gc.setBackground(background);
			gc.fillRoundRectangle(0, 0, parentBounds.width, height - 4, 2, 2);
			
			gc.setBackground(new Color(getDisplay(), 164, 167, 133));
			gc.fillRectangle(0, height-4, parentBounds.width, 4);
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		parent.redraw();

		if (visible)
		{
			for (Control child : getChildren())
			{
				child.pack();
			}
		}
	}
}
