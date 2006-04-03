package com.arcaner.warlock.client.internal;

import java.util.ArrayList;

import com.arcaner.warlock.client.IProperty;
import com.arcaner.warlock.client.IPropertyListener;
import com.arcaner.warlock.client.IWarlockClient;

public class ClientProperty implements IProperty {

	protected String data;
	protected ArrayList<IPropertyListener> listeners = new ArrayList<IPropertyListener>();
	protected IWarlockClient client;
	
	public ClientProperty(IWarlockClient client) {
		this.client = client;
	}
	
	public void set(String data) {
		this.data = data;
		for(IPropertyListener listener : listeners) {
			listener.propertyChanged();
		}
	}
	
	public String get() {
		return data;
	}
	
	public void clear() {
		data = null;
		
		for(IPropertyListener listener : listeners) {
			listener.propertyCleared();
		}
	}
	
	public void activate() { }
	
	public void addListener(IPropertyListener listener) {
		listeners.add(listener);
	}
	
	public boolean removeListener(IPropertyListener listener) {
		return listeners.remove(listener);
	}
	
	public IWarlockClient getClient() {
		return client;
	}
}
