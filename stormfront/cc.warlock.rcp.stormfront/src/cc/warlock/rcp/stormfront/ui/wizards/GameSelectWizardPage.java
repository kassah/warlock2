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
/*
 * Created on Dec 31, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.stormfront.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import cc.warlock.core.stormfront.network.ISGEGame;
import cc.warlock.core.stormfront.network.SGEConnection;
import cc.warlock.core.stormfront.network.SGEConnectionListener;
import cc.warlock.rcp.stormfront.adapters.SWTSGEConnectionListenerAdapter;
import cc.warlock.rcp.stormfront.ui.StormFrontSharedImages;
import cc.warlock.rcp.ui.WarlockSharedImages;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GameSelectWizardPage extends WizardPage {

	private Table games;
	private List<? extends ISGEGame> gameList;
	private SGEConnection connection;
	private TableViewer gamesViewer;
	private ISGEGame selectedGame;
	private Listener listener;
	
//	private static String[] gameFilterCodes = new String[] {
//		"CS", "DRDT", "GS4D", "HXD", "MOD"
//	};
	
	public GameSelectWizardPage (SGEConnection connection)
	{
		super ("Select a game.", "Please select a game.", WarlockSharedImages.getImageDescriptor(WarlockSharedImages.IMG_WIZBAN_WARLOCK));
		this.connection = connection;
		
		listener = new Listener();
		connection.addSGEConnectionListener(new SWTSGEConnectionListenerAdapter(listener));
		
		System.out.println("GameSelectWizardPage constructor");
	}
	
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub

		Composite controls = new Composite(parent, SWT.NONE);
		FillLayout layout = new FillLayout();
		controls.setLayout(layout);
		
		games = new Table(controls, SWT.BORDER | SWT.SINGLE);
		games.setEnabled(false);
		
		gamesViewer = new TableViewer (games);
		gamesViewer.setContentProvider(new ArrayContentProvider());
		gamesViewer.setLabelProvider(new GameLabelProvider());
		gamesViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				gameSelected(event.getSelection());
			}
		});
		gamesViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				gameSelected(gamesViewer.getSelection());
				
				IWizardPage nextPage = getWizard().getNextPage(GameSelectWizardPage.this);

				nextPage.setVisible(true);
				setVisible(false);
			}
		});
//		gamesViewer.addFilter(new ViewerFilter() {
//			public boolean select(Viewer viewer, Object parentElement, Object element) {
//				ISGEGame game = (ISGEGame) element;
//				
//				if (game.getAccountStatus() == ISGEGame.AccountStatus.Normal
//					|| game.getAccountStatus() == ISGEGame.AccountStatus.Trial)
//				{
//					return true;
//				}
//				return false;
//			}
//		});
		
		setControl(controls);
	}
	
	private class GameLabelProvider extends LabelProvider
	{
		public Image getImage(Object element) {
			return StormFrontSharedImages.getImage(StormFrontSharedImages.IMG_GAME);
		}
		
		public String getText(Object element) {
			ISGEGame game = (ISGEGame) element;
			
			if (game.getAccountStatus() == ISGEGame.AccountStatus.Trial)
			{
				return game.getGameName() + " (Trial: " + game.getAccountStatusInterval() + " days)";
			}
			return game.getGameName();
		}
	}
	
	private void gameSelected (ISelection selection)
	{
		if (selection instanceof IStructuredSelection)
		{
			IStructuredSelection selection2 = (IStructuredSelection) selection;
			selectedGame = (ISGEGame) selection2.getFirstElement();
		}
	}
	
	public void setVisible (boolean visible) {
		super.setVisible(visible);
		
		if (!visible && gameList != null && !gameList.isEmpty()) {
			try {
				getContainer().run(true, true, new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException
					{
						listener.setProgressMonitor(monitor);
						
						monitor.beginTask("Finding characters in \"" + selectedGame.getGameName() + "\"...", 2);
						connection.selectGame(selectedGame.getGameCode());
						monitor.worked(1);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		setPageComplete(true);
	}
	
	private class Listener extends SGEConnectionListener {
		private IProgressMonitor monitor;
		
		public void setProgressMonitor (IProgressMonitor monitor)
		{
			this.monitor = monitor;
		}
		
		public void gamesReady(SGEConnection connection, List<? extends ISGEGame> games) {
			GameSelectWizardPage.this.gameList = games;
			
			GameSelectWizardPage.this.games.setEnabled(true);
			gamesViewer.setInput(gameList);
		}

		public void charactersReady(SGEConnection connection, Map<String, String> characters) {
			if (monitor != null)
			{
				monitor.worked(1);
				monitor.done();
			}
		}
	}

	public String getSelectedGameCode() {
		return selectedGame.getGameCode();
	}
	
	public String getSelectedGameName() {
		return selectedGame.getGameName();
	}
	
}
