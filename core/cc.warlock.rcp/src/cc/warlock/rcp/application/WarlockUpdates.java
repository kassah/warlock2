package cc.warlock.rcp.application;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.update.configuration.IConfiguredSite;
import org.eclipse.update.configuration.ILocalSite;
import org.eclipse.update.core.IFeature;
import org.eclipse.update.core.IFeatureReference;
import org.eclipse.update.core.ISite;
import org.eclipse.update.core.SiteManager;
import org.eclipse.update.core.VersionedIdentifier;
import org.eclipse.update.internal.operations.UpdateUtils;
import org.eclipse.update.operations.IInstallFeatureOperation;
import org.eclipse.update.operations.OperationsManager;

public class WarlockUpdates {

	public static final String UPDATE_SITE = "http://www.warlock.cc/updates";
	
	public static List<IFeatureReference> promptUpgrade (Map<IFeatureReference, VersionedIdentifier> newVersions)
	{
//		Shell shell = new Shell();
//		WarlockUpdateDialog dialog = new WarlockUpdateDialog(shell, newVersions);
//		shell.setSize(400, 400);
//		int response = dialog.open();
//		
//		if (response == Window.OK)
//		{
//			return dialog.getSelectedFeatures();
//		}
		
		return Collections.emptyList();
	}
	
	public static void checkForUpdates ()
	{
		IRunnableWithProgress runnable = new IRunnableWithProgress ()
		{
			public void run (IProgressMonitor monitor)
			{
				try {
					ISite updateSite = SiteManager.getSite(new URL(UPDATE_SITE), monitor);
					IFeatureReference[] featureRefs = updateSite.getFeatureReferences();
					ILocalSite localSite = SiteManager.getLocalSite();
					IConfiguredSite configuredSite = localSite.getCurrentConfiguration().getConfiguredSites()[0];
					IFeatureReference[] localFeatureRefs = configuredSite.getConfiguredFeatures();
					
					HashMap<IFeatureReference, VersionedIdentifier> newVersions  = new HashMap<IFeatureReference, VersionedIdentifier>();
		
					for (int i = 0; i < featureRefs.length; i++) {
						for (int j = 0; j < localFeatureRefs.length; j++) {
		
							VersionedIdentifier featureVersion = featureRefs[i].getVersionedIdentifier();
							VersionedIdentifier localFeatureVersion = localFeatureRefs[j].getVersionedIdentifier();
		
							if (featureVersion.getIdentifier().equals(localFeatureVersion.getIdentifier())) {
		
								if (featureVersion.getVersion().isGreaterThan(localFeatureVersion.getVersion())) {
		
									newVersions.put(featureRefs[i], localFeatureVersion);
								}
							}
						}
					}
		
					if (newVersions.size() > 0)
					{
						ProgressMonitorDialog dialog = new ProgressMonitorDialog(new Shell());
						dialog.setBlockOnOpen(false);
						dialog.open();
						
						List<IFeatureReference> featuresToUpgrade = promptUpgrade(newVersions);
						for (IFeatureReference featureRef : featuresToUpgrade)
						{
							IFeature feature = featureRef.getFeature(monitor);
							
							IInstallFeatureOperation operation = OperationsManager.getOperationFactory().createInstallOperation(
								configuredSite, feature, null, null, null);
							
							operation.execute(dialog.getProgressMonitor(),null);
						}
						if (featuresToUpgrade.size() > 0) {
							localSite.save();
						}
						
						dialog.close();
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (CoreException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		};
			
		try {
			PlatformUI.getWorkbench().getProgressService().run(true, true, runnable);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
