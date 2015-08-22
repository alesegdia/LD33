package com.alesegdia.ld33;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.alesegdia.ld33.ability.AbilityGenerator;
import com.alesegdia.ld33.ability.AbilityInstance;
import com.alesegdia.ld33.ability.models.AttackModel;
import com.alesegdia.ld33.ability.models.HazardModel;
import com.alesegdia.ld33.ability.models.ProtectionModel;
import com.alesegdia.ld33.assets.Gfx;
import com.alesegdia.ld33.assets.Sfx;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;

public class GdxGame extends ApplicationAdapter {

	private TiledMap map;
	private TiledMapRenderer renderer;
	private OrthographicCamera camera;
	private BitmapFont font;
	private BitmapFont warningFont;
	private SpriteBatch batch;
	private SpriteBatch sprBatch;
	BitmapFont customFont;
	
	ShapeRenderer srend;
	
	int currentLevel = 0;
	
	GameCharacter player;
	GameCharacter enemy;
	
	Color innerBoxColor;
	Color outerBoxColor;
	
	String helpMain =
			"Show/Hide help - H\n" +
			"Show/Hide stats - S\n" +
			"Roll char - R\n" +
			"Start! - SPACE";
	
	String helpBattle = 
			"Show/Hide help - H\n" +
			"Show/Hide stats - S\n" +
			"Check status effects - E" +
			"Select option - SPACE";
			
	String help;
	
	public interface IState {
		public void init();
		public boolean step();
		public void render();
		public void exit();
	}
	
	Stack<IState> stateStack = new Stack<IState>();
	private Color healthBarColor;
	private Color xpBarColor;
	
	boolean showStatusEffects = false;
	
	public void addBattleBeginState() {
		stateStack.push(new IState(){

			@Override
			public void init() {
				player.currentHP = player.stats.stats[Stats.HP];
				playerTurn = true;
				enemy = GameCharacter.genChar("Wizard", (int) player.level);
				addTurnStep(true);
				addInfoState("A wizard got in your way!");
			}

			@Override
			public boolean step() {
				help = helpBattle;
				return false;
			}

			@Override
			public void render() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void exit() {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void addMainState() {
		stateStack.push(new IState(){

			@Override
			public boolean step() {

				checkHelp();
				//checkSelectAbilityInput();
				checkGen();
				checkStats();
				
				boolean ret = true;
				if( checkKey(Input.Keys.SPACE) ) {
					addBattleBeginState();
					ret = false;
				}
				return ret;
			}

			@Override
			public void init() {
				helpOn = true;
				statsOn = true;
				player.currentHP = player.stats.stats[Stats.HP];
				stateStack.clear();
				help = helpMain;

				Sfx.introMusic.stop();
				
				if( !Sfx.battleMusic.isPlaying() ) {
					Sfx.battleMusic.setVolume(0.8f);
					Sfx.battleMusic.play();
				}

			}

			@Override
			public void render() {
				drawMainBox(abilitiesText);
			}

			@Override
			public void exit() {
				enemy = GameCharacter.genChar("Wizard", (int) player.level);				
			}
			
		});
	}
	@Override
	public void create () {
		
		Gfx.Initialize();
		Sfx.Initialize();
		
		AbilityGenerator.InitAbilities();
		RNG.rng = new RNG();
		
		player = GameCharacter.genChar("Player", 5);
		//enemy = GameCharacter.genChar(5);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT);
		camera.update();
		camera.zoom = 1f;

		customFont = new BitmapFont(Gdx.files.internal("visitor.fnt"));
		font = new BitmapFont(Gdx.files.internal("visitor.fnt"));
		font.getData().setScale(1f);
		
		warningFont = new BitmapFont(Gdx.files.internal("visitor.fnt"));
		warningFont.getData().setScale(2f);
		
		customFont.setColor(139f/255f, 172f/255f, 15f/255f, 1f);
		font.setColor(68f/255f, 36f/255f, 52f/255f, 1f);
		warningFont.setColor(15f/255f, 56f/255f,  15f/255f, 1f);

		batch = new SpriteBatch();

		sprBatch = new SpriteBatch();
		
		srend = new ShapeRenderer();
		srend.setAutoShapeType(true);
		
		addIntroState();
		//addMainState();
		
		help = helpMain;

		
		currentState = stateStack.pop();
		
		innerBoxColor = new Color(210f/255f, 170f/255f, 153f/255f, 1f);
		outerBoxColor = new Color(68f/255f, 36f/255f, 52f/255f, 1f);

		healthBarColor = new Color(109f/255f, 170f/255f, 44f/255f, 1f);
		xpBarColor = new Color(48f/255f, 96f/255f, 130f/255f,1f);
		
	}
	
	String storyText = "For years, humans have been keeping captive all individuals of"
			+ " a mighty race of elementals, the Sefivms, in order to study"
			+ " their control of nature. Sefivms' abilities have been weakened"
			+ " with nullyfing orbs around their containers.\n\n"
			+ "One day, container maintainers go on for a strike because they"
			+ " claim not to be paid as they should.\n\n"
			+ "What they didn't know is that they just started the beginning of"
			+ " the end.";
	
	private void addIntroState() {
		stateStack.add(new IState() {

			
			static final float TIME_OFFSET = 3f;
			float timer = 0f;
			TextureRegion logoFrame;
			
			@Override
			public void init() {
				helpOn = false;
				logoFrame = Gfx.logoAnim.getKeyFrame(0);
				if( !Sfx.introMusic.isPlaying() ) {
					Sfx.introMusic.setVolume(0.8f);
					Sfx.introMusic.play();
				}
			}

			@Override
			public boolean step() {
				timer += Gdx.graphics.getDeltaTime();
				if( timer > TIME_OFFSET ) {
					logoFrame = Gfx.logoAnim.getKeyFrame(timer - TIME_OFFSET);
				}
				if( checkKey(Input.Keys.SPACE) ) {
					addMainState();
					return false;
				}
				return true;
			}

			@Override
			public void render() {
				int rw = logoFrame.getRegionWidth()*8;
				int rh = logoFrame.getRegionHeight()*8;
				
				drawTextBox(10,10,800-20,480-20,storyText);
				batch.begin();
				batch.draw(logoFrame, 110, 490, rw, rh);
				batch.end();
			}

			@Override
			public void exit() {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		stateStack.peek().init();
	}
	int selectedAbility = 0;
	private boolean statsOn = false;
	private boolean helpOn = false;
	
	public void addDealDamageState(final GameCharacter gc, final float dmg) {
		stateStack.push(new IState() {

			float timer = 1f;
			
			@Override
			public boolean step() {
				System.out.println("DEALIN " + timer);
				float dt = Gdx.graphics.getDeltaTime();
				timer -= dt;
				if( dt < 0 ) {
					dt = 0;
				}
				float amount = dmg * dt;
				gc.dealDamage(amount);
				return timer > 0;
			}

			@Override
			public void init() {
				helpOn = false;
				statsOn = false;				
			}

			@Override
			public void render() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void exit() {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void checkSelectAbilityInput() {
		if( checkKey(Input.Keys.DOWN) ) {
			selectedAbility = (selectedAbility + 1 ) % 6;
		} else if( checkKey(Input.Keys.UP) ) {
			if( selectedAbility == 0 ) {
				selectedAbility = 5;
			} else {
				selectedAbility = selectedAbility -1;
			}
		}
	}
	
	public void checkStatusEffects() {
		if( checkKey(Input.Keys.E) ) {
			this.showStatusEffects = !this.showStatusEffects;
		}
	}
	
	public void checkHelp() {
		if( checkKey(Input.Keys.H) ) {
			helpOn = !helpOn;
		}
	}

	public void checkGen() {
		if( checkKey(Input.Keys.R) ) {
			player = GameCharacter.genChar("Player", 5);
		}
	}
	
	public void checkStats() {
		if( checkKey(Input.Keys.S)) {
			statsOn = !statsOn;
		}
	}
	
	int lastUsedAttack = 0;
	private int lastEnemyAttack = 0;
	private float xpGainedLastBattle;

	private void addInfoState(final String infoText) {
		stateStack.push(new IState() {

			@Override
			public boolean step() {
				return !checkKey(Input.Keys.SPACE);
			}

			@Override
			public void init() {
				helpOn = false;
				statsOn = false;				
			}

			@Override
			public void render() {
				drawMainBox(infoText);				
			}

			@Override
			public void exit() {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	void addFillSlotState( final int slot ) {
		IState is = new IState(){

			int selected = 0;
			List<AbilityInstance> possibilities = new ArrayList<AbilityInstance>();
			String abiText;
			
			@Override
			public boolean step() {
				
				if( checkKey(Input.Keys.DOWN) ) {
					selected = (selected + 1 ) % 3;
				} else if( checkKey(Input.Keys.UP) ) {
					if( selected == 0 ) {
						selected = 5;
					} else {
						selected = selected -1;
					}
				}
				
				boolean ret = true;
				if( checkKey(Input.Keys.SPACE) ) {
					player.abilities[slot] = possibilities.get(selected);
					ret = false;
				}
				return ret;
			}

			@Override
			public void init() {
				possibilities.add(GameCharacter.GenAbilityForChar(player));
				possibilities.add(GameCharacter.GenAbilityForChar(player));
				possibilities.add(GameCharacter.GenAbilityForChar(player));
				helpOn = false;
				statsOn = false;
			}

			@Override
			public void render() {
				abiText = "You need to fill a slot\n\n";
				for( int i = 0; i < 3; i++ ) {
					if( i == selected ) {
						abiText += possibilities.get(i).toString() + " <\n";				
					} else {
						abiText += possibilities.get(i).toString() + "\n";
					}
				}

				drawTextBox(20, 100, 650,150, abiText);				
			}

			@Override
			public void exit() {
				// TODO Auto-generated method stub
				
			}
			
		};
		is.init();
		stateStack.push(is);
	}
	
	private void drawHealthBar(float x, float y, float w, float h, float percent) {
		
		if( w < 0 ) {
			w = 0;
		}
		srend.begin();
		srend.set(ShapeType.Filled);
		srend.setColor(outerBoxColor);
		srend.rect(x-5, y-5, w+10, h+10);
		srend.setColor(healthBarColor);
		w *= percent;

		if( w < 0 ) {
			w = 0;
		}
		srend.rect(x, y, w, h);
		srend.end();
	}

	
	private void addTurnStep(final boolean pturn) {

		stateStack.push(new IState() {
		@Override
		public boolean step() {
			boolean ret = true;
			if( pturn ) {
				checkStats();
				checkSelectAbilityInput();
				checkStatusEffects();
				if( checkKey(Input.Keys.SPACE ) ) {
					lastUsedAttack = selectedAbility;
				}

				if( checkKey(Input.Keys.SPACE) || player.isDead() ) {
					addMidturnState();
					System.out.println("GOTOMIDTURNPLAYER");
					ret = false;
				}
			} else {
				lastEnemyAttack = RNG.rng.nextInt(6);
				addMidturnState();
				ret = false;
			}
			return ret;
		}

		@Override
		public void init() {
			helpOn = false;
			statsOn = false;		}

		@Override
		public void render() {
			if( playerTurn ) {
				drawMainBox("Your turn, choose an ability!\n" + abilitiesSelectedText);
			}			
		}

		@Override
		public void exit() {
			// TODO Auto-generated method stub
			
		}
		});
	}
	
	private void addCheckStatusEffects(final GameCharacter gc) {
		stateStack.add(new IState() {

			@Override
			public void init() {
				List<StatusEffect> disabled = gc.checkStatusEffects();
				for( StatusEffect se : disabled ) {
					addInfoState("Effect of " + se.ai.onlyAbility() + " vanished from " + gc.name + "!");
				}
			}

			@Override
			public boolean step() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void render() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void exit() {
				// TODO Auto-generated method stub
				
			}
			
		});
		/*
		List<StatusEffect> rm = new LinkedList<StatusEffect>();
		for( StatusEffect se : gc.benefits ) {
			se.turns--;
			if( se.turns <= 0 ) {
				rm.add(se);
			}
		}
		
		for( StatusEffect se : rm ) {
			gc.benefits.remove(se);
		}
		*/
	}

	
	private void addMidturnState() {
		stateStack.push(new IState() {

			float timer = 1f;
			
			void handleMidturn( GameCharacter active, GameCharacter passive, boolean isPlayer ) {
				int used;
				if( isPlayer ) {
					used = lastUsedAttack;
				} else {
					used = lastEnemyAttack;
				}
				AbilityInstance ai = active.abilities[used];
				if( ai.model instanceof ProtectionModel ) {
					// cast on myself
					float r = RNG.rng.nextFloat();
					float p = 0.7f + active.stats.affinity[ai.element] * 0.01f;
					boolean success = r < p;
					if( success ) {
						active.abilityReceived(active, ai);						
					} else {
						addInfoState("But missed!");
					}
					addInfoState(active.name + " casted " + active.abilities[used].onlyAbility() + " on self!");
				} else if( ai.model instanceof HazardModel ) {
					float r = RNG.rng.nextFloat();
					float p = 0.2f + active.stats.affinity[ai.element] * 0.06f;
					boolean success = r < p;
					if( success ) {
						passive.abilityReceived(active, ai);
					} else {
						addInfoState("But missed!");						
					}
					addInfoState(active.name + " casted " + active.abilities[used].onlyAbility() + " on " + passive.name + "!");
				} else if( ai.model instanceof AttackModel ) {
					int dmg = passive.abilityReceived(active, ai);
					addDealDamageState( passive, dmg );
					addInfoState(active.name + " dealt " + dmg + " damage to " + passive.name + "!");
					addInfoState(active.name + " used " + active.abilities[used].onlyAbility() + "!");
				}
				if( isPlayer && active.abilities[used].uses <= 0 ) {
					addFillSlotState(used);
				}
				addCheckStatusEffects(active);
			}
			
			@Override
			public boolean step() {
				if( !checkWinOrLose() ) {
					addTurnStep(!playerTurn);
					playRandomNoise();
					if( playerTurn ) {
						handleMidturn( player, enemy, true );
					} else {
						handleMidturn( enemy, player, false );
					}
					playerTurn = !playerTurn;
				}
				return false;
			}


			private void playRandomNoise() {
				int nid = RNG.rng.nextInt(5);
				Sfx.PlayNoise(nid);
			}

			@Override
			public void init() {
				helpOn = false;
				statsOn = false;
			}

			@Override
			public void render() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void exit() {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}
	
	boolean checkWinOrLose() {
		if( player.isDead() ) {
			this.addMainState();
			this.addInfoState("You are dead...");
			return true;
		} else if( enemy.isDead() ) {
			xpGainedLastBattle = player.getXpByKilling(enemy);
			//enemy = GameCharacter.genChar(5);
			playerTurn = true;
			addBattleBeginState();
			addCheckLevelUp();
			addXPUpgradeState(xpGainedLastBattle);
			this.addInfoState("Enemy is dead, you won!\nGained " + ((int)xpGainedLastBattle) );
			return true;
		}
		return false;
	}
	
	public void addCheckLevelUp() {
		stateStack.add(new IState() {

			float levelsGained;
			String txt;
			int selected = 0;
			private int statPoints;
			private int affinityPoints;
			
			@Override
			public void init() {
				levelsGained = player.levelsGainedLastBattle;
				statPoints = (int) (levelsGained * 10);
				affinityPoints = (int) (levelsGained * 2);
			}

			@Override
			public boolean step() {
				if( checkKey(Input.Keys.DOWN) ) {
					if( statPoints > 0 ) {
						selected = (selected + 1 ) % Stats.NUM_STATS;						
					} else {
						selected = (selected + 1) % ElementType.NUM_ELEMENTS;
					}
				} else if( checkKey(Input.Keys.UP) ) {
					if( selected == 0 ) {
						if( statPoints > 0 ) {
							selected = Stats.NUM_STATS;							
						} else {
							selected = ElementType.NUM_ELEMENTS;
						}
					} else {
						selected = selected -1;
					}
				}
				if( checkKey(Input.Keys.SPACE) && statPoints > 0 ) {
					player.stats.stats[selected]++;
					statPoints--;
				} else if( checkKey(Input.Keys.SPACE) && affinityPoints > 0 ) {
					player.stats.affinity[selected]++;
					affinityPoints--;
				}
				return levelsGained != 0 && affinityPoints > 0;
			}

			@Override
			public void render() {
				drawMainBox("You gained " + levelsGained + " levels!\nSpend your points");
				if( statPoints > 0 ) {
					drawTextBox(30, 30, 300, 380, player.stats.statsStringSelected(selected) + statPoints + " points left");
				} else if( affinityPoints > 0 ){
					drawTextBox(30, 30, 300, 380, player.stats.affinitiesStringSelected(selected) + affinityPoints + " points left");
				}
			}

			@Override
			public void exit() {
				player.level += player.levelsGainedLastBattle;
				player.levelsGainedLastBattle = 0;
			}
			
		});
	}
	
	
	
	public void addXPUpgradeState(final float xpGained) {
		stateStack.add(new IState() {

			float timer = 1f;
			
			@Override
			public boolean step() {
				System.out.println("DEALIN " + timer);
				float dt = Gdx.graphics.getDeltaTime();
				timer -= dt;
				if( dt < 0 ) {
					dt = 0;
				}
				float amount = xpGained * dt;
				if( player.addXP(amount) ) {
					player.levelUp();
				}
				return timer > 0;
			}

			@Override
			public void init() {
				helpOn = false;
				statsOn = false;				
			}

			@Override
			public void render() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void exit() {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	void drawTextBox( float x, float y, float w, float h, String text ) {
		srend.begin();
		srend.set(ShapeType.Filled);
		srend.setColor(outerBoxColor);
		srend.box(x-5, y-5, 0, w+10, h+10, 1);
		srend.setColor(innerBoxColor);
		srend.box(x, y, 0, w, h, 1);
		srend.end();
		
		sprBatch.begin();
		font.draw(sprBatch, text, x- 10, y+h - 10, w-20, 4, true );
		sprBatch.end();		
	}
	
	float warningTimer = 0;
	
	IState currentState;

	float gameTimer = 0f;
	boolean playerTurn = true;
	private String abilitiesSelectedText;
	private String abilitiesText;
	
	void click() {
		Sfx.Play(0);
	}
	
	boolean checkKey( int k ) {
		if( Gdx.input.isKeyJustPressed(k) ) {
			click();
			return true;
		}
		return false;
	}
	
	@Override
	public void render () {
		
		
		Gdx.gl.glClearColor(133f / 255f, 149f / 255f, 161f / 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		batch.begin();
		if( !player.isDead() ) {
			batch.draw(Gfx.monster, 50, 0, Gfx.monster.getRegionWidth() * 8, Gfx.monster.getRegionHeight() * 8);
		}
		if( enemy != null && !enemy.isDead() ) {
			batch.draw(Gfx.wizard, 550, 370, Gfx.wizard.getRegionWidth() * 6, Gfx.wizard.getRegionHeight() * 6);			
		}
		font.draw(batch, "LV." + ((int)player.level), 400, 300);
		font.draw(batch, Math.max(0, ((int)player.currentHP)) + "/" + ((int)player.stats.stats[Stats.HP]), 600, 300);
		batch.end();
		
		drawPlayerHealthBar();
		drawXPBar();
		
		if( enemy != null ) {
			drawEnemyHealthBar();			
		}
		
		abilitiesText = "";
		abilitiesSelectedText = "";
		for( int i = 0; i < 6; i++ ) {
			if( i == selectedAbility ) {
				abilitiesSelectedText += player.abilities[i].toString() + " <\n";				
			} else {
				abilitiesSelectedText += player.abilities[i].toString() + "\n";
			}
			abilitiesText += player.abilities[i].toString() + "\n";
		}
		
		gameTimer += Gdx.graphics.getDeltaTime();
		
		if( !currentState.step() ) {
			currentState.exit();
			currentState = stateStack.pop();
			currentState.init();
		}
		
		currentState.render();

		if( helpOn ) {
			drawTextBox(30, 430, 500, 130, help);
		}
		
		if( statsOn ) {
			drawTextBox(30, 30, 300, 380, player.stats.toString());
		}
		
		if( showStatusEffects ) {
			renderStatusEffects(player, 0);
			renderStatusEffects(enemy, 400);
			
			batch.begin();
			
			/*
			*/
			batch.end();
		}
	}
	
	public void renderStatusEffects( GameCharacter gc, int x ) {
		drawTextBox( x + 20, 20, 400 - 40, 600 - 40, "");
		batch.begin();
		font.draw(batch, "BENEFITS", x+30, 600-40);
		font.draw(batch, "AILMENTS", x+30, 300);
		
		
		// AILMENTS
		batch.draw(Gfx.status.get(0), x+20, 300-135, 64, 64);
		batch.draw(Gfx.status.get(1), x+20, 300-195, 64, 64);
		batch.draw(Gfx.status.get(2), x+20, 300-255, 64, 64);
		
		for( int i = 0; i < ElementType.NUM_ELEMENTS; i++ ) {
			if( gc.growls[i] != null ) {
				batch.draw(Gfx.status.get(3 + i), x + 16 + i * 32+64, 300-135, 64, 64);
			}
		}

		for( int i = 0; i < ElementType.NUM_ELEMENTS; i++ ) {
			if( gc.shades[i] != null ) {
				batch.draw(Gfx.status.get(3 + i), x + 16 + i * 32+64, 300-195, 64, 64);
			}
		}

		for( int i = 0; i < ElementType.NUM_ELEMENTS; i++ ) {
			if( gc.haunts[i] != null ) {
				batch.draw(Gfx.status.get(3 + i), x + 16 + i * 32+64, 300-255, 64, 64);
			}
		}

		
		// BENEFITS
		batch.draw(Gfx.status.get(0), x+20, 600-135, 64, 64);
		batch.draw(Gfx.status.get(1), x+20, 600-195, 64, 64);
		batch.draw(Gfx.status.get(2), x+20, 600-255, 64, 64);

		for( int i = 0; i < ElementType.NUM_ELEMENTS; i++ ) {
			if( gc.auras[i] != null ) {
				batch.draw(Gfx.status.get(3 + i), x + 16 + i * 32+64, 600-135, 64, 64);
			}
		}

		for( int i = 0; i < ElementType.NUM_ELEMENTS; i++ ) {
			if( gc.barriers[i] != null ) {
				batch.draw(Gfx.status.get(3 + i), x + 16 + i * 38+64, 600-195, 64, 64);
			}
		}

		for( int i = 0; i < ElementType.NUM_ELEMENTS; i++ ) {
			if( gc.chants[i] != null ) {
				batch.draw(Gfx.status.get(3 + i), x + 16 + i * 32+64, 600-255, 64, 64);
			}
		}

		batch.end();
	}
	
	private void drawXPBar() {
		
		float percent = player.xp / player.nextLevelXP;
		srend.begin();
		srend.set(ShapeType.Filled);
		srend.setColor(outerBoxColor);
		int x, y, w, h;
		x = 400;
		y = 225;
		w = 400-20;
		h = 10;
		srend.rect(x-5, y-5, w+10, h+10);
		srend.setColor(xpBarColor);
		w *= percent;
		if( w < 0 ) w = 0;
		srend.rect(x, y, w, h);
		srend.end();

	}

	void drawPlayerHealthBar() {
		float hp = (int) player.currentHP;
		float maxhp = (int) player.stats.stats[Stats.HP];
		float percent = hp / maxhp;
		drawHealthBar(400, 250, 400-20, 10, percent);
	}

	void drawEnemyHealthBar() {
		float hp = (int) enemy.currentHP;
		float maxhp = (int) enemy.stats.stats[Stats.HP];
		float percent = hp / maxhp;
		drawHealthBar(10, 580, 400-20, 10, percent);
	}

	private void drawMainBox(String string) {
		drawTextBox(10, 10, 800-20, 200, string);		
	}

}
