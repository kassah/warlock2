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
package cc.warlock.core.stormfront.internal;

import java.util.regex.Pattern;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;


public class PushBoldTagHandler extends DefaultTagHandler {

	protected IWarlockStyle style;
	protected static final Pattern linkPattern = Pattern.compile("(www\\.|http://).+");
	
	public PushBoldTagHandler (IStormFrontProtocolHandler handler)
	{
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "pushBold", "popBold" };
	}
	
	@Override
	public void handleEnd() {
		if(style != null) {
			handler.removeStyle(style);
			style = null;
		}
		if (getCurrentTag().equals("pushBold"))
		{
			style = handler.getClient().getServerSettings().getPreset("bold").getStyle();
			handler.addStyle(style);
		}
		
		//else if (getCurrentTag().equals("popBold"))
		//{
		//	if(style != null) {
		//		handler.removeStyle(style);
		//		style = null;
		//	}
			// we'll need to figure out another way to auto linkify http links
//			IStyledString buffer = handler.peekBuffer();
//			
//			style.setLength(buffer.getBuffer().length());
//			buffer.addStyle(style);
//			
//			String matchText = buffer.getBuffer().toString();
//			
//			Matcher linkMatcher = linkPattern.matcher(matchText.trim());
//			if (linkMatcher.matches())
//			{
//				style.addStyleType(IWarlockStyle.StyleType.LINK);
//				String address = linkMatcher.group();
//				if (address.startsWith("www")) { address = "http://" + address; }
//				
//				try {
//					style.setLinkAddress(new URL(address));
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//				}
//			}
//			
//			handler.sendAndPopBuffer();
//		}
	}
	
	@Override
	public boolean ignoreNewlines() {
		return false;
	}
}
