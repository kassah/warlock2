package cc.warlock.core.stormfront.client;

public class StormFrontProgressBar implements IStormFrontDialogMessage {
	public String id;
	public String text;
	public String value;
	public String left;
	public String top;
	public String width;
	public String height;
	
	public StormFrontProgressBar(String id, String text, String value, String left, String top,
			String width, String height) {
		this.id = id;
		this.text = text;
		this.value = value;
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
	}
	
	public int getValue() {
		return Integer.parseInt(value);
	}
	
	public int getLeft() {
		return percentageToInt(left);
	}
	
	public int getWidth() {
		return percentageToInt(width);
	}
	
	public int getTop() {
		return percentageToInt(top);
	}
	
	public int getHeight() {
		return percentageToInt(height);
	}
	
	private int percentageToInt(String number) {
		int p = number.indexOf("%");
		return Integer.parseInt(number.substring(0, p));
	}
}
