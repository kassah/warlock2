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
package cc.warlock.rcp.help;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class WikiCategory {

	public static final String HELP_CATEGORY_NAME = "WarlockHelp";
	
	public static final String MEDIAWIKI_ROOT = "http://www.warlock.cc/wiki";
	public static final String MEDIAWIKI_API = MEDIAWIKI_ROOT + "/api.php";
	
	protected static WikiCategory helpCategory;
	
	public static WikiCategory getHelpCategory ()
	{
		if (helpCategory == null)
		{
			helpCategory = new WikiCategory(HELP_CATEGORY_NAME);
		}
		return helpCategory;
	}
	
	protected ArrayList<WikiPage> pages = new ArrayList<WikiPage>();
	protected String name;
	
	public WikiCategory (String name)
	{
		this.name = name;
		
		loadPages();
	}
	
	
	public static List<WikiPage> search (String text)
	{
		ArrayList<WikiPage> pages = new ArrayList<WikiPage>();
		
		try {
			URL url = new URL(MEDIAWIKI_API + "?action=query&list=search&srsearch=" + text + "&format=xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(url);
			
			for (Element page : (List<Element>) document.selectNodes("//p"))
			{
				WikiPage wikiPage = new WikiPage(null, page.attributeValue("title"));
				
				pages.add(wikiPage);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pages;
	}
	
	protected void loadPages ()
	{
		try {
			URL url = new URL(MEDIAWIKI_API + "?action=query&list=categorymembers&cmcategory=" + name + "&format=xml");
			
			SAXReader reader = new SAXReader();
			Document document = reader.read(url);
			
			for (Element page : (List<Element>) document.selectNodes("//cm"))
			{
				WikiPage wikiPage = new WikiPage(page.attributeValue("pageid"), page.attributeValue("title"));
				
				pages.add(wikiPage);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isPage () {
		return false;
	}

	public ArrayList<WikiPage> getPages() {
		return pages;
	}

	public String getName() {
		return name;
	}
}
