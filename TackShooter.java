import java.util.HashSet;
import java.util.Set;

public class TackShooter extends Tower {

	public static final int[] moneyForLevel = { 210, 100 };
	public static final String[] levelNames = { "Faster Shooting", "Extra Range" };
	private int timeAlive = 4;

	/*
	 * Constructor - takes in a position, whether or not it is a preview, and
	 * the upgrade level
	 */
	public TackShooter(int x, int y, boolean preview, int upgradeLevel) {
		super(x, y, 48, 1, null, preview, false, "files/TackShooter.png");
		while (getUpgradeLevel() < upgradeLevel) {
			this.upgradeTower(getUpgradeLevel() + 1);
		}
	}

	/*
	 * Overrides attack from Tower - returns a Set with 8 UndirectedDarts
	 */
	@Override
	public Set<Attacker> attack(Balloon b) {
		// this tower fires darts out in 8 directions
		// need to figure out target points here
		Set<Attacker> attackers = new HashSet<Attacker>();
		int[] xS = new int[8];
		int[] yS = new int[8];
		int xCenter = this.getX() + LevelMap.CELL_SIZE / 2;
		int yCenter = this.getY() + LevelMap.CELL_SIZE / 2;
		double[] angles = { 0, Math.PI / 4, Math.PI / 2, 3 * Math.PI / 4, Math.PI, 5 * Math.PI / 4, 3 * Math.PI / 2,
				7 * Math.PI / 4 };
		for (int i = 0; i < 8; i++) {
			xS[i] = (int) Math.round(xCenter + LevelMap.CELL_SIZE * Math.cos(angles[i]));
			yS[i] = (int) Math.round(yCenter - LevelMap.CELL_SIZE * Math.sin(angles[i]));
			Point p = new Point(xS[i], yS[i]);
			Attacker toAdd = new UndirectedDart(xCenter, yCenter,
					10 /* see about this */, p, timeAlive);
			attackers.add(toAdd);
		}
		return attackers;
	}

	/*
	 * Overrides upgradeTower from Tower
	 */
	@Override
	public void upgradeTower(int nextLevel) {
		if (nextLevel == 1) {
			this.setFireRate(40);
			this.increaseLevel();
		} else if (nextLevel == 2) {
			this.setRange(2);
			timeAlive = 6;
			this.increaseLevel();
		} else {
			return;
		}
	}
}
