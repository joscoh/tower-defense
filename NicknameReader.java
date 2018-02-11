import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * This class contains a single static function which reads all the stored nicknames 
 * from Nicknames.txt and stores them in a Map (mapping Nicknames to rank), which is returned
 */
public class NicknameReader {

	public static Map<String, Integer> getNicknameMap() {

		String filename = "files/Nicknames.txt";
		File toRead = new File(filename);
		Map<String, Integer> nicknames = new HashMap<String, Integer>();
		try {
			FileReader fr = new FileReader(toRead);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				String lTrim = line.trim();
				int space = lTrim.indexOf(" ");
				String name = lTrim.substring(0, space);
				String num = lTrim.substring(space + 1, lTrim.length());
				Integer rank = Integer.parseInt(num);
				nicknames.put(name, rank);
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Error in reading from save file");
		}
		return nicknames;
	}
}
