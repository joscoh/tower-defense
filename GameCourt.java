import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
//import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another.
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {
	// the state of the game logic
	private Mode mode = null;
	private LevelMap map;
	// files for round and map information
	private String roundFile = "files/Rounds.txt";
	private String mapFile = "files/EasyLevel.txt";
	private Point pathStart;
	private RoundReader rr;
	private Round currentRound;
	// collections of balloons, towers, attackers, etc
	private List<Balloon> balloons = new LinkedList<Balloon>();
	private Set<Tower> towers = new HashSet<Tower>();
	private Set<Attacker> attackers = new HashSet<Attacker>();
	private Set<RoadSpike> spikes = new HashSet<RoadSpike>();
	private Set<BananaFarm> farms = new HashSet<BananaFarm>();
	private Map<String, Integer> nicknames;
	private int countForBalloonsTaken = 0;
	private int balloonsTaken = 0;
	private int numRoundsPlayed = 0;
	// previews for different towers, farms, etc
	private Tower preview = null;
	private RoadSpike previewSpike = null;
	private BananaFarm previewFarm = null;
	private Target target = null;
	private boolean inRound = false;
	// initial state of player
	private int livesRemaining = 100;
	private int money = 650;
	private String username = "";
	private int rank = 1;
	private JLabel status;

	// Game constants
	public static final int COURT_WIDTH = 600;
	public static final int COURT_HEIGHT = 600;
	public static final int TEMP_VELOCITY = 4;
	public static final int TIME_BETWEEN_BALLOONS = 20;
	public static final int BALLOON_SPEED = 2;

	// Update interval for timer, in milliseconds
	public static final int INTERVAL = 35;

	/*
	 * Constructor for GameCourt. This initializes the map, rounds, etc by
	 * reading the appropriate files
	 */
	public GameCourt(JLabel status) {
		this.map = new LevelMap(mapFile);
		this.pathStart = map.getPathStart();
		this.rr = new RoundReader(roundFile, pathStart);
		this.nicknames = NicknameReader.getNicknameMap();

		// The timer is an object which triggers an action periodically with the
		// given INTERVAL.
		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start();

		// Enable keyboard focus on the court area.
		setFocusable(true);

		/*
		 * This section contains the mouse interaction with the game
		 */
		this.addMouseMotionListener(new MouseAdapter() {
			/*
			 * If the mouse has moved and we are in a tower mode, then show a
			 * preview tower in that space (provided the space is available)
			 */
			public void mouseMoved(MouseEvent e) {
				int y = e.getY();
				int x = e.getX();
				int xSpace = (x / LevelMap.CELL_SIZE) * LevelMap.CELL_SIZE;
				int ySpace = (y / LevelMap.CELL_SIZE) * LevelMap.CELL_SIZE;
				if (xSpace >= LevelMap.MAP_SIZE * LevelMap.CELL_SIZE || xSpace < 0
						|| ySpace >= LevelMap.MAP_SIZE * LevelMap.CELL_SIZE || ySpace < 0) {
					return;
				}
				Cell c = map.getCell(xSpace / LevelMap.CELL_SIZE, ySpace / LevelMap.CELL_SIZE);
				// deal with each possible mode
				if (mode == Mode.DartMonkey && money >= 170) {
					if (c.isAvailable() && !c.isWater()) {
						preview = new DartMonkey(xSpace, ySpace, map, true, 0);
						repaint();
					}
				} else if (mode == Mode.TackShooter && money >= 305 && rank >= 2) {
					if (c.isAvailable() && !c.isWater()) {
						preview = new TackShooter(xSpace, ySpace, true, 0);
						repaint();
					}
				} else if (mode == Mode.BombTower && money >= 595 && rank >= 4) {
					if (c.isAvailable() && !c.isWater()) {
						preview = new BombTower(xSpace, ySpace, map, true, 0);
						repaint();
					}
				} else if (mode == Mode.MortarTower && money >= 700 && rank >= 7) {
					if (c.isAvailable() && !c.isWater()) {
						preview = new MortarTower(xSpace, ySpace, true, 0);
						repaint();
					}
				} else if (mode == Mode.MortarTarget) {
					MortarTower m = target.getMortar();
					target = new Target(xSpace, ySpace, m);
					repaint();
				} else if (mode == Mode.MonkeyBuccaneer && money >= 600 && rank >= 12) {
					if (c.isAvailable() && c.isWater()) {
						preview = new MonkeyBuccaneer(xSpace, ySpace, map, true, 0);
						repaint();
					}
				} else if (mode == Mode.SuperMonkey && money >= 3400 && rank >= 20) {
					if (c.isAvailable() && !c.isWater()) {
						preview = new SuperMonkey(xSpace, ySpace, map, true, 0);
						repaint();
					}
				} else if (mode == Mode.RoadSpike && money >= 30 && rank >= 5) {
					if (c.isPath()) {
						previewSpike = new RoadSpike(xSpace, ySpace);
						repaint();
					}
				} else if (mode == Mode.BananaFarm && money >= 850 && rank >= 25) {
					if (c.isAvailable() && !c.isWater()) {
						previewFarm = new BananaFarm(xSpace, ySpace, 0);
						repaint();
					}
				}
			}
		});
		/*
		 * If the mouse is pressed, then if we are pointing to a valid
		 * (available) cell and we are in a particular tower mode, then build a
		 * tower on that space. Otherwise, if there is a tower in that space,
		 * bring up the upgrade screen and show the range of the tower.
		 */
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int y = e.getY();
				int x = e.getX();
				int xSpace = (x / LevelMap.CELL_SIZE) * LevelMap.CELL_SIZE;
				int ySpace = (y / LevelMap.CELL_SIZE) * LevelMap.CELL_SIZE;
				if (xSpace >= LevelMap.MAP_SIZE * LevelMap.CELL_SIZE || xSpace < 0
						|| ySpace >= LevelMap.MAP_SIZE * LevelMap.CELL_SIZE || ySpace < 0) {
					return;
				}
				Cell c = map.getCell(xSpace / LevelMap.CELL_SIZE, ySpace / LevelMap.CELL_SIZE);
				// deal with each possible tower mode
				if (mode != null) {
					if (mode == Mode.DartMonkey && money >= 170) {
						if (c.isAvailable() && !c.isWater()) {
							money -= 170;
							Tower toAdd = new DartMonkey(xSpace, ySpace, map, false, 0);
							towers.add(toAdd);
						}
					} else if (mode == Mode.TackShooter && money >= 305 && rank >= 2) {
						if (c.isAvailable() && !c.isWater()) {
							money -= 305;
							Tower toAdd = new TackShooter(xSpace, ySpace, false, 0);
							towers.add(toAdd);
						}
					} else if (mode == Mode.BombTower && money >= 595 && rank >= 4) {
						if (c.isAvailable() && !c.isWater()) {
							money -= 595;
							Tower toAdd = new BombTower(xSpace, ySpace, map, false, 0);
							towers.add(toAdd);
						}
					} else if (mode == Mode.MortarTower && money >= 700 && rank >= 7) {
						if (c.isAvailable() && !c.isWater()) {
							money -= 700;
							Tower toAdd = new MortarTower(xSpace, ySpace, false, 0);
							towers.add(toAdd);
						}
					} else if (mode == Mode.MortarTarget) {
						MortarTower m = target.getMortar();
						m.setTarget(xSpace, ySpace);
						// do normal upgrades for mortar only after putting down
						// target
						int level = m.getUpgradeLevel();
						if (level < 3) {
							int answer = JOptionPane.showConfirmDialog(null,
									"Click Yes to upgrade your Tower \n Upgrade :" + MortarTower.levelNames[level]
											+ "\n Price: " + MortarTower.moneyForLevel[level]);
							if (money >= MortarTower.moneyForLevel[level] && answer == 0) {
								money -= MortarTower.moneyForLevel[level];
								m.upgradeTower(level + 1);
							}
						}
						repaint();
						mode = null;
					} else if (mode == Mode.MonkeyBuccaneer && money >= 600 && rank >= 12) {
						if (c.isAvailable() && c.isWater()) {
							money -= 600;
							Tower toAdd = new MonkeyBuccaneer(xSpace, ySpace, map, false, 0);
							towers.add(toAdd);
						}
					} else if (mode == Mode.SuperMonkey && money >= 3400 && rank >= 20) {
						if (c.isAvailable() && !c.isWater()) {
							money -= 3400;
							Tower toAdd = new SuperMonkey(xSpace, ySpace, map, false, 0);
							towers.add(toAdd);
						}
					} else if (mode == Mode.RoadSpike && money >= 30 && rank >= 5) {
						if (c.isPath()) {
							money -= 30;
							RoadSpike toAdd = new RoadSpike(xSpace, ySpace);
							spikes.add(toAdd);
						}
					} else if (mode == Mode.BananaFarm && money >= 850 && rank >= 25) {
						if (c.isAvailable() && !c.isWater()) {
							money -= 850;
							BananaFarm toAdd = new BananaFarm(xSpace, ySpace, 0);
							farms.add(toAdd);
						}
					}
					c.setAvailable(false);
					mode = null;
					preview = null;
					previewFarm = null;
					previewSpike = null;
					repaint();
					status.setText("Round: " + numRoundsPlayed + " Lives: " + livesRemaining + " Money: " + money
							+ " Rank: " + rank);
				}
				// otherwise, see if there is a tower on that space, and if so,
				// show the upgrade screen and the tower range
				else {
					if (!c.isAvailable() && !c.isPath()) {
						for (Tower t : towers) {
							if ((t.getX() / LevelMap.CELL_SIZE) * LevelMap.CELL_SIZE == xSpace
									&& ySpace == (t.getY() / LevelMap.CELL_SIZE) * LevelMap.CELL_SIZE) {
								if (t instanceof DartMonkey) {
									int level = t.getUpgradeLevel();
									if (level < 2) {
										int answer = JOptionPane.showConfirmDialog(null,
												"Click Yes to upgrade your Tower \n Upgrade :"
														+ DartMonkey.levelNames[level] + "\n Price: "
														+ DartMonkey.moneyForLevel[level]);
										if (money >= DartMonkey.moneyForLevel[level] && answer == 0) {
											money -= DartMonkey.moneyForLevel[level];
											t.upgradeTower(level + 1);
											status.setText("Round: " + numRoundsPlayed + " Lives: " + livesRemaining
													+ " Money: " + money + " Rank: " + rank);
										}
									}
									preview = new DartMonkey(t.getX(), t.getY(), map, true, t.getUpgradeLevel());
								} else if (t instanceof TackShooter) {
									int level = t.getUpgradeLevel();
									if (level < 2) {
										int answer = JOptionPane.showConfirmDialog(null,
												"Click Yes to upgrade your Tower \n Upgrade :"
														+ TackShooter.levelNames[level] + "\n Price: "
														+ TackShooter.moneyForLevel[level]);
										if (money >= TackShooter.moneyForLevel[level] && answer == 0) {
											money -= TackShooter.moneyForLevel[level];
											t.upgradeTower(level + 1);
											status.setText("Round: " + numRoundsPlayed + " Lives: " + livesRemaining
													+ " Money: " + money + " Rank: " + rank);
										}
									}
									preview = new TackShooter(t.getX(), t.getY(), true, t.getUpgradeLevel());
								} else if (t instanceof BombTower) {
									int level = t.getUpgradeLevel();
									if (level < 3) {
										int answer = JOptionPane.showConfirmDialog(null,
												"Click Yes to upgrade your Tower \n Upgrade :"
														+ BombTower.levelNames[level] + "\n Price: "
														+ BombTower.moneyForLevel[level]);
										if (money >= BombTower.moneyForLevel[level] && answer == 0) {
											money -= BombTower.moneyForLevel[level];
											t.upgradeTower(level + 1);
											status.setText("Round: " + numRoundsPlayed + " Lives: " + livesRemaining
													+ " Money: " + money + " Rank: " + rank);
										}
									}
									preview = new BombTower(t.getX(), t.getY(), map, true, t.getUpgradeLevel());
								} else if (t instanceof MortarTower) {
									target = new Target(t.getX(), t.getY(), (MortarTower) t);
									mode = Mode.MortarTarget;
								} else if (t instanceof MonkeyBuccaneer) {
									int level = t.getUpgradeLevel();
									if (level < 3) {
										int answer = JOptionPane.showConfirmDialog(null,
												"Click Yes to upgrade your Tower \n Upgrade :"
														+ MonkeyBuccaneer.levelNames[level] + "\n Price: "
														+ MonkeyBuccaneer.moneyForLevel[level]);
										if (money >= MonkeyBuccaneer.moneyForLevel[level] && answer == 0) {
											money -= MonkeyBuccaneer.moneyForLevel[level];
											t.upgradeTower(level + 1);
											status.setText("Round: " + numRoundsPlayed + " Lives: " + livesRemaining
													+ " Money: " + money + " Rank: " + rank);
										}
									}
									preview = new MonkeyBuccaneer(t.getX(), t.getY(), map, true, t.getUpgradeLevel());
								} else if (t instanceof SuperMonkey) {
									int level = t.getUpgradeLevel();
									if (level < 3) {
										int answer = JOptionPane.showConfirmDialog(null,
												"Click Yes to upgrade your Tower \n Upgrade :"
														+ SuperMonkey.levelNames[level] + "\n Price: "
														+ SuperMonkey.moneyForLevel[level]);
										if (money >= SuperMonkey.moneyForLevel[level] && answer == 0) {
											money -= SuperMonkey.moneyForLevel[level];
											t.upgradeTower(level + 1);
											status.setText("Round: " + numRoundsPlayed + " Lives: " + livesRemaining
													+ " Money: " + money + " Rank: " + rank);
										}
									}
									preview = new SuperMonkey(t.getX(), t.getY(), map, true, t.getUpgradeLevel());
								}
							}
						}
						for (BananaFarm bf : farms) {
							if (bf.getX() / LevelMap.CELL_SIZE == xSpace && ySpace == bf.getY() / LevelMap.CELL_SIZE) {
								int level = bf.getLevel();
								if (level < 3) {
									int answer = JOptionPane.showConfirmDialog(null,
											"Click Yes to upgrade your Tower \n Upgrade: "
													+ BananaFarm.levelNames[level] + "\n Price: "
													+ BananaFarm.moneyForLevel[level]);
									if (money >= BananaFarm.moneyForLevel[level] && answer == 0) {
										money -= BananaFarm.moneyForLevel[level];
										bf.upgradeTower(level + 1);
										status.setText("Round: " + numRoundsPlayed + " Lives: " + livesRemaining
												+ " Money: " + money + " Rank: " + rank);
									}
								}
								previewFarm = null;
							}

						}
						repaint();
					} else {
						preview = null;
						previewSpike = null;
						previewFarm = null;
						target = null;
						repaint();
					}
				}
			}

		});
		this.status = status;
		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

	public void playLevel() {
		if (inRound) {
			return;
		}
		if (numRoundsPlayed >= 50) {
			return;
		}
		currentRound = rr.getRound(numRoundsPlayed);
		numRoundsPlayed++;
		inRound = true;
		// firstTime = true;
		status.setText(
				"Round: " + numRoundsPlayed + " Lives: " + livesRemaining + " Money: " + money + " Rank: " + rank);

	}

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers. It only does anything when we are in a round (otherwise, there
	 * is no state to update)
	 */
	void tick() {
		if (inRound) {
			// check to see if player lost
			if (livesRemaining == 0) {
				JOptionPane.showMessageDialog(null, "You Lost");
				inRound = false;
			}
			// iterate through attackers
			Iterator<Attacker> itrA = attackers.iterator();
			while (itrA.hasNext()) {
				Boolean hasRemoved = false;
				Attacker curr = itrA.next();
				// check to see if attacker has exceeded its lifespan or the
				// game court borders
				if (curr.timeToDie() || curr.getX() > LevelMap.CELL_SIZE * LevelMap.MAP_SIZE || curr.getX() < 0
						|| curr.getY() > LevelMap.CELL_SIZE * LevelMap.MAP_SIZE || curr.getY() < 0) {
					itrA.remove();
					continue;
				}
				curr.incrTimeAlive();
				// handle collisions for directed Attackers that have hit their
				// target
				if (curr.isDirected() && curr.hasCollided()) {
					// if bomb, see if any other balloons in ramge
					if (curr.getRange() > 0) {
						for (Balloon b : balloons) {
							boolean inRange = Attacker.inRange(curr, b);
							if (inRange && !b.isExplodeResistant()) {
								b.damage();
								money++;
							}
						}
					} else {
						if ((curr.isExplosive() && !curr.isSharp() && !curr.getTarget().isExplodeResistant())
								|| (curr.isSharp() && !curr.isExplosive() && !curr.getTarget().isSharpResistant())
								|| (curr.isExplosive() && curr.isSharp())) {
							curr.getTarget().damage();
							money++;
						}

					}
					if (!hasRemoved) {
						itrA.remove();
					}
					repaint();
					continue;
				}
				// handle undirected collisions by iterating through all
				// balloons and checking for collisions
				for (Balloon b : balloons) {
					if (curr.getRange() > 0 && !curr.isDirected()) {
						// deal with mortar shells
						boolean inRange = Attacker.inRange(curr, b);
						if (inRange && !b.isExplodeResistant()) {
							b.damage();
							if (curr instanceof MortarShell) {
								MortarShell mCurr = (MortarShell) curr;
								if (mCurr.isTwoLayer() && !b.isExplodeResistant()) {
									b.damage();
								}
							}
							money++;
							repaint();
						}
					}
					/*
					 * Deal with all dart projectiles, note that directed darts
					 * can hit a balloon other than their target along the way
					 */
					else if (curr.getRange() == 0) {
						boolean inRange = Attacker.hasUndirectedCollided(curr, b);
						boolean canDamage = true;
						;
						if (curr.isExplosive() && !curr.isSharp()) {
							canDamage = !b.isExplodeResistant();
						} else if (curr.isSharp() && !curr.isExplosive()) {
							canDamage = !b.isSharpResistant();
						}
						if (inRange && canDamage) {
							b.damage();
							money++;
							itrA.remove();
							hasRemoved = true;
							break;
						}
					}
				}
				curr.move();
				repaint();
			}
			/*
			 * Iterate through towers. If they are eligible to fire, check to
			 * see if any balloons are in range, and if so, call their attack
			 * method
			 */
			Iterator<Tower> itrT = towers.iterator();
			while (itrT.hasNext()) {
				Tower curr = itrT.next();
				// check to see if tower has reloaded yet
				if (curr.getCtr() % curr.getFireRate() != 0) {
					curr.itr();
					continue;
				}
				curr.itr();
				// deal with mortars (the only tower with 0 range)
				if (curr.getRange() == 0) {
					attackers.addAll(curr.attack(null));
					repaint();
				}
				// iterate through balloons until we find one in range
				for (Balloon b : balloons) {
					if (!curr.canDetectCamo() && b.isCamo()) {
						continue;
					}
					int bX = b.getX();
					int bY = b.getY();
					int x = curr.getX();
					int y = curr.getY();
					int range = curr.getRange();
					if (bX >= x - (range * LevelMap.CELL_SIZE) && bX <= x + ((range + 1) * LevelMap.CELL_SIZE)
							&& bY >= y - (range * LevelMap.CELL_SIZE) && bY <= y + ((range + 1) * LevelMap.CELL_SIZE)) {
						attackers.addAll(curr.attack(b));
						break;
					}
				}
			}
			/*
			 * Iterate through balloons, moving them, updating their color, and
			 * removing them if they die or if they move off the edge of the
			 * screen
			 */
			ListIterator<Balloon> itr = balloons.listIterator();
			while (itr.hasNext()) {
				Balloon b = itr.next();
				int pathIdx = b.getPathIndex(map);
				// check to see if balloon has reached the end of the board
				if (pathIdx == map.getPathSize() - 2) {
					int health = b.getHealth();
					livesRemaining = livesRemaining - health;
					status.setText("Round: " + numRoundsPlayed + " Lives: " + livesRemaining + " Money: " + money
							+ " Rank: " + rank);
					itr.remove();
					continue;
				}
				// move balloon
				Direction d = map.getPathElement(pathIdx).getDirection();
				b.setDirection(d);
				b.move();
				status.setText("Round: " + numRoundsPlayed + " Lives: " + livesRemaining + " Money: " + money
						+ " Rank: " + rank);
				// deal with updates for balloon (check to see if color changes,
				// and if so, change it)
				Balloon additional = b.update(map);
				if (b.getHealth() <= 0) {
					itr.remove();
				}
				if (additional != null) {
					itr.add(additional);
				}
				repaint();
			}
			// iterate through road spikes
			Iterator<RoadSpike> itrS = spikes.iterator();
			while (itrS.hasNext()) {
				RoadSpike curr = itrS.next();
				// for each balloon, if the balloon is on the same cell as the
				// spike, subtract the balloon's health by 10
				for (Balloon b : balloons) {
					if (curr.getNumBalloonsLeft() == 0) {
						itrS.remove();
						break;
					}
					double maxX = Math.max(b.getX(), b.getX() + Balloon.WIDTH);
					double minX = Math.min(b.getX(), b.getX() + Balloon.WIDTH);
					double maxY = Math.max(b.getY(), b.getY() + Balloon.HEIGHT);
					double minY = Math.min(b.getY(), b.getY() + Balloon.HEIGHT);
					int r = LevelMap.CELL_SIZE;

					boolean inRange = ((minY >= curr.getY() + r && minY <= curr.getY())
							|| (maxY >= curr.getY() && maxY <= curr.getY() + r))
							&& ((minX >= curr.getX() && minX <= curr.getX() + r)
									|| (maxX >= curr.getX() && maxX <= curr.getX() + r));
					if (inRange) {
						while (b.getHealth() > 0 && curr.getNumBalloonsLeft() > 0) {
							b.damage();
							curr.popBalloon();
						}
					}
				}
			}
			// add balloons at regular intervals
			if (countForBalloonsTaken % TIME_BETWEEN_BALLOONS == 0 && balloonsTaken < currentRound.getNumBalloons()) {
				Balloon b = currentRound.getNextBalloon();
				b.setDirection(map.getFirstDirection());
				balloons.add(b);
				balloonsTaken++;
			}
			countForBalloonsTaken++;
			// check if round is over, and if so reset state
			if (!currentRound.hasMoreBalloons() && balloons.size() == 0) {
				// check again to see if game has ended
				if (livesRemaining == 0) {
					balloons.clear();
					JOptionPane.showMessageDialog(null, "You Lost");
					inRound = false;
				}
				// if the player has completed 50 rounds, they win
				if (numRoundsPlayed == 50) {
					JOptionPane.showMessageDialog(null, "You Won!");
				}
				// reset state of attackers, spikes, balloons, and update money
				// and rank
				attackers.clear();
				spikes.clear();
				balloonsTaken = 0;
				countForBalloonsTaken = 0;
				inRound = false;
				money += (int) (100 * Math.pow(1.05, numRoundsPlayed));
				for (BananaFarm bf : farms) {
					int toAdd = bf.getNumBananas();
					money += toAdd;
				}
				if (rank == numRoundsPlayed) {
					incrRank();
				}
				status.setText("Round: " + numRoundsPlayed + " Lives: " + livesRemaining + " Money: " + money
						+ " Rank: " + rank);
			}
			repaint();
		}
	}

	/*
	 * Deals with painting of various components
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		map.drawMap(g);
		for (Balloon balloon : balloons) {
			if (balloon != null) {
				balloon.draw(g);
			}
		}
		for (Tower tower : towers) {
			tower.draw(g);
		}
		for (Attacker attacker : attackers) {
			attacker.draw(g);
		}
		for (RoadSpike rs : spikes) {
			rs.draw(g);
		}
		for (BananaFarm bf : farms) {
			bf.draw(g);
		}
		if (preview != null) {
			preview.draw(g);
		}
		if (target != null) {
			target.draw(g);
		}
		if (previewSpike != null) {
			previewSpike.draw(g);
		}
		if (previewFarm != null) {
			previewFarm.draw(g);
		}

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}

	/*
	 * Sets the mode (called by Buttons from the Game class)
	 */
	public void setMode(Mode m) {
		preview = null;
		repaint();
		this.mode = m;
	}

	/*
	 * Given a string, see if nickname previously saved. If it was, update the
	 * rank. Otherwise, add it to the collection of nicknames and write the list
	 * of nicknames to a file.
	 */
	public void checkUsername(String name) {
		if (name == null || name.equals("")) {
			username = "";
		} else {
			username = name;
			Integer rank = nicknames.get(name);
			if (rank != null) {
				this.rank = rank;
			} else {
				nicknames.put(name, this.rank);
				NicknameWriter.writeNicknames(nicknames);
			}
		}
		status.setText(
				"Round: " + numRoundsPlayed + " Lives: " + livesRemaining + " Money: " + money + " Rank: " + this.rank);
	}

	/*
	 * Save the current game state to a file (can only be done between levels)
	 */
	public void save() {
		if (inRound) {
			return;
		}
		if (username == null || username.equals("")) {
			String username = JOptionPane.showInputDialog("Enter a username and press OK");
			if (username == null || username.equals("")) {
				return;
			}
			this.username = username;
		}
		nicknames.put(username, this.rank);
		NicknameWriter.writeNicknames(nicknames);
		SaveWriter.writeToFile(username, towers, farms, numRoundsPlayed, money, livesRemaining);
	}

	/*
	 * Load a game if a saved username is entered, only works in between rounds
	 */
	public void load() {
		if (inRound) {
			return;
		}
		if (username == null || username.equals("")) {
			username = JOptionPane.showInputDialog("Enter a username and press OK");
		}
		// need to check twice in case user did not enter a username in the
		// dialog
		if (username == null || username.equals("")) {
			return;
		}
		if (!SaveReader.isUsernameValid(username)) {
			username = JOptionPane.showInputDialog("Enter a username and press OK");
		}
		// have to check again because user may have entered valid username
		if (SaveReader.isUsernameValid(username)) {
			SaveReader sr = new SaveReader(username, map);
			numRoundsPlayed = sr.getCurrRound();
			money = sr.getMoney();
			towers = sr.getTowers();
			for (Tower t : towers) {
				int x = t.getX();
				int y = t.getY();
				int xCell = x / LevelMap.CELL_SIZE;
				int yCell = y / LevelMap.CELL_SIZE;
				Cell c = map.getCell(xCell, yCell);
				c.setAvailable(false);
			}
			livesRemaining = sr.getLives();
			Integer rank = nicknames.get(username);
			if (rank != null) {
				this.rank = rank;
			}
			farms = sr.getFarms();
			for (BananaFarm b : farms) {
				int x = b.getX();
				int y = b.getY();
				int xCell = x / LevelMap.CELL_SIZE;
				int yCell = y / LevelMap.CELL_SIZE;
				Cell c = map.getCell(xCell, yCell);
				c.setAvailable(false);
			}
			repaint();
			status.setText(
					"Round: " + numRoundsPlayed + " Lives: " + livesRemaining + " Money: " + money + " Rank: " + rank);
		}
	}

	/*
	 * Private helper method to increase the rank. Note that it also writes the
	 * list of usernames and ranks to a file. This is necessary since we don't
	 * know when the user will close the program.
	 */
	private void incrRank() {
		rank++;
		if (!username.equals("")) {
			nicknames.put(username, rank);
			NicknameWriter.writeNicknames(nicknames);
		}
		status.setText(
				"Round: " + numRoundsPlayed + " Lives: " + livesRemaining + " Money: " + money + " Rank: " + rank);
	}

	/*
	 * Resets game state (other than rank), allows user to start over
	 */
	public void reset() {
		if (inRound) {
			return;
		}
		this.map = new LevelMap(mapFile);
		this.pathStart = map.getPathStart();
		this.rr = new RoundReader(roundFile, pathStart);
		countForBalloonsTaken = 0;
		balloonsTaken = 0;
		numRoundsPlayed = 0;
		preview = null;
		inRound = false;
		livesRemaining = 100;
		money = 650;
		towers.clear();
		attackers.clear();
		balloons.clear();
		farms.clear();
		repaint();
		status.setText(
				"Round: " + numRoundsPlayed + " Lives: " + livesRemaining + " Money: " + money + " Rank: " + rank);
	}
}
