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

import cc.warlock.core.client.settings.IClientSetting;
import cc.warlock.core.client.settings.IClientSettingProvider;

/**
 * The base implementation class for all client settings
 * @author marshall
 */
public class ClientSetting implements IClientSetting {

	protected IClientSettingProvider provider;
	protected ClientSetting originalSetting;
	protected boolean needsUpdate;
	
	public ClientSetting (IClientSettingProvider provider)
	{
		this.provider = provider;
	}
	
	public ClientSetting (ClientSetting other)
	{
		this.provider = other.provider;
		this.originalSetting = other;
	}
	
	/* (non-Javadoc)
	 * @see cc.warlock.core.client.settings.IClientSetting#getProvider()
	 */
	public IClientSettingProvider getProvider() {
		return provider;
	}
	
	public ClientSetting getOriginalSetting ()
	{
		return originalSetting;
	}
	
	public boolean needsUpdate ()
	{
		return needsUpdate;
	}

}
