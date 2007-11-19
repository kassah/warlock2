package cc.warlock.core.stormfront.xml;

public interface IStormFrontXMLHandler {
	
	/**
	 * Handle characters received in the current element
	 * @param ch The characters received
	 * @param start The start index
	 * @param length The length
	 */
	public void characters(char[] ch, int start, int length);
	
	/**
	 * Start parsing an element and it's attributes
	 * @param name The name of the element
	 * @param attributes The element's attributes
	 */
	public void startElement(String name, StormFrontAttributeList attributes, String newLine);
	
	/**
	 * Finish parsing an element
	 * @param name The name of the element
	 */
	public void endElement(String name, String newLine);
}
