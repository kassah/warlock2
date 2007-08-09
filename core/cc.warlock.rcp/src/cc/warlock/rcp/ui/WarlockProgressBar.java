/*
 * Created on Jan 15, 2005
 */
package cc.warlock.rcp.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Marshall
 * 
 * This is a custom progress bar that mimics the L&F of StormFront's status bars.
 * It's sort of a dirty hack, but it suffices for now. It needs to handle being in a LayoutManager better...
 */
public class WarlockProgressBar extends Canvas
{
	protected Font progressFont;
	protected String label;
	protected Color foreground, background, borderColor;
	protected int min, max, selection;
	protected int width, height;
	protected int borderWidth;
	protected boolean showText;
	
	public WarlockProgressBar (Composite composite, int style)
	{
		super(composite, style);
		
		// defaults
		width = 100; height = 15;
		showText = true;
		
		progressFont = new Font(getShell().getDisplay(), "Arial", 8, SWT.NONE);
		foreground = new Color(getShell().getDisplay(), 255, 255, 255);
		background = new Color(getShell().getDisplay(), 0, 0, 0);
		borderColor = new Color(getShell().getDisplay(), 0, 0, 0);
		borderWidth = 1;
		
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if (label != null) {
					Rectangle bounds = getBounds();
					
					e.gc.setFont (progressFont);
					
					Point extent = e.gc.textExtent(label);
					
					int totalPixels = 0;
					for (int i = 0; i < label.length(); i++)
					{
						totalPixels += e.gc.getCharWidth(label.charAt(i));
						totalPixels += e.gc.getAdvanceWidth(label.charAt(i));
					}
					
					int left = (int) Math.floor(((bounds.width - (borderWidth * 2)) - extent.x) / 2.0);
					int top = (int) Math.floor(((bounds.height - (borderWidth * 2)) - e.gc.getFontMetrics().getHeight()) / 2.0);
					
					int barWidth = 0;
					
					if (max > min)
					{
						double decimal = (selection / ((double)(max - min)));
						barWidth = (int) Math.floor(decimal * (bounds.width - (borderWidth*2)) - 1);
					}
					
					e.gc.setBackground(background);
					e.gc.fillRectangle(borderWidth, borderWidth, barWidth, (bounds.height - (borderWidth*2)));
					
					e.gc.setForeground(borderColor);
					e.gc.setLineWidth(borderWidth);
					e.gc.drawRectangle(0, 0, bounds.width, bounds.height);
					
					if (showText)
					{
						e.gc.setForeground(foreground);
						e.gc.drawText (label, left, top, true);
					}
				}
			}
		});
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public Point computeSize(int wHint, int hHint, boolean changed) {
		
		return new Point (width, height);
	}
	
	public void setForeground (Color color)
	{
		foreground = color;
		redraw();
	}
	
	public void setBackground (Color color)
	{
		background = color;
		redraw();
	}
	
	public void setLabel (String label)
	{
		this.label = label;
		redraw();
	}
	
	public void setMinimum (int min)
	{
		this.min = min;
	}
	
	public void setMaximum (int max)
	{
		this.max = max;
	}
	
	public int getSelection ()
	{
		return selection;
	}
	
	public void setSelection (int selection)
	{
		this.selection = selection;
		redraw();
	}
	
	public void setShowText (boolean showText)
	{
		this.showText = showText;
		redraw();
	}
	
	public void dispose() {
		background.dispose();
		foreground.dispose();
		progressFont.dispose();
		
		super.dispose();
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}
}
