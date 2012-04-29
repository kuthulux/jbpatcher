package com.mobileread.ixtab.jbpatch;

public final class Descriptor {

	public final String className;
	private String[] md5Sums;

	public Descriptor(String className, String[] md5Sums) {
		super();
		this.className = validate(className);
		this.md5Sums = validate(md5Sums);
	}

	private String validate(String className) {
		if (className == null) {
			throw new NullPointerException("class name must not be null");
		}
		return className;
	}
	
	private String[] validate(String[] md5Sums) {
		if (md5Sums == null || md5Sums.length == 0) {
			throw new IllegalArgumentException("at least one MD5 sum must be provided");
		}
		String[] copy = new String[md5Sums.length];
		System.arraycopy(md5Sums, 0, copy, 0, copy.length);
		return copy;
	}


	final boolean matches(String md5) {
		for (int i = 0; i < md5Sums.length; ++i) {
			if (md5.equals(md5Sums[i])) {
				return true;
			}
		}
		return false;
	}

}