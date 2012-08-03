package com.mobileread.ixtab.jbpatch.conf.ui;

public class SettingEntry {
	public final String key;
	public final String displayValue;
	
	
	public SettingEntry(String key, String displayValue) {
		super();
		this.key = key;
		this.displayValue = displayValue;
	}

	public String toString() {
		return displayValue;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((displayValue == null) ? 0 : displayValue.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SettingEntry other = (SettingEntry) obj;
		if (displayValue == null) {
			if (other.displayValue != null)
				return false;
		} else if (!displayValue.equals(other.displayValue))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	
	
}
