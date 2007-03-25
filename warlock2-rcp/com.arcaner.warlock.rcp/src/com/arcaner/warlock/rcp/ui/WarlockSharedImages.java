/*
 * Created on Dec 31, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.rcp.ui;

import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

import com.arcaner.warlock.rcp.plugin.Warlock2Plugin;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WarlockSharedImages {
	public static final String IMG_WIZBAN_WARLOCK = "warlock.wizard.image";
	public static final String IMG_CHARACTER = "warlock.character";
	public static final String IMG_GAME = "warlock.game";
	
	// Compass Images
	public static final String IMG_COMPASS_NORTH_OFF = "compass.north.off";
	public static final String IMG_COMPASS_NORTH_ON = "compass.north.on";
	public static final String IMG_COMPASS_SOUTH_OFF = "compass.south.off";
	public static final String IMG_COMPASS_SOUTH_ON = "compass.south.on";
	public static final String IMG_COMPASS_EAST_OFF = "compass.east.off";
	public static final String IMG_COMPASS_EAST_ON = "compass.east.on";
	public static final String IMG_COMPASS_WEST_OFF = "compass.west.off";
	public static final String IMG_COMPASS_WEST_ON = "compass.west.on";
	public static final String IMG_COMPASS_NORTHEAST_OFF = "compass.northeast.off";
	public static final String IMG_COMPASS_NORTHEAST_ON = "compass.northeast.on";
	public static final String IMG_COMPASS_NORTHWEST_OFF = "compass.northwest.off";
	public static final String IMG_COMPASS_NORTHWEST_ON = "compass.northwest.on";
	public static final String IMG_COMPASS_SOUTHEAST_OFF = "compass.southeast.off";
	public static final String IMG_COMPASS_SOUTHEAST_ON = "compass.southeast.on";
	public static final String IMG_COMPASS_SOUTHWEST_OFF = "compass.southwest.off";
	public static final String IMG_COMPASS_SOUTHWEST_ON = "compass.southwest.on";
	public static final String IMG_COMPASS_UP_ON = "compass.up.on";
	public static final String IMG_COMPASS_UP_OFF = "compass.up.off";
	public static final String IMG_COMPASS_DOWN_ON = "compass.down.on";
	public static final String IMG_COMPASS_DOWN_OFF = "compass.down.off";
	public static final String IMG_COMPASS_OUT_ON = "compass.out.on";
	public static final String IMG_COMPASS_OUT_OFF = "compass.out.off";
	
	private static WarlockSharedImages instance;
	
	private HashMap<String, Image> images;
	private HashMap<String, ImageDescriptor> descriptors;
	
	private WarlockSharedImages ()
	{
		instance = this;
		images = new HashMap<String, Image>();
		descriptors = new HashMap<String, ImageDescriptor>();
		
		addImage(IMG_WIZBAN_WARLOCK, "/icons/warlock-wizard-icon.gif");
		addImage(IMG_CHARACTER, "/icons/character.gif");
		addImage(IMG_GAME, "/icons/game.gif");
		
		
		addImage(IMG_COMPASS_NORTH_OFF, "/icons/north.gif");
		addImage(IMG_COMPASS_NORTH_ON, "/icons/north_on.gif");
		addImage(IMG_COMPASS_SOUTH_OFF, "/icons/south.gif");
		addImage(IMG_COMPASS_SOUTH_ON, "/icons/south_on.gif");
		addImage(IMG_COMPASS_EAST_OFF, "/icons/east.gif");
		addImage(IMG_COMPASS_EAST_ON, "/icons/east_on.gif");
		addImage(IMG_COMPASS_WEST_OFF, "/icons/west.gif");
		addImage(IMG_COMPASS_WEST_ON, "/icons/west_on.gif");
		addImage(IMG_COMPASS_NORTHEAST_OFF, "/icons/northeast.gif");
		addImage(IMG_COMPASS_NORTHEAST_ON, "/icons/northeast_on.gif");
		addImage(IMG_COMPASS_NORTHWEST_OFF, "/icons/northwest.gif");
		addImage(IMG_COMPASS_NORTHWEST_ON, "/icons/northwest_on.gif");
		addImage(IMG_COMPASS_SOUTHEAST_OFF, "/icons/southeast.gif");
		addImage(IMG_COMPASS_SOUTHEAST_ON, "/icons/southeast_on.gif");
		addImage(IMG_COMPASS_SOUTHWEST_OFF, "/icons/southwest.gif");
		addImage(IMG_COMPASS_SOUTHWEST_ON, "/icons/southwest_on.gif");
		addImage(IMG_COMPASS_UP_ON, "/icons/up_on.gif");
		addImage(IMG_COMPASS_UP_OFF, "/icons/up.gif");
		addImage(IMG_COMPASS_DOWN_ON, "/icons/down_on.gif");
		addImage(IMG_COMPASS_DOWN_OFF, "/icons/down.gif");
		addImage(IMG_COMPASS_OUT_ON, "/icons/out_on.gif");
		addImage(IMG_COMPASS_OUT_OFF, "/icons/out.gif");
		
		for (String key : descriptors.keySet()) {
			ImageDescriptor descriptor = descriptor(key);
			images.put(key,  descriptor.createImage());	
		}
		
	}
	
	private void addImage (String id, String path)
	{
		Bundle pluginBundle = Warlock2Plugin.getDefault().getBundle();
		descriptors.put(id, ImageDescriptor.createFromURL(pluginBundle.getEntry(path)));
	}
	
	private static WarlockSharedImages instance() {
		if (instance == null)
			new WarlockSharedImages();
		
		return instance;
	}
	
	public static Image getImage(String key)
	{
		return instance().image(key);
	}
	
	public static ImageDescriptor getImageDescriptor(String key)
	{
		return instance().descriptor(key);
	}
	
	public Image image(String key)
	{
		return (Image) images.get(key);
	}
	
	public ImageDescriptor descriptor(String key)
	{
		return (ImageDescriptor) descriptors.get(key);
	}
	
	protected void finalize() throws Throwable {
		for (Image image : images.values()) {
			image.dispose();
		}
		super.finalize();
	}
}
