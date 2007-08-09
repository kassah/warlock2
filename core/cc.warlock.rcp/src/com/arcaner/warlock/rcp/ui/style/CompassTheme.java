package com.arcaner.warlock.rcp.ui.style;

import java.util.Hashtable;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import com.arcaner.warlock.client.ICompass.DirectionType;
import com.arcaner.warlock.rcp.ui.WarlockSharedImages;

public class CompassTheme {

	private String name, title, description;
	private Image mainImage;
	private Hashtable<DirectionType, Image> directionImages = new Hashtable<DirectionType, Image>();
	private Hashtable<DirectionType, Point> directionPositions = new Hashtable<DirectionType, Point>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Image getMainImage() {
		return mainImage;
	}
	
	public void setMainImage(String mainImagePath) {
		this.mainImage = WarlockSharedImages.getImage(mainImagePath);
	}
	
	public Image getDirectionImage (DirectionType direction)
	{
		return directionImages.get(direction);
	}
	
	public void setDirectionImage (DirectionType direction, String imagePath)
	{
		directionImages.put(direction, WarlockSharedImages.getImage(imagePath));
	}
	
	public Point getDirectionPosition (DirectionType direction)
	{
		return directionPositions.get(direction);
	}
	
	public void setDirectionPosition (DirectionType direction, String position)
	{
		String[] xy = position.split(",");
		setDirectionPosition(direction, new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
	}
	
	public void setDirectionPosition (DirectionType direction, Point position)
	{
		directionPositions.put(direction, position);
	}
}
