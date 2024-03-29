package com.mobileread.ixtab.patch.passwd;

import java.awt.event.ActionEvent;
import java.security.AllPermission;
import java.security.Permission;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;

import serp.bytecode.BCClass;
import serp.bytecode.Code;
import serp.bytecode.Instruction;

import com.amazon.agui.swing.ConfirmationDialog;
import com.amazon.kindle.content.catalog.CatalogEntry;
import com.mobileread.ixtab.jbpatch.Environment;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;

public class PasswordPatch extends Patch {

	private static final String CLASS_OPENITEMACTION = "com.amazon.kindle.home.action.OpenItemAction";
	public static final String MD5_OPENITEMACTION_510_BEFORE = "4fb5c0bc58d97807f3c589452f0c26a6";
	public static final String MD5_OPENITEMACTION_510_AFTER = "3e2085590a40ff1644dd0d10b3ef3e43";
	
	private static final String CLASS_GOTODIALOGACTION = "com.amazon.kindle.home.action.DetailsActions$GotoDialogAction";
	public static final String MD5_GOTODIALOGACTION_510_BEFORE = "9e1e84844665f942455c6d08130ae820";
	public static final String MD5_GOTODIALOGACTION_510_AFTER = "1d432a4b367a89f04f2ab37ec55aba4f";

	private static final String CLASS_OPENDETAILSACTION = "com.amazon.kindle.home.action.OpenDetailsAction";
	public static final String MD5_OPENDETAILSACTION_510_BEFORE = "689382577ea0cd5e8bb4c452c4f0fcb9";
	public static final String MD5_OPENDETAILSACTION_510_AFTER = "a1e3a1e7b65058b4047de07d2b94e572";
	
	private static final String CLASS_DETAILVIEW = "com.amazon.kindle.swing.DetailView";
	public static final String MD5_DETAILVIEW_510_BEFORE = "43b9d37a3eabb49728ef2021ff3a9e31";
	public static final String MD5_DETAILVIEW_510_AFTER = "13975eac8c19e963fd3ccc57434bfee8";
	


	private static PasswordPatch instance;

	private String currentKey = null;

	public PasswordPatch() {
		instance = this;
	}

	public int getVersion() {
		return 20130413;
	}
	
	public boolean isAvailable() {
		int jb = Environment.getJBPatchVersionDate();
		String fw = Environment.getFirmware();
		return jb >= 20130328 && "5.1.0".equals(fw);
	}

	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Password-protect Content");
			map.put(I18N_JBPATCH_DESCRIPTION, "This patch can be used to add a simple password protection for items such as books, collections, or applications.");
			map.put("ok","OK");
			map.put("cancel","Cancel");
			map.put("error.title","Error");
			map.put("error.message","The password database could not be updated. This should not have happened, so please report this error.");
			map.put("password.enter","Enter Password");
			map.put("password.set","Set Password");
			map.put("password.verify","Confirm Password");
			map.put("item.protected","This item is protected. Please enter the password to gain access.");
			map.put("item.protecting","Please enter the password to use for protecting this item.");
			map.put("item.verify","Please confirm the password for protecting the item.");
			map.put("menu.protect","Protect with a password");
			map.put("menu.unprotect","Remove password protection");
			map.put("protected.title","Item protected");
			map.put("protected.message","Password protection enabled. Subsequent attempts to open this item will require the password.");
			map.put("unprotected.title","Item unprotected");
			map.put("unprotected.message","Password protection was disabled for this item.");

		}
	}

	public PatchMetadata getMetadata() {
		PatchMetadata meta = new PatchMetadata(this);
		fillMetadata(meta);
		return meta;
	}

	private void fillMetadata(PatchMetadata pd) {
		pd.withClass(new PatchableClass(CLASS_DETAILVIEW).withChecksums(
				MD5_DETAILVIEW_510_BEFORE, MD5_DETAILVIEW_510_AFTER));
		pd.withClass(new PatchableClass(CLASS_OPENDETAILSACTION).withChecksums(
				MD5_OPENDETAILSACTION_510_BEFORE,
				MD5_OPENDETAILSACTION_510_AFTER));
		pd.withClass(new PatchableClass(CLASS_GOTODIALOGACTION).withChecksums(
				MD5_GOTODIALOGACTION_510_BEFORE,
				MD5_GOTODIALOGACTION_510_AFTER));
		pd.withClass(new PatchableClass(CLASS_OPENITEMACTION).withChecksums(
				MD5_OPENITEMACTION_510_BEFORE, MD5_OPENITEMACTION_510_AFTER));
	}

	public Permission[] getRequiredPermissions() {
		return new Permission[] { new AllPermission() };
	}

	static String getResource(String key) {
		return instance.localize(key);
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_OPENITEMACTION_510_BEFORE)) {
			return patchItemAction510(clazz);
		}
		if (md5.equals(MD5_OPENDETAILSACTION_510_BEFORE)) {
			return patchDetailsAction510(clazz);
		}
		if (md5.equals(MD5_DETAILVIEW_510_BEFORE)) {
			return patchDetailView510(clazz);
		}
		if (md5.equals(MD5_GOTODIALOGACTION_510_BEFORE)) {
			return patchGotoAction510(clazz);
		}
		return "Unexpected error: unknown MD5 " + md5;
	}

	private void sanitize(Code c) {
		c.calculateMaxLocals();
		c.calculateMaxStack();
	}

	private String patchItemAction510(BCClass clazz)
			throws NoSuchMethodException {
		Code c = clazz.getDeclaredMethod("D").getCode(false);

		c.afterLast();
		Instruction ret = c.previous();
		c.beforeFirst();

		c.aload().setLocal(1);
		c.invokestatic().setMethod(
				PasswordPatch.class.getMethod("onOpenItem",
						new Class[] { Object.class }));
		c.ifeq().setTarget(ret);

		sanitize(c);
		return null;
	}

	private String patchGotoAction510(BCClass clazz) throws Throwable {
		Code c = clazz.getDeclaredMethod("actionPerformed").getCode(false);
		
		c.afterLast();
		Instruction ret = c.previous();
		c.beforeFirst();

		c.invokestatic().setMethod(
				PasswordPatch.class.getMethod("onGotoAction",
						new Class[] {}));
		c.ifeq().setTarget(ret);
		
		sanitize(c);
		return null;
	}

	private String patchDetailsAction510(BCClass clazz) throws Throwable {
		Code c = clazz.getDeclaredMethod("actionPerformed").getCode(false);

		c.after(3);
		c.aload().setLocal(2);
		c.invokestatic().setMethod(
				PasswordPatch.class.getMethod("onShowDetails",
						new Class[] { Object.class }));

		sanitize(c);

		return null;
	}

	private String patchDetailView510(BCClass clazz) throws Throwable {
		Code c = clazz.getDeclaredMethod("createMenuPanel").getCode(false);
		c.beforeFirst();
		c.aload().setLocal(1);
		c.invokestatic().setMethod(
				PasswordPatch.class.getMethod("onCreateMenuPanel",
						new Class[] { Object.class }));
		sanitize(c);
		return null;
	}

	public static boolean onOpenItem(Object o) {
		if (o == null || !(o instanceof CatalogEntry)) {
			log("Unexpected state in onOpenItem(): got argument " + o);
			return true;
		}
		CatalogEntry entry = (CatalogEntry) o;
		String uuid = entry.getUUID().toString();

		return verifyAccess(uuid);
	}

	private static boolean verifyAccess(String uuid) {
		Password encrypted = PasswordStore.get(uuid);

		if (encrypted == null) {
			return true;
		}

		String clear = PasswordDialog.showDialog(PasswordDialog.MODE_OPEN,
				encrypted);
		return clear == null ? false : encrypted.matches(clear);
	}
	
	public static boolean onGotoAction() {
		return verifyAccess(instance.currentKey);
	}

	public static void onShowDetails(Object o) {
		if (o != null && o instanceof CatalogEntry) {
			CatalogEntry c = (CatalogEntry) o;
			instance.currentKey = c.getUUID().toString();
		} else {
			log("Unexpected state in onShowDetails(): got argument " + o);
		}
	}

	public static void onCreateMenuPanel(Object o) {
		if (o == null || !(o instanceof List)) {
			log("Unexpected state in onCreateMenu(): got argument " + o);
			return;
		}
		String key = instance.currentKey;
		if (key == null) {
			log("Unexpected state in onCreateMenu(): no currentKey");
			return;
		}
		List actions = (List) o;

		Password password = PasswordStore.get(key);
		Action injectedAction = null;
		if (password == null) {
			injectedAction = new ProtectAction(key);
		} else {
			injectedAction = new UnprotectAction(key, password);
		}
		if (injectedAction != null) {
			actions.add(injectedAction);
		}
	}

	private static void showDialog(String messageKey, String titleKey) {
		String message = PasswordPatch.getResource(messageKey);
		String title = PasswordPatch.getResource(titleKey);
		ConfirmationDialog.showDialog(PasswordDialog.APP_ID, message, title,
				ConfirmationDialog.OK_ONLY_OPTIONS);
	}

	private static class ProtectAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		private final String key;

		public void actionPerformed(ActionEvent e) {
			String first = PasswordDialog.showDialog(PasswordDialog.MODE_SET,
					null);
			if (first != null) {
				Password passwd = Password.create(first);
				String second = PasswordDialog.showDialog(
						PasswordDialog.MODE_VERIFY, passwd);
				if (passwd.matches(second)) {
					boolean committed = PasswordStore.set(key, passwd);
					if (committed) {
						showDialog("protected.message", "protected.title");
					} else {
						showDialog("error.message", "error.title");
					}
				}
			}
		}

		public ProtectAction(String key) {
			super(PasswordPatch.getResource("menu.protect"));
			this.key = key;
		}

	}

	private static class UnprotectAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		private final String key;
		private final Password encrypted;

		public void actionPerformed(ActionEvent e) {
			String verify = PasswordDialog.showDialog(PasswordDialog.MODE_OPEN,
					encrypted);
			if (verify != null && encrypted.matches(verify)) {
				boolean committed = PasswordStore.remove(key, encrypted);
				if (committed) {
					showDialog("unprotected.message", "unprotected.title");
				} else {
					showDialog("error.message", "error.title");
				}
			}
		}

		public UnprotectAction(String key, Password encrypted) {
			super(PasswordPatch.getResource("menu.unprotect"));
			this.key = key;
			this.encrypted = encrypted;
		}

	}
}
