package com.mobileread.ixtab.patch.progressbar;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.reflect.Field;
import java.security.AllPermission;
import java.security.Permission;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.amazon.ebook.booklet.reader.sdk.content.Book;
import com.amazon.ebook.booklet.reader.sdk.content.TableOfContents;
import com.amazon.ebook.booklet.reader.sdk.content.TableOfContentsEntry;
import com.amazon.ebook.booklet.reader.sdk.ui.element.Viewport;
import com.mobileread.ixtab.jbpatch.Log;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSetting;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSettings;
import com.mobileread.ixtab.jbpatch.conf.ui.RadioButtonsSettingPanel;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingChangeListener;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingEntry;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingPanel;

public class ProgressBarPatch extends Patch {

	private static final String CLASS = "com.amazon.ebook.booklet.reader.impl.ui.ProgressBarImpl";
	public static final String MD5_BEFORE_PRISTINE = "4b168e63797ccaf1f642872b71c09ef9";
	public static final String MD5_BEFORE_MARGINSPATCH = "c2b3eb58a2bc09a6948c5ef2984cb106";
	
	private static final String MD5_AFTER_PRISTINE = "23ae27ca1a3a9b0a95f8f2c7b4211cd3";
	private static final String MD5_AFTER_MARGINSPATCH = "17760c3396b51575af68608bf466a8df";

	private static final String MODE_DEFAULT = "default";
	private static final String MODE_FULL = "full";
	private static final String MODE_PROGRESS = "progress";
	
	
	
	private static ProgressBarPatch instance;
	
	private static Field showFullInfoField = null;
	private static int leftBorder;
	private static int rightBorder;
	
	private static Integer lastBookHashCode = null;
	private static int startReadingPosition = 0;
	private static TocEntry[] tocEntries = null;
	
	private static final String CONF_KEY = "mode";
	private static final String UI_DESC = "mode.desc";
	private static final String UI_NAME = "mode.name";
	private static final String UI_HINT = "mode.hint";
	private static final String UI_MODE_DEFAULT = "mode.desc.default";
	private static final String UI_MODE_FULL = "mode.desc.full";
	private static final String UI_MODE_PROGRESS = "mode.desc.progress";
	private static final String UI_MODE_DEFAULT_SHORT = "mode.desc.default.short";
	private static final String UI_MODE_FULL_SHORT = "mode.desc.full.short";
	private static final String UI_MODE_PROGRESS_SHORT = "mode.desc.progress.short";
	
	private String displayMode = null;
	
	public ProgressBarPatch() {
		instance = this;
	}

	public int getVersion() {
		return 20120825;
	}

	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Customize Progress Indicator");
			map.put(I18N_JBPATCH_DESCRIPTION, "This patch allows to customize the appearance of the progress indicator displayed at the bottom of the E-Book reader.");
			map.put(UI_NAME, "Progress Indicator Appearance");
			map.put(UI_DESC, "Controls what is displayed in the progress indicator while reading a book.");
			map.put(UI_HINT, "The progress indicator is shown at the bottom of the screen while reading a book. Note: if you want to completely hide the progress indicator, you can use the patch which modifies the reader margins.");
			
			map.put(UI_MODE_DEFAULT, "Default: Position and percentage");
			map.put(UI_MODE_FULL, "Full: Display all information");
			map.put(UI_MODE_PROGRESS, "Graphical: Progress bar, similar to the Kindle 3");
			map.put(UI_MODE_DEFAULT_SHORT, "Default");
			map.put(UI_MODE_FULL_SHORT, "Full");
			map.put(UI_MODE_PROGRESS_SHORT, "Graphical");
		}
	}

	public PatchMetadata getMetadata() {
		PatchableClass pc = new PatchableClass(CLASS).withChecksums(MD5_BEFORE_MARGINSPATCH,
				MD5_AFTER_MARGINSPATCH).withChecksums(MD5_BEFORE_PRISTINE, MD5_AFTER_PRISTINE);
		return new PatchMetadata(this).withClass(pc);
	}
	
	

	public Permission[] getRequiredPermissions() {
		return new Permission[] {new AllPermission()};
	}

	private String getDisplayMode() {
		if (displayMode == null) {
			String configuredMode = getConfigured(CONF_KEY);
			if (isValidDisplayMode(configuredMode)) {
				displayMode = configuredMode;
			} else {
				displayMode = MODE_PROGRESS;
			}
		}
		return displayMode;
	}

	private static boolean isValidDisplayMode(String value) {
		return MODE_DEFAULT.equals(value) || MODE_FULL.equals(value) || MODE_PROGRESS.equals(value);
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_BEFORE_MARGINSPATCH) || md5.equals(MD5_BEFORE_PRISTINE)) {
			return patchPaintProgress(clazz);
		}
		return "unsupported MD5: "+md5;
	}

	private String patchPaintProgress(BCClass clazz) throws Exception {
		Code c = clazz.getDeclaredMethod("D", new String[]{"java.awt.Graphics"})
				.getCode(false);
		c.before(37);
		c.aload().setThis();
		c.invokestatic().setMethod(ProgressBarPatch.class.getMethod("onPaintStart", new Class[]{Object.class}));
		
		c.before(133);
		c.aload().setThis();
		c.getfield().setFieldIndex(4);
		c.aload().setLocal(8);
		c.aload().setLocal(3);
		c.aload().setThis();
		c.getfield().setFieldIndex(21);
		c.invokestatic().setMethod(ProgressBarPatch.class.getMethod("onLeftStringSet", new Class[]{boolean.class, String.class, FontMetrics.class, int.class }));

		c.before(157);
		c.aload().setThis();
		c.getfield().setFieldIndex(4);
		c.iload().setLocal(10);
		c.aload().setThis();
		c.aload().setLocal(2);
		c.aload().setThis();
		c.getfield().setFieldIndex(21);
		c.aload().setLocal(1);
		c.invokestatic().setMethod(ProgressBarPatch.class.getMethod("onRightStringSet", new Class[]{boolean.class, int.class, Object.class, Object.class, int.class, Object.class}));

//		dump(c);
		c.calculateMaxLocals();
		c.calculateMaxStack();
		return null;
	}

	public static void onPaintStart(Object progressBarImplObject) {
		try {
			initFields(progressBarImplObject);
			if (MODE_FULL.equals(instance.getDisplayMode())) {
				showFullInfoField.set(progressBarImplObject, Boolean.TRUE);
			}
		} catch (Exception e) {
			e.printStackTrace(Log.INSTANCE);
		}
	}
	
	public static void onLeftStringSet(boolean full, String string, FontMetrics metrics, int margin) {
		if (!full && MODE_PROGRESS.equals(instance.getDisplayMode())) {
			int textWidth = metrics.stringWidth(string);
			leftBorder = margin + textWidth;
		}
	}
	
	public static void onRightStringSet(boolean full, int width, Object progressBarImplObject, Object viewPortObject, int margin, Object graphicsObject) {
		if (full || !MODE_PROGRESS.equals(instance.getDisplayMode())) {
			return;
		}
		
		JComponent footer = (JComponent) progressBarImplObject;
		int physicalWidth = footer.getWidth();
		int center = physicalWidth / 2;
		rightBorder = footer.getWidth() - margin - width;
		
		int maxHalfWidth = Math.min(rightBorder - center, center - leftBorder);
		maxHalfWidth -= ProgressBarPainter.MIN_PADDING_PX; // at least a little bit of spacing
		
		float relateWidthAvailable = (((float)maxHalfWidth)*2 / physicalWidth);
		float relativeWidthUsed = Math.min(relateWidthAvailable, ProgressBarPainter.PREFERRED_RELATIVE_WIDTH);
		int absoluteWidthUsed = (int)(((float)physicalWidth) * relativeWidthUsed);
		
		int offsetX = center - absoluteWidthUsed / 2;
		
		Graphics g = (Graphics) graphicsObject;
		Graphics gc = g.create(offsetX, 0, absoluteWidthUsed, footer.getHeight());
		paintProgressBar(progressBarImplObject, viewPortObject, gc, absoluteWidthUsed, footer.getHeight());
	}
	
	private static void initFields(Object o) throws Exception {
		if (showFullInfoField == null) {
			showFullInfoField = makeFieldAccessible(o.getClass(), "showFullInfo");
		}
	}

	private static Field makeFieldAccessible(Class clazz, String fieldname) throws Exception {
		Field f = clazz.getDeclaredField(fieldname);
		f.setAccessible(true);
		return f;
	}
	
	private static void paintProgressBar(Object progressBarImplObject, Object viewPortObject, Graphics g, int width, int height) {
		g.fillRect(0, 0, width, height);
		try {
			Viewport vp = (Viewport) viewPortObject;
			Book book = vp.k();
			TableOfContents toc = book.c();
			int bookStart = book.C().c();
			int bookEnd = book.d().c() - bookStart;
			int position = vp.H().c() - bookStart;
			if (lastBookHashCode == null || lastBookHashCode.intValue() != book.hashCode()) {
				lastBookHashCode = new Integer(book.hashCode());
				startReadingPosition = position;
				tocEntries = createTocEntries(toc, bookStart);
			}
			ProgressBarPainter.paint((Graphics2D)g, width, height, bookEnd, tocEntries, position, startReadingPosition);
		} catch (Throwable t) {
			t.printStackTrace(Log.INSTANCE);
		}
	}
	
	private static TocEntry[] createTocEntries(TableOfContents toc, int bookStart) {
		List entries = new LinkedList();
		try {
			if (toc != null) {
				addTocEntries(entries, toc, toc.h(null), bookStart);
			}
		} catch (Exception e) {
			e.printStackTrace(Log.INSTANCE);
			entries.clear();
		}
		TocEntry[] array = new TocEntry[entries.size()];
		int p = 0;
		Iterator it = entries.iterator();
		while (it.hasNext()) {
			TocEntry entry = (TocEntry) it.next();
			array[p++] = entry;
		}
		return array;
	}

	private static void addTocEntries(List list, TableOfContents toc, TableOfContentsEntry entry, int bookStart) throws Exception {
		while (entry != null) {
			int position = entry.B().c();
			int level = entry.K();
			list.add(new TocEntry(position - bookStart, level));
			// children
			addTocEntries(list, toc, toc.H(entry), bookStart);
			entry = toc.h(entry);
		}
	}
	
	protected ConfigurableSettings initConfigurableSettings() {
		ConfigurableSettings settings = new ConfigurableSettings();
		settings.add(new ProgressBarSetting());
		return settings;
	}

	private class ProgressBarSetting extends ConfigurableSetting {

		private final SettingEntry[] entries;
		public ProgressBarSetting() {
			super(localize(UI_NAME), localize(UI_DESC), localize(UI_HINT), CONF_KEY , MODE_PROGRESS);
			entries = new SettingEntry[3];
			entries[0] = new SettingEntry(MODE_DEFAULT, localize(UI_MODE_DEFAULT));
			entries[1] = new SettingEntry(MODE_FULL, localize(UI_MODE_FULL));
			entries[2] = new SettingEntry(MODE_PROGRESS, localize(UI_MODE_PROGRESS));
		}

		public SettingPanel getPanel(SettingChangeListener listener) {
			return new RadioButtonsSettingPanel(listener, entries);
		}

		public boolean isValid(String value) {
			return isValidDisplayMode(value);
		}

		public String getLocalized(String value) {
			if (value.equals(MODE_DEFAULT)) {
				return localize(UI_MODE_DEFAULT_SHORT);
			}
			if (value.equals(MODE_FULL)) {
				return localize(UI_MODE_FULL_SHORT);
			}
			if (value.equals(MODE_PROGRESS)) {
				return localize(UI_MODE_PROGRESS_SHORT);
			}
			return value;
		}
		
	}

}
