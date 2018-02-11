import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/*
 * Class that runs game
 */
public class Game implements Runnable {
	public void run() {

		final JFrame frame = new JFrame("TOP LEVEL FRAME");
		frame.setLocation(0, 0);

		// Status panel
		final JPanel status_panel = new JPanel();
		frame.add(status_panel, BorderLayout.SOUTH);
		final JLabel status = new JLabel("Round: 0 Lives: 100 Money: 650");
		status_panel.add(status);
		// Main playing area
		final GameCourt court = new GameCourt(status);
		frame.add(court, BorderLayout.CENTER);

		// Start Round Button
		final JPanel control_panel = new JPanel();
		frame.add(control_panel, BorderLayout.LINE_END);

		/*
		 * This button allows the user to enter DartMonkey mode when pressed,
		 * provided they have enough money
		 */
		BufferedImage dartMonkeyImage;
		ImageIcon dartMonkeyIcon = null;
		try {
			dartMonkeyImage = ImageIO.read(new File("files/DartMonkey.png"));
			dartMonkeyIcon = new ImageIcon(dartMonkeyImage);

		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		final JButton dartMonkey = new JButton("<html>Dart Monkey<br />Cost: 165</html>", dartMonkeyIcon);
		dartMonkey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.setMode(Mode.DartMonkey);
			}
		});

		/*
		 * This button allows the user to enter TackShooter mode when pressed,
		 * provided they have unlocked this tower and they have enough money
		 */
		BufferedImage tackShooterImage;
		ImageIcon tackShooterIcon = null;
		try {
			tackShooterImage = ImageIO.read(new File("files/TackShooter.png"));
			tackShooterIcon = new ImageIcon(tackShooterImage);

		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		final JButton tackShooter = new JButton("<html>Tack Shooter<br />Cost: 305</html>", tackShooterIcon);

		tackShooter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.setMode(Mode.TackShooter);
			}
		});

		/*
		 * This button allows the user to enter BombTower mode when pressed,
		 * provided they have unlocked this tower and they have enough money
		 */
		BufferedImage bombTowerImage;
		ImageIcon bombTowerIcon = null;
		try {
			bombTowerImage = ImageIO.read(new File("files/BombTower.png"));
			bombTowerIcon = new ImageIcon(bombTowerImage);

		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		final JButton bombTower = new JButton("<html>Bomb Tower<br />Cost: 595</html>", bombTowerIcon);

		bombTower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.setMode(Mode.BombTower);
			}
		});

		/*
		 * This button allows the user to enter MortarTower mode when pressed,
		 * provided they have unlocked this tower and they have enough money
		 */
		BufferedImage mortarTowerImage;
		ImageIcon mortarTowerIcon = null;
		try {
			mortarTowerImage = ImageIO.read(new File("files/MortarTower.png"));
			mortarTowerIcon = new ImageIcon(mortarTowerImage);

		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		final JButton mortarTower = new JButton("<html>Mortar Tower<br />Cost: 700</html>", mortarTowerIcon);

		mortarTower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.setMode(Mode.MortarTower);
			}
		});

		/*
		 * This button allows the user to enter MonkeyBuccanneer mode when
		 * pressed, provided they have unlocked this tower and they have enough
		 * money
		 */
		BufferedImage monkeyBuccaneerImage;
		ImageIcon monkeyBuccaneerIcon = null;
		try {
			monkeyBuccaneerImage = ImageIO.read(new File("files/MonkeyBuccaneer.png"));
			monkeyBuccaneerIcon = new ImageIcon(monkeyBuccaneerImage);

		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		final JButton monkeyBuccaneer = new JButton("<html>Monkey Buccaneer<br />Cost: 600</html>",
				monkeyBuccaneerIcon);

		monkeyBuccaneer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.setMode(Mode.MonkeyBuccaneer);
			}
		});

		/*
		 * This button allows the user to enter SuperMonkey mode when pressed,
		 * provided they have unlocked this tower and they have enough money
		 */
		BufferedImage superMonkeyImage;
		ImageIcon superMonkeyIcon = null;
		try {
			superMonkeyImage = ImageIO.read(new File("files/SuperMonkey.png"));
			superMonkeyIcon = new ImageIcon(superMonkeyImage);

		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		final JButton superMonkey = new JButton("<html>Super Monkey<br />Cost: 3400</html>", superMonkeyIcon);

		superMonkey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.setMode(Mode.SuperMonkey);
			}
		});

		/*
		 * This button allows the user to enter RoadSpike mode when pressed,
		 * provided they have unlocked this tower and they have enough money
		 */
		BufferedImage roadSpikeImage;
		ImageIcon roadSpikeIcon = null;
		try {
			roadSpikeImage = ImageIO.read(new File("files/RoadSpike.png"));
			roadSpikeIcon = new ImageIcon(roadSpikeImage);

		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		final JButton roadSpike = new JButton("<html>Road Spike<br />Cost: 30</html>", roadSpikeIcon);

		roadSpike.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.setMode(Mode.RoadSpike);
			}
		});

		/*
		 * This button allows the user to enter BananaFarm mode when pressed,
		 * provided they have unlocked this tower and they have enough money
		 */
		BufferedImage bananaFarmImage;
		ImageIcon bananaFarmIcon = null;
		try {
			bananaFarmImage = ImageIO.read(new File("files/BananaFarm.png"));
			bananaFarmIcon = new ImageIcon(bananaFarmImage);

		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		final JButton bananaFarm = new JButton("<html>Banana Farm<br />Cost: 850</html>", bananaFarmIcon);

		bananaFarm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.setMode(Mode.BananaFarm);
			}
		});
		// control layout of buttons
		control_panel.setLayout(new GridLayout(2, 1));
		JPanel inner = new JPanel();
		// update
		inner.setLayout(new GridLayout(8, 1));
		inner.add(dartMonkey);
		inner.add(tackShooter);
		inner.add(bombTower);
		inner.add(roadSpike);
		inner.add(mortarTower);
		inner.add(monkeyBuccaneer);
		inner.add(superMonkey);
		inner.add(bananaFarm);

		/*
		 * This button starts a round when pressed
		 */
		final JButton startRound = new JButton("Start Round");
		startRound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.playLevel();
			}
		});

		/*
		 * This button allows a user to cancel a tower creation action
		 */
		final JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.setMode(null);
			}
		});

		/*
		 * This button allows a user to save their game (if they have not
		 * entered a username already, they will be prompted)
		 */
		final JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.save();
			}
		});

		/*
		 * This button allows a user to load a previous game (if they have not
		 * entered a username already, they will be prompted)
		 */
		final JButton load = new JButton("Load");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.load();
			}
		});

		/*
		 * This button allows a user to reset the game state and restart a game
		 * from round 1 (their rank is unchanged, so they do not have to unlock
		 * towers again)
		 */
		final JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.reset();
			}
		});

		/*
		 * Button that shows the rules
		 */
		final JButton rules = new JButton("Rules");
		rules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"Rules of the Game \n \n The purpose of this game is to defend your home from balloons."
								+ " You can do this by placing towers around the track.\n If more than 100 balloons get through,"
								+ " you lose. After 50 rounds, if you survive, you win. \n You can see the current round, along with your"
								+ " current amount of money and lives, on the bottom screen. \n \n Balloons"
								+ " \n \n Each round involves multiple kinds of balloons. \n Red, Blue, Green, Yellow,"
								+ " and Pink balloons vary only in their health. \n But Black and Zebra balloons are"
								+ " immune to explosions, Lead Balloons are immune to sharp things, and Camo Balloons"
								+ " are invisible to most towers. \n Furthermore, some types of Balloons take many hits"
								+ " to destroy. Make sure you are prepared! \n Each tower has its own method of attacking"
								+ " the balloons.\n \n Towers \n \n DartMonkey: Unlocked at Rank 1, Fires a single dart at the leading balloon,"
								+ " and occasionally misses. \n Tack Shooter: Unlocked at Rank 2, Shoots 8 tacks but has very short range. It is"
								+ " good at corners.  \n Bomb Tower: Unlocked at Rank 4, Shoots bombs that damage multiple balloons at once. \n"
								+ " Mortar Tower: Unlocked at Rank 7, Has unlimited range and you control where it attacks. \n Monkey Buccaneer :"
								+ " Unlocked at Rank 12, Is a powerful tower with a large range that can only be placed on water. \n Super Monkey:"
								+ " Unlocked at rank 20: An extremely powerful tower that fires very quickly and is nearly unstoppable when upgraded."
								+ " \n Road Spike : Unlocked at Rank 5, Can be placed on the track to pop up to 10 balloons. Use in emergencies."
								+ " \n Banana Farm: Unlocked at Rank 25, gives extra money after each round. \n \n Controls \n \n You can place a"
								+ " tower by pressing the appropriate button and clicking the map.  To cancel this action, press cancel.\n Note"
								+ " that if you enter a previously-used nickname, your rank stays the same, so you do not have to unlock any"
								+ " towers again.\n To save your current game state at the end of any level, press save and enter a username,"
								+ " if you have not already done so. To load a previous save, enter your username. \n To upgrade a tower"
								+ " (increase the range, firing rate, etc) click on the Tower and, if you have enough money, you can buy the:"
								+ " upgrade. Each tower has between 2 and 3 upgrades.");
			}
		});
		// layout buttons
		JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(6, 1));
		controls.add(rules);
		controls.add(reset);
		controls.add(load);
		controls.add(save);
		controls.add(cancel);
		controls.add(startRound);

		control_panel.add(inner);
		control_panel.add(controls);
		// prompt the user for a username
		String name = JOptionPane.showInputDialog("If you have a saved username, enter it. Otherwise, press OK");
		if (name != null) {
			court.checkUsername(name);
		}
		// Put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());

	}

}
