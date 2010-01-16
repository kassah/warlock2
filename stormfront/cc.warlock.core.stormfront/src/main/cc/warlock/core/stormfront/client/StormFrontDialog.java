/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2010, Warlock LLC, and individual contributors as indicated
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

package cc.warlock.core.stormfront.client;

import java.util.ArrayList;

import cc.warlock.core.stormfront.IStormFrontDialogListener;

/**
 * @author sproctor
 *
 */
public class StormFrontDialog {
	private String id;
	private ArrayList<IStormFrontDialogListener> listeners = new ArrayList<IStormFrontDialogListener>();
	
	public StormFrontDialog(String id) {
		this.id = id;
	}
	
	public void addListener(IStormFrontDialogListener listener) {
		listeners.add(listener);
	}
	
	public void progressBar(String id, String text, int value, String left,
			String top, String width, String height) {
		for(IStormFrontDialogListener listener : listeners) {
			listener.progressBar(id, text, value, left, top, width, height);
		}
	}
	
	public String getId() {
		return id;
	}
}
