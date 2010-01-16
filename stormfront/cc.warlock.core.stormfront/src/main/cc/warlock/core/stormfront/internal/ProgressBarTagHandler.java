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
 * Created on Jan 15, 2005
 */
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.BarStatus;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.client.StormFrontDialog;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


/**
 * @author Marshall
 *
 * An XPath Listener that handles the health, mana, fatigue, and spirit bars.
 */
public class ProgressBarTagHandler extends BaseTagHandler {
	private IStormFrontProtocolHandler handler;
	private DialogDataTagHandler parent;
	
	public ProgressBarTagHandler (IStormFrontProtocolHandler handler, DialogDataTagHandler parent) {
		this.handler = handler;
		this.parent = parent;
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "progressBar" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
    	String id = attributes.getValue("id");
    	int value = Integer.parseInt(attributes.getValue("value"));
    	String text = attributes.getValue("text");
    	BarStatus bar = new BarStatus(value, text);
    	String left = attributes.getValue("left");
    	String top = attributes.getValue("top");
    	String width = attributes.getValue("width");
    	String height = attributes.getValue("height");

		IStormFrontClient client = handler.getClient();
		StormFrontDialog dialog = client.getDialog(parent.id);
		dialog.progressBar(id, text, value, left, top, width, height);
		
		if (id.equals("health"))
		{
			client.getHealth().set(bar);
		}
		else if (id.equals("mana"))
		{
			client.getMana().set(bar);
		}
		else if (id.equals("stamina"))
		{
			client.getFatigue().set(bar);
		}
		else if (id.equals("spirit"))
		{
			client.getSpirit().set(bar);
		}
	}
}
