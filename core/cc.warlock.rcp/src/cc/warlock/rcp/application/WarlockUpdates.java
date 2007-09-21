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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.update.configuration.IConfiguredSite;
import org.eclipse.update.configuration.ILocalSite;
import org.eclipse.update.core.IFeatureReference;
import org.eclipse.update.core.ISite;
import org.eclipse.update.core.SiteManager;
import org.eclipse.update.core.VersionedIdentifier;
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
		try {
			IProgressMonitor monitor = new NullProgressMonitor();
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

							newVersions.put(localFeatureRefs[j], featureVersion);
						}
					}
				}
			}

			if (newVersions.size() > 0)
			{
				List<IFeatureReference> featuresToUpgrade = promptUpgrade(newVersions);
				for (IFeatureReference featureRef : featuresToUpgrade)
				{
					IInstallFeatureOperation operation = OperationsManager.getOperationFactory().createInstallOperation(
						configuredSite, featureRef.getFeature(monitor), null, null, null);
					
					operation.execute(monitor, null);
				}
				if (featuresToUpgrade.size() > 0)
					localSite.save();
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
