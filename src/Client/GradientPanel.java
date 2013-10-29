package Client;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GradientPanel extends JPanel {

	private Color color1 = null;
	private Color color2 = null;

	private static final long serialVersionUID = 46778110791286960L;

	public GradientPanel(Color col1, Color col2) {
		this.color1 = col1;
		this.color2 = col2;
	}

	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		GradientPaint gp1 = new GradientPaint(0, 0, color1, 0,
				this.getHeight(), color2, true);

		g2d.setPaint(gp1);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

	}
}
