package com.alesegdia.ld33.ability.models;

public class ProtectionModel extends AbilityModel {
	
	public static enum ProtectionType {
		AURA, 	// enhances affinity for mgkatk
		CHANT, 	// heals effect or heal yourself (normal)
		BARRIER // enhances affinity for mgkdef
	}
	
	public ProtectionType protectionType;
	
	public ProtectionModel( String name, ProtectionModel.ProtectionType ht ) {
		this.basePower = 1;
		this.name = name;
		this.protectionType = ht;
	}
}