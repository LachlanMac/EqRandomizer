package EverquestUtility.Database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.ibatis.jdbc.ScriptRunner;

import EverquestUtility.Database.Helpers.Faction;
import EverquestUtility.Database.Helpers.Mob;
import EverquestUtility.Database.Helpers.NPCFaction;
import EverquestUtility.Database.Helpers.Spawn;
import EverquestUtility.Database.Helpers.SpawnEntry;
import EverquestUtility.Database.Helpers.SpawnGroup;
import EverquestUtility.Database.Helpers.Zone;
import EverquestUtility.FactionBuilder.BuiltFaction;
import EverquestUtility.Util.ComparableFactionObject;
import EverquestUtility.Util.ConfigReader;
import EverquestUtility.Util.Util;

public class EQDao {

	public List<NPCFaction> factions;
	public List<Mob> mobs;
	public List<Mob> raidMobs;
	public ArrayList<Mob> filteredMobs;
	public List<SpawnGroup> spawnGroups;;
	public List<SpawnEntry> spawnEntries;
	public List<Spawn> spawns;
	public List<Zone> zones;
	public List<Faction> primaryFactions;
	public List<Faction> trimmedFactions;

	public int FAILURES = 0;
	public int COMPLETE_FAILURES = 0;
	public int[] VARIETY_REPORT = new int[100];

	private final int MAX_RANDOM_FACTION_CHECK = 500;

	private final int MIN_MOBS_IN_FACTION = 10;
	private Connection floatingConnection;

	public Connection getFloatingConnection() {
		return floatingConnection;
	}

	private final static String CONNECTION_STRING = "jdbc:mysql://localhost/xeq?" + "user=root&password=Movingon1";

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
		raidMobs = new ArrayList<Mob>();
		trimmedFactions = new ArrayList<Faction>();
		filteredMobs = new ArrayList<Mob>();

		try {
			floatingConnection = DriverManager.getConnection(CONNECTION_STRING);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		load();

	}

	public void load() {
		System.out.println("Loading Factions");
		loadFactions();
		loadPrimaryFactions();
		System.out.println("Loading Zones");
		loadZones();
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

		Faction f = GetPrimaryFactionByID(0);

		printReport();

	}

	private void FilterMobs() {

		for (Mob m : mobs) {

			if (!m.FilterMob()) {

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

	private void addMobsToFactions() {

		for (Mob m : filteredMobs) {

			Faction f = GetPrimaryFactionByID(m.getPrimaryFaction().getId());
			f.addMob(m);

		}
		System.out.println("_____FACTIONS______");
		Collections.sort(primaryFactions);

		// for (int i = 0; i < 30; i++) {
		// System.out.println("ID[" + primaryFactions.get(i).getId() + "] " +
		// primaryFactions.get(i).getName() + " C:"
		// + primaryFactions.get(i).getMobs().size());
		// }

		for (SpawnGroup sg : spawnGroups) {
			sg.setPrimaryFaction(this);
		}

		for (Faction f : primaryFactions) {
			if (f.getMobs().size() >= MIN_MOBS_IN_FACTION) {
				f.updateFaction();
				trimmedFactions.add(f);

			}

		}

	}

	public void Rebase() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			ScriptRunner sr = new ScriptRunner(conn);

			BufferedReader reader = new BufferedReader(new FileReader("data/mobs.sql"));
			// Running the script

			conn.close();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				int runspeed = rs.getInt("runspeed");
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

	public void LoadSpawnsFromZones() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			for (Zone z : zones) {
				PreparedStatement stmt = null;
				ResultSet rs = null;
				String statement = "SELECT id,spawngroupID,zone from spawn2 where zone=?";
				stmt = conn.prepareStatement(statement);
				stmt.setString(1, z.getName());
				rs = stmt.executeQuery();
				while (rs.next()) {

					int id = rs.getInt("id");
					int spawngroupID = rs.getInt("spawngroupID");
					String zone = rs.getString("zone");
					Spawn s = new Spawn(id, spawngroupID, zone);
					spawns.add(s);
					z.addSpawn(s);
					AddSpawnGroup(spawngroupID);
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

	public SpawnGroup AddSpawnGroup(int spawnGroupID) {

		for (SpawnGroup s : spawnGroups) {

			if (s.getId() == spawnGroupID)
				return s;

		}

		SpawnGroup sg = new SpawnGroup(spawnGroupID);
		spawnGroups.add(sg);
		return sg;

	}

	public void LoadSpawnGroupsFromZone() {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			for (Zone z : zones) {

				for (Spawn s : z.getSpawns()) {

					PreparedStatement stmt = null;
					ResultSet rs = null;

					String statement = "SELECT id,spawngroupID,zone from spawn2 WHERE spawngroupID=?";
					stmt = conn.prepareStatement(statement);
					stmt.setInt(1, s.getSpawngroupId());
					rs = stmt.executeQuery();
					while (rs.next()) {

						int id = rs.getInt("id");
						int spawngroupID = rs.getInt("spawngroupID");
						String zone = rs.getString("zone");
						spawns.add(new Spawn(id, spawngroupID, zone));
						SpawnGroup sg = AddSpawnGroup(spawngroupID);

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
			spawnGroup.addMob(m);
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

	public ArrayList<SpawnGroup> GetAllSpawnGroupsInZone(Zone zone) {

		Zone z = GetZoneByName(zone.getName());

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

	public ArrayList<Faction> GetPrimaryFactionsByZone(Zone zone) {

		ArrayList<Faction> factions = new ArrayList<Faction>();
		ArrayList<Mob> mobs = GetMobsInZone(zone);

		for (Mob m : mobs) {

			zone.addMob(m);

			if (!factions.contains(m.getPrimaryFaction())) {

				factions.add(m.getPrimaryFaction());

			}

		}

		return factions;

	}

	public ComparableFactionObject GetComparableMobs(Faction faction, ArrayList<Mob> factionMobs, boolean filterAnimals,
			boolean filterKOS) {
		ComparableFactionObject compObj = null;
		ArrayList<Mob> comp = new ArrayList<Mob>();

		int lowest = 99;
		int highest = 1;
		int rLowest = 99;
		int rHighest = 1;
		for (Mob m : factionMobs) {
			if (m.getLevel() < lowest)
				lowest = m.getLevel();
			if (m.getLevel() > highest) {
				highest = m.getLevel();
			}
		}

		Faction likeFaction = null;

		int counter = 0;
		while (likeFaction == null) {
			if (counter >= MAX_RANDOM_FACTION_CHECK) {
				System.out.println("AHHHHHH");
				System.exit(0);
				break;
			}

			Faction randomFaction = trimmedFactions.get(rn.nextInt(trimmedFactions.size()));

			if (randomFaction.getId() != faction.getId()) {
				boolean shouldContinue = true;
				for (int i = 0; i < ConfigReader.REJECTED_FACTION_IDS.length; i++) {

					if (ConfigReader.REJECTED_FACTION_IDS[i] == randomFaction.getId()) {
						if (ConfigReader.REJECTED_FACTION_IDS[i] == 332) {
							System.out.println("HIGHPASS GUARDS WTF");
							System.exit(0);
						}
						shouldContinue = false;
					}

				}
				if (filterAnimals) {
					if (randomFaction.getId() == ConfigReader.ANIMAL_FACTION) {
						shouldContinue = false;
					}
					if (randomFaction.getId() == ConfigReader.BEETLE_FACTION) {
						shouldContinue = false;
					}
				}
				if (filterKOS) {
					if (randomFaction.getId() == ConfigReader.KOS_FACTION) {
						shouldContinue = false;
					}
				}

				if (!shouldContinue)
					continue;
				rLowest = randomFaction.getLowest();
				rHighest = randomFaction.getHighest();
				if (Math.abs(randomFaction.getAverage() - faction.getAverage()) > 20)
					continue;

				// the lowest + 3 4 >= lowest of this one, and the highest is less than the
				// highest of this + 3... that is within range.
				if (lowest + 3 >= rLowest && highest <= rHighest + 3) {
					// this is within range

					for (Mob rm : randomFaction.getMobs()) {

						if (rm.getLevel() >= lowest - 3 && rm.getLevel() <= highest + 3) {

							comp.add(rm);

						}

					}

					if (comp.size() <= 5)
						continue;

					compObj = new ComparableFactionObject(randomFaction, comp, rLowest, rHighest);

					return compObj;

				}

			}

			counter++;
		}

		return compObj;
	}

	public Faction GetRandomLikeFaction(Faction faction) {

		Faction likeFaction = faction;
		Random rn = new Random();

		for (int i = 0; i < 500; i++) {

			Faction randomFaction = trimmedFactions.get(rn.nextInt(trimmedFactions.size()));

			if (randomFaction.getId() == likeFaction.getId()) {
				i++;
			} else {

				if (Faction.CompareFactions(likeFaction, randomFaction)) {

					return randomFaction;
				}
			}

		}
		// System.out.println("No Like Faction found for " + faction.getName());
		return likeFaction;
	}

	public void ReplaceAllFactionInZoneWithFaction(Zone zone, Faction original, Faction rep) {

		ArrayList<SpawnGroup> sgs = GetAllSpawnGroupsInZone(zone);

	}

	public ArrayList<SpawnEntry> GetEntriesInZone(String zone) {
		Zone z = GetZoneByName(zone);

		ArrayList<SpawnEntry> zoneEntries = new ArrayList<SpawnEntry>();

		List<Integer> sgIds = new ArrayList<Integer>();

		for (Spawn s : z.getSpawns()) {

			// WE WANT SPAWN GROUPS...
			if (!sgIds.contains(s.getSpawngroupId()))
				sgIds.add(s.getSpawngroupId());
		}

		for (Integer i : sgIds) {

			SpawnGroup sg = GetSpawnGroupByID(i);

			for (SpawnEntry se : sg.getEntries()) {
				zoneEntries.add(se);
			}

		}

		return zoneEntries;

	}

	public ArrayList<Mob> GetMobsInZone(Zone z) {

		ArrayList<Mob> zoneEntries = new ArrayList<Mob>();

		List<Integer> sgIds = new ArrayList<Integer>();

		for (Spawn s : z.getSpawns()) {
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
					if (m.getName().length() <= 3) {
						System.out.println("ARE YOU FUCKING KIDDING ME?");
						System.exit(0);
					}
					zoneEntries.add(GetMobByID(se.getNpcId()));
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

	public Zone GetZoneByName(String name) {
		Zone z = zones.get(0);

		for (Zone zones : zones) {
			if (zones.getName().equals(name)) {
				return zones;
			}
		}

		return z;

	}

	public Zone GetZoneByID(int id) {
		Zone z = zones.get(0);

		for (Zone zones : zones) {
			if (zones.getId() == id) {
				return zones;
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
			String statement = "SELECT spawngroupID,npcID from spawnentry";
			stmt = conn.prepareStatement(statement);
			rs = stmt.executeQuery();
			while (rs.next()) {

				int spawngroupID = rs.getInt("spawngroupID");
				int npcID = rs.getInt("npcID");
				SpawnEntry se = new SpawnEntry(spawngroupID, npcID);
				SpawnGroup sg = GetSpawnGroupByID(spawngroupID);
				if (sg.getId() == 999999) {

				} else {
					spawnEntries.add(se);
					sg.AddEntry(se);
					sg.addMob(GetMobByID(npcID));
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

	public void loadSpawns() {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String statement = "SELECT id,spawngroupID,zone from spawn2";
			stmt = conn.prepareStatement(statement);
			rs = stmt.executeQuery();
			while (rs.next()) {

				int id = rs.getInt("id");
				int spawngroupID = rs.getInt("spawngroupID");
				String zone = rs.getString("zone");
				spawns.add(new Spawn(id, spawngroupID, zone));

			}
			conn.close();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public void loadMobs() {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String statement = "SELECT id,name,level,merchant_id,npc_faction_id,trap_template,adventure_template_id,qglobal,raid_target,untargetable,runspeed,underwater,race  FROM npc_types";
			stmt = conn.prepareStatement(statement);
			rs = stmt.executeQuery();

			while (rs.next()) {

				String name = rs.getString("name");
				int underwater = rs.getInt("underwater");
				int id = rs.getInt("id");
				int lev = rs.getInt("level");
				int runspeed = rs.getInt("runspeed");
				int merchantID = rs.getInt("merchant_id");
				int factionID = rs.getInt("npc_faction_id");
				int trapID = rs.getInt("trap_template");
				int globalq = rs.getInt("qglobal");
				int adventureID = rs.getInt("adventure_template_id");
				int race = rs.getInt("race");
				int untargetable = rs.getInt("untargetable");
				int raidTarget = rs.getInt("raid_target");
				Mob m = new Mob(id, name, lev, adventureID, trapID, factionID, merchantID, globalq, raidTarget,
						untargetable, race);
				if (runspeed > 0) {
					mobs.add(m);
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

	public void updateSpawnEntriesByZoneAndLevel(ArrayList<SpawnGroup> groups, String zone) {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);

			for (SpawnGroup sg : groups) {

				// if (sg.getZone().equals(zone)) {

				for (SpawnEntry se : sg.getEntries()) {

					Mob m = Mob.GetMobByID(se.getNpcId());

					if (m == null) {
						continue;
					}

					if ((m.getName().charAt(0) == 'a' && m.getName().charAt(1) == '_')
							|| (m.getName().charAt(0) == 'A' && m.getName().charAt(1) == '_')
							|| (m.getName().charAt(0) == 'a' && m.getName().charAt(1) == 'n'
									&& m.getName().charAt(2) == '_')
							|| (m.getName().charAt(0) == 'A' && m.getName().charAt(1) == 'n'
									&& m.getName().charAt(2) == '_')) {

						int replacementLevel = Math.min(Math.max(1, (rn.nextInt(5) - 2) + m.getLevel()), 100);

						ArrayList<Mob> possible = Mob.GetMobsByLevel(replacementLevel);

						Mob random = possible.get(rn.nextInt(possible.size()));
						try {
							String statement = "UPDATE spawnentry SET npcID=? WHERE spawngroupID=?";
							PreparedStatement stmt = conn.prepareStatement(statement);

							stmt.setInt(1, random.getId());
							stmt.setInt(2, se.getId());
							stmt.executeUpdate();
						} catch (SQLException ex) {
							// handle any errors
							System.out.println("SQLException: " + ex.getMessage());
							System.out.println("SQLState: " + ex.getSQLState());
							System.out.println("VendorError: " + ex.getErrorCode());
						}

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

		//

	}

	public ArrayList<SpawnEntry> getSpawnEntryBySpawnGroupID(int sgID, Connection conn) {

		ArrayList<SpawnEntry> entries = new ArrayList<SpawnEntry>();

		try {

			PreparedStatement stmt = null;
			ResultSet rs = null;
			String statement = "SELECT npcID FROM spawnentry where spawngroupID=?";
			stmt = conn.prepareStatement(statement);
			stmt.setInt(1, sgID);
			rs = stmt.executeQuery();

			while (rs.next()) {

				int npcID = rs.getInt("npcID");
				entries.add(new SpawnEntry(sgID, npcID));
			}

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		return entries;

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

	public void CloseFloating() {
		try {
			floatingConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void WriteFaction(BuiltFaction bf) {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);

			String statement = "INSERT INTO faction_list(id,name,base) VALUES(?,?,-30000);";
			// String statement = "UPDATE spawnentry SET npcID=? WHERE spawngroupID=?";
			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, bf.getId());
			stmt.setString(2, bf.getName());
			stmt.execute();

			statement = "INSERT INTO npc_faction(id,name,primaryfaction,ignore_primary_assist) VALUES(?,?,?,0);";
			// String statement = "UPDATE spawnentry SET npcID=? WHERE spawngroupID=?";
			stmt = conn.prepareStatement(statement);
			stmt.setInt(1, bf.getId());
			stmt.setString(2, bf.getName());
			stmt.setInt(3, bf.getId());
			stmt.execute();
			conn.close();

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		//

	}

	public void ReplaceMobs(ArrayList<SpawnEntry> entries, ArrayList<Mob> replacementMobs) {

		ArrayList<Mob> unique = new ArrayList<Mob>();

		for (Mob repMob : replacementMobs) {

			if (!unique.contains(repMob)) {
				unique.add(repMob);
			}

		}
		int totalMobs = entries.size();

		if (entries.size() < 3) {
			totalMobs += rn.nextInt(2) + 1;
		}

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

				Mob rep = unique.get(rn.nextInt(unique.size()));

				ArrayList<Mob> copyMobs = (ArrayList<Mob>) unique.clone();
				System.out.println(copyMobs.size());
				ArrayList<Mob> closestMobs = new ArrayList<Mob>();
				// get highest
				Collections.shuffle(copyMobs);
				boolean gotClose = false;

				for (int xx = 3; xx < 20; xx++) {
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

				if (original.getName().contains("Animation")) {

					Mob highest = unique.get(0);
					for (Mob m : unique) {
						if (m.level > highest.level) {
							highest = m;
						}

					}

					rep = highest;

				}
				unique.remove(rep);

				String statement = "UPDATE spawnentry SET npcID=?,chance=? WHERE spawngroupID=? AND npcID=?";
				PreparedStatement stmt = floatingConnection.prepareStatement(statement);
				// System.out.println("ENTRIES=" + entries.size() + " LOL " +
				// entries.get(i).getId());
				stmt.setInt(1, rep.getId());
				stmt.setInt(2, chances[i]);
				stmt.setInt(3, entries.get(i).getId());
				stmt.setInt(4, entries.get(i).getNpcId());
				stmt.executeUpdate();
				// System.out.println("***************************************\n Replaced "
				// + GetMobByID(entries.get(i).getNpcId()) + " WITH " + rep + "\n");
				// System.out.println("ADDED " + rep);

			} catch (SQLException e) {

				System.out.println(e.getMessage());
				System.exit(0);
			}

		}

		for (int ii = entries.size(); ii < totalMobs; ii++) {

			try {

				if (unique.size() == 0) {
					COMPLETE_FAILURES++;
					return;
				}

				Mob rep = unique.get(rn.nextInt(unique.size()));
				unique.remove(rep);

				String statement = "INSERT INTO spawnentry(spawngroupID,npcID,chance,condition_value_filter) VALUES(?,?,?,?);";
				PreparedStatement stmt = floatingConnection.prepareStatement(statement);
				stmt.setInt(1, entries.get(0).getId());
				stmt.setInt(2, rep.getId());
				stmt.setInt(3, chances[ii]);
				stmt.setInt(4, 1);
				stmt.execute();
				// System.out.println("ADDED " + rep);

			} catch (SQLException e) {

				System.out.println(e.getMessage());
				System.exit(0);

			}
		}

	}

	public ArrayList<Mob> filterMobSet(ArrayList<Mob> mobs) {
		int total = 0;

		for (Mob m : mobs) {
			System.out.println(m);

			total += m.level;
		}
		int average = total / mobs.size();

		ArrayList<Mob> mobsToFilter = new ArrayList<Mob>();

		for (Mob m : mobs) {
			if (m.level - average >= 25) {
				mobsToFilter.add(m);
			}
		}
		for (Mob m : mobsToFilter) {
			mobs.remove(m);
		}
		return mobsToFilter;

	}

	public Random getRandom() {
		return rn;
	}
	//

}
