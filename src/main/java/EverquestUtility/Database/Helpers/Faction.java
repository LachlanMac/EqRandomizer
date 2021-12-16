package EverquestUtility.Database.Helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import EverquestUtility.FactionBuilder.FactionBuilder;
import EverquestUtility.FactionBuilder.FactionBuilder.RACE;
import EverquestUtility.Util.ConfigReader;

public class Faction implements Comparable {
	protected int id;
	private String name;
	private ArrayList<Integer> levels;
	private int highest, lowest, average;
	List<NPCFaction> subFactions;
	protected ArrayList<Mob> mobs;

	boolean destroyFaction = false;

	Random rn;
	private int tempMobsInZone;
	protected ArrayList<Mob> namedMobs;
	protected ArrayList<Mob> normalMobs;
	private ArrayList<Integer> raceIds;

	public Faction(int id, String name) {
		this.id = id;
		this.name = name;
		levels = new ArrayList<Integer>();
		mobs = new ArrayList<Mob>();
		rn = new Random();
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

	public Mob getNamedMobNearLevel(int level) {
		Random rn = new Random();
		Mob m = null;
		ArrayList<Mob> closeMobs = new ArrayList<Mob>();
		Collections.shuffle(namedMobs);
		for (Mob mobs : namedMobs) {
			if (mobs.getLevel() >= (level - 1) && mobs.getLevel() <= level + 7) {
				closeMobs.add(mobs);
			}
		}

		if (closeMobs.size() == 0) {
			return null;
		}

		return closeMobs.get(rn.nextInt(closeMobs.size()));
	}

	public void setAverageLevel() {

		int total = 0;
		for (Mob m : mobs) {

			total += m.getLevel();
		}
		if (total == 0) {
			destroyFaction = true;
			return;
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

	public void setUnique() {

		for (Mob m : mobs) {
			// name check...
			String name = m.name.toLowerCase();
			String subName = "";
			try {
				subName = name.substring(0, 4);
			} catch (IndexOutOfBoundsException e) {
				continue;
			}

			if (m.getId() == 500) {
				continue;
			}

			if (name.contains("fabled")) {
				namedMobs.add(m);
				continue;
			}
			if (subName.contains("a_") || subName.contains("an_")) {
				normalMobs.add(m);
				continue;
			}

			if (name.contains("#")) {
				if (subName.contains("#a_") || subName.contains("#an_")) {
					normalMobs.add(m);
					continue;
				} else {
					namedMobs.add(m);
					continue;
				}
			}

			if (m.spawn_limit == 1) {
				namedMobs.add(m);
				continue;
			}
			boolean probablyNamed = true;
			for (String s : ConfigReader.NORMAL_MOB_INDICATORS) {

				if (name.contains("sorceror_")) {
					// this word has orc in it...
					break;
				}

				if (name.contains(s)) {
					normalMobs.add(m);
					probablyNamed = false;
					break;
				}

			}
			if (probablyNamed)
				namedMobs.add(m);
			else {
				normalMobs.add(m);
			}
			// check for other normal shit

		}

		for (Mob m : namedMobs) {
			m.unique_spawn_by_name = 1;
			m.unique_ = 1;
			m.spawn_limit = 1;
			convertToNamedMonster(m);

		}

	}

	public boolean DestroyFaction() {
		return destroyFaction;
	}

	public Mob convertToNamedMonster(Mob m) {

		m.factionID = id;

		if (m.isNecro()) {
			m.name = "A_Necromancer_Lord";
			return m;
		}
		if (m.isMage()) {
			m.name = "A_Mage_Lord";
			return m;
		}

		if (m.name.toLowerCase().contains("fabled")) {
			m.setName("A Fabled " + getMonsterName(getRaceByID(m.race), m));
		}

		for (Integer i : ConfigReader.ANIMAL_RACES) {
			if (m.race == i.intValue()) {
				m.name = getMonsterName(getRaceByID(m.race), m) + " Alpha";
				return m;
			}
		}
		for (Integer i : ConfigReader.VERMIN_RACES) {
			if (m.race == i.intValue()) {
				m.name = getMonsterName(getRaceByID(m.race), m) + " Packleader";
				return m;
			}

		}
		for (Integer i : ConfigReader.ANIMAL_MONSTER_RACES) {
			if (m.race == i.intValue()) {
				m.name = getMonsterName(getRaceByID(m.race), m) + " Chief";
				return m;
			}

		}
		for (Integer i : ConfigReader.WHORE_RACES) {
			if (m.race == i.intValue()) {
				m.name = getMonsterName(getRaceByID(m.race), m) + " Seductress";
				return m;
			}

		}
		for (Integer i : ConfigReader.DOG_PEOPLE_RACES) {
			if (m.race == i.intValue()) {
				m.name = getMonsterName(getRaceByID(m.race), m) + " Direlord";
				return m;
			}

		}
		for (Integer i : ConfigReader.UNDEAD_RACES) {
			if (m.race == i.intValue()) {
				m.name = getMonsterName(getRaceByID(m.race), m) + " Deathlord";
				return m;
			}

		}
		for (Integer i : ConfigReader.BUG_RACES) {
			if (m.race == i.intValue()) {
				m.name = getMonsterName(getRaceByID(m.race), m) + " Hivemaster";
				return m;
			}
		}

		for (Integer i : ConfigReader.HUMANOID_RACES) {
			if (m.race == i.intValue()) {
				m.name = getMonsterName(getRaceByID(m.race), m) + " Lord";
				return m;
			}
		}

		try {
			m.name = getMonsterName(getRaceByID(m.race), m) + " Leader";
		} catch (Exception e) {
			System.out.println("COUDNT FIND " + m.race);
			m.name = "Wanderer";
		}

		return m;

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

	public String GetMageName(Mob m) {

		if (m.level <= 7) {
			return "A_Magician_Apprentice";
		} else if (m.level > 7 && m.level <= 20) {
			return "A_Magician_Scholar";
		} else if (m.level > 20 && m.level <= 35) {
			return "A_Magician_Novice";
		} else if (m.level > 35 && m.level <= 50) {
			return "A_Madened_Magician";
		} else {
			return "A_Legendary_Magician";
		}

	}

	public String GetSKName(Mob m) {

		if (m.level <= 7) {
			return "A_Dark_Initiate";
		} else if (m.level > 7 && m.level <= 20) {
			return "A_Dark_Knight";
		} else if (m.level > 20 && m.level <= 35) {
			return "A_Shadowknight";
		} else if (m.level > 35 && m.level <= 50) {
			return "A_Dreadlord";
		} else {
			return "A_Death_Commander";
		}

	}

	public String GetNecroName(Mob m) {

		if (m.level <= 7) {
			return "A_Necromancer_Initiate";
		} else if (m.level > 7 && m.level <= 20) {
			return "A_Novice_Necromancer";
		} else if (m.level > 20 && m.level <= 35) {
			return "A_Necromancer";
		} else if (m.level > 35 && m.level <= 50) {
			return "A_Dreadmancer";
		} else {
			return "A_Legendary_Lichlord";
		}

	}

	public void SetNames() {
		for (Mob m : normalMobs) {
			if (m.isMage()) {
				m.name = GetMageName(m);
				continue;
			} else if (m.isNecro()) {
				m.name = GetNecroName(m);
				continue;
			} else if (m.isSK()) {
				m.name = GetSKName(m);
				continue;
			}
			RACE r = getRaceByID(m.race);
			if (r == null) {
				m.name = "Unanmed";
			} else if (m.isMage()) {
				m.name = GetMageName(m);

			} else if (m.isNecro()) {
				m.name = GetNecroName(m);
			} else {
				m.name = getMonsterNamePrefix(m, r) + getMonsterName(r, m)
						+ getMonsterNameSuffix(m.level, r, m.Class, m.race);
			}
		}
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
			if (ConfigReader.NOOB_REPLACE_FACTION_IDS[i] == this.getId()) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Mob> GetBalancedNamed(int count, int minLevel, int maxLevel) {

		ArrayList<Mob> namedTempList = new ArrayList<Mob>();
		ArrayList<Mob> allNamedMobsBetween = new ArrayList<Mob>();
		Collections.shuffle(namedMobs);

		int counter = 0;
		for (Mob m : namedMobs) {
			if (counter >= count) {
				return namedTempList;
			}

			if (m.level >= minLevel && m.level <= maxLevel) {
				namedTempList.add(m);
				counter++;
			}

		}

		return namedTempList;

	}

	public Mob GetBalancedNamedMob(int minLevel, int maxLevel) {
		Random rn = new Random();
		Collections.shuffle(namedMobs);
		ArrayList<Mob> namedTempList = new ArrayList<Mob>();
		for (Mob m : namedMobs) {
			if (m.level >= minLevel && m.level <= maxLevel) {
				namedTempList.add(m);
			}
		}

		if (namedTempList.size() != 0) {
			return namedTempList.get(rn.nextInt(namedTempList.size()));
		}
		return null;
	}

	public static String[] MONSTER_NAMES_MAIN = new String[] { "A Drolvarg ", "A Gnoll ", "An Orc ", "A Goblin ",
			"A Goblin ", "A Kobold ", "A Kobold ", "An Othmir ", "A Grimling ", "A Froglok ", "A Minotaur",
			" An Evil Eye ", "A Burynai", " Netherbian", "A Galorian", "A Walrus ", "A Ratman ", "A Sarnak ",
			"A Lizardman ", "An Aviak ", "A Tegi " };

	public String getMonsterNameSuffix(int level, RACE r, int Class, int raceID) {
		String suffix = "";

		switch (r) {
		case ANIMAL:
			break;
		case BUG:
			break;
		case FEY:
			break;
		case GIANT:
			break;
		case GOLEM:
		case MONSTER:
			switch (raceID) {
			case 39: // gnoll
			case 54: // orc
			case 133: // drovlarg
			case 40: // goblin
			case 137: // alos goblin
			case 48: // kobold
			case 455: // kobld
			case 190: // othmir
			case 202: // GRIMLINg
			case 27: // FROGLOK
			case 144: // Burynai
			case 229: // NETHERBIAN
			case 228: // GALORIAN
			case 191: // WALRUSMAN
			case 156: // RATMAN
			case 131: // SARNAK
			case 51: // LIZARD
			case 13: // AVIAK
			case 215: // TEGI
				if (Class == 3 || Class == 5 || Class == 4) {
					if (level <= 7) {
						suffix = "_Pawn";
					} else if (level > 7 && level <= 20) {
						suffix = "_Minion";
					} else if (level > 20 && level <= 35) {
						suffix = "_Knight";
					} else if (level > 35 && level <= 50) {
						suffix = "_Noble";
					} else {
						suffix = "_Commander";
					}

				} else if (Class == 2 || Class == 6 || Class == 10 || Class == 15) {
					if (level <= 7) {
						suffix = "_Witch Doctor";
					} else if (level > 7 && level <= 20) {
						suffix = "_Herbalist";
					} else if (level > 20 && level <= 35) {
						suffix = "_Mystic";
					} else if (level > 35 && level <= 50) {
						suffix = "_Shaman";
					} else {
						suffix = "_Chief";
					}
				} else if (Class == 9) {
					if (level <= 7) {
						suffix = "_Priest";
					} else if (level > 7 && level <= 20) {
						suffix = "_Cleric";
					} else if (level > 20 && level <= 35) {
						suffix = "_Prophet";
					} else if (level > 35 && level <= 50) {
						suffix = "_Archpriest";
					} else {
						suffix = "_Diviner";
					}
				} else if (Class == 11 || Class == 12 || Class == 13 || Class == 14) {
					if (level <= 7) {
						suffix = "_Cultist";
					} else if (level > 7 && level <= 20) {
						suffix = "_Mage";
					} else if (level > 20 && level <= 35) {
						suffix = "_Summoner";
					} else if (level > 35 && level <= 50) {
						suffix = "_Spellslinger";
					} else {
						suffix = "_Archmage";
					}
				}
			}
			break;
		case PLANT:
			break;
		case UNDEAD:
			switch (raceID) {
			case ConfigReader.SKELETON_RACE:
			case ConfigReader.SARNAK_SKELETON_RACE:
			case ConfigReader.SKELETON_RACE_2:
				if (Class == 3 || Class == 5 || Class == 4) {
					if (level <= 15) {
						suffix = "_Minion";
					} else if (level > 15 && level <= 35) {
						suffix = "_Warrior";
					} else {
						suffix = "_Knight";
					}

				} else if (Class == 2 || Class == 6 || Class == 10 || Class == 15) {
					if (level <= 15) {
						suffix = "_Heretic";
					} else if (level > 15 && level <= 35) {
						suffix = "_Cleric";
					} else {
						suffix = "_Bishop";
					}
				} else if (Class == 9) {
					if (level <= 15) {
						suffix = "_Brigand";
					} else if (level > 15 && level <= 35) {
						suffix = "_Stalker";
					} else {
						suffix = "_Assassin";
					}
				} else if (Class == 11 || Class == 12 || Class == 13 || Class == 14) {
					if (level <= 15) {
						suffix = "_Magician";
					} else if (level > 15 && level <= 35) {
						suffix = "_Arcanist";
					} else {
						suffix = "_Archmage";
					}
				}

				break;
			case ConfigReader.ZOMBIE_RACE:
				if (Class == 3 || Class == 5 || Class == 4) {
					if (level <= 15) {
						suffix = "_Minion";
					} else if (level > 15 && level <= 35) {
						suffix = "_Warrior";
					} else {
						suffix = "_Knight";
					}

				} else if (Class == 2 || Class == 6 || Class == 10 || Class == 15) {
					if (level <= 15) {
						suffix = "_Heretic";
					} else if (level > 15 && level <= 35) {
						suffix = "_Cleric";
					} else {
						suffix = "_Bishop";
					}
				} else if (Class == 9) {
					if (level <= 15) {
						suffix = "_Brigand";
					} else if (level > 15 && level <= 35) {
						suffix = "_Stalker";
					} else {
						suffix = "_Assassin";
					}
				} else if (Class == 11 || Class == 12 || Class == 13 || Class == 14) {
					if (level <= 15) {
						suffix = "_Magician";
					} else if (level > 15 && level <= 35) {
						suffix = "_Arcanist";
					} else {
						suffix = "_Archmage";
					}
				}

				break;
			case ConfigReader.GHOUL_RACE:
				if (Class == 2 || Class == 6 || Class == 10 || Class == 15) {
					if (level <= 15) {
						suffix = "_Heretic";
					} else if (level > 15 && level <= 35) {
						suffix = "_Cleric";
					} else {
						suffix = "_Bishop";
					}
				}
				break;
			case ConfigReader.ELF_VAMPIRE_RACE:
			case ConfigReader.VAMPIRE_RACE:
				if (level <= 15) {
					suffix = "_Spawn";
				} else if (level > 15 && level <= 35) {
					suffix = "";
				} else {
					suffix = "_Lord";
				}

				break;
			case ConfigReader.SCARECROW_RACE:
				break;
			case ConfigReader.GHOST_RACE:
			case ConfigReader.DWARF_SPIRIT_RACE:
				if (Class == 3 || Class == 5 || Class == 4) {

				} else if (Class == 2 || Class == 6 || Class == 10 || Class == 15) {
					if (level <= 15) {
						suffix = "_Heretic";
					} else if (level > 15 && level <= 35) {
						suffix = "_Cleric";
					} else {
						suffix = "_Bishop";
					}
				} else if (Class == 9) {

				} else if (Class == 11 || Class == 12 || Class == 13 || Class == 14) {
					if (level <= 15) {
						suffix = "_Magician";
					} else if (level > 15 && level <= 35) {
						suffix = "_Arcanist";
					} else {
						suffix = "_Archmage";
					}
				}
			case ConfigReader.SHADE_RACE:

			}
			break;
		case VERMIN:
			break;
		default:
			break;

		}

		return suffix;

	}

	public RACE getRaceByID(int id) {

		for (Integer i : ConfigReader.PIRATE_RACES) {
			if (i.intValue() == id)
				return RACE.BANDITS;
		}
		for (Integer i : ConfigReader.HUMANOID_RACES) {
			if (i.intValue() == id)
				return RACE.BANDITS;
		}
		for (Integer i : ConfigReader.ANIMAL_RACES) {
			if (i.intValue() == id)
				return RACE.ANIMAL;
		}
		for (Integer i : ConfigReader.ANIMAL_MONSTER_RACES) {
			if (i.intValue() == id)
				return RACE.ANIMAL_PERSON;
		}
		for (Integer i : ConfigReader.WHORE_RACES) {
			if (i.intValue() == id)
				return RACE.WHORE;
		}
		for (Integer i : ConfigReader.GIANT_RACES) {
			if (i.intValue() == id)
				return RACE.GIANT;
		}
		for (Integer i : ConfigReader.MINOR_DUNGEON_RACES) {
			if (i.intValue() == id)
				return RACE.MONSTROSITY;
		}
		for (Integer i : ConfigReader.MONSTER_RACES) {
			if (i.intValue() == id)
				return RACE.MONSTER;
		}
		for (Integer i : ConfigReader.VERMIN_RACES) {
			if (i.intValue() == id)
				return RACE.VERMIN;
		}
		for (Integer i : ConfigReader.BUG_RACES) {
			if (i.intValue() == id)
				return RACE.BUG;
		}
		for (Integer i : ConfigReader.GOLEM_RACES) {
			if (i.intValue() == id)
				return RACE.GOLEM;
		}
		for (Integer i : ConfigReader.FEY_RACES) {
			if (i.intValue() == id)
				return RACE.FEY;
		}
		for (Integer i : ConfigReader.PLANT_RACES) {
			if (i.intValue() == id)
				return RACE.PLANT;
		}
		for (Integer i : ConfigReader.WHORE_RACES) {
			if (i.intValue() == id)
				return RACE.WHORE;
		}

		for (Integer i : ConfigReader.UNDEAD_RACES) {
			if (i.intValue() == id)
				return RACE.UNDEAD;
		}
		for (Integer i : ConfigReader.ELEMENTAL_RACES) {
			if (i.intValue() == id)
				return RACE.ELEMENTAL;
		}
		for (Integer i : ConfigReader.DOG_PEOPLE_RACES) {
			if (i.intValue() == id)
				return RACE.DOG_PERSON;
		}
		for (Integer i : ConfigReader.DRAGON_RACES) {
			if (i.intValue() == id)
				return RACE.DRAGON;
		}

		for (Integer i : ConfigReader.DRAGONISH_RACES) {
			if (i.intValue() == id)
				return RACE.DRAGON;
		}

		if (id == ConfigReader.DRAKE_RACE) {
			return RACE.DRAKE;
		}

		for (Integer i : ConfigReader.GUARD_RACES) {
			if (i.intValue() == id)
				return RACE.GUARD;
		}
		if (id == ConfigReader.WURM_RACE) {
			return RACE.WURM;
		}
		if (id == 45) {
			return RACE.UNDEAD;
		}
		if (id == ConfigReader.COLDAIN_RACE) {
			return RACE.MONSTER;
		}
		if (id == ConfigReader.CENTAUR_RACE) {
			return RACE.ANIMAL_PERSON;
		}
		if (id == ConfigReader.FUNGAL_FIEND_RACE) {
			return RACE.MONSTER;
		}
		System.out.println("NO ID FOUND: " + id);
		return null;
	}

	public String getMonsterNamePrefix(Mob m, RACE r) {
		String prefix = "A ";
		int level = m.getLevel();
		switch (r) {
		case DRAGON:
		case DRAKE:
		case WURM:
			if (level <= 3) {
				prefix = "A_Runty_";
			} else if (level <= 8) {
				prefix = "A_Stunted_";
			} else if (level > 8 && level <= 12) {
				prefix = "A_Weakened_";
			} else if (level > 12 && level <= 18) {
				prefix = "A_Young_";
			} else if (level > 18 && level <= 26) {
				prefix = "An_Adult_";
			} else if (level > 26 && level <= 33) {
				prefix = "A_Mature_";
			} else if (level > 33 && level <= 40) {
				prefix = "An_Elder_";
			} else if (level > 40 && level <= 46) {
				prefix = "A_Great_";
			} else if (level > 46 && level <= 54) {
				prefix = "An_Ancient_";
			} else {
				prefix = "An_Ancient_";
			}
			break;
		case GUARD:
			if (level <= 3) {
				prefix = "An_Ineffective_";
			} else if (level <= 8) {
				prefix = "A_Hired_";
			} else if (level > 8 && level <= 12) {
				prefix = "An_Apprentice_";
			} else if (level > 12 && level <= 18) {
				prefix = "A_Novice_";
			} else if (level > 18 && level <= 26) {
				prefix = "A_";
			} else if (level > 26 && level <= 33) {
				prefix = "A_Veteran_";
			} else if (level > 33 && level <= 40) {
				prefix = "A_Decorated_";
			} else if (level > 40 && level <= 46) {
				prefix = "An_Honored_";
			} else if (level > 46 && level <= 54) {
				prefix = "A_Legendary_";
			} else {
				prefix = "A_Legendary_";
			}
			break;
		case ANIMAL:
			if (level <= 3) {
				prefix = "A_Small_";
			} else if (level <= 8) {
				prefix = "A_Pestering_";
			} else if (level > 8 && level <= 12) {
				prefix = "A_Large_";
			} else if (level > 12 && level <= 18) {
				prefix = "A_Pack_";
			} else if (level > 18 && level <= 26) {
				prefix = "A_Mature_";
			} else if (level > 26 && level <= 33) {
				prefix = "A_Feral_";
			} else if (level > 33 && level <= 40) {
				prefix = "A_Rabid_";
			} else if (level > 40 && level <= 46) {
				prefix = "A_Great_";
			} else if (level > 46 && level <= 54) {
				prefix = "An_Alpha_";
			} else {
				prefix = "An_Ancient_";
			}
			break;
		case BUG:
			if (level <= 3) {
				prefix = "A_Small_";
			} else if (level <= 8) {
				prefix = "A_Pestering_";
			} else if (level > 8 && level <= 12) {
				prefix = "A_Large_";
			} else if (level > 12 && level <= 18) {
				prefix = "A_Bloated_";
			} else if (level > 18 && level <= 26) {
				prefix = "A_Poisonous_";
			} else if (level > 26 && level <= 33) {
				prefix = "A_Venemous_";
			} else if (level > 33 && level <= 40) {
				prefix = "A_Noxious_";
			} else if (level > 40 && level <= 46) {
				prefix = "A_Lethal_";
			} else if (level > 46 && level <= 54) {
				prefix = "A_Bane_";
			} else {
				prefix = "An_Ancient_";
			}
			break;
		case FEY:
			if (level <= 4) {
				prefix = "A_Small_";
			} else if (level > 4 && level <= 11) {
				prefix = "A_Tricky_";
			} else if (level > 11 && level <= 18) {
				prefix = "A_Playful_";
			} else if (level > 18 && level <= 26) {
				prefix = "An_Impish_";
			} else if (level > 26 && level <= 33) {
				prefix = "A_Spiteful_";
			} else if (level > 33 && level <= 40) {
				prefix = "A_Mischievous_";
			} else if (level > 40 && level <= 46) {
				prefix = "A_Destructive_";
			} else if (level > 46 && level <= 54) {
				prefix = "A_Lordling_";
			} else {
				prefix = "An_Ancient_";
			}
			break;
		case GIANT:
			if (level <= 4) {
				prefix = "A_Stunted_";
			} else if (level > 4 && level <= 11) {
				prefix = "A_Clumsy_";
			} else if (level > 11 && level <= 18) {
				prefix = "A_Lesser_";
			} else if (level > 18 && level <= 26) {
				prefix = "A_Formidable_";
			} else if (level > 26 && level <= 33) {
				prefix = "A_Strong_";
			} else if (level > 33 && level <= 40) {
				prefix = "A_Raging_";
			} else if (level > 40 && level <= 46) {
				prefix = "A_Furious_";
			} else if (level > 46 && level <= 54) {
				prefix = "An_Elder_";
			} else {
				prefix = "An_Ancient_";
			}
			break;
		case GOLEM:
			if (level <= 4) {
				prefix = "A_Cracked_";
			} else if (level > 4 && level <= 11) {
				prefix = "A_Malformed_";
			} else if (level > 11 && level <= 18) {
				prefix = "A_Jagged_";
			} else if (level > 18 && level <= 26) {
				prefix = "A_Weathered_";
			} else if (level > 26 && level <= 33) {
				prefix = "A_Sturdy_";
			} else if (level > 33 && level <= 40) {
				prefix = "A_Reinforced_";
			} else if (level > 40 && level <= 46) {
				prefix = "A_Colossal_";
			} else if (level > 46 && level <= 54) {
				prefix = "A_Crystaline_";
			} else {
				prefix = "An Ancient ";
			}
			break;
		case MONSTER:
			if (level <= 4) {
				prefix = "A_Feeble_";
			} else if (level > 4 && level <= 11) {
				prefix = "A_Weakened_";
			} else if (level > 11 && level <= 18) {
				prefix = "A_Brawny_";
			} else if (level > 18 && level <= 26) {
				prefix = "A_Strong_";
			} else if (level > 26 && level <= 33) {
				prefix = "A_Toughened_";
			} else if (level > 33 && level <= 40) {
				prefix = "A_Raging_";
			} else if (level > 40 && level <= 46) {
				prefix = "A_Battle-Hardened_";
			} else if (level > 46 && level <= 54) {
				prefix = "A_Veteran_";
			} else {
				prefix = "A_Legendary_";
			}
			break;
		case ANIMAL_PERSON:
			if (level <= 4) {
				prefix = "A_Feeble_";
			} else if (level > 4 && level <= 11) {
				prefix = "A_Weakened_";
			} else if (level > 11 && level <= 18) {
				prefix = "A_Brawny_";
			} else if (level > 18 && level <= 26) {
				prefix = "A_Strong_";
			} else if (level > 26 && level <= 33) {
				prefix = "A_Toughened_";
			} else if (level > 33 && level <= 40) {
				prefix = "A_Raging_";
			} else if (level > 40 && level <= 46) {
				prefix = "A_Battle-Hardened_";
			} else if (level > 46 && level <= 54) {
				prefix = "A_Veteran_";
			} else {
				prefix = "A_Legendary_";
			}
			break;
		case MONSTROSITY:
			if (level <= 4) {
				prefix = "A_Puny_";
			} else if (level > 4 && level <= 11) {
				prefix = "A_Miniscule_";
			} else if (level > 11 && level <= 18) {
				prefix = "A_Deformed_";
			} else if (level > 18 && level <= 26) {
				prefix = "A_Twisted_";
			} else if (level > 26 && level <= 33) {
				prefix = "A_Hideous_";
			} else if (level > 33 && level <= 40) {
				prefix = "A_Ghastly_";
			} else if (level > 40 && level <= 46) {
				prefix = "A_Horrid_";
			} else if (level > 46 && level <= 54) {
				prefix = "A_Nightmare_";
			} else {
				prefix = "An_Ancient_";
			}
			break;
		case PLANT:
			if (level <= 4) {
				prefix = "A_Sprouting_";
			} else if (level > 4 && level <= 11) {
				prefix = "A_Prickly_";
			} else if (level > 11 && level <= 18) {
				prefix = "A_Blooming_";
			} else if (level > 18 && level <= 26) {
				prefix = "A_Twisted_";
			} else if (level > 26 && level <= 33) {
				prefix = "A_Flourishing_";
			} else if (level > 33 && level <= 40) {
				prefix = "A_Thriving_";
			} else if (level > 40 && level <= 46) {
				prefix = "A_Sun-dried_";
			} else if (level > 46 && level <= 54) {
				prefix = "A_Malignant_";
			} else {
				prefix = "An_Ancient_";
			}
			break;
		case WHORE:
			if (level <= 4) {
				prefix = "A_Trap_";
			} else if (level > 4 && level <= 11) {
				prefix = "An_Alluring_";
			} else if (level > 11 && level <= 18) {
				prefix = "A_Captivating_";
			} else if (level > 18 && level <= 26) {
				prefix = "A_Charming_";
			} else if (level > 26 && level <= 33) {
				prefix = "A_Mesmerizing_";
			} else if (level > 33 && level <= 40) {
				prefix = "An_Enthralling_";
			} else if (level > 40 && level <= 46) {
				prefix = "A_Haunting_";
			} else if (level > 46 && level <= 54) {
				prefix = "An_Irresistible_";
			} else {
				prefix = "An_Ancient_";
			}
			break;
		case UNDEAD:
			if (level <= 4) {
				prefix = "A_Decaying_";
			} else if (level > 4 && level <= 11) {
				prefix = "A_Weak_";
			} else if (level > 11 && level <= 18) {
				prefix = "A_Lesser_";
			} else if (level > 18 && level <= 23) {
				prefix = "A_Frightening_";
			} else if (level > 23 && level <= 28) {
				prefix = "A_Risen_";
			} else if (level > 28 && level <= 33) {
				prefix = "An_Awakened_";
			} else if (level > 33 && level <= 40) {
				prefix = "A_Risen_";
			} else if (level > 40 && level <= 46) {
				prefix = "A_Greater_";
			} else if (level > 46 && level <= 54) {
				prefix = "A_Deadly_";
			} else {
				prefix = "An_Ancient_";
			}
			break;
		case VERMIN:
			if (level <= 3) {
				prefix = "A_Pestering_";
			} else if (level <= 8) {
				prefix = "A_Small_";
			} else if (level > 8 && level <= 11) {
				prefix = "A_Wandering_";
			} else if (level > 11 && level <= 15) {
				prefix = "A_Large_";
			} else if (level > 15 && level <= 18) {
				prefix = "A_Pack_";
			} else if (level > 18 && level <= 26) {
				prefix = "A_Mature_";
			} else if (level > 26 && level <= 33) {
				prefix = "A_Feral_";
			} else if (level > 33 && level <= 40) {
				prefix = "A_Rabid_";
			} else if (level > 40 && level <= 46) {
				prefix = "A_Great_";
			} else if (level > 46 && level <= 54) {
				prefix = "An_Alpha_";
			} else {
				prefix = "An_Ancient_";
			}
			break;

		default:
			break;
		}
		return prefix;
	}

	public String getMonsterName(RACE r, Mob m) {

		String name = "X";

		switch (r) {
		case BANDITS:
			name = "Bandit";
			break;
		case ANIMAL:
			switch (m.getRace()) {
			case ConfigReader.ARMADILLO_RACE:
				name = "Armadillo";
				break;
			case ConfigReader.TIGER_RACE:
				name = "Tiger";
				break;
			case ConfigReader.YETI_RACE:
				name = "Yeti";
				break;
			case ConfigReader.BEAR_RACE:
				name = "Bear";
				break;
			case ConfigReader.WOLF_RACE:
				name = "Wolf";
				break;
			case ConfigReader.PUMA_RACE:
				name = "Feline";
				break;
			case ConfigReader.OWLBEAR_RACE:
				name = "Owlbear";
				break;
			case ConfigReader.RHINO_RACE:
				name = "Rhino";
				break;
			case ConfigReader.MAMMOTH_RACE:
				name = "Mammoth";
				break;
			case ConfigReader.SABERTOOTH_RACE:
				name = "Sabertooth";
				break;
			case ConfigReader.WALRUS_RACE:
				name = "Walrus";
				break;
			case ConfigReader.GRIFFON_RACE:
				name = "Griffon";
				break;
			case ConfigReader.GORILLA_RACE:
				name = "Gorilla";
				break;
			case ConfigReader.ROCK_HOPPER_RACE:
				name = "Rock Hopper";
				break;
			case ConfigReader.RAPTOR_RACE:
				name = "Raptor";
			case ConfigReader.ZELNIAK_RACE:
				name = "Zelniak";
				break;
			case ConfigReader.SONIC_WOLF_RACE:
				name = "Sonic Wolf";
				break;
			case ConfigReader.DIRE_WOLF_RACE:
				name = "Dire Wolf";
				break;
			case ConfigReader.LION_RACE:
				if (m.gender == 0)
					name = "Lion";
				else {
					name = "Lioness";
				}
				break;
			case ConfigReader.MANTICORE_RACE:
				name = "Manticore";
				break;
			default:
				System.out.println("ANIMAL ERROR " + r + "  " + m.name + "    " + m.race);

				System.exit(0);
			}

			break;

		case BUG:
			switch (m.getRace()) {
			case ConfigReader.LEECH_RACE:
				name = "Leech";
				break;
			case ConfigReader.LIGHT_CRAWLER_RACE:
				name = "Crawler";
				break;
			case ConfigReader.SPIDER_RACE:
				name = "Spider";
				break;
			case ConfigReader.BEETLE_RACE:
				name = "Beetle";
				break;
			case ConfigReader.RHINO_BEETLE_RACE:
				name = "Beetle";
				break;
			case ConfigReader.MOSQUITO_RACE:
				name = "Mosquito";
				break;
			case ConfigReader.MUCK_DIGGER_RACE:
				name = "Digger";
				break;
			case ConfigReader.SILK_WORM_RACE:
				name = "Worm";
				break;
			case ConfigReader.SCORPION_RACE:
				name = "Scorpion";
				break;
			case ConfigReader.WASP_RACE:
				name = "Wasp";
				break;
			default:
				System.out.println("BUG ERROR " + r + "  " + m.name + "    " + m.race);

				System.exit(0);
			}

			break;
		case FEY:
			switch (m.getRace()) {
			case ConfigReader.FAIRY_RACE:
				name = "Fairy";
				break;
			case ConfigReader.PIXIE_RACE:
				name = "Pixie";
				break;
			case ConfigReader.DRIXIE_RACE:
				name = "Drixie";
				break;
			case ConfigReader.UNICORN_RACE:
				name = "Unicorn";
				break;
			case ConfigReader.FAE_DRAKE_RACE:
				name = "Fairy Drake";
				break;
			case ConfigReader.PEGASUS_RACE:
				name = "Pegasus";
				break;
			case ConfigReader.BROWNIE_RACE:
				name = "Brownie";
				break;
			case ConfigReader.WISP_RACE:
				name = "Wisp";
				break;
			case ConfigReader.NYMPH_RACE:
			case ConfigReader.NAIAD_RACE:
				name = "Nymph";
				break;
			default:
				System.out.println("FEY ERROR " + r + "  " + m.name + "    " + m.race);

				System.exit(0);
			}
			break;
		case GIANT:
			name = "Giant";
			break;
		case GOLEM:

			switch (m.getRace()) {
			case ConfigReader.IKSAR_GOLEM_RACE:
			case ConfigReader.SARNAK_GOLEM_RACE:
			case ConfigReader.ROCK_GEM_RACE:
				name = "Construct";
				break;
			case ConfigReader.MUDMAN_RACE:
				name = "Golem";
				break;
			default:
				name = "Construct";

			}
			break;
		case MONSTROSITY:
			switch (m.getRace()) {
			case ConfigReader.IKSAR_HAND_RACE:
				name = "Hand";
				break;
			case ConfigReader.HAND_RACE:
				name = "Hand";
				break;
			case ConfigReader.GOO_RACE:
				name = "Goo";
				break;
			case ConfigReader.DEVOURER_RACE:
				name = "Devourer";
				break;
			case ConfigReader.HOLGRESH_RACE:
				name = "Holgresh";
				break;
			case ConfigReader.TENTACLE_RACE:
				name = "Tentacle";
				break;
			case ConfigReader.EVIL_EYE_RACE:
				name = "Evil_Eye";
				break;
			case ConfigReader.GARGOYLE_RACE:
				name = "Gargoyle";
				break;
			case ConfigReader.MINOTAUR_RACE:
				name = "Minotaur";
				break;
			case ConfigReader.THOUGHT_HORROR_RACE:
				name = "Horror";
				break;
			case ConfigReader.CUBE_RACE:
				name = "Cube";
				break;
			default:
				System.out.println("MONSTROSITY NAME ERROR " + r + "  " + m.name + "    " + m.race);

				System.exit(0);
			}
			break;
		case WHORE:
			switch (m.getRace()) {
			case ConfigReader.HARPY_RACE:
				name = "Harpy";
				break;
			case ConfigReader.SIREN_RACE:
				name = "Siren";
				break;
			case ConfigReader.GORGON_RACE:
				name = "Gorgon";
				break;
			case ConfigReader.HAG_RACE:
				name = "Hag";
				break;
			default:
				System.out.println("WHORE ERROR " + r + "  " + m.name + "    " + m.race);
				System.exit(0);

			}
		case MONSTER:
			switch (m.getRace()) {
			case ConfigReader.DROLVARG_RACE:
				name = "Drolvarg";
				break;
			case ConfigReader.GNOLL_RACE:
				name = "Gnoll";
				break;
			case ConfigReader.ORC_RACE:
				name = "Orc";
				break;
			case ConfigReader.GOBLIN_RACE:
			case ConfigReader.BLOODGIL_GOBLIN_RACE:
			case ConfigReader.KUNARK_GOBLIN_RACE:
				name = "Goblin";
				break;
			case ConfigReader.KOBOLD_RACE:
			case ConfigReader.NEW_KOBOLD_RACE:
				name = "Kobold";
				break;
			case ConfigReader.OTHMIR_RACE:
				name = "Othmir";
				break;
			case ConfigReader.GRIMLING_RACE:
				name = "Grimling";
				break;
			case ConfigReader.FROGLOK_RACE:
				name = "Froglok";
				break;
			case ConfigReader.BURYNAI_RACE:
				name = "Burynai";
				break;
			case ConfigReader.NETHERBIAN_RACE:
				name = "Netherbian";
				break;
			case ConfigReader.GALORIAN_RACE:
				name = "Galorian";
				break;
			case ConfigReader.WALRUS_MAN_RACE:
				name = "Walruth";
				break;
			case ConfigReader.RAT_MAN_RACE:
				name = "Ratman";
				break;
			case ConfigReader.SARNAK_RACE:
				name = "Sarnak";
				break;
			case ConfigReader.LIZARD_MAN_RACE:
				name = "Lizardman";
				break;
			case ConfigReader.AVIAK_RACE:
				name = "Aviak";
				break;
			case ConfigReader.TEGI_RACE:
				name = "Tegi";
				break;
			case ConfigReader.COLDAIN_RACE:
				name = "Coldain";
				break;

			case ConfigReader.FUNGAL_FIEND_RACE:
				name = "Fungoid";
				break;
			default:
				name = "Monstrosity";
			}
			break;
		case PLANT:
			switch (m.getRace()) {
			case ConfigReader.PLANT_RACE:
				name = "Flower";
				break;
			case ConfigReader.MUSHROOM_RACE:
				if (m.level <= 20) {
					name = "Sporeling";
				} else {
					name = "Spore";
				}
				break;
			case ConfigReader.CACTUS_RACE:
				name = "Cactus";
				break;
			case ConfigReader.TREANT_RACE:
				if (m.level <= 20) {
					name = "Entling";
				} else {
					name = "Ent";
				}
				break;
			case ConfigReader.ENT_RACE:
				if (m.level <= 20) {
					name = "Entling";
				} else {
					name = "Ent";
				}
				break;
			case ConfigReader.MUSHROOM_MAN_RACE:
				if (m.level <= 20) {
					name = "Sporeling";
				} else {
					name = "Sporeman";
				}
				break;
			case ConfigReader.SUNFLOWER_RACE:
				name = "Sunflower";
				break;
			default:
				System.out.println("PLANT ERROR " + r + "  " + m.name + "    " + m.race);
				System.exit(0);
			}
			break;
		case GUARD:
			name = "Guardian";
			break;
		case UNDEAD:
			switch (m.getRace()) {
			case ConfigReader.SKELETON_RACE:
			case ConfigReader.SARNAK_SKELETON_RACE:
			case ConfigReader.SKELETON_RACE_2:
				name = "Skeleton";
				break;
			case ConfigReader.ZOMBIE_RACE:
				name = "Zombie";
				break;
			case ConfigReader.GHOUL_RACE:
				name = "Ghoul";
				break;
			case ConfigReader.ELF_VAMPIRE_RACE:
			case ConfigReader.VAMPIRE_RACE:
				name = "Vampire";
				break;
			case ConfigReader.SCARECROW_RACE:
				name = "Scarecrow";
				break;
			case ConfigReader.GHOST_RACE:
			case ConfigReader.DWARF_SPIRIT_RACE:
				name = "Spirit";
				break;
			case ConfigReader.SHADE_RACE:
				name = "Shade";
				break;
			case ConfigReader.UNDEAD_FROGLOK_RACE:
				name = "Froglok";
				break;
			case ConfigReader.SPECTRE_RACE:
				name = "Spectre";
				break;
			case ConfigReader.VAH_SHIR_SKELETON:
				name = "Skeleton";
				break;
			case 45:
				name = "Lich";
				break;
			default:
				System.out.println("UNDEAD RACE ERRO " + r + "  " + m.name + "    " + m.race);
				System.exit(0);

			}
			break;

		case ANIMAL_PERSON:
			switch (m.getRace()) {
			case ConfigReader.RAT_MAN_RACE:
				name = "Ratman";
				break;
			case ConfigReader.WALRUS_MAN_RACE:
				name = "Walrusman";
				break;
			case ConfigReader.OTHMIR_RACE:
				name = "Othmir";
				break;
			case ConfigReader.AVIAK_RACE:
				name = "Aviak";
				break;
			case ConfigReader.CENTAUR_RACE:
				name = "Centaur";
				break;
			default:
				System.out.println("ANIMAL PERSON HMMM " + r + "  " + m.name + "    WJKEFJKLKSD  " + m.getRace());
				System.exit(0);
			}
			break;

		case DOG_PERSON:
			name = "Werewolf";
			break;

		case WURM:
			name = "Wurm";
			break;
		case DRAKE:
			name = "Drake";
			break;
		case DRAGON:
			name = "Dragon";
			break;
		case ELEMENTAL:
			switch (m.getRace()) {
			case ConfigReader.AIR_ELEMENTAL:
			case ConfigReader.DJINN_RACE:
				name = "Air Elemental";
				break;
			case ConfigReader.FIRE_ELEMENTAL:
			case ConfigReader.EFREETI_RACE:
				name = "Fire Elemental";
				break;
			case ConfigReader.WATER_ELEMENTAL:
				name = "Water Elemental";
				break;
			case ConfigReader.EARTH_ELEMENTAL:
				name = "Earth Elemental";
				break;
			default:
				System.out.println("ELEEMNTAL PERSON HMMM " + r + "  " + m.name);
				System.exit(0);
			}
			break;

		case VERMIN:
			switch (m.getRace()) {
			case ConfigReader.BAT_RACE:
				name = "Bat";
				break;
			case ConfigReader.SNAKE_RACE:
				name = "Snake";
				break;
			case ConfigReader.RAT_RACE:
				name = "Rat";
				break;
			case ConfigReader.SKUNK_RACE:
				name = "Skunk";
				break;
			case ConfigReader.BUNNY_RACE:
				name = "Bunny";
				break;
			case ConfigReader.COCKATRICE_RACE:
				name = "Cockatrice";
				break;
			case ConfigReader.ARMADILLO_RACE:
				name = "Armadillo";
				break;
			case ConfigReader.SHIKNAR_RACE:
				name = "Shiknar";
				break;
			default:
				System.out.println("VERMIN ERROR " + r + "  " + m.name);
				System.exit(0);
			}
			break;
		default:
			System.out.println("UH OH EXIT " + m + "\n\n[" + m.getRace() + "    " + r);
			System.exit(0);
			break;

		}

		if (name.equals("X"))

		{
			System.out.println("EEEEE");
			System.out.println("UNDEFINED ERRO " + r + "  " + m.name);
			System.exit(0);
		}

		return name;

	}

	public ArrayList<Integer> getRaceIds() {
		return raceIds;
	}

	public void setRaceIds(ArrayList<Integer> raceIds) {
		this.raceIds = raceIds;
	}

	public ArrayList<Mob> GetMobsToWrite() {
		return mobs;
	}

}
