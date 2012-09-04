package com.mobileread.ixtab.patch.collectioncount;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.amazon.ebook.util.lang.UUID;
import com.amazon.kindle.content.catalog.CatalogEntry;
import com.amazon.kindle.content.catalog.CatalogEntryCollection;
import com.amazon.kindle.content.catalog.CatalogService;
import com.amazon.kindle.content.catalog.CatalogService.QueryResultDepth;
import com.amazon.kindle.content.catalog.CatalogService.QueryResults;
import com.amazon.kindle.content.catalog.CollationCriteria;
import com.amazon.kindle.content.catalog.Predicate;
import com.amazon.kindle.content.catalog.PredicateFactory;
import com.amazon.kindle.restricted.runtime.Framework;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;

public class CollectionCountPatch extends Patch {

	private static final String CLASS = "com.amazon.kindle.apps.content.view.DefaultContentCell";
	public static final String MD5_BEFORE = "c25ca62e95f31a5184cce2fd573be007";
	private static final String MD5_AFTER = "d490aff2bc23c1e53ffbaec770c1162d";
	
	private static final Predicate PRED_COLLECTION = PredicateFactory.equals("type", "Collection");

	public int getVersion() {
		return 20120904;
	}

	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Modify Collection Count Behavior");
			map.put(I18N_JBPATCH_DESCRIPTION, "This patch modifies the way that the collection content is counted and displayed on the home screen. This is only relevant if you are using nested collections. If this patch is enabled, the total number of items reachable from a collection (and its subcollections) is displayed. If it is disabled, only the items directly contained in a collection are counted.");
		}
	}

	public PatchMetadata getMetadata() {
		PatchableClass pc = new PatchableClass(CLASS).withChecksums(MD5_BEFORE,
				MD5_AFTER);
		return new PatchMetadata(this).withClass(pc);
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_BEFORE)) {
			return patchDisplayMethod(clazz);
		}
		return "unsupported MD5: "+md5;
	}

	private String patchDisplayMethod(BCClass clazz) throws Exception {
		Code c = clazz.getDeclaredMethod("D", new Class[] {int.class}).getCode(false);
		c.before(27);
		c.next();
		c.remove();
		c.next();
		c.remove();
		c.invokestatic().setMethod(CollectionCountPatch.class.getMethod("countCollectionMembers", new Class[] {Object.class}));
		
		c.calculateMaxLocals();
		c.calculateMaxStack();
		return null;
	}
	
	public static int countCollectionMembers(Object collection) {
		CatalogEntryCollection c = (CatalogEntryCollection) collection;
		if (c.getMemberCount() == 0) {
			return 0;
		}
		int count = countRecursive(new UUID[] {c.getUUID()}, new ArrayList());
		return count == 0 ? c.getMemberCount() : count;
	}
	
	private static int countRecursive(UUID[] uuids, Collection seen) {
		// uuids are items; entries will "filter" these uuids to only collections.
		for (int i=0; i < uuids.length; ++i) {
			seen.add(uuids[i]);
		}
		CatalogEntry[] collections = getEntries(uuids);
		if (collections == null) {
			return 0;
		}
		int books = uuids.length - collections.length;
		
		List entries = new ArrayList();
		
		for (int i=0; i < collections.length; ++i) {
			CatalogEntryCollection collection = (CatalogEntryCollection) collections[i];
			UUID[] children = collection.getMembers();
			if (children != null) {
				for (int c=0; c < children.length; ++c) {
					UUID child = children[c];
					if (!seen.contains(child) && !entries.contains(child)) {
						entries.add(child);
					}
				}
			}
		}
		
		if (!entries.isEmpty()) {
			UUID[] children = new UUID[entries.size()];
			for (int i=0; i < children.length; ++i) {
				children[i] = (UUID) entries.get(i);
			}
			books += countRecursive(children, seen);
		}
		return books;
	}
	
	private static CatalogEntry[] getEntries(UUID[] uuids) {
		Predicate predicate = PredicateFactory.inList("uuid", uuids);
		predicate = PredicateFactory.and(new Predicate[] {predicate, PRED_COLLECTION});
		
		Results r = new Results();
		synchronized (r) {
			((CatalogService) Framework.getService(CatalogService.class)).find(predicate, CollationCriteria.SORT_BY_MOST_RECENT, uuids.length, 0, r, QueryResultDepth.FULL);
			while (!r.done) {
				try {
					r.wait();
				} catch (InterruptedException e) {
					
				}
			}
		}
		return r.entries;
	}
	
	private static class Results implements QueryResults {
		
		private CatalogEntry[] entries = new CatalogEntry[0];
		private volatile boolean done = false;
		public void queryComplete(boolean arg0, int arg1, int arg2,
				CatalogEntry[] results, Map arg4) {
			synchronized (this) {
				if (results != null && results.length > 0) {
					entries = new CatalogEntry[results.length];
					System.arraycopy(results, 0, entries, 0, results.length);
				}
				done = true;
				this.notifyAll();
			}
		}
		
	}
}
