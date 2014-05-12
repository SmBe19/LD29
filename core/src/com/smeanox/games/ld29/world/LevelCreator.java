package com.smeanox.games.ld29.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.smeanox.games.ld29.GameConsts;
import com.smeanox.games.ld29.helper.IntVal;

public class LevelCreator {
	// 0 = free, 1 = wall, 2 = start, 3 = dest
	private static Array<Array<IntVal>> level;

	private static int[] dirX = { -1, 1, 0, 0 };
	private static int[] dirY = { 0, 0, -1, 1 };

	private static int destX, destY;

	public static void createLevel(Array<Array<IntVal>> createLevel,
			int startX, int startY) {
		level = createLevel;
		createLevel.clear();
		for (int y = 0; y < GameConsts.LevelHeight; y++) {
			level.add(new Array<IntVal>());
			for (int x = 0; x < GameConsts.LevelWidth; x++) {
				level.get(y).add(new IntVal(1));
			}
		}
		visit(startX, startY);

		int ax, ay;
		for (int i = 0; i < GameConsts.LevelWidth * 5; i++) {
			do {
				ax = MathUtils.random(1, GameConsts.LevelWidth - 2);
				ay = MathUtils.random(1, GameConsts.LevelHeight - 2);
			} while (level.get(ay).get(ax).val != 0);
			visit(ax, ay);
		}

		level.get(startY).get(startX).val = 2;
		int runs = 0;
		do {
			ax = MathUtils.random(1, GameConsts.LevelWidth - 2);
			ay = MathUtils.random(1, GameConsts.LevelHeight - 2);
		} while (level.get(ay).get(ax).val != 0
				|| (ax - startX) * (ax - startX) + (ay - startY)
						* (ay - startY) < GameConsts.LevelWidth
						* GameConsts.LevelHeight / 2 && ++runs < 100000);
		level.get(ay).get(ax).val = 3;
		destX = ax;
		destY = ay;
	}

	private static void visit(int x, int y) {
		level.get(y).get(x).val = 0;

		boolean theEnd = true;
		for (int adir = 0; adir < 4; adir++) {
			if (canRemove(x + dirX[adir], y + dirY[adir])) {
				theEnd = false;
				break;
			}
		}
		int dir = -1;
		if (!theEnd) {
			dir = 1;
			do {
				dir = MathUtils.random(3);
			} while (!canRemove(x + dirX[dir], y + dirY[dir]));
			visit(x + dirX[dir], y + dirY[dir]);
		}
	}

	private static boolean canRemove(int x, int y) {
		if (x == 0 || y == 0 || x == GameConsts.LevelWidth - 1
				|| y == GameConsts.LevelHeight - 1)
			return false;
		if (level.get(y).get(x).val != 1)
			return false;

		int count = 0;
		for (int adir = 0; adir < 4; adir++) {
			if (level.get(y + dirY[adir]).get(x + dirX[adir]).val != 1) {
				count++;
			}
		}
		return count < 2 || MathUtils.randomBoolean(0.1f);
	}

	public static int getDestX() {
		return destX;
	}

	public static int getDestY() {
		return destY;
	}

}
