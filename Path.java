import java.util.ArrayList;

/*
 * a Path consists of an ArrayList of PathElements and thus allows 
 * the elements to be accessed in constant time (and their order is important)
 * This class ensures the ArrayList is properly encapsulated
 */
public class Path {
	private ArrayList<PathElement> segments;

	/*
	 * Constructor - creates a new Path
	 */
	public Path() {
		segments = new ArrayList<PathElement>();
	}

	/*
	 * Adds a Path Element to the Path
	 */
	public void add(PathElement p) {
		segments.add(p);
	}

	/*
	 * Returns the PathElement for a given index
	 */
	public PathElement get(int idx) {
		return segments.get(idx);
	}

	/*
	 * Returns the size of the path
	 */
	public int size() {
		return segments.size();
	}

	/*
	 * Returns the direction of the first PathElement
	 */
	public Direction getFirstDirection() {
		return segments.get(0).getDirection();
	}

	/*
	 * Returns the location of the first pathElement
	 */
	public Point getPathStart() {
		return segments.get(0).getLocation();
	}

	/*
	 * Returns the index of a given point, or -1 if the point is not in the list
	 */

	public int getNumberOfPoint(Point p) {
		for (PathElement pe : segments) {
			if (pe.getLocation().equals(p)) {
				return pe.getNumAlongPath();
			}
		}
		return -1;
	}

}
