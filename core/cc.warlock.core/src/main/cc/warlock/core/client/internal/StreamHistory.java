package cc.warlock.core.client.internal;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.WarlockStringMarker;

public class StreamHistory implements IStreamListener {
	
	private WarlockString buffer = new WarlockString();

	@Override
	public void streamCleared(IStream stream) {
		buffer.clear();
	}

	@Override
	public void streamCreated(IStream stream) {}

	@Override
	public void streamFlush(IStream stream) {}

	@Override
	public void streamPrompted(IStream stream, String prompt) {}

	@Override
	public void streamReceivedCommand(IStream stream, ICommand command) {}

	@Override
	public void streamReceivedText(IStream stream, WarlockString text) {
		buffer.append(text);
	}

	@Override
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
	}

	@Override
	public void streamTitleChanged(IStream stream, String title) {}
	
	public WarlockString getHistory() {
		return buffer;
	}
}
