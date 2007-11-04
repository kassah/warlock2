/**
 * 
 */
package cc.warlock.rcp.userstreams.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.warlock.core.client.WarlockString;
import cc.warlock.rcp.userstreams.IStreamFilter;

/**
 * @author Will Robertson
 * Implementation of IStreamFilter
 */
public class StreamFilter implements IStreamFilter {
	private String content = "";
	private Pattern regexCompile;
	private type filterType = type.string;
	
	public StreamFilter() {
		// Do nothing, this is our default state.
	}
	
	public StreamFilter(String content, type filterType) {
		// Doing construction here allows us to do it in the most efficient order
		setContent(content);
		setType(filterType);
	}
	
	/* (non-Javadoc)
	 * @see cc.warlock.rcp.userstreams.IStreamFilter#getContent()
	 */
	public String getContent() {
		return this.content;
	}

	/* (non-Javadoc)
	 * @see cc.warlock.rcp.userstreams.IStreamFilter#getType()
	 */
	public type getType() {
		return filterType;
	}

	/* (non-Javadoc)
	 * @see cc.warlock.rcp.userstreams.IStreamFilter#match(java.lang.String)
	 */
	public boolean match(String text) {
		if (this.filterType == type.regex) {
			Matcher m = regexCompile.matcher(text);
			if(m.find()) {
				return true;
			} else {
				return false;
			}
		} else {
			return text.contains(this.content);
		}
	}
	
	public boolean match(WarlockString text) {
		return match(text.toString());
	}

	/* (non-Javadoc)
	 * @see cc.warlock.rcp.userstreams.IStreamFilter#setContent(java.lang.String)
	 */
	public void setContent(String content) {
		// TODO Auto-generated method stub
		this.content = content;
		// Compile our regex if our type is regex
		if (this.filterType == type.regex) {
			this.regexCompile = Pattern.compile(this.content);
		}
	}

	/* (non-Javadoc)
	 * @see cc.warlock.rcp.userstreams.IStreamFilter#setType(cc.warlock.rcp.userstreams.IStreamFilter.type)
	 */
	public void setType(type filterType) {
		// Test for change, so we don't have to recompile if we don't have to.
		if (this.filterType != filterType) {
			if (filterType == type.regex) {
				this.regexCompile = Pattern.compile(this.content);
			}
			this.filterType = filterType;
		}
	}

}
