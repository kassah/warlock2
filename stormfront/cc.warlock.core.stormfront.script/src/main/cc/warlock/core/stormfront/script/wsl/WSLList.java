package cc.warlock.core.stormfront.script.wsl;

import java.util.List;

public class WSLList extends WSLAbstractString {

	private List<IWSLValue> list;
	
	public WSLList(List<IWSLValue> list) {
		this.list = list;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		for(IWSLValue value : list) {
			buffer.append(value.toString());
		}
		
		return buffer.toString();
	}
}
