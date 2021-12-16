package EverquestUtility.data;

import java.util.ArrayList;
import java.util.Random;

import EverquestUtility.Database.EQDao;
import EverquestUtility.Database.Helpers.Mob;
import EverquestUtility.Database.Helpers.SpawnEntry;
import EverquestUtility.Database.Helpers.SpawnGroup;
import EverquestUtility.Util.ConfigReader;

public class LdonMobPacket {

	public static int CURRENT_RAID_MOB_ID = 5000000;

	public static int[] BOSS_SPAWN_GROUPS = { 287826, 287827, 287828, 287829 };

	private ArrayList<Mob> regularSpawns;
	private Mob bossMob;
	private int level;
	private int race;
	private int texture;
	private String[] randomDesc = { "Hulking", "Marauding", "Rampaging", "Ferocious", "Savage", "Violent", "Merciless",
			"Deadly", "Nightmare" };
	private ArrayList<String> desc;
	private ArrayList<SpawnGroup> spawnGroups;

	public LdonMobPacket(int level) {
		setRegularSpawns(new ArrayList<Mob>());
		desc = new ArrayList<String>();
		for (String s : randomDesc) {
			desc.add(s);
		}
		setSpawnGroups(new ArrayList<SpawnGroup>());
	}

	public ArrayList<Mob> getRegularSpawns() {
		return regularSpawns;
	}

	public void setRegularSpawns(ArrayList<Mob> regularSpawns) {
		this.regularSpawns = regularSpawns;
	}

	public Mob getBossMob() {
		return bossMob;
	}

	public void setBossMob(Mob bossMob) {
		this.bossMob = bossMob;
	}

	public int getLevel() {
		return level;
	}

	public int getRace() {
		return race;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setRace(int race) {
		this.race = race;
	}

	public void setTexture(int race) {

	}

	public static int GetNextID() {
		CURRENT_RAID_MOB_ID++;
		return CURRENT_RAID_MOB_ID;
	}

	public String getName(String prefix, int Class) {

		String suffix = "";
		String name = "";

		switch (race) {

		case ConfigReader.GNOLL_RACE:
			name = "Gnoll";
			break;
		case ConfigReader.ORC_RACE:
			name = "Orc";
			break;
		case ConfigReader.GOBLIN_RACE:
			name = "Goblin";
			break;
		case ConfigReader.KUNARK_GOBLIN_RACE:
			name = "Goblin";
			break;
		case ConfigReader.FROGLOK_RACE:
			name = "Froglock";
			break;
		case ConfigReader.LIZARD_MAN_RACE:
			name = "Lizardman";
			break;
		case ConfigReader.SARNAK_RACE:
			name = "Sarnak";
			break;
		case ConfigReader.NETHERBIAN_RACE:
			name = "Netherbian";
			break;
		}

		if (Class == 3 || Class == 5 || Class == 4) {
			suffix = "_Commander";
		} else if (Class == 2) {
			suffix = "_Archpriest";
		} else if (Class == 6 || Class == 10 || Class == 15) {
			suffix = "_Shaman";
		} else if (Class == 9) {
			suffix = "_Shadow_Walker";
		} else if (Class == 14) {
			suffix = "_Manipulator";
		} else if (Class == 11) {
			suffix = "_Bonewalker";
		} else if (Class == 12 || Class == 13) {
			suffix = "_Archmage";
		}
		return "A_" + prefix + "_" + name + suffix;
	}

	public void setMonsters(EQDao dao) {
		Random rn = new Random();

		for (Mob m : regularSpawns) {
			m.id = GetNextID();
			m.race = race;
			System.out.println("ID:" + m.id);
			m.texture = texture;
			m.aggroRadius = rn.nextInt(30) + 50;
			m.assistRadius = rn.nextInt(10) + 10;
			m.hp = rn.nextInt(5000) + 10000;
			m.size = 6 + rn.nextInt(3);
			String s = desc.remove(rn.nextInt(desc.size()));
			m.name = getName(s, m.Class);
			m.factionID = EQDao.RAID_FACTION_ID;
			m.see_invis = 1;
			m.see_hide = 1;
			m.see_improved_hide = 1;
			m.see_invis_undead = 1;
			m.gender = 2;
			m.writeMob(dao.GetConnection());
		}

		try {

			bossMob = (Mob) regularSpawns.get(rn.nextInt(regularSpawns.size())).clone();
			bossMob.race = race;
			bossMob.id = GetNextID();
			System.out.println("ID:" + bossMob.id);
			bossMob.texture = texture;
			bossMob.aggroRadius = rn.nextInt(50) + 50;
			bossMob.assistRadius = rn.nextInt(20) + 10;
			bossMob.hp = rn.nextInt(5000) + 10000;
			bossMob.size = 10 + rn.nextInt(5);
			bossMob.gender = 2;
			bossMob.name = getName("Legendary", bossMob.Class);
			bossMob.factionID = EQDao.RAID_FACTION_ID;

			bossMob.hp = bossMob.hp * 2;
			bossMob.AC = (int) ((float) bossMob.AC * 1.1f);
			bossMob.mindmg = (int) ((float) bossMob.mindmg * 1.1f);
			bossMob.maxdmg = (int) ((float) bossMob.maxdmg * 1.2f);
			bossMob.MR = (int) ((float) bossMob.MR * 1.2f);
			bossMob.FR = (int) ((float) bossMob.FR * 1.2f);
			bossMob.DR = (int) ((float) bossMob.DR * 1.2f);
			bossMob.CR = (int) ((float) bossMob.CR * 1.2f);
			bossMob.PR = (int) ((float) bossMob.PR * 1.2f);
			bossMob.see_invis = 1;
			bossMob.see_hide = 1;
			bossMob.see_improved_hide = 1;
			bossMob.see_invis_undead = 1;
			bossMob.writeMob(dao.GetConnection());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (SpawnGroup sg : spawnGroups) {
			boolean isBoss = false;
			for (int i = 0; i < BOSS_SPAWN_GROUPS.length; i++) {
				if (sg.getId() == BOSS_SPAWN_GROUPS[i])
					isBoss = true;
			}
			if (isBoss) {
				System.out.println("***ADDING " + bossMob.getName());
				dao.ReplaceMobs3(sg.getId(), bossMob);
			} else {
				Mob mm = regularSpawns.get(rn.nextInt(regularSpawns.size()));
				System.out.println("ADDING " + mm.getName());
				dao.ReplaceMobs3(sg.getId(), mm);
			}

			// got random mob...
		}

	}

	public ArrayList<SpawnGroup> getSpawnGroups() {
		return spawnGroups;
	}

	public void setSpawnGroups(ArrayList<SpawnGroup> spawnGroups) {
		this.spawnGroups = spawnGroups;

	}

}
