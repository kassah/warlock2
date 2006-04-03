/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.client.internal;

import java.util.HashMap;

import com.arcaner.warlock.client.ICompass;
import com.arcaner.warlock.client.IPropertyListener;
import com.arcaner.warlock.client.IWarlockClient;

/**
 * @author Marshall
 */
public class Compass extends ClientProperty implements ICompass {
	
	protected boolean[] compass;

	protected static HashMap<String, Integer> directions;
	
	public Compass(IWarlockClient client) {
		super(client);
		
		compass = new boolean[N_DIRECTIONS];
		for(int i = 0; i < N_DIRECTIONS; i++) {
			compass[i] = false;
		}
		directions = new HashMap<String, Integer>();
		directions.put("n", new Integer(ICompass.NORTH));
		directions.put("s", new Integer(ICompass.SOUTH));
		directions.put("e", new Integer(ICompass.EAST));
		directions.put("w", new Integer(ICompass.WEST));
		directions.put("ne", new Integer(ICompass.NORTHEAST));
		directions.put("nw", new Integer(ICompass.NORTHWEST));
		directions.put("se", new Integer(ICompass.SOUTHEAST));
		directions.put("sw", new Integer(ICompass.SOUTHWEST));
		directions.put("out", new Integer(ICompass.OUT));
		directions.put("down", new Integer(ICompass.DOWN));
		directions.put("up", new Integer(ICompass.UP));

		directions.put("north", new Integer(ICompass.NORTH));
		directions.put("south", new Integer(ICompass.SOUTH));
		directions.put("east", new Integer(ICompass.EAST));
		directions.put("west", new Integer(ICompass.WEST));
		directions.put("northeast", new Integer(ICompass.NORTHEAST));
		directions.put("northwest", new Integer(ICompass.NORTHWEST));
		directions.put("southeast", new Integer(ICompass.SOUTHEAST));
		directions.put("southwest", new Integer(ICompass.SOUTHWEST));
		directions.put("out", new Integer(ICompass.OUT));
		directions.put("down", new Integer(ICompass.DOWN));
		directions.put("up", new Integer(ICompass.UP));
		
	}
	
	public boolean[] getDirections() {
		return compass;
	}

	public void clear() {
		for (int i = 0; i < N_DIRECTIONS; i++) {
			compass[i] = false;
		}
		
		for (IPropertyListener listener : listeners) {
			listener.propertyCleared();
		}
	}

	public void set(String data) {
		// We do not notify on set, only when the compass is finished.
		int direction = directions.get(data);
		
		compass[direction] = true;
	}
	
	public void activate() {
		for(IPropertyListener listener : listeners) {
			listener.propertyActivated();
		}
	}
	
	
	public static int getValue(String direction) {
		return directions.get(direction);
	}
	
	public static int oppositeDir(int direction) {
		switch( direction ) {
		case ICompass.SOUTHWEST: return ICompass.NORTHEAST; 
		case ICompass.SOUTH: return ICompass.NORTH;
		case ICompass.SOUTHEAST: return ICompass.NORTHWEST;
		case ICompass.EAST: return ICompass.WEST;
		case ICompass.NORTHEAST: return ICompass.SOUTHWEST;
		case ICompass.NORTH: return ICompass.SOUTH;
		case ICompass.NORTHWEST: return ICompass.SOUTHEAST;
		case ICompass.WEST: return ICompass.EAST;
		case ICompass.UP: return ICompass.DOWN;
		case ICompass.DOWN: return ICompass.UP;
		}
		return -1;
	}
	
	public static String nameFromInt(int direction) {
		switch( direction ) {
		case ICompass.SOUTHWEST: return "southwest"; 
		case ICompass.SOUTH: return "south";
		case ICompass.SOUTHEAST: return "southeast";
		case ICompass.EAST: return "east";
		case ICompass.NORTHEAST: return "northeast";
		case ICompass.NORTH: return "north";
		case ICompass.NORTHWEST: return "northwest";
		case ICompass.WEST: return "west";
		case ICompass.UP: return "up";
		case ICompass.DOWN: return "down";
		}
		return "";
	}
}
