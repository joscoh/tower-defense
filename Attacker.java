import java.awt.Graphics;

/*
 * This is an abstract class for Attacker types, which include Darts, Bombs, Lasers, etc.
 * Most of the implemented methods are getters/setters, and only Draw is abstract
 */
public abstract class Attacker {
	private boolean isSharp;
	private boolean isExplosive;
	private double x;
	private double y;
	private Balloon target;
	private double vx;
	private double vy;
	private double angle;
	private Point tgtLocation;
	private boolean directed;
	private int timeAlive;
	private int timeAliveSoFar = 0;
	private int range;

	/*
	 * Constructor for Attacker Note that if the attacker is undirected, its
	 * target is null, and if it can only hit 1 balloon (eg: a dart), then its
	 * range is 0
	 */
	public Attacker(Point tgtLocation, boolean isSharp, boolean isExplosive, double x, double y, Balloon target,
			double speed, boolean directed, int timeAlive, int range) {
		this.tgtLocation = tgtLocation;
		this.isSharp = isSharp;
		this.isExplosive = isExplosive;
		this.x = x;
		this.y = y;
		this.directed = directed;
		this.target = target;
		this.timeAlive = timeAlive;
		this.range = range;
		updateAngle();
		vx = speed * Math.cos(angle);
		vy = -speed * Math.sin(angle);
	}

	/*
	 * Private helper method to update the angle of the attacker based on the
	 * Attacker's location and the target location
	 */
	private void updateAngle() {
		Point cart = toCartesian(new Point((int) x, (int) y));
		Point cartTgt = toCartesian(tgtLocation);
		angle = findAngle(cart, cartTgt);
	}

	/*
	 * This is a static method to find the angle between two points, given in
	 * Cartesian coordinates (ie with the origin at the bottom left) Inputs: Two
	 * points in cartesian coordinates Ouputs: A double representing the angle,
	 * in radians, between the two points
	 */
	public static double findAngle(Point a, Point b) {
		return Math.atan2(b.getY() - a.getY(), b.getX() - a.getX());

	}

	/*
	 * This is a static helper method to convert a point into Cartesian
	 * coordinates (ie with the origin at the bottom left) Inputs: A Point in
	 * local coordinates (origin at upper left) Output: A Point in Cartesian
	 * Coordinates
	 */
	public static Point toCartesian(Point p) {
		int y = (int) p.getY();
		int newY = LevelMap.MAP_SIZE * LevelMap.CELL_SIZE - y;
		return new Point(p.getX(), newY);
	}

	/*
	 * Static helper method to rotate a given point around the origin by a given
	 * angle Inputs: A Point to rotate, an angle in radians Output: A point
	 * rotated around the origin by the given angle
	 */
	public static Point rotate(Point p, double angle) {
		double x = p.getX();
		double y = p.getY();
		double x0 = x * Math.cos(angle) - y * Math.sin(angle);
		double y0 = -(x * Math.sin(angle) + y * Math.cos(angle));
		return new Point((int) x0, (int) y0);
	}

	/*
	 * Returns the range of the given attacker
	 */
	public int getRange() {
		return range;
	}

	/*
	 * Returns the angle of the given attacker
	 */
	public double getAngle() {
		return angle;
	}

	/*
	 * Abstract method to draw the Attacker
	 */
	public abstract void draw(Graphics g);

	/*
	 * Moves the Attacker by updating the x and y coordinates
	 */
	public void move() {
		x += vx;
		y += vy;
	}

	/*
	 * Returns whether the given attacker is sharp
	 */
	public boolean isSharp() {
		return isSharp;
	}

	/*
	 * Returns whether the given attacker is explosive
	 */
	public boolean isExplosive() {
		return isExplosive;
	}

	/*
	 * Returns the x coordinate of the given Attacker
	 */
	public double getX() {
		return x;
	}

	/*
	 * Returns the y coordinate of the given Attacker
	 */
	public double getY() {
		return y;
	}

	/*
	 * Returns the target of the given Attacker
	 */
	public Balloon getTarget() {
		return target;
	}

	/*
	 * Returns true if the attacker has collided with its target Balloon, false
	 * otherwise
	 */
	public boolean hasCollided() {
		return hasUndirectedCollided(this, target);
	}

	/*
	 * Static method that checks if a given attacker has collided with a balloon
	 * (for Attackers with 0 range) Inputs: An Attacker and a Balloon Output: A
	 * boolean that is true if the attacker has collided with the balloon and
	 * false otherwise
	 */
	public static Boolean hasUndirectedCollided(Attacker a, Balloon b) {
		double maxX = Math.max(b.getX(), b.getX() + Balloon.WIDTH);
		double minX = Math.min(b.getX(), b.getX() + Balloon.WIDTH);
		double maxY = Math.max(b.getY(), b.getY() + Balloon.HEIGHT);
		double minY = Math.min(b.getY(), b.getY() + Balloon.HEIGHT);
		double x = a.getX();
		double y = a.getY();
		return (x >= minX && x <= maxX && y <= maxY && y >= minY);
	}

	/*
	 * Returns true if the given attacker is directed, false otherwise
	 */
	public boolean isDirected() {
		return directed;
	}

	/*
	 * Returns an int that represents how long the Balloon has been alive (it is
	 * incremented in every iteration of the timer)
	 */
	public int getTimeAliveSoFar() {
		return timeAliveSoFar;
	}

	/*
	 * Increases the time alive by 1
	 */
	public void incrTimeAlive() {
		timeAliveSoFar++;
	}

	/*
	 * Returns true if the time the balloons has been alive equals the amount of
	 * time for which it is supposed to be alive (and thus its lifespan has
	 * expired)
	 */
	public boolean timeToDie() {
		return timeAlive == timeAliveSoFar;
	}

	/*
	 * Static method that determines if a given balloon is the range of an
	 * attacker (with range >0) Inputs: An attacker, a balloon Outputs: A
	 * boolean that is true if the given balloon is currently in the attacker's
	 * range, false otherwise
	 */
	public static boolean inRange(Attacker a, Balloon b) {
		double maxX = Math.max(b.getX(), b.getX() + Balloon.WIDTH);
		double minX = Math.min(b.getX(), b.getX() + Balloon.WIDTH);
		double maxY = Math.max(b.getY(), b.getY() + Balloon.HEIGHT);
		double minY = Math.min(b.getY(), b.getY() + Balloon.HEIGHT);
		int r = a.getRange() * LevelMap.CELL_SIZE;
		int rPlus = (a.getRange() + 1) * LevelMap.CELL_SIZE;
		boolean inRange = ((minY >= a.getY() + rPlus && minY <= a.getY() - r)
				|| (maxY >= a.getY() - r && maxY <= a.getY() + rPlus))
				&& ((minX >= a.getX() - r && minX <= a.getX() + rPlus)
						|| (maxX >= a.getX() - r && maxX <= a.getX() + rPlus));
		return inRange;
	}

}
