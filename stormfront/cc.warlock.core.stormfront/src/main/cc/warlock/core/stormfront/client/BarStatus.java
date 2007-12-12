package cc.warlock.core.stormfront.client;

public class BarStatus {
	private int value;
	private String text;
	
	public BarStatus(int value, String text) {
		this.value = value;
		this.text = text;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getText() {
		return text;
	}
}
