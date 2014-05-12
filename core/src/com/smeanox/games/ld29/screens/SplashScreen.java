package com.smeanox.games.ld29.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.smeanox.games.ld29.AssetManager;
import com.smeanox.games.ld29.GameConsts;

public class SplashScreen implements Screen {

	private BitmapFontCache title, instructions, click;
	private float timePassed;
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	private int logoX, logoY;
	private float logoScale;
	private boolean finalTransition;

	private Game game;

	public SplashScreen(Game game) {
		this.game = game;
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render(float delta) {
		timePassed += delta;

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (timePassed > GameConsts.SplashAnimationTime * 3
				&& Gdx.input.isTouched() && !finalTransition) {
			finalTransition = true;
			timePassed = GameConsts.SplashAnimationTime / 2;
		}
		if (finalTransition && timePassed > GameConsts.SplashAnimationTime) {
			game.setScreen(new GameScreen(game));
		}
		spriteBatch.begin();
		if (!finalTransition && timePassed < GameConsts.SplashAnimationTime) {
			spriteBatch.draw(AssetManager.smeanox, logoX, logoY,
					AssetManager.smeanox.getWidth() * logoScale,
					AssetManager.smeanox.getHeight() * logoScale);
		} else if (!finalTransition
				&& timePassed < GameConsts.SplashAnimationTime * 2) {
			title.draw(spriteBatch);
		} else if (!finalTransition
				&& timePassed < GameConsts.SplashAnimationTime * 3) {
			instructions.draw(spriteBatch);
		} else {
			click.draw(spriteBatch);
		}
		spriteBatch.end();
		drawTransition();
	}

	private void drawTransition() {
		if (timePassed > GameConsts.SplashAnimationTime * 3.5f) {
			return;
		}

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		shapeRenderer.begin(ShapeType.Filled);
		if (finalTransition) {
			shapeRenderer.setColor(
					GameConsts.WallColor.r,
					GameConsts.WallColor.g,
					GameConsts.WallColor.b,
					(MathUtils.cos(timePassed / GameConsts.SplashAnimationTime
							* MathUtils.PI2) + 1) / 2);
		} else {
			shapeRenderer.setColor(
					0,
					0,
					0,
					(MathUtils.cos(timePassed / GameConsts.SplashAnimationTime
							* MathUtils.PI2) + 1) / 2);
		}
		shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {

		timePassed = 0;
		logoScale = Gdx.graphics.getWidth() * 0.7f
				/ AssetManager.smeanox.getWidth();
		logoX = (int) ((Gdx.graphics.getWidth() - AssetManager.smeanox
				.getWidth() * logoScale) / 2);
		logoY = (int) ((Gdx.graphics.getHeight() - AssetManager.smeanox
				.getHeight() * logoScale) / 2);

		title = new BitmapFontCache(AssetManager.rocksolid);
		title.addText(GameConsts.GameName,
				(Gdx.graphics.getWidth() - AssetManager.rocksolid
						.getBounds(GameConsts.GameName).width) / 2,
				(Gdx.graphics.getHeight() + AssetManager.rocksolid
						.getBounds(GameConsts.GameName).height * 2) / 2);

		String instructions1 = "find the exit";
		String instructions2 = "drag to move";
		instructions = new BitmapFontCache(AssetManager.rocksolid);
		instructions.addText(instructions1,
				(Gdx.graphics.getWidth() - AssetManager.rocksolid
						.getBounds(instructions1).width) / 2, (Gdx.graphics
						.getHeight() + AssetManager.rocksolid
						.getBounds(instructions1).height * 2) / 2);
		instructions.addText(instructions2,
				(Gdx.graphics.getWidth() - AssetManager.rocksolid
						.getBounds(instructions2).width) / 2, (Gdx.graphics
						.getHeight() + AssetManager.rocksolid
						.getBounds(instructions2).height * 2) / 2 - 50);

		String click1 = "click to start";
		click = new BitmapFontCache(AssetManager.rocksolid);
		click.addText(click1, (Gdx.graphics.getWidth() - AssetManager.rocksolid
				.getBounds(click1).width) / 2,
				(Gdx.graphics.getHeight() + AssetManager.rocksolid
						.getBounds(click1).height * 2) / 2);
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
