package com.smeanox.games.ld29;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AssetManager {
	public static BitmapFont rocksolid;
	public static Texture smeanox;

	public static void load() {
		rocksolid = new BitmapFont(Gdx.files.internal("rocksolid.fnt"));
		smeanox = new Texture(Gdx.files.internal("smeanox.png"));
	}
}
