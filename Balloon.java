import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;

public class Balloon {

	/*
	 * Static fields include the dimensions, which are the same for all
	 * balloons, and 2 arrays that give the health and colors for different
	 * types of balloons (to avoid a lot of complicated conditionals)
	 */
	public static final int WIDTH = 26;
	public static final int HEIGHT = 30;
	public static final int[] colorHealth = { 1, 2, 3, 4, 5, 11, 11, 11, 23, 23, 47, 103 };
	public static final Color[] colors = { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PINK, Color.WHITE,
			Color.BLACK, new Color(0, 73, 0), new Color(105, 105, 105) };
	private static final int speed = 2;

	private int color;
	private boolean isSharpResistant;
	private boolean isExplodeResistant;
	private boolean isCamo;
	private int health;
	private int x;
	private int y;
	private int vx;
	private int vy;
	private Direction direction;

	/*
	 * Balloon constructor takes in an int representing the color and a location
	 */
	public Balloon(int color, int x, int y) {
		updateBalloonColor(color);
		health = colorHealth[color];
		this.color = color;
		this.x = x;
		this.y = y;
	}

	/*
	 * Private helper function that updates whether a balloon is camo, explosive
	 * resistant, or sharp resistant based on the color Called in constructor
	 * and when color changes
	 */
	private void updateBalloonColor(int color) {
		isCamo = false;
		isSharpResistant = false;
		isExplodeResistant = false;
		// camo balloon
		if (color == 7) {
			isCamo = true;
		}
		// lead balloon
		else if (color == 8) {
			isSharpResistant = true;
		}
		// black and zebra balloons
		else if (color == 6 || color == 9) {
			isExplodeResistant = true;
		}
	}

	/*
	 * Returns the index of the path element the balloon is currently on,
	 * relative to a given level map
	 */
	public int getPathIndex(LevelMap map) {
		return map.getNumberOfPoint(new Point(x, y));
	}

	/*
	 * Returns the health of the given balloon
	 */
	public int getHealth() {
		return health;
	}

	/*
	 * Damages the given balloon, subtracting one from its health
	 */
	public void damage() {
		health--;
	}

	/*
	 * Updates a balloon, if it has downgraded to a new color Note that at some
	 * levels, if a balloon dies it spawns two new balloons of a lower color; in
	 * other cases, it simply decrements the color
	 */
	public Balloon update(LevelMap map) {
		if (health <= 0 || color == 0) {
			return null;
		}
		// black, white, and camo balloons create 2 pinks
		if (color >= 5 && color <= 7 && health < colorHealth[color]) {
			color = 4;
			Balloon b = anotherBalloon(color, map);
			updateBalloonColor(color);
			health = colorHealth[color];
			return b;
		}
		// lead balloons create 2 black balloons
		else if (color == 8 && health < colorHealth[color]) {
			color = 6;
			Balloon b = anotherBalloon(color, map);
			updateBalloonColor(color);
			health = colorHealth[color];
			return b;

		}
		// zebra balloons create a black and a white
		else if (color == 9 && health < colorHealth[color]) {
			color = 6;
			Balloon b = anotherBalloon(color, map);
			color = 5;
			updateBalloonColor(color);
			health = colorHealth[color];
			return b;
		}
		// rainbow balloons create two zebras
		else if (color == 10 && health < colorHealth[color]) {
			color = 9;
			Balloon b = anotherBalloon(color, map);
			updateBalloonColor(color);
			health = colorHealth[color];
			return b;
		}
		// ceramic balloons take 9 hits to destroy, and then creates 2 rainbows
		else if (color == 11) {
			if (health < colorHealth[color] - 9) {
				color = 10;
				Balloon b = anotherBalloon(color, map);
				updateBalloonColor(color);
				health = colorHealth[color];
				return b;
			}

		}
		// for all other balloons, decrease color
		else if (health < colorHealth[color]) {
			color--;
		}
		updateBalloonColor(color);
		return null;

	}

	/*
	 * Private helper method that creates a Balloon with the given color one
	 * cell behind the current balloon. It is used when creating additional
	 * balloons for zebras, rainbows, and the like
	 */
	private Balloon anotherBalloon(int color, LevelMap map) {
		int pathIdx = this.getPathIndex(map);
		if (pathIdx >= 30) {
			pathIdx -= 30;
		}
		Direction d = map.getPathElement(pathIdx).getDirection();
		Point location = map.getPathElement(pathIdx).getLocation();
		Balloon b = new Balloon(color, location.getX(), location.getY());
		b.setDirection(d);
		return b;
	}

	/*
	 * Tells whether the given balloon is resistant to sharp objects
	 */
	public boolean isSharpResistant() {
		return isSharpResistant;
	}

	/*
	 * Tells whether the given balloon is resistant to explosives
	 */
	public boolean isExplodeResistant() {
		return isExplodeResistant;
	}

	/*
	 * Tells whether the given balloon is a camo balloon
	 */
	public boolean isCamo() {
		return isCamo;
	}

	/*
	 * Returns the x coordinate of the given balloon
	 */
	public int getX() {
		return x;
	}

	/*
	 * Returns the y coordinate of the given balloon
	 */
	public int getY() {
		return y;
	}

	/*
	 * Moves the given balloon, depending on its direction
	 */
	public void move() {
		switch (direction) {
		case up:
			vx = 0;
			vy = -speed;
			break;
		case down:
			vx = 0;
			vy = speed;
			break;
		case right:
			vx = speed;
			vy = 0;
			break;
		case left:
			vx = -speed;
			vy = 0;
			break;
		}
		x += vx;
		y += vy;
	}

	/*
	 * Draws the balloon, given a Graphics object
	 */
	public void draw(Graphics g) {
		if (color <= 8) {
			Color paintColor = colors[color];
			g.setColor(paintColor);
			g.fillOval(x, y, WIDTH, HEIGHT);
		}
		// zebra balloon
		else if (color == 9) {
			Graphics2D g0 = (Graphics2D) g;
			g.setColor(Color.BLACK);
			Shape bottom = new Arc2D.Double(x, y, WIDTH, HEIGHT, 180, 180, 2);
			g0.fill(bottom);
			g.setColor(Color.WHITE);
			Shape top = new Arc2D.Double(x, y, WIDTH, HEIGHT, 0, 180, 2);
			g0.fill(top);
		}
		// rainbow balloon
		else if (color == 10) {
			Graphics2D g0 = (Graphics2D) g;
			g.setColor(Color.RED);
			Shape first = new Arc2D.Double(x, y, WIDTH, HEIGHT, 0, 90, 2);
			g0.fill(first);
			g.setColor(Color.YELLOW);
			Shape second = new Arc2D.Double(x, y, WIDTH, HEIGHT, 90, 90, 2);
			g0.fill(second);
			g.setColor(Color.BLUE);
			Shape third = new Arc2D.Double(x, y, WIDTH, HEIGHT, 180, 90, 2);
			g0.fill(third);
			g.setColor(Color.GREEN);
			Shape fourth = new Arc2D.Double(x, y, WIDTH, HEIGHT, 270, 90, 2);
			g0.fill(fourth);
		} else if (color == 11) {
			Color paintColor = new Color(139, 69, 19);
			g.setColor(paintColor);
			g.fillOval(x, y, WIDTH, HEIGHT);
		}
	}

	/*
	 * Sets the direction of the balloon to the given direction
	 */
	public void setDirection(Direction d) {
		this.direction = d;
	}
}
