
public class Point /* implements Comparable<Point> */ {
	private int x;
	private int y;

	/*
	 * Constructor - takes in x and y
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/*
	 * Return the x coordinate
	 */
	public int getX() {
		return x;
	}

	/*
	 * Return the y coordinate
	 */
	public int getY() {
		return y;
	}

	/*
	 * Static function that calculates the distance between two points
	 */
	public static double distance(Point a, Point b) {
		double dx = a.getX() - b.getX();
		double dy = a.getY() - b.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	/*
	 * Overrides HashCode - two points are equal iff their x and y coordinates
	 * are equal
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
	 * Overrides equals - two points are equal iff their x and y coordinates are
	 * equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Point)) {
			return false;
		}
		Point other = (Point) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}
}
