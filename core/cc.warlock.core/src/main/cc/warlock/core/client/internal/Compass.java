package cc.warlock.core.client.internal;

import java.util.HashSet;
import java.util.Set;

import cc.warlock.core.client.ICompass;

public class Compass implements ICompass {

	private HashSet<DirectionType> dirs = new HashSet<DirectionType>();
	
	public boolean addDirection(DirectionType dir) {
		return dirs.add(dir);
	}
	
	public Set<DirectionType> getDirections() {
		return dirs;
	}

}