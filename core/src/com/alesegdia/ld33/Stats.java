package com.alesegdia.ld33;

public class Stats {
	
	public static final int HP = 0;
	public static final int PHSATK = 1;
	public static final int PHSDEF = 2;
	public static final int MGKATK = 3;
	public static final int MGKDEF = 4;
	public static final int SPEED = 5;
	public static final int NUM_STATS = 6;
	
	public float[] stats = new float[NUM_STATS];
	public float[] affinity = new float[ElementType.NUM_ELEMENTS];
	
	public Stats() {
		this.stats[HP] = 10;
	}
	
	public String toString() {
		return
				"STATS" + "\n" +
				"HP: " + ((int)stats[HP]) + "\n" +
				"phsAtk: " + ((int)stats[PHSATK]) + "\n" +
				"phsDef: " + ((int)stats[PHSDEF]) + "\n" +
				"mgkAtk: " + ((int)stats[MGKATK]) + "\n" +
				"mgkDef: " + ((int)stats[MGKDEF]) + "\n" +
				"speed: " + ((int)stats[SPEED]) + "\n\n" +
				"AFFINITIES\n" +
				"Sacred: " + ((int)affinity[ElementType.SACRED]) + "\n" +
				"Evil: " + ((int)affinity[ElementType.EVIL]) + "\n" +
				"Fire: " + ((int)affinity[ElementType.FIRE]) + "\n" +
				"Ice: " + ((int)affinity[ElementType.ICE]) + "\n" +
				"Venom: " + ((int)affinity[ElementType.VENOM]) + "\n" +
				"Normal: " + ((int)affinity[ElementType.NORMAL]) + "\n";
	}
	
	public String statsStringSelected( int selected ) {
		return (selected==0?"> ":"") + "HP: " + ((int)stats[HP]) + "\n" +
				(selected==1?"> ":"") + "phsAtk: " + ((int)stats[PHSATK]) + "\n" +
				(selected==2?"> ":"") + "phsDef: " + ((int)stats[PHSDEF]) + "\n" +
				(selected==3?"> ":"") + "mgkAtk: " + ((int)stats[MGKATK]) + "\n" +
				(selected==4?"> ":"") + "mgkDef: " + ((int)stats[MGKDEF]) + "\n" +
				(selected==5?"> ":"") + "speed: " + ((int)stats[SPEED]) + "\n\n";
	}
	
	public String affinitiesStringSelected( int selected ) {
		return (selected==0?"> ":"") + "Sacred: " + ((int)affinity[ElementType.SACRED]) + "\n" +
				(selected==1?"> ":"") + "Evil: " + ((int)affinity[ElementType.EVIL]) + "\n" +
				(selected==2?"> ":"") + "Fire: " + ((int)affinity[ElementType.FIRE]) + "\n" +
				(selected==3?"> ":"") + "Ice: " + ((int)affinity[ElementType.ICE]) + "\n" +
				(selected==4?"> ":"") + "Venom: " + ((int)affinity[ElementType.VENOM]) + "\n" +
				(selected==5?"> ":"") + "Normal: " + ((int)affinity[ElementType.NORMAL]) + "\n";

	}
	
	
}