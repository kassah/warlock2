/*
 * Created on Dec 31, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.ui;

import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

import cc.warlock.rcp.plugin.Warlock2Plugin;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WarlockSharedImages {
	
	public static final String IMG_WIZBAN_WARLOCK = "/images/warlock-wizard-icon.png";
	public static final String IMG_CHARACTER = "/images/character.gif";
	
	// Compass Images
	public static final String IMG_COMPASS_LARGE_MAIN = "/images/compass/large/compass_main.png";
	public static final String IMG_COMPASS_LARGE_NORTH_ON = "/images/compass/large/north_on.png";
	public static final String IMG_COMPASS_LARGE_SOUTH_ON = "/images/compass/large/south_on.png";
	public static final String IMG_COMPASS_LARGE_EAST_ON = "/images/compass/large/east_on.png";
	public static final String IMG_COMPASS_LARGE_WEST_ON = "/images/compass/large/west_on.png";
	public static final String IMG_COMPASS_LARGE_NORTHEAST_ON = "/images/compass/large/northeast_on.png";
	public static final String IMG_COMPASS_LARGE_NORTHWEST_ON = "/images/compass/large/northwest_on.png";
	public static final String IMG_COMPASS_LARGE_SOUTHEAST_ON = "/images/compass/large/southeast_on.png";
	public static final String IMG_COMPASS_LARGE_SOUTHWEST_ON = "/images/compass/large/southwest_on.png";
	public static final String IMG_COMPASS_LARGE_UP_ON = "/images/compass/large/up_on.png";
	public static final String IMG_COMPASS_LARGE_DOWN_ON = "/images/compass/large/down_on.png";
	public static final String IMG_COMPASS_LARGE_OUT_ON = "/images/compass/large/out_on.png";
	
	public static final String IMG_COMPASS_SMALL_MAIN = "/images/compass/small/compass_main.png";
	public static final String IMG_COMPASS_SMALL_NORTH_ON = "/images/compass/small/north_on.png";
	public static final String IMG_COMPASS_SMALL_SOUTH_ON = "/images/compass/small/south_on.png";
	public static final String IMG_COMPASS_SMALL_EAST_ON = "/images/compass/small/east_on.png";
	public static final String IMG_COMPASS_SMALL_WEST_ON = "/images/compass/small/west_on.png";
	public static final String IMG_COMPASS_SMALL_NORTHEAST_ON = "/images/compass/small/northeast_on.png";
	public static final String IMG_COMPASS_SMALL_NORTHWEST_ON = "/images/compass/small/northwest_on.png";
	public static final String IMG_COMPASS_SMALL_SOUTHEAST_ON = "/images/compass/small/southeast_on.png";
	public static final String IMG_COMPASS_SMALL_SOUTHWEST_ON = "/images/compass/small/southwest_on.png";
	public static final String IMG_COMPASS_SMALL_UP_ON = "/images/compass/small/up_on.png";
	public static final String IMG_COMPASS_SMALL_DOWN_ON = "/images/compass/small/down_on.png";
	public static final String IMG_COMPASS_SMALL_OUT_ON = "/images/compass/small/out_on.png";
	
	public static final String IMG_CONNECT = "/images/connect.png";
	public static final String IMG_CONNECT_LARGE = "/images/connect_large.png";
	public static final String IMG_WINDOW = "/images/window.png";
	public static final String IMG_ADD = "/images/add.png";
	public static final String IMG_REMOVE = "/images/remove.png";
	
	private static WarlockSharedImages instance;
	
	private HashMap<String, Image> images;
	private HashMap<String, ImageDescriptor> descriptors;
	
	private WarlockSharedImages ()
	{
		instance = this;
		images = new HashMap<String, Image>();
		descriptors = new HashMap<String, ImageDescriptor>();
	}
	
	private void addImage (String path)
	{
		Bundle pluginBundle = Warlock2Plugin.getDefault().getBundle();
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(pluginBundle.getEntry(path));
		
		descriptors.put(path, descriptor);
		images.put(path, descriptor.createImage()); 
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
		if (!images.containsKey(key))
		{
			addImage(key);
		}
		
		return (Image) images.get(key);
	}
	
	public ImageDescriptor descriptor(String key)
	{
		if (!descriptors.containsKey(key))
		{
			addImage(key);
		}
		
		return (ImageDescriptor) descriptors.get(key);
	}
	
	protected void finalize() throws Throwable {
		for (Image image : images.values()) {
			image.dispose();
		}
		super.finalize();
	}
}
