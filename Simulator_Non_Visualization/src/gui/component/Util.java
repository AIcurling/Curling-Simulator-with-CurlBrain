package gui.component;

import java.awt.Color;
import java.awt.image.BufferedImage;

import simulator.FunctionUtils;
import simulator.Pebble;
import simulator.Statics;

public class Util {

	private static Color color = new Color(0.8f, 0.8f, 0.0f, 0.5f);
	private static Color white = new Color(1f, 1f, 1f, 0f);
	public static int area_color = (color.getAlpha() << 24) | (color.getRed() << 16) | (color.getGreen() << 8) | (color.getBlue());
	public static int area_color_white = (white.getAlpha() << 24) | (white.getRed() << 16) | (white.getGreen() << 8) | (white.getBlue());
	
	public static BufferedImage createIceImage() {

		BufferedImage pebble_image = new BufferedImage(Statics.pebble_countY, Statics.pebble_countX, BufferedImage.TYPE_INT_ARGB);
		
		for (int y = 0; y < Statics.pebble_countY; y++) {
			for (int x = 0; x < Statics.pebble_countX; x++) {
				Pebble p = FunctionUtils.pebbles[(y * Statics.pebble_countX) + x];
				int argb = area_color_white;
				
				if(p.isSweep())
					argb = area_color;
				
				pebble_image.setRGB(y, x, argb);
			}
		}
		
		
		return pebble_image;
	}
	
	
	
}
