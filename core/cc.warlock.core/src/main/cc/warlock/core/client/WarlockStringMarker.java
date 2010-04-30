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
	
	public void setStyle(IWarlockStyle style) {
		this.style = style;
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
	
	public WarlockStringMarker clone() {
		WarlockStringMarker clone = new WarlockStringMarker(style, start, end);
		
		for(WarlockStringMarker marker : subMarkers) {
			clone.addMarker(marker.clone());
		}
		
		return clone;
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
	
	public String getName() {
		if(style == null)
			return null;
		return style.getName();
	}
	
	public String getComponentName() {
		if(style == null)
			return null;
		return style.getComponentName();
	}
	
	public WarlockStringMarker getMarkerByComponent(String componentName) {
		String myName = getComponentName();
		if(myName != null && myName.equals(componentName))
			return this;
		for(WarlockStringMarker subMarker : subMarkers) {
			WarlockStringMarker marker = subMarker.getMarkerByComponent(componentName);
			if(marker != null)
				return marker;
		}
		return null;
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
	
	// this function removes the first "delta" amount of characters
	public static boolean updateMarkers(int delta, WarlockStringMarker afterMarker,
			Collection<WarlockStringMarker> markerList) {
		// remove markers between start and end.
		// all markers after start need to be adjusted by offset.
		// returns whether or not the afterMarker was found
		boolean started = false;
		for(Iterator<WarlockStringMarker> iter = markerList.iterator();
		iter.hasNext(); )
		{
			WarlockStringMarker marker = iter.next();
			
			if(marker == afterMarker) {
				started = true;
				continue;
			}
			
			if(!started) {
				started = updateMarkers(delta, afterMarker, marker.getSubMarkers());
				if(started)
					marker.setEnd(marker.getEnd() + delta);
			} else
				marker.move(delta);
		}
		return started;
	}
	
	public IWarlockStyle getBaseStyle(WarlockStringMarker marker) {
		if(this == marker)
			return style;
		for(WarlockStringMarker subMarker : subMarkers) {
			IWarlockStyle baseStyle = subMarker.getBaseStyle(marker);
			if(baseStyle != null)
				return baseStyle.mergeWith(style);
		}
		return null;
	}
}
