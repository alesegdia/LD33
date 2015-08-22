package com.alesegdia.ld33.tests;

import com.alesegdia.ld33.RNG;
import com.alesegdia.ld33.ability.AbilityGenerator;

public class Test_GenAbilities {

	public static void main(String[] args) {
		AbilityGenerator.InitAbilities();
		RNG.rng = new RNG();
		for( int i = 0; i < 50; i++ ) {
			System.out.println(AbilityGenerator.MakeRandomAbility());
		}
	}

}
