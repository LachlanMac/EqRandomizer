package EverquestUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import EverquestUtility.Database.EQDao;
import EverquestUtility.Database.Helpers.Faction;
import EverquestUtility.Database.Helpers.Item;
import EverquestUtility.Database.Helpers.LootEntry;
import EverquestUtility.Database.Helpers.Mob;
import EverquestUtility.Database.Helpers.Spawn;
import EverquestUtility.Database.Helpers.SpawnEntry;
import EverquestUtility.Database.Helpers.SpawnGroup;
import EverquestUtility.Database.Helpers.Zone;
import EverquestUtility.FactionBuilder.BuiltFaction;
import EverquestUtility.FactionBuilder.FactionBuilder;
import EverquestUtility.FactionBuilder.FactionBuilder.TYPE;
import EverquestUtility.FactionBuilder.FactionBundle;
import EverquestUtility.Util.ConfigReader;
import EverquestUtility.Util.Debug;
import EverquestUtility.Util.Util;
import EverquestUtility.discord.DiscordBot;

public class Launcher {

	public enum ZONE_TYPE {
		NOOB, DUNGEON, OUTDOOR
	}

	public static ArrayList<Zone> dungeonZones = new ArrayList<Zone>();
	public static ArrayList<Zone> noobZones = new ArrayList<Zone>();
	public static ArrayList<Zone> outdoorZones = new ArrayList<Zone>();

	public static ArrayList<Faction> approvedList = new ArrayList<Faction>();
	public static Random rn = new Random();

	public static void main(String[] args) {
		ConfigReader.ReadConfig();
		// DiscordBot dBot = DiscordBot.GetInstance();
		Debug.SKIP_SQL = false;
		EQDao dao = new EQDao();
		// dao.PrintRaidMobs();
		// dBot.initJDA(dao);
		// dao.findItem("ancient snake");

		buildServer(dao);

		dao.CloseFloating();
		System.out.println("Finished!");
	}

	public static void replaceZones(EQDao dao) {
		ReplaceOutdoorZones(dao);
		ReplaceDungeons(dao);
		ReplaceNoobZones(dao);
	}

	public static void buildServer(EQDao dao) {
		dao.SetLDONZones();
		dao.WriteRaidFaction();
		dao.GetLevelOneRaidMobs("guka");
		dao.GetLevelOneRaidMobs("mira");
		dao.GetLevelOneRaidMobs("mmca");
		dao.GetLevelOneRaidMobs("ruja");
		replaceZones(dao);
		replaceLdonZones(dao);
		setZoneExp(dao);
		setHotzones(dao);
	}

	public static void replaceLdonZones(EQDao dao) {

		for (Zone z : dao.ldonZones) {
			System.out.println(z.getName() + "  " + z.getLongName() + "  " + z.getId());
		}
		// z.getSpawnGroups()

	}

	public static void PrintAllFactions(EQDao dao) {
		ArrayList<Faction> fs = (ArrayList<Faction>) dao.trimmedFactions;
		Collections.sort(fs);
		for (Faction f : fs) {
			System.out.println("ID[" + f.getId() + "] " + f.getName() + "   Mob Count:" + f.getMobs().size());
		}

	}

	public static void AddApprovedFaction(Faction f) {
		if (!approvedList.contains(f)) {
			approvedList.add(f);
		}
	}

	public static void ReplaceZone(Zone zone, EQDao dao, int namedChance, TYPE type) {

		System.out.println("\n  ***  Replacing Zone:" + zone.getName() + "[" + zone.getId() + "] ***");
		// lets get all the factions in the zone and figure out which ones we are going
		// to replace
		Random rn = new Random();
		boolean DEBUG_NOOB_ZONE = true;
		ArrayList<FactionBundle> factions = new ArrayList<FactionBundle>();
		if (type == TYPE.NOOB) {
			for (Faction f : dao.GetPrimaryFactionsByZone(zone)) {
				for (int i = 0; i < ConfigReader.NOOB_REPLACE_FACTION_IDS.length; i++) {
					if (ConfigReader.NOOB_REPLACE_FACTION_IDS[i] == f.getId()) {
						factions.add(new FactionBundle(f));
					}
				}
			}
		} else if (type == TYPE.OUTSIDE) {

			for (Faction f : dao.GetPrimaryFactionsByZone(zone)) {
				boolean skippedFaction = false;
				for (int i = 0; i < ConfigReader.OUTDOOR_SKIP.length; i++) {
					if (ConfigReader.OUTDOOR_SKIP[i] == f.getId()) {
						skippedFaction = true;
						break;
					}
				}
				if (!skippedFaction) {
					factions.add(new FactionBundle(f));
				} else {
					System.out.println("**** Skipped faction " + f.getName());
				}
			}

		}
		// we now have the factions we want to replace, lets replace them logically...
		for (FactionBundle fb : factions) {
			ArrayList<Mob> mobsInFactionInZone = dao.FilterMobsByFaction(zone, fb.getOriginal());

			fb.setReplacement(BuildFaction(dao, zone, type, 0, mobsInFactionInZone));

			// System.out.println(fb + " ID[" + fb.getOriginal().getId() + "]");

		}

		// lets just straight replace every spawn group in the zone and get every mob in
		// that spawn group.

		for (SpawnGroup sg : dao.GetAllSpawnGroupsInZone(zone, false)) {

			// Sanity check for mobs we really dont want to mess with...
			if (sg.isForbiddenSpawngroup()) {
				System.out.println("Skipping Translocator");
				continue;
			}

			if (sg.isWaterSpawn()) {

				int level = 0;
				for (Mob m : sg.getMobs()) {
					level += m.getLevel();
				}

				Mob mmm = dao.getWaterMobAroundLevel(level / sg.getMobs().size());
				mmm.factionID = 0;
				mmm.setId(FactionBuilder.CURRENT_NPC_ID);
				mmm.writeMob(dao.getFloatingConnection());
				// System.out.println("ADDING SPECIALTY : " + mmm.getName() + ":" +
				// mmm.getLevel());
				dao.ReplaceWithOneMob(sg.getId(), mmm);
				continue;
			}

			FactionBundle lastFaction = null;
			ArrayList<Mob> replacementMobs = new ArrayList<Mob>();
			for (Mob m : sg.getMobs()) {
				// lets just replace each mob based on faction
				FactionBundle f = Util.GetPossibleFaction(m, factions);

				if (f != null) {
					if (f.getOriginal().getId() == 0) {
						{
							if (m.AllowedNoobMobs()) {
								Mob rep = null;
								// replace noob mob...
								if (m.underwater != 0) {// random fish
									rep = dao.getWaterMobAroundLevel(m.level);
									rep.factionID = 0;
									rep.setId(FactionBuilder.CURRENT_NPC_ID);
									rep.writeMob(dao.getFloatingConnection());
									dao.ReplaceWithOneMob(sg.getId(), rep);
								} else {// something in the faction
									rep = f.getReplacement().GetMobNearLevel(m.getLevel());
									rep.factionID = 0;
									rep.setId(FactionBuilder.CURRENT_NPC_ID);
									rep.writeMob(dao.getFloatingConnection());
									dao.ReplaceWithOneMob(sg.getId(), rep);

								}
								// System.out.println("REPLACING " + m.getName() + " with " + rep.getName());
							}
						}
					} else {
						lastFaction = f;
						// we will now replace the mob with a faction approved mob...
						// System.out.println("Replacing " + m.getName() + " with a mob from " +
						// f.getReplacement().getName());

						// replacementMobs.add(e)

						ArrayList<Mob> closeMobs = new ArrayList<Mob>();
						// if any of the replacements are close in level...

						for (Mob r : f.getReplacement().getNormalMobs()) {
							if (Math.abs(r.getLevel() - m.getLevel()) <= 2) {
								if (r != null)
									closeMobs.add(r);
							}
						}
						if (closeMobs.size() < 4 || sg.getTotalSpawns() >= 4) {
							for (Mob r : f.getReplacement().getNormalMobs()) {
								if (Math.abs(r.getLevel() - m.getLevel()) <= 4
										&& Math.abs(r.getLevel() - m.getLevel()) > 2) {
									if (r != null)
										closeMobs.add(r);
								}
							}
						}
						if (closeMobs.size() == 0) {
							for (Mob r : f.getReplacement().getNormalMobs()) {
								if (Math.abs(r.getLevel() - m.getLevel()) <= 5) {
									if (r != null)
										closeMobs.add(r);
								}
							}
						}
						if (closeMobs.size() == 0) {
							for (Mob r : f.getReplacement().getNormalMobs()) {
								if (Math.abs(r.getLevel() - m.getLevel()) <= 10) {
									if (r != null)
										closeMobs.add(r);
								}
							}
						}
						if (closeMobs.size() == 0) {
							for (Mob r : f.getReplacement().getNormalMobs()) {
								if (Math.abs(r.getLevel() - m.getLevel()) <= 50) {
									if (r != null) {
										closeMobs.add(r);
									}
								}
							}
						}
						if (closeMobs.size() == 0) {
							System.out.println("  #########   Couldnt replace " + m.getName() + ":::" + m.getLevel());
							System.out.println("MOBS IN FACTION:::" + f.getReplacement().getMobs().size() + " NAMED:"
									+ f.getReplacement().getNamedMobs().size() + " NORMAL:"
									+ f.getReplacement().getNormalMobs().size());

							for (Mob aMob : f.getReplacement().getMobs()) {
								System.out.println(aMob.getName() + " " + aMob.getLevel() + " " + aMob.getRace());
							}

							System.exit(0);

						} else {

							Mob replacementMob;
							if (rn.nextInt(100) <= namedChance) {
								replacementMob = f.getReplacement().getNamedMobNearLevel(m.getLevel());
								if (replacementMob != null)
									replacementMobs.add(replacementMob);
								replacementMob = f.getReplacement().getNamedMobNearLevel(m.getLevel());
								if (replacementMob != null)
									replacementMobs.add(replacementMob);
							} else {
								replacementMob = closeMobs.get(rn.nextInt(closeMobs.size()));
								if (replacementMob != null)
									replacementMobs.add(replacementMob);
								replacementMob = closeMobs.get(rn.nextInt(closeMobs.size()));
								if (replacementMob != null)
									replacementMobs.add(replacementMob);
							}

							// System.out.println("Replacing " + m.getName() + " with " +
							// replacementMob.getName());

						}
					}

				} else {
					// add the original mob because we don't want to change it
					// replacementMobs.add(m);
				}

			}
			// end of spawn group
			// we now have a list of mobs to write
			if (replacementMobs.size() > 0) {
				dao.ReplaceMobs4(sg.getId(), replacementMobs, lastFaction);
			}
		}
	}

	public static void ReplaceDungeon(Zone zone, EQDao dao) {
		int totalLevels = 0;
		int totalMobs = 0;
		ArrayList<Mob> originalMobsInZone = dao.GetMobsInZone(zone);
		BuiltFaction f = BuildFaction(dao, zone, FactionBuilder.TYPE.DUNGEON, 0, originalMobsInZone);
		System.out.println(
				"Bulding Faction for " + zone.getName() + " with a total mob count of " + originalMobsInZone.size());
		for (Mob m : f.GetMobsToWrite()) {
			m.factionID = f.getId();
			m.setId(FactionBuilder.CURRENT_NPC_ID);
			m.writeMob(dao.getFloatingConnection());
		}

		for (SpawnGroup sg : dao.GetAllSpawnGroupsInZone(zone, false)) {
			ArrayList<Mob> replacementMobs = new ArrayList<Mob>();
			sg.checkForDiscrepencies();
			for (Mob m : sg.getMobs()) {

				if (m.getId() == 500) {
					continue;
				}
				// Create array list of mobs that are close
				ArrayList<Mob> closeMobs = new ArrayList<Mob>();
				// if any of the replacements are close in level...

				for (Mob r : f.GetMobsToWrite()) {
					if (Math.abs(r.getLevel() - m.getLevel()) <= 2)
						closeMobs.add(r);

				}
				if (closeMobs.size() == 0 || sg.getTotalSpawns() >= 6) {
					for (Mob r : f.GetMobsToWrite()) {
						if (Math.abs(r.getLevel() - m.getLevel()) <= 4 && Math.abs(r.getLevel() - m.getLevel()) > 2) {

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
						if (Math.abs(r.getLevel() - m.getLevel()) <= 7) {
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

				for (Mob m : replacementMobs) {
					totalLevels += m.level;
					totalMobs++;

				}
				int average = totalLevels / totalMobs;
				ArrayList<Mob> namedMobsToAdd = new ArrayList<Mob>();
				for (Mob m : dao.GetBalancedNamed(8, average, average + 15)) {
					Mob mm = f.convertToNamedMonster(m);
					mm.setId(FactionBuilder.CURRENT_NPC_ID);
					mm.writeMob(dao.getFloatingConnection());
					// System.out.println(mm);
					namedMobsToAdd.add(mm);
				}

				dao.ReplaceWithManyMobs(sg, replacementMobs, namedMobsToAdd, 22);
				// dao.ReplaceMobs(sg.getEntries(), replacementMobs, namedMobsToAdd, 22f,
				// sg.getId());
			}

		}

		// lets write the named...

	}

	public static BuiltFaction BuildFaction2(EQDao dao, Faction faction, FactionBuilder.TYPE ftype, int override,
			Zone zone) {

		FactionBuilder fb;
		ArrayList<Mob> originalMobs = faction.getMobs();
		int high = 1, low = 99;

		for (Mob m : originalMobs) {
			if (m.level > high) {
				high = m.level;
			}
			if (m.level < low) {
				low = m.level;
			}
		}

		BuiltFaction f = null;

		boolean anyFalse = true;
		int counter = 0;
		while (anyFalse == true) {
			fb = new FactionBuilder();
			f = fb.BuildFaction(ftype, zone, dao, override);
			counter++;
			try {
				f.getLowest();
				f.getHighest();
			} catch (Exception e) {
				continue;
			}

			if (counter >= 20) {
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
		f.SetNames();

		dao.WriteFaction(f);
		for (Mob m : f.GetMobsToWrite()) {
			m.factionID = f.getId();
			m.setId(FactionBuilder.CURRENT_NPC_ID);
			m.writeMob(dao.getFloatingConnection());
		}
		return f;
	}

	public static BuiltFaction BuildFaction(EQDao dao, Zone zone, FactionBuilder.TYPE ftype, int override,
			ArrayList<Mob> ogMobs) {
		FactionBuilder fb;
		ArrayList<Mob> originalMobsInZone = dao.GetMobsInZone(zone);
		int high = 1, low = 99;

		for (Mob m : originalMobsInZone) {
			if (m.level > high) {
				high = m.level;
			}
			if (m.level < low) {
				low = m.level;
			}
		}

		BuiltFaction f = null;

		boolean anyFalse = true;
		int counter = 0;
		while (anyFalse == true) {
			fb = new FactionBuilder();
			f = fb.BuildFaction(ftype, zone, dao, override);
			counter++;

			boolean validFaction = true;

			try {
				f.getLowest();
				f.getHighest();
			} catch (Exception e) {
				validFaction = false;
				continue;
			}

			if (counter >= 20) {
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
		f.SetNames();

		dao.WriteFaction(f);
		for (Mob m : f.GetMobsToWrite()) {
			m.factionID = f.getId();
			m.setId(FactionBuilder.CURRENT_NPC_ID);
			m.writeMob(dao.getFloatingConnection());
		}
		return f;
	}

	public static void setZoneExp(EQDao dao) {

		for (Zone dungeon : dungeonZones) {
			dao.addDungeonExp(dungeon);
		}
		for (Zone outdoor : outdoorZones) {
			dao.addExp(outdoor, 1.2f);
		}
		for (Zone noob : noobZones) {
			dao.addExp(noob, 1.2f);
		}
	}

	public static void setHotzones(EQDao dao) {

		for (int i = 0; i < 6; i++) {
			Zone z = dungeonZones.remove(rn.nextInt(dungeonZones.size()));
			dao.addHotzone(z);
		}

		for (int i = 0; i < 6; i++) {
			Zone z = outdoorZones.remove(rn.nextInt(outdoorZones.size()));
			dao.addHotzone(z);
		}

	}

	public static void ReplaceDungeons(EQDao dao) {

		for (Zone zone : dao.zones) {

			for (int i = 0; i < ConfigReader.DUNGEON_ZONE_IDS.length; i++) {
				if (zone.getId() == ConfigReader.DUNGEON_ZONE_IDS[i]) {
					dungeonZones.add(zone);
					ReplaceDungeon(zone, dao);

					// String sca = s.nextLine();
				}
			}
		}

	}

	public static void ReplaceNoobZones(EQDao dao) {

		for (Zone zone : dao.zones) {

			for (int i = 0; i < ConfigReader.ZONE_IDS.length; i++) {
				if (zone.getId() == ConfigReader.ZONE_IDS[i]) {
					// if (zone.getName().contentEquals("gfaydark")) {
					noobZones.add(zone);
					ReplaceZone(zone, dao, 10, TYPE.NOOB);

					// }
				}
			}
		}

	}

	public static void ReplaceOutdoorZones(EQDao dao) {

		ArrayList<Integer> is = getOutdoorZoneIds(dao);

		for (Zone zone : dao.zones) {

			for (int i = 0; i < is.size(); i++) {
				if (zone.getId() == is.get(i)) {
					outdoorZones.add(zone);
					ReplaceZone(zone, dao, 15, TYPE.OUTSIDE);
				}
			}
		}
	}

	public static ArrayList<Integer> getOutdoorZoneIds(EQDao dao) {

		ArrayList<Integer> outsideIds = new ArrayList<Integer>();

		for (Zone zone : dao.zones) {
			boolean valid = true;
			for (int i = 0; i < ConfigReader.ZONE_IDS.length; i++) {
				if (zone.getId() == ConfigReader.ZONE_IDS[i]) {
					// System.out.println("NOT VALID: " + zone.getLongName());
					valid = false;
				}
			}
			for (int i = 0; i < ConfigReader.DUNGEON_ZONE_IDS.length; i++) {
				if (zone.getId() == ConfigReader.DUNGEON_ZONE_IDS[i]) {
					// System.out.println("NOT VALID: " + zone.getLongName());
					valid = false;
				}
			}
			for (int i = 0; i < ConfigReader.RAID_ZONE_IDS.length; i++) {
				if (zone.getId() == ConfigReader.RAID_ZONE_IDS[i]) {
					// System.out.println("NOT VALID: " + zone.getLongName());
					valid = false;
				}
			}

			for (int i = 0; i < ConfigReader.NON_COMBAT_ZONE_IDS.length; i++) {
				if (zone.getId() == ConfigReader.NON_COMBAT_ZONE_IDS[i]) {
					// System.out.println("NOT VALID: " + zone.getLongName());
					valid = false;
				}
			}
			if (valid) {
				outsideIds.add(zone.getId());
			}

		}

		return outsideIds;

	}

	public static void BuildWeeklyEncounters(EQDao dao) {

	}
}
