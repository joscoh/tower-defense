import java.util.HashSet;
import java.util.Set;

public class BombTower extends Tower {

	public static final int[] moneyForLevel = { 400, 300, 210 };
	public static final String[] levelNames = { "Bigger Bombs", "Longer Range", "Better Bombs" };
	private int range = 2;

	/*
	 * Constructor - takes in a position, a levelmap, whether or not it is a
	 * preview, and the upgrade leve
	 */
	public BombTower(int x, int y, LevelMap m, boolean preview, int upgradeLevel) {
		super(x, y, 45, 3, m, preview, true, "files/BombTower.png");
		while (getUpgradeLevel() < upgradeLevel) {
			this.upgradeTower(getUpgradeLevel() + 1);
		}
	}

	/*
	 * Overrides attack from Tower, returns a set with a single bomb
	 */
	@Override
	public Set<Attacker> attack(Balloon b) {
		int directX = b.getX();
		int directY = b.getY();
		double speed = this.getSpeedForPoint(new Point(directX, directY));
		Point p = Tower.whereToAttack(new Point(directX, directY), this.getMap());
		Attacker attacker = new Bomb(p, b, this.getX() + LevelMap.CELL_SIZE / 2, this.getY() + LevelMap.CELL_SIZE / 2,
				speed, range);
		Set<Attacker> toReturn = new HashSet<Attacker>();
		toReturn.add(attacker);
		return toReturn;

	}

	/*
	 * Overrides upgradeTower from Tower
	 */
	public void upgradeTower(int nextLevel) {
		if (nextLevel == 1) {
			range = 3;
			this.increaseLevel();
		} else if (nextLevel == 2) {
			this.setRange(4);
			this.increaseLevel();
		} else if (nextLevel == 3) {
			range = 4;
			this.setFireRate(40);
			this.setRange(5);
			this.increaseLevel();
		} else {
			return;
		}
	}
}
