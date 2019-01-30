package simulator;

import com.badlogic.gdx.math.Vector2;

public class Pebble {
	private float radius = 0.01f;
	private float friction = 0.0f;
	private boolean isCreated = false;
	private boolean isContacted = false;
	private float wear = 1.0f;
	
	private Vector2 position;
	private int index;
	
	private Vector2 Scratch = new Vector2(0, 0);
	
	private int contact_count = 0;
	
	private float water_film_thickness = 0.0f;
	
	private boolean isSweep = false;
	
	
	public Pebble(float friction, int index, float x, float y) {
		this.friction = friction;
		this.index = index;
		
		position = new Vector2(x, y);
		
	}
	
	public boolean isSweep() {
		return isSweep;
	}
	
	public void isSweep(boolean isSweep) {
		this.isSweep = isSweep;
	}
	
	public float getWater() {
		return water_film_thickness;
	}
	
	public void updateWater(float water) {
		this.water_film_thickness += water;
	}
	
	public boolean isContacted() {
		return isContacted;
	}
	
	public void setContacted(boolean isContacted) {
		this.isContacted = isContacted;
	}
	
	
	public int getIndex() {
		return index;
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public int getContactCount() {
		return contact_count;
	}
	
	public void increateContactCount() {
		contact_count++;
	}
	
	public boolean isCreated() {
		return isCreated;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public float getFriction() {
		return friction;
	}
	
	public void setScratch(Vector2 vec) {
		this.Scratch.set(vec);
	}
	
	public void setScratch(float vx, float vy) {
		this.Scratch.set(vx, vy);
	}
	
	public Vector2 getScratch() {
		return new Vector2(Scratch);
	}
	 
//	public void updateWaterFilm(float temperature, float relatedHumidity) {
//		
//		float dewPoint = Weather.Calculation_dewPoint(temperature, relatedHumidity);
//		float heatIndex = Weather.Calculation_Heat(temperature, relatedHumidity);
//		float coolingIndex = Weather.Calculation_Heat(7.0f, relatedHumidity);
//		
//		heatIndex -= coolingIndex;
//		
//		if(heatIndex < dewPoint)
//			return;
//		
//		float water = Weather.Heat2Joules(heatIndex);
//		water = Weather.getKilogramOfMeltedWater(water);
//		water /= (42.5f * 4.75f);
//		
//		
//		this.water_film_thickness += water;
//	}
	
	public float getWear2() {
		return wear;
	}
	
	public float getWear() {
		
		
		float w = wear > 0.5f ? (float)((0.5f*(wear * wear))*(Math.cos(1.5f * wear * Math.PI)/Math.exp(wear)))+1.0f
				: (wear - 0.5f)*(wear - 0.5f)+1.0f;
		
		//w += 1.0f;
		//System.out.println(w);
		//System.out.println(w);
		
		return w;
	}
	
	public void updateWear() {
		
		wear = wear - (0.0094f * 3f);
		
		if(wear <= 0)
			wear = 0;
		
		
	}
	
	public void setWear(float wear) {
		this.wear = wear;
	}
	
	public void updateWear(float frost) {
		
		float wf = (water_film_thickness * frost);
		water_film_thickness -= wf;
		if(water_film_thickness < 0f)
			water_film_thickness = 1.0f;
		
		wear += wf;
		if(wear > 1.0f)
			wear = 1.0f;
		else if(wear < 0.0f)
			wear = 0.0f;
		
		//System.out.println("wear : "+ wear);
		
	}
}
