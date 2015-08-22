package com.alesegdia.ld33.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Gfx {

	public static TextureRegion baseText;
	public static TextureRegion monster;
	public static TextureRegion wizard;
	
	public static Spritesheet status;
	public static Spritesheet logo;
	public static Animation logoAnim;
	
	public static TextureRegion LoadRegion( String path ) {
		TextureRegion tr = new TextureRegion();
		tr.setRegion(new Texture(Gdx.files.internal(path)));
		return tr;
	}
	
	public static void Initialize() {
		monster = LoadRegion("monster.png");
		wizard = LoadRegion("wizard.png");
		status = new Spritesheet("status.png", 3, 3);
		logo = new Spritesheet("logo_anim.png", 1, 7);
		logoAnim = new Animation(0.05f, logo.getRange(0, 6));
	}
	
}
