package com.mobileread.ixtab.patch.passwd;

import java.io.Serializable;

import com.amazon.ebook.util.lang.UUID;
import com.mobileread.ixtab.jbpatch.MD5;

public class Password implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String salt;
	private String encrypted;
	
	public boolean matches(String clear) {
		return clear == null ? false : encrypt(clear).equals(encrypted);
	}

	private String encrypt(String clear) {
		return MD5.getMd5String((salt+clear).getBytes());
	}
	

	private Password() {
	}
	
	public static Password create(String clearText) {
		Password p = new Password();
		p.salt = randomSalt();
		p.encrypted = p.encrypt(clearText);
		return p;
	}

	private static String randomSalt() {
		return MD5.getMd5String(new UUID().toString().getBytes());
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((encrypted == null) ? 0 : encrypted.hashCode());
		result = prime * result + ((salt == null) ? 0 : salt.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Password other = (Password) obj;
		if (encrypted == null) {
			if (other.encrypted != null)
				return false;
		} else if (!encrypted.equals(other.encrypted))
			return false;
		if (salt == null) {
			if (other.salt != null)
				return false;
		} else if (!salt.equals(other.salt))
			return false;
		return true;
	}
	
	
}
