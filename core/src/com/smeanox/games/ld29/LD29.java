package com.smeanox.games.ld29;

import com.badlogic.gdx.Game;
import com.smeanox.games.ld29.screens.SplashScreen;

public class LD29 extends Game {

	@Override
	public void create() {
		AssetManager.load();
		setScreen(new SplashScreen(this));
	}
}
