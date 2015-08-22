package com.alesegdia.ld33.tests;

import com.alesegdia.ld33.GameCharacter;
import com.alesegdia.ld33.RNG;
import com.alesegdia.ld33.ability.AbilityGenerator;

public class Test_Game {

	public static void main(String[] args) {
		RNG.rng = new RNG();
		AbilityGenerator.InitAbilities();
		
		GameCharacter player = GameCharacter.genChar("p",5);
		GameCharacter enemy = GameCharacter.genChar("e",5);
		System.out.print(player);
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.print(enemy);
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println(enemy.abilities[0]);
		player.abilityReceived(enemy, enemy.abilities[0]);
	}
	
}
