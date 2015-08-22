package com.alesegdia.ld33;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.alesegdia.ld33.ability.AbilityGenerator;
import com.alesegdia.ld33.ability.AbilityInstance;
import com.alesegdia.ld33.ability.models.AttackModel;
import com.alesegdia.ld33.ability.models.HazardModel;
import com.alesegdia.ld33.ability.models.ProtectionModel;

public class GameCharacter {
	
	Stats stats = new Stats();
	
	public GameCharacter() {
		for( int i = 0; i < ElementType.NUM_ELEMENTS; i++ ) {
			haunts[i] = null;
			chants[i] = null;
		}
	}
	
	public static final int NUM_ABILITIES = 6;
	public AbilityInstance[] abilities = new AbilityInstance[NUM_ABILITIES];
	float xp = 96;
	float level;
	float nextLevelXP = 100;

	int levelsGainedLastBattle;

	public StatusEffect[] chants = new StatusEffect[ElementType.NUM_ELEMENTS];
	public StatusEffect[] barriers = new StatusEffect[ElementType.NUM_ELEMENTS];
	public StatusEffect[] auras = new StatusEffect[ElementType.NUM_ELEMENTS];

	public StatusEffect[] growls = new StatusEffect[ElementType.NUM_ELEMENTS];
	public StatusEffect[] shades = new StatusEffect[ElementType.NUM_ELEMENTS];
	public StatusEffect[] haunts = new StatusEffect[ElementType.NUM_ELEMENTS];

	float currentHP = 0;
	public String name;

	public static AbilityInstance GenAbilityForChar( GameCharacter gc ) {
		AbilityInstance ai;
		List<Integer> affinities = new ArrayList<Integer>();
		for( int i = 0; i < ElementType.NUM_ELEMENTS; i++ ) {
			if( gc.stats.affinity[i] != 0 ) {
				affinities.add(i);
			}
		}

		ai = AbilityGenerator.MakeRandomAbility();
		int element = RNG.rng.nextInt(affinities.size());
		ai.element = affinities.get(element);
		return ai;
	}

	public void checkList( StatusEffect[] list ) {
		for( int i = 0; i < ElementType.NUM_ELEMENTS; i++ ) {
			if( list[i] != null ) {
				list[i].turns--;
				if( list[i].turns <= 0 ) {
					list[i] = null;
				}
			}
		}
	}
	
	public void checkStatusEffects() {
		checkList(chants);
		checkList(barriers);
		checkList(auras);
		
		checkList(haunts);
		checkList(shades);
		checkList(growls);
	}
	
	public int abilityReceived( GameCharacter caster, AbilityInstance ai ) {
		float finalDmg = 0;
		System.out.println("CAST!");
		ai.uses--;
		if( ai.model instanceof HazardModel ) {
			HazardModel hm = (HazardModel) ai.model;
			StatusEffect se = new StatusEffect();
			se.ai = ai;
			se.turns = (int) (hm.basePower + ai.strMod.power);
			switch(hm.hazardType) {
			case GROWL:		growls[ai.element] = se; break;
			case SHADE:		shades[ai.element] = se; break;
			case HAUNT: 	haunts[ai.element] = se; break;
			}
		} else if( ai.model instanceof ProtectionModel ) {
			ProtectionModel pm = (ProtectionModel) ai.model;
			StatusEffect se = new StatusEffect();
			se.ai = ai;
			se.turns = (int) (pm.basePower + ai.strMod.power);
			switch(pm.protectionType) {
			case CHANT:		chants[ai.element] = se; break;
			case BARRIER: 	barriers[ai.element] = se; break;
			case AURA: 		auras[ai.element] = se; break;
			}
		} else if( ai.model instanceof AttackModel ) {
			AttackModel am = (AttackModel) ai.model;
			System.out.println(caster);
			System.out.println(this);
			float defMod = 0f;
			float atkMod = 1f;
			
			float casterAfi = caster.stats.affinity[ai.element];

			for( int i = 0; i < ElementType.NUM_ELEMENTS; i++ ) {
				float thisAffinity = this.stats.affinity[i];
				System.out.println("THISAFI: " + thisAffinity);
				float mod = AbilityGenerator.multipliers[ai.element][i];
				System.out.println("MOD: " + mod);
				if( thisAffinity > 0 ) {
					if( mod < 1 ) {
						defMod += mod * thisAffinity;
					} else if( mod > 1 ) {
						atkMod += mod * thisAffinity;
					}
				}
				System.out.println("ATKMOD: " + atkMod);
				System.out.println("DEFMOD: " + defMod);
				System.out.println("-----------");
			}
			float finalMod = casterAfi * atkMod - defMod;

			System.out.println("CASTERAFI: " + casterAfi);
			System.out.println("FINALMOD: " + finalMod);
			System.out.println("ABILITY BASEPWR: " + am.basePower);
			System.out.println("STRMOD BASEPWR: " + ai.strMod.power);
			float basePwr = (am.basePower + ai.strMod.power);
			System.out.println("FINALBASEPWR: " + basePwr);
			float baseDmg = basePwr * finalMod;
			System.out.println("FINALBASEDMG: " + baseDmg);
			// deal damage
			switch( am.attackType ) {
			case MGK:
				// x1.5, x0.5
				finalDmg = baseDmg * (
						caster.stats.stats[Stats.MGKATK] / this.stats.stats[Stats.MGKDEF] * 1.2f +
						caster.stats.stats[Stats.PHSATK] / this.stats.stats[Stats.PHSDEF] * 0.8f) / 2f;
				break;
			case MASH:
				// x1.5, x0.5
				finalDmg = baseDmg * (
						caster.stats.stats[Stats.MGKATK] / this.stats.stats[Stats.MGKDEF] * 0.8f +
						caster.stats.stats[Stats.PHSATK] / this.stats.stats[Stats.PHSDEF] * 1.2f) / 2f;
				break;
			case CUT:
				// x1, x1
				finalDmg = baseDmg * (
						caster.stats.stats[Stats.MGKATK] / this.stats.stats[Stats.MGKDEF] * 1f +
						caster.stats.stats[Stats.PHSATK] / this.stats.stats[Stats.PHSDEF] * 1f) / 2f;
				break;
			}
			System.out.println("FINALDMG: " + finalDmg);
			float lvlMod = caster.level / (this.level * 0.8f);
			System.out.println("LVLMOD: " + lvlMod);
			finalDmg *= lvlMod;
			System.out.println("FINALDMG WITHLVL: " + finalDmg);
			//dealDamage(finalDmg);
		}
		return (int) (finalDmg * 0.2f);
	}
	
	void dealDamage(float finalDmg) {
		this.currentHP -= finalDmg;
		System.out.println("HP: " + currentHP);
		System.out.println("FinalDMG: " + finalDmg);
	}

	public void killedChar( GameCharacter gc ) {
		float xpGained = gc.level / this.level * 10f;
		addXP(xpGained);
	}
	
	public boolean addXP(float qtt) {
		xp += qtt;
		if( nextLevelXP <= xp ) {
			xp = xp - nextLevelXP;
			nextLevelXP = 100;
			return true;
		} else {
			return false;
		}
	}
	
	void levelUp() {
		levelsGainedLastBattle++;
	}

	public String toString() {
		String str = "\n";
		for( int i = 0; i < NUM_ABILITIES; i++ ) {
			str = str + abilities[i] + "\n";
		}
		return stats.toString() + str;
	}

	public static GameCharacter genChar( String string, int level ) {
		GameCharacter c = new GameCharacter();
		c.name = string;
		c.level = level;
		int numPoints = level * 10;
		
		for( int i = 0; i < Stats.NUM_STATS; i++ ) {
			c.stats.stats[i] = 1;
		}
		
		for( int i = 0; i < numPoints; i++ ) {
			int stat = RNG.rng.nextInt(Stats.NUM_STATS);
			c.stats.stats[stat]++;
		}
		
		c.stats.stats[Stats.HP] *= 10;
		c.currentHP = c.stats.stats[Stats.HP];
		
		for( int i = 0; i < numPoints / 5; i++ ) {
			int stat = RNG.rng.nextInt(ElementType.NUM_ELEMENTS);
			c.stats.affinity[stat]++;
		}
		
		for( int i = 0; i < NUM_ABILITIES; i++ ) {
			c.abilities[i] = GenAbilityForChar(c);
		}
		return c;
	}

	public boolean isDead() {
		return currentHP <= 0;
	}

	public float getXpByKilling(GameCharacter enemy) {
		float xpmult = enemy.level / this.level;
		return 10 * xpmult;
	}
	
}