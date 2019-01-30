package gui.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class CurlingSheet {

	public static void render(BufferedImage image) {
	
		Graphics2D gg2 = image.createGraphics();
		
		Rectangle2D.Float sqare1 = new Rectangle2D.Float(10f, 15f, 4250f, 475f);
		gg2.setColor(Color.white);
		gg2.fill(sqare1);
		
		
		Ellipse2D.Float houseCircle1 = new Ellipse2D.Float( (15f + 488f) - 183f, (10f + 237.5f) - 183f, 366f, 366f);
		gg2.setColor(new Color(0f,0f,0.8f,0.3f));
		gg2.fill(houseCircle1);
		
		Ellipse2D.Float houseCircle2 = new Ellipse2D.Float( (15f + 488f) - 122f, (10f + 237.5f) - 122f, 244f, 244f);
		gg2.setColor(new Color(1f,1f,1f,1f));
		gg2.fill(houseCircle2);
		
		Ellipse2D.Float houseCircle3 = new Ellipse2D.Float( (15f + 488f) - 61f, (10f + 237.5f) - 61f, 122f, 122f);
		gg2.setColor(new Color(0.8f,0f,0f,0.3f));
		gg2.fill(houseCircle3);
		
		Ellipse2D.Float houseCircle4 = new Ellipse2D.Float( (15f + 488f) - 20f, (10f + 237.5f) - 20f, 40f, 40f);
		gg2.setColor(new Color(1f,1f,1f,1f));
		gg2.fill(houseCircle4);
		
		Ellipse2D.Float houseCircle11 = new Ellipse2D.Float( 4250f - (15f + 488f) - 183f, (10f + 237.5f) - 183f, 366f, 366f);
		gg2.setColor(new Color(0f,0f,0.8f,0.3f));
		gg2.fill(houseCircle11);
		
		Ellipse2D.Float houseCircle22 = new Ellipse2D.Float( 4250f - (15f + 488f) - 122f, (10f + 237.5f) - 122f, 244f, 244f);
		gg2.setColor(new Color(1f,1f,1f,1f));
		gg2.fill(houseCircle22);
		
		Ellipse2D.Float houseCircle33 = new Ellipse2D.Float( 4250f - (15f + 488f) - 61f, (10f + 237.5f) - 61f, 122f, 122f);
		gg2.setColor(new Color(0.8f,0f,0f,0.3f));
		gg2.fill(houseCircle33);
		
		Ellipse2D.Float houseCircle44 = new Ellipse2D.Float( 4250f - (15f + 488f) - 20f, (10f + 237.5f) - 20f, 40f, 40f);
		gg2.setColor(new Color(1f,1f,1f,1f));
		gg2.fill(houseCircle44);
		
		
		
		Rectangle2D.Float hack1 = new Rectangle2D.Float( (15f + 122f), (10f + 222.5f), 2f, 10f);
		gg2.setColor(Color.BLACK);
		gg2.fill(hack1);
		
		Rectangle2D.Float hack2 = new Rectangle2D.Float( (15f + 122f), (10f + 242.5f), 2f, 9.5f);
		gg2.fill(hack2);
		
		Rectangle2D.Float hack11 = new Rectangle2D.Float( 4250f - (15f + 122f), (10f + 222.5f), 2f, 10f);
		gg2.setColor(Color.BLACK);
		gg2.fill(hack11);
		
		Rectangle2D.Float hack22 = new Rectangle2D.Float( 4250f - (15f + 122f), (10f + 242.5f), 2f, 9.5f);
		gg2.fill(hack22);
		
		
		Rectangle2D.Float backline = new Rectangle2D.Float( (15f + 305f), (10f + 0f), 2f, 515f);
		gg2.fill(backline);
		
		Rectangle2D.Float tee = new Rectangle2D.Float( (15f + 305f + 183f), (10f + 0f), 2f, 515f);
		gg2.fill(tee);
		
		Rectangle2D.Float backline1 = new Rectangle2D.Float( 4250f - (15f + 305f), (10f + 0f), 2f, 515f);
		gg2.fill(backline1);
		
		Rectangle2D.Float tee1 = new Rectangle2D.Float( 4250f - (15f + 305f + 183f), (10f + 0f), 2f, 515f);
		gg2.fill(tee1);
		
		Rectangle2D.Float centtre = new Rectangle2D.Float( 15f + 2125f, (10f + 0f), 2f, 515f);
		gg2.fill(centtre);
		
		Rectangle2D.Float hog1 = new Rectangle2D.Float( 15f + 1128f, (10f + 0f), 2f, 515f);
		gg2.fill(hog1);
		
		Rectangle2D.Float hog2 = new Rectangle2D.Float( 4250f - (15f + 1128f), (10f + 0f), 2f, 515f);
		gg2.fill(hog2);
		
		
		Rectangle2D.Float center = new Rectangle2D.Float( (15f + 128f), (10f + 237.5f), 3965f, 2f);
		gg2.fill(center);
		
	}
	
}
