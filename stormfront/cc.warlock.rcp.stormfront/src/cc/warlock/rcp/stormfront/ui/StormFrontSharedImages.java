package cc.warlock.rcp.stormfront.ui;

import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

import cc.warlock.rcp.stormfront.StormFrontRCPPlugin;

/**
 * @author Marshall
 */
public class StormFrontSharedImages {
	
	public static final String IMG_CHARACTER = "/images/character.gif";
	public static final String IMG_GAME = "/images/game.gif";
	
	// Status images
	public static final String IMG_STATUS_BLANK = "/images/status/standard/blank.gif";
	public static final String IMG_STATUS_HIDE = "/images/status/standard/hide.gif";
	public static final String IMG_STATUS_KNEEL = "/images/status/standard/kneel.gif";
	public static final String IMG_STATUS_LIE = "/images/status/standard/lie.gif";
	public static final String IMG_STATUS_SIT = "/images/status/standard/sit.gif";
	public static final String IMG_STATUS_STAND = "/images/status/standard/stand.gif";
	
	public static final String IMG_LEFT_HAND = "/images/left_hand.gif";
	public static final String IMG_RIGHT_HAND = "/images/right_hand.gif";
	public static final String IMG_LEFT_HAND_SMALL = "/images/left_hand_small.gif";
	public static final String IMG_RIGHT_HAND_SMALL= "/images/right_hand_small.gif";
	public static final String IMG_SPELL_HAND_SMALL = "/images/spell_hand_small.gif";
	public static final String IMG_STORMFRONT_SCRIPTS = "/images/stormfront_scripts.gif";
	
	private static StormFrontSharedImages instance;
	
	private HashMap<String, Image> images;
	private HashMap<String, ImageDescriptor> descriptors;
	
	private StormFrontSharedImages ()
	{
		instance = this;
		images = new HashMap<String, Image>();
		descriptors = new HashMap<String, ImageDescriptor>();
	}
	
	private void addImage (String path)
	{
		Bundle pluginBundle = StormFrontRCPPlugin.getDefault().getBundle();
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(pluginBundle.getEntry(path));
		
		descriptors.put(path, descriptor);
		images.put(path, descriptor.createImage()); 
	}
	
	private static StormFrontSharedImages instance() {
		if (instance == null)
			new StormFrontSharedImages();
		
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
