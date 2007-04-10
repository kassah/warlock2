package com.arcaner.warlock.rcp.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.arcaner.warlock.client.ICompass;
import com.arcaner.warlock.client.IProperty;
import com.arcaner.warlock.client.IPropertyListener;
import com.arcaner.warlock.client.ICompass.DirectionType;
import com.arcaner.warlock.rcp.ui.client.SWTPropertyListener;
import com.arcaner.warlock.rcp.ui.style.CompassTheme;

/**
 * This is our custom compass that is drawn on top of the text widget and is movable like a normal window (within the text area for now).
 * @author marshall
 */
public class WarlockCompass implements PaintListener, MouseMoveListener, MouseListener, IPropertyListener<String> {

	private WarlockText text;
	private boolean dragging = false;
	private Point originalPosition = new Point(-1,-1), startedDraggingFrom = new Point(-1,-1);
	private int x = -1, y = -1;
	private Cursor moveCursor;
	private CompassTheme theme;
	private ICompass compass;
	
	private Image compassImage = WarlockSharedImages.getImage(WarlockSharedImages.IMG_COMPASS_SMALL_MAIN);
	private Rectangle compassBounds = compassImage.getBounds();
	
	public WarlockCompass (WarlockText text, CompassTheme theme)
	{	
		this.text = text;
		this.theme = theme;
		
		moveCursor = new Cursor(text.getDisplay(), SWT.CURSOR_HAND);
		compassImage = theme.getMainImage();
		compassBounds = compassImage.getBounds();	
		
		text.addPaintListener(this);
		text.addMouseListener(this);
		text.addMouseMoveListener(this);
		text.getVerticalBar().addSelectionListener(new SelectionListener () {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				redraw();
			}
		});
		text.addExtendedModifyListener(new ExtendedModifyListener() {
			public void modifyText(ExtendedModifyEvent e) {
				redraw();
			}
		});
	}
	
	public void paintControl(PaintEvent e) {
		drawCompass(e.gc);
	}
	
	public void redraw ()
	{
		this.text.redraw(x, 0, compassBounds.width, text.getBounds().height, true);
//		text.redraw();
	}
	
	private void drawCompass (GC gc)
	{
		if (x == -1)
			x = text.getClientArea().width - compassBounds.width - 5;
		if (y == -1)
			y = 5;
		
		gc.drawImage(compassImage, x, y);
		if (compass != null)
		{
			for (DirectionType direction : DirectionType.values())
			{
				if (direction != DirectionType.None && compass.getDirections().get(direction))
				{
					Point point = theme.getDirectionPosition(direction);
					gc.drawImage(theme.getDirectionImage(direction), point.x + x, point.y + y);
				}
			}
		}
	}
	
	private boolean insideCompass (int x, int y)
	{
		return (x >= this.x && x <= this.x+compassBounds.width
				&& y >= this.y && y <= this.y+compassBounds.height);
	}
	
	private boolean boundsInsideText (int topX, int topY)
	{
		int bottomX = topX + compassBounds.width;
		int bottomY = topY + compassBounds.height;
		Rectangle textBounds = text.getBounds();
		return textBounds.contains(bottomX, bottomY);
	}
	
	public void mouseDoubleClick(MouseEvent e) {}
	
	public void mouseDown(MouseEvent e) {
		if (insideCompass(e.x, e.y))
		{
			dragging = true;
			originalPosition.x = x; originalPosition.y = y;
			startedDraggingFrom.x = e.x; startedDraggingFrom.y = e.y;
		}
	}
	
	public void mouseMove(MouseEvent e) {
		if (dragging)
		{
			int newX = originalPosition.x + (e.x - startedDraggingFrom.x);
			int newY = originalPosition.y + (e.y - startedDraggingFrom.y);
			if (boundsInsideText(newX, newY))
			{
				this.x = newX;
				this.y = newY;
				text.redraw();
			}
		}
		
		if (insideCompass(e.x, e.y))
		{
			text.setCursor(moveCursor);
		}
	}
	
	public void propertyActivated(IProperty<String> property) {}
	public void propertyCleared(IProperty<String> property, String oldValue) {}
	public void propertyChanged(IProperty<String> property, String oldValue) {
		if (property.equals(compass))
		{
			redraw();
		}
	}
	
	
	public void mouseUp(MouseEvent e) {
		dragging = false;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public ICompass getCompass() {
		return compass;
	}

	public void setCompass(ICompass compass) {
		this.compass = compass;
		
		compass.addListener(new SWTPropertyListener<String>(this));
		redraw();
	}
	
}
