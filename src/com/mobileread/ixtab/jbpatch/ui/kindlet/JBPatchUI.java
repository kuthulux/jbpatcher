package com.mobileread.ixtab.jbpatch.ui.kindlet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.TreeMap;

import javax.swing.JTabbedPane;

import com.mobileread.ixtab.jbpatch.conf.ConfigurableSettings;
import com.mobileread.ixtab.jbpatch.resources.JBPatchResource;
import com.mobileread.ixtab.jbpatch.resources.KeyValueResource;
import com.mobileread.ixtab.jbpatch.resources.ResourceMapProvider;

public class JBPatchUI implements ResourceMapProvider, LocalizationKeys {

	/* Mostly localization-related stuff */
	

	private TreeMap getDefaultEnglishResourceMap() {
		TreeMap m = new TreeMap();
		m.put(_TAB_OVERVIEW, "Overview");
		m.put(_TAB_CONFIGURE, "Patch");
		m.put(_TAB_SYSTEM, "System");
		m.put(_OVERVIEW_VERSION, "Version");
		m.put(_OVERVIEW_NOPATCHES, "No patches found.");
		m.put(_SYSTEM_LOG_TITLE, "JBPatch log file");
		m.put(_SYSTEM_ACTIONS_REVERSESYNC, "Reverse Sync");
		m.put(_SYSTEM_ACTIONS_RESTART, "Restart framework");
		m.put(_SYSTEM_ACTIONS_LOGREFRESH, "Refresh log");
		m.put(_SYSTEM_ACTIONS_CLEANUP, "Cleanup");
		m.put(_SYSTEM_ACTIONS_TITLE, "Actions");
		m.put(_SYSTEM_ABOUT_VERSION, "JBPatch Version");
		m.put(_SYSTEM_CONFIRM_CONTINUE, "Are you sure?");
		m.put(_SYSTEM_CONFIRM_DANGEROUS, "This is a potentially dangerous action. Please make sure that you have read the documentation! Continue anyway?");
		m.put(_CONFIG_BUTTON_RESET, "Reset");
		m.put(_CONFIG_BUTTON_COMMIT, "Save");
		m.put(_CONFIG_BUTTON_UNDO, "Undo");
		m.put(_CONFIG_BUTTON_ALL, "Back");
		m.put(_CONFIG_NOPATCH_DESCRIPTION, "This page can be used to view information about particular patches, and to configure them. Please select a patch to configure from the overview panel.");
		m.put(_CONFIG_NOPATCH_TITLE, "No Patch Selected");
		m.put(_CONFIG_NOPATCH_SUBTITLE, "Information");
		m.put(_CONFIG_PATCH_SUBTITLE, "Configuration overview");
		m.put(_CONFIG_PATCH_NOTCONFIGURABLE, "This patch does not require configuration.");
		m.put(_CONFIG_UNSAVED_TITLE, "Save changed configuration?");
		m.put(_CONFIG_UNSAVED_MESSAGE, "You have modified the configuration of this patch, but did not save your changes. Do you want to save the changes now?");
		m.put(_CONFIG_SAVEFAILED_TITLE, "Error while saving");
		m.put(_CONFIG_SAVEFAILED_MESSAGE, "The configuration could not be saved. Please check the log file for further information.");
		m.put(_CONFIG_SETTING_CURRENT, "Active:");
		m.put(_CONFIG_SETTING_PREVIOUS, "Previous:");
		m.put(_CONFIG_SETTING_DEFAULT, "Default:");
		
		return m;
	}

	private TreeMap getDefaultGermanResourceMap() {
		TreeMap m = new TreeMap();
		m.put(_TAB_OVERVIEW, "Übersicht");
		m.put(_TAB_CONFIGURE, "Patch");
		m.put(_TAB_SYSTEM, "System");
		m.put(_OVERVIEW_VERSION, "Version");
		m.put(_OVERVIEW_NOPATCHES, "Keine Patches gefunden.");
		m.put(_SYSTEM_LOG_TITLE, "JBPatch-Logdatei");
		m.put(_SYSTEM_ACTIONS_REVERSESYNC, "Umgekehrte Sync.");
		m.put(_SYSTEM_ACTIONS_RESTART, "Neustart");
		m.put(_SYSTEM_ACTIONS_LOGREFRESH, "Log neu einlesen");
		m.put(_SYSTEM_ACTIONS_CLEANUP, "Aufräumen");
		m.put(_SYSTEM_ACTIONS_TITLE, "Aktionen");
		m.put(_SYSTEM_ABOUT_VERSION, "JBPatch Version");
		
		m.put(_CONFIG_SETTING_CURRENT, "Aktuell:");
		m.put(_CONFIG_SETTING_DEFAULT, "Standard:");

		return m;
	}



	private static KeyValueResource l10n = null;
	
	public String id() {
		return JBPatchUI.class.getName();
	}

	static String localize(String key) {
		if (l10n == null) {
			synchronized (JBPatchUI.class) {
				if (l10n == null) {
					l10n = JBPatchResource.getResource(instance, JBPatchResource.TYPE_LOCALIZATION);
				}
			}
		}
		// should never go wrong, but who knows...
		return l10n == null ? key : l10n.getValue(key);
	}

	public TreeMap getDefaultResourceMap(String resourceId) {
		if (ResourceMapProvider.RESOURCE_ID_ENGLISH.equals(resourceId)) {
			return getDefaultEnglishResourceMap();
		} else if ("de".equals(resourceId)) {
			return getDefaultGermanResourceMap();
		}
		return null;
	}


	/* Below is the actual logic, not much really. */
	private static JBPatchUI instance;
	
	private final Container root;
	
	JBPatchUI(Container rootContainer) {
		instance = this;
		this.root = rootContainer;
	}

	public void init() {
		root.setLayout(new BorderLayout());
		JTabbedPane tabs = new JTabbedPane();
		
		tabs.addTab(localize(_TAB_OVERVIEW), new OverviewPanel());
		tabs.addTab(localize(_TAB_CONFIGURE), new ConfigurationPanel(tabs, 1));
		tabs.addTab(localize(_TAB_SYSTEM), new SystemPanel());
		
		root.add(tabs, BorderLayout.CENTER);
		
		root.validate();
	}

	public ConfigurableSettings getConfigurableSettings() {
		return null;
	}

}
