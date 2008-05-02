/**
 * 
 */
package cc.warlock.rcp.menu;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.actions.CompoundContributionItem;

/**
 * @author simTel
 *
 */
public abstract class SubMenuContributionItem extends CompoundContributionItem {

	private String itemName;
	
	public SubMenuContributionItem() {
		this(null, null);
	}
	
	public SubMenuContributionItem(String name) {
		this(name, null);
	}
	
	public SubMenuContributionItem(String name, String id) {
		super(id);
		itemName = name;
	}

	public void setItemName(String newName) {
		itemName = newName;
	}
	
	@Override
	protected abstract IContributionItem[] getContributionItems();
	
	@Override
	public void fill(Menu menu, int index) {
		if(index == -1) {
			index = menu.getItemCount();
		}

		Menu menuToUse = menu;
		if(itemName != null) {
	        MenuItem parentItem = new MenuItem(menu, SWT.CASCADE, index);
	        parentItem.setText(itemName);
	        
	        menuToUse = new Menu(parentItem);
	        parentItem.setMenu(menuToUse);

	        super.fill(menuToUse, -1);
		}
	}
}
