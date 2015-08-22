package com.alesegdia.ld33.desktop;

import com.alesegdia.ld33.GameConfig;
import com.alesegdia.ld33.GdxGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GameConfig.WINDOW_WIDTH;
		config.height = GameConfig.WINDOW_HEIGHT;
		config.title = "RPGBattles";
		new LwjglApplication(new GdxGame(), config);
	}
}
