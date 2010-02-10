package cc.warlock.core.client;

import java.util.Collection;
import java.util.Iterator;


public class WarlockStringMarker {
	public enum Type {
		BEGIN,
		END,
		EMPTY
	}
	
	public Type type;
	public IWarlockStyle style;
	public int offset;
	
	public WarlockStringMarker(Type type, IWarlockStyle style, int offset) {
		this.type = type;
		this.style = style;
		this.offset = offset;
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
