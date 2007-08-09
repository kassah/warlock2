package cc.warlock.rcp.util;

import java.util.ArrayList;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FontDialog;

public class FontSelector {

	private Button button;
	private FontData fontData;
	private Font font;
	private ArrayList<IPropertyChangeListener> listeners;
	
	public FontSelector (Composite parent)
	{
		listeners = new ArrayList<IPropertyChangeListener>();
		button = new Button(parent, SWT.PUSH);
		button.setText("Choose...");
		button.addSelectionListener(new SelectionListener () {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				FontDialog dialog = new FontDialog(button.getShell());
				if (fontData != null)
					dialog.setFontList(new FontData[] {fontData} );
				
				FontData oldData = fontData;
				fontData = dialog.open();
				
				if (fontData != null) {
					font = new Font(button.getDisplay(), fontData);
					
					for (IPropertyChangeListener listener : listeners)
						listener.propertyChange(new PropertyChangeEvent(e.getSource(), "fontData", oldData, fontData));
				}
			}
		});
	}

	public FontData getFontData() {
		return fontData;
	}

	public void setFontData(FontData fontData) {
		this.fontData = fontData;
	}
	
	public Font getFont()
	{
		return font;
	}
	
	public void addListener (IPropertyChangeListener listener)
	{
		listeners.add(listener);
	}
}
