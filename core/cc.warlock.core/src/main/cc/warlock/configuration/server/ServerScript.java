package cc.warlock.configuration.server;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Element;

public class ServerScript extends ServerSetting {

	protected String name, comment, format;
	protected String scriptContents;
	
	public ServerScript (ServerSettings settings, Element scriptElement)
	{
		super(settings);
		
		loadScript(scriptElement);
	}
	
	
	protected void loadScript (Element scriptElement)
	{
		this.name = scriptElement.attributeValue("name");
		this.comment = scriptElement.attributeValue("comment");
		this.format = scriptElement.attributeValue("fmt");
		
		if ("tok".equals(format))
		{
			convertTokScript(scriptElement.getTextTrim());
		}
	}
	
	protected static final HashMap<String,String> shortCommands = new HashMap<String,String>();
	static {
		shortCommands.put("A", "move n\n");
		shortCommands.put("B", "move ne\n");
		shortCommands.put("C", "move e\n");
		shortCommands.put("D", "move se\n");
		shortCommands.put("E", "move s\n");
		shortCommands.put("F", "move sw\n");
		shortCommands.put("G", "move w\n");
		shortCommands.put("H", "move nw\n");
		shortCommands.put("I", "move out\n");
		shortCommands.put("J", "move up\n");
		shortCommands.put("K", "move down\n");
		shortCommands.put("L", "put ");
	}
	
	protected void convertTokScript(String tokScript)
	{
		if (tokScript.startsWith("\\"))				
		{
			tokScript = tokScript.substring(1);
		}
		
		tokScript = tokScript.replaceAll("\\\\\\.", "\n");
		tokScript = tokScript.replaceAll("\\&apos\\;", "'");
		tokScript = tokScript.replaceAll("\\&quot\\;", "\"");
		
		Pattern pattern = Pattern.compile("([A-L]*)\\\\");
		
		Matcher matcher = pattern.matcher(tokScript);
		StringBuffer buffer = new StringBuffer();
		
		while (matcher.find())
		{
			String replacement = "";
			if (matcher.groupCount() == 1)
			{
				String commands = matcher.group(1);
				for (char command : commands.toCharArray())
				{
					replacement += shortCommands.get("" + command);
				}
			}
			
			matcher.appendReplacement(buffer, replacement);
		}
		matcher.appendTail(buffer);
		scriptContents = buffer.toString();
	}
	
	@Override
	protected void saveToDOM() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String toStormfrontMarkup() {
		// TODO Auto-generated method stub
		return null;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getFormat() {
		return format;
	}


	public void setFormat(String format) {
		this.format = format;
	}


	public String getScriptContents() {
		return scriptContents;
	}


	public void setScriptContents(String scriptContents) {
		this.scriptContents = scriptContents;
	}

}
