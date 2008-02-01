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
package cc.warlock.rcp.stormfront.ui.script;

import java.text.Collator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import cc.warlock.core.stormfront.serversettings.server.IServerScriptInfo;

public class ServerScriptSorter extends ViewerSorter {

	public ServerScriptSorter() {
		// TODO Auto-generated constructor stub
	}

	public ServerScriptSorter(Collator collator) {
		super(collator);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		int comparison = super.compare(viewer, e1, e2);
		
		if (e1 instanceof IServerScriptInfo && e2 instanceof IServerScriptInfo)
		{
			IServerScriptInfo s1 = (IServerScriptInfo)e1;
			IServerScriptInfo s2 = (IServerScriptInfo)e2;
			
			return s1.getScriptName().compareTo(s2.getScriptName());
		}
		
		return comparison;
	}
}
