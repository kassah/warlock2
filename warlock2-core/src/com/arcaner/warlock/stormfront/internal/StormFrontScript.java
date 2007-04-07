package com.arcaner.warlock.stormfront.internal;

import com.arcaner.warlock.stormfront.IStormFrontScript;

public class StormFrontScript implements IStormFrontScript {

	private String comment, name, contents;
	
	public StormFrontScript () { }
	
	public StormFrontScript(String comment, String name, String contents)
	{
		this.comment = comment;
		this.name =  name;
		this.contents = contents;
	}
	 
	public String getComment() {
		return comment;
	}

	public String getName() {
		return name;
	}

	public String getContents() {
		return contents;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
}
