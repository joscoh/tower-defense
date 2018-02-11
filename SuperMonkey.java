import java.util.HashSet;
import java.util.Set;

public class SuperMonkey extends Tower {
	public static final int[] moneyForLevel = { 850, 2975, 3400 };
	public static final String[] levelNames = { "Super Range", "Laser Vision", "Plasma Vision" };
	private boolean lasers = false;
	private boolean plasma = false;

	/*
	 * Constructor takes in a position, a levelmap, whether or not it is a
	 * preview, and the upgrade level
	 */
	public SuperMonkey(int x, int y, LevelMap m, boolean preview, int upgradeLevel) {
		super(x, y, 2, 4, m, preview, true, "files/SuperMonkey.png");
		while (getUpgradeLevel() < upgradeLevel) {
			this.upgradeTower(getUpgradeLevel() + 1);
		}

	}

	/*
	 * Overrides attack -returns a set containing a sigle directedDart, laser,
	 * or plasma, depending on the upgrade level
	 */
	@Override
	public Set<Attacker> attack(Balloon b) {
		int directX = b.getX();
		int directY = b.getY();
		double speed = this.getSpeedForPoint(new Point(directX, directY));
		Point p = Tower.whereToAttack(new Point(directX, directY), this.getMap());
		Set<Attacker> toReturn = new HashSet<Attacker>();
		Attacker attacker;
		if (!lasers && !plasma) {
			attacker = new DirectedDart(p, b, this.getX() + LevelMap.CELL_SIZE / 2,
					this.getY() + LevelMap.CELL_SIZE / 2, speed);
		} else if (lasers) {
			attacker = new Laser(p, b, this.getX() + LevelMap.CELL_SIZE / 2, this.getY() + LevelMap.CELL_SIZE / 2,
					speed);
		} else {
			attacker = new Plasma(p, b, this.getX() + LevelMap.CELL_SIZE / 2, this.getY() + LevelMap.CELL_SIZE / 2,
					speed);
		}

		toReturn.add(attacker);
		return toReturn;
	}

	/*
	 * Overrides upgradeTower from Tower
	 */
	@Override
	public void upgradeTower(int nextLevel) {
		if (nextLevel == 1) {
			this.setRange(6);
			this.increaseLevel();
		} else if (nextLevel == 2) {
			lasers = true;
			this.increaseLevel();
		} else if (nextLevel == 3) {
			lasers = false;
			plasma = true;
			this.setFireRate(1);
			this.increaseLevel();
		} else {
			return;
		}

	}

}
