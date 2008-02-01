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
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.client;

import java.util.Set;

/**
 * @author Marshall
 *
 * This interface represents the compass data structure
 */
public interface ICompass {
	
	public static enum DirectionType
	{
		North, NorthEast, East, SouthEast, South, SouthWest, West, NorthWest, Up, Down, Out, None;
		
		public DirectionType oppositeDir () {
			switch( this ) {
				case SouthWest: return DirectionType.NorthEast; 
				case South: return DirectionType.North;
				case SouthEast: return DirectionType.NorthWest;
				case East: return DirectionType.West;
				case NorthEast: return DirectionType.SouthWest;
				case North: return DirectionType.South;
				case NorthWest: return DirectionType.SouthEast;
				case West: return DirectionType.East;
				case Up: return DirectionType.Down;
				case Down: return DirectionType.Up;
			}
			return DirectionType.None;
		}
		
		public String getShortName() {
			switch( this ) {
				case SouthWest: return "sw"; 
				case South: return "s";
				case SouthEast: return "se";
				case East: return "e";
				case NorthEast: return "ne";
				case North: return "n";
				case NorthWest: return "nw";
				case West: return "w";
				case Up: return "up";
				case Down: return "down";
				case Out: return "out";
			}
			return "";
		}
		
		public String getName() {
			switch( this ) {
				case SouthWest: return "southwest"; 
				case South: return "south";
				case SouthEast: return "southeast";
				case East: return "east";
				case NorthEast: return "northeast";
				case North: return "north";
				case NorthWest: return "northwest";
				case West: return "west";
				case Up: return "up";
				case Down: return "down";
				case Out: return "out";
			}
			return "";
		}
	};
	
	/**
	 * @return An array of the available directions
	 */
	public Set<DirectionType> getDirections();
	
}
