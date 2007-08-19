/**
 * 
 */
package cc.warlock.rcp.views;

import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;

/**
 *
 * Creates a Composite View to put further ViewParts in.
 * TODO: Make Listeners to pass  appropriate calls onto the Composite
 *
 * @author kassah
 */
public class CompositeView extends ViewPart {

	protected Composite container;

	public CompositeView() {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
	}
	
	@Override
	public void setFocus() {
		container.setFocus();
	}
	
	
	/**
	 * Gets the Composite container
	 * @return container
	 */
	public Composite getComposite() {
		return container;
	}
}
