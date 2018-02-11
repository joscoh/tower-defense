import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bomb extends Attacker {

	private static String filename = "files/Explosion.png";
	private static BufferedImage img;

	/*
	 * Constructor for bomb, takes in a point to target, a balloon to target, a
	 * position, a speed, and a range
	 */
	public Bomb(Point p, Balloon b, double x, double y, double speed, int range) {
		super(p, false, true, x, y, b, speed, true, Integer.MAX_VALUE, range);
		try {
			if (img == null) {
				img = ImageIO.read(new File(filename));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}

	/*
	 * Overrides draw (from Attacker)
	 */
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		int xCenter = (int) this.getX();
		int yCenter = (int) this.getY();
		g.fillOval(xCenter, yCenter, LevelMap.CELL_SIZE, LevelMap.CELL_SIZE);
		if (this.hasCollided()) {
			g.drawImage(img, xCenter - 130, yCenter - 130, null);
		}

	}

	/*
	 * Overrides hasCollided in Attacker, since a bomb can collide differently
	 * than most projectiles Since it is a circle, it collides if any part of
	 * the circle hits the target balloon, not just the point
	 */
	@Override
	public boolean hasCollided() {
		// if distance smaller than sum of radii
		int bCenterX = getTarget().getX() + Balloon.WIDTH / 2;
		int bCenterY = getTarget().getY() + Balloon.HEIGHT / 2;
		int currCenterX = (int) this.getX() + LevelMap.CELL_SIZE / 2;
		int currCenterY = (int) this.getY() + LevelMap.CELL_SIZE / 2;
		// appoximation since a balloon is an oval
		return (Point.distance(new Point(bCenterX, bCenterY), new Point(currCenterX, currCenterY)) < LevelMap.CELL_SIZE
				+ Balloon.WIDTH);
	}
}
