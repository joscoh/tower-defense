import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//Note: though interacted with in similar ways to Towers, a Banana Farm is not
//a Tower since it does not attack any Balloons
public class BananaFarm {

	public static final int[] moneyForLevel = { 340, 1020, 2125 };
	public static final String[] levelNames = { "120 Money", "250 Money", "500 Monkey" };
	private int level = 0;
	private int bananasPerRound = 80;
	private int x;
	private int y;
	private static final String filename = "files/BananaFarm.png";
	private BufferedImage img;

	/*
	 * Constructor for Banana Farm, takes in location and upgrade level
	 */
	public BananaFarm(int x, int y, int level) {
		this.x = x;
		this.y = y;
		while (this.level < level) {
			upgradeTower(this.level + 1);
		}
		try {
			if (img == null) {
				img = ImageIO.read(new File(filename));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}

	/*
	 * Returns the x coordinate of the farm
	 */
	public int getX() {
		return x;
	}

	/*
	 * Returns the x coordinate of the farm
	 */
	public int getY() {
		return y;
	}

	/*
	 * Returns the number of bananas per round the farm grows
	 */
	public int getNumBananas() {
		return bananasPerRound;
	}

	/*
	 * Upgrade the Farm to the given level
	 */
	public void upgradeTower(int nextLevel) {
		if (nextLevel == 1) {
			bananasPerRound = 120;
			level++;
		} else if (nextLevel == 2) {
			bananasPerRound = 250;
			level++;
		} else if (nextLevel == 3) {
			bananasPerRound = 500;
			level++;
		}
	}

	/*
	 * Return the upgrade level of the farm
	 */
	public int getLevel() {
		return level;
	}

	/*
	 * Draws the farm with a given Graphics
	 */

	public void draw(Graphics g) {
		g.drawImage(img, x, y, null);
	}

	/*
	 * Overrides hashCode - note that two Banana Farms are equal iff their x and
	 * y are equal
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/*
	 * Overrides equals - two Banana Farms are equal iff they have the same
	 * position
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BananaFarm)) {
			return false;
		}
		BananaFarm other = (BananaFarm) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

}
