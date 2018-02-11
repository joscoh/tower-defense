import java.awt.Color;
import java.awt.Graphics;
import java.io.*;

/*
 * A LevelMap reads map information from a file and stores the map, a 2D array of Cells, 
 * along with the Path, along which the balloons travel
 */
public class LevelMap {
	private BufferedReader br;
	private String line;
	private Point prev;
	public static final int MAP_SIZE = 20;
	public static final int CELL_SIZE = 30;
	private Cell[][] map = new Cell[MAP_SIZE][MAP_SIZE];
	private final Color backgroundColor = new Color(213, 200, 158);
	private final Color pathColor = new Color(128, 128, 128);
	private Path path;

	/*
	 * A LevelMap takes in a text file with a specific format
	 */
	public LevelMap(String filename) {
		try {
			Reader r = new FileReader(filename);
			br = new BufferedReader(r);
			line = br.readLine();
			prev = readPoint(line);
			path = new Path();
			// reads path information - on the file, each line is the next point
			// in the path that changes direction
			while (line != null && !line.equals("End of Path")) {
				Point p = readPoint(line);
				int prevX = prev.getX() * LevelMap.CELL_SIZE;
				int prevY = prev.getY() * LevelMap.CELL_SIZE;
				int currX = p.getX() * LevelMap.CELL_SIZE;
				int currY = p.getY() * LevelMap.CELL_SIZE;
				Direction d;
				if (prevX == currX && currY > prevY) {
					d = Direction.down;
					for (int i = prevY; i < currY; i++) {
						map[prevX / LevelMap.CELL_SIZE][i / LevelMap.CELL_SIZE] = new Cell(true, false, false);
						PathElement e = new PathElement(new Point(prevX, i), path.size(), d);
						path.add(e);
					}
				} else if (prevX == currX && currY < prevY) {
					d = Direction.up;
					for (int i = prevY; i > currY; i--) {
						map[prevX / LevelMap.CELL_SIZE][i / LevelMap.CELL_SIZE] = new Cell(true, false, false);
						PathElement e = new PathElement(new Point(prevX, i), path.size(), d);
						path.add(e);
					}
				} else if (prevY == currY && currX > prevX) {
					d = Direction.right;
					for (int i = prevX; i < currX; i++) {
						map[i / LevelMap.CELL_SIZE][prevY / LevelMap.CELL_SIZE] = new Cell(true, false, false);
						PathElement e = new PathElement(new Point(i, currY), path.size(), d);
						path.add(e);
					}
				} else if (prevY == currY && currX < prevX) {
					d = Direction.left;
					for (int i = prevX; i > currX; i--) {
						map[i / LevelMap.CELL_SIZE][prevY / LevelMap.CELL_SIZE] = new Cell(true, false, false);
						PathElement e = new PathElement(new Point(i, currY), path.size(), d);
						path.add(e);
					}
				}
				prev = p;
				line = br.readLine();
			}
			line = br.readLine();
			// sets water cells as water (listed on file after path)
			while (line != null && !line.equals("End of Water")) {
				Point p = readPoint(line);
				int x = p.getX();
				int y = p.getY();
				map[x][y] = new Cell(false, true, true);
				line = br.readLine();
			}
			// initialize the rest of the squares as available
			for (int i = 0; i < MAP_SIZE; i++) {
				for (int j = 0; j < MAP_SIZE; j++) {
					if (map[i][j] == null) {
						map[i][j] = new Cell(false, false, true);
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Error in reading file");
		}
	}

	// Note that the following several methods are to ensure the encapsulation
	// of the path and its ArrayList
	/*
	 * Returns the PathElement of a given index
	 */
	public PathElement getPathElement(int idx) {
		return path.get(idx);
	}

	/*
	 * Returns the first Point of the path
	 */
	public Point getPathStart() {
		return path.getPathStart();
	}

	/*
	 * Returns the size of the path
	 */
	public int getPathSize() {
		return path.size();
	}

	/*
	 * Returns the cell of the given indices
	 */
	public Cell getCell(int i, int j) {
		return map[i][j];
	}

	/*
	 * Returns the first Direction of the path
	 */
	public Direction getFirstDirection() {
		return path.getFirstDirection();
	}

	/*
	 * Returns the pathElement index of a given Point (or -1 if the Point is not
	 * on the path)
	 */
	public int getNumberOfPoint(Point p) {
		return path.getNumberOfPoint(p);
	}

	/*
	 * Draws the map, given a Graphics object. Water is colored blue, the path
	 * is gray, and the rest is light brown
	 */
	public void drawMap(Graphics g) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				if (map[i][j] == null) {
				} else if (map[i][j].isPath()) {
					g.setColor(pathColor);
					g.fillRect(CELL_SIZE * i, CELL_SIZE * j, CELL_SIZE, CELL_SIZE);
				} else if (map[i][j].isWater()) {
					g.setColor(Color.BLUE);
					g.fillRect(CELL_SIZE * i, CELL_SIZE * j, CELL_SIZE, CELL_SIZE);
				} else {
					g.setColor(backgroundColor);
					g.fillRect(CELL_SIZE * i, CELL_SIZE * j, CELL_SIZE, CELL_SIZE);
				}
			}
		}

	}

	/*
	 * Private static helper method to read a point based on a string that has 2
	 * numbers separated by a space
	 */
	private static Point readPoint(String str) {
		String trimLine = str.trim();
		int space = trimLine.indexOf(" ");
		String xStr = trimLine.substring(0, space);
		String yStr = trimLine.substring(space + 1, trimLine.length());
		String xTr = xStr.trim();
		String yTr = yStr.trim();
		int x = Integer.parseInt(xTr);
		int y = Integer.parseInt(yTr);
		return new Point(x, y);
	}
}
