package com.alesegdia.ld33.ability;

import com.alesegdia.ld33.ElementType;
import com.alesegdia.ld33.ability.models.AbilityModel;

public class AbilityInstance {

	public AbilityModel model;
	public StrengthModifier strMod;
	public int element;
	public int uses;

	public AbilityInstance( AbilityModel model, StrengthModifier strmod, int element ) {
		this.model = model;
		this.strMod = strmod;
		this.element = element;
		this.uses = StrengthModifier.GetUsesForMod(strmod);
	}
	
	public String toString() {
		return onlyAbility() + "x " + uses;
	}

	public String onlyAbility() {
		return strMod.name + ElementType.GetNameForElement(element) + model.name;
	}
	
}
