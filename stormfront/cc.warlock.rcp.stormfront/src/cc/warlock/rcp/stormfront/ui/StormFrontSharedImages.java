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
	public static final String IMG_STATUS_BLANK = "/images/status/standard/blank.png";
	public static final String IMG_STATUS_HIDDEN = "/images/status/standard/hidden.png";
	public static final String IMG_STATUS_KNEELING = "/images/status/standard/kneeling.png";
	public static final String IMG_STATUS_PRONE = "/images/status/standard/prone.png";
	public static final String IMG_STATUS_SITTING = "/images/status/standard/sitting.png";
	public static final String IMG_STATUS_STANDING = "/images/status/standard/standing.png";
	public static final String IMG_STATUS_DEAD = "/images/status/standard/dead.png";
	public static final String IMG_STATUS_JOINED = "/images/status/standard/joined.png";
	public static final String IMG_STATUS_STUNNED = "/images/status/standard/stunned.png";
	public static final String IMG_STATUS_BLEEDING = "/images/status/standard/bleeding.png";
	public static final String IMG_STATUS_WEBBED = "/images/status/standard/webbed.png";
	
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
