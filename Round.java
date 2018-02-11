/*
 * a round stores the balloons for a given round in an array
 */

public class Round {
	private int numBalloons;
	private Balloon[] balloons;
	private int idx = 0;
	private int balloonsTaken = 0;

	/*
	 * Creates a round with a given number of balloons
	 */
	public Round(int numBalloons) {
		this.numBalloons = numBalloons;
		balloons = new Balloon[numBalloons];
	}

	/*
	 * Adds a balloon to the round
	 */
	public void addBalloon(Balloon b) {
		balloons[idx] = b;
		idx++;
	}

	/*
	 * Returns the number of balloons in the round
	 */
	public int getNumBalloons() {
		return numBalloons;
	}

	/*
	 * Returns the next Balloon
	 */
	public Balloon getNextBalloon() {
		if (balloonsTaken >= balloons.length) {
			throw new IllegalArgumentException("No More Balloons To Take");
		}
		Balloon b = balloons[balloonsTaken];
		balloonsTaken++;
		return b;
	}

	/*
	 * Returns true if the round has balloons left, false otherwise
	 */
	public boolean hasMoreBalloons() {
		return balloonsTaken != numBalloons;
	}
}