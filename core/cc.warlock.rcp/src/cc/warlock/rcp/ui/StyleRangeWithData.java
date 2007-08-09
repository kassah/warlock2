/**
 * 
 */
package cc.warlock.rcp.ui;

import java.util.Hashtable;

import org.eclipse.swt.custom.StyleRange;

/**
 * A custom style range extension that allows for arbitrary style hints/properties
 * 
 * @author marshall
 */
public class StyleRangeWithData extends StyleRange
{
	public Hashtable<String, String> data = new Hashtable<String, String>();
}