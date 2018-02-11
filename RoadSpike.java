import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RoadSpike {

	private int numBalloonsLeft = 10;
	private int x;
	private int y;
	private static final String filename = "files/RoadSpike.png";
	private BufferedImage img;

	/*
	 * Constructor - takes in location
	 */
	public RoadSpike(int x, int y) {
		this.x = x;
		this.y = y;
		try {
			if (img == null) {
				img = ImageIO.read(new File(filename));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}

	/*
	 * Returns the x coordinate
	 */
	public int getX() {
		return x;
	}

	/*
	 * Returns the y coordinator
	 */
	public int getY() {
		return y;
	}

	/*
	 * Return the number of Balloons the spikes can still pop
	 */
	public int getNumBalloonsLeft() {
		return numBalloonsLeft;
	}

	/*
	 * Decreases the number of Balloons left by 1
	 */
	public void popBalloon() {
		numBalloonsLeft--;
	}

	/*
	 * Returns true if the spikes have no more balloons to pop
	 */
	public boolean isDead() {
		return numBalloonsLeft == 0;
	}

	/*
	 * Draws the spikes, given a Graphics
	 */
	public void draw(Graphics g) {
		g.drawImage(img, x, y, null);
	}
}
