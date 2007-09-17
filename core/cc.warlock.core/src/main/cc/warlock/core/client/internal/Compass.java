/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.client.internal;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import cc.warlock.core.client.ICompass;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.client.IWarlockClient;


/**
 * @author Marshall
 */
public class Compass extends ClientProperty<String> implements ICompass {
	
	protected Map<DirectionType, Boolean> compass;
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
	
	public Compass(IWarlockClient client) {
		super(client, "compass", null);
		
		compass = new Hashtable<DirectionType, Boolean>();
		
		for(DirectionType type : DirectionType.values())
		{
			compass.put(type, false);
		}		
	}
	
	public Map<DirectionType, Boolean> getDirections() {
		return compass;
	}

	public void clear() {
		for (DirectionType type : DirectionType.values())
		{
			compass.put(type, false);
		}
		
		for (IPropertyListener<String> listener : listeners) {
			listener.propertyCleared(this, null);
		}
	}

	public void set(String data) {
		// We do not notify on set, only when the compass is finished.
		DirectionType direction = directions.get(data);
		
		compass.put(direction, true);
		for (IPropertyListener<String> listener : listeners)
		{
			listener.propertyChanged(this, null);
		}
	}
	
	public void activate() {
		for(IPropertyListener<String> listener : listeners) {
			listener.propertyActivated(this);
		}
	}
	
	public static DirectionType getValue(String direction) {
		return directions.get(direction);
	}
}
