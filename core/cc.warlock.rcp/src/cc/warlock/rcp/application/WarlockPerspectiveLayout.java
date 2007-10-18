package cc.warlock.rcp.application;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.internal.PerspectiveHelper;
import org.eclipse.ui.internal.WorkbenchPage;

import cc.warlock.core.configuration.IConfigurationProvider;

public class WarlockPerspectiveLayout implements IConfigurationProvider {

	protected XMLMemento savedLayout;
	protected static WarlockPerspectiveLayout _instance;
	
	public static WarlockPerspectiveLayout instance()
	{
		if (_instance == null) _instance = new WarlockPerspectiveLayout();
		return _instance;
	}
	
	protected WarlockPerspectiveLayout() { }
	
	public List<Element> getTopLevelElements() {
		try {
			StringWriter writer = new StringWriter();
			savedLayout.save(writer);
			
			StringBuffer buffer = writer.getBuffer();
			
			Document doc = DocumentHelper.parseText(buffer.toString());
			Element windowLayout = doc.getRootElement();
			
			windowLayout.detach();
			return Arrays.asList(new Element[] { windowLayout });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public void parseElement(Element element) {
		try {
			StringWriter writer = new StringWriter();
			element.write(writer);
			StringBuffer buffer = writer.getBuffer();
			
			savedLayout = XMLMemento.createReadRoot(new StringReader(buffer.toString()));
		} catch (WorkbenchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public boolean supportsElement(Element element) {
		if (element.getName().equals("window-layout"))
		{
			return true;
		}
		return false;
	}

	public void saveLayout ()
	{
		WorkbenchPage page = (WorkbenchPage) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		PerspectiveHelper presentation = page.getPerspectivePresentation();
		
		savedLayout = XMLMemento.createWriteRoot("window-layout");
		presentation.saveState(savedLayout);
	}
	
	public void loadSavedLayout ()
	{
		if (savedLayout != null)
		{
			WorkbenchPage page = (WorkbenchPage) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			PerspectiveHelper presentation = page.getPerspectivePresentation();
			
			presentation.restoreState(savedLayout);
		}
	}
}
