package com.mobileread.ixtab.jbpatch.kindlet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.TreeMap;

import javax.swing.JTabbedPane;

import com.mobileread.ixtab.jbpatch.resources.JBPatchResource;
import com.mobileread.ixtab.jbpatch.resources.KeyValueResource;
import com.mobileread.ixtab.jbpatch.resources.ResourceMapProvider;

public class JBPatchUI implements ResourceMapProvider {

	/* Mostly localization-related stuff */
	
	public static final String _TAB_OVERVIEW = "tab.overview";
	public static final String _TAB_DETAILS = "tab.details";
	public static final String _TAB_SYSTEM = "tab.system";
	
	public static final String _OVERVIEW_NOPATCHES = "overview.nopatches";
	public static final String _OVERVIEW_VERSION = "overview.version";
	
	public static final String _SYSTEM_LOG_TITLE = "system.log.title";
	public static final String _SYSTEM_ACTIONS_REVERSESYNC = "system.actions.reversesync";
	public static final String _SYSTEM_ACTIONS_RESTART = "system.actions.restart";
	public static final String _SYSTEM_ACTIONS_LOGREFRESH = "system.actions.logrefresh";
	public static final String _SYSTEM_ACTIONS_CLEANUP = "system.actions.cleanup";
	public static final String _SYSTEM_ACTIONS_TITLE = "system.actions.title";
	public static final String _SYSTEM_ABOUT_VERSION = "system.about.version";
//	public static final String _SYSTEM_ABOUT_TITLE = "system.about.title";


	private TreeMap getDefaultEnglishResourceMap() {
		TreeMap m = new TreeMap();
		m.put(_TAB_OVERVIEW, "Overview");
		m.put(_TAB_SYSTEM, "Details");
		m.put(_TAB_SYSTEM, "System");
		m.put(_OVERVIEW_NOPATCHES, "No patches found.");
		m.put(_SYSTEM_LOG_TITLE, "JBPatch log file");
		m.put(_SYSTEM_ACTIONS_REVERSESYNC, "Reverse Sync");
		m.put(_SYSTEM_ACTIONS_RESTART, "Restart framework");
		m.put(_SYSTEM_ACTIONS_LOGREFRESH, "Refresh log");
		m.put(_SYSTEM_ACTIONS_CLEANUP, "Cleanup");
		m.put(_SYSTEM_ACTIONS_TITLE, "Actions");
		m.put(_SYSTEM_ABOUT_VERSION, "JBPatch Version");
		m.put(_OVERVIEW_VERSION, "Version");
		return m;
	}

	private TreeMap getDefaultGermanResourceMap() {
		TreeMap m = new TreeMap();
		m.put(_TAB_OVERVIEW, "Übersicht");
		m.put(_TAB_SYSTEM, "Details");
		m.put(_TAB_SYSTEM, "System");
		m.put(_OVERVIEW_NOPATCHES, "Keine Patches gefunden.");
		m.put(_SYSTEM_LOG_TITLE, "JBPatch-Logdatei");
		m.put(_SYSTEM_ACTIONS_REVERSESYNC, "Umgekehrte Sync.");
		m.put(_SYSTEM_ACTIONS_RESTART, "Neustart");
		m.put(_SYSTEM_ACTIONS_LOGREFRESH, "Log neu einlesen");
		m.put(_SYSTEM_ACTIONS_CLEANUP, "Aufräumen");
		m.put(_SYSTEM_ACTIONS_TITLE, "Aktionen");
		m.put(_SYSTEM_ABOUT_VERSION, "JBPatch Version");
		m.put(_OVERVIEW_VERSION, "Version");
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
		
		// details is the second tab, thus "1"
		DetailsPanel details = new DetailsPanel(tabs, 1);
		
		tabs.addTab(localize(_TAB_OVERVIEW), new OverviewPanel());
		tabs.addTab(localize(_TAB_DETAILS), details);
		tabs.addTab(localize(_TAB_SYSTEM), new SystemPanel());
		
		root.add(tabs, BorderLayout.CENTER);
		
		root.validate();
	}

}
