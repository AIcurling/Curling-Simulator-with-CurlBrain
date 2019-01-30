package simulator;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class FunctionUtils {

	public static Pebble pebbles[] = new Pebble[Statics.pebble_countY * Statics.pebble_countX];
	public static boolean trajectories[] = new boolean[Statics.pebble_countY * Statics.pebble_countX];
	public static boolean PebbleisInitialized = false;

	public static int size_x = 32, size_y = 32;
	public static int interval_x = size_x == 32 ? 7 : 14, interval_y = size_y == 32 ? 50 : 25;
	
	public static void initializePebbles() {
		if (PebbleisInitialized)
			return;

		float y = -6.0f;
		for (int i = 0; i < Statics.pebble_countY; i++, y += 0.02f) {
			float x = 0.0f;

			for (int j = 0; j < Statics.pebble_countX; j++, x += 0.02f) {
				float friction = MathUtils.random(0.0046f, 0.002f);
				pebbles[(i * Statics.pebble_countX) + j] = new Pebble(friction, (i * Statics.pebble_countX) + j, x, y);
			}
		}

		PebbleisInitialized = true;
	}

	public static void pebble_scratch_reset() {
		for (int i = 0; i < Statics.pebble_countY; i++) {
			for (int j = 0; j < Statics.pebble_countX; j++) {
				pebbles[(i * Statics.pebble_countX) + j].setScratch(Vector2.Zero);
				pebbles[(i * Statics.pebble_countX) + j].isSweep(false);
			}
		}
	}
	
	// 32 by 32
	public static float[] average_bool() {
		
		float avr[] = new float[32 * 32];
		
		for(int y = 0; y < 32; y++) {
			for(int x = 0; x < 32; x++) {
				
				int true_count = 0;
				for(int y2 = (y * 50) + 22; y2 < ((y * 50) + 22) + 50; y++) {
					for(int x2 = ((x * 7) + 7); x2 < ((x * 7) + 7) + 7 ; x2++) {
						true_count += trajectories[(y2 * 237) + x2] == true ? 1 : 0;
					}
				}
				avr[(y * 32) + x] = true_count > 0 ? (float)true_count / 350f : 0.0f;
			}
		}
		
		return avr;
	}
	
	// 32 by 32
	public static float[] average_wear() {
		
		float avr[] = new float[32 * 32];
		
		for(int y = 0; y < 32; y++) {
			for(int x = 0; x < 32; x++) {
				
				float t_wear = 0.0f;
				for(int y2 = (y * 50) + 22; y2 < ((y * 50) + 22) + 50; y2++) {
					for(int x2 = ((x * 7) + 7); x2 < ((x * 7) + 7) + 7 ; x2++) {
						t_wear += pebbles[(y2 * 237) + x2].getWear2();
					}
				}
				avr[(y * 32) + x] = t_wear / 350.0f;
			}
		}
		
		return avr;
		
	}
	
	public static float[] average_bool_paging(int page_index) {
		float avr[] = new float[32 * 32];
		
		int interval = Statics.pebble_countY == 1622 ? 25 : 30;
		int offset = Statics.pebble_countY == 1622 ? 22 : 2;
		
		int y = page_index == 0 ? 0 : 32, limit = page_index == 0 ? 32 : 64;
		for(; y < limit; y++) {
			for(int x = 0; x < 32; x++) {
				
				int true_count = 0;
				for(int y2 = (y * interval) + offset; y2 < ((y * interval) + offset) + interval; y2++) {
					for(int x2 = ((x * 7) + 7); x2 < ((x * 7) + 7) + 7; x2++) {
						true_count += trajectories[(y2 * 237) + x2] == true ? 1 : 0;
					}
				}
				
				if(limit == 32)
					avr[(y * 32) + x] = true_count > 0 ? (float)true_count / 210f : 0.0f;
				else
					avr[( (y - 32) * 32) + x] = true_count > 0 ? (float)true_count / 210f : 0.0f;
					
			}
		}
		
		return avr;
	}
	
	public static float[] average_wear_paging(int page_index) {
		float avr[] = new float[32 * 32];
		
		int interval = Statics.pebble_countY == 1622 ? 25 : 30;
		int offset = Statics.pebble_countY == 1622 ? 22 : 2;
		
		int y = page_index == 0 ? 0 : 32, limit = page_index == 0 ? 32 : 64;
		for(; y < limit; y++) {
			for(int x = 0; x < 32; x++) {
				
				float t_wear = 0.0f;
				for(int y2 = (y * interval) + offset; y2 < ((y * interval) + offset) + interval; y2++) {
					for(int x2 = ((x * 7) + 7); x2 < ((x * 7) + 7) + 7; x2++) {
						t_wear += pebbles[(y2*237)+x2].getWear2();
					}
				}
				
				if(limit == 32)
					avr[(y * 32) + x] = t_wear / 210f;
				else
					avr[( (y - 32) * 32) + x] = t_wear / 210f;
			}
		}
		
		return avr;
	}
	
	public static int[] average_trajectory(boolean isSweep, int page) {
		
		boolean trajectory[] = new boolean[237 * 1622];
		
		ArrayList <Vector2> trajectList = isSweep ? Simulator.sweep_trajectory : Simulator.trajectory;
		
		for(int i = 0; i < trajectList.size(); i++	) {
			
			Vector2 pos = new Vector2(trajectList.get(i));
			pos.scl(100);
			
			int X = (int)(pos.x / 2.0f);
			int Y = Math.abs(1622 - Statics.pebble_countY)+(int)(pos.y / 2.0f);
			
			X = X >= Statics.pebble_countX ? Statics.pebble_countX-1 : X < 0 ? 0 : X;
			Y = Y >= Statics.pebble_countY ? Statics.pebble_countY-1 : Y < 0 ? 0 : Y;
			trajectory[(Y * Statics.pebble_countX) + X] = true;
		}
		
		int avr[] = new int[32 * 32];
		
		int y = FunctionUtils.size_y == 32 ? 0 : page == 0 ? 0 : 32;
		int limit = y == 0 ? 32 : 64;
		int interval = Statics.pebble_countY == 1622 ? (FunctionUtils.size_y == 32 ? 50 : 25) : 30;
		int offset = Statics.pebble_countY == 1622 ? 22 : 2;
		for(; y < limit; y++) {
			for (int x = 0; x< 32; x++) {
				
				boolean isPassed = false;
				for(int y2 = (y * interval) + offset; y2 < ((y * interval) + offset) + interval; y2++) {
					for(int x2 = ((x * 7) + 7); x2 < ((x*7)+7)+7; x2++) {
						isPassed = trajectory[(y2 * 237) + x2];
						
						if(isPassed)
							break;
					}
					
					if(isPassed)
						break;
				}
				if(page == 0)
					avr[(y * 32) + x] = isPassed ? 1 : 0;
				else
					avr[((y-32) * 32) + x] = isPassed ? 1 : 0;
			}
		}
		
		return avr;
		
	}

	public static void resetPebbles() {
		PebbleisInitialized = false;
		initializePebbles();
	}

	public static float getDistance(float x, float y, float x2, float y2) {
		return (float) (Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2)));
	}

	public static void checkVector(Vector2 vec) {
		if (vec.y < Statics.YVector_MAX)
			vec.set(0.0f, 0.0f);
	}

	public static float[] VectorToPosition(Vector2 vec, int curl) {

		Vector2 t_vec = new Vector2(vec);
		float length = (float) Math.pow(t_vec.len(), 2) / 2.0f / Statics.Stone_friction;
		t_vec.nor();

		t_vec.scl(length);

		Vector2 t_vec2 = new Vector2(t_vec.x + 2.375f, t_vec.y + 41.28f);

		if (curl == 0) {
			float x = (t_vec2.x - (t_vec2.y * 0.0335f) + 1.38614534375f) / 1.00112225f;
			float y = ((0.0335f * x) - 0.0795625f) + t_vec2.y;
			return new float[] { x, y };
		} else {
			float x = (t_vec2.x + (t_vec2.y * 0.0335f) - 1.38614534375f) / 1.00112225f;
			float y = ((-0.0335f * x) + 0.0795625f) + t_vec2.y;
			return new float[] { x, y };
		}

	}

	public static int getStoneState(float x, float y) {

		int state = 0;
		Vector2 vec = new Vector2(x - Statics.tee_X, y - Statics.tee_Y);

		if ((x > Statics.rink_x + Statics.Stone_size && x < (Statics.rink_x + Statics.width) - Statics.Stone_size)
				&& (y > 1.22f - Statics.Stone_size
						&& y < (Statics.playground_y + Statics.height) - Statics.Stone_size)) {

			state |= Statics.position_rink;
		}
		if ((x > Statics.rink_x + Statics.Stone_size && x < (Statics.rink_x + Statics.width) - Statics.Stone_size)
				&& (y > Statics.playground_y - Statics.Stone_size)
				&& y < (Statics.playground_y + Statics.playground_height) - Statics.Stone_size) {
			state |= Statics.position_playground;
		}

		if (vec.len() < 1.83f + Statics.Stone_size) {
			state |= Statics.position_house;
		}

		if ((state & Statics.position_house) != Statics.position_house
				&& (x > Statics.rink_x + Statics.Stone_size
						&& x < (Statics.rink_x + Statics.width) - Statics.Stone_size)
				&& (y > Statics.tee_Y + Statics.Stone_size
						&& y < (Statics.playground_y + Statics.playground_height) - Statics.Stone_size)) {
			state |= Statics.position_freeguard;
		}

		return state;

	}

	public static int getStoneState(Stone s) {
		Vector2 pos = s.getPosition();

		return getStoneState(pos.x, pos.y);
	}

	public static Vector2 createShot(float x, float y) {
		Vector2 shot = new Vector2(0, 0);
		float len = 0.0f;

		shot.set(x - 2.375f, y - 41.28f);
		len = shot.len();

		shot.nor();
		shot.scl((float) (Math.sqrt(len * 2.0f * Statics.Stone_friction)));

		return shot;
	}

	public static Vector2 createShot(Vector2 pos, int curl) {
		float tt = 0.0335f;
		Vector2 vec, tee = new Vector2(Statics.tee_X, Statics.tee_Y);

		if (curl == 1) {
			vec = createShot(1.22f + pos.x - tt * (pos.y - tee.y), tt * (pos.x - tee.x) + pos.y);
		} else {
			vec = createShot(-1.22f + pos.x + tt * (pos.y - tee.y), -tt * (pos.x - tee.x) + pos.y);
		}

		// checkVector(vec);
		return vec;
	}

	public static void destroyBody(World world, Stone[] s) {
		for (int i = 0; i < s.length; i++) {
			Stone stone = s[i];
			if (stone.getBody() != null) {
				world.destroyBody(stone.getBody());
			}
		}
	}

	public static int GetScore(ArrayList<Stone> stones) {
		int score[] = new int[2], loop;
		float dist[] = new float[16], f_near = 999.0f, a_near = 999.0f;

		for (loop = 0; loop < stones.size(); loop++) {
			Stone s = stones.get(loop);
			if (s.getY() == 0.0f)
				continue;

			float dists = FunctionUtils.getDistance(s.getX(), s.getY(), Statics.tee_X, Statics.tee_Y);

			if (dists > (1.83f + Statics.Stone_size))
				continue;

			if ((loop % 2) == 0 && dists < f_near)
				f_near = dists;
			else if ((loop % 2) == 1 && dists < a_near)
				a_near = dists;

			dist[loop] = dists;
		}

		loop = f_near < a_near ? 0 : 1;
		for (; loop < stones.size(); loop += 2) {
			if (dist[loop] == 0.0f)
				continue;

			if ((loop % 2) == 0) {
				if (dist[loop] < a_near)
					score[loop % 2]++;
			} else {
				if (dist[loop] < f_near)
					score[loop % 2]++;
			}
		}

		return score[0] > score[1] ? score[0] : (score[1] * -1);
	}

	public static ArrayList<Vector2> getTrajectory(float x, float y, int curl) {

		Vector2 vec = createShot(new Vector2(x, y), curl);
		Vector2 position = new Vector2(Statics.tee_X, Statics.rink_y);

		ArrayList<Vector2> positions = new ArrayList<Vector2>();
		float vx = vec.x, vy = vec.y;
		float len, len2, temp;
		boolean loop = true;

		while (loop) {
			loop = false;

			len = (vx * vx) + (vy * vy);
			if (len > 0.0144221268934656f) {
				loop = true;

				position.x += (vx / 100.0f);
				position.y += (vy / 100.0f);

				// if(position.y <= 11.28f)
				positions.add(new Vector2(position));

				len = (float) (Math.sqrt(len));
				temp = (float) (Math.pow(len - (Statics.Stone_friction / 100.0f), 2));
				len2 = (float) (Math.sqrt(temp
						/ (temp + (float) (Math.pow((Statics.Stone_friction / 100.0f) * (Statics.AngularSpeed), 2)))));
				if (curl == 1) {
					vx = (vx - (vx * (Statics.Stone_friction / 100.0f)
							- vy * (Statics.Stone_friction / 100.0f) * (Statics.AngularSpeed)) / len) / len2;
					vy = (vy - (vy * (Statics.Stone_friction / 100.0f)
							+ vx * (Statics.Stone_friction / 100.0f) * (Statics.AngularSpeed)) / len) / len2;
				} else {
					vx = (vx - (vx * (Statics.Stone_friction / 100.0f)
							+ vy * (Statics.Stone_friction / 100.0f) * (Statics.AngularSpeed)) / len) / len2;
					vy = (vy - (vy * (Statics.Stone_friction / 100.0f)
							- vx * (Statics.Stone_friction / 100.0f) * (Statics.AngularSpeed)) / len) / len2;
				}

			} else {
				vx = 0.0f;
				vy = 0.0f;
			}
		}

		return positions;
	}
	
	public static void saveFile(StringBuffer sb, String fileName) {
		
		try {
			
			File root = new File("./Data");
			if(!root.exists())
				root.mkdir();
			
			File f = new File("./Data/"+fileName);
			if(!f.exists())
				f.createNewFile();
			
			PrintWriter pw = new PrintWriter(f);
			pw.println(sb.toString());
			pw.flush();
			pw.close();
			
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
	}
	
}
