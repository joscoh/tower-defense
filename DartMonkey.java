import java.util.HashSet;
import java.util.Set;

public class DartMonkey extends Tower {

	public static final int[] moneyForLevel = { 140, 90 };
	public static final String[] levelNames = { "Faster Darts", "Long Range Darts" };

	/*
	 * Constructor - takes in location, levelMap, whether or not tower is a
	 * preview, and upgrade level
	 */
	public DartMonkey(int x, int y, LevelMap m, boolean preview, int upgradeLevel) {
		super(x, y, 28, 3, m, preview, true, "files/DartMonkey.png");
		while (getUpgradeLevel() < upgradeLevel) {
			this.upgradeTower(getUpgradeLevel() + 1);
		}

	}

	/*
	 * Overrides attack from Tower - returns a set with one DirectedDart
	 */
	public Set<Attacker> attack(Balloon b) {
		int directX = b.getX();
		int directY = b.getY();
		double speed = this.getSpeedForPoint(new Point(directX, directY));
		Point p = Tower.whereToAttack(new Point(directX, directY), this.getMap());
		Attacker attacker = new DirectedDart(p, b, this.getX() + LevelMap.CELL_SIZE / 2,
				this.getY() + LevelMap.CELL_SIZE / 2, speed);
		Set<Attacker> toReturn = new HashSet<Attacker>();
		toReturn.add(attacker);
		return toReturn;
	}

	/*
	 * Override upgradeTower from Tower, takes in next level
	 */
	public void upgradeTower(int nextLevel) {
		if (nextLevel == 1) {
			this.setFireRate(22);
			this.increaseLevel();
		} else if (nextLevel == 2) {
			this.setRange(4);
			this.increaseLevel();
		} else {
			return;
		}
	}

}
