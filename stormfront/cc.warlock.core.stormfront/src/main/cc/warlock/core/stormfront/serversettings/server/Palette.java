package cc.warlock.core.stormfront.serversettings.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import cc.warlock.core.stormfront.client.StormFrontColor;
import cc.warlock.core.stormfront.xml.StormFrontElement;

public class Palette extends ServerSetting {

	public static final String STORMFRONT_MARKUP_PREFIX = "<palette><<m>";
	public static final String STORMFRONT_MARKUP_SUFFIX = "</<m></palette>";
	
	protected Hashtable<String, PaletteEntry> palette = new Hashtable<String, PaletteEntry>();
	
	public Palette (ServerSettings serverSettings, StormFrontElement paletteElement)
	{
		super(serverSettings, paletteElement);
		
		for (StormFrontElement iElement : paletteElement.elements())
		{
			String paletteId = iElement.attributeValue("id");
			
			PaletteEntry entry = new PaletteEntry(paletteId, new StormFrontColor(iElement.attributeValue("color")));
			entry.getColor().setPaletteId(paletteId);
			palette.put(paletteId, entry);
		}
	}
	
	protected class PaletteEntry {
		protected String id;
		protected StormFrontColor originalColor, color;
		protected boolean needsUpdate = false;
		
		public PaletteEntry (String id, StormFrontColor color)
		{
			this.id = id;
			this.color = color;
		}
		
		public void setColor (StormFrontColor color)
		{
			originalColor = this.color;
			
			if (!color.equals(this.color)) {
				needsUpdate = true;
				StormFrontElement iElement = getIElement(id);
				
				setAttribute(iElement, "color", color.toHexString());
			}
			
			this.color = color;
		}
		
		public StormFrontColor getColor () { return this.color; }
		public String getId() { return this.id; }
	}
	
	public StormFrontColor getPaletteColor (String id)
	{
		if (palette.containsKey(id))
		{
			return palette.get(id).getColor();
		}
		return null;
	}
	
	public List<StormFrontColor> getAllColors ()
	{
		ArrayList<StormFrontColor> colors = new ArrayList<StormFrontColor>();
		ArrayList<String> ids = new ArrayList<String>();
		ids.addAll(palette.keySet());
		Collections.sort(ids, new Comparator<String> () {
			public int compare(String o1, String o2) {
				return Integer.parseInt(o1) - Integer.parseInt(o2);
			}
		});
		
		
		for (String id : ids) {
			colors.add(palette.get(id).color);
		}
		
		return colors;
	}
	
	public String findColor (StormFrontColor color)
	{
		for (PaletteEntry entry : palette.values())
		{
			if (color.equals(entry.getColor()))
				return entry.getId();
		}
		return null;
	}
	
	// A "free" palette color for now is just one that's defined as white or #FFFFFF, or has 0 references.
	// We'll start with the 2nd instance we can find so there's always at least one white palette color.
	// Not sure what to do when we start running out of palette entries but hopefully that error won't pop up
	// for 99% of usecases. (Best case scenario we can just start inserting new palette entries)
//	public String getFirstFreePaletteId ()
//	{
//		boolean skipEntry = true;
//		PaletteEntry firstFreeEntry = null;
//		
//		for (PaletteEntry entry : palette.values())
//		{
//			StormFrontColor color = entry.getColor();
//			
//			if (color.getRed() == 255 && color.getGreen() == 255 && color.getBlue() == 255)
//			{
//				if (skipEntry) {
//					skipEntry = false;
//				} else {
//					firstFreeEntry = entry;
//					break;
//				}
//			} else if (color.getPaletteReferenceCount() == 0) {
//				firstFreeEntry = entry;
//				break;
//			}
//		}
//		
//		return firstFreeEntry == null ? null : firstFreeEntry.getId();
//	}
	
	public void setPaletteColor (String id, StormFrontColor color)
	{
		if (palette.containsKey(id))
		{
			palette.get(id).setColor(color);
		}
	}
	
	protected StormFrontElement getIElement(String id)
	{
		StormFrontElement iElement = null;
		
		for (StormFrontElement el : element.elements())
		{
			if (el.attributeValue("id").equals(id))
			{
				iElement = el;
				break;
			}
		}
		
		if (iElement == null)
		{
			iElement = new StormFrontElement("i");
			element.addElement(iElement);
			iElement.setAttribute("id", id);
		}
		return iElement;
	}
	
	protected void saveToDOM ()
	{
		for (String id : palette.keySet())
		{
			StormFrontElement iElement = getIElement(id);
			
			setAttribute(iElement, "color", palette.get(id).getColor().toHexString());
		}
	}
	
	protected String toStormfrontMarkup() {
		String markup = STORMFRONT_MARKUP_PREFIX;
		
		for (String id : palette.keySet())
		{
			PaletteEntry entry = palette.get(id);
			if (entry.needsUpdate)
			{
				markup += "<i id='" + id + "' color='" + entry.originalColor.toHexString() + "'/>";
				markup += "<i id='" + id + "' color='" + entry.getColor().toHexString() + "'/>";
				entry.needsUpdate = false;
				entry.originalColor = null;
			}
		}
		
		markup += STORMFRONT_MARKUP_SUFFIX;
		return markup;
	}
	
	public boolean needsUpdate ()
	{
		for (PaletteEntry entry : palette.values())
			if (entry.needsUpdate) return true;
		return false;
	}
}
