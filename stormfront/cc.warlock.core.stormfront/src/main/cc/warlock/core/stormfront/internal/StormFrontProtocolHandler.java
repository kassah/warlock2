/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
/*
 * Created on Feb 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.stormfront.internal;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.IStormFrontTagHandler;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


/**
 * @author sproctor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StormFrontProtocolHandler implements IStormFrontProtocolHandler {
	
	protected IStormFrontClient client;
	protected HashMap<String, IStormFrontTagHandler> defaultTagHandlers = new HashMap<String, IStormFrontTagHandler>();
	protected Stack<IStream> streamStack = new Stack<IStream>();
	protected Stack<String> tagStack = new Stack<String>();
	protected HashMap<IWarlockStyle, Boolean> styles = new HashMap<IWarlockStyle, Boolean>();
	protected int currentSpacing = 0;
	protected int monsterCount = 0;
	protected IWarlockStyle boldStyle = null;
	
 	public StormFrontProtocolHandler(IStormFrontClient client) {
		
		this.client = client;
		
		// server settings handlers
		new PlayerIDTagHandler(this);
		new ModeTagHandler(this);
		new SettingsTagHandler(this);
		new SettingsInfoTagHandler(this);
		new CmdtimestampTagHandler(this, new CmdlistTagHandler(this));
		new SentSettingsTagHandler(this);
		
		// Register the handlers
		new AppTagHandler(this);
		new DialogDataTagHandler(this);
		new PromptTagHandler(this);
		new RoundtimeTagHandler(this);
		new CasttimeTagHandler(this);
		new CompDefTagHandler(this); // for the room stream
		new NavTagHandler(this); // for nextRoom notification
		new CompassTagHandler(this);
		
		// stream handlers
		new PushStreamTagHandler(this);
		new PopStreamTagHandler(this);
		new ClearStreamTagHandler(this);
		new StreamTagHandler(this);
		new StreamWindowTagHandler(this);
		
		// Container handlers
		new ClearContainerTagHandler(this);
		new InvTagHandler(this);
		
		new SpellTagHandler(this);
		new LeftTagHandler(this);
		new RightTagHandler(this);
		new ComponentTagHandler(this);
		new StyleTagHandler(this);
		new OutputTagHandler(this);
		
		new PushBoldTagHandler(this);
		new PopBoldTagHandler(this);
		new PresetTagHandler(this);
		new IndicatorTagHandler(this);
		new LaunchURLTagHandler(this);
		new ResourceTagHandler(this);
		new ATagHandler(this);
		new BTagHandler(this);
		new DTagHandler(this);
		
		new StubTagHandler(this); // handles knows tags that don't have an implementation.
	}
	
	/*
	 * This function is to register handlers for xml tags
	 */
	public void registerHandler(IStormFrontTagHandler tagHandler) {
		for (String tagName : tagHandler.getTagNames())
		{
			defaultTagHandlers.put(tagName, tagHandler);
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
	public void pushStream(String streamId) {
		IStream stream = client.getStream(streamId);
		if(stream != null) {
			// remove the stream if we already have the same one on the stack
			for(Iterator<IStream> iter = streamStack.iterator(); iter.hasNext(); ) {
				IStream curStream = iter.next();
				if(curStream.equals(stream)) {
					iter.remove();
					break;
				}
			}
			streamStack.push(stream);
		}
	}
	
	/*
	 * pop a stream from the stack
	 */
	public void popStream()
	throws EmptyStackException {
		if (streamStack.size() > 0)
			streamStack.pop();
	}
	
	public void clearStreams() {
		streamStack.clear();
	}
	
	public void appendStream(String streamId, WarlockString str) {
		IStream stream = client.getStream(streamId);
		stream.put(str);
	}

	public IStream getCurrentStream ()
	{
		try {
			return streamStack.peek();
		} catch(EmptyStackException e) {
			return client.getDefaultStream();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(String characters) {
		// if there was no handler or it couldn't handle the characters,
		// take a default action
		if(!handleCharacters(characters)) {
			WarlockString str = new WarlockString(characters);
			for(Map.Entry<IWarlockStyle, Boolean> style : styles.entrySet()) {
				str.addStyle(style.getKey());
				style.setValue(true);
			}
			
			IStream stream = getCurrentStream();
			
			stream.put(str);
		}
	}
	
	private boolean handleCharacters(String characters) {
		// Start looking for handlers at the highest level tag
		for(int pos = tagStack.size() - 1; pos >= 0; pos--) {
			String tagName = tagStack.get(pos);
		
			IStormFrontTagHandler tagHandler = getTagHandlerForElement(tagName, null, 0);
			if(tagHandler != null) {
				tagHandler.setCurrentTag(tagName);
				// if the handler handled the characters, we're done
				if(tagHandler.handleCharacters(characters))
					return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String name, String rawXML, boolean newLine) {
		// Get the tag name off the stack
		if(tagStack.size() == 0 || !name.equals(tagStack.peek())) {
			System.err.println("Unexpected close tag \"" + name + "\". Probably an unsupported tag.");
		} else {
			tagStack.pop();
		}
		
		// call the method for the object
		IStormFrontTagHandler tagHandler = getTagHandlerForElement(name, null, 0);
		if(tagHandler != null) {
			
			tagHandler.setCurrentTag(name);
			tagHandler.handleEnd(rawXML);
			if(newLine && !tagHandler.ignoreNewlines()) {
				characters("\n");
			}
		} /* else {
			if (rawXML != null)
				characters(rawXML);
		} */
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String name, StormFrontAttributeList attributes, String rawXML, boolean newLine) {
		
		// call the method for the object
		IStormFrontTagHandler tagHandler = getTagHandlerForElement(name, null, 0);
		
		if(tagHandler != null) {
			
			tagStack.push(name);
			tagHandler.setCurrentTag(name);
			tagHandler.handleStart(attributes, rawXML);
			if(newLine && !tagHandler.ignoreNewlines()) {
				characters("\n");
			}
		} /*else {
			if(rawXML != null)
				characters(rawXML);
		}*/
	}
	
	private IStormFrontTagHandler getTagHandlerForElement(String name,
			IStormFrontTagHandler parentHandler, int stackPosition) {
		if(stackPosition < tagStack.size()) {
			String tagName = tagStack.get(stackPosition);
			
			// next handler is the child the parent
			IStormFrontTagHandler nextHandler;
			if(parentHandler != null) nextHandler = parentHandler.getTagHandler(tagName);
			else nextHandler = defaultTagHandlers.get(tagName);
			// If there was no match, use the current parent
			if(nextHandler == null) nextHandler = parentHandler;
			
			// see if there is a valid handler further down the tree.
			IStormFrontTagHandler tagHandler = getTagHandlerForElement(name,
					nextHandler, stackPosition + 1);
			if(tagHandler != null) {
				return tagHandler;
			} else {
				if(parentHandler != null) return parentHandler.getTagHandler(name);
				else return defaultTagHandlers.get(name);
			}
		} else {
			if(parentHandler != null) return parentHandler.getTagHandler(name);
			else return defaultTagHandlers.get(name);
		}
	}
	
	public void addStyle(IWarlockStyle style) {
		styles.put(style, false);
	}
	
	public void removeStyle(IWarlockStyle style) {
		Boolean wasSent = styles.remove(style);
		
		// If the style was never output, put it in the string anyway
		if(wasSent != null && !wasSent.booleanValue()) {
			WarlockString str = new WarlockString();
			str.addStyle(style);

			IStream stream = getCurrentStream();
			
			stream.put(str);
		}
	}
	
	public void clearStyles() {
		boldStyle = null;
		styles.clear();
	}
	
	public Map<IWarlockStyle, Boolean> getStyles() {
		return styles;
	}
	
	public void startBold() {
		if (boldStyle == null) {
			boldStyle = this.getClient().getNamedStyle("bold");
			this.addStyle(boldStyle);
		}
	}
	
	public void stopBold() {
		if (boldStyle != null) {
			this.removeStyle(boldStyle);
			boldStyle = null;
		}
	}
	
	public IStormFrontTagHandler getTagHandler(Class<? extends IStormFrontTagHandler> handlerType) {
		
		for (IStormFrontTagHandler handler : defaultTagHandlers.values())
		{
			if (handler.getClass().equals(handlerType))
				return handler;
		}
		return null;
	}
	
	public void resetMonsterCount() {
		monsterCount = 0;
	}
	
	public void incrementMonsterCount() {
		monsterCount++;
	}
	
	public int getMonsterCount() {
		return monsterCount;
	}
}
