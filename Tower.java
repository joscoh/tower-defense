import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import java.util.Map;
import java.util.Set;

public abstract class Tower {
	private int x;
	private int y;
	private int fireRate;
	private int range;
	private int ctr = 0;
	private Map<Point, Double> fireSpeed;
	private LevelMap map;
	private boolean preview;
	private int level = 1;
	public String imgFile;
	private BufferedImage img;
	private boolean directed;
	private boolean detectCamo = false;

	/*
	 * Constructor - takes in position, how fast the tower fires, its range, a
	 * LevelMap, whether or not it is a preview, whether or not it is directed
	 * (targets specific darts), and a filename for the picture of the tower
	 */
	public Tower(int x, int y, int fireRate, int range, LevelMap map, boolean preview, boolean directed,
			String filename) {
		this.x = x;
		this.y = y;
		this.fireRate = fireRate;
		this.range = range;
		this.map = map;
		fireSpeed = new HashMap<Point, Double>();
		ctr = (int) fireRate / GameCourt.INTERVAL;
		this.preview = preview;
		this.imgFile = filename;
		this.directed = directed;
		if (!preview && directed) {
			updateFireMap();
		}
		try {
			if (img == null) {
				img = ImageIO.read(new File(imgFile));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		this.level = 0;
	}

	/*
	 * Static function that determines what Point at which to attack based on a
	 * given Point and LevelMap. It will attack at the PathElement that is 30
	 * ahead of the given Point's pathElement (corresponding to 1 cell)
	 */
	public static Point whereToAttack(Point p, LevelMap m) {
		Point p0 = new Point(p.getX() / LevelMap.CELL_SIZE, p.getY() / LevelMap.CELL_SIZE);
		Point p1 = new Point(p0.getX() * LevelMap.CELL_SIZE, p0.getY() * LevelMap.CELL_SIZE);
		int pathNumber = m.getNumberOfPoint(p1);
		Point nextPoint;
		if (pathNumber + 30 < m.getPathSize()) {
			nextPoint = m.getPathElement(pathNumber + 30).getLocation();
		} else {
			nextPoint = m.getPathElement(pathNumber).getLocation();
		}
		return new Point(nextPoint.getX() + LevelMap.CELL_SIZE / 2, nextPoint.getY() + LevelMap.CELL_SIZE / 2);
	}

	/*
	 * Updates a Map that maps each Point to a speed. This is done so that the
	 * tower can look up in constant time what the speed should be when it sees
	 * a balloon at a specific point. This way, we ensure with reasonable
	 * accuracy that the attacker launched will reach the given balloon. Note:
	 * this only applies to directed and non-preview towers, and this function
	 * is only called when a non-preview tower is created or if it is upgraded
	 * in a way that changes the range
	 */

	private void updateFireMap() {
		Point towerCenter = new Point(this.getX() + LevelMap.CELL_SIZE / 2, this.getY() + LevelMap.CELL_SIZE / 2);
		// look through points in range, see if each are in path
		int xMin = Math.max(0, getX() - getRange() * LevelMap.CELL_SIZE);
		int yMin = Math.max(0, getY() - getRange() * LevelMap.CELL_SIZE);
		int xMax = Math.min(getX() + (getRange() + 1) * LevelMap.CELL_SIZE, LevelMap.MAP_SIZE * LevelMap.CELL_SIZE - 1);
		int yMax = Math.min(getY() + (getRange() + 1) * LevelMap.CELL_SIZE, LevelMap.MAP_SIZE * LevelMap.CELL_SIZE - 1);
		for (int i = xMin; i <= xMax; i++) {
			for (int j = yMin; j <= yMax; j++) {
				int xCell = i / LevelMap.CELL_SIZE;
				int yCell = j / LevelMap.CELL_SIZE;
				if (xCell == this.getX() / LevelMap.CELL_SIZE && yCell == this.getY() / LevelMap.CELL_SIZE) {
					continue;
				}
				if (map.getCell(xCell, yCell).isPath()) {
					int x = xCell * LevelMap.CELL_SIZE;
					int y = yCell * LevelMap.CELL_SIZE;
					int pathNumber = map.getNumberOfPoint(new Point(x, y));
					// objective : calculate speed so that dart arrives at next
					// square when balloon does
					Point nextPoint;
					if (pathNumber + 30 < map.getPathSize()) {
						nextPoint = map.getPathElement(pathNumber + 30).getLocation();
					} else {
						nextPoint = map.getPathElement(pathNumber).getLocation();
					}
					Point center = new Point(nextPoint.getX() + LevelMap.CELL_SIZE / 2,
							nextPoint.getY() + LevelMap.CELL_SIZE / 2);
					double distance = Point.distance(towerCenter, center);
					double speed = distance / 15;
					fireSpeed.put(new Point(i, j), (speed));
				}
			}
		}
	}

	/*
	 * Returns the speed for a given point based on the fireMap
	 */
	public double getSpeedForPoint(Point p) {
		return fireSpeed.get(p);
	}

	/*
	 * Returns the levelMap of the Tower
	 */
	public LevelMap getMap() {
		return map;
	}

	/*
	 * Returns the x coordinate of the Tower
	 */
	public int getX() {
		return x;
	}

	/*
	 * Returns the y coordinate of the Tower
	 */
	public int getY() {
		return y;
	}

	/*
	 * Sets the range of the tower to the given range Used in upgrades
	 */
	public void setRange(int range) {
		this.range = range;
	}
	/*
	 * Sets the fireRate of the tower to the given range Used in upgrades
	 */

	public void setFireRate(int fireRate) {
		this.fireRate = fireRate;
	}

	/*
	 * Gets the range
	 */
	public int getRange() {
		return range;
	}

	/*
	 * Gets the fireRate
	 */
	public int getFireRate() {
		return fireRate;
	}

	/*
	 * Returns the counter (used to determine if a tower has reloaded)
	 */
	public int getCtr() {
		return ctr;
	}

	/*
	 * Iterates the counter (used to determine if a tower has reloaded)
	 */

	public void itr() {
		ctr++;
	}

	/*
	 * Resets the counter (used to determine if a tower has reloaded)
	 */
	public void resetItr() {
		ctr = 0;
	}

	/*
	 * Returns whether the tower is a preview
	 */
	public boolean isPreview() {
		return preview;
	}

	/*
	 * Abstract function to attack a Balloon, returns a Set of Attackers
	 */

	public abstract Set<Attacker> attack(Balloon b);

	/*
	 * Draws the given tower, given a Graphics
	 */

	public void draw(Graphics g) {
		if (isPreview()) {
			this.drawPreview(g);
		}
		g.drawImage(img, this.getX(), this.getY(), null);
	}

	/*
	 * Private helper function to draw the preview, as this draws the range
	 */
	private void drawPreview(Graphics g) {
		int xMin = Math.max(0, getX() - this.getRange() * LevelMap.CELL_SIZE);
		int yMin = Math.max(0, getY() - LevelMap.CELL_SIZE * this.getRange());
		int xMax = Math.min(getX() + LevelMap.CELL_SIZE * (getRange() + 1), LevelMap.CELL_SIZE * (LevelMap.MAP_SIZE));
		int yMax = Math.min(getY() + LevelMap.CELL_SIZE * (getRange() + 1), LevelMap.CELL_SIZE * (LevelMap.MAP_SIZE));
		g.setColor(Color.BLACK);
		g.drawLine(xMin, yMax, xMin, yMin);
		g.drawLine(xMin, yMin, xMax, yMin);
		g.drawLine(xMax, yMax, xMax, yMin);
		g.drawLine(xMax, yMax, xMin, yMax);
	}

	/*
	 * Returns the upgrade level of the tower
	 */
	public int getUpgradeLevel() {
		return level;
	}

	/*
	 * increase the upgrade level of the tower
	 */
	public void increaseLevel() {
		level++;
		if (!preview && directed) {
			updateFireMap();
		}
	}

	/*
	 * Returns whether or not the given tower can detect Camo Balloons
	 */
	public boolean canDetectCamo() {
		return detectCamo;
	}

	/*
	 * Enables the tower to detect Camo Balloons
	 */
	public void enableDetectCamo() {
		detectCamo = true;
	}

	/*
	 * Abstract method to upgrade the tower, changing some aspects of its state
	 */
	public abstract void upgradeTower(int nextLevel);

}
