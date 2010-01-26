/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.rcp.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
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

import cc.warlock.core.client.ICompass;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.client.ICompass.DirectionType;
import cc.warlock.rcp.ui.style.CompassTheme;

/**
 * This is our custom compass that is drawn on top of the text widget and is movable like a normal window (within the text area for now).
 * @author marshall
 */
public class WarlockCompass implements PaintListener, MouseMoveListener, MouseListener, IPropertyListener<ICompass> {

	private WarlockText text;
	private boolean dragging = false;
	private Point originalPosition = new Point(-1,-1), startedDraggingFrom = new Point(-1,-1);
	private int x = -1, y = -1, rightDiff, bottomDiff;
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
		text.addControlListener(new ControlListener () {
			public void controlMoved(ControlEvent e) {}
			public void controlResized(ControlEvent e) {
				textResized(e);
			}
		});
		
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
		if (!this.text.isDisposed())
		{
			this.text.redraw(x, 0, compassBounds.width, text.getBounds().height, true);
		}
//		text.redraw();
	}
	
	private void drawCompass (GC gc)
	{
		if (x == -1)
			setX(text.getClientArea().width - compassBounds.width - 5);
		if (y == -1)
			setY(text.getClientArea().height - compassBounds.height - 5);
		
		gc.drawImage(compassImage, x, y);
		if (compass != null)
		{
			for (DirectionType direction : DirectionType.values())
			{
				if (direction != DirectionType.None && compass.getDirections().contains(direction))
				{
					Point point = theme.getDirectionPosition(direction);
					gc.drawImage(theme.getDirectionImage(direction), point.x + x, point.y + y);
				}
			}
		} else {
			// draw all "on" by default
			for (DirectionType direction : DirectionType.values())
			{
				if (direction != DirectionType.None)
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
				setX(newX);
				setY(newY);
				text.redraw();
			}
		}
		
		if (insideCompass(e.x, e.y))
		{
			text.setCursor(moveCursor);
		}
	}
	
	public void propertyCleared() {}
	
	public void propertyChanged(ICompass value) {
		compass = value;
		redraw();
	}
	
	protected void textResized (ControlEvent e)
	{
		Rectangle textBounds = text.getBounds();
		
		if (x <= rightDiff)
		{
			redraw();
		} else {
			setX(textBounds.width - rightDiff);
		}
		
		if (y <= bottomDiff)
		{
			redraw();
		} else {
			setY(textBounds.height - bottomDiff);
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
		this.rightDiff = text.getBounds().width - x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		
		this.bottomDiff = text.getBounds().height - y;
	}

	public ICompass getCompass() {
		return compass;
	}

}
