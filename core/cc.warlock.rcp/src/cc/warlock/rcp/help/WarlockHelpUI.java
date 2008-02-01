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
package cc.warlock.rcp.help;

import org.eclipse.help.IContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.AbstractHelpUI;

import cc.warlock.rcp.ui.WarlockSharedImages;

public class WarlockHelpUI extends AbstractHelpUI {
	@Override
	public void displayHelp() {
		Shell parent = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		Shell shell = new Shell(parent, SWT.RESIZE);
		GridLayout layout = new GridLayout(1, false);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;	
		shell.setLayout(layout);
		shell.setText("Warlock Online Help");
		
		createContents(shell);

		Rectangle bounds = parent.getBounds();
		
		shell.setBounds(bounds.x + 25, bounds.y + 25, bounds.width - 50, bounds.height - 50);
		shell.open();
	}
	
	protected void createContents (Shell parent)
	{
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout(1, false));
		topComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Composite locationComposite = new Composite(topComposite, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = layout.marginWidth = 0;
		locationComposite.setLayout(layout);
		locationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		new Label(locationComposite, SWT.NONE).setImage(
			WarlockSharedImages.getImage(WarlockSharedImages.IMG_HELP_INDEX));
		new Label(locationComposite, SWT.NONE).setText(
			"Warlock Online Help > Help Index");
		
//		Composite searchComposite = new Composite(topComposite, SWT.NONE);
//		layout = new GridLayout(2, false);
//		layout.marginHeight = layout.marginWidth = 0;
//		
//		searchComposite.setLayout(layout);
//		searchComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//		
//		Text searchText = new Text(searchComposite, SWT.BORDER);
//		searchText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//		
//		Button search = new Button(searchComposite, SWT.PUSH);
//		search.setText("Search");
//		search.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_HELP_SEARCH));
//		search.addSelectionListener(new SelectionAdapter () {
//			public void widgetSelected(SelectionEvent e) {
//				
//			}
//		});
		
		createHelpBrowser(parent);
	}
	
	protected Tree categories;
	protected Browser browser;
	
	protected void createHelpBrowser (Composite parent)
	{
		Composite helpBrowserComposite = new Composite(parent, SWT.NONE);
		helpBrowserComposite.setLayout(new FormLayout());
		helpBrowserComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Sash sash = new Sash(helpBrowserComposite, SWT.VERTICAL);
		FormData data = new FormData();
	    data.top = new FormAttachment(0, 0);
	    data.bottom = new FormAttachment(100, 0);
	    data.left = new FormAttachment(35, 0);
	    sash.setLayoutData(data);
	    
	    categories = new Tree(helpBrowserComposite, SWT.BORDER | SWT.SINGLE);
	    data = new FormData();
	    data.top = new FormAttachment(0, 0);
	    data.bottom = new FormAttachment(100, 0);
	    data.left = new FormAttachment(0, 0);
	    data.right = new FormAttachment(sash, 0);
	    categories.setLayoutData(data);
	    
	    Composite browserComposite = new Composite(helpBrowserComposite, SWT.NONE);
	    data = new FormData();
	    data.top = new FormAttachment(0, 0);
	    data.bottom = new FormAttachment(100, 0);
	    data.left = new FormAttachment(sash, 0);
	    data.right = new FormAttachment(100, 0);
	    browserComposite.setLayoutData(data);
	    browserComposite.setLayout(new GridLayout(1, false));
	    
	    Composite browserControls = new Composite(browserComposite, SWT.NONE);
	    browserControls.setLayout(new GridLayout(5, false));
	    browserControls.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	    
	    final Button back = new Button(browserControls, SWT.PUSH);
	    back.setText("Back");
	    back.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_BACK));
	    back.addSelectionListener(new SelectionAdapter () {
	    	public void widgetSelected(SelectionEvent e) {
	    		browser.back();
	    	}
	    });
	    
	    final Button forward = new Button(browserControls, SWT.PUSH | SWT.RIGHT_TO_LEFT);
	    forward.setText("Forward");
	    forward.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_NEXT));
	    forward.addSelectionListener(new SelectionAdapter () {
	    	public void widgetSelected(SelectionEvent e) {
	    		browser.forward();
	    	}
	    });
	    
	    final Button stop = new Button(browserControls, SWT.PUSH);
	    stop.setText("Stop");
	    stop.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_STOP));
	    stop.addSelectionListener(new SelectionAdapter () {
	    	public void widgetSelected(SelectionEvent e) {
	    		browser.stop();
	    	}
	    });
	    
	    final Button refresh = new Button(browserControls, SWT.PUSH);
	    refresh.setText("Refresh");
	    refresh.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_REFRESH));
	    refresh.addSelectionListener(new SelectionAdapter () {
	    	public void widgetSelected(SelectionEvent e) {
	    		browser.refresh();
	    	}
	    });
	    
	    final ProgressBar progress = new ProgressBar(browserControls, SWT.NONE);
	    progress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	    
		browser = new Browser(browserComposite, SWT.BORDER);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		browser.addProgressListener(new ProgressListener () {
			public void changed(ProgressEvent event) {
				progress.setMaximum(event.total);
		        progress.setSelection(event.current);
			}
			public void completed(ProgressEvent event) {
				progress.setSelection(0);
			}
		});
	    
		browser.addLocationListener(new LocationListener () {
			public void changing(LocationEvent event) {
				stop.setEnabled(true);
			}
			
			public void changed(LocationEvent event) {
				back.setEnabled(browser.isBackEnabled());
				forward.setEnabled(browser.isForwardEnabled());
				stop.setEnabled(false);
			}
		});
		
	    loadContent();
	}
	
	protected void loadContent ()
	{
		WikiCategory helpCategory = WikiCategory.getHelpCategory();
		
		for (WikiPage page : helpCategory.getPages())
		{
			TreeItem item = new TreeItem(categories, SWT.NONE);
			item.setText(page.getTitle());
			item.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_CATEGORY));
			item.setData(page);
		}
		
		categories.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				WikiPage page = (WikiPage) categories.getSelection()[0].getData();
				
				browser.setUrl(page.getURL());
			}
		});
	}

	@Override
	public void displayHelpResource(String href) {
		System.out.println("display help resource: " + href);
	}

	@Override
	public boolean isContextHelpDisplayed() {
		System.out.println("is context help displayed?");
		return false;
	}
	
	@Override
	public void displayContext(IContext context, int x, int y) {
		System.out.println("display context: " + context + ", x:" + x +", y:" + y);
	}

}
