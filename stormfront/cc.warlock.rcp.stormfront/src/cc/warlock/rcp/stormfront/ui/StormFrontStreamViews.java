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

import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.rcp.views.StreamView;

public class StormFrontStreamViews {
	public static final String DEATH_VIEW_ID =  StreamView.STREAM_VIEW_PREFIX + IStormFrontClient.DEATH_STREAM_NAME;
	public static final String INVENTORY_VIEW_ID = StreamView.STREAM_VIEW_PREFIX  + IStormFrontClient.INVENTORY_STREAM_NAME;
	public static final String THOUGHTS_VIEW_ID = StreamView.STREAM_VIEW_PREFIX + IStormFrontClient.THOUGHTS_STREAM_NAME ;
}
