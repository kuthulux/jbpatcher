package com.mobileread.ixtab.patch.scrollbar;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.JScrollBar;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.amazon.agui.swing.PagingContainer;
import com.mobileread.ixtab.jbpatch.KindleDevice;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableDevice;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSetting;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSettings;
import com.mobileread.ixtab.jbpatch.conf.ui.IntegerSettingPanel;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingChangeListener;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingPanel;

public class ScrollbarPatch extends Patch implements AdjustmentListener {

	private static final String CONF_SCROLLBAR_WIDTH_KEY = "scrollbar.width";
	private static final String CONF_SCROLLBAR_WIDTH_DEFAULT = "66";
	private static final String CONF_SCROLLBAR_WIDTH_I18N_NAME = "scrollbar.width.name";
	private static final String CONF_SCROLLBAR_WIDTH_I18N_DESC = "scrollbar.width.description";
	private static final String CONF_SCROLLBAR_WIDTH_I18N_HINT = "scrollbar.width.hint";
	
	private static final int CONF_MIN_WIDTH = 5;
	private static final int CONF_MAX_WIDTH = 150;


	public static ScrollbarPatch INSTANCE;

	private static final String THEME_CLASS = "com.amazon.agui.swing.plaf.kindle.KindleTheme";
	private static final String THEME_MD5_BEFORE = "26150e376f27cf44484e788e35af8829";
	private static final String THEME_MD5_AFTER = "e5bc21f8a2cd91b526bdfb0f1bb0391d";

	private static final String PAGINGCONTAINER_CLASS = "com.amazon.agui.swing.PagingContainer";
	private static final String PAGINGCONTAINER_MD5_BEFORE = "c3b30042a1bbab39b1c5cb33a5603dde";
	private static final String PAGINGCONTAINER_MD5_AFTER = "31e20abf2204ea67363f09d2384a2957";

	public ScrollbarPatch() {
		synchronized (ScrollbarPatch.class) {
			if (INSTANCE == null) {
				INSTANCE = this;
			}
		}
	}

	public int getVersion() {
		return 20120708;
	}

	public PatchMetadata getMetadata() {
		PatchableDevice pd = new PatchableDevice(KindleDevice.KT_510_1557760049);
		pd.withClass(new PatchableClass(THEME_CLASS).withChecksums(
				THEME_MD5_BEFORE, THEME_MD5_AFTER));
		pd.withClass(new PatchableClass(PAGINGCONTAINER_CLASS).withChecksums(
				PAGINGCONTAINER_MD5_BEFORE, PAGINGCONTAINER_MD5_AFTER));
		return new PatchMetadata(this).withDevice(pd);
	}

	protected void initLocalization(String locale, Map map) {
		if (locale.equals(RESOURCE_ID_ENGLISH)) {
			map.put(I18N_JBPATCH_NAME, "Make scrollbars usable");
			map.put(I18N_JBPATCH_DESCRIPTION,
					"The default scrollbars are too tiny to be usable with a finger. In addition, menus in landscape mode may be too long to fit on the screen. While a scrollbar is displayed, it is not functional. This patch fixes both of these issues.");
			map.put(CONF_SCROLLBAR_WIDTH_I18N_NAME, "Scrollbar size");
			map.put(CONF_SCROLLBAR_WIDTH_I18N_DESC, "The size of the scrollbars, in pixels.");
			map.put(CONF_SCROLLBAR_WIDTH_I18N_HINT, "Sensible values are between 5 and 150.");
		}
	}

	protected ConfigurableSettings initConfigurableSettings() {
		ConfigurableSettings map = new ConfigurableSettings();
		map.add(new IntegerConfigurableSetting(localize(CONF_SCROLLBAR_WIDTH_I18N_NAME), localize(CONF_SCROLLBAR_WIDTH_I18N_DESC), localize(CONF_SCROLLBAR_WIDTH_I18N_HINT), CONF_SCROLLBAR_WIDTH_KEY,
				CONF_SCROLLBAR_WIDTH_DEFAULT));
		return map;
	}
	
	protected class IntegerConfigurableSetting extends ConfigurableSetting {

		public IntegerConfigurableSetting(String name, String description, String hint,
				String key, String defaultValue) {
			super(name, description, hint, key, defaultValue);
		}

		public SettingPanel getPanel(SettingChangeListener listener) {
			return new IntegerSettingPanel(listener);
		}

		public boolean isValid(String value) {
			if (value == null) return false;
			try {
				int i = Integer.parseInt(value);
				return i >= CONF_MIN_WIDTH && i <= CONF_MAX_WIDTH;
			} catch (Throwable t) {
				return false;
			}
		}
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(THEME_MD5_BEFORE)) {
			patchTheme(clazz, "getDefaultResources167");
			patchTheme(clazz, "getDefaultResources212");
		} else {
			patchContainer(clazz);
		}
		return null;
	}

	private void patchTheme(BCClass clazz, String methodName) throws Throwable {
		Code c = clazz.getDeclaredMethods(methodName)[0].getCode(false);
		c.after(72);
		c.remove();
		c.anew().setType(Integer.class);
		c.dup();
		c.constant().setValue(
				Integer.parseInt(getConfigured(CONF_SCROLLBAR_WIDTH_KEY)));
		c.invokespecial().setMethod(
				Integer.class.getConstructor(new Class[] { int.class }));
		c.calculateMaxLocals();
		c.calculateMaxStack();
		// dump(c);
	}

	private void patchContainer(BCClass clazz) throws Throwable {
		Code c = clazz.getDeclaredMethods("enableScrollIndicator")[0]
				.getCode(false);
		// scrollbar.setEnabled(true) instead of false
		c.after(22);
		c.remove();
		c.constant().setValue(true);

		// hook in
		c.after(24);
		c.getstatic().setField(ScrollbarPatch.class.getField("INSTANCE"));
		c.aload().setLocal(0);
		c.getfield().setField(clazz.getDeclaredField("scrollBar"));
		c.aload().setThis();
		c.invokevirtual().setMethod(
				ScrollbarPatch.class.getMethod("hook", new Class[] {
						JScrollBar.class, Object.class }));

		c.calculateMaxLocals();
		c.calculateMaxStack();
	}

	private final WeakHashMap hooks = new WeakHashMap();

	public void hook(JScrollBar scrollbar, Object pager) {
		scrollbar.addAdjustmentListener(this);
		hooks.put(scrollbar, new WeakReference(pager));
	}

	public void adjustmentValueChanged(AdjustmentEvent evt) {
		JScrollBar source = (JScrollBar) evt.getSource();
		WeakReference ref = (WeakReference) hooks.get(source);
		PagingContainer target = null;
		if (ref != null) {
			target = (PagingContainer) ref.get();
		}
		if (target == null) {
			source.removeAdjustmentListener(this);
			hooks.remove(source);
		} else {
			int from = target.getCurrentPage();
			int to = evt.getValue();
			if (from != to) {
				// log("Change page: "+from+" -> "+to);
				target.setPage(to);
			}
		}
	}
}
