package com.smeanox.games.ld29.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.smeanox.games.ld29.AssetManager;
import com.smeanox.games.ld29.GameConsts;
import com.smeanox.games.ld29.helper.IntVal;

public class GameRenderer {

	private GameWorld world;

	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	// private ShapeRenderer dragRenderer;
	private OrthographicCamera camera;

	// Vars from world
	private Array<Array<IntVal>> level;
	private Vector2 heroPos;

	// private Vector2 dragStart;

	public GameRenderer(GameWorld gameWorld) {
		world = gameWorld;
		level = world.getLevel();
		heroPos = world.getHeroPos();
		// dragStart = world.getDragStart();

		Gdx.gl.glClearColor(0, 0, 0, 1);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameConsts.ScreenWidth,
				GameConsts.ScreenHeight);

		camera.position.set(0, 0, 0);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
		spriteBatch = new SpriteBatch();
		// dragRenderer = new ShapeRenderer();
		// dragRenderer.setProjectionMatrix(camera.combined);
	}

	public void render() {
		camera.position.set(heroPos, 0);
		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);

		/*
		 * if (!world.isDragging()) {
		 * dragRenderer.setProjectionMatrix(camera.combined); }
		 */

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.begin(ShapeType.Filled);

		int rectsX = GameConsts.ScreenWidth / GameConsts.FieldWidth + 2;
		int rectsY = GameConsts.ScreenHeight / GameConsts.FieldHeight + 2;
		int heroX = MathUtils.floor(heroPos.x / GameConsts.FieldWidth);
		int heroY = MathUtils.floor(heroPos.y / GameConsts.FieldHeight);

		for (int y = Math.max(0, heroY - rectsY / 2); y < Math.min(
				GameConsts.LevelHeight, heroY + rectsY / 2 + 1); y++) {
			for (int x = Math.max(0, heroX - rectsX / 2); x < Math.min(
					GameConsts.LevelWidth, heroX + rectsX / 2 + 1); x++) {
				switch (level.get(y).get(x).val) {
				case 0:
					shapeRenderer.setColor(0, 0, 0, 1);
					break;
				case 1:
					shapeRenderer.setColor(GameConsts.WallColor);
					break;
				case 2:
					shapeRenderer.setColor(GameConsts.StartColor);
					break;
				case 3:
					shapeRenderer.setColor(GameConsts.DestColor);
					break;
				}
				shapeRenderer.rect(x * GameConsts.FieldWidth, y
						* GameConsts.FieldHeight, GameConsts.FieldWidth,
						GameConsts.FieldHeight);
			}
		}
		shapeRenderer.setColor(GameConsts.HeroColor);
		shapeRenderer.rect(heroPos.x - GameConsts.HeroWidth / 2, heroPos.y
				- GameConsts.HeroHeight / 2, GameConsts.HeroWidth,
				GameConsts.HeroHeight);
		shapeRenderer.end();

		if (TimeUtils.millis() - world.getStartTimeActiveLevel() > 20000
				&& (TimeUtils.millis() - world.getStartTimeActiveLevel()) % 4000 < 1000) {
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(GameConsts.DestColor);
			int destX = LevelCreator.getDestX();
			int destY = LevelCreator.getDestY();
			float deltaX = (destX + 0.5f) * GameConsts.FieldWidth - heroPos.x;
			float deltaY = (destY + 0.5f) * GameConsts.FieldHeight - heroPos.y;
			float dist = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
			shapeRenderer.line(heroPos.x, heroPos.y, heroPos.x + deltaX / dist
					* 10, heroPos.y + deltaY / dist * 10);
			shapeRenderer.end();
		}

		spriteBatch.begin();
		AssetManager.rocksolid.draw(spriteBatch,
				"Lvl " + (world.getLevelsDone() + 1) + "; Size: "
						+ GameConsts.LevelWidth + " x "
						+ GameConsts.LevelHeight, 20, 50);

		long seconds = (TimeUtils.millis() - world.getStartTime()) / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		seconds %= 60;
		minutes %= 60;
		String time = "";
		if (hours < 10)
			time += "0";
		time += hours;
		time += " ";
		if (minutes < 10)
			time += "0";
		time += minutes;
		time += " ";
		if (seconds < 10)
			time += "0";
		time += seconds;
		AssetManager.rocksolid.draw(spriteBatch, time, GameConsts.ScreenWidth
				- AssetManager.rocksolid.getBounds("88 88 88").width - 20, 50);
		spriteBatch.end();

		/*
		 * dragRenderer.begin(ShapeType.Line); if (world.isDragging()) {
		 * dragRenderer.circle(dragStart.x, dragStart.y, 20); }
		 * dragRenderer.end();
		 */
		drawTransition();
	}

	private void drawTransition() {
		if (!world.isLevelChange())
			return;

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer
				.setColor(GameConsts.WallColor.r, GameConsts.WallColor.g,
						GameConsts.WallColor.b, 1 - (MathUtils.cos((TimeUtils
								.millis() - world.getLevelChangeStart())
								/ 4000f * MathUtils.PI2) + 1) / 2);
		shapeRenderer.rect(heroPos.x - GameConsts.ScreenWidth / 2, heroPos.y
				- GameConsts.ScreenHeight / 2, GameConsts.ScreenWidth,
				GameConsts.ScreenHeight);
		shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
}
