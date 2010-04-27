package cc.warlock.core.client.internal;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.WarlockStringMarker;

public class StreamHistory implements IStreamListener {
	
	private WarlockString buffer = new WarlockString();
	private int lineLimit = 5000;

	public void streamCleared(IStream stream) {
		buffer.clear();
	}

	public void streamCreated(IStream stream) {}

	public void streamFlush(IStream stream) {}

	public void streamPrompted(IStream stream, String prompt) {}

	public void streamReceivedCommand(IStream stream, ICommand command) {}

	public void streamReceivedText(IStream stream, WarlockString text) {
		buffer.append(text);
		constrainLines();
	}

	public void componentUpdated(IStream stream, String id, WarlockString value) {
		WarlockStringMarker marker = buffer.getMarker(id);
		if(marker == null)
			return;
		marker.clear();
		int start = marker.getStart();
		int end = marker.getEnd();
		int length = end - start;
		int newLength = value.length();
		buffer.replace(start, end, value.toString());
		marker.setEnd(start + newLength);
		WarlockStringMarker.updateMarkers(newLength - length, marker, buffer.getStyles());
		
		for(WarlockStringMarker newMarker : value.getStyles()) {
			marker.addMarker(newMarker.copy(start));
		}
		constrainLines();
	}

	public void streamTitleChanged(IStream stream, String title) {}
	
	public WarlockString getHistory() {
		return buffer;
	}
	
	private void constrainLines() {
		// if we have fewer characters, we certainly have fewer newlines
		if(buffer.length() <= lineLimit)
			return;
		int lines = buffer.getLineCount();
		if(lines <= lineLimit)
			return;
		int pos = 0;
		while(lines > lineLimit) {
			lines--;
			pos = buffer.indexOf("\n", pos) + 1;
		}
		buffer = buffer.substring(pos);
	}
}
