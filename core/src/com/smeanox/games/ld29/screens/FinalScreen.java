package com.smeanox.games.ld29.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.smeanox.games.ld29.AssetManager;
import com.smeanox.games.ld29.GameConsts;

public class FinalScreen implements Screen {
	private enum animationStatus {
		StartBlending, Throwing, EndBlending, End
	};

	private animationStatus activeStatus;
	private long animationStart;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	private OrthographicCamera camera;
	private BitmapFontCache theEnd;

	public FinalScreen() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameConsts.ScreenWidth,
				GameConsts.ScreenHeight);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
		spriteBatch = new SpriteBatch();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (activeStatus == animationStatus.StartBlending
				&& timePassed() > 2000) {
			activeStatus = animationStatus.Throwing;
			animationStart = TimeUtils.millis();
		}
		if (activeStatus == animationStatus.Throwing && timePassed() > 5000) {
			activeStatus = animationStatus.EndBlending;
			animationStart = TimeUtils.millis();
		}
		if (activeStatus == animationStatus.EndBlending && timePassed() > 2000) {
			activeStatus = animationStatus.End;
			animationStart = TimeUtils.millis();
		}
		if (activeStatus == animationStatus.End && timePassed() > 5000) {
			Gdx.app.exit();
		}

		shapeRenderer.begin(ShapeType.Filled);

		shapeRenderer.setColor(GameConsts.HeavenColor);
		shapeRenderer.rect(0, 0, GameConsts.ScreenWidth,
				GameConsts.ScreenHeight);

		shapeRenderer.setColor(GameConsts.SunColor);
		shapeRenderer.circle(GameConsts.ScreenWidth, GameConsts.ScreenHeight,
				100);

		if (activeStatus == animationStatus.Throwing) {
			shapeRenderer.setColor(1, 1, 1, 1);
			float x, y;
			float time = timePassed();
			if (time > 3590) {
				time = 3590;
			}
			x = time / 10 + 200;
			y = -((x - 380) * (x - 380) / 100) + 200 + GameConsts.ScreenHeight
					* 0.5f;
			shapeRenderer.rect(x, y, 16, 16, 32, 32, 1, 1, -time / 10 - 1);
		}
		if (activeStatus == animationStatus.EndBlending) {
			shapeRenderer.setColor(1, 1, 1, 1);
			float x, y;
			float time = 3590;
			x = time / 10 + 200;
			y = -((x - 380) * (x - 380) / 100) + 200 + GameConsts.ScreenHeight
					* 0.5f;
			shapeRenderer.rect(x, y, 16, 16, 32, 32, 1, 1, -time / 10 - 1);
		}

		shapeRenderer.setColor(GameConsts.WallColor);
		shapeRenderer.rect(0, 0, GameConsts.ScreenWidth,
				GameConsts.ScreenHeight * 0.25f);
		shapeRenderer.setColor(GameConsts.DestColor);
		shapeRenderer.rect(100, GameConsts.ScreenHeight * 0.25f, 200,
				GameConsts.ScreenHeight * 0.25f);

		if (activeStatus == animationStatus.StartBlending) {
			shapeRenderer
					.setColor(
							GameConsts.WallColor.r,
							GameConsts.WallColor.g,
							GameConsts.WallColor.b,
							(MathUtils.cos(timePassed() / 2000f * MathUtils.PI) + 1) / 2);
			shapeRenderer.rect(0, 0, GameConsts.ScreenWidth,
					GameConsts.ScreenHeight);
		}
		if (activeStatus == animationStatus.EndBlending) {
			shapeRenderer.setColor(
					0,
					0,
					0,
					(MathUtils.cos(timePassed() / 2000f * MathUtils.PI
							+ MathUtils.PI) + 1) / 2);
			shapeRenderer.rect(0, 0, GameConsts.ScreenWidth,
					GameConsts.ScreenHeight);
		}
		if (activeStatus == animationStatus.End) {
			shapeRenderer.setColor(0, 0, 0, 1);
			shapeRenderer.rect(0, 0, GameConsts.ScreenWidth,
					GameConsts.ScreenHeight);
		}

		shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);

		if (activeStatus == animationStatus.End) {
			spriteBatch.begin();
			theEnd.draw(spriteBatch);
			spriteBatch.end();
			if (timePassed() < 2000) {
				Gdx.gl.glEnable(GL20.GL_BLEND);
				Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,
						GL20.GL_ONE_MINUS_SRC_ALPHA);

				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer
						.setColor(
								0,
								0,
								0,
								(MathUtils.cos(timePassed() / 2000f
										* MathUtils.PI) + 1) / 2);
				shapeRenderer.rect(0, 0, GameConsts.ScreenWidth,
						GameConsts.ScreenHeight);
				shapeRenderer.end();

				Gdx.gl.glDisable(GL20.GL_BLEND);
			}
		}
	}

	private long timePassed() {
		return (TimeUtils.millis() - animationStart);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		activeStatus = animationStatus.StartBlending;
		animationStart = TimeUtils.millis();

		theEnd = new BitmapFontCache(AssetManager.rocksolid);
		String theEndStr = "the end";
		theEnd.addText(theEndStr,
				(Gdx.graphics.getWidth() - AssetManager.rocksolid
						.getBounds(theEndStr).width) / 2, (Gdx.graphics
						.getHeight() + AssetManager.rocksolid
						.getBounds(theEndStr).height * 2) / 2);
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
