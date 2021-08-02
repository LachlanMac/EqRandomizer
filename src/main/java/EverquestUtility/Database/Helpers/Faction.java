package EverquestUtility.Database.Helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import EverquestUtility.Util.ConfigReader;

public class Faction implements Comparable {
	private int id;
	private String name;
	private ArrayList<Integer> levels;
	private int highest, lowest, average;
	List<NPCFaction> subFactions;
	protected ArrayList<Mob> mobs;
	Random rn;
	private int tempMobsInZone;
	protected ArrayList<Mob> namedMobs;
	protected ArrayList<Mob> normalMobs;

	public Faction(int id, String name) {
		this.id = id;
		this.name = name;
		levels = new ArrayList<Integer>();
		mobs = new ArrayList<Mob>();

		rn = new Random(System.currentTimeMillis() + this.hashCode());
		namedMobs = new ArrayList<Mob>();
		normalMobs = new ArrayList<Mob>();
	}

	public ArrayList<Mob> getNamedMobs() {
		return namedMobs;
	}

	public void setNamedMobs(ArrayList<Mob> namedMobs) {
		this.namedMobs = namedMobs;
	}

	public ArrayList<Mob> getNormalMobs() {
		return normalMobs;
	}

	public void setNormalMobs(ArrayList<Mob> normalMobs) {
		this.normalMobs = normalMobs;
	}

	public void setAverageLevel() {

		int total = 0;
		for (Mob m : mobs) {
			total += m.getLevel();
		}
		if (total == 0) {
			System.out.println("ERROR");
			System.exit(0);
		}
		average = total / mobs.size();
	}

	public void AddLevel(int level) {

		for (Integer i : levels) {
			if (i.intValue() == level) {
				return;
			}
		}
		levels.add(level);

	}

	public static boolean CompareFactions2(Faction a, Faction b) {
		return true;
	}

	public static boolean CompareFactions(Faction a, Faction b) {

		int passCounter = 0;
		int failCounter = 0;

		for (int facALevel : a.getLevels()) {

			boolean match = false;

			for (int facBLevel : b.getLevels()) {

				if (Math.abs(facBLevel - facALevel) <= 5) {
					match = true;
				}
			}

			if (match) {
				passCounter++;
			} else {
				failCounter++;
			}

		}

		float totalCounter = passCounter + failCounter;

		if (((float) failCounter / totalCounter) <= .20f) {
			System.out.println("FAILURE " + a.getLevels().size() + " vs " + b.getLevels().size());
			return false;
		}
		// System.out.println("SUCCESS " + a.getLevels().size() + "," +
		// b.getLevels().size() + a.getName() + " vs " + b.getName());
		return true;

	}

	public static void FactionReport() {

	}

	public void addMob(Mob m) {
		mobs.add(m);
	}

	public ArrayList<Mob> getMobs() {

		return mobs;
	}

	public void setMobs(ArrayList<Mob> mobs) {
		this.mobs = mobs;
	}

	public ArrayList<Integer> getLevels() {
		return levels;
	}

	public void setLevels(ArrayList<Integer> levels) {
		this.levels = levels;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<NPCFaction> getSubFactions() {
		return subFactions;
	}

	public void setSubFactions(List<NPCFaction> subFactions) {
		this.subFactions = subFactions;
	}

	@Override
	public int compareTo(Object faction) {

		Faction f = (Faction) faction;
		if (this.mobs.size() < f.mobs.size()) {
			return 1;
		} else if (this.mobs.size() == f.mobs.size()) {
			return 0;
		} else {

			return -1;
		}

	}

	public void updateFaction() {
		setHighest();
		setLowest();
		setAverageLevel();

	}

	public int getHighest() {
		return highest;
	}

	public void setHighest() {
		int high = 1;
		for (Mob m : mobs) {
			if (m.getLevel() > high)
				high = m.getLevel();
		}

		this.highest = high;
	}

	public int getLowest() {
		return lowest;
	}

	public void setLowest() {
		int low = 99;
		for (Mob m : mobs) {
			if (m.getLevel() < low)
				low = m.getLevel();
		}

		this.lowest = low;
	}

	public Mob GetMobNearLevel(int level) {
		Mob m = null;
		int min = level - 3;
		int max = level + 3;
		ArrayList<Mob> potential = new ArrayList<Mob>();
		int counter = 0;
		while (potential.size() == 0) {
			if (counter > 100) {
				return null;
			}
			counter++;
			for (Mob mo : mobs) {
				if (mo.getLevel() >= min && mo.getLevel() <= max) {
					potential.add(mo);
				}
			}
			min--;
			max++;

		}

		return potential.get(rn.nextInt(potential.size()));

	}

	public void reset() {
		tempMobsInZone = 0;
	}

	public Random getRn() {
		return rn;
	}

	public void setRn(Random rn) {
		this.rn = rn;
	}

	public int getTempMobsInZone() {
		return tempMobsInZone;
	}

	public void setTempMobsInZone(int tempMobsInZone) {
		this.tempMobsInZone = tempMobsInZone;
	}

	public void setHighest(int highest) {
		this.highest = highest;
	}

	public void setLowest(int lowest) {
		this.lowest = lowest;
	}

	public int getAverage() {
		return average;
	}

	public void setAverage(int average) {
		this.average = average;
	}

	public boolean IsReplaceableNoobFaction() {

		for (int i = 0; i < ConfigReader.NOOB_REPLACE_FACTION_IDS.length; i++) {
			System.out.print(ConfigReader.NOOB_REPLACE_FACTION_IDS[i] + ", ");
			if (ConfigReader.NOOB_REPLACE_FACTION_IDS[i] == this.getId()) {

				return true;
			}
		}
		return false;
	}

}
