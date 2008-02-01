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
package cc.warlock.rcp.application;

import java.util.Arrays;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.PlatformUI;

import cc.warlock.core.configuration.IConfigurationProvider;

public class WarlockPerspectiveLayout implements IConfigurationProvider {
	protected Rectangle bounds = new Rectangle(25, 25, 1024, 768);
	protected static WarlockPerspectiveLayout _instance;
	
	public static WarlockPerspectiveLayout instance()
	{
		if (_instance == null) _instance = new WarlockPerspectiveLayout();
		return _instance;
	}
	
	protected WarlockPerspectiveLayout() { }
	
	public List<Element> getTopLevelElements() {
		Element windowLayout = DocumentHelper.createElement("window-layout");
		windowLayout.addAttribute("x", "" + bounds.x);
		windowLayout.addAttribute("y", "" + bounds.y);
		windowLayout.addAttribute("width", "" + bounds.width);
		windowLayout.addAttribute("height", "" + bounds.height);
		
		return Arrays.asList(new Element[] { windowLayout });
	}

	public void parseElement(Element element) {
		if (element.attribute("x") != null)
		{
			bounds.x = Integer.parseInt(element.attributeValue("x"));
			bounds.y = Integer.parseInt(element.attributeValue("y"));
			bounds.width = Integer.parseInt(element.attributeValue("width"));
			bounds.height = Integer.parseInt(element.attributeValue("height"));
		}
	}

	public boolean supportsElement(Element element) {
		if (element.getName().equals("window-layout"))
		{
			return true;
		}
		return false;
	}
	
	public void saveLayout ()
	{
		bounds = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getBounds();
	}
	
	public void loadBounds ()
	{
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setBounds(bounds);
	}
}
