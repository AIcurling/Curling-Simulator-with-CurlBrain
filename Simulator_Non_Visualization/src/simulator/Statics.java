package simulator;

public class Statics {
	
	public static boolean nowShoot = false;
	public static boolean environments_update = false;
	public static boolean isSweep = false;
	public static boolean forSH = false;
	
	public static float information_transform_interval = 0.1f;
	
	public static int pebble_countX = 237;
	// limit 0
	//public static int pebble_countY = 1622;
	// limit -6
	public static int pebble_countY = 1922;
	
	public static final float Stone_size = 0.145f;
	public static final float Stone_density = 10.0f;
//	public static final float Stone_restitution = 1.0f;
	public static final float Stone_restitution = 0.95f;
	public static float Stone_friction = 12.009216f;
	public static final short Stone_category = 0x0001;
	public static final short Pebble_category = 0x0010;
	public static final short RunningBand_category = 0x0100;
	
	public static boolean useWearMode = false;
	public static int rBandsCount = 16;
	public static int rBandsbundle = useWearMode ? 2 : 1;
	public static float power_adjust = useWearMode ? 1.29f : 1.11f;
	
	public static final short Stone_maskbit = Stone_category;
	public static final short Pebble_maskbit = Pebble_category;
	public static final short RunningBand_maskbit = 0x0100;
	public static final float YVector_MAX = -33.7419f;
	public static float AngularSpeed = 0.066696f;
	public static float curl_adjust = -0.08f;
	
	public static float x_adjust_out = 0.0f;
	public static float x_adjust_in = 0.0f;
	//public static float AngularSpeed = 0.1f;
	
//	public static float AngularSpeed_out = 0.104464811379097f;
//	public static float AngularSpeed_in = 0.104464811379097f;
	
	public static float AngularSpeed_out = 0.0826f;
	public static float AngularSpeed_in = 0.0826f;
	
	public static float AngularSpeed_out_offset = 0.0f;
	public static float AngularSpeed_in_offset = 0.0f;
	
	//public static float AngularSpeed_in = 0.121145566239316f;
	
	public static final float timeStep_100 = 1.0f / 100.0f;
	public static final float timeStep_1000 = 1.0f/ 1000.0f;
	public static final int Iteration_v = 10;
	public static final int Iteration_p = 10;
	
	public static final float mu = 0.0f;
	public static final float sigX = 0.0725f;
	public static final float sigY = 0.2900f;
//	public static final float sigX = 0.0f;
//	public static final float sigY = 0.0f;
	
	public static final float width = 4.75f;
	public static final float height = 42.5f;
	public static final float rink_x = 0.0f;
	public static final float rink_y = 41.28f;
	public static final float playground_height = 8.23f;
	public static final float playground_y = 3.05f;
	
	public static final float tee_X = 2.375f;
	public static final float tee_Y = 4.88f;
	
	public static final int position_rink = 0x0001;
	public static final int position_playground = position_rink << 1;
	public static final int position_freeguard = position_playground << 1;
	public static final int position_house = position_freeguard << 1;
}
