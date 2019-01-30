package simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Stone {

	Vector2 position = new Vector2(0, 0);
	Body body;
	
	int team = 0;
	int index = 0;
	
	RunningBand rBands[] = new RunningBand[7];
	RunningBand rBands_[][] = new RunningBand[Statics.rBandsbundle][Statics.rBandsCount];
	
	public Stone(World world, float x, float y, int team) {
		setBody(createBody(world, this, x, y, Statics.Stone_size, 0.0f));
		
		this.team = team;
		setPosition(x, y);
	}
	
	public Stone(World world, Stone stone, int team) {
		this(world, stone.getPosition().x, stone.getPosition().y, team);
	}
	
	public RunningBand[] getRunningBands() {
		return rBands;
	}
	
	public RunningBand[][] getRunningBands_(){
		return rBands_;
	}
	
	public Body getBody() {
		return body;
	}
	
	public Vector2 getPosition() {
		if(body == null)
			return position;
		
		return body.getPosition();
	}
	
	public float getY() {
		if(body == null)
			return position.y;
		
		return body.getPosition().y;
	}
	
	public float getX() {
		if(body == null)
			return position.x;
		
		return body.getPosition().x;
	}
	
	public void setLastPosition(float x, float y) {
		this.position.set(x,y);
	}
	
	public void setLastPostiion(Vector2 pos) {
		this.position.set(pos);
	}
	
	public void setPosition(float x, float y) {
		if(body == null) {
			position.set(x, y);
			return;
		}
		
		body.getPosition().set(x, y);
	}
	
	public void setPosition(Vector2 pos) {
		setPosition(pos.x, pos.y);
	}
	
	public void setBody(Body body) {
		this.body = body;
	}
	
	public int getTeam() {
		return team;
	}
	
	public Body createBody(World world, Stone stone, float x, float y, float radius, float angle) {
		
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set(x, y);
		def.angle = angle;
		
		Body body = world.createBody(def);
		
		FixtureDef f_def = new FixtureDef();
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
		
		f_def.shape = circle;
		f_def.density = Statics.Stone_density;
		f_def.friction = Statics.Stone_friction;
		f_def.restitution = Statics.Stone_restitution;
		f_def.filter.categoryBits = Statics.Stone_category;
		f_def.filter.maskBits = Statics.Stone_maskbit;
		f_def.filter.groupIndex = 0;
		
		body.createFixture(f_def);
		circle.dispose();
		
		for(int i = 0; i < 7; i++) {
			float xx = 0.06f * (float)(Math.cos(Math.toRadians(51.428571f * i)));
			float yy = 0.06f * (float)(Math.sin(Math.toRadians(51.428571f * i)));
			stone.rBands[i] = new RunningBand(new Vector2(xx, yy), null, i);
		}
		
		return body;
	}
	
	public void render(BufferedImage image) {
		
		Graphics2D gg2 = image.createGraphics();
		
		gg2.setColor(Color.GRAY);
		Ellipse2D.Float background = new Ellipse2D.Float(4250f - (15f + ((getY()) * 100f)) - 14.5f, (10f + (getX() * 100f))- 14.5f, 29f, 29f);
		gg2.fill(background);
		
		gg2.setColor(team == 0 ? Color.RED : Color.yellow);
		Ellipse2D.Float stoneTop = new Ellipse2D.Float(4250f - (15f + ((getY()) * 100f)) - 13f,(10f + (getX() * 100f))- 13f, 26f, 26f);
		gg2.fill(stoneTop);
		
	}
}
