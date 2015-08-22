package com.alesegdia.ld33.ability;

import java.util.ArrayList;
import java.util.List;

import com.alesegdia.ld33.ElementType;
import com.alesegdia.ld33.RNG;
import com.alesegdia.ld33.ability.models.AbilityModel;
import com.alesegdia.ld33.ability.models.AttackModel;
import com.alesegdia.ld33.ability.models.HazardModel;
import com.alesegdia.ld33.ability.models.ProtectionModel;
import com.alesegdia.ld33.ability.models.AttackModel.AttackType;
import com.alesegdia.ld33.ability.models.HazardModel.HazardType;
import com.alesegdia.ld33.ability.models.ProtectionModel.ProtectionType;

public class AbilityGenerator {

	static List<AttackModel> attacks = new ArrayList<AttackModel>();
	static List<HazardModel> hazards = new ArrayList<HazardModel>();
	static List<ProtectionModel> protections = new ArrayList<ProtectionModel>();
	
	static List<StrengthModifier> strMods = new ArrayList<StrengthModifier>();
	
	static List<AbilityModel> abilityModels = new ArrayList<AbilityModel>();
	
	public static void InitAbilities() {
		
		attacks.add(new AttackModel("Shard ", AttackType.MGK, 1));
		attacks.add(new AttackModel("Breath ", AttackType.MGK, 2));
		attacks.add(new AttackModel("Crystal ", AttackType.MGK, 3));
		
		attacks.add(new AttackModel("Tackle ", AttackType.MASH, 1));
		attacks.add(new AttackModel("Smash ", AttackType.MASH, 2));
		attacks.add(new AttackModel("Slam ", AttackType.MASH, 3));
		attacks.add(new AttackModel("Cut ", AttackType.CUT, 1));
		attacks.add(new AttackModel("Slash ", AttackType.CUT, 2));
		attacks.add(new AttackModel("Razor ", AttackType.CUT, 3));
		
		protections.add(new ProtectionModel("Aura ", ProtectionType.AURA));
		//protections.add(new ProtectionModel("Chant ", ProtectionType.CHANT));
		protections.add(new ProtectionModel("Barrier ", ProtectionType.BARRIER));

		hazards.add(new HazardModel("Growl ", HazardType.GROWL));
		hazards.add(new HazardModel("Shade ", HazardType.SHADE));
		//hazards.add(new HazardModel("Haunt ", HazardType.HAUNT));
		
		abilityModels.addAll(attacks);
		abilityModels.addAll(protections);
		abilityModels.addAll(hazards);
		
		strMods.add(new StrengthModifier("", 1));
		strMods.add(new StrengthModifier("Super ", 2));
		strMods.add(new StrengthModifier("Mega ", 3));
		strMods.add(new StrengthModifier("Ultra ", 4));
		strMods.add(new StrengthModifier("Hyper ", 5));

	}

	public static AbilityInstance MakeRandomAbility() {
		int model = RNG.rng.nextInt(abilityModels.size());
		int mod = RNG.rng.nextInt(strMods.size());
		int effect = RNG.rng.nextInt(ElementType.NUM_ELEMENTS);
		AbilityModel abModel = abilityModels.get(model);
		StrengthModifier strMod = strMods.get(mod);
		return new AbilityInstance(abModel, strMod, effect);
	}
	
	

	// ElementA
	// 			 ElementB
	// ElementB attacks ElementA.
	
	public static final float M15 = 1.2f;
	public static final float M20 = 1.5f;
	public static final float M05 = 0.8f;
	
	
	public static float[][] multipliers = {
			// 1 x 1.5f, 1 x 0.5f, 1 x 2f
			// 	SACRED	EVIL	FIRE	ICE		VENOM	NORMAL
	/*OK*/	{ 	1f,		M20, 	M15,	1f,		1f, 	M05		}, // SACRED
	/*OK*/	{ 	M05, 	M15,	M20,	1, 		1f,		1f		}, // EVIL
	/*OK*/	{ 	1f,		M05,	1f,		M20,	M15,	1f		}, // FIRE
	/*OK*/	{ 	M15,	1f,		M05,	1f,		M20,	1f		}, // ICE
			{ 	1f,		1f,		1, 		M15,	M05,	M20		}, // VENOM
	/*OK*/	{ 	M20,	1f,		1, 		M05,	1f,		M15	}, // NORMAL
	}; 			/*OK*/	/*OK*/ 	/*OK*/ 			/*OK*/	/*OK*/

}
