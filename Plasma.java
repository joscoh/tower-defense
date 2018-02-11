import java.awt.Color;
import java.awt.Graphics;

public class Plasma extends Attacker {

	/*
	 * Constructor - takes in target point, target Balloon, a location, and the
	 * speed
	 */
	public Plasma(Point p, Balloon b, double x, double y, double speed) {
		super(p, true, true, x, y, b, speed, true, Integer.MAX_VALUE, 0);
	}

	/*
	 * Overrides draw from Attacker with a given Graphics
	 */
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.MAGENTA);
		int xCenter = (int) this.getX();
		int yCenter = (int) this.getY();
		g.fillOval(xCenter, yCenter, LevelMap.CELL_SIZE / 2, LevelMap.CELL_SIZE / 2);

	}

}
