package cc.warlock.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class FeatureVersionReplacer extends Task {

	protected String buildDirectory;
	protected String version;
	protected String feature;
	
	@Override
	public void execute() throws BuildException {
		if (buildDirectory == null)
		{
			buildDirectory = getProject().getProperty("buildDirectory");
			if (buildDirectory == null)
			{
				throw new BuildException("buildDirectory can't be null");
			}
		}
		
		if (version == null)
		{
			throw new BuildException("version can't be null");
		}
		if (feature == null)
		{
			throw new BuildException("feature can't be null");
		}
		
		replaceVersions();
	}
	
	protected void replaceVersions () 
		throws BuildException
	{
		 SAXReader reader = new SAXReader();
		 
		 try {
			File featureFile = new File(new File(new File(buildDirectory, "features"), feature), "feature.xml");
			 Document featureDoc = reader.read(featureFile);
			 Element featureEl = featureDoc.getRootElement();
			 featureEl.attribute("version").setValue(version);
			 
//			 Properties versions = new Properties();
//			 File versionsFile = new File(buildDirectory, "finalPluginsVersions.properties");
//			 FileInputStream stream = new FileInputStream(versionsFile);
//			 versions.load(stream);
//			 stream.close();
//			 
//			 for (Element pluginEl : (List<Element>) featureEl.elements("plugin"))
//			 {
//				 if (pluginEl.attributeValue("version").equals("0.0.0"))
//				 {
//					 if (versions.containsKey(pluginEl.attributeValue("id")))
//						 pluginEl.attribute("version").setValue(versions.getProperty(pluginEl.attributeValue("id")));
//				 }
//			 }
			 
			 OutputFormat format = OutputFormat.createPrettyPrint();
			 
			 FileOutputStream outStream = new FileOutputStream(featureFile);
			 XMLWriter writer = new XMLWriter(outStream, format);
			 writer.write(featureDoc);
			 outStream.close();
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getBuildDirectory() {
		return buildDirectory;
	}
	public void setBuildDirectory(String buildDirectory) {
		this.buildDirectory = buildDirectory;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}
	
	
}
