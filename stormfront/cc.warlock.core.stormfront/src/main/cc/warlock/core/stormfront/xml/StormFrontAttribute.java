package cc.warlock.core.stormfront.xml;

public class StormFrontAttribute {

	protected String name;
	protected String value;
	
	protected char quoteType;

	public StormFrontAttribute () { }
	public StormFrontAttribute (StormFrontAttribute other)
	{
		this.name = new String(other.name);
		this.value = other.value == null ? null :new String(other.value);
		this.quoteType = other.quoteType;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public char getQuoteType() {
		return quoteType;
	}

	public void setQuoteType(char quoteType) {
		this.quoteType = quoteType;
	}
	
	@Override
	public String toString() {
		return name + "=" + quoteType + value + quoteType;
	}
	
}
