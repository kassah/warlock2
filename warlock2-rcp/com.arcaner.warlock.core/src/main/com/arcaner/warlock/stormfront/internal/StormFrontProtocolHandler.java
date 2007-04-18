/*
 * Created on Feb 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.stormfront.internal;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

import org.apache.commons.lang.StringEscapeUtils;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.arcaner.warlock.client.IStream;
import com.arcaner.warlock.client.IStyledString;
import com.arcaner.warlock.client.IWarlockStyle;
import com.arcaner.warlock.client.internal.StyledString;
import com.arcaner.warlock.client.internal.WarlockStyle;
import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;
import com.arcaner.warlock.stormfront.IStormFrontTagHandler;

/**
 * @author sproctor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StormFrontProtocolHandler extends DefaultHandler implements IStormFrontProtocolHandler {
	
	protected IStormFrontClient client;
	protected HashMap<String, IStormFrontTagHandler> tagHandlers = new HashMap<String, IStormFrontTagHandler>();
	protected Stack<IStream> streamStack = new Stack<IStream>();
	protected Stack<String> tagStack = new Stack<String>();
	protected Stack<IStyledString> bufferStack = new Stack<IStyledString>();
	protected StringBuffer rawXMLBuffer;
	protected String rawXMLEndOnTag;
	protected int currentSpacing = 0;
	protected IWarlockStyle currentStyle = WarlockStyle.EMPTY_STYLE;
	
 	public StormFrontProtocolHandler(IStormFrontClient client) {
		
		this.client = client;
		
		// server settings handlers
		new PlayerIDTagHandler(this);
		new SettingsInfoTagHandler(this);
		new SettingsTagHandler(this);
		new SentSettingsTagHandler(this);
		
		// Register the handlers
		new AppTagHandler(this);
		new BarTagHandler(this);
		new PromptTagHandler(this, new RoundtimeTagHandler(this));
		
		// compass handlers
		new CompDefTagHandler(this);
		new DirectionTagHandler(this);
		
		// stream handlers
		new PushStreamTagHandler(this);
		new PopStreamTagHandler(this);
		new ClearStreamTagHandler(this);
		new StreamTagHandler(this);
		new StreamWindowTagHandler(this);
		
		new InvTagHandler(this);
		new SpellTagHandler(this);
		new LeftTagHandler(this);
		new RightTagHandler(this);
		new ComponentTagHandler(this);
		new StyleTagHandler(this);
		
		new BoldTagHandler(this);
		new PushBoldTagHandler(this);
		new PresetTagHandler(this);
	}
	
	/*
	 * This function is to register handlers for xml tags
	 */
	public void registerHandler(IStormFrontTagHandler tagHandler) {
		for (String tagName : tagHandler.getTagNames())
		{
			tagHandlers.put(tagName, tagHandler);
		}
	}
	
	/*
	 * The purpose of this function is painfully obvious.
	 */
	public IStormFrontClient getClient() {
		return client;
	}
	
	/*
	 *  push a stream onto the stack
	 */
	public void pushStream(String name) {
		streamStack.push(client.getStream(name));
	}
	
	/*
	 * pop a stream from the stack
	 */
	public void popStream()
	throws EmptyStackException {
		if (streamStack.size() > 0)
			streamStack.pop();
	}
	
	public IStream getCurrentStream ()
	{
		if (streamStack.size() > 0)
			return streamStack.peek();
		
		return client.getDefaultStream();
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length)
	throws SAXException {
		/*String str = String.copyValueOf(ch, start, length);
		System.out.print(str);*/
		
		if (rawXMLBuffer != null)
		{
			rawXMLBuffer.append(StringEscapeUtils.escapeXml(String.copyValueOf(ch, start, length)));
			return;
		}
		
		boolean handled = false;
		
		// get the handler
		if(!tagStack.isEmpty()) {
			String tagName = tagStack.peek();
			IStormFrontTagHandler tagHandler = tagHandlers.get(tagName);
		
			// if we have a handler, let it try to handle the characters
			if(tagHandler != null) {
				tagHandler.setCurrentTag(tagName);
				handled = tagHandler.handleCharacters(ch, start, length);
			}
		}
		
		// if there was no handler or it couldn't handle the characters,
		// take a default action
		if(!handled) {
			String str = String.copyValueOf(ch, start, length);
			
			if (bufferStack.size() > 0)
			{
				bufferStack.peek().getBuffer().append(str);
			}
			else
			{
				IStream stream;
				try {
					stream = streamStack.peek();
				} catch(EmptyStackException e) {
					stream = client.getDefaultStream();
				}

				if (!WarlockStyle.EMPTY_STYLE.equals(currentStyle))
				{
					currentStyle.setLength(str.length());
				}
				
				stream.send(str, currentStyle);
				//streamTable.send(stream, str);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		System.out.println("END DOCUMENT");
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName)
	throws SAXException {
		// TODO Auto-generated method stub
		String name;
		if("".equals(localName)) name = qName;
		else name = localName;
		
		if (rawXMLBuffer != null)
		{
			if (name.equals(rawXMLEndOnTag))
			{
				rawXMLBuffer = null;
			} else {
				rawXMLBuffer.append(repeat("\t", currentSpacing) + "</" + name + ">\n");
				currentSpacing -= 1;
				return;
			}
		}
		
		//System.out.print("</" + name + ">");
		
		String popName = tagStack.pop();
		assert(name == popName);
		
		// call the method for the object
        IStormFrontTagHandler tagHandler = tagHandlers.get(name);
        if(tagHandler != null) {
        	tagHandler.setCurrentTag(name);
        	tagHandler.handleEnd();
        }
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub

	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub

	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	public void processingInstruction(String target, String data)
	throws SAXException {
		// TODO Auto-generated method stub
		System.out.print("PROCESS: ");
		System.out.print("<?"+target+" "+data+"?>");
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub

	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub

	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		System.out.println("START DOCUMENT");
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		// debug info
		String name;
		if("".equals(localName)) name = qName;
		else name = localName;
		
		//System.out.print("<" + name);
		if (rawXMLBuffer != null)
		{
			String startTag = "<" + name;
			if (atts != null) {
	            for (int i = 0; i < atts.getLength(); i++) {
	                String aName = atts.getLocalName(i); // Attr name
	                if ("".equals(aName)) aName = atts.getQName(i);
	                startTag += " ";
	                startTag += aName + "=\"" + atts.getValue(i) + "\"";
	            }
	        }
			startTag += ">";
			rawXMLBuffer.append(repeat("\t", currentSpacing) + startTag + "\n");
			
			currentSpacing += 1;
			return;
		}
        //System.out.print(">");
        
		tagStack.push(name);
		
        // call the method for the object
        IStormFrontTagHandler tagHandler = tagHandlers.get(name);
        if(tagHandler != null) {
        	tagHandler.setCurrentTag(name);
        	tagHandler.handleStart(atts);
        }
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	public void startPrefixMapping(String prefix, String uri)
	throws SAXException {
		// TODO Auto-generated method stub

	}
	
	public void warning(SAXParseException e)
	throws SAXParseException {
		System.out.println("WARNING");
		e.printStackTrace();
	}
	
	public void error(SAXParseException e)
	throws SAXParseException {
		System.out.println("RECOVERABLE ERROR");
		e.printStackTrace();
	}
	
	public void fatalError(SAXParseException e)
	throws SAXParseException {
		System.out.println("FATAL ERROR");
	}
	
	public void pushBuffer() {
		bufferStack.push(new StyledString());
	}
	
	public IStyledString popBuffer() {
		return bufferStack.pop();
	}
	
	public IStyledString peekBuffer() {
		if (bufferStack.size() > 0)
			return bufferStack.peek();
		else return null;
	}
	
	public void startSavingRawXML(StringBuffer buffer, String endOnTag) {
		rawXMLBuffer = buffer;
		rawXMLEndOnTag = endOnTag;
	}
	
	public void stopSavingRawXML() {
		rawXMLBuffer = null;
		rawXMLEndOnTag = null;
	}
	
	private String repeat (String toRepeat, int times)
	{
		String str = "";
		for (int i = 0; i < times; i++)
			str += toRepeat;
		return str;
	}
	
	public void setCurrentStyle(IWarlockStyle style) {
		this.currentStyle = style;
	}
	
	public IWarlockStyle getCurrentStyle() {
		return currentStyle;
	}
	
	public void clearCurrentStyle() {
		this.currentStyle = WarlockStyle.EMPTY_STYLE;
	}
	
	public void sendAndPopBuffer() {
		IStyledString buffer = popBuffer();
		if (peekBuffer() != null)
		{
			IStyledString parentBuffer = peekBuffer();
			
			for (IWarlockStyle style : buffer.getStyles())
			{
				style.setStart(parentBuffer.getBuffer().length() + style.getStart());
				if (!WarlockStyle.EMPTY_STYLE.equals(currentStyle))
				{
					// the current style in this context will be used for style inheritance
					style.inheritFrom(currentStyle);
				}
				
				parentBuffer.addStyle(style);
			}
			parentBuffer.getBuffer().append(buffer.getBuffer());
		}
		else {
			getCurrentStream().send(buffer, true);
		}
	}
}
