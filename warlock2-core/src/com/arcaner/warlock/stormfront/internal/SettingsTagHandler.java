package com.arcaner.warlock.stormfront.internal;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.Attributes;

import com.arcaner.warlock.configuration.WarlockConfiguration;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class SettingsTagHandler extends DefaultTagHandler {

	private StringBuffer buffer = new StringBuffer();
	
	public SettingsTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getTagNames() {
		return new String[] {"settings"};
	}
	
	@Override
	public void handleStart(Attributes atts) {
		buffer.setLength(0);
		
		handler.startSavingRawXML(buffer, "settings");
	}

	@Override
	public void handleEnd() {
		handler.stopSavingRawXML();
		
		buffer.insert(0, "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<settings>\n");
		buffer.append("</settings>");
		
		String playerId = handler.getClient().getPlayerId();
		File serverSettings = WarlockConfiguration.getConfigurationFile("serverSettings_" + playerId + ".xml");
		
		try {

			FileOutputStream stream = new FileOutputStream(serverSettings);
//			TransformerFactory factory = TransformerFactory.newInstance();
//			Transformer transformer = factory.newTransformer();
//			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//			transformer.transform(new StreamSource(new ByteArrayInputStream(buffer.toString().getBytes())), new StreamResult(stream));
			stream.write(buffer.toString().getBytes());
			stream.close();
			buffer = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
