package com.smeanox.games.ld29.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.smeanox.games.ld29.GameConsts;
import com.smeanox.games.ld29.helper.IntVal;

public class GameWorld {

	private Array<Array<IntVal>> level;
	private Vector2 heroPos, heroVelo;

	private long startTime;
	private long startTimeActiveLevel;

	private boolean levelChange;
	private boolean levelChanged;
	private long levelChangeStart;

	private int levelsDone;

	private OrthographicCamera camera;
	private Vector2 touchDownPos;
	private boolean dragging;
	private Vector3 vector3;

	public GameWorld() {
		level = new Array<Array<IntVal>>();
		for (int y = 0; y < GameConsts.LevelHeight; y++) {
			level.add(new Array<IntVal>());
			for (int x = 0; x < GameConsts.LevelWidth; x++) {
				level.get(y).add(new IntVal(0));
			}
		}

		heroPos = new Vector2(GameConsts.FieldWidth * 1.5f,
				GameConsts.FieldHeight * 1.5f);
		heroVelo = new Vector2(0, 0);

		levelChange = true;
		levelChanged = false;
		levelChangeStart = TimeUtils.millis() - 2000;
		levelsDone = -1;

		touchDownPos = new Vector2();
		dragging = false;
		vector3 = new Vector3();

		startTime = TimeUtils.millis();
	}

	public void update(float delta) {
		handleInput();

		if (levelChange && !levelChanged
				&& TimeUtils.millis() - levelChangeStart > 2000) {
			levelsDone++;
			if (levelsDone % 4 == 0) {
				GameConsts.LevelWidth *= 2;
				GameConsts.LevelHeight *= 2;
			}
			LevelCreator.createLevel(level, getFieldX(heroPos.x),
					getFieldY(heroPos.y));
			levelChanged = true;
			startTimeActiveLevel = TimeUtils.millis();
		}
		if (levelChange && TimeUtils.millis() - levelChangeStart > 4000) {
			levelChange = false;
		}
		float veloScale = 1;
		if (levelChange) {
			veloScale = (MathUtils.cos((TimeUtils.millis() - levelChangeStart)
					/ ((TimeUtils.millis() - levelChangeStart) < 2000 ? 500f
							: 4000f) * MathUtils.PI2) + 1) / 2;
			if ((TimeUtils.millis() - levelChangeStart) < 2000
					&& (TimeUtils.millis() - levelChangeStart) > 250) {
				veloScale = 0f;
			}
		}

		int heroX, heroY;
		heroX = getFieldX(heroPos.x);
		heroY = getFieldY(heroPos.y);

		heroPos.add(heroVelo.cpy().scl(delta * veloScale));

		// left
		if (getFieldX(heroPos.x - GameConsts.HeroWidth / 2) != heroX
				&& level.get(heroY).get(heroX - 1).val == 1) {
			heroPos.x = heroX * GameConsts.FieldWidth + GameConsts.HeroWidth
					/ 2;
		}
		// right
		if (getFieldX(heroPos.x + GameConsts.HeroWidth / 2) != heroX
				&& level.get(heroY).get(heroX + 1).val == 1) {
			heroPos.x = (heroX + 1) * GameConsts.FieldWidth
					- GameConsts.HeroWidth / 2;
		}
		// up
		if (getFieldY(heroPos.y - GameConsts.HeroHeight / 2) != heroY
				&& level.get(heroY - 1).get(heroX).val == 1) {
			heroPos.y = heroY * GameConsts.FieldHeight + GameConsts.HeroHeight
					/ 2;
		}
		// down
		if (getFieldY(heroPos.y + GameConsts.HeroHeight / 2) != heroY
				&& level.get(heroY + 1).get(heroX).val == 1) {
			heroPos.y = (heroY + 1) * GameConsts.FieldHeight
					- GameConsts.HeroHeight / 2;
		}

		if (!levelChange && level.get(heroY).get(heroX).val == 3
				&& heroX == getFieldX(heroPos.x + GameConsts.HeroWidth / 2)
				&& heroX == getFieldX(heroPos.x - GameConsts.HeroWidth / 2)
				&& heroY == getFieldY(heroPos.y + GameConsts.HeroHeight / 2)
				&& heroY == getFieldY(heroPos.y - GameConsts.HeroHeight / 2)) {
			levelChange = true;
			levelChanged = false;
			levelChangeStart = TimeUtils.millis();
		}
	}

	private void handleInput() {
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			vector3.set(camera.unproject(vector3.set(Gdx.input.getX(),
					Gdx.input.getY(), 0)));
			heroVelo.set(vector3.x - heroPos.x, vector3.y - heroPos.y).scl(2f);
		} else if (Gdx.input.isTouched()) {
			if (dragging) {
				heroVelo.set(Gdx.input.getX() - touchDownPos.x,
						touchDownPos.y - Gdx.input.getY()).scl(2f);
			} else {
				dragging = true;
				touchDownPos.set(Gdx.input.getX(), Gdx.input.getY());
			}
		} else {
			dragging = false;
			heroVelo.set(0, 0);
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			heroVelo.x = -250;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			heroVelo.x = 250;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			heroVelo.y = -250;
		} else if (Gdx.input.isKeyPressed(Keys.UP)) {
			heroVelo.y = 250;
		}

		if (heroVelo.len() > 500) {
			heroVelo.scl(500 / heroVelo.len());
		}
	}

	private int getFieldX(float posX) {
		return MathUtils.floor(posX / GameConsts.FieldWidth);
	}

	private int getFieldY(float posY) {
		return MathUtils.floor(posY / GameConsts.FieldHeight);
	}

	public Array<Array<IntVal>> getLevel() {
		return level;
	}

	public Vector2 getHeroPos() {
		return heroPos;
	}

	public long getLevelChangeStart() {
		return levelChangeStart;
	}

	public int getLevelsDone() {
		return levelsDone;
	}

	public long getStartTime() {
		return startTime;
	}
	
	public long getStartTimeActiveLevel() {
		return startTimeActiveLevel;
	}

	public boolean isLevelChange() {
		return levelChange;
	}

	public boolean isDone() {
		return levelsDone >= GameConsts.LevelsUntilEnd;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}
}
