package simulator;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import gui.MainFrame;

public class Main {

	public static ArrayList<Message> messages = new ArrayList<Message>();
	public static PrintWriter pw;

	public static boolean isSendMsg = false;

	public static void main(String[] args) {
		FunctionUtils.resetPebbles();
		adjustXPos();
		
//		for(float y = 0; y < 14;  y += 1) {
//			float yError = 0.0f;
//			for(int i = 0; i < 100; i++) {
//				float x = MathUtils.random(1.0f, 3.75f);
//				//float y = MathUtils.random(0.0f, 14.0f);
//				int curl = 0;
//				//if( ())
//				//System.out.print("cur Param: "+ x +" " + y + " " + curl);
//				float[] tt = Simulator.simulate_pebble(x, y, curl, 0.0f, false);
//				
//				yError += (Math.abs(y - tt[1]));
//			}
//			
//			System.out.println(yError / 100.0f);
//			
////			float x = MathUtils.random(1.0f, 3.75f);
////			//float y = MathUtils.random(0.0f, 14.0f);
////			int curl = 0;
////			//if( ())
////			//System.out.print("cur Param: "+ x +" " + y + " " + curl);
////			float[] tt = Simulator.simulate_pebble(x, y, curl, 0.0f, false);
////			//System.out.print(" "+y + " " + tt[1]);
////			
////			System.out.println(String.format("%f %f %d %f %f",x, y, curl, Math.abs(x - tt[0]), Math.abs(y - tt[1])));
//		}
//		System.exit(0);

		ServerSocket s_soc;

		int port = 2228;
		if (args.length > 1) {
			if (args[0] != null)
				port = Integer.parseInt(args[0]);
		}
		try {

			s_soc = new ServerSocket(port);

			// 1:1 networking
			final Socket client = s_soc.accept();
			System.out.println(client.getInetAddress() + " has conneted");

			pw = new PrintWriter(client.getOutputStream());
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						try {

							String s = null;
							byte[] buffer = new byte[128];
							client.getInputStream().read(buffer);
							s = new String(buffer);

							if (s != null || !s.isEmpty() || !(s.length() < 2)) {
								String parsed[] = s.split(" ");

								if (parsed.length == 1) {
									parsed[0] = subString(parsed[0]);
								} else {
									for (int i = 1; i < parsed.length; i++) {
										parsed[i] = subString(parsed[i]);
									}
								}

								// System.out.println(s);
								messages.add(new Message(parsed));
							}

						} catch (Exception e) {
							try {
								client.close();
								s_soc.close();
								System.exit(-1);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}
					}

				}
			}).start();

		} catch (Exception e) {
		}

		while (true) {

			doCommand();
			if (!isSendMsg) {
				Simulator.simulate_pebble();
				if(Statics.isSweep)
					sweepImage();
			}

			if (Statics.environments_update) {
				Environments.updateEnvironments();
				Environments.updatePebbleFrost();
				Environments.updateWaterFilm();
			}

		}

	}
	
	public static void adjustXPos() {
		
		// curl == 0;
		float[]	tt = Simulator.simulate_pebble(2.375f, 4.88f, 0, 0.0f, false);
		float gap = 2.375f - tt[0];
		Statics.x_adjust_in = gap;
		
		tt = Simulator.simulate_pebble(2.375f, 4.88f, 1, 0.0f, false);
		gap = 2.375f - tt[0];
		Statics.x_adjust_out = gap-0.0725f;
		
	}

	public static void sweepImage() {

		if (!Statics.nowShoot)
			return;

		Stone s = Functions.stones_simulate.get(0);

		Vector2 position = new Vector2(s.getPosition());
		
		if(position.y > 32f)
			return;
		
		position.scl(100);
		int dy = Math.abs(1622 - Statics.pebble_countY) + (int)(position.y / 2.0f), dx = (int) (position.x/ 2.0f);
		
		dx = dx >= Statics.pebble_countX ? Statics.pebble_countX - 1 : dx < 0 ? 0 : dx;
		dy = dy >= Statics.pebble_countY ? Statics.pebble_countY - 1 : dy < 0 ? 0 : dy;

		dy += 15;

		for (int y = dy; y > (dy - 15); y--) {
			if(y < 0)
				break;
			
			for (int x = dx - 10; x < (dx + 10); x++) {
				if (x >= Statics.pebble_countX)
					continue;
				
				FunctionUtils.pebbles[(y * Statics.pebble_countX) + x].isSweep(true);;
			}
		}

	}

	public static void sendData() {
		try {
			isSendMsg = true;

			// Send Position
			Vector2 v = Functions.stones_simulate.get(0).getBody().getPosition();
			sendMsg("STONE_POS " + v.x + " " + v.y);
//
//			Thread.sleep(500);
//
//			// Send Wear
//			float[] wears = FunctionUtils.average_wear();
//			StringBuffer sb = new StringBuffer();
//			sb.append("ICESTATE");
//			for (int y = 0; y < FunctionUtils.size_y; y++) {
//				for (int x = 0; x < FunctionUtils.size_x; x++) {
//					sb.append(String.format(" %.4f", wears[(y * FunctionUtils.size_x) + x]));
//				}
//			}
//			sendMsg(sb.toString());
//
//			Thread.sleep(500);
//
//			// send Trajectory
//			sb = new StringBuffer();
//			int[] trajectory = FunctionUtils.average_trajectory(false, 0);
//
//			sb.append("TRAJECTORY");
//			for (int y = 0; y < FunctionUtils.size_y; y++) {
//				for (int x = 0; x < FunctionUtils.size_x; x++) {
//					sb.append(String.format(" %d", trajectory[(y * FunctionUtils.size_x) + x]));
//				}
//			}
//
//			sendMsg(sb.toString());
//
//			// Send Sweep
//			Thread.sleep(500);
//			sb = new StringBuffer();
//			trajectory = FunctionUtils.average_trajectory(true, 0);
//
//			sb.append("SWEEP_TRAJECTORY");
//			for (int y = 0; y < FunctionUtils.size_y; y++) {
//				for (int x = 0; x < FunctionUtils.size_x; x++) {
//					sb.append(String.format(" %d", trajectory[(y * FunctionUtils.size_x) + x]));
//				}
//			}
//
//			sendMsg(sb.toString());

			//

		} catch (Exception e) {
		}

	}

	public static void sendMsg(String msg) {

		pw.println(msg);
		pw.flush();

		// System.out.println("Sended: "+msg);

	}

	public static String subString(String s) {
		// System.out.println(s);
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if ((c >= 97 && c <= 122) || (c >= 65 && c <= 90) || (c >= 48 && c <= 57)) {
				count++;
			}

		}
		// System.out.println(s.substring(0, count));
		return s.substring(0, count);
	}

	public static void doCommand() {
		if (messages.size() < 1)
			return;

		Message cur_m = messages.get(0);
		if (cur_m == null)
			return;

		try {
			switch (cur_m.getType()) {
			
			case "CURL":
				Statics.curl_adjust = Float.parseFloat(cur_m.getContents()[1]);
				System.out.println("current curl: "+Statics.curl_adjust);
				adjustXPos();
				break;
			
			case "GUI":
				if (MainFrame.nowGUIRunning)
					MainFrame.getInstance().stopFrame();
				else
					MainFrame.getInstance().startFrame();

				break;
			case "RESUME":
				isSendMsg = false;

				if (!Statics.nowShoot)
					sendMsg("SIMULATION_END");

				break;

			case "SIZE":
				String[] d = cur_m.contents;
				int size = Integer.parseInt(d[1]);
				FunctionUtils.size_y = size;
				System.out.println("Set size OK ? Size : " + size);
				sendMsg("OK");
				break;

			case "SWEEPON":
				Statics.isSweep = true;
				break;

			case "SWEEPOFF":
				Statics.isSweep = false;
				break;

			case "START":
				Statics.environments_update = true;
				System.out.println("Environment update started");
				break;
			case "STOP":
				Statics.environments_update = false;
				System.out.println("Environment update stopped");
				break;
			case "RESET":
				FunctionUtils.resetPebbles();
				System.out.println("Ice reset ok");
				sendMsg("OK");
				break;
			case "GETICE":
				try {

					String[] data1 = cur_m.getContents();
					int page_num = 0;

					if (data1.length > 1) {
						page_num = Integer.parseInt(data1[1]);
					}

					// System.out.println("Page num : "+page_num);

					StringBuffer sb = new StringBuffer();

					float[] ice_state = page_num == 0 ? FunctionUtils.average_wear()
							: FunctionUtils.average_wear_paging(page_num);

					for (int y = 0; y < 32; y++) {
						for (int x = 0; x < 32; x++) {
							sb.append(String.format("%.4f ", ice_state[(y * 32) + x]));
						}
					}

					sendMsg(sb.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				// get ice state
				break;

			case "SIMULATE":
				StringBuffer sb = new StringBuffer();
				String[] data2 = cur_m.getContents();
				float x2 = Float.parseFloat(data2[1]), y2 = Float.parseFloat(data2[2]),
						uncertainty2 = Float.parseFloat(data2[4]);
				int curl2 = (int) Float.parseFloat(data2[3]);
				float[] lastposition = Simulator.simulate_pebble(x2, y2, curl2, uncertainty2,
						Boolean.parseBoolean(data2[5]));
				sb = new StringBuffer();
				sb.append(String.format("RESULT %f %f", lastposition[0], lastposition[1]));

				sendMsg(sb.toString());

				break;

			case "SIMULATE2":
				String[] data = cur_m.getContents();

				float x = Float.parseFloat(data[1]), y = Float.parseFloat(data[2]),
						uncertainty = Float.parseFloat(data[4]);
				int curl = (int) Float.parseFloat(data[3]);
				Simulator.isWear = Boolean.parseBoolean(data[5]);
				// simulate
				Functions.setWorld();
				Simulator.trajectory.clear();
				Simulator.sweep_trajectory.clear();
				Simulator.time_interval = 0.0f;

				Stone s = new Stone(Functions.world, Statics.tee_X, Statics.rink_y, 0);

//				x += curl == 1 ? 0.3f : 0f;
//				if( x < 2.375f && curl == 0)
//					x -= 0.15f;

//				Vector2 s_vec = FunctionUtils.createShot(new Vector2(curl == 1 ? x + 0.87f : x - 0.87f, y), curl);

//				float side_adjust = Math.abs(2.375f - x) / 42.5f;
//				float updown_adjust = Math.abs(4.88f - y) / 42.5f;
//				
//				side_adjust /= 12.5f;
//				updown_adjust /= 12.5f;

				float side_adjust = Math.abs(2.375f - x) / 42.5f;
				float updown_adjust = Math.abs(11.28f - y) / 42.5f;

				side_adjust /= 2f;
				updown_adjust /= 4f;
				Vector2 vec = FunctionUtils.createShot(new Vector2(curl == 1 ? x + 0.87f : x - 0.87f, y), curl);
				vec = Simulator.uncertainty(vec, uncertainty, curl);
				float power_adjust = Simulator.isWear ? 1.06f : 1.1f;

				s.getBody().setLinearVelocity(vec.scl(0.69f).scl(power_adjust + side_adjust, 1.0f + updown_adjust));

//				s.getBody().setLinearVelocity(vec.scl((curl == 1 ? Statics.power_adjust
//						: (Statics.useWearMode ? Statics.power_adjust + 0.01f : Statics.power_adjust - 0.005f))
//						+ (x > 2.375f ? 0.0f : -side_adjust)
//						+ ((y <= 3.05f) ? -updown_adjust : (y >= 6.71f) ? updown_adjust : 0.0f)));
				s.getBody().setAngularVelocity(
						curl == 0 ? (float) Math.toRadians(380) : ((float) Math.toRadians(380) * -1));
				Simulator.prev = Vector2.Zero;
				Functions.stones_simulate.add(s);
				Statics.nowShoot = true;
				System.out.println("Now, simulation started.");

				break;
			case "TIMESTEP":
				// set time step
				Statics.information_transform_interval = Float.parseFloat(cur_m.getContents()[1]);
				System.out.println("Time step setted : " + Statics.information_transform_interval);
				break;

			}

			messages.remove(0);
			cur_m = null;

		} catch (Exception e) {
			System.out.println("Command process error!");
			e.printStackTrace();
			System.exit(-1);
		}

	}

}
