package cc.warlock.core.client.internal;

import java.util.Hashtable;
import java.util.Map;

import cc.warlock.core.client.ICharacterStatus;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.client.IWarlockClient;

public class CharacterStatus extends ClientProperty<String> implements ICharacterStatus {
	
	protected Map<StatusType, Boolean> status = new Hashtable<StatusType, Boolean>();
	
	public CharacterStatus (IWarlockClient client)
	{
		super(client, "characterStatus", null);
		
		for (StatusType type : StatusType.values())
		{
			status.put(type, false);
		}
	}
	
	@Override
	public void clear() {
		for (StatusType type : StatusType.values())
		{
			status.put(type, false);
		}
		
		for (IPropertyListener<String> listener : listeners) {
			listener.propertyCleared(this, null);
		}
	}
	
	public void set(String data) {
		StatusType statusType = StatusType.getStatusType(data);
		
		if (statusType != null)
		{
			status.put(statusType, true);
			for (IPropertyListener<String> listener : listeners)
			{
				listener.propertyChanged(this, null);
			}
		}
	}
	
	public void unset(String data) {
		StatusType statusType = StatusType.getStatusType(data);
		
		if (statusType != null)
		{
			status.put(statusType, false);
			for (IPropertyListener<String> listener : listeners)
			{
				listener.propertyChanged(this, null);
			}
		}
	}
	
	public Map<StatusType, Boolean> getStatus() {
		return status;
	}
}
