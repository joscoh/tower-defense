import java.util.HashSet;
import java.util.Set;

public class MonkeyBuccaneer extends Tower {

	public static final int[] moneyForLevel = { 255, 255, 155 };
	public static final String[] levelNames = { "Multiple Darts", "Detect Camo Balloons", "Longer Range" };
	private boolean multipleDarts = false;

	/*
	 * Constructor takes in position, levelMap, whether or not it is a preview,
	 * and the upgrade level
	 */
	public MonkeyBuccaneer(int x, int y, LevelMap m, boolean preview, int upgradeLevel) {
		super(x, y, 20, 5, m, preview, true, "files/MonkeyBuccaneer.png");
		while (getUpgradeLevel() < upgradeLevel) {
			this.upgradeTower(getUpgradeLevel() + 1);
		}

	}

	/*
	 * Overrides attack method from Tower. This method returns a single
	 * directedDart, unless the tower is sufficiently upgraded, when it will
	 * return a single undirectedDart and 5 other undirectedDarts at angles
	 * close to the original
	 */
	@Override
	public Set<Attacker> attack(Balloon b) {
		int directX = b.getX();
		int directY = b.getY();
		double speed = this.getSpeedForPoint(new Point(directX, directY));
		Point p = Tower.whereToAttack(new Point(directX, directY), this.getMap());
		Attacker attacker = new DirectedDart(p, b, this.getX() + LevelMap.CELL_SIZE / 2,
				this.getY() + LevelMap.CELL_SIZE / 2, speed);
		Set<Attacker> toReturn = new HashSet<Attacker>();
		toReturn.add(attacker);
		if (multipleDarts) {
			double angle = Math.atan2((this.getY() - b.getY()), this.getX() - b.getX());
			for (int i = -2; i < 3; i++) {
				double newAngle = angle + (0.1) * i;
				int targetX = (int) Math.round(this.getX() - LevelMap.CELL_SIZE * Math.cos(newAngle));
				int targetY = (int) Math.round(this.getY() - LevelMap.CELL_SIZE * Math.sin(newAngle));
				Point p0 = new Point(targetX, targetY);
				Attacker toAdd = new UndirectedDart(this.getX(), this.getY(), (int) speed, p0, 20);
				toReturn.add(toAdd);
			}
		}

		return toReturn;
	}

	/*
	 * Overrides upgradeTower
	 */
	@Override
	public void upgradeTower(int nextLevel) {
		if (nextLevel == 1) {
			multipleDarts = true;
			this.increaseLevel();
		} else if (nextLevel == 2) {
			this.enableDetectCamo();
			this.increaseLevel();
		} else if (nextLevel == 3) {
			this.setRange(getRange() + 2);
			this.increaseLevel();
		} else {
			return;
		}

	}

}
