package com.mobileread.ixtab.jbpatch.ui.validate;

public class IntegerInputValidator implements InputValidator {

	private final Integer minimum;
	private final Integer maximum;
	
	
	public IntegerInputValidator() {
		this(null, null);
	}
	
	public IntegerInputValidator(int minimum) {
		this(Integer.valueOf(minimum), null);
	}
	
	public IntegerInputValidator(int minimum, int maximum) {
		this(Integer.valueOf(minimum), Integer.valueOf(maximum));
	}
	
	private IntegerInputValidator(Integer minimum, Integer maximum) {
		super();
		this.minimum = minimum;
		this.maximum = maximum;
	}


	public boolean isValid(String input) {
		if (input == null || input.length() < 1) {
			return false;
		}
		try {
			int number = Integer.parseInt(input);
			if (minimum != null && number < minimum.intValue()) {
				return false;
			}
			if (maximum != null && number > maximum.intValue()) {
				return false;
			}
			return true;
		} catch (Throwable t) {
			return false;
		}
		
	}

}
