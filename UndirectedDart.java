import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Line2D;

public class UndirectedDart extends Attacker {
	private static final int LENGTH = 10;

	/*
	 * Constructor takes in a position, speed, a target point, and its lifespan
	 */
	public UndirectedDart(int x, int y, int speed, Point tgt, int timeAlive) {
		super(tgt, true, false, x, y, null, speed, false, timeAlive, 0);
	}

	/*
	 * Draws the given dart in the same way as a DirectedDart (as an arrow
	 * pointed toward the target)
	 */
	@Override
	public void draw(Graphics g) {
		// first consider (x,y) as (0,0), then rotate around origin, then
		// translate
		g.setColor(Color.BLACK);
		// first 3 points are head, last 2 are shaft
		Point[] pts = { new Point(-LENGTH / 2, LENGTH / 2), new Point(-LENGTH / 2, -LENGTH / 2), new Point(0, 0),
				new Point(-LENGTH / 2, 0), new Point(-3 / 2 * LENGTH, 0) };

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
		int[] xS = { ptsTrans[0].getX(), ptsTrans[1].getX(), ptsTrans[2].getX() };
		int[] yS = { ptsTrans[0].getY(), ptsTrans[1].getY(), ptsTrans[2].getY() };
		Shape head = new Polygon(xS, yS, 3);
		Shape shaft = new Line2D.Double(ptsTrans[3].getX(), ptsTrans[3].getY(), ptsTrans[4].getX(), ptsTrans[4].getY());
		Graphics2D g0 = (Graphics2D) g;
		g0.fill(head);
		g0.draw(head);
		g0.draw(shaft);

	}

}
