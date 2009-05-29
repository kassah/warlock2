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

import cc.warlock.core.client.settings.ITrigger;
import cc.warlock.core.client.settings.ITriggerProvider;

public class TriggerConfigurationProvider extends PatternConfigurationProvider
		implements ITriggerProvider {

	protected ArrayList<ITrigger> triggers = new ArrayList<ITrigger>();
	
	public TriggerConfigurationProvider ()
	{
		super("triggers");
		
		setHandleChildren(false);
	}
	
	@Override
	protected void parseData() {}

	@Override
	protected void parseChild(Element child) {
		if (child.getName().equals("trigger"))
		{
			Trigger trigger = new Trigger(this, null);
			fillSetting(trigger, child);
			
			triggers.add(trigger);
		}
	}
	
	@Override
	protected void saveTo(List<Element> elements) {
		Element triggersElement = DocumentHelper.createElement("triggers");
		
		for (ITrigger trigger : triggers)
		{
			Element tElement = triggersElement.addElement("trigger");
			fillElement(tElement, trigger);
			
			elements.add(tElement);
		}
		
		elements.add(triggersElement);
	}

	public void addTrigger(ITrigger trigger) {
		triggers.add(trigger);
	}

	public List<? extends ITrigger> getTriggers() {
		return triggers;
	}

	public void removeTrigger(ITrigger trigger) {
		triggers.remove(trigger);
	}

}
