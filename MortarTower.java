import java.util.HashSet;
import java.util.Set;

public class MortarTower extends Tower {
	public static final int[] moneyForLevel = {170, 170, 680};
	public static final String[] levelNames = {"Bigger Bombs", "Faster Firing", "Destroy 2 Layers of Ballons at Once"};
	private Point target;
	private int timeAlive = 1;
	private int range = 3;
	private boolean twoLayer = false;
	/*
	 * Constructor - takes in location, whether or not it is a preview, and the upgradeLevel
	 */
	public MortarTower(int x, int y, boolean preview, int upgradeLevel) {
		super(x, y, 59, 0, null, preview, false, "files/MortarTower.png");
		target = new Point(x, y);
		while(getUpgradeLevel() < upgradeLevel) {
			this.upgradeTower(getUpgradeLevel() + 1);
		}
	}
	
	/*
	 * Sets the target to the given coordinates, given as Cell coordinates (from 0 to 20)
	 */
	public void setTarget(int x, int y) {
		target = new Point(x, y);
	}
	
	/*
	 * Overrides attack from Tower - returns a single MortarShell
	 */
	@Override
	public Set<Attacker> attack(Balloon b) {
		Set<Attacker> toReturn = new HashSet<Attacker>();
		toReturn.add(new MortarShell(target.getX(), target.getY(), timeAlive, range, twoLayer));
		return toReturn;
	}
	

	/*
	 * Overrides upgradeTower from Attacker
	 */
	@Override
	public void upgradeTower(int nextLevel) {
		if(nextLevel == 1) {
			range = 4;
			this.increaseLevel();
		}
		else if(nextLevel == 2) {
			this.setFireRate(50);
			this.increaseLevel();
		}
		else if(nextLevel == 3) {
			twoLayer = true;
			this.increaseLevel();
		}
		else{
			return;
		}
		
	}
	
	/*
	 * Returns the range of the mortar shell
	 */
	public int getMortarRange() {
		return range;
	}

}
