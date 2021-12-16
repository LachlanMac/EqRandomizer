package EverquestUtility.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import EverquestUtility.Database.Helpers.Faction;
import EverquestUtility.Database.Helpers.Item;
import EverquestUtility.Database.Helpers.LootEntry;
import EverquestUtility.Database.Helpers.Mob;
import EverquestUtility.Database.Helpers.MobItemPacket;
import EverquestUtility.Database.Helpers.NPCFaction;
import EverquestUtility.Database.Helpers.Spawn;
import EverquestUtility.Database.Helpers.SpawnEntry;
import EverquestUtility.Database.Helpers.SpawnGroup;
import EverquestUtility.Database.Helpers.Zone;
import EverquestUtility.FactionBuilder.BuiltFaction;
import EverquestUtility.FactionBuilder.FactionBundle;
import EverquestUtility.Util.ComparableFactionObject;
import EverquestUtility.Util.ConfigReader;
import EverquestUtility.Util.Debug;
import EverquestUtility.Util.Util;
import EverquestUtility.data.LdonMobPacket;

public class EQDao {

	public List<LootEntry> entries;
	public List<NPCFaction> factions;
	public List<Mob> mobs;
	public List<Mob> raidMobs;
	public ArrayList<Mob> filteredMobs;
	public List<SpawnGroup> spawnGroups;
	public List<SpawnEntry> spawnEntries;
	public List<Spawn> spawns;
	public List<Zone> zones;
	public List<Faction> primaryFactions;
	public List<Faction> smallPrimaryFactions;
	public List<Faction> trimmedFactions;
	public List<Mob> namedMobs;
	public List<Zone> ldonZones;
	public List<Mob> necroMobs;
	public List<Mob> mageMobs;
	public List<Mob> skMobs;
	public List<Mob> waterMobs;
	ArrayList<Mob> tinyFactionMobs;
	public int FAILURES = 0;
	public int COMPLETE_FAILURES = 0;
	public int[] VARIETY_REPORT = new int[100];
	public static int MOBS_ADDED_TO_ZONE = 0;
	private final int MIN_MOBS_IN_FACTION = 8;
	private Connection floatingConnection;

	public Connection getFloatingConnection() {
		return floatingConnection;
	}

	private final static String CONNECTION_STRING = "jdbc:mysql://192.168.10.69/xeq?user=remote&password=Movingon1";

	Random rn;

	public EQDao() {
		rn = new Random(System.currentTimeMillis());
		factions = new ArrayList<NPCFaction>();
		spawnGroups = new ArrayList<SpawnGroup>();
		spawnEntries = new ArrayList<SpawnEntry>();
		spawns = new ArrayList<Spawn>();
		mobs = new ArrayList<Mob>();
		zones = new ArrayList<Zone>();
		primaryFactions = new ArrayList<Faction>();
		smallPrimaryFactions = new ArrayList<Faction>();
		raidMobs = new ArrayList<Mob>();
		trimmedFactions = new ArrayList<Faction>();
		filteredMobs = new ArrayList<Mob>();
		namedMobs = new ArrayList<Mob>();
		ldonZones = new ArrayList<Zone>();
		tinyFactionMobs = new ArrayList<Mob>();
		mageMobs = new ArrayList<Mob>();
		necroMobs = new ArrayList<Mob>();
		skMobs = new ArrayList<Mob>();
		waterMobs = new ArrayList<Mob>();

		try {
			floatingConnection = DriverManager.getConnection(CONNECTION_STRING);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		load();

	}

	public void addHotzone(Zone zone) {

		PreparedStatement stmt = null;
		int rs = 1;
		String statement = "UPDATE zone set hotzone=1 where short_name=?";
		try {
			stmt = getFloatingConnection().prepareStatement(statement);
			stmt.setString(1, zone.getName());
			rs = stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addExp(Zone zone, float amount) {
		PreparedStatement stmt = null;
		int rs = 1;
		String statement = "UPDATE zone set zone_exp_multiplier=? where short_name=?";
		try {
			stmt = getFloatingConnection().prepareStatement(statement);
			stmt.setFloat(1, amount);
			stmt.setString(2, zone.getName());
			rs = stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addDungeonExp(Zone zone) {

		PreparedStatement stmt = null;
		int rs = 1;
		String statement = "UPDATE zone set zone_exp_multiplier=1.70 where short_name=?";
		try {
			stmt = getFloatingConnection().prepareStatement(statement);
			stmt.setString(1, zone.getName());
			rs = stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addUnique(ArrayList<Mob> mList, Mob m) {
		boolean contains = false;
		for (Mob mm : mList) {
			if (m.name.contentEquals(mm.name)) {
				contains = true;
				break;
			}
		}
		if (!contains) {
			mList.add(m);
		}

	}

	public int GetRandomTextureForRace(int race) {

		int texture = 0;
		ArrayList<Integer> mobsOfRace = new ArrayList<Integer>();
		for (Mob m : mobs) {
			if (m.race == race) {
				boolean exists = false;
				for (Integer ii : mobsOfRace) {
					if (ii.intValue() == m.texture) {
						exists = true;
					}
				}
				if (!exists) {
					mobsOfRace.add(texture);
				}
			}
		}

		return mobsOfRace.get(rn.nextInt(mobsOfRace.size()));

	}

	public LdonMobPacket GetLevelOneRaidMobs(String zone) {

		ArrayList<Mob> goodMobs = new ArrayList<Mob>();

		for (Mob m : raidMobs) {
			if (!m.FilterMob()) {
				if (m.getLevel() >= 55 || m.getLevel() <= 45) {
					continue;
				}
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
					continue;
				}
				if (subName.contains("a_") || subName.contains("an_")) {
					addUnique(goodMobs, m);
					continue;
				}

				if (name.contains("#")) {
					if (subName.contains("#a_") || subName.contains("#an_")) {
						addUnique(goodMobs, m);
						continue;
					}
				}
				if (m.spawn_limit == 1) {
					continue;
				}

				for (String s : ConfigReader.NORMAL_MOB_INDICATORS) {
					if (name.contains("sorceror_")) {
						// this word has orc in it...
						break;
					}

					if (name.contains(s)) {
						addUnique(goodMobs, m);
						break;
					}

				}

			}

		}

		LdonMobPacket p = new LdonMobPacket(1);
		p.setSpawnGroups(GetAllSpawnGroupsInZone(zone, true));
		p.setRace(ConfigReader.RANDOM_RAID_RACES[rn.nextInt(ConfigReader.RANDOM_RAID_RACES.length)]);
		p.setTexture(GetRandomTextureForRace(p.getRace()));
		Random rn = new Random();
		for (int i = 0; i < 5; i++) {
			Mob m = goodMobs.get(rn.nextInt(goodMobs.size()));
			try {
				Mob mCopy = (Mob) m.clone();
				p.getRegularSpawns().add(mCopy);
				goodMobs.remove(m);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// p.getRegularSpawns().add()
		}
		p.setMonsters(this);

		return p;

	}

	public void PrintRaidMobs() {

		for (Mob m : raidMobs) {
			if (m.getLevel() <= 55) {
				System.out.println(m.getName() + ":" + m.getLevel());
			}
		}

	}

	public void load() {
		System.out.println("Loading Factions");
		loadFactions();
		loadPrimaryFactions();
		System.out.println("Loading Zones");
		loadZones();
		loadLDONZones();
		System.out.println("Loading Normal Mobs");
		LoadNormalMobs();
		System.out.println("Filtering Mobs");
		FilterMobs();
		System.out.println("Loading Spawns From Zone");
		LoadSpawnsFromZones();
		System.out.println("Loading Spawn Entries");
		loadSpawnEntries();
		System.out.println("Linking To Factions");
		addMobsToFactions();
		GetSpecialtyMobs();

		printReport();

	}

	public Faction getNecromancerFaction() {

		return null;

	}

	public ArrayList<MobItemPacket> findItem(String name) {
		ArrayList<MobItemPacket> results = new ArrayList<MobItemPacket>();
		for (Item item : FindItemsByName(name)) {
			for (LootEntry e : GetEntries(item)) {
				setLootTables(e);
				for (Integer i : e.getLoottable_ids()) {
					for (Mob m : GetMobByLoottableID(i)) {
						for (SpawnGroup sg : getSpawnGroupsWithMob(m)) {
							for (SpawnEntry se : sg.getEntries()) {
								if (se.getNpcId() == m.getId()) {
									results.add(new MobItemPacket(sg.getFormatZones(), m, se.getChance()));
								}
							}
						}
					}
				}
			}
		}
		return results;
	}

	public Mob getWaterMobAroundLevel(int level) {
		ArrayList<Mob> possible = new ArrayList<Mob>();
		for (Mob m : waterMobs) {
			if (Math.abs(level - m.level) <= 5) {
				possible.add(m);
			}
		}
		Mob mm = null;
		try {
			mm = (Mob) possible.get(rn.nextInt(possible.size())).clone();

			switch (mm.race) {

			case ConfigReader.BARRACUDA_RACE:
				mm.name = "A_Barracuda";
				break;
			case ConfigReader.SHARK_RACE:
				mm.name = "A_Shark";
				break;
			case ConfigReader.FISH_RACE:
				mm.name = "A_Fish";
				break;
			case ConfigReader.TIDE_FEASTER:
				mm.name = "A_Tide_Feaster";
				break;
			case ConfigReader.ANGLER_RACE:
				mm.name = "An_Angler";
				break;
			case ConfigReader.SEAHORSE_RACE:
				mm.name = "A_Seahorse";
				break;
			case ConfigReader.SWARM_RACE:
				mm.name = "A_Swarm";
				break;
			case ConfigReader.MINNOW_RACE:
				mm.name = "A_Minnow";
				break;
			case ConfigReader.NERIAD_RACE:
				mm.name = "A_Neriad";
				break;
			case ConfigReader.BARRACUDA_2_RACE:
				mm.name = "A_Barracuda";
				break;
			default:
				mm.name = "A_Curious_Fish";

			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mm;
	}

	private void FilterMobs() {

		for (Mob m : mobs) {

			if (!m.FilterMob()) {

				if (m.underwater != 0) {
					waterMobs.add(m);
				}

				if (m.raidtarget != 0) {
					if (m.name.length() >= 4) {
						raidMobs.add(m);
					}
				} else {
					if (m.name.length() >= 4) {
						filteredMobs.add(m);
					}
				}
			}
		}

		System.out.println("REPORT:");
		System.out.println("OG:" + mobs.size() + "   FILTERD:" + filteredMobs.size() + " RAID " + raidMobs.size());

	}

	public void printReport() {
		System.out.println("Normal Mobs Loaded " + mobs.size() + " \nRaid MObs Loaded " + raidMobs.size());
		System.out.println("Spawn Groups=" + spawnGroups.size());
		System.out.println("Entries=" + spawnEntries.size());
		System.out.println("Original Factions " + primaryFactions.size());
		System.out.println("Trimmed Faction " + trimmedFactions.size());
	}

	public ArrayList<SpawnGroup> getSpawnGroupsWithMob(Mob m) {

		ArrayList<SpawnGroup> sgWMob = new ArrayList<SpawnGroup>();

		for (SpawnGroup sg : spawnGroups) {
			for (SpawnEntry e : sg.getEntries()) {
				if (e.getNpcId() == m.getId()) {
					sgWMob.add(sg);
				}
			}
		}

		return sgWMob;
	}

	private void addMobsToFactions() {

		for (Mob m : filteredMobs) {

			Faction f = GetPrimaryFactionByID(m.getPrimaryFaction().getId());
			Mob freshMob = null;
			try {
				freshMob = (Mob) m.clone();
			} catch (CloneNotSupportedException e) {

				e.printStackTrace();
				System.exit(0);
			}
			f.addMob(freshMob);

		}
		Collections.sort(primaryFactions);
		for (SpawnGroup sg : spawnGroups) {
			sg.setPrimaryFaction(this);
		}

		ArrayList<Faction> destroyFactions = new ArrayList<Faction>();

		for (Faction f : primaryFactions) {
			if (f.getMobs().size() >= MIN_MOBS_IN_FACTION) {
				f.updateFaction();
				trimmedFactions.add(f);
			} else {
				smallPrimaryFactions.add(f);
				f.updateFaction();
				if (f.DestroyFaction())
					destroyFactions.add(f);
			}

		}

		for (Faction dF : destroyFactions) {
			// System.out.println("Destroyed " + dF.getName());
			smallPrimaryFactions.remove(dF);
		}
	}

	public void LoadNormalMobs() {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String statement = "SELECT * FROM npc_types";
			stmt = conn.prepareStatement(statement);
			rs = stmt.executeQuery();

			while (rs.next()) {

				String name = rs.getString("name");
				int id = rs.getInt("id");
				int lev = rs.getInt("level");
				int merchantID = rs.getInt("merchant_id");
				int factionID = rs.getInt("npc_faction_id");
				int trapID = rs.getInt("trap_template");
				int globalq = rs.getInt("qglobal");
				int adventureID = rs.getInt("adventure_template_id");
				int untargetable = rs.getInt("untargetable");
				int raidTarget = rs.getInt("raid_target");
				String lastname = rs.getString("lastname");
				int race = rs.getInt("race");
				int underwater = rs.getInt("underwater");

				Mob m = new Mob(id, name, lev, adventureID, trapID, factionID, merchantID, globalq, raidTarget,
						untargetable, race);
				m.setFaction(getFactionByID(m.getFactionID()));
				m.setPrimaryFaction(GetPrimaryFactionByID(m.getFaction().getPrimaryFactionID()));
				m.Class = rs.getInt("class");
				m.bodytype = rs.getInt("bodytype");
				m.hp = rs.getInt("hp");
				m.mana = rs.getInt("mana");
				m.gender = rs.getInt("gender");
				m.size = rs.getInt("size");
				m.lastName = lastname;
				m.texture = rs.getInt("texture");
				m.hp_regen_rate = rs.getInt("hp_regen_rate");
				m.mana_regen_rate = rs.getInt("mana_regen_rate");
				m.loottable_id = rs.getInt("loottable_id");
				m.alt_currency_id = rs.getInt("alt_currency_id");
				m.npc_spells_id = rs.getInt("npc_spells_id");
				m.npc_spells_effects_id = rs.getInt("npc_spells_effects_id");
				m.mindmg = rs.getInt("mindmg");
				m.maxdmg = rs.getInt("maxdmg");
				m.attack_count = rs.getInt("attack_count");
				m.npcSpecialAttacks = rs.getString("npcspecialattks");
				m.specialAbilities = rs.getString("special_abilities");
				m.aggroRadius = rs.getInt("aggroradius");
				m.assistRadius = rs.getInt("assistradius");
				m.prim_melee_type = rs.getInt("prim_melee_type");
				m.sec_melee_type = rs.getInt("sec_melee_type");
				m.ranged_type = rs.getInt("ranged_type");
				m.runspeed = rs.getInt("runspeed");
				m.MR = rs.getInt("MR");
				m.CR = rs.getInt("CR");
				m.DR = rs.getInt("DR");
				m.FR = rs.getInt("FR");
				m.PR = rs.getInt("PR");
				m.Corrup = rs.getInt("Corrup");
				m.PhR = rs.getInt("PhR");
				m.see_invis = rs.getInt("see_invis");
				m.see_invis_undead = rs.getInt("see_invis_undead");
				m.trackable = rs.getInt("trackable");
				m.findable = rs.getInt("findable");
				m.qglobal = rs.getInt("qglobal");
				m.AC = rs.getInt("AC");
				m.npc_aggro = rs.getInt("npc_aggro");
				m.spawn_limit = rs.getInt("spawn_limit");
				m.attack_speed = rs.getInt("attack_speed");
				m.attack_delay = rs.getInt("attack_delay");
				m.STR = rs.getInt("STR");
				m.STA = rs.getInt("STA");
				m.DEX = rs.getInt("DEX");
				m.WIS = rs.getInt("WIS");
				m._INT = rs.getInt("_INT");
				m.CHA = rs.getInt("CHA");
				m.AGI = rs.getInt("AGI");
				m.see_hide = rs.getInt("see_hide");
				m.see_improved_hide = rs.getInt("see_improved_hide");
				m.isbot = rs.getInt("isbot");
				m.exclude = rs.getInt("exclude");
				m.atk = rs.getInt("ATK");
				m.accuracy = rs.getInt("Accuracy");
				m.avoidance = rs.getInt("Avoidance");
				m.slow_mitigation = rs.getInt("slow_mitigation");
				m.version = rs.getInt("version");
				m.maxlevel = rs.getInt("maxlevel");
				m.scalerate = rs.getInt("scalerate");
				m.privatecorpse = rs.getInt("private_corpse");
				m.unique_spawn_by_name = rs.getInt("unique_spawn_by_name");
				m.underwater = underwater;
				m.isquest = rs.getInt("isquest");
				m.emoteid = rs.getInt("emoteid");
				m.spellscale = rs.getInt("spellscale");
				m.healscale = rs.getInt("healscale");
				m.no_target_hotkey = rs.getInt("no_target_hotkey");
				m.raidtarget = raidTarget;
				m.light = rs.getInt("light");
				m.walkspeed = rs.getInt("walkspeed");
				m.peqid = rs.getInt("peqid");
				m.unique_ = rs.getInt("unique_");
				m.fixed = rs.getInt("fixed");
				m.ignore_despawn = rs.getInt("ignore_despawn");
				m.show_name = rs.getInt("show_name");
				m.untargetable = rs.getInt("untargetable");
				m.charm_ac = rs.getInt("charm_ac");
				m.charm_min_dmg = rs.getInt("charm_min_dmg");
				m.charm_max_dmg = rs.getInt("charm_max_dmg");
				m.charm_attack_delay = rs.getInt("charm_attack_delay");
				m.charm_accuracy_rating = rs.getInt("charm_accuracy_rating");
				m.charm_attack_delay = rs.getInt("charm_attack_delay");
				m.charm_atk = rs.getInt("charm_atk");
				m.charm_avoidance_rating = rs.getInt("charm_avoidance_rating");
				m.skip_global_loot = rs.getInt("skip_global_loot");
				m.rarespawn = rs.getInt("rare_spawn");
				m.stuck_behavior = rs.getInt("stuck_behavior");
				m.model = rs.getInt("model");
				m.flymode = rs.getInt("flymode");
				m.always_aggro = rs.getInt("always_aggro");
				m.exp_mod = rs.getInt("exp_mod");

				mobs.add(m);

			}
			conn.close();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void commit() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			conn.commit();
			conn.close();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public void LoadSpawnsFromZone() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			for (Zone z : zones) {
				PreparedStatement stmt = null;
				ResultSet rs = null;
				String statement = "SELECT id,spawngroupID,zone,version from spawn2 where zone=?";
				stmt = conn.prepareStatement(statement);
				stmt.setString(1, z.getName());
				rs = stmt.executeQuery();
				while (rs.next()) {

					int id = rs.getInt("id");
					int spawngroupID = rs.getInt("spawngroupID");
					String zone = rs.getString("zone");
					int version = rs.getInt("version");
					Spawn s = new Spawn(id, spawngroupID, zone, version);
					spawns.add(s);

					z.addSpawn(s);
					AddSpawnGroup(spawngroupID, zone);

				}
			}

			for (Zone z : ldonZones) {
				PreparedStatement stmt = null;
				ResultSet rs = null;
				String statement = "SELECT id,spawngroupID,zone,version from spawn2 where zone=?";
				stmt = conn.prepareStatement(statement);
				stmt.setString(1, z.getName());
				rs = stmt.executeQuery();
				while (rs.next()) {

					int id = rs.getInt("id");
					int spawngroupID = rs.getInt("spawngroupID");
					int version = rs.getInt("version");
					String zone = rs.getString("zone");
					Spawn s = new Spawn(id, spawngroupID, zone, version);
					spawns.add(s);
					z.addSpawn(s);
					AddSpawnGroup(spawngroupID, zone);
				}
			}

			conn.close();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public void LoadSpawnsFromZones() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			for (Zone z : zones) {
				PreparedStatement stmt = null;
				ResultSet rs = null;
				String statement = "SELECT id,spawngroupID,zone,version from spawn2 where zone=?";
				stmt = conn.prepareStatement(statement);
				stmt.setString(1, z.getName());
				rs = stmt.executeQuery();
				while (rs.next()) {

					int id = rs.getInt("id");
					int spawngroupID = rs.getInt("spawngroupID");
					String zone = rs.getString("zone");
					int version = rs.getInt("version");
					Spawn s = new Spawn(id, spawngroupID, zone, version);
					spawns.add(s);

					z.addSpawn(s);
					AddSpawnGroup(spawngroupID, zone);

				}
			}

			for (Zone z : ldonZones) {
				PreparedStatement stmt = null;
				ResultSet rs = null;
				String statement = "SELECT id,spawngroupID,zone,version from spawn2 where zone=?";
				stmt = conn.prepareStatement(statement);
				stmt.setString(1, z.getName());
				rs = stmt.executeQuery();
				while (rs.next()) {

					int id = rs.getInt("id");
					int spawngroupID = rs.getInt("spawngroupID");
					int version = rs.getInt("version");
					String zone = rs.getString("zone");
					Spawn s = new Spawn(id, spawngroupID, zone, version);
					if (version == 2) {
						spawns.add(s);
						z.addSpawn(s);
						AddSpawnGroup(spawngroupID, zone);
					}
				}
			}

			conn.close();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public ArrayList<SpawnGroup> GetSpawnGroupsFromZone(String zone) {
		ArrayList<SpawnGroup> zsg = new ArrayList<SpawnGroup>();
		for (SpawnGroup sg : spawnGroups) {
			System.out.println(sg.getId());
			if (sg.getZone().contentEquals(zone)) {
				zsg.add(sg);
			}
		}
		return zsg;

	}

	public SpawnGroup AddSpawnGroup(int spawnGroupID, String zone) {

		for (SpawnGroup s : spawnGroups) {

			if (s.getId() == spawnGroupID) {
				s.addZone(zone);
				s.addTotalSpawn();
				return s;
			}
		}

		SpawnGroup sg = new SpawnGroup(spawnGroupID);

		for (int i = 0; i < ConfigReader.FORBIDDEN_SPAWNGROUPS.length; i++) {
			if (spawnGroupID == ConfigReader.FORBIDDEN_SPAWNGROUPS[i])
				sg.forbid();
		}

		sg.addZone(zone);
		spawnGroups.add(sg);
		sg.addTotalSpawn();
		return sg;

	}

	public void setSpawns() {

		for (SpawnGroup sg : spawnGroups) {

			for (SpawnEntry se : spawnEntries) {

				if (se.getId() == sg.getId()) {
					sg.AddEntry(se);
				}
			}

			sg.setPrimaryFaction(this);

		}
	}

	public void dumpFactions(int mobsInFactionMinimum) {

		ArrayList<Faction> dumpFactions = new ArrayList<Faction>();
		for (Faction f : primaryFactions) {

			for (Mob m : mobs) {

				if (m.getPrimaryFaction().getId() == f.getId()) {
					f.AddLevel(m.getLevel());
				}

			}
			if (f.getLevels().size() <= mobsInFactionMinimum) {
				dumpFactions.add(f);
			}
		}
		for (Faction f : dumpFactions) {
			primaryFactions.remove(f);
		}
	}

	public void linkAll() {
		// set mob factions
		for (Mob m : mobs) {

			m.setFaction(getFactionByID(m.getFactionID()));

			m.setPrimaryFaction(GetPrimaryFactionByID(m.getFaction().getPrimaryFactionID()));

		}
		dumpFactions(10);

		for (Zone z : zones) {
			for (Spawn s : spawns) {
				if (z.getName().equals(s.getZone())) {
					z.addSpawn(s);
				}
			}
		}
	}

	public Faction GetFactionFromSpawnGroup(SpawnGroup spawnGroup) {

		ArrayList<Faction> f1 = new ArrayList<Faction>();
		ArrayList<Faction> f2 = new ArrayList<Faction>();

		for (SpawnEntry se : spawnGroup.getEntries()) {
			// haven't set one yet..

			Mob m = GetMobByID(se.getNpcId());
			Mob freshMob = null;
			try {
				freshMob = (Mob) m.clone();
			} catch (CloneNotSupportedException e) {

				e.printStackTrace();
				System.exit(0);
			}

			spawnGroup.addMob(freshMob);
			if (f1.size() == 0 && f2.size() == 0) {
				f1.add(m.getPrimaryFaction());
			} else {
				Faction g = m.getPrimaryFaction();
				if (g.getId() == f1.get(0).getId()) {
					f1.add(g);
				} else if (f2.size() == 0) {
					f2.add(g);
				} else if (g.getId() == f2.get(0).getId()) {
					f2.add(g);
				} else {
					// do nothing
				}
			}
		}

		if (f1.size() == 0) {
			return primaryFactions.get(0);
		}

		if (f1.size() >= f2.size()) {
			return f1.get(0);
		} else {
			return f2.get(0);
		}
	}

	public ArrayList<SpawnGroup> GetAllSpawnGroupsInZone(Zone zone, boolean ldon) {

		Zone z = GetZoneByName(zone.getName(), ldon);

		ArrayList<SpawnGroup> groups = new ArrayList<SpawnGroup>();

		List<Integer> sgIds = new ArrayList<Integer>();

		for (Spawn s : z.getSpawns()) {

			// WE WANT SPAWN GROUPS...
			if (!sgIds.contains(s.getSpawngroupId()))
				sgIds.add(s.getSpawngroupId());
		}

		for (Integer i : sgIds) {
			groups.add(GetSpawnGroupByID(i));
		}

		return groups;

	}

	public ArrayList<SpawnGroup> GetAllSpawnGroupsInZone(String zone, boolean ldon) {

		ArrayList<SpawnGroup> groups = new ArrayList<SpawnGroup>();
		Zone z = GetZoneByName(zone, ldon);
		List<Integer> sgIds = new ArrayList<Integer>();

		for (Spawn s : z.getSpawns()) {
			if (!sgIds.contains(s.getSpawngroupId()))
				sgIds.add(s.getSpawngroupId());
		}

		for (Integer i : sgIds) {
			groups.add(GetSpawnGroupByID(i));

		}

		return groups;

	}

	public ArrayList<Faction> GetPrimaryFactionsByZone(Zone zone) {

		ArrayList<Faction> factions = new ArrayList<Faction>();
		ArrayList<Mob> mobs = GetMobsInZone(zone);

		for (Mob m : mobs) {
			Mob freshMob = null;
			try {
				freshMob = (Mob) m.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			zone.addMob(freshMob);

			if (!factions.contains(m.getPrimaryFaction())) {

				factions.add(m.getPrimaryFaction());

			}

		}

		return factions;

	}

	public ArrayList<Mob> GetMobsInZone(Zone z) {

		ArrayList<Mob> zoneEntries = new ArrayList<Mob>();
		List<Integer> sgIds = new ArrayList<Integer>();

		for (Spawn s : z.getSpawns()) {
			if (Debug.DEBUG_SPAWN_GROUPS)
				System.out.println(s.getId() + " " + s.getSpawngroupId());

			// WE WANT SPAWN GROUPS...
			if (!sgIds.contains(s.getSpawngroupId()))
				sgIds.add(s.getSpawngroupId());
		}

		for (Integer i : sgIds) {

			SpawnGroup sg = GetSpawnGroupByID(i);

			for (SpawnEntry se : sg.getEntries()) {
				Mob m = GetMobByID(se.getNpcId());
				if (m != null) {

					zoneEntries.add(m);

				}
			}
		}

		return zoneEntries;

	}

	public SpawnGroup GetSpawnGroupByID(int id) {

		SpawnGroup g = new SpawnGroup(999999, "NONE");
		for (SpawnGroup group : spawnGroups) {
			if (group.getId() == id) {
				return group;
			}

		}

		return g;

	}

	public Zone GetZoneByName(String name, boolean ldon) {
		Zone z = zones.get(0);

		if (ldon) {
			for (Zone zones : ldonZones) {
				if (zones.getName().equals(name)) {
					System.out.println("RETURNING " + name);
					return zones;
				}
			}

		} else {
			for (Zone zones : zones) {
				if (zones.getName().equals(name)) {
					return zones;
				}
			}
		}

		return z;

	}

	public ArrayList<Mob> FilterMobsByFaction(Zone z, Faction f) {

		ArrayList<Mob> mobs = new ArrayList<Mob>();

		ArrayList<Mob> zoneMobs = f.getMobs();
		ArrayList<Mob> factionMobs = z.getMobsInZone();

		for (Mob m : factionMobs) {
			for (Mob mm : zoneMobs) {
				if (m.getId() == mm.getId()) {
					mobs.add(mm);
				}
			}

		}

		return mobs;
	}

	public ArrayList<Mob> GetAllMobsByFaction(int factionID) {

		ArrayList<Mob> mobsInFaction = new ArrayList<Mob>();

		for (Mob mob : mobs) {

			if (mob.getPrimaryFaction().getId() == factionID) {

				mobsInFaction.add(mob);
			}
		}

		return mobsInFaction;
	}

	public ArrayList<Mob> GetAllMobsByPrimaryFaction(int factionID) {

		ArrayList<Mob> mobsInFaction = new ArrayList<Mob>();

		for (Mob mob : mobs) {

			if (mob.getPrimaryFaction().getId() == factionID) {

				mobsInFaction.add(mob);
			}
		}

		return mobsInFaction;
	}

	public Faction GetPrimaryFactionByID(int id) {

		Faction primary = new Faction(0, "none");

		for (Faction f : primaryFactions) {
			if (id == f.getId()) {
				return f;
			}
		}
		return primary;
	}

	public Mob GetMobByID(int id) {

		Mob m = mobs.get(0);
		for (Mob mob : filteredMobs) {

			if (mob.getId() == id)
				return mob;

		}
		return m;

	}

	public NPCFaction getFactionByID(int i) {

		NPCFaction f = new NPCFaction(0, "None", 0);

		for (NPCFaction nf : factions) {
			if (nf.getId() == i) {
				return nf;
			}
		}

		return f;
	}

	public void loadLDONZones() {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String statement = "SELECT id,short_name,long_name from zone WHERE expansion = 7";
			stmt = conn.prepareStatement(statement);
			rs = stmt.executeQuery();
			while (rs.next()) {

				int id = rs.getInt("id");
				String name = rs.getString("short_name");
				String longName = rs.getString("long_name");
				Zone z = new Zone(id, name);
				z.setLongName(longName);
				ldonZones.add(z);

			}
			conn.close();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void loadZones() {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String statement = "SELECT id,short_name,long_name from zone WHERE expansion < 5";
			stmt = conn.prepareStatement(statement);
			rs = stmt.executeQuery();
			while (rs.next()) {

				int id = rs.getInt("id");
				String name = rs.getString("short_name");
				String longName = rs.getString("long_name");
				Zone z = new Zone(id, name);
				z.setLongName(longName);
				zones.add(z);

			}
			conn.close();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public void loadFactions() {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String statement = "SELECT id,name,primaryfaction from npc_faction";
			stmt = conn.prepareStatement(statement);
			rs = stmt.executeQuery();
			while (rs.next()) {

				int id = rs.getInt("id");
				String name = rs.getString("name");
				int pfac = rs.getInt("primaryfaction");
				factions.add(new NPCFaction(id, name, pfac));
			}
			conn.close();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public void loadPrimaryFactions() {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String statement = "SELECT id,name from faction_list";
			stmt = conn.prepareStatement(statement);
			rs = stmt.executeQuery();
			while (rs.next()) {

				int id = rs.getInt("id");
				String name = rs.getString("name");
				primaryFactions.add(new Faction(id, name));

			}
			conn.close();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public void loadSpawnEntries() {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String statement = "SELECT spawngroupID,npcID,chance from spawnentry";
			stmt = conn.prepareStatement(statement);
			rs = stmt.executeQuery();
			while (rs.next()) {

				int spawngroupID = rs.getInt("spawngroupID");
				int npcID = rs.getInt("npcID");
				int chance = rs.getInt("chance");
				SpawnEntry se = new SpawnEntry(spawngroupID, npcID, chance);
				SpawnGroup sg = GetSpawnGroupByID(spawngroupID);
				if (sg.getId() == 999999) {

				} else {
					spawnEntries.add(se);
					sg.AddEntry(se);
					Mob m = null;
					try {
						m = (Mob) GetMobByID(npcID).clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					sg.addMob(m);
				}

			}
			conn.close();
			System.out.println("loaded spawn entries");
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public void loadSpawnGroups() {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String statement = "SELECT id,name from spawngroup";
			stmt = conn.prepareStatement(statement);
			rs = stmt.executeQuery();

			while (rs.next()) {

				int id = rs.getInt("id");
				String name = rs.getString("name");
				spawnGroups.add(new SpawnGroup(id, name));

			}
			conn.close();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public ArrayList<Mob> GetBalancedNamed(int count, int minLevel, int maxLevel) {

		ArrayList<Mob> namedTempList = new ArrayList<Mob>();
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

	public Connection GetConnection() {

		try {
			floatingConnection = DriverManager.getConnection(CONNECTION_STRING);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return floatingConnection;
	}

	public void GetSpecialtyMobs() {

		for (Mob m : filteredMobs) {
			if (m.Class == 11) {
				necroMobs.add(m);
			} else if (m.Class == 12 || m.Class == 13) {
				mageMobs.add(m);
			} else if (m.Class == 5) {
				skMobs.add(m);
			}
		}

	}

	public void CloseFloating() {
		try {
			floatingConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setLootTables(LootEntry lootEntry) {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String statement = "SELECT loottable_id FROM loottable_entries where lootdrop_id=?";
			stmt = conn.prepareStatement(statement);
			stmt.setInt(1, lootEntry.getLootdrop_id());
			rs = stmt.executeQuery();

			while (rs.next()) {

				int ldid = rs.getInt("loottable_id");
				lootEntry.getLoottable_ids().add(ldid);

			}
			conn.close();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}

	}

	public ArrayList<Mob> GetMobByLoottableID(int lootableID) {

		ArrayList<Mob> mList = new ArrayList<Mob>();
		for (Mob m : mobs) {

			if (m.loottable_id == lootableID) {
				mList.add(m);
			}

		}

		return mList;
	}

	public ArrayList<LootEntry> GetEntries(Item item) {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String statement = "SELECT lootdrop_id FROM lootdrop_entries where item_id=?";
			stmt = conn.prepareStatement(statement);
			stmt.setInt(1, item.getId());
			rs = stmt.executeQuery();
			ArrayList<LootEntry> entries = new ArrayList<LootEntry>();

			while (rs.next()) {
				int ldid = rs.getInt("lootdrop_id");
				LootEntry e = new LootEntry(item);
				e.setLootdrop_id(ldid);
				entries.add(e);
			}
			conn.close();
			return entries;

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return new ArrayList<LootEntry>();
		}

	}

	public ArrayList<Item> FindItemsByName(String item) {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String statement = "SELECT name,id FROM items where name like ?";
			stmt = conn.prepareStatement(statement);
			stmt.setString(1, "%" + item + "%");
			rs = stmt.executeQuery();
			ArrayList<Item> items = new ArrayList<Item>();
			while (rs.next()) {
				int itemID = rs.getInt("id");
				String name = rs.getString("name");
				items.add(new Item(itemID, name));
			}
			conn.close();
			return items;

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return new ArrayList<Item>();
		}

	}

	public static int RAID_FACTION_ID = 5000000;

	public void WriteRaidFaction() {
		try {

			if (!Debug.SKIP_SQL) {

				Connection conn = DriverManager.getConnection(CONNECTION_STRING);
				String statement = "INSERT INTO faction_list(id,name,base) VALUES(?,?,?);";
				PreparedStatement stmt = conn.prepareStatement(statement);
				stmt.setInt(1, RAID_FACTION_ID);
				stmt.setString(2, "Raid Faction");
				stmt.setInt(3, -9999);
				stmt.execute();
				statement = "INSERT INTO npc_faction(id,name,primaryfaction,ignore_primary_assist) VALUES(?,?,?,0);";
				stmt = conn.prepareStatement(statement);
				stmt.setInt(1, RAID_FACTION_ID);
				stmt.setString(2, "Raid Faction");
				stmt.setInt(3, 0);
				stmt.execute();
				System.out.println("Wrote Raid Faction");
				conn.close();
			}

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public void WriteFaction(BuiltFaction bf) {

		try {

			if (!Debug.SKIP_SQL) {
				Connection conn = DriverManager.getConnection(CONNECTION_STRING);
				String statement = "INSERT INTO faction_list(id,name,base) VALUES(?,?,?);";
				PreparedStatement stmt = conn.prepareStatement(statement);
				stmt.setInt(1, bf.getId());
				stmt.setString(2, bf.getName());
				stmt.setInt(3, bf.getBaseFaction());
				stmt.execute();
				statement = "INSERT INTO npc_faction(id,name,primaryfaction,ignore_primary_assist) VALUES(?,?,?,0);";
				stmt = conn.prepareStatement(statement);
				stmt.setInt(1, bf.getId());
				stmt.setString(2, bf.getName());
				stmt.setInt(3, bf.getId());
				stmt.execute();
				conn.close();
			}

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		//

	}

	public void ReplaceMobs(ArrayList<SpawnEntry> entries, ArrayList<Mob> replacementMobs,
			ArrayList<Mob> namedMobsToAdd, float percentChance, int popularity) {

		int totalLevels = 0;
		int ogMobs = entries.size();

		ArrayList<Mob> unique = new ArrayList<Mob>();

		// i wonder if we can build a more intelligent list if its too weighted towards
		// a certain mob...
		for (Mob repMob : replacementMobs) {

			if (!unique.contains(repMob)) {
				unique.add(repMob);
			}

		}
		int totalMobs = entries.size();

		if (entries.size() < 3) {
			totalMobs += rn.nextInt(2);
		}
		totalMobs += Util.caclulateExtraSpawns(popularity);

		if (totalMobs > unique.size()) {
			totalMobs = unique.size();
		}

		int[] chances = Util.GetChancesFor(totalMobs);

		// VARIETY_REPORT[unique.size()]++;

		for (int i = 0; i < entries.size(); i++) {

			try {
				if (unique.size() == 0) {
					COMPLETE_FAILURES++;
					return;
				}
				Mob original = GetMobByID(entries.get(i).getNpcId());
				Mob rep;
				int rChance = rn.nextInt(100);
				if (rChance <= percentChance) {
					rep = replacementMobs.get(rn.nextInt(replacementMobs.size()));
				} else {
					rep = unique.get(rn.nextInt(unique.size()));
				}

				ArrayList<Mob> copyMobs = (ArrayList<Mob>) unique.clone();

				// get highest
				Collections.shuffle(copyMobs);
				boolean gotClose = false;

				for (int xx = 2; xx < 40; xx++) {
					rep = copyMobs.get(rn.nextInt(copyMobs.size()));
					for (Mob mm : copyMobs) {

						if (Math.abs(mm.level - original.level) <= xx) {
							rep = mm;
							gotClose = true;

							break;
						}

					}
					if (gotClose) {
						break;
					}
				}
				unique.remove(rep);
				MOBS_ADDED_TO_ZONE++;
				totalLevels += rep.level;
				String statement = "UPDATE spawnentry SET npcID=?,chance=? WHERE spawngroupID=? AND npcID=?";
				PreparedStatement stmt = floatingConnection.prepareStatement(statement);
				// System.out.println("ENTRIES=" + entries.size() + " LOL " +
				// entries.get(i).getId());

				if (!Debug.SKIP_SQL) {

					if (original.getId() != 500) {
						stmt.setInt(1, rep.getId());
						stmt.setInt(2, chances[i]);
						stmt.setInt(3, entries.get(i).getId());
						stmt.setInt(4, entries.get(i).getNpcId());
						stmt.executeUpdate();
					}
				}

			} catch (SQLException e) {

				System.out.println("SQL ERROR " + e.getMessage());
				System.exit(0);

			}

		}

		int average = totalLevels / ogMobs;

		for (int ii = entries.size(); ii < totalMobs; ii++) {

			Mob rep = unique.get(rn.nextInt(unique.size()));

			ArrayList<Mob> copyMobs = (ArrayList<Mob>) unique.clone();

			// get highest
			Collections.shuffle(copyMobs);
			boolean gotClose = false;

			for (int xx = 1; xx < 20; xx++) {
				rep = copyMobs.get(rn.nextInt(copyMobs.size()));
				for (Mob mm : copyMobs) {

					if (Math.abs(mm.level - average) <= xx) {
						rep = mm;
						gotClose = true;

						break;
					}

				}
				if (gotClose) {
					break;
				}
			}
			try {

				if (unique.size() == 0) {
					COMPLETE_FAILURES++;
					return;
				}
				rep = unique.get(rn.nextInt(unique.size()));
				unique.remove(rep);
				String statement = "INSERT INTO spawnentry(spawngroupID,npcID,chance,condition_value_filter) VALUES(?,?,?,?);";
				PreparedStatement stmt = floatingConnection.prepareStatement(statement);
				// System.out.println("ADDING: " + rep);
				if (!Debug.SKIP_SQL) {

					stmt.setInt(1, entries.get(0).getId());
					stmt.setInt(2, rep.getId());
					stmt.setInt(3, chances[ii]);
					stmt.setInt(4, 1);
					stmt.execute();
				} else {
					// System.out.println(rep);
				}
				// System.out.println("ADDED " + rep);

			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	public ArrayList<Integer> GetTexturesByRace(int raceID) {

		ArrayList<Integer> textures = new ArrayList<Integer>();
		for (Mob m : filteredMobs) {
			if (m.race == raceID) {
				int texture = m.texture;
				boolean contains = false;
				for (Integer t : textures) {
					if (t.intValue() == texture)
						contains = true;
				}
				if (!contains) {
					textures.add(texture);
				}
			}
		}
		return textures;

	}

	public Random getRandom() {
		return rn;
	}
	//

	public void ReplaceMobs4(int spawnGroupID, ArrayList<Mob> replacementMobs, FactionBundle helperBundle) {
		int maxMobs = 8;
		ArrayList<Mob> unique = new ArrayList<Mob>();
		// adding rep mobs to a unique arraylist
		for (Mob repMob : replacementMobs) {
			if (!unique.contains(repMob)) {
				unique.add(repMob);
			}
		}

		int totalMobs = Math.min(maxMobs, unique.size()); // take the minum of these two

		try {

			if (!Debug.SKIP_SQL) {
				String statement = " DELETE from spawnentry where spawngroupID=?;";
				PreparedStatement stmt = floatingConnection.prepareStatement(statement);
				stmt.setInt(1, spawnGroupID);
				stmt.executeUpdate();
				for (int i = 0; i < totalMobs; i++) {

					Mob rep = unique.remove(rn.nextInt(unique.size()));

					statement = "INSERT INTO spawnentry VALUES(?,?,?,?);";
					stmt = floatingConnection.prepareStatement(statement);
					stmt.setInt(1, spawnGroupID);
					stmt.setInt(2, rep.getId());
					stmt.setInt(3, 100);
					stmt.setInt(4, 1);
					stmt.executeUpdate();
					MOBS_ADDED_TO_ZONE++;

				}

			}

		} catch (SQLException e) {

			System.out.println("SQL ERROR WRITE ERROR" + e.getMessage());
			// System.exit(0);

		}

	}

	public void ReplaceMobs2(ArrayList<SpawnEntry> entries, ArrayList<Mob> replacementMobs,
			FactionBundle helperBundle) {
		// size of entry
		int totalMobs = entries.size();

		ArrayList<Mob> unique = new ArrayList<Mob>();
		// adding rep mobs to a unique arraylist
		for (Mob repMob : replacementMobs) {
			if (!unique.contains(repMob)) {
				unique.add(repMob);
			}
		}

		int maxCounter = 0;
		while (unique.size() < entries.size() + 2) {
			maxCounter++;
			if (maxCounter > 20) {
				break;
			}
			Mob cMob = helperBundle.getOriginal().GetMobNearLevel(unique.get(0).getLevel());
			if (cMob == null) {
				System.out.println("WE COULD NOT FIND A CLOSE REP");
				System.exit(0);
			}
			boolean stillUnique = true;
			for (Mob uMob : unique) {
				if (uMob.getId() == cMob.getId()) {
					stillUnique = false;
				}
			}
			if (stillUnique) {
				unique.add(cMob);
			}

		}
		int originalUniqueSize = unique.size();

		totalMobs = unique.size();
		int[] chances = Util.GetChancesFor(totalMobs);

		// VARIETY_REPORT[unique.size()]++;

		for (int i = 0; i < entries.size(); i++) {

			try {
				Mob original = GetMobByID(entries.get(i).getNpcId());

				if (unique.size() == 0) {
					continue;
				}
				Mob rep = unique.get(rn.nextInt(unique.size()));

				while (rep == null) {
					unique.remove(rep);
					rep = unique.get(rn.nextInt(unique.size()));
				}

				MOBS_ADDED_TO_ZONE++;
				String statement = "UPDATE spawnentry SET npcID=?,chance=? WHERE spawngroupID=? AND npcID=?";
				PreparedStatement stmt = floatingConnection.prepareStatement(statement);
				// System.out.println("ENTRIES=" + entries.size() + " LOL " +
				// entries.get(i).getId());

				if (!Debug.SKIP_SQL) {

					if (original.getId() != 500) {
						stmt.setInt(1, rep.getId());
						stmt.setInt(2, chances[i]);
						stmt.setInt(3, entries.get(i).getId());
						stmt.setInt(4, entries.get(i).getNpcId());
						stmt.executeUpdate();
					}
				}
				unique.remove(rep);

			} catch (SQLException e) {

				System.out.println("SQL ERROR WRITE ERROR" + e.getMessage());
				// System.exit(0);

			} catch (ArrayIndexOutOfBoundsException ee) {
				System.out.println("CHANCES =" + chances.length + "   TOTAL MOBS=" + totalMobs + "   ENTRIES="
						+ entries.size() + " OG UIQUE " + originalUniqueSize);
				System.exit(0);
			}

		}

		for (int ii = entries.size(); ii < totalMobs; ii++) {

			Mob rep = unique.get(rn.nextInt(unique.size()));

			try {
				int yikesCounter = 0;
				rep = unique.get(rn.nextInt(unique.size()));
				while (rep == null) {
					unique.remove(rep);
					yikesCounter++;
					rep = unique.get(rn.nextInt(unique.size()));
					if (yikesCounter > 100) {
						System.out.println("OOOF");
						System.exit(0);
					}
				}
				String statement = "INSERT INTO spawnentry(spawngroupID,npcID,chance,condition_value_filter) VALUES(?,?,?,?);";
				PreparedStatement stmt = floatingConnection.prepareStatement(statement);
				// System.out.println("ADDING: " + rep);
				if (!Debug.SKIP_SQL) {

					stmt.setInt(1, entries.get(0).getId());
					stmt.setInt(2, rep.getId());
					stmt.setInt(3, chances[ii]);
					stmt.setInt(4, 1);
					stmt.execute();
				} else {
					// System.out.println(rep);
				}
				unique.remove(rep);
				// System.out.println("ADDED " + rep);

			} catch (SQLException e) {

				System.out.println(e.getMessage());

			}
		}

	}

	public void SetLDONZones() {

		if (!Debug.SKIP_SQL) {
			String statement = "UPDATE zone set expansion=1 where expansion=7";
			try {
				PreparedStatement stmt = floatingConnection.prepareStatement(statement);
				stmt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void ReplaceMobs3(int spawnGroupID, Mob rep) {

		try {

			if (!Debug.SKIP_SQL) {
				String statement = " DELETE from spawnentry where spawngroupID=?;";
				PreparedStatement stmt = floatingConnection.prepareStatement(statement);
				stmt.setInt(1, spawnGroupID);
				stmt.executeUpdate();
				statement = "INSERT INTO spawnentry VALUES(?,?,?,?);";
				stmt = floatingConnection.prepareStatement(statement);
				stmt.setInt(1, spawnGroupID);
				stmt.setInt(2, rep.getId());
				stmt.setInt(3, 100);
				stmt.setInt(4, 1);
				stmt.executeUpdate();
				MOBS_ADDED_TO_ZONE++;

				statement = " UPDATE spawn2 set respawntime=20000 where spawngroupID=?;";
				stmt = floatingConnection.prepareStatement(statement);
				stmt.setInt(1, spawnGroupID);
				stmt.executeUpdate();

			}

		} catch (SQLException e) {

			System.out.println("SQL ERROR WRITE ERROR" + e.getMessage());
			// System.exit(0);

		}

	}

	public void ReplaceWithManyMobs(SpawnGroup sg, ArrayList<Mob> normalMobs, ArrayList<Mob> namedMobs, int chance) {
		try {
			ArrayList<Mob> unique = new ArrayList<Mob>();
			int mobNum = Math.min((3 + rn.nextInt(3)), unique.size());
			mobNum = Math.max(1, mobNum);
			// adding rep mobs to a unique arraylist
			for (Mob repMob : normalMobs) {
				if (!unique.contains(repMob)) {
					unique.add(repMob);
				}
			}
			ArrayList<Mob> repMobs = new ArrayList<Mob>();
			for (int i = 0; i < mobNum; i++) {
				if (rn.nextInt(100) < chance && namedMobs.size() > 0) {
					repMobs.add(namedMobs.remove(rn.nextInt(namedMobs.size())));
				} else {
					repMobs.add(unique.remove(rn.nextInt(unique.size())));
				}
			}

			int[] chances = Util.GetChancesFor(mobNum);
			int chanceCounter = 0;
			if (!Debug.SKIP_SQL) {

				String statement = " DELETE from spawnentry where spawngroupID=?;";
				PreparedStatement stmt = floatingConnection.prepareStatement(statement);
				stmt.setInt(1, sg.getId());
				stmt.executeUpdate();

				for (Mob rep : repMobs) {
					statement = "INSERT INTO spawnentry VALUES(?,?,?,?);";
					stmt = floatingConnection.prepareStatement(statement);
					stmt.setInt(1, sg.getId());
					stmt.setInt(2, rep.getId());
					stmt.setInt(3, chances[chanceCounter]);
					stmt.setInt(4, 1);
					stmt.executeUpdate();
					MOBS_ADDED_TO_ZONE++;
					chanceCounter++;
				}
			}

		} catch (SQLException e) {

			System.out.println("SQL ERROR WRITE ERROR" + e.getMessage());
			// System.exit(0);

		}

	}

	public void ReplaceWithOneMob(int spawnGroupID, Mob rep) {

		try {

			if (!Debug.SKIP_SQL) {
				String statement = " DELETE from spawnentry where spawngroupID=?;";
				PreparedStatement stmt = floatingConnection.prepareStatement(statement);
				stmt.setInt(1, spawnGroupID);
				stmt.executeUpdate();
				statement = "INSERT INTO spawnentry VALUES(?,?,?,?);";
				stmt = floatingConnection.prepareStatement(statement);
				stmt.setInt(1, spawnGroupID);
				stmt.setInt(2, rep.getId());
				stmt.setInt(3, 100);
				stmt.setInt(4, 1);
				stmt.executeUpdate();
				MOBS_ADDED_TO_ZONE++;

			}

		} catch (SQLException e) {

			System.out.println("SQL ERROR WRITE ERROR" + e.getMessage());
			// System.exit(0);

		}

	}

}
