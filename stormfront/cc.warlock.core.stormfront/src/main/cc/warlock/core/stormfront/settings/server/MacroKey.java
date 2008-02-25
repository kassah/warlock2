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
package cc.warlock.core.stormfront.settings.server;

import java.util.ArrayList;

@Deprecated
public class MacroKey extends ServerSetting {

	protected String keyString;
	protected String action;
	protected String key;
	protected ArrayList<String> modifiers = new ArrayList<String>();
	
	public MacroKey (ServerSettings settings, String keyString, String action)
	{
		super(settings);
		
		setKeyString(keyString);
		setAction(action);
	}
	
	private void parseKeyString ()
	{
		modifiers.clear();
		
		String tokens[] = keyString.split("-");
		if (tokens.length > 1)
		{
			for (int i = 0; i < tokens.length - 1; i++) {
				modifiers.add(tokens[i]);
			}
			key = tokens[tokens.length-1];
		}
		else {
			key = keyString;
		}
	}
	
	@Override
	protected void saveToDOM() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String toStormfrontMarkup() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getKey() {
		return key;
	}

	public ArrayList<String> getModifiers() {
		return modifiers;
	}

	public String getKeyString() {
		return keyString;
	}
	
	public void setKeyString (String keyString)
	{
		this.keyString = keyString;
		
		parseKeyString();
	}
	
	public String getAction ()
	{
		return action;
	}
	
	public void setAction (String action)
	{
		this.action = action;
	}

}
