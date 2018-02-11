import java.io.*;
import java.util.Set;

//This class contains a single static function that writes a game state to a file
public class SaveWriter {

	public static void writeToFile(String username, Set<Tower> towers, Set<BananaFarm> farms, int currRound, int money,
			int lives) {
		String filename = ("files/" + username + ".txt");
		File fileOut = new File(filename);
		try {
			FileWriter fr = new FileWriter(fileOut);
			BufferedWriter br = new BufferedWriter(fr);
			br.write(username);
			br.newLine();
			br.write("" + currRound);
			br.newLine();
			br.write("" + money);
			br.newLine();
			br.write("" + lives);
			br.newLine();
			if (towers.size() > 0) {
				for (Tower t : towers) {
					br.write(t.getClass() + " " + t.getX() + " " + t.getY() + " " + t.getUpgradeLevel());
					br.newLine();
				}
			}
			br.write("Towers Done");
			br.newLine();
			if (farms.size() > 0) {
				for (BananaFarm b : farms) {
					br.write(b.getX() + " " + b.getY() + " " + b.getLevel());
					br.newLine();
				}
			}
			br.write("Farms Done");
			br.close();
		} catch (IOException e) {
			System.out.println("problem writing");
		}
	}
}
