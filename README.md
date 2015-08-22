# LD33

# Game rules

## Elements

There are 6 elements in the game: Sacred, Evil, Fire, Ice, Venom and Normal (heh, sounds familiar?). Each element has weakness and enforcements from and for other elements. In the next table, you can check strengths and weaknesses

· | S| E| F| I| V| M
--|--|--|--|--|--|--
 S|  |VW| W|  |  | S
 E| S| W|VW|  |  |  
 F|  | S|  |VW| W|  
 I| W|  | S|  |VW|  
 V|  |  |  | W| S|VW
 M|VW|  |  | S|  | W

 Game characters can have affinity to any element, none of them exclusive, so one character can have affinity even to all elements.

## Abilities

There are 3 kinds of abilities: **attacks**, **benefits** and **ailments**. Attacks are just abilities that deal damage, depending on the strength of the attack, stats and benefits/ailments. 

### Modifiers

Each ability will have a modifier that will determine its uses and bias its strength. More strength, less uses and so.

1 |     2 |    3 |     4 |     5
--|-------|------|-------|------
  | Super | Mega | Ultra | Hyper


### Attacks

An attack damage will be computed from its caster affinity to that element and its stats, and the receiver affinity and its stats. There are 3 kinds of attacks:

* Magic: influenced by magic stats
* Cuts: influenced by magic and physic stats at equal
* Mash: more influenced by physic stats

There are also 3 attacks of each type, depending on its base strength:

Power | Magic   | Cut   | Mash
------|---------|-------|------
1     | Shard   | Cut   | Tackle
2     | Breath  | Slash | Smash
3     | Crystal | Razor | Slam

### Benefits and ailments

Benefits and ailments can be categorized as follow:

·       | Attack bias | Defense bias | Special effect
--------|-------------|--------------|----------------
Benefit | Aura 		  | Barrier      | Chant
Ailment | Growl       | Shade        | Haunt

**Chants** and **haunts** have not still been coded. **Attack bias** means that it will influence the caster affinity when attacks, and **defense bias** will influence the receiver affinity when attacked.

