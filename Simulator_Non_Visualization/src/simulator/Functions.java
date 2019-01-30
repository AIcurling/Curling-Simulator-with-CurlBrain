package simulator;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Functions {

	public static World world = new World(new Vector2(0.0f, 0.0f), true);
	public static volatile ArrayList<Stone> stones_origin = new ArrayList<Stone>();
	public static volatile ArrayList<Stone> stones_simulate = new ArrayList<Stone>();

	public static int first = 0;
	public static int End = 0;
	public static int Max_end = 0;

	public static float Stone_mass = 19.96f;
	public static float Gravity = 9.8f;
	public static float normal_force = 195.74073457163246f;
	public static float normal_force_dt = normal_force * Statics.timeStep_1000;
	public static float Maximum_staticFriction = 0.02f;
	public static float KineticFriction = 0.01f;
	public static float Maximum_friction = Maximum_staticFriction * normal_force * Statics.timeStep_1000;
	public static float Kinetic_friction = (KineticFriction * normal_force * Statics.timeStep_1000) / 2.0f;
	public static float ice_friction_coefficient = 0.0046f;
	public static float omega_velocity = Statics.AngularSpeed * Statics.Stone_size * Statics.timeStep_1000;

	public static void setPebbles(Stone s, boolean isWear) {

		RunningBand[] runningBands = s.getRunningBands();

		for (int i = 0; i < runningBands.length; i++) {
			RunningBand rBand = runningBands[i];

			if (rBand.getPebble() != null) {
				if (s.getBody() == null)
					continue;

				Vector2 rBand_position = rBand.getCurrentPosition(s.getBody().getAngle());
				rBand_position.add(s.getBody().getPosition());

				Pebble p = rBand.getPebble();

				float dist = FunctionUtils.getDistance(rBand_position.x, rBand_position.y, p.getX(), p.getY());
				float angle = rBand.getAngle(s.getBody().getAngle());
				boolean isFront = angle > 180.0f ? true : false;

				if (dist > 0.0003141592f) {

					if (isFront) {
						Vector2 scratch = rBand.getCurrentPosition(s.getBody().getAngle());
						float scratch_angle = scratch.angle();
						scratch_angle -= 90.0f;
						scratch.set((float) Math.cos(Math.toRadians(scratch_angle)),
								(float) Math.sin(Math.toRadians(scratch_angle)));
						p.setScratch(scratch);

						float limits = s.getBody().getLinearVelocity().len() * Statics.timeStep_1000;
						if (limits > Kinetic_friction) {
							if (isWear) {
								p.updateWear();
								FunctionUtils.pebbles[p.getIndex()].updateWear();
							}
						}

					} else {
						p.setScratch(Vector2.Zero);
					}

					rBand_position.scl(100);

					int dy = Math.abs(1622 - Statics.pebble_countY) + (int) ((rBand_position.y) / 2.0f),
							dx = (int) ((rBand_position.x) / 2.0f);

					dx = dx >= Statics.pebble_countX ? Statics.pebble_countX - 1 : dx < 0 ? 0 : dx;
					dy = dy >= Statics.pebble_countY ? Statics.pebble_countY - 1 : dy < 0 ? 0 : dy;

					Pebble p2 = FunctionUtils.pebbles[(dy * 237) + dx];
					rBand.setPebble(p2);
				} else {
					rBand.setPebble(null);
				}

			} else {
				if (s.getBody() == null)
					return;

				Vector2 rBand_position = rBand.getCurrentPosition(s.getBody().getAngle());
				rBand_position.add(s.getBody().getPosition());
				rBand_position.scl(100);

				int dy = Math.abs(1622 - Statics.pebble_countY) + (int) ((rBand_position.y) / 2.0f),
						dx = (int) ((rBand_position.x) / 2.0f);

				dx = dx >= Statics.pebble_countX ? Statics.pebble_countX - 1 : dx < 0 ? 0 : dx;
				dy = dy >= Statics.pebble_countY ? Statics.pebble_countY - 1 : dy < 0 ? 0 : dy;

				Pebble p2 = FunctionUtils.pebbles[(dy * 237) + dx];
				rBand.setPebble(p2);
				p2.increateContactCount();
			}

		}

	}

	public static void calcFriction(Stone s) {
		if (s.getBody().getLinearVelocity().len2() == 0.0f)
			return;

		RunningBand[] runningBands = s.getRunningBands();

		if (s.getBody() == null)
			return;

		for (int i = 0; i < runningBands.length; i++) {

			RunningBand rBand = runningBands[i];
			Pebble p = rBand.getPebble();

//			if(p != null)
//				System.out.println(p.getWear());

			// float friction_base = 0.02f
			// System.out.println(friction_base);
			float kinematic_friction = 0.02f / Math.abs(s.getBody().getLinearVelocity().len());
			kinematic_friction = MathUtils.clamp(kinematic_friction, 0.01f, 0.02f);
			if (p == null)
				continue;

			Vector2 rBand_position = rBand.getCurrentPosition(s.getBody().getAngle());
			rBand_position.add(s.getBody().getPosition());

			float dist = FunctionUtils.getDistance(rBand_position.x, rBand_position.y, p.getX(), p.getY());
			if (dist > 0.02641592f) {
				rBand.setPebble(null);
				continue;
			}

			float angle = rBand.getAngle(s.getBody().getAngle());
			boolean isFront = angle > 180.0f ? true : false;
			angle = isFront ? angle - 180f : angle;

			float friction_dt = kinematic_friction * normal_force_dt;
			friction_dt = isFront ? (float) ((friction_dt - (Math.sin(angle)) * Statics.timeStep_1000))
					: (float) ((friction_dt + (Math.sin(angle)) * Statics.timeStep_1000));
			friction_dt = Math.abs(friction_dt);

			Vector2 rBand_friction = rBand.getCurrentPosition(s.getBody().getAngle());
			float rBf_angle = rBand_friction.angle();
			rBf_angle = s.getBody().getAngularVelocity() < 0 ? rBf_angle - 90.0f : rBf_angle + 90.0f;
			rBand_friction.set((float) Math.cos(Math.toRadians(rBf_angle)),
					(float) Math.sin(Math.toRadians(rBf_angle)));
			rBand_friction.set(-1 * rBand_friction.y, rBand_friction.x);
			rBand_friction.set(s.getBody().getAngularVelocity() < 0 ? rBand_friction.x : -rBand_friction.x,
					rBand_friction.y);

			friction_dt *= p.getWear();
			friction_dt -= (Environments.WaterFilm2Friction(p != null ? p.getWater() : 0.0f) * 0.005f);

			if (Statics.isSweep)
				friction_dt *= 0.9f;

			float limits = s.getBody().getLinearVelocity().len() * Statics.timeStep_1000;
			if (limits > Kinetic_friction) {

				Vector2 t_vec = new Vector2(s.getBody().getLinearVelocity());
				rBand_friction.add(new Vector2(t_vec).nor());
				rBand_friction.nor();
				rBand_friction.scl(friction_dt);

				if (Statics.forSH) {
					if (s.getBody().getAngularVelocity() < 0) {
						rBand_friction.scl(rBand_friction.x > 0 ? 1.27f : 1.0f, 0.95f);
					}else {
						rBand_friction.scl(rBand_friction.x < 0 ? 1.27f : 1.0f, 0.95f);
					}
				} else {
					float adjust = (0.001f * ((float) Math.pow(t_vec.len() - 18.5f, 2))) + Statics.curl_adjust;
					// adjust = MathUtils.clamp(adjust, 0f, 0.03f);
					if (s.getBody().getAngularVelocity() < 0) {
						rBand_friction.scl(rBand_friction.x > 0 ? 1.27f + adjust : 1.0f, 0.95f);
					} else {
						rBand_friction.scl(rBand_friction.x < 0 ? 1.27f + adjust : 1.0f, 0.95f);
					}
				}

				t_vec.sub(rBand_friction);

				Vector2 t_vec2 = new Vector2(s.getBody().getLinearVelocity());
				Vector2 c_vec = new Vector2();

				if (!isFront && p.getScratch().angle() != 0.0f) {
					t_vec2.nor();
					t_vec2.set(s.getBody().getAngularVelocity() < 0 ? -t_vec2.x : t_vec2.x, t_vec2.y);
					t_vec2 = t_vec2.sub(p.getScratch());
					t_vec2.nor();

					float difference_of_angle = (t_vec2.angle() - p.getScratch().angle());

					if (difference_of_angle > 0) {
						difference_of_angle = s.getBody().getAngularVelocity() > 0 ? difference_of_angle - 360.0f
								: difference_of_angle;
						c_vec.set((float) Math.cos(Math.toRadians(difference_of_angle)),
								(float) Math.sin(Math.toRadians(difference_of_angle)));
						c_vec.scl(
								s.getBody().getAngularVelocity() > 0 ? -(Statics.AngularSpeed * 3.141592f) * friction_dt
										: (Statics.AngularSpeed * 3.141592f) * friction_dt);

						float len = t_vec.len();
						t_vec = t_vec.add(c_vec);
						t_vec.nor();
						t_vec.scl(len);
					}
				}
				s.getBody().setLinearVelocity(t_vec);
			} else {
				s.getBody().setLinearVelocity(Vector2.Zero);
				s.getBody().setAngularVelocity(0.0f);
			}
		}
	}

	public static void setWorld(ArrayList<Stone> stones_origin, ArrayList<Stone> target) {

		world = new World(Vector2.Zero, true);

		for (int i = 0; i < stones_origin.size(); i++) {
			target.add(new Stone(world, stones_origin.get(i), i % 2));
		}

	}

	public static void setWorld() {
		world = new World(Vector2.Zero, true);
	}

	public static void destroyWorld() {
		for (int i = 0; i < stones_simulate.size(); i++) {
			if (stones_simulate.get(i).getBody() == null) {
				continue;
			}

			world.destroyBody(stones_simulate.get(i).getBody());
			stones_simulate.get(i).setBody(null);
		}

		stones_simulate.clear();
		Functions.world.dispose();
		Functions.world = null;
	}

}
