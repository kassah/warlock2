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
package cc.warlock.core.stormfront.settings.server;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.warlock.core.stormfront.xml.StormFrontElement;

@Deprecated
public class ServerScript extends ServerSetting {

	protected String name, comment, format;
	protected String scriptContents, tokens;
	
	public ServerScript (ServerSettings settings, StormFrontElement scriptElement)
	{
		super(settings, scriptElement);
		
		loadScript(scriptElement);
	}
	
	
	protected void loadScript (StormFrontElement scriptElement)
	{
		this.name = scriptElement.attributeValue("name");
		this.comment = scriptElement.attributeValue("comment");
		this.format = scriptElement.attributeValue("fmt");
		
		if ("tok".equals(format))
		{
			tokens = scriptElement.getText();
			setScriptContents(convertTokensToScript(tokens));
		}
		else if (format == null) {
			// just assume plain text (for importing)
			setScriptContents(scriptElement.getText());
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
	
	public static String convertScriptToTokens (String script)
	{
		return convertScriptToTokens(script, true);
	}
	
	public static String convertScriptToTokens (String script, boolean escape)
	{
		StringBuffer tokens = new StringBuffer();
		String lines[] = script.split("\\n", -1);
		boolean startNewSeparator = false;
		
		mainLoop:
		for (int i = 0; i < lines.length; i++)
		{
			String line = lines[i];
			
			if (startNewSeparator)
				tokens.append("\\.");
			
			if (escape)
			{
				line = line.replaceAll("'", "&apos;");
				line = line.replaceAll("\"", "&quot;");
			}
			
			for (Map.Entry<String,String> entry : shortCommands.entrySet())
			{
				String shortCommand = entry.getKey();
				String command = entry.getValue();
				
				if (command.indexOf('\n') > 0)
				{
					command = command.substring(0, command.length()-1);
					
					if (line.matches("^" + command + "$"))
					{
						tokens.append(shortCommand);
						startNewSeparator = false;
						continue mainLoop;
					}
				}
			}
			
			int putIndex = line.indexOf("put ");
			while (putIndex == 0)
			{
				tokens.append('L');
				line = line.substring(4);
				
				putIndex = line.indexOf("put ");
			}

			if (i < lines.length - 1)
				tokens.append("\\");
			tokens.append(line);
			startNewSeparator = true;
		}
		
		return tokens.toString();
	}
	
	public static String convertTokensToScript (String tokens)
	{
		if (tokens.startsWith("\\"))				
		{
			tokens = tokens.substring(1);
		}
		
		tokens = tokens.replaceAll("\\&apos\\;", "'");
		tokens = tokens.replaceAll("\\&quot\\;", "\"");

		StringBuffer buffer = new StringBuffer();
		String lines[] = tokens.split("\\\\\\.");
		for (int i = 0; i < lines.length; i++)
		{
			String line = lines[i];
			
			Pattern pattern = Pattern.compile("^([A-L]*)(\\\\|$)");
			Matcher matcher = pattern.matcher(line);
			
			while (matcher.find())
			{
				String replacement = "";
				if (matcher.groupCount() >= 1)
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
			
			if (i < lines.length - 1)
				buffer.append("\n");
		}
		if (tokens.lastIndexOf("\\.") == tokens.length() - 2)
		{
			buffer.append("\n");
		}
		
		return buffer.toString();
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

	public String getTokens() {
		return tokens;
	}
}
