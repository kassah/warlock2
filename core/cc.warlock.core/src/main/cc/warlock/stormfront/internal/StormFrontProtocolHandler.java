/*
 * Created on Feb 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.stormfront.internal;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringEscapeUtils;

import cc.warlock.client.IStream;
import cc.warlock.client.IStyledString;
import cc.warlock.client.IWarlockStyle;
import cc.warlock.client.internal.StyledString;
import cc.warlock.client.internal.WarlockStyle;
import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.stormfront.IStormFrontProtocolHandler;
import cc.warlock.stormfront.IStormFrontTagHandler;


/**
 * @author sproctor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StormFrontProtocolHandler implements IStormFrontProtocolHandler {
	
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
	
	public void removeHandler(IStormFrontTagHandler tagHandler) {
		for (String tagName : tagHandler.getTagNames())
		{
			if (tagHandlers.get(tagName).equals(tagHandler))
				tagHandlers.remove(tagName);
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
	public void characters(char[] ch, int start, int length) {
		/*String str = String.copyValueOf(ch, start, length);
		System.out.print(str);*/
		
		if (rawXMLBuffer != null)
		{
			rawXMLBuffer.append(StringEscapeUtils.escapeXml(String.copyValueOf(ch, start, length)));
		}
		
		boolean handled = false;
		
		// get the handler
		if(!tagStack.isEmpty()) {
			for(int i = tagStack.size() - 1; i >= 0; i--) {
				String tagName = tagStack.get(i);
			
				// if we have a handler, let it try to handle the characters
				IStormFrontTagHandler tagHandler = tagHandlers.get(tagName);
				if(tagHandler != null) {
					tagHandler.setCurrentTag(tagName);
					handled = tagHandler.handleCharacters(ch, start, length);
				}
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
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String name) {
		
		if (rawXMLBuffer != null)
		{
			if (name.equals(rawXMLEndOnTag))
			{
				rawXMLBuffer = null;
			} else {
				rawXMLBuffer.append(repeat("\t", currentSpacing) + "</" + name + ">\n");
				currentSpacing -= 1;
			}
		}
		
		//System.out.print("</" + name + ">");
		
		String popName = tagStack.pop();
		if(!name.equals(popName))
			System.out.println("Whoa!! close tag we got \"" + name + "\" is not what we expected \"" + popName + "\"");
		assert(name.equals(popName));
		
		// call the method for the object
		IStormFrontTagHandler tagHandler = tagHandlers.get(name);
		if(tagHandler != null) {
			tagHandler.setCurrentTag(name);
			tagHandler.handleEnd();
		} else {
			System.out.println("didn't handle close for tag \"" + name + "\"");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String name, Map<String,String> attributes) {
		
		System.out.print("<" + name);
		if (rawXMLBuffer != null)
		{
			String startTag = "<" + name;
			if (attributes != null) {
	            for (String aName : attributes.keySet())
	            {
	                startTag += " " + aName + "=\"" + attributes.get(aName) + "\"";
	                System.out.print(" " + aName + "=\"" + attributes.get(aName) + "\"");
	            }
	        }
			startTag += ">";
			rawXMLBuffer.append(repeat("\t", currentSpacing) + startTag + "\n");
			
			currentSpacing += 1;
		}
        System.out.println(">");
        
		tagStack.push(name);
		
		// call the method for the object
		IStormFrontTagHandler tagHandler = tagHandlers.get(name);
		if(tagHandler != null) {
			tagHandler.setCurrentTag(name);
			tagHandler.handleStart(attributes);
		} else {
			System.out.println("didn't handle open for tag \"" + name + "\"");
		}
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
			getCurrentStream().send(buffer);
		}
	}
}
