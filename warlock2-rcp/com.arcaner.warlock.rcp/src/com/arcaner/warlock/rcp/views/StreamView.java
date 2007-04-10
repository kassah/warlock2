package com.arcaner.warlock.rcp.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.client.stormfront.IStormFrontStyle;
import com.arcaner.warlock.client.stormfront.WarlockColor;
import com.arcaner.warlock.configuration.ServerSettings;
import com.arcaner.warlock.rcp.ui.client.SWTStreamListener;
import com.arcaner.warlock.rcp.ui.style.StyleMappings;
import com.arcaner.warlock.stormfront.IStream;
import com.arcaner.warlock.stormfront.IStreamListener;

public class StreamView extends ViewPart implements IStreamListener{
	public static final String VIEW_ID = "com.arcaner.warlock.rcp.views.StreamView";
	private static ArrayList<StreamView> openViews = new ArrayList<StreamView>();
	
	private IStream stream;
	private IStormFrontClient client;
	private StyledText text;
	
	public StreamView() {
		openViews.add(this);
	}

	public static StreamView createNext (String streamName) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			StreamView nextInstance = (StreamView) page.showView(VIEW_ID, streamName, IWorkbenchPage.VIEW_ACTIVATE);
			return nextInstance;
		} catch (PartInitException e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		Composite top = new Composite (parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		top.setLayout(layout);
		
		text = new StyledText(top, SWT.V_SCROLL);
		text.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		text.setEditable(false);
		text.setWordWrap(true);
		text.setBackground(new Color(text.getDisplay(), 25, 25, 50));
		text.setForeground(new Color(text.getDisplay(), 240, 240, 255));
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public IStream getStream() {
		return stream;
	}

	public void setStream(IStream stream) {
		this.stream = stream;
		
		setPartName(stream.getTitle());
		stream.addStreamListener(new SWTStreamListener(this));
	}
	
	public void streamCleared() {
		text.setText("");
	}
	
	public void streamReceivedText(String text, IStormFrontStyle style) {
		text = text + "\n";
		
		StyleRange range = StyleMappings.getStyle(client.getServerSettings(), style, this.text.getCharCount(), text.length());
		
		this.text.append(text);
		if (range != null)
			this.text.setStyleRange(range);
	}

	public static Collection<StreamView> getOpenViews ()
	{
		return openViews;
	}
	
	private Color createColor (WarlockColor color)
	{
		return new Color(getSite().getShell().getDisplay(), color.getRed(), color.getGreen(), color.getBlue());
	}
	
	public void setClient (IStormFrontClient client)
	{
		this.client = client;
	}
	
	public void loadServerSettings (ServerSettings settings)
	{
		// just inherit from the main window for now
		WarlockColor bg = settings.getColorSetting(ServerSettings.ColorType.MainWindow_Background);
		WarlockColor fg = settings.getColorSetting(ServerSettings.ColorType.MainWindow_Foreground);
		String fontFace = settings.getStringSetting(ServerSettings.StringType.MainWindow_FontFace);
		int fontSize = settings.getIntSetting(ServerSettings.IntType.MainWindow_FontSize);
		
		text.setBackground(createColor(bg));
		text.setForeground(createColor(fg));
		
		Font normalFont = new Font(getSite().getShell().getDisplay(), fontFace, fontSize-2, SWT.NONE);
		text.setFont(normalFont);
	}
}
