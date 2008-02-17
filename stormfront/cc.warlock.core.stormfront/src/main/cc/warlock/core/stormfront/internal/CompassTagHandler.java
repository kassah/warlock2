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

import java.util.HashMap;

import cc.warlock.core.client.ICompass.DirectionType;
import cc.warlock.core.client.internal.Compass;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class CompassTagHandler extends DefaultTagHandler {

	Compass compass;
	protected static final HashMap<String, DirectionType> directions = new HashMap<String, DirectionType>();
	
	static {
		for (DirectionType direction : DirectionType.values())
		{
			if (direction != DirectionType.None)
			{
				directions.put(direction.getName(), direction);
				directions.put(direction.getShortName(), direction);
			}
		}
	}
	public CompassTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		addTagHandler(new DirTagHandler(handler, this));
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "compass" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		compass = new Compass();
	}
	
	public void addDirection(String dir) {
		DirectionType d = directions.get(dir);
		if(d != null) {
			compass.addDirection(d);
		}
	}
	
	@Override
	public void handleEnd(String rawXML) {
		handler.getClient().getCompass().set(compass);
	}
}
