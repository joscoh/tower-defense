import java.io.*;

//creates an array of 50 Rounds from the given file
public class RoundReader {
	private BufferedReader br;
	String line = null;
	Point p;
	private Round[] rounds = new Round[50];

	/*
	 * Constructor - takes in a file and a starting point
	 */
	public RoundReader(String filename, Point p) {
		this.p = p;
		try {
			Reader r = new FileReader(filename);
			br = new BufferedReader(r);
			line = br.readLine();
			while (line != null) {
				int numRound = Integer.parseInt(line);
				line = br.readLine();
				int numBalloons = Integer.parseInt(line);
				Round toAdd = new Round(numBalloons);
				line = br.readLine();
				while (!line.equals("End of Round")) {
					if (line.contains("Red")) {
						for (int i = 0; i < getNumBalloons(line); i++) {
							toAdd.addBalloon(new Balloon(0, p.getX(), p.getY()));
						}
					} else if (line.contains("Blue")) {
						for (int i = 0; i < getNumBalloons(line); i++) {
							toAdd.addBalloon(new Balloon(1, p.getX(), p.getY()));
						}
					} else if (line.contains("Green")) {
						for (int i = 0; i < getNumBalloons(line); i++) {
							toAdd.addBalloon(new Balloon(2, p.getX(), p.getY()));
						}
					} else if (line.contains("Yellow")) {
						for (int i = 0; i < getNumBalloons(line); i++) {
							toAdd.addBalloon(new Balloon(3, p.getX(), p.getY()));
						}
					} else if (line.contains("Pink")) {
						for (int i = 0; i < getNumBalloons(line); i++) {
							toAdd.addBalloon(new Balloon(4, p.getX(), p.getY()));
						}
					}

					else if (line.contains("White")) {
						for (int i = 0; i < getNumBalloons(line); i++) {
							toAdd.addBalloon(new Balloon(5, p.getX(), p.getY()));
						}
					}

					else if (line.contains("Black")) {
						for (int i = 0; i < getNumBalloons(line); i++) {
							toAdd.addBalloon(new Balloon(6, p.getX(), p.getY()));
						}
					} else if (line.contains("Camo")) {
						for (int i = 0; i < getNumBalloons(line); i++) {
							toAdd.addBalloon(new Balloon(7, p.getX(), p.getY()));
						}
					} else if (line.contains("Lead")) {
						for (int i = 0; i < getNumBalloons(line); i++) {
							toAdd.addBalloon(new Balloon(8, p.getX(), p.getY()));
						}
					} else if (line.contains("Zebra")) {
						for (int i = 0; i < getNumBalloons(line); i++) {
							toAdd.addBalloon(new Balloon(9, p.getX(), p.getY()));
						}
					} else if (line.contains("Rainbow")) {
						for (int i = 0; i < getNumBalloons(line); i++) {
							toAdd.addBalloon(new Balloon(10, p.getX(), p.getY()));
						}
					} else if (line.contains("Ceramic")) {
						for (int i = 0; i < getNumBalloons(line); i++) {
							toAdd.addBalloon(new Balloon(11, p.getX(), p.getY()));
						}
					} else {
						System.out.println("Incorrect word: " + line);
					}
					line = br.readLine();
					rounds[numRound - 1] = toAdd;
				}
				line = br.readLine();
			}
		} catch (IOException e) {
			System.out.println("Round file not found");
		}
	}

	/*
	 * Returns the Round specified by the given index
	 */
	public Round getRound(int idx) {
		return rounds[idx];
	}

	/*
	 * Private static helper function to get the number of balloons from a
	 * string in the filename (eg "Red 10")
	 */
	private static int getNumBalloons(String s) {
		int firstSpace = s.indexOf(" ");
		String number = s.substring(firstSpace + 1, s.length());
		String numberTrim = number.trim();
		return Integer.parseInt(numberTrim);
	}

}
