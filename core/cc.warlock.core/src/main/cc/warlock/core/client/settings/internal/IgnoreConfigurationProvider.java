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
package cc.warlock.core.client.settings.internal;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cc.warlock.core.client.settings.IIgnore;
import cc.warlock.core.client.settings.IIgnoreProvider;

public class IgnoreConfigurationProvider extends PatternConfigurationProvider
		implements IIgnoreProvider {

	protected ArrayList<IIgnore> ignores = new ArrayList<IIgnore>();
	
	public IgnoreConfigurationProvider ()
	{
		super("ignores");
		
		setHandleChildren(false);
	}
	
	@Override
	protected void parseData() {}

	@Override
	protected void parseChild(Element child) {
		if (child.getName().equals("ignore"))
		{
			Ignore ignore = new Ignore(this);
			fillSetting(ignore, child);
			
			ignores.add(ignore);
		}
	}
	
	@Override
	protected void saveTo(List<Element> elements) {
		Element ignoresElement = DocumentHelper.createElement("ignores");
		
		for (IIgnore ignore : ignores)
		{
			Element iElement = ignoresElement.addElement("ignore");
			fillElement(iElement, ignore);
		}
		
		elements.add(ignoresElement);
	}

	public void addIgnore(IIgnore ignore) {
		ignores.add(ignore);
	}

	public List<? extends IIgnore> getIgnores() {
		return ignores;
	}

	public void removeIgnore(IIgnore ignore) {
		ignores.remove(ignore);
	}

}
