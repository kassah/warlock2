package cc.warlock.scribe.ui;

import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScribeSharedImages {

	public static final String IMG_SCRIPT = "/icons/script.gif";
	public static final String IMG_START = "/icons/run_exc.gif";
	public static final String IMG_RESUME = "/icons/resume_co.gif";
	public static final String IMG_SUSPEND = "/icons/suspend_co.gif";
	public static final String IMG_TERMINATE = "/icons/terminate_co.gif";
	
	private static ScribeSharedImages instance;
	
	private HashMap<String, Image> images;
	private HashMap<String, ImageDescriptor> descriptors;
	
	private ScribeSharedImages ()
	{
		instance = this;
		images = new HashMap<String, Image>();
		descriptors = new HashMap<String, ImageDescriptor>();
	}
	
	private void addImage (String path)
	{
		Bundle pluginBundle = Activator.getDefault().getBundle();
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(pluginBundle.getEntry(path));
		
		descriptors.put(path, descriptor);
		images.put(path, descriptor.createImage()); 
	}
	
	private static ScribeSharedImages instance() {
		if (instance == null)
			new ScribeSharedImages();
		
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
