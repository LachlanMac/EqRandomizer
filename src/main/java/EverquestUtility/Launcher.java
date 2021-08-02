package EverquestUtility;

import java.util.ArrayList;
import java.util.Collections;

import EverquestUtility.Database.EQDao;
import EverquestUtility.Database.Helpers.Faction;
import EverquestUtility.Database.Helpers.Mob;
import EverquestUtility.Database.Helpers.SpawnGroup;
import EverquestUtility.Database.Helpers.Zone;
import EverquestUtility.FactionBuilder.BuiltFaction;
import EverquestUtility.FactionBuilder.FactionBuilder;
import EverquestUtility.Util.ComparableFactionObject;
import EverquestUtility.Util.ConfigReader;
import EverquestUtility.Util.Util;

public class Launcher {

	public static ArrayList<Faction> approvedList = new ArrayList<Faction>();

	public static void main(String[] args) {
		ConfigReader.ReadConfig();

		EQDao dao = new EQDao();

		// dao.Rebase();

		// dao.WriteFaction(f);
		// ReplaceNoobZones(dao);
		// ReplaceMonsters(dao);
		ReplaceDungeons(dao);
		dao.CloseFloating();
	}

	public static void PrintAllFactions(EQDao dao) {
		ArrayList<Faction> fs = (ArrayList<Faction>) dao.trimmedFactions;
		Collections.sort(fs);
		for (Faction f : fs) {
			System.out.println("ID[" + f.getId() + "] " + f.getName() + "   Mob Count:" + f.getMobs().size());
		}

	}

	public static void printAllLevels(ArrayList<Mob> factionMobs) {
		for (Mob m : factionMobs) {
			System.out.println(m);
		}
		System.out.print("\n");
	}

	public static void AddApprovedFaction(Faction f) {
		if (!approvedList.contains(f)) {
			approvedList.add(f);
		}
	}

	public static void ReplaceNoobZone(Zone zone, EQDao dao) {

		System.out.println("____ZONE " + zone.getId() + ":" + zone.getName() + "_____\n");
		ArrayList<SpawnGroup> sgs = dao.GetAllSpawnGroupsInZone(zone);
		for (Faction f : dao.GetPrimaryFactionsByZone(zone)) {

			boolean approved = false;

			for (int i = 0; i < ConfigReader.NOOB_REPLACE_FACTION_IDS.length; i++) {
				if (ConfigReader.NOOB_REPLACE_FACTION_IDS[i] == f.getId()) {
					// System.out.println(f.getName() + " is approved!");
					approved = true;
				}
			}

			if (!approved) {

				continue;
			}

			boolean randomReplaceFaction = false;

			for (int i = 0; i < ConfigReader.NOOB_REPLACE_FACTION_IDS.length; i++) {
				if (ConfigReader.NOOB_REP_RANDOM_IDS[i] == f.getId()) {
					randomReplaceFaction = true;
				}
			}

			ArrayList<Mob> mobsInFactionInZone = dao.FilterMobsByFaction(zone, f);

			ArrayList<Mob> mobsToFilter = dao.filterMobSet(mobsInFactionInZone);
			int high = 1, low = 99, total = 0;

			for (Mob m : mobsInFactionInZone) {
				if (m.level > high) {
					high = m.level;
				}
				if (m.level < low) {
					low = m.level;
				}
				total += m.level;
			}

			if (randomReplaceFaction) {

			} else {

			}

			BuiltFaction bf = BuildFaction(dao, zone, high, low, FactionBuilder.TYPE.NOOB, 0);

		}
		// System.out.println("FUCKUPS " + fuckups);

	}

	public static void ReplaceDungeon(Zone zone, EQDao dao) {
		System.out.println("____ZONE " + zone.getId() + ":" + zone.getName() + "_____");

		ArrayList<Mob> originalMobsInZone = dao.GetMobsInZone(zone);

		ArrayList<Mob> mobsToFilter = dao.filterMobSet(originalMobsInZone);
		int high = 1, low = 99, total = 0;

		for (Mob m : originalMobsInZone) {
			if (m.level > high) {
				high = m.level;
			}
			if (m.level < low) {
				low = m.level;
			}
			total += m.level;
		}

		// FILTER MOBS

		BuiltFaction f = BuildFaction(dao, zone, high, low, FactionBuilder.TYPE.DUNGEON, 0);

		for (Mob m : f.GetMobsToWrite()) {
			m.factionID = f.getId();
			m.setId(FactionBuilder.CURRENT_NPC_ID);
			FactionBuilder.CURRENT_NPC_ID++;
			m.writeMob(dao.getFloatingConnection());
		}

		for (SpawnGroup sg : dao.GetAllSpawnGroupsInZone(zone)) {
			ArrayList<Mob> replacementMobs = new ArrayList<Mob>();
			for (Mob m : sg.getMobs()) {

				if (m.getName().contains("Animation")) {
					continue;
				}

				// Create array list of mobs that are close
				ArrayList<Mob> closeMobs = new ArrayList<Mob>();
				// if any of the replacements are close in level...

				for (Mob r : f.GetMobsToWrite()) {
					if (mobsToFilter.contains(m)) {

					} else if (Math.abs(r.getLevel() - m.getLevel()) <= 2) {
						closeMobs.add(r);
					}
				}
				if (closeMobs.size() == 0) {
					for (Mob r : f.GetMobsToWrite()) {
						if (Math.abs(r.getLevel() - m.getLevel()) <= 3) {

							closeMobs.add(r);
						}
					}
				}
				if (closeMobs.size() == 0) {
					for (Mob r : f.GetMobsToWrite()) {
						if (Math.abs(r.getLevel() - m.getLevel()) <= 5) {

							closeMobs.add(r);
						}
					}
				}
				if (closeMobs.size() == 0) {
					for (Mob r : f.GetMobsToWrite()) {
						if (Math.abs(r.getLevel() - m.getLevel()) <= 10) {
							closeMobs.add(r);
						}
					}
				}

				if (closeMobs.size() == 0) {

				}

				for (Mob cm : closeMobs) {
					// System.out.println("ADDING " + cm);
					replacementMobs.add(cm);
				}

			}
			if (replacementMobs.size() > 0) {
				dao.ReplaceMobs(sg.getEntries(), replacementMobs);
			}

		}

	}

	public static BuiltFaction BuildFaction(EQDao dao, Zone zone, int high, int low, FactionBuilder.TYPE ftype,
			int override) {
		FactionBuilder fb = new FactionBuilder();

		BuiltFaction f = null;

		boolean anyFalse = true;
		int counter = 0;
		while (anyFalse == true) {
			f = fb.BuildFaction(FactionBuilder.TYPE.DUNGEON, dao.GetZoneByID(zone.getId()), dao, 0);
			counter++;

			if (counter >= 5) {
				System.out.println("ERROR IN OMEGA WAY");

				System.exit(0);
			}

			if (f.getLowest() > low) {
				// if its higher, lets make sure it is within range that is accetable...
				if (f.getLowest() - low >= 5) {
					anyFalse = true;
					continue;
				}
			}

			if (f.getHighest() < high) {
				if (high - f.getHighest() <= 5) {

					anyFalse = true;
					continue;
				}
			}

			anyFalse = false;
		}

		f.setUnique();

		dao.WriteFaction(f);
		return f;
	}

	public static BuiltFaction BuildFaction(EQDao dao, ArrayList<Mob> originalMobsInZone, Zone zone) {

		FactionBuilder fb = new FactionBuilder();
		ArrayList<Mob> mobsToFilter = dao.filterMobSet(originalMobsInZone);
		int high = 1, low = 99, total = 0;

		for (Mob m : originalMobsInZone) {
			if (m.level > high) {
				high = m.level;
			}
			if (m.level < low) {
				low = m.level;
			}
			total += m.level;
		}
		int average = total / originalMobsInZone.size();

		// FILTER MOBS

		BuiltFaction f = null;

		boolean anyFalse = true;
		int counter = 0;
		while (anyFalse == true) {
			f = fb.BuildFaction(FactionBuilder.TYPE.DUNGEON, dao.GetZoneByID(zone.getId()), dao, 0);
			counter++;

			if (counter >= 5) {
				System.out.println("ERROR IN OMEGA WAY");

				System.exit(0);
			}

			if (f.getLowest() > low) {
				// if its higher, lets make sure it is within range that is accetable...
				if (f.getLowest() - low >= 5) {
					anyFalse = true;
					continue;
				}
			}

			if (f.getHighest() < high) {
				if (high - f.getHighest() <= 5) {

					anyFalse = true;
					continue;
				}
			}

			anyFalse = false;
		}

		f.setUnique();

		dao.WriteFaction(f);

		return f;

	}

	public static void ReplaceDungeons(EQDao dao) {
		for (Zone zone : dao.zones) {

			for (int i = 0; i < ConfigReader.DUNGEON_ZONE_IDS.length; i++) {
				if (zone.getId() == ConfigReader.DUNGEON_ZONE_IDS[i]) {
					// if (zone.getName().contains("crush"))
					ReplaceDungeon(zone, dao);
				}
			}

		}
	}

	public static void ReplaceNoobZones(EQDao dao) {

		for (Zone zone : dao.zones) {

			for (int i = 0; i < ConfigReader.ZONE_IDS.length; i++) {
				if (zone.getId() == ConfigReader.ZONE_IDS[i]) {
					ReplaceNoobZone(zone, dao);
				}
			}

		}

	}
}
