package com.mobileread.ixtab.patch.passwd;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.security.AllPermission;
import java.security.Permission;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import serp.bytecode.BCClass;
import serp.bytecode.Code;
import serp.bytecode.Instruction;

import com.amazon.agui.swing.ConfirmationDialog;
import com.amazon.kindle.content.catalog.CatalogEntry;
import com.mobileread.ixtab.jbpatch.Descriptor;
import com.mobileread.ixtab.jbpatch.Patch;

public class PasswordPatch extends Patch {

	public static final String MD5_OPENITEMACTION_510 = "4fb5c0bc58d97807f3c589452f0c26a6";
	public static final String MD5_OPENDETAILSACTION_510 = "689382577ea0cd5e8bb4c452c4f0fcb9";
	public static final String MD5_DETAILVIEW_510 = "43b9d37a3eabb49728ef2021ff3a9e31";

	private static PasswordPatch instance;

	private String currentKey = null;

	public PasswordPatch() {
		instance = this;
	}

	protected Descriptor[] getDescriptors() {
		return new Descriptor[] {
				new Descriptor("com.amazon.kindle.home.action.OpenItemAction",
						new String[] { MD5_OPENITEMACTION_510 }),
				new Descriptor(
						"com.amazon.kindle.home.action.OpenDetailsAction",
						new String[] { MD5_OPENDETAILSACTION_510 }),
				new Descriptor("com.amazon.kindle.swing.DetailView",
						new String[] { MD5_DETAILVIEW_510 }),

		};
	}

	public Permission[] getRequiredPermissions() {
		return new Permission[] { new AllPermission() };
	}

	protected URL getResourcesUrl() {
		return this.getClass().getResource("/ixtab-patch-passwd.txt");
	}

	static String getResource(String key) {
		return instance.get(key);
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_OPENITEMACTION_510)) {
			return patchItemAction510(clazz);
		}
		if (md5.equals(MD5_OPENDETAILSACTION_510)) {
			return patchDetailsAction510(clazz);
		}
		if (md5.equals(MD5_DETAILVIEW_510)) {
			return patchDetailView510(clazz);
		}
		return "Unexpected error: unknown MD5 " + md5;
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

	private String patchDetailsAction510(BCClass clazz) throws Throwable {
		Code c = clazz.getDeclaredMethod("actionPerformed").getCode(false);

		c.after(3);
		c.aload().setLocal(2);
		c.invokestatic().setMethod(
				PasswordPatch.class.getMethod("onShowDetails",
						new Class[] { Object.class }));

		sanitize(c);

		// injectDialogHooks(c, new int[] {72, 120});
		//
		// c = clazz.getDeclaredMethod("B").getCode(false);
		// injectDialogHooks(c, new int[] {52});
		// sanitize(c);
		//
		// c = clazz.getDeclaredMethod("D").getCode(false);
		// injectDialogHooks(c, new int[] {36});
		// sanitize(c);

		return null;
	}

	private void sanitize(Code c) {
		c.calculateMaxLocals();
		c.calculateMaxStack();
	}

	// private void injectDialogHooks(Code c, int[] indexes) throws Throwable {
	// /* The indexes refer to the original code. Because
	// * they change as we perform modifications, we retrieve the
	// * instructions before actually tampering with the code.
	// */
	// Instruction[] instructions = new Instruction[indexes.length];
	// for (int i=0; i < indexes.length; ++i) {
	// c.before(indexes[i]);
	// instructions[i] = c.next();
	// }
	//
	// for (int i=0; i < instructions.length; ++i) {
	// c.after(instructions[i]);
	// c.dup();
	// c.invokestatic().setMethod(PasswordPatch.class.getMethod("onShowDetails",
	// new Class[] {Object.class}));
	// }
	// }

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

		Password encrypted = PasswordStore.get(entry.getUUID().toString());

		if (encrypted == null) {
			return true;
		}

		String clear = PasswordDialog.showDialog(PasswordDialog.MODE_OPEN,
				encrypted);
		return clear == null ? false : encrypted.matches(clear);
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
			// actions.add(0, injectedAction);
			actions.add(injectedAction);
		}
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
					if (!committed) {
						String message = PasswordPatch
								.getResource("error.message");
						String title = PasswordPatch.getResource("error.title");
						ConfirmationDialog.showDialog(PasswordDialog.APP_ID,
								message, title,
								ConfirmationDialog.OK_ONLY_OPTIONS);
					} else {
						String message = PasswordPatch
								.getResource("protected.message");
						String title = PasswordPatch
								.getResource("protected.title");
						ConfirmationDialog.showDialog(PasswordDialog.APP_ID,
								message, title,
								ConfirmationDialog.OK_ONLY_OPTIONS);
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
				if (!committed) {
					String message = PasswordPatch.getResource("error.message");
					String title = PasswordPatch.getResource("error.title");
					ConfirmationDialog.showDialog(PasswordDialog.APP_ID,
							message, title, ConfirmationDialog.OK_ONLY_OPTIONS);
				} else {
					String message = PasswordPatch
							.getResource("unprotected.message");
					String title = PasswordPatch
							.getResource("unprotected.title");
					ConfirmationDialog.showDialog(PasswordDialog.APP_ID,
							message, title, ConfirmationDialog.OK_ONLY_OPTIONS);
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
