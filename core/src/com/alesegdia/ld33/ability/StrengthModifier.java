package com.alesegdia.ld33.ability;

public class StrengthModifier {
	
	public StrengthModifier(String name, float power) {
		this.name = name; this.power = power;
	}
	
	public String name;
	public float power;
	
	public static int GetUsesForMod(StrengthModifier strmod) {
		int i = (int) (6 - strmod.power);
		return i;
	}
}