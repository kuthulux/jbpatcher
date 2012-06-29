package com.mobileread.ixtab.patch;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.lang.ref.WeakReference;
import java.util.TreeMap;
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

public class ScrollbarPatch extends Patch implements AdjustmentListener {

	private static final int SCROLLBAR_WIDTH = 66;
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
		return 20120605;
	}
	
	public PatchMetadata getMetadata() {
		PatchableDevice pd = new PatchableDevice(KindleDevice.KT_510_1557760049);
		pd.withClass(new PatchableClass(THEME_CLASS).withChecksums(THEME_MD5_BEFORE, THEME_MD5_AFTER));
		pd.withClass(new PatchableClass(PAGINGCONTAINER_CLASS).withChecksums(PAGINGCONTAINER_MD5_BEFORE, PAGINGCONTAINER_MD5_AFTER));
		return new PatchMetadata(this).withDevice(pd);
	}

	public TreeMap getDefaultResourceMap(String id) {
		if (RESOURCE_ID_ENGLISH.equals(id)) {
			TreeMap map = new TreeMap();
			map.put(RESOURCE_JBPATCH_PATCHNAME, "Make scrollbars usable");
			return map;
		} else if ("de".equals(id)) {
			TreeMap map = new TreeMap();
			map.put(RESOURCE_JBPATCH_PATCHNAME, "Scrollbars benutzbar machen");
			return map;
		}
		return null;
	}


	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(THEME_MD5_BEFORE)) {
			patchTheme167(clazz);
			patchTheme212(clazz);
		} else {
			patchContainer(clazz);
		}
		return null;
	}

	private void patchTheme167(BCClass clazz) throws Throwable {
		Code c = clazz.getDeclaredMethods("getDefaultResources167")[0]
				.getCode(false);
		c.after(72);
		c.remove();
		c.anew().setType(Integer.class);
		c.dup();
		c.constant().setValue(SCROLLBAR_WIDTH);
		c.invokespecial().setMethod(
				Integer.class.getConstructor(new Class[] { int.class }));
		c.calculateMaxLocals();
		c.calculateMaxStack();
		// dump(c);
	}

	private void patchTheme212(BCClass clazz) throws Throwable {
		Code c = clazz.getDeclaredMethods("getDefaultResources212")[0]
				.getCode(false);
		c.after(72);
		c.remove();
		c.anew().setType(Integer.class);
		c.dup();
		c.constant().setValue(SCROLLBAR_WIDTH);
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
