package cc.warlock.script.wsl;

import java.util.List;

public class WSLList extends WSLString {

	private List<IWSLValue> list;
	
	public WSLList(List<IWSLValue> list) {
		this.list = list;
	}
	
	public String getString() {
		StringBuffer buffer = new StringBuffer();
		
		for(IWSLValue value : list) {
			buffer.append(value.getString());
		}
		
		return buffer.toString();
	}
}
