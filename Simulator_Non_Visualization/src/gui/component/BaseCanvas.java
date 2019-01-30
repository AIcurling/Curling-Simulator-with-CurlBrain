package gui.component;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import gui.MainFrame;
import simulator.Functions;
import simulator.Statics;
import simulator.Stone;

public class BaseCanvas extends Canvas{

	private static final long serialVersionUID = 1L;
	private static BufferedImage baseImage;
	private BufferStrategy bs;
	private Graphics2D g2d;
	
	private boolean runn = true;
	public BaseCanvas() {
		
		baseImage = new BufferedImage(4290, 515, BufferedImage.TYPE_INT_ARGB);
		
	}
	
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(runn) {
					render();
					update();
				}
			}
		}).start();
	}
	
	public void stop() {
		runn = false;
	}
		
	public void render() {
		
		if(this.getBufferStrategy() == null) {
			this.createBufferStrategy(3);
			bs = this.getBufferStrategy();
			return;
		}
		
		
		Graphics2D gg2 = baseImage.createGraphics();
		gg2.clearRect(0, 0, 4290, 515);
		
		CurlingSheet.render(baseImage);
		gg2.drawImage(Util.createIceImage(), 1143, 15, 
				4290+564, 490,
				Statics.pebble_countY, 0, 0, Statics.pebble_countX, this);

		for(int i = 0; i < Functions.stones_origin.size(); i++) {
			Stone s = Functions.stones_origin.get(i);
			s.render(baseImage);
		}
		
		for(int i = 0; i < Functions.stones_simulate.size(); i++) {
			Stone s = Functions.stones_simulate.get(i);
			s.render(baseImage);
		}
		
		g2d = (Graphics2D)bs.getDrawGraphics();
		g2d.setBackground(Color.BLACK);
		g2d.clearRect(0, 0, MainFrame.width, MainFrame.height);
		g2d.drawImage(baseImage, 1, 1, (MainFrame.width-10), (int)((float)(MainFrame.width-10)*0.111765f),this);
		
		bs.show();
	}
	
	public void update() {
		
	}
	
}
