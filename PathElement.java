/* A PathElement is essentially an int * Point * Direction  tuple
 * NOTE: Points are relative to array (so 20 x 20)
*/
public class PathElement {
	private int numAlongPath;
	private Point location;
	private Direction direction;

	/*
	 * Constructor for PathElement - takes in a location, the number along the
	 * path this point is, and the direction
	 */
	public PathElement(Point location, int numAlongPath, Direction d) {
		this.location = location;
		this.numAlongPath = numAlongPath;
		this.direction = d;
	}

	/*
	 * Returns the location
	 */
	public Point getLocation() {
		return location;
	}

	/*
	 * Returns the direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/*
	 * Returns the number along the path
	 */
	public int getNumAlongPath() {
		return numAlongPath;
	}

}
