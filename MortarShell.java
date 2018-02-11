import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MortarShell extends Attacker {

	private static String filename = "files/Explosion.png";
	private static BufferedImage img;
	private boolean twoLayer;

	/*
	 * Constructor, takes in position, how long it will be alive for, a range,
	 * and whether or not it destroys 2 layers of balloons
	 */
	public MortarShell(int x, int y, int timeAlive, int range, boolean twoLayer) {
		super(new Point(x, y), false, true, x, y, null, 0, false, timeAlive, range);
		this.twoLayer = twoLayer;
		try {
			if (img == null) {
				img = ImageIO.read(new File(filename));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}

	/*
	 * Overrides draw from Attacker, draws explosion
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(img, (int) this.getX() - 130, (int) this.getY() - 130, null);

	}

	/*
	 * Returns whether or not this mortar shell destroys 2 layers of balloons
	 * (if mortar tower upgraded to level 3)
	 */
	public boolean isTwoLayer() {
		return twoLayer;
	}

}
