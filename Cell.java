/*
 * This is a cell of the game board, and tells whether each portion is on the path, can be
 * built upon, and whether it is water.
 */
public class Cell {

	private final boolean isPath;
	private final boolean isWater;
	private boolean isAvailable;

	/*
	 * Construct a cell, telling whether or not is is on the path, water, and
	 * available
	 */
	public Cell(boolean isPath, boolean isWater, boolean isAvailable) {
		this.isPath = isPath;
		this.isWater = isWater;
		this.setAvailable(isAvailable);
	}

	/*
	 * Tells whether or not the given cell is on the path
	 */
	public boolean isPath() {
		return isPath;
	}

	/*
	 * Tells whether or not the given cell is water
	 */
	public boolean isWater() {
		return isWater;
	}

	/*
	 * Tells whether or not the given cell is available (ie no tower in cell)
	 */
	public boolean isAvailable() {
		return isAvailable;
	}

	/*
	 * Sets a given cell's availability to the given boolean
	 */
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

}
