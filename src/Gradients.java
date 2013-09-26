import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Gradients extends JPanel {

	private Color color1 = null;
	private Color color2 = null;
	private int width;
	private int height;

	private static final long serialVersionUID = 46778110791286960L;

	public Gradients(Color col1, Color col2, int w, int h) {
		width = w;
		height = h;
		setBounds(0, 0, width, height);
		color1 = col1;
		color2 = col2;
	}

	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		GradientPaint gp1 = new GradientPaint(0, 0, color1, 0, height, color2,
				true);

		g2d.setPaint(gp1);
		g2d.fillRect(0, 0, width, height);

	}
}
