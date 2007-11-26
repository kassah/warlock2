package cc.warlock.rcp.stormfront.ui.actions;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

import cc.warlock.core.configuration.Profile;
import cc.warlock.core.stormfront.network.ISGEConnectionListener;
import cc.warlock.core.stormfront.network.SGEConnection;
import cc.warlock.rcp.plugin.Warlock2Plugin;
import cc.warlock.rcp.stormfront.adapters.SWTSGEConnectionListenerAdapter;
import cc.warlock.rcp.stormfront.ui.StormFrontSharedImages;
import cc.warlock.rcp.stormfront.ui.util.LoginUtil;
import cc.warlock.rcp.views.GameView;

public class ProfileConnectAction extends Action implements ISGEConnectionListener {
	private Profile profile;
	private IProgressMonitor monitor;
	private boolean finished;
	private IStatus status;
	private GameView gameView;
	
	public ProfileConnectAction (Profile profile) {
		super(profile.getCharacterName(), StormFrontSharedImages.getImageDescriptor(StormFrontSharedImages.IMG_CHARACTER));
		setDescription(profile.getGameName() + " character \"" + profile.getCharacterName() + "\"");
		
		this.profile = profile;
	}
	
	@Override
	public void run() {	
		finished = false;
		status = Status.OK_STATUS;
		
		Job connectJob = new Job("Logging into profile \"" + profile.getCharacterName() + "\"...") {
			protected IStatus run(IProgressMonitor monitor) {
				ProfileConnectAction.this.monitor = monitor;
				
				SGEConnection connection = new SGEConnection();
				connection.addSGEConnectionListener(new SWTSGEConnectionListenerAdapter(ProfileConnectAction.this));
				monitor.beginTask("Logging into profile \"" + profile.getCharacterName() + "\"...", 5);

				connection.connect();
				
				while (!ProfileConnectAction.this.finished) {
					Display.getDefault().syncExec(new Runnable() {
						public void run () { 
							Display.getDefault().readAndDispatch();
						}
					});
				}
				
				return status;
			}
		};
		
		connectJob.setUser(true);
		connectJob.schedule();
	}

	public void loginReady(SGEConnection connection) {
		monitor.worked(1);
		
		if (!monitor.isCanceled()) {
			connection.login(profile.getAccount().getAccountName(), profile.getAccount().getPassword());
		} else {
			status = Status.CANCEL_STATUS;
			finished = true;
		}
	}
	
	public void loginFinished(SGEConnection connection) {
		monitor.worked(1);
	}
	
	public void sgeError(SGEConnection connection, int errorCode) {
		LoginUtil.showAuthenticationError(errorCode);
		this.status = new Status(IStatus.ERROR, Warlock2Plugin.PLUGIN_ID, LoginUtil.getAuthenticationError(errorCode));
		finished = true;
	}
	
	public void gamesReady(SGEConnection connection, Map<String, String> games) {
		monitor.worked(1);
		
		if (!monitor.isCanceled())
		{
			connection.selectGame(profile.getGameCode());
		} else {
			status = Status.CANCEL_STATUS;
			finished = true;
		}
	}
	
	public void charactersReady(SGEConnection connection, Map<String, String> characters) {
		monitor.worked(1);
		
		connection.selectCharacter(profile.getCharacterCode());
	}

	public void readyToPlay(SGEConnection connection, Map<String,String> loginProperties) {
		monitor.worked(1);
		monitor.done();
		
		if (!monitor.isCanceled())
		{
			if (gameView == null)
				LoginUtil.connectAndOpenGameView(loginProperties);
			else
				LoginUtil.connect(gameView, loginProperties);
		} else {
			status = Status.CANCEL_STATUS;
		}

		finished = true;
	}

	public void setGameView(GameView gameView) {
		this.gameView = gameView;
	}

	public Profile getProfile() {
		return profile;
	}

}
