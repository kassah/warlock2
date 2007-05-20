package com.arcaner.warlock.configuration.server;

import java.util.Hashtable;

import org.dom4j.Element;

import com.arcaner.warlock.client.stormfront.WarlockColor;

public class Palette extends ServerSetting {

	public static final String STORMFRONT_MARKUP_PREFIX = "<palette><<m>";
	public static final String STORMFRONT_MARKUP_SUFFIX = "</<m></palette>";
	
	protected Hashtable<String, PaletteEntry> palette = new Hashtable<String, PaletteEntry>();
	protected Element element;
	
	public Palette (ServerSettings serverSettings, Element paletteElement)
	{
		super(serverSettings, paletteElement);
		
		for (Object o : paletteElement.elements())
		{
			Element iElement = (Element) o;
			String paletteId = iElement.attributeValue("id");
			
			PaletteEntry entry = new PaletteEntry(paletteId, new WarlockColor(iElement.attributeValue("color")));
			entry.getColor().setPaletteId(paletteId);
			palette.put(paletteId, entry);
		}
	}
	
	protected class PaletteEntry {
		protected String id;
		protected WarlockColor originalColor, color;
		protected boolean needsUpdate = false;
		
		public PaletteEntry (String id, WarlockColor color)
		{
			this.id = id;
			this.color = color;
		}
		
		public void setColor (WarlockColor color)
		{
			originalColor = this.color;
			
			if (!color.equals(this.color))
				needsUpdate = true;
			
			this.color = color;
		}
		
		public WarlockColor getColor () { return this.color; }
		public String getId() { return this.id; }
	}
	
	public WarlockColor getPaletteColor (String id)
	{
		if (palette.containsKey(id))
		{
			return palette.get(id).getColor();
		}
		return null;
	}
	
	public String findColor (WarlockColor color)
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
	public String getFirstFreePaletteId ()
	{
		boolean skipEntry = true;
		PaletteEntry firstFreeEntry = null;
		
		for (PaletteEntry entry : palette.values())
		{
			WarlockColor color = entry.getColor();
			
			if (color.getRed() == 255 && color.getGreen() == 255 && color.getBlue() == 255)
			{
				if (skipEntry) {
					skipEntry = false;
				} else {
					firstFreeEntry = entry;
					break;
				}
			} else if (color.getPaletteReferenceCount() == 0) {
				firstFreeEntry = entry;
				break;
			}
		}
		
		return firstFreeEntry == null ? null : firstFreeEntry.getId();
	}
	
	public void setPaletteColor (String id, WarlockColor color)
	{
		if (palette.containsKey(id))
		{
			palette.get(id).setColor(color);
		}
	}
	
	protected Element getIElement(String id)
	{
		Element iElement = (Element) element.selectSingleNode("i[@id='" + id + "']");
		if (iElement == null)
		{
			iElement = element.addElement("i");
			iElement.addAttribute("id", id);
		}
		return iElement;
	}
	
	protected void saveToDOM ()
	{
		for (String id : palette.keySet())
		{
			Element iElement = getIElement(id);
			
			setAttribute(iElement, "color", palette.get(id).getColor().toHexString());
		}
	}
	
	protected String toStormfrontMarkup() {
		String markup = new String(STORMFRONT_MARKUP_PREFIX);
		
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
