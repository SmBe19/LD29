package com.smeanox.games.ld29.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.smeanox.games.ld29.world.GameRenderer;
import com.smeanox.games.ld29.world.GameWorld;

public class GameScreen implements Screen {

	private GameWorld gameWorld;
	private GameRenderer gameRenderer;
	private Game game;

	public GameScreen(Game game) {
		gameWorld = new GameWorld();
		gameRenderer = new GameRenderer(gameWorld);
		gameWorld.setCamera(gameRenderer.getCamera());
		this.game = game;
	}

	@Override
	public void render(float delta) {
		gameWorld.update(delta);
		gameRenderer.render();
		if (gameWorld.isDone())
			game.setScreen(new FinalScreen());
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
