package com.alesegdia.ld33.ability.models;

public class AttackModel extends AbilityModel {

	public AttackModel(String name, AttackModel.AttackType atkType, int basePwr) {
		this.name = name;
		this.attackType = atkType;
		this.basePower = basePwr;
	}

	public static enum AttackType {
		MGK,	// x1.5 affinity + 0.5 physic
		MASH, 	// x0.5 affinity + x1.5 physic
		CUT		// x1 affinity + x1 physic
	}
	public AttackModel.AttackType attackType;
}