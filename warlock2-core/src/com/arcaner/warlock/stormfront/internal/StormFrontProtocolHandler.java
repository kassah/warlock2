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

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.arcaner.warlock.client.IWarlockClient;
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
	protected Stack<String> streamStack = new Stack<String>();
	protected Stack<String> tagStack = new Stack<String>();
	protected Stack<StringBuffer> bufferStack = new Stack<StringBuffer>();
	
 	public StormFrontProtocolHandler(IStormFrontClient client) {
		
		this.client = client;
		
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
		
		new InvTagHandler(this);
		new SpellTagHandler(this);
		new LeftTagHandler(this);
		new RightTagHandler(this);
		new ComponentTagHandler(this);
		new StyleTagHandler(this);
		
		new PushBoldTagHandler(this);
		new PopBoldTagHandler(this);
		new PresetTagHandler(this);
		new OutputTagHandler(this);
	}
	
	/*
	 * This function is to register handlers for xml tags
	 */
	public void registerHandler(IStormFrontTagHandler tagHandler) {
		tagHandlers.put(tagHandler.getName(), tagHandler);
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
		streamStack.push(name);
	}
	
	/*
	 * pop a stream from the stack
	 */
	public void popStream()
	throws EmptyStackException {
		if (streamStack.size() > 0)
			streamStack.pop();
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length)
	throws SAXException {
		/*String str = String.copyValueOf(ch, start, length);
		System.out.print(str);*/
		
		boolean handled = false;
		
		// get the handler
		if(!tagStack.isEmpty()) {
			String tagName = tagStack.peek();
			IStormFrontTagHandler tagHandler = tagHandlers.get(tagName);
		
			// if we have a handler, let it try to handle the characters
			if(tagHandler != null) {
				handled = tagHandler.handleCharacters(ch, start, length);
			}
		}
		
		// if there was no handler or it couldn't handle the characters,
		// take a default action
		if(!handled) {
			String str = String.copyValueOf(ch, start, length);
			
			if (bufferStack.size() > 0)
			{
				bufferStack.peek().append(str);
			}
			else
			{
				String stream;
				try {
					stream = streamStack.peek();
				} catch(EmptyStackException e) {
					stream = IWarlockClient.DEFAULT_VIEW;
				}
				//streamTable.send(stream, str);
				client.append(stream, str);
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
		
		//System.out.print("</" + name + ">");
		
		String popName = tagStack.pop();
		assert(name == popName);
		
		// call the method for the object
        IStormFrontTagHandler tagHandler = tagHandlers.get(name);
        if(tagHandler != null) {
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
		
		/*System.out.print("<" + name);
		if (atts != null) {
            for (int i = 0; i < atts.getLength(); i++) {
                String aName = atts.getLocalName(i); // Attr name
                if ("".equals(aName)) aName = atts.getQName(i);
                System.out.print(" ");
                System.out.print(aName + "=\"" + atts.getValue(i) + "\"");
            }
        }
        System.out.print(">");*/
        
		tagStack.push(name);
		
        // call the method for the object
        IStormFrontTagHandler tagHandler = tagHandlers.get(name);
        if(tagHandler != null) {
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
		bufferStack.push(new StringBuffer());
	}
	
	public StringBuffer popBuffer() {
		return bufferStack.pop();
	}
}
