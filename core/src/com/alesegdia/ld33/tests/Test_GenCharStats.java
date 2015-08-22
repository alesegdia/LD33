package com.alesegdia.ld33.tests;

import com.alesegdia.ld33.GameCharacter;
import com.alesegdia.ld33.RNG;
import com.alesegdia.ld33.ability.AbilityGenerator;

public class Test_GenCharStats {
	
	public static void main( String[] args ) {
		RNG.rng = new RNG();
		AbilityGenerator.InitAbilities();
		for( int i = 0; i < 10; i++ ) {
			GameCharacter c = GameCharacter.genChar("c",2);
			System.out.println(c);
			System.out.println("=======================");
		}
	}

}
