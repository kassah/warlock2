/*
 * Created on Jan 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.client;

/**
 * @author Marshall
 *
 * This interface represents the compass data structure
 */
public interface ICompass extends IProperty {
	
	public static int NORTH = 0;
	public static int NORTHEAST = 1;
	public static int EAST = 2;
	public static int SOUTHEAST = 3;
	public static int SOUTH = 4;
	public static int SOUTHWEST = 5;
	public static int WEST = 6;
	public static int NORTHWEST = 7;
	public static int UP = 8;
	public static int DOWN = 9;
	public static int OUT = 10;
	public static final int N_DIRECTIONS = 11;
	
	/**
	 * @return An array of the available directions
	 */
	public boolean[] getDirections();
	
}
