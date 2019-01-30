package simulator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

public class RunningBand {
	private Vector2 position;
	private Fixture fixture;
	
	private Pebble pebble;
	private int index;
	
	private Vector2 friction;
	
	
	public RunningBand(Vector2 position, Fixture fixture, int index) {
		this.position = position;
		this.fixture = fixture;
		this.index = index;
	}
	
	public void setFriction(Vector2 friction) {
		this.friction = friction;
	}
	
	public Vector2 getFriction() {
		return friction;
	}
	
	public int getIndex() {
		return index;
	}
	
	public Vector2 getInitialPosition() {
		return position;
	}
	
	public float getInitialX() {
		return position.x;
	}
	
	public float getInitialY() {
		return position.y;
	}
	
	public Fixture getFixture() {
		return fixture;
	}
	
	public void setPebble(Pebble pebble) {
		this.pebble = pebble;
	}
	
	public Pebble getPebble() {
		return pebble;
	}
	
	public Vector2 getCurrentPosition(float rad) {
		
		return new Vector2(position).rotateRad(rad);
		
	}
	
	public float getAngle(float rad) {
		
		Vector2 cur_pos = getCurrentPosition(rad);

		return cur_pos.angle();
		
	}
}
