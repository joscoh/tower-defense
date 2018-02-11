
import java.util.Map;
import java.util.Set;
import java.io.*;

/*
 * This class contains a single static function which writes a given map of nicknames to a file Nicknames.txt
 */
public class NicknameWriter {

	public static void writeNicknames(Map<String, Integer> nicknames) {
		String filename = "files/Nicknames.txt";
		File fileOut = new File(filename);
		try {
			FileWriter fr = new FileWriter(fileOut);
			BufferedWriter br = new BufferedWriter(fr);
			Set<String> keys = nicknames.keySet();
			for (String k : keys) {
				int rank = nicknames.get(k);
				br.write(k + " " + rank);
				br.newLine();
			}
			br.close();
		} catch (IOException e) {
			System.out.println("problem writing");
		}
	}
}
