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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.WarlockString.WarlockStringStyleRange;

/**
 * This is an extension of the StyledText widget which has special support for embedding of arbitrary Controls/Links
 * 
 * To embed a control in the widget, add the constant OBJECT_HOLDER where you want to see your control, and then use
 * addLink / addImage / addControl to add a control for that placeholder.
 * 
 * @author Marshall
 */
public class WarlockText {

	public static final char OBJECT_HOLDER = '\uFFFc';
	
	private StyledText textWidget;
	private Cursor handCursor, defaultCursor;
	private int lineLimit = 5000;
	private int doScrollDirection = SWT.DOWN;
	private IStyleProvider styleProvider;
	private Menu contextMenu;
	private HashMap<String, WarlockTextMarker> markers = new HashMap<String, WarlockTextMarker>();
	
	private class WarlockTextMarker {
		public int offset;
		public int length;
		
		public WarlockTextMarker(int offset, int length) {
			this.offset = offset;
			this.length = length;
		}
	}
	
	public WarlockText(Composite parent) {
		textWidget = new StyledText(parent, SWT.V_SCROLL);

		textWidget.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		textWidget.setEditable(false);
		textWidget.setWordWrap(true);
		textWidget.setIndent(1);

		ISharedImages images = PlatformUI.getWorkbench().getSharedImages();

		Display display = parent.getDisplay();
		handCursor = new Cursor(display, SWT.CURSOR_HAND);
		defaultCursor = parent.getCursor();

		contextMenu = new Menu(textWidget);
		MenuItem itemCopy = new MenuItem(contextMenu, SWT.PUSH);
		itemCopy.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				textWidget.copy();
			}
		});
		itemCopy.setText("Copy");
		itemCopy.setImage(images.getImage(ISharedImages.IMG_TOOL_COPY));
		MenuItem itemClear = new MenuItem(contextMenu, SWT.PUSH);
		itemClear.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				textWidget.setText("");
			}
		});
		itemClear.setText("Clear");
		itemClear.setImage(images.getImage(ISharedImages.IMG_TOOL_DELETE));
		textWidget.setMenu(contextMenu);

		textWidget.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				try {
					if (!textWidget.isDisposed() && textWidget.isVisible())
					{
						Point point = new Point(e.x, e.y);
						int offset = textWidget.getOffsetAtLocation(point);
						StyleRange range = textWidget.getStyleRangeAtOffset(offset);
						if (range != null && range instanceof StyleRangeWithData)
						{
							StyleRangeWithData range2 = (StyleRangeWithData) range;
							if (range2.action != null)
							{
								textWidget.setCursor(handCursor);
								return;
							}

						}
						textWidget.setCursor(defaultCursor);
					}
				} catch (IllegalArgumentException ex) {
					// swallow -- this happens if the mouse cursor moves to an
					// area not covered by the imaginary rectangle surround the
					// current text
					textWidget.setCursor(defaultCursor);
				}
			}
		});

		textWidget.addMouseListener(new MouseListener () {
			public void mouseDoubleClick(MouseEvent e) {}
			public void mouseDown(MouseEvent e) {}
			public void mouseUp(MouseEvent e) {
				try {
					Point point = new Point(e.x, e.y);
					int offset = textWidget.getOffsetAtLocation(point);
					StyleRange range = textWidget.getStyleRangeAtOffset(offset);
					if (range != null && range instanceof StyleRangeWithData)
					{
						StyleRangeWithData range2 = (StyleRangeWithData) range;
						if (range2.action != null)
						{
							range2.action.run();
						}
					}
				} catch (IllegalArgumentException ex) {
					// swallow -- see note above
				}
			}
		});
	}
	
	public void selectAll() {
		textWidget.selectAll();
	}
	
	public void copy() {
		textWidget.copy();
	}
	
	public void pageUp() {
		if (isAtBottom()) {
			textWidget.setCaretOffset(textWidget.getCharCount());
		}
		textWidget.invokeAction(ST.PAGE_UP);
	}
	
	public void pageDown() {
		textWidget.invokeAction(ST.PAGE_DOWN);
	}
	
	public void setBackground(Color color) {
		textWidget.setBackground(color);
	}
	
	public void setForeground(Color color) {
		textWidget.setForeground(color);
	}
	
	public void setFont(Font font) {
		textWidget.setFont(font);
	}
	
	public void clearText() {
		textWidget.setText("");
		clearMarkers();
	}
	
	public void setLineLimit(int limit) {
		lineLimit = limit;
	}
	
	public void append(String string) {
		boolean atBottom = isAtBottom();
		
		textWidget.append(string);
		constrainLineLimit(atBottom);

		postTextChange(atBottom);
	}
	
	private void addStyles(List<WarlockStringStyleRange> styles, int offset, int length) {
		// add a marker for each style with a name
		for(WarlockStringStyleRange style : styles) {
			String name = style.style.getName();
			if(name != null) {
				WarlockTextMarker marker = markers.get(name);
				if(marker != null && marker.offset + marker.length == offset + style.getStart()) {
					marker.length += style.getLength();
					continue;
				}
				this.addMarker(name, offset + style.getStart(), style.getLength());
			}
		}
		
		/* Break up the ranges and merge overlapping styles because SWT only
		 * allows 1 style per section
		 */
		ArrayList<WarlockStringStyleRange> currentStyles = new ArrayList<WarlockStringStyleRange>();
		ArrayList<StyleRangeWithData> finishedStyles = new ArrayList<StyleRangeWithData>();
		int pos = 0;
		while(pos >= 0) {
			// update current styles
			for(WarlockStringStyleRange style : styles) {
				if(style.getStart() == pos) {
					currentStyles.add(style);
				} else if(style.getStart() + style.getLength() == pos) {
					currentStyles.remove(style);
				}
			}
			
			// create style segment for pos to next pos
			int foundPos = findNextEvent(styles, pos + 1);
			int nextPos = foundPos < 0 ? length : foundPos;

			if(currentStyles.size() > 0) {
				// merge all of the styles
				StyleRangeWithData style = warlockStringStyleRangeToStyleRange(
						currentStyles.get(0), offset);
				for(int i = 1; i < currentStyles.size(); i++) {
					StyleRangeWithData nextStyle = warlockStringStyleRangeToStyleRange(
							currentStyles.get(i), offset);
					if(nextStyle.font != null)
						style.font = nextStyle.font;
					if(nextStyle.background != null)
						style.background = nextStyle.background;
					if(nextStyle.foreground != null)
						style.foreground = nextStyle.foreground;
					if(nextStyle.fontStyle != SWT.NORMAL)
						style.fontStyle = nextStyle.fontStyle;
					if(nextStyle.strikeout) style.strikeout = true;
					if(nextStyle.underline) style.underline = true;
					style.data.putAll(nextStyle.data);
					if(nextStyle.action != null)
						style.action = nextStyle.action;
					if(nextStyle.tooltip != null)
						style.tooltip = nextStyle.tooltip;
				}
				style.start = offset + pos;
				style.length = nextPos - pos;
				finishedStyles.add(style);
			}
			
			pos = foundPos;
		}
		
		for(StyleRangeWithData style : finishedStyles) {
			textWidget.setStyleRange(style);
		}
	}
	
	public void append(WarlockString wstring) {
		boolean atBottom = isAtBottom();
		
		int charCount = textWidget.getCharCount();
		String string = wstring.toString();
		textWidget.append(string);
		addStyles(wstring.getStyles(), charCount, string.length());
		
		constrainLineLimit(atBottom);

		postTextChange(atBottom);
	}
	
	/* find an element in styles that intersects with the first element of styles, starting at pos */
	private int findNextEvent(List<WarlockStringStyleRange> styles, int pos) {
		int nextPos = -1;
		for(WarlockStringStyleRange style : styles) {
			int start = style.getStart();
			int end = start + style.getLength();
			if(start >= pos) {
				if(nextPos < 0 || start < nextPos)
					nextPos = start;
			} else if(end >= pos) {
				if(nextPos < 0 || end < nextPos)
					nextPos = end;
			}
		}
		return nextPos;
	}
	
	private StyleRangeWithData warlockStringStyleRangeToStyleRange(WarlockStringStyleRange range, int offset) {
		StyleRangeWithData styleRange = styleProvider.getStyleRange(range.style);
		if(styleRange == null)
			return null;

		styleRange.start = offset + range.getStart();
		styleRange.length = range.getLength();
		
		if(range.style.isFullLine())
			textWidget.setLineBackground(textWidget.getLineAtOffset(styleRange.start), 1, styleRange.background);
		if(range.style.getAction() != null)
			styleRange.action = range.style.getAction();
		if(range.style.getName() != null)
			styleRange.data.put("name", range.style.getName());
		
		return styleRange;
	}
	
	public boolean isAtBottom() {
		return textWidget.getLinePixel(textWidget.getLineCount()) <= textWidget.getClientArea().height;
	}
	
	public void postTextChange(boolean atBottom) {
		// TODO: Make preTextChange private
		// Explination: right now we can't listen for the before and after of our resize, so this must be called
		//     after an action that will cause a resize.
		if (atBottom && doScrollDirection == SWT.DOWN) {
			textWidget.setTopPixel(textWidget.getTopPixel()
					+ textWidget.getLinePixel(textWidget.getLineCount()));
			
			// FIXME: is this still needed?
			//if (Platform.getOS().equals(Platform.OS_MACOSX)) {
			//	textWidget.redraw();
			//}
		}
	}
	
	// Don't not call this function on append, only replace/remove/insert
	private void updateMarkers(int offset, int delta) {
		for(Iterator<Map.Entry<String, WarlockTextMarker>> iter = markers.entrySet().iterator();
		iter.hasNext(); )
		{
			Map.Entry<String, WarlockTextMarker> entry = iter.next();
			WarlockTextMarker marker = entry.getValue();
			// If the replaced text contains us, remove us (replaced text must
			// remove a character before and after (can't get rid of markers on
			// borders, unfortunately) this shouldn't happen much, if ever.
			if(offset < marker.offset && marker.offset - offset + delta < 0) {
				iter.remove();
				continue;
			}
			// move us accordingly
			if(marker.offset > offset)
				marker.offset += delta;
			else if(marker.offset == offset)
				marker.length += delta;
		}
	}
	
	public void addMarker(String name, int offset, int length) {
		markers.put(name, new WarlockTextMarker(offset, length));
	}
	
	public void clearMarkers() {
		markers.clear();
	}
	
	public void replaceMarker(String name, WarlockString text) {
		WarlockTextMarker marker = markers.get(name);
		if(marker == null)
			return;
		
		int start = marker.offset;
		int length = marker.length;
		boolean atBottom = isAtBottom();
		textWidget.replaceTextRange(start, length, text.toString());
		updateMarkers(start, text.length() - length);
		addStyles(text.getStyles(), start, text.length());
		postTextChange(atBottom);
	}
	
	private void constrainLineLimit(boolean atBottom) {
		// 'status' is a pointer that allows us to change the object in our parent..
		// in this method... it is intentional.
		if (lineLimit > 0) {
			int lines = textWidget.getLineCount();
			if (lines > lineLimit) {
				int linesToRemove = lines - lineLimit;
				int charsToRemove = textWidget.getOffsetAtLine(linesToRemove);
				if(atBottom) {
					textWidget.replaceTextRange(0, charsToRemove, "");
					updateMarkers(0, -charsToRemove);
				} else {
					int pixelsToRemove = textWidget.getLinePixel(linesToRemove);
					textWidget.replaceTextRange(0, charsToRemove, "");
					updateMarkers(0, -charsToRemove);
					if(pixelsToRemove < 0)
						textWidget.setTopPixel(-pixelsToRemove);
				}
			}
		}
	}
	
	public void setScrollDirection(int dir) {
		if (dir == SWT.DOWN || dir == SWT.UP)
			doScrollDirection = dir;
		// TODO: Else throw an error
	}
	
	public StyledText getTextWidget() {
		return textWidget;
	}
	
	public void setStyleProvider(IStyleProvider styleProvider) {
		this.styleProvider = styleProvider;
	}
}