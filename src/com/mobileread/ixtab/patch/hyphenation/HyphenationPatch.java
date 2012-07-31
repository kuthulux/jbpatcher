package com.mobileread.ixtab.patch.hyphenation;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.security.AllPermission;
import java.security.Permission;
import java.util.Map;

import javax.swing.JComboBox;

import serp.bytecode.BCClass;
import serp.bytecode.BCMethod;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;

import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSetting;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSettings;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingChangeListener;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingPanel;

public class HyphenationPatch extends Patch {

	private static final String CLASS_HYPHENATIONMANAGER_510 = "com.mobipocket.common.library.reader.hyphenation.j";
	private static final String CLASS_FRAMECONSTRUCTOR_510 = "com.mobipocket.common.library.reader.db";

	public static final String MD5_HYPHENATIONMANAGER_510 = "630a7704149435140c4ef8406749160d";
	public static final String MD5_FRAMECONSTRUCTOR_510 = "cbbba07fab3a4e1be58a9553f8a2e700";
	
	static final String MODE_JUSTIFY_KEY = "Justify";
	static final String MODE_DUMB_KEY = "Dumb";
	static final String MODE_SMART_KEY = "Smart";
	
	private static final String CONF_MODE_KEY = "mode";
	private static final String CONF_MODE_DEFAULT = MODE_SMART_KEY;
	private static final String CONF_MODE_I18N_NAME = "mode.name";
	private static final String CONF_MODE_I18N_DESC = "mode.description";
	private static final String CONF_MODE_I18N_HINT = "mode.hint";

	private static final String CONF_MODE_JUSTIFY_I18N = "mode.justify";
	private static final String CONF_MODE_DUMB_I18N = "mode.dumb";
	private static final String CONF_MODE_SMART_I18N = "mode.smart";
	
	static HyphenationPatch INSTANCE;
	
	public HyphenationPatch() {
		INSTANCE = this;
	}

	public int getVersion() {
		return 20120801;
	}

	public PatchMetadata getMetadata() {
		return new PatchMetadata(this).withClass(
				new PatchableClass(CLASS_FRAMECONSTRUCTOR_510).withChecksums(
						MD5_FRAMECONSTRUCTOR_510, "?")).withClass(
				new PatchableClass(CLASS_HYPHENATIONMANAGER_510).withChecksums(
						MD5_HYPHENATIONMANAGER_510, "?"));
	}


	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Fix Reader Layout and Hyphenation");
			map.put(I18N_JBPATCH_DESCRIPTION,
					"This patch fixes the layout of the E-Book reader, and allows for hyphenation.");
			map.put(CONF_MODE_I18N_NAME, "Adjustment mode");
			map.put(CONF_MODE_I18N_DESC, "Select how you want the reader behavior to be modified.");
			map.put(CONF_MODE_I18N_HINT, "If you are having trouble with hyphenation, ensure that the book has the correct language set, and that hyphenation rules for that language are available. Please refer to the patch documentation on the Wiki page for further information.");
			
			map.put(CONF_MODE_JUSTIFY_I18N, "Justify: only justify text to block layout");
			map.put(CONF_MODE_DUMB_I18N, "Dumb: justification and dumb hyphenation");
			map.put(CONF_MODE_SMART_I18N, "Smart: justification and language-specific hyphenation");
		}
	}

	protected ConfigurableSettings initConfigurableSettings() {
		ConfigurableSettings map = new ConfigurableSettings();
		map.add(new ModeSetting());
		return map;
	}
	
	public String getMode() {
		return getConfigured(CONF_MODE_KEY);
	}
	
	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_HYPHENATIONMANAGER_510)) {
			return patchHyphenationManager510(clazz);
		}
		if (md5.equals(MD5_FRAMECONSTRUCTOR_510)) {
			return patchFrameConstructor510(clazz);
		}
		return "Unexpected error: unknown MD5 " + md5;
	}

	public Permission[] getRequiredPermissions() {
		return new Permission[] { new AllPermission() };
	}

	private String patchFrameConstructor510(BCClass clazz) throws Throwable {
		BCMethod m = clazz.getDeclaredMethod("e");
		Code c = m.getCode(false);
		c.before(47);
		// percentage of a word's width that must be available in the current
		// line, for it to be considered for hyphenation
		// original is 25%
		((ConstantInstruction) c.next()).setValue(-1);

		// this fixes the weird behavior that some lines do not get justified
		// it essentially always returns a "false" value for the following
		// condition
		// (by turning it into "if (0 > 1)")
		c = clazz.getDeclaredMethod("L").getCode(false);
		c.before(45);
		c.pop();
		c.pop();
		c.constant().setValue(0);
		c.constant().setValue(1);

		return null;
	}

	private String patchHyphenationManager510(BCClass clazz) throws Throwable {

		BCMethod m = clazz.getDeclaredMethod("D", new String[] { "int" });
		Code c = m.getCode(false);

		c.beforeFirst();
		c.iload().setLocal(1);
		c.invokestatic().setMethod(
				UniversalHyphenationEngine.class.getMethod("configure",
						new Class[] { int.class }));
		c.areturn();

		return null;

	}
	
	private class ModeSetting extends ConfigurableSetting {

		public ModeSetting() {
			super(localize(CONF_MODE_I18N_NAME), localize(CONF_MODE_I18N_DESC), localize(CONF_MODE_I18N_HINT), CONF_MODE_KEY, CONF_MODE_DEFAULT);
		}

		public SettingPanel getPanel(SettingChangeListener listener) {
			return new ModeSettingPanel(listener);
		}

		public boolean isValid(String v) {
			return MODE_JUSTIFY_KEY.equals(v) || MODE_DUMB_KEY.equals(v) || MODE_SMART_KEY.equals(v);
		}
		
	}
	
	private class ModeSettingPanel extends SettingPanel {
		private static final long serialVersionUID = 1L;

		private final Mode[] modes = new Mode[3];
		private final JComboBox combo = new JComboBox();
		
		public ModeSettingPanel(final SettingChangeListener listener) {
			super(listener);
			
			modes[0] = new Mode(MODE_JUSTIFY_KEY, localize(CONF_MODE_JUSTIFY_I18N));
			modes[1] = new Mode(MODE_DUMB_KEY, localize(CONF_MODE_DUMB_I18N));
			modes[2] = new Mode(MODE_SMART_KEY, localize(CONF_MODE_SMART_I18N));
			
			setLayout(new BorderLayout());
			add(combo);
			for (int i=0; i < modes.length; ++i) {
				combo.addItem(modes[i]);
			}
			combo.addItemListener(new ItemListener(){

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						listener.valueChanged(ModeSettingPanel.this, ((Mode)e.getItem()).key);
					}
				}});
		}

		public void setValue(String value) {
			for (int i=0; i < modes.length; ++i) {
				if (modes[i].key.equals(value)) {
					combo.setSelectedItem(modes[i]);
					break;
				}
			}
		}
	}
	
	class Mode {
		public final String key;
		public final String description;
		
		public Mode(String key, String descriptionKey) {
			super();
			this.key = key;
			this.description = localize(descriptionKey);
		}
		
		public String toString() {
			return description;
		}

		// generated code
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Mode other = (Mode) obj;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			return true;
		}
	}
}
