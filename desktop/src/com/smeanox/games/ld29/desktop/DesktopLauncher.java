package com.smeanox.games.ld29.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.smeanox.games.ld29.GameConsts;
import com.smeanox.games.ld29.LD29;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GameConsts.ScreenWidth;
		config.height = GameConsts.ScreenHeight;
		config.title = GameConsts.GameName;
		new LwjglApplication(new LD29(), config);
	}
}
