package cc.warlock.rcp.prefs;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.PropertyPage;

import cc.warlock.rcp.util.FontSelector;

public abstract class PreferencePageUtils extends PropertyPage implements SelectionListener, IPropertyChangeListener {
	
	public Button createButton(Composite parent, int flags)
	{
		Button button = new Button(parent, flags);
		button.addSelectionListener(this);
		
		return button;
	}
	
	public Button createButton(Composite parent, String text, int flags)
	{
		Button button = createButton(parent, flags);
		button.setText(text);
		
		return button;
	}
	
	public Button createButton(Composite parent)
	{
		return createButton(parent, SWT.PUSH);
	}
	
	public Button createButton(Composite parent, String text)
	{
		return createButton(parent, text, SWT.PUSH);
	}
	
	public Button createCheckbox(Composite parent)
	{
		return createButton(parent, SWT.CHECK);
	}
	
	public Button createCheckbox(Composite parent, String text)
	{
		return createButton(parent, text, SWT.CHECK);
	}
	
	public Button createRadio(Composite parent)
	{
		return createButton(parent, SWT.RADIO);
	}
	
	public Button createRadio(Composite parent, String text)
	{
		return createButton(parent, text, SWT.RADIO);
	}

	public ColorSelector createColorSelector (Composite parent)
	{
		ColorSelector selector = new ColorSelector(parent);
		selector.addListener(this);
		
		return selector;
	}
	
	public FontSelector createFontSelector (Composite parent)
	{
		FontSelector selector = new FontSelector(parent);
		selector.addListener(this);
		
		return selector;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() instanceof Button)
		{
			buttonPressed((Button)e.getSource());
		}
	}
	
	protected void buttonPressed(Button button) {
		
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() instanceof ColorSelector)
		{
			colorSelectorChanged((ColorSelector)event.getSource());
		}
		else if (event.getSource() instanceof FontSelector)
		{
			fontSelectorChanged((FontSelector)event.getSource());
		}
	}
	
	protected void colorSelectorChanged(ColorSelector selector) {
		
	}
	
	protected void fontSelectorChanged(FontSelector selector) {
		
	}
}
