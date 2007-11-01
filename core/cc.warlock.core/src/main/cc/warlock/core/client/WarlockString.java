package cc.warlock.core.client;

import java.util.ArrayList;
import java.util.List;

public class WarlockString {

	private StringBuffer text = new StringBuffer();
	private ArrayList<WarlockStringStyleRange> styles = new ArrayList<WarlockStringStyleRange>();
	private IWarlockClient client;
	
	public class WarlockStringStyleRange {
		public int start;
		public int length;
		public IWarlockStyle style;
		
		public WarlockStringStyleRange(int start, int length, IWarlockStyle style) {
			this.start = start;
			this.length = length;
			this.style = style;
		}
	}
	
	public WarlockString(IWarlockClient client) {
		this.client = client;
	}
	
	public WarlockString(IWarlockClient client, CharSequence text) {
		this.client = client;
		this.text.append(text);
	}
	
	public WarlockString(IWarlockClient client, String text) {
		this.client = client;
		this.text.append(text);
	}
	
	public String toString() {
		return text.toString();
	}
	
	public void append(String text) {
		this.text.append(text);
	}
	
	public void append(WarlockString string) {
		int charCount = text.length();
		text.append(string.toString());
		for(WarlockStringStyleRange range : string.getStyles()) {
			range.start += charCount;
			styles.add(range);
		}
	}
	
	public void addStyle(int start, int length, IWarlockStyle style) {
		styles.add(new WarlockStringStyleRange(start, length, style));
	}
	
	public List<WarlockStringStyleRange> getStyles() {
		return styles;
	}
	
	public int length() {
		return text.length();
	}
	
	public void clear() {
		text.setLength(0);
		styles.clear();
	}
	
	public IWarlockClient getClient() {
		return client;
	}
}
