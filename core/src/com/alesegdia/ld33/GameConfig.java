package com.alesegdia.ld33;

public class GameConfig {

	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	
	public static final float RATIO = ((float)WINDOW_WIDTH) / ((float)WINDOW_HEIGHT);
	public static final float VIEWPORT_WIDTH = 10.f;
	public static final float VIEWPORT_HEIGHT = VIEWPORT_WIDTH / RATIO;
	
	public static final float METERS_TO_PIXELS = 16f;
	public static final float PIXELS_TO_METERS = 1f / METERS_TO_PIXELS;;
	
}
