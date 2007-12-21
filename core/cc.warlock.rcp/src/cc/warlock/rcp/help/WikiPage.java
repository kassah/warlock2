package cc.warlock.rcp.help;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

public class WikiPage extends WikiCategory {

	protected String id;
	protected String title;
	protected StringBuffer htmlContents;
	
	public WikiPage (String id, String title)
	{
		super(title);
		this.id = id;
		this.title = title;
	}
	
	protected void loadContents ()
	{
		htmlContents = new StringBuffer();
		
		try {
			URL url = new URL(getURL());
			InputStream stream = url.openStream();
			Reader reader = new InputStreamReader(stream);
			
			char[] buffer = new char[1024];
			while (true)
			{
				int read = reader.read(buffer);
				if (read > 0)
				{
					htmlContents.append(buffer, 0, read);
				}
				if (read == -1) break;
			}
			stream.close();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean isPage() {
		return true;
	}
	
	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}
	
	public String getURL ()
	{
		String urlTitle = new String(title);
		urlTitle = urlTitle.replace(' ', '_');
		
		return MEDIAWIKI_ROOT + "/index.php/" + urlTitle + "?useskin=htmldump";
	}

	public StringBuffer getHtmlContents() {
		if (htmlContents == null)
		{
			loadContents();
		}
		
		return htmlContents;
	}
}
