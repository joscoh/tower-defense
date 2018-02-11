import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Target {

	private int x;
	private int y;
	private static final String filename = "files/Target.png";
	private BufferedImage img;
	private MortarTower m;

	/*
	 * Constructor - takes in location and MortarTower
	 */
	public Target(int x, int y, MortarTower m) {
		this.x = x;
		this.y = y;
		this.m = m;
		try {
			if (img == null) {
				img = ImageIO.read(new File(filename));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}

	/*
	 * Draws the target at its location
	 */
	public void draw(Graphics g) {
		g.drawImage(img, x - 130, y - 130, null);
	}

	/*
	 * Returns the MortarTower associated with the target
	 */
	public MortarTower getMortar() {
		return m;
	}
}
