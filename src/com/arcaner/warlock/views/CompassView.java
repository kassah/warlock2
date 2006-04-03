/*
 * Created on Jan 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

import com.arcaner.warlock.client.ICompass;
import com.arcaner.warlock.client.IPropertyListener;
import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.internal.Compass;
import com.arcaner.warlock.client.stormfront.IStormFrontClientListener;
import com.arcaner.warlock.client.stormfront.internal.StormFrontClient;
import com.arcaner.warlock.ui.WarlockSharedImages;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CompassView extends ViewPart implements IPropertyListener {

	public static final String VIEW_ID = "com.arcaner.warlock.views.compassView";
	
	private static CompassView instance;	
	
	private IWarlockClient client;
	private ICompass compass;
	private Label compassLabels[];
	private String onIds[];
	private String offIds[];
	private static String commands[];
	
	private CompassAction runAction, powerWalkAction, peerAction;
	private WalkingAction walkingAction;
	
	private Button stopMoving;
	
	public static CompassView getDefault ()
	{
		return instance;
	}
	
	public CompassView (IWarlockClient client) {

		
		this.client = client;
		instance = this;
		
		compassLabels = new Label[ICompass.N_DIRECTIONS];
		onIds = new String[ICompass.N_DIRECTIONS];
		offIds = new String[ICompass.N_DIRECTIONS];
		commands = new String[ICompass.N_DIRECTIONS];
		
		offIds[ICompass.NORTHWEST] = WarlockSharedImages.IMG_COMPASS_NORTHWEST_OFF;
		offIds[ICompass.NORTH] = WarlockSharedImages.IMG_COMPASS_NORTH_OFF;
		offIds[ICompass.NORTHEAST] = WarlockSharedImages.IMG_COMPASS_NORTHEAST_OFF;
		offIds[ICompass.UP] = WarlockSharedImages.IMG_COMPASS_UP_OFF;
		offIds[ICompass.WEST] = WarlockSharedImages.IMG_COMPASS_WEST_OFF;
		offIds[ICompass.OUT] = WarlockSharedImages.IMG_COMPASS_OUT_OFF;
		offIds[ICompass.EAST] = WarlockSharedImages.IMG_COMPASS_EAST_OFF;
		offIds[ICompass.SOUTHEAST] = WarlockSharedImages.IMG_COMPASS_SOUTHEAST_OFF;
		offIds[ICompass.SOUTH] = WarlockSharedImages.IMG_COMPASS_SOUTH_OFF;
		offIds[ICompass.SOUTHWEST] = WarlockSharedImages.IMG_COMPASS_SOUTHWEST_OFF;
		offIds[ICompass.DOWN] = WarlockSharedImages.IMG_COMPASS_DOWN_OFF;
		
		onIds[ICompass.NORTHWEST] = WarlockSharedImages.IMG_COMPASS_NORTHWEST_ON;
		onIds[ICompass.NORTH] = WarlockSharedImages.IMG_COMPASS_NORTH_ON;
		onIds[ICompass.NORTHEAST] = WarlockSharedImages.IMG_COMPASS_NORTHEAST_ON;
		onIds[ICompass.UP] = WarlockSharedImages.IMG_COMPASS_UP_ON;
		onIds[ICompass.WEST] = WarlockSharedImages.IMG_COMPASS_WEST_ON;
		onIds[ICompass.OUT] = WarlockSharedImages.IMG_COMPASS_OUT_ON;
		onIds[ICompass.EAST] = WarlockSharedImages.IMG_COMPASS_EAST_ON;
		onIds[ICompass.SOUTHEAST] = WarlockSharedImages.IMG_COMPASS_SOUTHEAST_ON;
		onIds[ICompass.SOUTH] = WarlockSharedImages.IMG_COMPASS_SOUTH_ON;
		onIds[ICompass.SOUTHWEST] = WarlockSharedImages.IMG_COMPASS_SOUTHWEST_ON;
		onIds[ICompass.DOWN] = WarlockSharedImages.IMG_COMPASS_DOWN_ON;
		
		commands[ICompass.NORTHWEST] = "northwest";
		commands[ICompass.NORTH] = "north";
		commands[ICompass.NORTHEAST] = "northeast";
		commands[ICompass.UP] = "up";
		commands[ICompass.WEST] = "west";
		commands[ICompass.OUT] = "out";
		commands[ICompass.EAST] = "east";
		commands[ICompass.SOUTHEAST] = "southeast";
		commands[ICompass.SOUTH] = "south";
		commands[ICompass.SOUTHWEST] = "southwest";
		commands[ICompass.DOWN] = "down";
		
	}
	
	public void propertyActivated()
	{
		Display.getDefault().asyncExec(new Runnable() {
			public void run () {
				// TODO this can be made more efficient by remembering the state of the compass
				try {
					clearCompass();
				} catch( Exception e ) {
					System.out.println("HERE HERE HERE");
				}
				
				boolean[] directions = compass.getDirections();
				for(int i = 0; i < directions.length; i++) {
					compassLabels[i].setImage(WarlockSharedImages.getImage(
							directions[i] ? onIds[i] : offIds[i]));
				}
			}
		});
	}
	
	public void propertyCleared() {
		clearCompass();
	}
	
	public void propertyChanged() {
		// not used
	}
	
	private void clearCompass() {
		compassLabels[ICompass.NORTHWEST].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_COMPASS_NORTHWEST_OFF));
		compassLabels[ICompass.NORTH].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_COMPASS_NORTH_OFF));
		compassLabels[ICompass.NORTHEAST].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_COMPASS_NORTHEAST_OFF));
		compassLabels[ICompass.UP].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_COMPASS_UP_OFF));
		compassLabels[ICompass.WEST].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_COMPASS_WEST_OFF));
		compassLabels[ICompass.OUT].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_COMPASS_OUT_OFF));
		compassLabels[ICompass.EAST].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_COMPASS_EAST_OFF));
		compassLabels[ICompass.SOUTHEAST].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_COMPASS_SOUTHEAST_OFF));
		compassLabels[ICompass.SOUTH].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_COMPASS_SOUTH_OFF));
		compassLabels[ICompass.SOUTHWEST].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_COMPASS_SOUTHWEST_OFF));
		compassLabels[ICompass.DOWN].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_COMPASS_DOWN_OFF));
		System.out.println("****************\nCLEARED\n**************");
	}
	
	/*
	public void updateDirection (final int direction)
	{
		Display.getDefault().asyncExec(new Runnable() {
			public void run () {
				String imageName = compass.isDirectionEnabled(direction) ? onIds[direction] : offIds[direction];
				compassLabels[direction].setImage( 	WarlockSharedImages.getImage( imageName	));
			}
		});
	}*/
	
	public void createPartControl(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new FormLayout());
		
		Composite compassComp = new Composite(main, SWT.NONE);
		FormData compassCompData = new FormData();
		compassCompData.left = new FormAttachment(0,0);
		compassCompData.top = new FormAttachment(0,0);
		compassComp.setLayoutData(compassCompData);
		
		compassComp.setLayout(new GridLayout(4, false));
		
		compassLabels[ICompass.NORTHWEST] = new Label(compassComp, SWT.NONE);
		compassLabels[ICompass.NORTH] = new Label(compassComp, SWT.NONE);
		compassLabels[ICompass.NORTHEAST] = new Label(compassComp, SWT.NONE);
		compassLabels[ICompass.UP] = new Label(compassComp, SWT.NONE);
		
		compassLabels[ICompass.WEST] = new Label(compassComp, SWT.NONE);
		compassLabels[ICompass.OUT] = new Label(compassComp, SWT.NONE);
		compassLabels[ICompass.EAST] = new Label(compassComp, SWT.NONE);
		new Label(main, SWT.NONE);

		compassLabels[ICompass.DOWN] = new Label(compassComp, SWT.NONE);
		compassLabels[ICompass.SOUTHWEST] = new Label(compassComp, SWT.NONE);
		compassLabels[ICompass.SOUTH] = new Label(compassComp, SWT.NONE);
		compassLabels[ICompass.SOUTHEAST] = new Label(compassComp, SWT.NONE);
		
		clearCompass();
		createActions();


		for (int i = 0; i < ICompass.N_DIRECTIONS; i++)
		{
			final int direction = i;
			compassLabels[i].addMouseListener(new MouseListener() {
				public void mouseDown(MouseEvent e) {}
				public void mouseDoubleClick(MouseEvent e) {}
				public void mouseUp(MouseEvent e) {
					// THis is only the left-click listener.
					// RIght clicking will popup a context menu
					if( e.button == 1 ) {
						// directionClicked(direction);
					}
				}
			});

			MenuManager menuMgr = new MenuManager("#PopupMenu");
			menuMgr.setRemoveAllWhenShown(true);
			menuMgr.addMenuListener(new IMenuListener() {
				public void menuAboutToShow(IMenuManager manager) {
					CompassView.this.fillContextMenu(manager, direction);
				}
			});
			Menu menu = menuMgr.createContextMenu(compassLabels[direction]);
			compassLabels[i].setMenu(menu);

		
		}
		
		
		
		// Just add the run button
		stopMoving = new Button(main, SWT.NONE);
		stopMoving.setText("STOP " );
		
		FormData stopData = new FormData();
		stopData.left = new FormAttachment(0,10);
		stopData.top = new FormAttachment(compassComp, 10);
		stopMoving.setLayoutData(stopData);
		stopMoving.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				walkingAction.stop();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			} 
			
		});
		
		
		
	}
	
	private static class CompassAction extends Action {
		public static final String DIR_KEY = "%DIR";
		
		private int direction;
		private String text, command;
		private CompassView view;

		public CompassAction(CompassView view, String text, 
				String command) {
			this.view = view;
			this.text = text;
			this.command = command;			
		}

		public void setDirection(int direction) {
			this.direction = direction;
			String t = text.replaceAll(DIR_KEY, commands[direction]);
			this.setText(t);
		}
		
		public String getDirection() {
			return commands[direction];
		}
		// Some may choose to over ride me. THis is for simple actions.
		public void run() {
			String com = command.replaceAll(DIR_KEY, commands[direction]);
			view.sendCommand(com);
		}
	}

	private class WalkingAction implements Runnable {
		private CompassView view;
		private String[] commands;
		private String[] turns;
		
		private boolean stopped;
		private boolean inRT;
		private boolean atFork;
		private int lastTurn;
		private StormFrontClient sfClient;
		private IStormFrontClientListener rtListener;
		
		public WalkingAction(CompassView view, String[] commands, String[] turns) {
			this.view = view;
			this.commands = commands;
			this.turns = turns;
			this.stopped = false;
			this.inRT = false;
			this.atFork = false;
			/*
			if( view.getCompass().getWarlockClient() instanceof StormFrontClient ) {
				this.sfClient = (StormFrontClient)view.getCompass().getWarlockClient();
				rtListener = new IStormFrontClientListener() {

					public void addWarlockClient(IWarlockClient client) {
					}
					public void setActiveClient(IWarlockClient active) {
					}
					public void healthChanged(IWarlockClient source, int health, String label) {
					}
					public void manaChanged(IWarlockClient source, int mana, String label) {
					}
					public void fatigueChanged(IWarlockClient source, int fatigue, String label) {
					}
					public void spiritChanged(IWarlockClient source, int spirit, String label) {
					}
					public void roundtimeStarted(IWarlockClient source, int roundtime) {
						System.out.println("[listener] starting roundtime");
						inRT = true;
					}
					public void roundtimeChanged(IWarlockClient source, int roundtime) {
						if( roundtime == 0 ) inRT = false;
					} 
				};
				sfClient.addStormFrontClientListener(rtListener);
			} else {
				this.sfClient = null;
			}
			*/
		}
		
		public void stop() {
			this.stopped = true;
			sfClient.removeStormFrontClientListener(rtListener);
			System.out.println("someone clicked stop");
		}
		
		// TODO FINISH
		public void run() {
			int turnPointer = 0;
			while( turnPointer < turns.length && !stopped) {
				atFork = false;
				
				System.out.println("turn " + turnPointer + " is " + turns[turnPointer]);
				sendCommand(turns[turnPointer]);
				lastTurn = Compass.getValue(turns[turnPointer]);
				turnPointer++;
				try {					Thread.sleep(500);				} catch( Exception e ) { }
				int x = 0;
				while( !atFork && !stopped ) {
					handleCurrentRoom();
					moveNextRoom();
					x++;
				}
				System.out.println("Hit a fork or stopped: fork: " + atFork + ", stopped: " + stopped);
			}
		}
		
		private void executeCommand(String command) {
			sendCommand(command);
			try {
				Thread.sleep(500); // to ensure lag doesnt ruin it
			} catch( Exception e ) {
				
			}
			System.out.println("[execute command] finished 500 nap");
			if( this.sfClient != null ) {
				this.inRT = true;
				if( sfClient.getRoundtime() == 0 ) {
					this.inRT = false; 
					return; 
				}
				while( inRT && !stopped ) {
					try {
						Thread.sleep(250);
					} catch( Exception e ) {
					}					
				}
			}
		}
		
		
		private void handleCurrentRoom() {

			// do the actions
			int commandPointer = 0;
			while( commandPointer < commands.length && !stopped) {
				executeCommand(commands[commandPointer]);
				commandPointer++;
			}			
		}
		
		private void moveNextRoom() {
			// go forward a room
			
			boolean[] dirs = view.getCompass().getDirections();
			int justCameFrom = Compass.oppositeDir(lastTurn);
			
			if( dirs.length == 1 ) { stopped = true; System.out.println("only " + dirs.length + " exits");}// dead end
			if( dirs.length > 2 ) atFork = true; // need to fork
			if( dirs.length == 2 ) {
				System.out.print("Possible exits: ");
				for( int i = 0; i < dirs.length; i++ ) {
					System.out.print(dirs[i] + ", ");
					// TODO: Continue
				}
				int weGo = -1;
				// FIXME STP I just broke this function.
				//if( dirs[justCameFrom] ) weGo = dirs[1];
				//if( dirs[1] == justCameFrom ) weGo = dirs[0];
				String goDir = Compass.nameFromInt(weGo);

				System.out.println("last move was (" + lastTurn+", " + 
						"), disqualify " + justCameFrom + 
						", we go (" + weGo + ", " + goDir + ")");
				
				if( weGo != -1 ) {
					lastTurn = weGo;
					sendCommand(goDir);
				} else {
					// ok either we're going too fast... or we lost our model.
					// if we have no commands, we're going to fast. Ignore it.
					// if we have commands, we've lost the model. STOP.
					if( commands.length > 0 ) {
						stopped = true;
						System.out.println("[compass debug] We took a wrong turn and thought it worked.");
					}
				}
			}
			
			try {
				Thread.sleep(2000);
			} catch( Exception e ) {
				
			}

		}
	}
	

	
	
	private void createActions() {
		this.peerAction = new CompassAction(this,
				"PEER " + CompassAction.DIR_KEY,
				"peer " + CompassAction.DIR_KEY);
		
		this.runAction = 
			new CompassAction(this, "run " + CompassAction.DIR_KEY, "") {
				public void run() {
					if(walkingAction != null ) {
						System.out.println("STOPPING");
						walkingAction.stop();
					}
					walkingAction = new WalkingAction(
							CompassView.this, new String[] {}, new String[] {getDirection()});
					new Thread( walkingAction ).start();
				}
		};
		
		this.powerWalkAction = 
			new CompassAction(this, "powerwalk " + CompassAction.DIR_KEY, "") {
				public void run() {
					if(walkingAction != null ) {
						System.out.println("STOPPING");
						walkingAction.stop();
					}
					walkingAction = new WalkingAction(	CompassView.this, 
							new String[] {"perc"}, new String[] {getDirection()});
					new Thread( walkingAction ).start();
				}
		};
		
		
	}
	
	
	private void fillContextMenu(IMenuManager manager, int direction) {
		peerAction.setDirection(direction);
		powerWalkAction.setDirection(direction);
		runAction.setDirection(direction);
		manager.add(peerAction);
		manager.add(powerWalkAction);
		manager.add(runAction);
	}
	
	public void setCompass(ICompass compass) {
		this.compass = compass;
		compass.addListener(this);
		System.out.println("*****\nSETTING COMPASS\n*****");
	}
	
	private void directionClicked (int direction) {
		compass.getClient().send(commands[direction]);
	}

	public ICompass getCompass() {
		return this.compass;
	}
	
	public void sendCommand( String text ) {
		compass.getClient().send(text);
		System.out.println("[compass command] - " + text);
	}
	
	public void setFocus() {	}

}
