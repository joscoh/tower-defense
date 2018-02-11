import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

//this class reads save information from a save file
public class SaveReader {
	private String username;
	private int currRound;
	// private int level;
	private int money;
	private int lives;
	private Set<Tower> towers;
	private Set<BananaFarm> farms;
	private BufferedReader br;

	/*
	 * Constructor - takes in a username and a levelMap
	 */
	public SaveReader(String username, LevelMap map) {
		String filename = ("files/" + username + ".txt");
		File toRead = new File(filename);
		towers = new HashSet<Tower>();
		farms = new HashSet<BananaFarm>();
		try {
			FileReader fr = new FileReader(toRead);
			br = new BufferedReader(fr);
			// error checking for username
			if (!username.equals(br.readLine())) {
				throw new IOException();
			}
			this.currRound = Integer.parseInt(br.readLine());
			this.money = Integer.parseInt(br.readLine());
			this.lives = Integer.parseInt(br.readLine());
			String line = br.readLine();
			// add towers
			while (!line.equals("Towers Done")) {
				String ridOfClass = line.substring(5, line.length());
				String trim = ridOfClass.trim();
				int firstSpace = trim.indexOf(" ");
				String towerName = trim.substring(0, firstSpace);
				String rest = trim.substring(firstSpace + 1, trim.length());
				int secondSpace = rest.indexOf(" ");
				String xCoord = rest.substring(0, secondSpace);
				int x = Integer.parseInt(xCoord);
				String lastTwoNumbers = rest.substring(secondSpace + 1, rest.length());
				int thirdSpace = lastTwoNumbers.indexOf(" ");
				String yCoord = lastTwoNumbers.substring(0, thirdSpace);
				int y = Integer.parseInt(yCoord);
				String lastNumber = lastTwoNumbers.substring(thirdSpace + 1, lastTwoNumbers.length());
				String upgradeLevelStr = lastNumber.trim();
				int upgradeLevel = Integer.parseInt(upgradeLevelStr);
				if (towerName.equals("DartMonkey")) {
					towers.add(new DartMonkey(x, y, map, false, upgradeLevel));
				} else if (towerName.equals("TackShooter")) {
					towers.add(new TackShooter(x, y, false, upgradeLevel));
				} else if (towerName.equals("BombTower")) {
					towers.add(new BombTower(x, y, map, false, upgradeLevel));
				} else if (towerName.equals("MortarTower")) {
					towers.add(new MortarTower(x, y, false, upgradeLevel));
				} else if (towerName.equals("MonkeyBuccaneer")) {
					towers.add(new MonkeyBuccaneer(x, y, map, false, upgradeLevel));
				} else if (towerName.equals("SuperMonkey")) {
					towers.add(new SuperMonkey(x, y, map, false, upgradeLevel));
				} else {
					throw new IOException();
				}
				line = br.readLine();
			}
			line = br.readLine();
			// add banana farms
			while (!line.equals("Farms Done")) {
				String trim = line.trim();
				int firstSpace = trim.indexOf(" ");
				String xCoord = trim.substring(0, firstSpace);
				int x = Integer.parseInt(xCoord);
				String twoNums = trim.substring(firstSpace + 1, trim.length());
				int secondSpace = twoNums.indexOf(" ");
				String yCoord = twoNums.substring(0, secondSpace);
				int y = Integer.parseInt(yCoord);
				String levelStr = twoNums.substring(secondSpace + 1, twoNums.length());
				int level = Integer.parseInt(levelStr);
				farms.add(new BananaFarm(x, y, level));
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}

	/*
	 * See if a given username is valid to load from (if its file exists)
	 */
	public static boolean isUsernameValid(String username) {
		try {
			String filename = ("files/" + username + ".txt");
			File toRead = new File(filename);
			FileReader fr = new FileReader(toRead);
			fr.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/*
	 * Return the username
	 */
	public String getUsername() {
		return username;
	}

	/*
	 * Return the round saved on
	 */
	public int getCurrRound() {
		return currRound;
	}

	/*
	 * Return the money the user had at the time of saving
	 */
	public int getMoney() {
		return money;
	}

	/*
	 * Return the lives a user had left
	 */
	public int getLives() {
		return lives;
	}

	/*
	 * Return the set of towers the user had
	 */
	public Set<Tower> getTowers() {
		return towers;
	}

	/*
	 * Return the set of banana farms the user had
	 */
	public Set<BananaFarm> getFarms() {
		return farms;
	}

}
