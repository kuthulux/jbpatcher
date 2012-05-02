package com.mobileread.ixtab.patch;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import javax.swing.JScrollBar;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.amazon.agui.swing.PagingContainer;
import com.mobileread.ixtab.jbpatch.Descriptor;
import com.mobileread.ixtab.jbpatch.Patch;

public class ScrollbarPatch extends Patch implements AdjustmentListener {

	private static final int SCROLLBAR_WIDTH = 66;
	private static final String MD5_THEME = "26150e376f27cf44484e788e35af8829";
	private static final String MD5_PAGINGCONTAINER = "c3b30042a1bbab39b1c5cb33a5603dde";
	public static ScrollbarPatch INSTANCE;
	
	
	public ScrollbarPatch() {
		synchronized (ScrollbarPatch.class) {
			if (INSTANCE == null) {
				INSTANCE = this;
			}
		}
	}

	protected Descriptor[] getDescriptors() {
		return new Descriptor[] { new Descriptor(
				"com.amazon.agui.swing.plaf.kindle.KindleTheme",
				new String[] { MD5_THEME }),
				new Descriptor(
						"com.amazon.agui.swing.PagingContainer",
						new String[] { MD5_PAGINGCONTAINER })};
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_THEME)) {
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
//		c.after(44);
//		c.remove();
//		c.constant().setValue("UDRL");
		c.after(72);
		c.remove();
		c.anew().setType(Integer.class);
		c.dup();
		c.constant().setValue(SCROLLBAR_WIDTH);
		c.invokespecial().setMethod(Integer.class.getConstructor(new Class[] {int.class}));
		c.calculateMaxLocals();
		c.calculateMaxStack();
//		dump(c);
	}

	private void patchTheme212(BCClass clazz) throws Throwable {
		Code c = clazz.getDeclaredMethods("getDefaultResources212")[0]
		                                            				.getCode(false);
		c.after(72);
		c.remove();
		c.anew().setType(Integer.class);
		c.dup();
		c.constant().setValue(SCROLLBAR_WIDTH);
		c.invokespecial().setMethod(Integer.class.getConstructor(new Class[] {int.class}));
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
		c.invokevirtual().setMethod(ScrollbarPatch.class.getMethod("hook", new Class[] {JScrollBar.class, Object.class}));
		
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
//				log("Change page: "+from+" -> "+to);
				target.setPage(to);
			}
		}
	}
}
