package cc.warlock.core.client;

import java.util.Map;

/**
 * This interface represents the current status of the current character
 * @author marshall
 *
 */
public interface ICharacterStatus extends IProperty<String> {

	public static enum StatusType {
		Kneeling, Prone, Sitting, Standing, Stunned, Bleeding, Hidden, Invisible, Dead, Webbed, Joined;
		
		public String toIconString() {
			return "Icon" + name().toUpperCase();
		}
		
		public static StatusType getStatusType (String name)
		{
			for (StatusType type : StatusType.values())
			{
				if (type.toIconString().equals(name))
					return type;
			}
			return null;
		}
	};
	
	public Map<StatusType, Boolean> getStatus();
	public void unset(String status);
}
