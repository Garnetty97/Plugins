package com.gmail.garnetyeates.pvpplugin.bulletarrows;

public enum RomanNumeral {
	
	I(1), II(2), III(3), IV(4), V(5), VI(6), VII(7), VIII(8), IX(9), X(10), XI(11), XII(12), XIII(13), XIV(14), XV(15);
	
	private int number;
	
	private RomanNumeral(int number) {
		this.number = number;
	}
	
	public int getAssociatedNumber() {
		return number;
	}
	
	public static RomanNumeral fromNumber(int number) {
		for (RomanNumeral numeral : RomanNumeral.values()) {
			if (numeral.getAssociatedNumber() == number) return numeral;
		} return null;
	}
	
	public static RomanNumeral fromString(String s) {
		for (RomanNumeral numeral : RomanNumeral.values()) {
			if (numeral.toString().equalsIgnoreCase(s)) {
				return numeral;
			}
		} return null;
	}
	
}
