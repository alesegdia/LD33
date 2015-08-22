package com.alesegdia.ld33.ability.models;

public class HazardModel extends AbilityModel {
	public static enum HazardType {
		GROWL,	// decreases affinity for mgkatk
		SHADE,	// decreases affinity for mgkdef
		HAUNT 	// provokes effect
	}
	
	public HazardModel(String name, HazardModel.HazardType ht ) {
		this.name = name;
		this.basePower = 1;
		this.hazardType = ht;
	}
	
	public HazardModel.HazardType hazardType;
}