package com.alesegdia.ld33.assets;

import com.alesegdia.ld33.RNG;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sfx {

	public static Music introMusic;
	public static Music battleMusic;
	public static Sound click;
	
	public static Sound[] noises = new Sound[5];
	
	public static void Initialize() {
		battleMusic = Gdx.audio.newMusic(Gdx.files.internal("trashemon.mp3"));
		battleMusic.setLooping(true);
		
		introMusic = Gdx.audio.newMusic(Gdx.files.internal("walk.mp3"));
		introMusic.setLooping(true);
		
		click = Gdx.audio.newSound(Gdx.files.internal("click.wav"));
		ids[0] = click.play(0);
		
		for( int i = 1; i <= 5; i++ ) {
			noises[i-1] = Gdx.audio.newSound(Gdx.files.internal("n" + i + ".wav"));
		}

	}
	
	public static long noisesIDs[] = new long[5];
	
	public static void PlayNoise(int sfx) {
		noises[sfx].stop(noisesIDs[sfx]);
		noisesIDs[sfx] = noises[sfx].play();
		float r = 1f + RNG.rng.nextFloat() / 2f - 0.25f;
		noises[sfx].setPitch(noisesIDs[sfx], r);
	}
	
	public static long ids[] = new long[10];
	
	public static void Play(int sfx) {
		if( sfx == 0 ) {
			click.stop(ids[sfx]);
			float r = 1f + RNG.rng.nextFloat() / 8f - 1f/16f;
			ids[sfx] = click.play(1f);
			click.setPitch(ids[sfx], 1.5f);
		}
	}

}
