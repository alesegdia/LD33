package com.alesegdia.ld33;

public class ElementType {
	
	public static final int SACRED = 0;
	public static final int EVIL = 1;
	public static final int FIRE = 2;
	public static final int ICE = 3;
	public static final int VENOM = 4;
	public static final int NORMAL = 5;
	public static final int NUM_ELEMENTS = 6;

	public static String GetNameForElement(int element) {
		if( element == SACRED ) return "Sacred ";
		if( element == EVIL ) return "Evil ";
		if( element == FIRE ) return "Fire ";
		if( element == ICE ) return "Ice ";
		if( element == VENOM ) return "Veno ";
		if( element == NORMAL ) return "";
		return "";
	}

}