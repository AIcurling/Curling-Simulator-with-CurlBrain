package simulator;

import java.util.ArrayList;

import org.apache.commons.math3.distribution.NormalDistribution;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Simulator {

	public static boolean isWear = false;
	public static ArrayList<Vector2> trajectory = new ArrayList<Vector2>();
	public static ArrayList<Vector2> sweep_trajectory = new ArrayList<Vector2>();

	public static Vector2 uncertainty(Vector2 vec, float uncertainty, int curl) {

		if (uncertainty <= 0.0f)
			return vec;

		NormalDistribution nor_x = new NormalDistribution(0.0, uncertainty / 2.0);
		NormalDistribution nor_y = new NormalDistribution(0.0, uncertainty * 2.0);

		Vector2 teeV = FunctionUtils.createShot(new Vector2(curl == 1 ? 2.375f + 0.87f : 2.375f - 0.87f, 4.88f), curl);
		Vector2 shotPos = new Vector2(curl == 1 ? 2.375f + 0.87f : 2.75f - 0.87f, 4.88f);

		shotPos.x += (float) nor_x.sample();
		shotPos.y += (float) nor_y.sample();

		Vector2 shotVec = FunctionUtils.createShot(shotPos, curl);

		vec.x += (teeV.x - shotVec.x);
		vec.y += (teeV.y - shotVec.y);

		return vec;
	}

	public static Vector2 prev;
	public static float time_interval = 0.0f;
	public static void simulate_pebble() {

		if (!Statics.nowShoot)
			return;

		FunctionUtils.initializePebbles();
		Stone s = Functions.stones_simulate.get(0);
		if (s.getPosition().y < 32f && s.getBody() != null)
			Functions.setPebbles(s, isWear);

		trajectory.add(s.getPosition());
		if (Statics.isSweep)
			sweep_trajectory.add(s.getPosition());

		Functions.world.step(Statics.timeStep_1000, Statics.Iteration_v, Statics.Iteration_p);

		Vector2 stone_pos = s.getPosition();
		if (stone_pos.y <= -10.0f || stone_pos.x <= 0.145f || stone_pos.x >= 4.6f) {
			s.getBody().setLinearVelocity(Vector2.Zero);
			s.getBody().setAngularVelocity(0.0f);

			trajectory.add(s.getPosition());
			if (Statics.isSweep)
				sweep_trajectory.add(s.getPosition());

			Statics.nowShoot = false;
			Main.sendData();
			Functions.destroyWorld();
			return;
		}

		if (s.getBody() != null && s.getPosition().y < 32.44f) {
			Functions.calcFriction(s);

			if (s.getBody().getLinearVelocity().len() < 0.1f) {
				s.getBody().setLinearVelocity(Vector2.Zero);
				s.getBody().setAngularVelocity(0.0f);

				trajectory.add(s.getPosition());
				if (Statics.isSweep)
					sweep_trajectory.add(s.getPosition());

				Statics.nowShoot = false;
				Main.sendData();
				Functions.destroyWorld();
				return;
			}
		}

		if (s.getBody() != null) {
			Vector2 s_pos = s.getPosition();
			if(s_pos.x <= 0.145f || s_pos.x >= 4.6f) {
				s.setLastPostiion(s.getPosition());
				trajectory.add(s.getPosition());
				if(Statics.isSweep)
					sweep_trajectory.add(s.getPosition());
				
				Statics.nowShoot = false;
				Main.sendData();
				Functions.destroyWorld();
				return;
			} else if (s.getBody().isAwake() == true) {
				if (prev.equals(s.getPosition())) {
					Statics.nowShoot = false;
					trajectory.add(s.getPosition());
					if (Statics.isSweep)
						sweep_trajectory.add(s.getPosition());

					Statics.nowShoot = false;
					Main.sendData();
					Functions.destroyWorld();

					return;
				}
				time_interval += 0.013636425620116f;
				if(time_interval >= Statics.information_transform_interval) {
					Main.sendData();
					time_interval = 0.0f;
				}
				
				prev = new Vector2(s.getPosition());
				return;
			} else {
				s.setLastPostiion(s.getPosition());

				trajectory.add(s.getPosition());
				if (Statics.isSweep)
					sweep_trajectory.add(s.getPosition());

				Statics.nowShoot = false;
				Main.sendData();
				Functions.destroyWorld();
				return;
			}
		} else {
			s.setPosition(s.getPosition());
			trajectory.add(s.getPosition());
			if (Statics.isSweep)
				sweep_trajectory.add(s.getPosition());

			Statics.nowShoot = false;
			Main.sendData();
			Functions.destroyWorld();
			return;
		}
	}
	
	public static float[] simulate_pebble(float x, float y, int curl, float uncertainty, boolean isWear) {
		FunctionUtils.initializePebbles();

//		int stoneState = 0;
		boolean loop = true;

		World world = new World(Vector2.Zero, true);
		Stone s = new Stone(world, Statics.tee_X, Statics.rink_y, 0);
		Vector2 prev = Vector2.Zero;

		x += curl == 1 ? (-0.023f) : (-0.056f);
		x += curl == 1 ? Statics.x_adjust_out : Statics.x_adjust_in;
		//Vector2 s_vec = FunctionUtils.createShot(new Vector2(curl == 1 ? x + 0.65f : x - 0.65f, y), curl);
		//System.out.println(String.format("Input shot params : %f %f %d", s_vec.x, s_vec.y, curl));
		//s_vec = uncertainty(s_vec, uncertainty, curl);
		//System.out.println(String.format("Output shot params : %f %f %d", s_vec.x, s_vec.y, curl));
		
		//float side_adjust = Math.abs(2.375f - x) / 42.5f;
		float side_adjust = Math.abs(2.375f - x) / 42.5f;
		float updown_adjust = Math.abs(11.28f - y) / 42.5f;

		side_adjust /= 2f;
		updown_adjust /= 4f;

//		System.out.println(side_adjust +" " + updown_adjust);
//		Vector2 vec = FunctionUtils.createShot(new Vector2(curl == 1 ? x + 0.87f : x - 0.87f, y), curl);
		Vector2 vec = FunctionUtils.createShot(new Vector2(curl == 1 ? x + 0.87f : x - 0.87f, y), curl);
//		sPebble.getBody().setLinearVelocity(vec.scl( (Statics.curl == 1 ? Statics.power_adjust :  (Statics.useWearMode ? Statics.power_adjust + 0.01f : Statics.power_adjust - 0.008f))
//				+ (x > 2.375f? side_adjust : side_adjust)
//				+ (y < 4.88f ? updown_adjust-side_adjust : updown_adjust-side_adjust)));
		
		float power_adjust = isWear ? 1.06f : 1.1f;
//		s.getBody()
//				.setLinearVelocity(vec.scl((curl == 1 ? power_adjust
//						: (isWear ? power_adjust + 0.01f : power_adjust - 0.008f))
//						+ (x > 2.375f ? 0.0f : 0.0f)
//						+ ((y <= 3.05f) ? -updown_adjust : (y >= 6.71f) ? updown_adjust : 0.0f)));
		
		s.getBody().setLinearVelocity(vec.scl(0.69f).scl(power_adjust+side_adjust, 1.0f+updown_adjust));
		s.getBody().setAngularVelocity(curl == 0 ? (float) Math.toRadians(380) : ((float) Math.toRadians(380) * -1));

		while (loop) {

			if (s.getPosition().y < 32f)
				Functions.setPebbles(s, isWear);
			
			{// Trajectories
				
				Vector2 pos = new Vector2(s.getPosition());
				pos.scl(100);
				int tX = (int)(pos.x / 2.0f);
				int tY = Math.abs(1622 - Statics.pebble_countY)+(int)(pos.y / 2.0f);
				
				tX = tX >= Statics.pebble_countX ? Statics.pebble_countX-1 : tX < 0 ? 0 : tX;
				tY = tY >= Statics.pebble_countY ? Statics.pebble_countY-1 : tY < 0 ? 0 : tY;
				FunctionUtils.trajectories[(tY * Statics.pebble_countX) + tX] = true;
				
			}
			
			world.step(Statics.timeStep_1000, Statics.Iteration_v, Statics.Iteration_p);
			
			Vector2 stone_pos = s.getPosition();
			if(stone_pos.x <= 0.145f || stone_pos.x >= 4.6f) {
				s.getBody().setLinearVelocity(Vector2.Zero);
				s.getBody().setAngularVelocity(0.0f);
				prev = new Vector2(stone_pos);
				loop = false;
				break;
			}
			
			
			if (s.getBody() != null && s.getPosition().y < 32.44f) {
				Functions.calcFriction(s);

				if (s.getBody().getLinearVelocity().len() < 0.1f) {
					s.getBody().setLinearVelocity(Vector2.Zero);
					s.getBody().setAngularVelocity(0.0f);
				}
			}

			if (s.getBody() != null) {
//				stoneState = FunctionUtils.getStoneState(s);
//				if (stoneState == 0) {
//					s.setLastPostiion(s.getPosition());
//					world.destroyBody(s.getBody());
//					s.setBody(null);
//				} else 
					if (s.getBody().isAwake() == true) {
					if (prev.equals(s.getPosition()))
						loop = false;

					prev = new Vector2(s.getPosition());
					continue;
				} else {
					s.setLastPostiion(s.getPosition());
					loop = false;
				}
			}else {
				s.setPosition(s.getPosition());
				loop = false;
			}
		}

		world.dispose();
		return new float[] { prev.x, prev.y };
	}
}
