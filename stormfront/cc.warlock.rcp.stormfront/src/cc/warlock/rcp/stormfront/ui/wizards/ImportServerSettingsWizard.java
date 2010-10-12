package cc.warlock.rcp.stormfront.ui.wizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.configuration.Profile;
import cc.warlock.core.stormfront.client.internal.StormFrontClient;
import cc.warlock.core.stormfront.settings.StormFrontServerSettings;
import cc.warlock.core.stormfront.settings.internal.StormFrontClientSettings;
import cc.warlock.rcp.wizards.WizardWithNotification;

public class ImportServerSettingsWizard extends WizardWithNotification implements IImportWizard {

	protected ImportSettingsWizardPage page1;

	@Override
	public void addPages() {
		page1 = new ImportSettingsWizardPage();
		addPage(page1);
	}
	
	@Override
	public boolean performFinish() {
		Profile profile = page1.getTargetProfile();
		
		StormFrontServerSettings serverSettings = null;
		StormFrontClientSettings clientSettings = null;
		StormFrontClient profileClient = null;
		
		for (IWarlockClient client : WarlockClientRegistry.getActiveClients()) 
		{
			if (client instanceof StormFrontClient) {
				StormFrontClient sfClient = (StormFrontClient) client;
				
				if (sfClient.getCharacterName().get().equals(profile.getName())) {
					clientSettings = (StormFrontClientSettings) sfClient.getStormFrontClientSettings();
					serverSettings = sfClient.getServerSettings();
					profileClient = sfClient;
				}
			}
		}
		
		if (clientSettings != null && serverSettings != null && profileClient != null) {
			File settingsFile = null;
			if (page1.getType() == ImportSettingsWizardPage.FILE_IMPORT) {
				settingsFile = new File(page1.getFile());
			}
			if (page1.getType() == ImportSettingsWizardPage.SERVER_IMPORT) {
				System.out.println(profileClient.getPlayerId());
				settingsFile = ConfigurationUtil.getConfigurationFile("serverSettings_" + profileClient.getPlayerId() + ".xml", false);
			}
			try {
				FileInputStream stream = new FileInputStream(settingsFile);
				serverSettings.importServerSettings(stream, clientSettings);
				
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {}
}
