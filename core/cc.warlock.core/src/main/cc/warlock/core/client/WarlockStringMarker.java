package cc.warlock.core.client;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


public class WarlockStringMarker {
	
	private IWarlockStyle style;
	private int start;
	private int end;
	private LinkedList<WarlockStringMarker> subMarkers = new LinkedList<WarlockStringMarker>();
	
	public WarlockStringMarker(IWarlockStyle style, int start, int end) {
		this.style = style;
		this.start = start;
		this.end = end;
	}
	
	public IWarlockStyle getStyle() {
		return style;
	}
	
	public int getStart() {
		return start;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	public int getEnd() {
		return end;
	}
	
	public void setEnd(int end) {
		this.end = end;
	}
	
	public LinkedList<WarlockStringMarker> getSubMarkers() {
		return subMarkers;
	}
	
	public void addMarker(WarlockStringMarker marker) {
		subMarkers.add(marker);
	}
	
	public WarlockStringMarker copy(int offset) {
		WarlockStringMarker copy = new WarlockStringMarker(style, start + offset, end + offset);
		
		for(WarlockStringMarker marker : subMarkers) {
			copy.addMarker(marker.copy(offset));
		}
		
		return copy;
	}
	
	public WarlockStringMarker copy(int offset, int length) {
		int newStart = start + offset;
		if(newStart < 0)
			newStart = 0;
		int newEnd = end + offset;
		if(newEnd > length)
			newEnd = length;
		WarlockStringMarker copy = new WarlockStringMarker(style, newStart, newEnd);
		
		for(WarlockStringMarker marker : subMarkers) {
			if(marker.getEnd() + offset > 0)
				copy.addMarker(marker.copy(offset, length));
		}
		
		return copy;
	}
	
	public void move(int offset) {
		this.start += offset;
		if(this.start < 0)
			start = 0;
		this.end += offset;
		if(this.end < 0)
			this.end = 0;
		
		
		for(Iterator<WarlockStringMarker> iter = subMarkers.iterator(); iter.hasNext();) {
			WarlockStringMarker marker = iter.next();
			if(marker.getEnd() + offset < 0)
				iter.remove();
			else 
				marker.move(offset);
		}
	}
	
	public boolean hasStyleNamed(String styleName) {
		String name = style.getName();
		if(name != null && name.equals(styleName))
			return true;
		
		for(WarlockStringMarker marker : subMarkers) {
			if(marker.hasStyleNamed(styleName))
				return true;
		}
		
		return false;
	}
	
	public void clear() {
		subMarkers.clear();
	}
	
	// helper function to remove a style from a list
	public static void removeStyle(Collection<WarlockStringMarker> list, IWarlockStyle style) {
		Iterator<WarlockStringMarker> iter = list.iterator();
		while(iter.hasNext()) {
			WarlockStringMarker cur = iter.next();
			if(style == cur.style) {
				iter.remove();
				break;
			}
		}
	}
	
}
