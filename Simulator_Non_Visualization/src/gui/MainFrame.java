package gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import gui.component.BaseCanvas;

public class MainFrame {

	private JFrame frame;
	public static boolean nowGUIRunning = false;
	private static String versionInfo = "v0.1";
	
	public static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static int height = (int)((float)width * 0.111765f)+35;
	
	private static MainFrame INSTANCE = new MainFrame();
	public static MainFrame getInstance() {
		return INSTANCE;
	}
	
	BaseCanvas c;
	public void startFrame() {
		nowGUIRunning = true;
		
		frame = new JFrame("Curling Simulator "+versionInfo);
		
		c = new BaseCanvas();
		frame.getContentPane().add(c);
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds( (d.width - width)/2, (d.height - height)/2, width, height);
		
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		c.start();
		
	}
	
	public void stopFrame() {
		if(!nowGUIRunning)
			return;
		
		frame.setVisible(false);
		frame.dispose();
		nowGUIRunning = false;
		c.stop();
	}
	
	public static void main(String[] args) {
		new MainFrame().startFrame();
	}
	
}
