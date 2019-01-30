package simulator;

import org.apache.commons.math3.distribution.NormalDistribution;

public class Environments {

	public static float temperature = 8.0f;
	public static float RelatedHumidity = 30.0f;
	public static float ice_temperature = -8.0f;
	
	public static void updateEnvironments() {
		
		NormalDistribution temp_dist = new NormalDistribution(0f, 0.1f);
		NormalDistribution humid_dist = new NormalDistribution(0f, temperature / RelatedHumidity);
		
		temperature += temp_dist.sample();
		if(temperature < 7f)
			temperature = 7.0f;
		
		RelatedHumidity += humid_dist.sample();
		if(RelatedHumidity < 5f)
			RelatedHumidity = 5.0f;
		
		if(RelatedHumidity > 99.9f)
			RelatedHumidity = 99.9f;
		
		
	}
	
	public static float Calculation_dewPoint(float temperature, float humidity) {

		double H = (Math.log10(humidity) - 2.0d) / 0.4343d + (17.62d * temperature) / (243.12d + temperature);

		return (float) ((243.12d * H) / (17.62d - H));

	}

	public static float Calculation_frostPoint(float temperature, float dewPoint) {

		float dew_kelbin = 273.15f + dewPoint;
		float temp_kelbin = 273.15f + temperature;

		double frost_point_kelbin = dew_kelbin - temp_kelbin
				+ 2671.02d / ((2954.61d / temp_kelbin) + 2.193665d * Math.log(temp_kelbin) - 13.3448d);

		return (float) (frost_point_kelbin - 273.15d);
	}
	
	public static float getMeltedWaterRate(float energy) {
		
		return getKilogramOfMeltedWater(energy) / (Statics.width * Statics.height);
		
	}
	
	public static float WaterFilm2Friction(float x) {
		
//		if(x < 6)
//			return 0.0f;
		
		
		float coef =  (-0.00007f*(x * x * x)) + (0.0032f * (x * x)) - (0.0378f * x);//+ 0.001568f;
		
		return (x < 0.004) ? coef : -coef;
		
	}
	
	public static float Heat2Joules(float heat) {
		
		float temp = heat * 1899.100534716f;
		
		float precision = 18.0f;
		if(!Float.isNaN(temp)) {
			
			return (float)(Math.round(temp * precision)) / precision; 
			
			
		}
		
		return 0.0f;
		
	}
	
	public static float getGramOfMeltedWater(float joules) {
		
		return joules / 334.0f;
		
	}
	
	public static float getKilogramOfMeltedWater(float joules) {
		
		return (joules / 334.0f) / 1000.0f;
		
	}
	
	public static void updateWaterFilm() {
		
		if(!FunctionUtils.PebbleisInitialized)
			FunctionUtils.initializePebbles();
		
		float dewPoint = Calculation_dewPoint(temperature,RelatedHumidity);
		float heatIndex =Calculation_Heat(temperature, RelatedHumidity);
		float coolingIndex = Calculation_Heat(7.0f, RelatedHumidity);
		
		heatIndex -= coolingIndex;
		
		if(heatIndex < dewPoint)
			return;
		
		float water = Heat2Joules(heatIndex);
		water = getKilogramOfMeltedWater(water);
		water /= (float)(42.5f * 4.75f) ;
		
		water *= 100.0f;
		for(int y = 0; y < 1622; y++) {
			
			for(int x = 0; x < 237; x++) {
				
				FunctionUtils.pebbles[(y * 237) + x].updateWater(water);
				FunctionUtils.pebbles[(y * 237) + x].updateWear(-water);
				
			}
			
		}
		if(RelatedHumidity < 99.9f)
			RelatedHumidity += water;
		
		if(RelatedHumidity >= 100.0f)
			RelatedHumidity = 95.0f;
		
		if(RelatedHumidity <= 0.0f)
			RelatedHumidity = 5.0f;
		//System.out.println("Melted Water : "+meltedWater);
		

		
	}
	
	public static void updatePebbleFrost() {
		
		
		if(!FunctionUtils.PebbleisInitialized)
			FunctionUtils.initializePebbles();
		
		float frost_point = Environments.Calculation_frostPoint(temperature, Environments.Calculation_dewPoint(temperature, RelatedHumidity));	
		float frozen_heat = frost_point - Environments.ice_temperature;
		
		float frost_rate = frozen_heat / frost_point;
		float humid_rate = (RelatedHumidity * frost_rate) / (42.5f * 4.75f);
		humid_rate = Math.abs(humid_rate);
		//System.out.println("frost_rate :"+frost_rate);
		//System.out.println("humid rate :"+( (RelatedHumidity * frost_rate)) / (42.5f * 4.75f));

		for(int y = 0; y < 1622; y ++) {
			
			for(int x = 0; x < 237; x++) {
				
				
				FunctionUtils.pebbles[(y * 237) + x].updateWear(humid_rate);
				if(FunctionUtils.pebbles[(y * 237) + x].getWear() <= 0.0f)
					FunctionUtils.pebbles[(y * 237) + x].setWear(0.0f);
				
				if(FunctionUtils.pebbles[(y * 237) + x].getWater() >= 1.0f)
					FunctionUtils.pebbles[(y * 237) + x].setWear(1.0f);
					
				
			}
			
		}
		
		//System.out.println(frost_rate);
		
		if(RelatedHumidity > 0) {
			float h_rate = RelatedHumidity * (humid_rate * 0.01f);
			RelatedHumidity -= h_rate;
			
			if(RelatedHumidity >= 100.0f)
				RelatedHumidity = 95.0f;
			
			if(RelatedHumidity <= 0.0f)
				RelatedHumidity = 5.0f;
		}
		
		
	}
	
	public static float Calculation_Heat(float temperature, float relatedHumidity) {
		
		float hi;
		float hifinal;
		float tempair_in_fahrenheit;
		float hitemp;
		float fptemp;
		
		if(relatedHumidity > 100) {
			return 0.0f;
		}else if(relatedHumidity < 0) {
			return 0.0f;
		}else if(temperature <= 4.44f) {
			hi = temperature;
		}else {
			
			tempair_in_fahrenheit = 1.80f * temperature + 32.0f;
			hitemp = 61.0f + ((tempair_in_fahrenheit-68.0f)*1.2f)+(relatedHumidity * 0.094f);
			fptemp = temperature;
			hifinal = 0.5f* (fptemp + hitemp);
			
			if(hifinal > 79.0f) {
				
				hi = -42.379f + 2.04901523f * tempair_in_fahrenheit + 10.14333127f * relatedHumidity - 0.22475541f * tempair_in_fahrenheit * relatedHumidity - 6.83783f * (float)(Math.pow(10,  -3) * (Math.pow(tempair_in_fahrenheit,  2)) - 5.481717f * (Math.pow(10,  2)) *
						(Math.pow(relatedHumidity, 2)) + 1.22874f * (Math.pow(10,-3)) * (Math.pow(tempair_in_fahrenheit, 2)) * relatedHumidity + 8.5282f * (Math.pow(10,  -4)) * tempair_in_fahrenheit * (Math.pow(relatedHumidity, 2)) - 1.99f * (Math.pow(10, -6)) * (Math.pow(tempair_in_fahrenheit,  2)) * (Math.pow(relatedHumidity, 2)));
				
				if( (relatedHumidity <= 13.0f) && (tempair_in_fahrenheit >= 80.0f) && (tempair_in_fahrenheit <= 112.0f)) {
					
					float adj1 = (13.0f - relatedHumidity) / 4.0f;
					float adj2 = (float)(Math.sqrt((17.0f - Math.abs(tempair_in_fahrenheit - 95.0f)) / 17.0f));
					float adj = adj1 * adj2;
					
					hi = hi - adj;
					
				}else if ((relatedHumidity > 85.0f) && (tempair_in_fahrenheit >= 80.0f) && (tempair_in_fahrenheit <= 87.0f)) {
					
					float adj1 = (relatedHumidity - 85.0f) / 10.0f;
					float adj2 = (87.0f - tempair_in_fahrenheit) / 5.0f;
					float adj = adj1 * adj2;
					
					hi = hi + adj;
					
				}
				
			}else {
				hi = hifinal;
			}
			
		}
		
		float heatIndex = (float)(Math.round(hi - 32) * .556f);	

		return heatIndex;
	}
	

}
