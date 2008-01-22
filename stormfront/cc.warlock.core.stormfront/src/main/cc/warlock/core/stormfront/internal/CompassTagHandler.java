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
	public void handleStart(StormFrontAttributeList attributes) {
		compass = new Compass();
	}
	
	public void addDirection(String dir) {
		DirectionType d = directions.get(dir);
		if(d != null) {
			compass.addDirection(d);
		}
	}
	
	@Override
	public void handleEnd() {
		handler.getClient().getCompass().set(compass);
	}
}
