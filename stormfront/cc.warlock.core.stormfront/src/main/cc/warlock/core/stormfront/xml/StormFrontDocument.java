package cc.warlock.core.stormfront.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Stack;

import cc.warlock.core.stormfront.internal.ParseException;
import cc.warlock.core.stormfront.internal.StormFrontProtocolParser;

public class StormFrontDocument implements IStormFrontXMLHandler {

	protected Stack<StormFrontElement> elementStack = new Stack<StormFrontElement>();
	protected StormFrontElement rootElement;

	public StormFrontElement getRootElement() {
		return rootElement;
	}
	
	public StormFrontDocument (InputStream stream)
	{
		StormFrontProtocolParser parser = new StormFrontProtocolParser(stream);
		parser.setHandler(this);
		
		try {
			parser.Document();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startElement(String name, StormFrontAttributeList attributes, String newLine) {
		StormFrontElement currentElement = new StormFrontElement(name);
		for (StormFrontAttribute attribute : attributes.getList())
		{
			currentElement.addAttribute(attribute);
		}
		
		if (rootElement == null)
		{
			rootElement = currentElement;
		}
		else
		{
			if (elementStack.size() > 0)
			{
				elementStack.peek().addElement(currentElement);
			}
		}

		elementStack.push(currentElement);
	}
	
	public void characters(String characters) {
		StormFrontElement element = elementStack.peek();
		
		element.appendText(characters);
	}
	
	public void endElement(String name, String newLine) {
		elementStack.pop();
	}
	
	public void saveTo (Writer writer, boolean prettyPrint)
	{
		try {
			writer.append(rootElement.toXML("", prettyPrint, true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
