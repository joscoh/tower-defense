import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;

public class Laser extends Attacker {
	private static final int LENGTH = 10;

	/*
	 * Constructor for Laser (essentially a DirectedDart that can pop lead
	 * balloons), takes in target point, target balloon, position, speed
	 */
	public Laser(Point p, Balloon b, double x, double y, double speed) {
		super(p, true, true, x, y, b, speed, true, Integer.MAX_VALUE, 0);
	}

	/*
	 * Override draw from Attacker, it draws a red line that is angled towards
	 * the target
	 */
	@Override
	public void draw(Graphics g) {
		// first consider (x,y) as (0,0), then rotate around origin, then
		// translate
		g.setColor(Color.RED);
		Point[] pts = { new Point(-LENGTH / 2, 0), new Point(-3 / 2 * LENGTH, 0) };
		// rotate the points
		Point[] ptsRotate = new Point[pts.length];
		for (int i = 0; i < ptsRotate.length; i++) {
			ptsRotate[i] = Attacker.rotate(pts[i], this.getAngle());
		}
		// translate the points
		Point[] ptsTrans = new Point[ptsRotate.length];
		for (int i = 0; i < pts.length; i++) {
			int x = (int) this.getX() + ptsRotate[i].getX();
			int y = (int) this.getY() + ptsRotate[i].getY();
			ptsTrans[i] = new Point(x, y);
		}
		Shape shaft = new Line2D.Double(ptsTrans[0].getX(), ptsTrans[0].getY(), ptsTrans[1].getX(), ptsTrans[1].getY());
		Graphics2D g0 = (Graphics2D) g;
		g0.draw(shaft);
	}

}
