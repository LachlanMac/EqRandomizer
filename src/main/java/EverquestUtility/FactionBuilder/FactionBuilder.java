package EverquestUtility.FactionBuilder;

import java.util.ArrayList;
import java.util.Random;

import EverquestUtility.Database.EQDao;
import EverquestUtility.Database.Helpers.Zone;
import EverquestUtility.Util.ConfigReader;
import EverquestUtility.Util.Debug;

public class FactionBuilder {

	private int high, low, average;
	private final int[] FACTION_VALUES = new int[] { -1200, -800, -400, 0, 200, 600, 800, 1400 };
	public static int FACTION_COUNTER = 0;

	public FactionBuilder() {
		rn = new Random();

	}

	public String getName(Zone z, RACE r) {

		String name = "The ";
		switch (r) {
		case ANIMAL:
			name += ANIMAL_NAMES[rn.nextInt(ANIMAL_NAMES.length)];
			break;
		case BUG:
			name += BUG_NAMES[rn.nextInt(BUG_NAMES.length)];
			break;
		case GIANT:
			name += GIANT_NAMES[rn.nextInt(GIANT_NAMES.length)];
			break;
		case GOLEM:
			name += GOLEM_NAMES[rn.nextInt(GOLEM_NAMES.length)];
			break;
		case MONSTER:
			name += MONSTER_NAMES[rn.nextInt(MONSTER_NAMES.length)];
			break;
		case PLANT:
			name += PLANT_NAMES[rn.nextInt(PLANT_NAMES.length)];
			break;
		case UNDEAD:
			name += UNDEAD_NAMES[rn.nextInt(UNDEAD_NAMES.length)];
			break;
		case VERMIN:
			name += VERMIN_NAMES[rn.nextInt(VERMIN_NAMES.length)];
			break;
		case FEY:
			name += FEY_NAMES[rn.nextInt(FEY_NAMES.length)];
			break;
		case BANDITS:
			name += BANDITS_NAMES[rn.nextInt(BANDITS_NAMES.length)];
			break;
		case MONSTROSITY:
			name += "Abominations";
		default:
			name += "Creatures";
			break;
		}

		FACTION_COUNTER++;
		name += " of " + z.getName() + "  " + FACTION_COUNTER;
		return name;

	}

	private void buildArray(int[] list, int count, ArrayList<Integer> raceIDs) {
		Random rn = new Random();
		ArrayList<Integer> indexes = new ArrayList<>(); // creates list of indexes
		for (int i : list) {
			indexes.add(i);
		}
		for (int j = 0; j < count; j++) {
			raceIDs.add(indexes.remove(rn.nextInt(indexes.size())));
		}

	}

	public BuiltFaction BuildFaction(TYPE t, Zone z, EQDao dao, int FORCE) {
		BuiltFaction bf = null;
		ArrayList<Integer> race_ids = new ArrayList<Integer>();
		ArrayList<Integer> unique;
		switch (t) {

		case DUNGEON:
			DUNGEON_TYPE dt;
			if (FORCE == 0) {
				dt = DUNGEON_TYPE.values()[rn.nextInt(DUNGEON_TYPE.values().length)];
			} else {
				dt = DUNGEON_TYPE.values()[FORCE + 1];
			}
			switch (dt) {
			case LIVE_WITH_GOLEM:
				race_ids.add(ConfigReader.DUNGEON_MONSTER_RACES[rn.nextInt(ConfigReader.DUNGEON_MONSTER_RACES.length)]);
				buildArray(ConfigReader.GOLEM_RACES, 2, race_ids);
				// race_ids.add(ConfigReader.GOLEM_RACES[rn.nextInt(ConfigReader.GOLEM_RACES.length)]);
				// race_ids.add(ConfigReader.GOLEM_RACES[rn.nextInt(ConfigReader.GOLEM_RACES.length)]);
				// race_ids.add(ConfigReader.GOLEM_RACES[rn.nextInt(ConfigReader.GOLEM_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.GOLEM), race_ids, dao, z,
						FACTION_VALUES[1], true);
				bf.updateFaction();

				break;
			case LIVE_WITH_SECONDARY:
				race_ids.add(ConfigReader.DUNGEON_MONSTER_RACES[rn.nextInt(ConfigReader.DUNGEON_MONSTER_RACES.length)]);
				buildArray(ConfigReader.MINOR_DUNGEON_RACES, 2, race_ids);
				// race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				// race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				// race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z,
						FACTION_VALUES[1], true);
				bf.updateFaction();
				break;
			case ELEMENTAL_CHAOS:
				buildArray(ConfigReader.ELEMENTAL_RACES, 3, race_ids);
				// race_ids.add(ConfigReader.ELEMENTAL_RACES[rn.nextInt(ConfigReader.ELEMENTAL_RACES.length)]);
				// race_ids.add(ConfigReader.ELEMENTAL_RACES[rn.nextInt(ConfigReader.ELEMENTAL_RACES.length)]);
				// race_ids.add(ConfigReader.ELEMENTAL_RACES[rn.nextInt(ConfigReader.ELEMENTAL_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z,
						FACTION_VALUES[1], true);
				System.out.println("BUILDING MAGE FACTION IN ZONE: " + z.getLongName() + "!!!!!]\n");
				bf.buildElementalChaos();
				bf.updateFaction();
				break;
			case NECROMANCER:
				buildArray(ConfigReader.UNDEAD_RACES, 3, race_ids);
				// race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				// race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				// race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.UNDEAD), race_ids, dao, z,
						FACTION_VALUES[0], true);
				System.out.println("BUILDING NECRO FACTION IN ZONE: " + z.getLongName() + "!!!!!]\n");
				bf.buildNecros();
				bf.updateFaction();
				break;
			case PURE_UNDEAD:
				buildArray(ConfigReader.UNDEAD_RACES, 5, race_ids);
				// race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				// race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				// race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				// race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.UNDEAD), race_ids, dao, z,
						FACTION_VALUES[0], true);
				bf.updateFaction();
				break;
			case MONSTERS_AND_THIEVES:
				race_ids.add(ConfigReader.MONSTER_RACES[rn.nextInt(ConfigReader.MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.VERMIN), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(1) + 1], true);
				bf.buildBandits();
				bf.updateFaction();
			case UNDEAD_WITH_MONSTERS:
				buildArray(ConfigReader.UNDEAD_RACES, 3, race_ids);
				// race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				// race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				// race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				buildArray(ConfigReader.MINOR_DUNGEON_RACES, 2, race_ids);

				// race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				// race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				// race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.UNDEAD), race_ids, dao, z,
						FACTION_VALUES[0], true);
				bf.updateFaction();
				break;
			case UNDEAD_LIVE_MIX:
				buildArray(ConfigReader.UNDEAD_RACES, 3, race_ids);
				// race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				// race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				// race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				buildArray(ConfigReader.DUNGEON_MONSTER_RACES, 2, race_ids);
				// race_ids.add(ConfigReader.DUNGEON_MONSTER_RACES[rn.nextInt(ConfigReader.DUNGEON_MONSTER_RACES.length)]);
				// race_ids.add(ConfigReader.DUNGEON_MONSTER_RACES[rn.nextInt(ConfigReader.DUNGEON_MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.UNDEAD), race_ids, dao, z,
						FACTION_VALUES[1], true);
				bf.updateFaction();
				break;
			case LIVE_WITH_ANIMALS:
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.DUNGEON_MONSTER_RACES[rn.nextInt(ConfigReader.DUNGEON_MONSTER_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_MONSTER_RACES[rn.nextInt(ConfigReader.ANIMAL_MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z,
						FACTION_VALUES[1], true);
				bf.updateFaction();
				break;
			case FEY:
				buildArray(ConfigReader.FEY_RACES, 3, race_ids);

				// race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				// race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				// race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				// race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);

				buildArray(ConfigReader.PLANT_RACES, 2, race_ids);

				// race_ids.add(ConfigReader.PLANT_RACES[rn.nextInt(ConfigReader.PLANT_RACES.length)]);
				// race_ids.add(ConfigReader.PLANT_RACES[rn.nextInt(ConfigReader.PLANT_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.FEY), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(3) + 1], true);
				bf.updateFaction();
				break;
			case CHAOS:
				buildArray(ConfigReader.DUNGEON_MONSTER_RACES, 2, race_ids);

				// race_ids.add(ConfigReader.DUNGEON_MONSTER_RACES[rn.nextInt(ConfigReader.DUNGEON_MONSTER_RACES.length)]);
				// race_ids.add(ConfigReader.DUNGEON_MONSTER_RACES[rn.nextInt(ConfigReader.DUNGEON_MONSTER_RACES.length)]);
				buildArray(ConfigReader.MINOR_DUNGEON_RACES, 2, race_ids);

				race_ids.add(ConfigReader.GOLEM_RACES[rn.nextInt(ConfigReader.GOLEM_RACES.length)]);
				// race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				// race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(2) + 1], true);
				bf.updateFaction();
				break;
			case COLD_RACES:
				race_ids.add(ConfigReader.OTHMIR_RACE);
				race_ids.add(ConfigReader.WALRUS_MAN_RACE);
				race_ids.add(ConfigReader.COLD_DUNGEON_RACES[rn.nextInt(ConfigReader.COLD_DUNGEON_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				break;
			case PURE_MONSTER:
				buildArray(ConfigReader.MINOR_DUNGEON_RACES, 5, race_ids);

				// race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				// race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				// race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				// race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				// race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTROSITY), race_ids, dao, z,
						FACTION_VALUES[1], true);
				bf.updateFaction();

				break;

			case PLAGUE:
				if (rn.nextInt(2) == 1) {
					race_ids.add(ConfigReader.FUNGAL_FIEND_RACE);

				} else {
					race_ids.add(ConfigReader.MUTANT_RACE);
				}

				if (rn.nextInt(2) == 1) {
					race_ids.add(ConfigReader.MUSHROOM_MAN_RACE);

				} else {
					race_ids.add(ConfigReader.MUSHROOM_RACE);
				}
				if (rn.nextInt(2) == 1) {
					race_ids.add(ConfigReader.SPIDER_RACE);

				} else {
					race_ids.add(ConfigReader.RAT_RACE);
				}
				if (rn.nextInt(2) == 1) {
					race_ids.add(ConfigReader.ZOMBIE_RACE);

				} else {
					race_ids.add(ConfigReader.SKELETON_RACE);
				}

				break;
			case GOBLINS:
				System.out.println("BUILT GOBLINS!");
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z,
						FACTION_VALUES[2], true);
				bf.buildGoblins();
				bf.updateFaction();
				break;
			default:
				break;

			}
			if (Debug.DEBUG_FACTION_BUILDER) {
				System.out.println("Dungeon Faction [" + dt.toString() + "]" + bf.getRaceIDInfo() + " FORCE:" + FORCE);
			}

			return bf;
		case NOOB:
			NOOB_TYPE dd;
			if (FORCE == 0) {
				dd = NOOB_TYPE.values()[rn.nextInt(NOOB_TYPE.values().length)];
			} else {
				dd = NOOB_TYPE.values()[FORCE - 1];
			}
			switch (dd) {
			case BUGS:
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.VERMIN), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(2) + 2], true);
				bf.updateFaction();
				break;
			case BUGS_AND_VERMIN:
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.VERMIN), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(3) + 1], true);
				bf.updateFaction();
				break;
			case FEYLINGS:
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				if (rn.nextInt(100) <= 15)
					race_ids.add(ConfigReader.WISP_RACE);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.FEY), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(3) + 2], true);
				bf.updateFaction();
				break;
			case MIXED:
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				race_ids.add(ConfigReader.NOOB_UNDEAD_RACES[rn.nextInt(ConfigReader.NOOB_UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.NOOB_UNDEAD_RACES[rn.nextInt(ConfigReader.NOOB_UNDEAD_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(1) + 1], true);
				bf.updateFaction();
				break;
			case PURE_MONSTER:
				race_ids.add(ConfigReader.MONSTER_RACES[rn.nextInt(ConfigReader.MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(1) + 1], true);
				bf.updateFaction();
				break;
			case MONSTER_RACE:
				race_ids.add(ConfigReader.MONSTER_RACES[rn.nextInt(ConfigReader.MONSTER_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(2) + 1], true);
				bf.updateFaction();
				break;

			case MONSTERS_AND_THIEVES:
				race_ids.add(ConfigReader.MONSTER_RACES[rn.nextInt(ConfigReader.MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.BANDITS), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(1) + 1], true);
				bf.buildBandits();
				bf.updateFaction();
			case PURE_ANIMALS:
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);

				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.ANIMAL), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(2) + 1], true);
				bf.updateFaction();
				break;
			case UNDEAD:
				race_ids.add(ConfigReader.NOOB_UNDEAD_RACES[rn.nextInt(ConfigReader.NOOB_UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.NOOB_UNDEAD_RACES[rn.nextInt(ConfigReader.NOOB_UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.NOOB_UNDEAD_RACES[rn.nextInt(ConfigReader.NOOB_UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.NOOB_UNDEAD_RACES[rn.nextInt(ConfigReader.NOOB_UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.NOOB_UNDEAD_RACES[rn.nextInt(ConfigReader.NOOB_UNDEAD_RACES.length)]);
				if (rn.nextInt(100) <= 15)
					race_ids.add(ConfigReader.WISP_RACE);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.UNDEAD), race_ids, dao, z,
						FACTION_VALUES[0], true);
				bf.updateFaction();
				break;
			case VERMIN_AND_BEETLES:
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.MONSTER_RACES[rn.nextInt(ConfigReader.MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.VERMIN), race_ids, dao, z,
						FACTION_VALUES[2], true);
				bf.updateFaction();
				break;
			case BANDITS:
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.BANDITS), race_ids, dao, z,
						FACTION_VALUES[1], true);
				bf.buildBandits();
				bf.updateFaction();
				break;
			default:
				break;

			}
			if (Debug.DEBUG_FACTION_BUILDER) {
				System.out.println("NOOB Faction [" + dd.toString() + "]" + bf.getRaceIDInfo() + " FORCE:" + FORCE);
			}
		case OUTSIDE:

			OUTSIDE_TYPE ot;
			if (FORCE == 0) {
				ot = OUTSIDE_TYPE.values()[rn.nextInt(OUTSIDE_TYPE.values().length)];
			} else {
				ot = OUTSIDE_TYPE.values()[FORCE - 1];
			}
			switch (ot) {
			case ANIMAL_RACES:
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.ANIMAL), race_ids, dao, z,
						FACTION_VALUES[1], true);
				bf.updateFaction();
				break;

			case DRAGONISH_RACES:
				race_ids.add(ConfigReader.DRAGONISH_RACES[rn.nextInt(ConfigReader.DRAGONISH_RACES.length)]);
				race_ids.add(ConfigReader.DRAGONISH_RACES[rn.nextInt(ConfigReader.DRAGONISH_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.DRAGON), race_ids, dao, z,
						FACTION_VALUES[1], true);
				bf.updateFaction();
				break;

			case BUGS_AND_VERMIN:
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.VERMIN), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(3) + 1], true);
				bf.updateFaction();
				break;
			case COLD_AND_MASTERS:
				race_ids.add(ConfigReader.COLD_RACES[rn.nextInt(ConfigReader.COLD_RACES.length)]);
				race_ids.add(ConfigReader.COLD_RACES[rn.nextInt(ConfigReader.COLD_RACES.length)]);
				race_ids.add(ConfigReader.COLD_RACES[rn.nextInt(ConfigReader.COLD_RACES.length)]);
				race_ids.add(ConfigReader.MONSTER_RACES[rn.nextInt(ConfigReader.MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.FEY), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(3) + 1], true);
				bf.updateFaction();
				break;
			case MONSTERS_AND_THIEVES:
				race_ids.add(ConfigReader.MONSTER_RACES[rn.nextInt(ConfigReader.MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.BANDITS), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(1) + 1], true);
				bf.buildBandits();
				bf.updateFaction();
				break;
			case DRAGONS:
				race_ids.add(ConfigReader.DRAGON_RACES[rn.nextInt(ConfigReader.DRAGON_RACES.length)]);
				race_ids.add(ConfigReader.DRAGON_RACES[rn.nextInt(ConfigReader.DRAGON_RACES.length)]);
				race_ids.add(ConfigReader.DRAGON_RACES[rn.nextInt(ConfigReader.DRAGON_RACES.length)]);
				race_ids.add(ConfigReader.DRAGON_RACES[rn.nextInt(ConfigReader.DRAGON_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.FEY), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(3) + 1], true);
				bf.updateFaction();
				break;
			case DRAKES:
				race_ids.add(ConfigReader.DRAGON_RACES[rn.nextInt(ConfigReader.DRAGON_RACES.length)]);
				race_ids.add(ConfigReader.DRAGON_RACES[rn.nextInt(ConfigReader.DRAGON_RACES.length)]);
				if (rn.nextInt(2) > 0)
					race_ids.add(ConfigReader.WURM_RACE);
				else {
					race_ids.add(ConfigReader.DRAKE_RACE);
				}
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.FEY), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(3) + 1], true);
				bf.updateFaction();
				break;
			case ELEMENTALS:
				race_ids.add(ConfigReader.ELEMENTAL_RACES[rn.nextInt(ConfigReader.ELEMENTAL_RACES.length)]);
				race_ids.add(ConfigReader.ELEMENTAL_RACES[rn.nextInt(ConfigReader.ELEMENTAL_RACES.length)]);
				race_ids.add(ConfigReader.ELEMENTAL_RACES[rn.nextInt(ConfigReader.ELEMENTAL_RACES.length)]);
				race_ids.add(ConfigReader.ELEMENTAL_RACES[rn.nextInt(ConfigReader.ELEMENTAL_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.FEY), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(3) + 1], true);
				bf.updateFaction();
				break;
			case FEY:
				race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				if (rn.nextInt(3) > 1) {
					race_ids.add(ConfigReader.WISP_RACE);
				} else {
					race_ids.add(ConfigReader.WASP_RACE);
				}
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.FEY), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(3) + 1], true);
				bf.updateFaction();
				break;
			case FEY_AND_FLOWERS:
				race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				race_ids.add(ConfigReader.PLANT_RACES[rn.nextInt(ConfigReader.PLANT_RACES.length)]);
				race_ids.add(ConfigReader.PLANT_RACES[rn.nextInt(ConfigReader.PLANT_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.FEY), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(3) + 1], true);
				bf.updateFaction();
				break;
			case FIRE_AND_MASTERS:
				race_ids.add(ConfigReader.FIRE_RACES[rn.nextInt(ConfigReader.FIRE_RACES.length)]);
				race_ids.add(ConfigReader.FIRE_RACES[rn.nextInt(ConfigReader.FIRE_RACES.length)]);
				if (rn.nextInt(4) > 2) {
					race_ids.add(ConfigReader.WURM_RACE);
				} else {
					race_ids.add(ConfigReader.DRAKE_RACE);
				}
				race_ids.add(ConfigReader.MONSTER_RACES[rn.nextInt(ConfigReader.MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.FEY), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(3) + 1], true);
				bf.updateFaction();

				break;
			case COLD_RACES:
				race_ids.add(ConfigReader.OTHMIR_RACE);
				race_ids.add(ConfigReader.WALRUS_MAN_RACE);
				race_ids.add(ConfigReader.COLD_DUNGEON_RACES[rn.nextInt(ConfigReader.COLD_DUNGEON_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(2) + 1], true);
				bf.updateFaction();
				break;
			case GIANTS:
				race_ids.add(ConfigReader.GIANT_RACES[rn.nextInt(ConfigReader.FIRE_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				break;
			case MONSTER_RACE_1:
				race_ids.add(ConfigReader.MONSTER_RACES[rn.nextInt(ConfigReader.MONSTER_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(2) + 1], true);
				bf.updateFaction();
				break;
			case MONSTER_RACE_2:
				race_ids.add(ConfigReader.MONSTER_RACES[rn.nextInt(ConfigReader.MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(2) + 1], true);
				bf.updateFaction();
				break;
			case MONSTER_RACE_3:
				race_ids.add(ConfigReader.MONSTER_RACES[rn.nextInt(ConfigReader.MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(2) + 1], true);
				bf.updateFaction();
				break;
			case WHORES:
				race_ids.add(ConfigReader.WHORE_RACES[rn.nextInt(ConfigReader.WHORE_RACES.length)]);
				race_ids.add(ConfigReader.WHORE_RACES[rn.nextInt(ConfigReader.WHORE_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(2) + 1], true);
				bf.updateFaction();
				break;
			case WISPS_AND_ANIMALS:
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.WISP_RACE);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.ANIMAL), race_ids, dao, z,
						FACTION_VALUES[1], true);
				bf.updateFaction();
				break;
			case DOG_PEOPLE:
				race_ids.add(ConfigReader.DOG_PEOPLE_RACES[rn.nextInt(ConfigReader.DOG_PEOPLE_RACES.length)]);
				race_ids.add(ConfigReader.DOG_PEOPLE_RACES[rn.nextInt(ConfigReader.DOG_PEOPLE_RACES.length)]);
				race_ids.add(ConfigReader.DOG_PEOPLE_RACES[rn.nextInt(ConfigReader.DOG_PEOPLE_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z,
						FACTION_VALUES[rn.nextInt(2) + 1], true);
				bf.updateFaction();
				break;
			case MORE_ANIMALS:
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_MONSTER_RACES[rn.nextInt(ConfigReader.ANIMAL_MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.ANIMAL), race_ids, dao, z,
						FACTION_VALUES[1], true);
				bf.updateFaction();
				break;
			case ANIMALS_AND_KEEPERS:
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_MONSTER_RACES[rn.nextInt(ConfigReader.ANIMAL_MONSTER_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_MONSTER_RACES[rn.nextInt(ConfigReader.ANIMAL_MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.ANIMAL), race_ids, dao, z,
						FACTION_VALUES[1], true);
				bf.updateFaction();
				break;
			case GUARDS:
				race_ids.add(ConfigReader.GUARD_RACES[rn.nextInt(ConfigReader.GUARD_RACES.length)]);
				race_ids.add(ConfigReader.GUARD_RACES[rn.nextInt(ConfigReader.GUARD_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.GUARD), race_ids, dao, z,
						FACTION_VALUES[1], true);
				bf.updateFaction();
			case ROAMING_UNDEAD:
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.UNDEAD), race_ids, dao, z,
						FACTION_VALUES[0], true);
				bf.updateFaction();
				break;
			case BANDITS:
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.BANDITS), race_ids, dao, z,
						FACTION_VALUES[1], true);
				bf.buildBandits();
				bf.updateFaction();
				break;
			default:
				break;

			}

			break;

		case RAID:

		}

		return bf;

	}

	public void setName() {

	}

	public static final int STARTING_FACTION_LIST_ID = 6000;
	public static final int STARTING_NPC_ID = 1000000;
	public static final int STARTING_NPC_FACTION_ID = 6000;
	public static int CURRENT_NPC_ID = STARTING_NPC_ID;
	public static int CURRENT_FACTION_LIST_ID = STARTING_FACTION_LIST_ID;
	private Random rn;

	public final String[] UNDEAD_NAMES = new String[] { "Haunted", "Bonemongers", "Death Legion" };
	public final String[] ANIMAL_NAMES = new String[] { "Animals", "Wild", "Herd", "Pack" };
	public final String[] VERMIN_NAMES = new String[] { "Vermin", "Filth", "Swarm", "Pets" };
	public final String[] BUG_NAMES = new String[] { "Crawlers", "Swarm", "Host" };
	public final String[] PLANT_NAMES = new String[] { "Foliage", "Growth", "Thorns" };
	public final String[] GIANT_NAMES = new String[] { "Giantkind", "Goliaths", "Titans" };
	public final String[] GOLEM_NAMES = new String[] { "Reanimated", "Golems", "Constructs", "Builders" };
	public final String[] MONSTER_NAMES = new String[] { "Legion", "Army", "Horde", "Protectors", "Skirmishers" };
	public final String[] FEY_NAMES = new String[] { "Fey Outpost", "Pixie Invasion", "Feywild" };
	public final String[] BANDITS_NAMES = new String[] { "Bandit Legion", "Mercanaries", "Smugglers" };
	public final String[] GUARDS_NAMES = new String[] { "Guardians", "Fighters", "Crusaders" };
	public static final int[] RANDOM_NOOB_FACTION_REPLACER = new int[] { 3, 4, 8, 9, 10 };

	public enum RACE {
		UNDEAD, GIANT, MONSTER, ANIMAL, VERMIN, BUG, PLANT, GOLEM, FEY, MONSTROSITY, ANIMAL_PERSON, ELEMENTAL,
		DOG_PERSON, DRAKE, DRAGON, WHORE, GUARD, WURM, BANDITS;
	}

	public enum TYPE {
		DUNGEON, NOOB, OUTSIDE, RAID
	}

	public enum NOOB_TYPE {
		VERMIN_AND_BEETLES, PURE_ANIMALS, MONSTER_RACE, UNDEAD, BUGS, BUGS_AND_VERMIN, MIXED, FEYLINGS, PURE_MONSTER,
		MONSTERS_AND_THIEVES, BANDITS
	}

	public enum DUNGEON_TYPE {
		UNDEAD_LIVE_MIX, PURE_UNDEAD, NECROMANCER, LIVE_WITH_GOLEM, LIVE_WITH_SECONDARY, LIVE_WITH_ANIMALS, FEY,
		UNDEAD_WITH_MONSTERS, PURE_MONSTER, CHAOS, MONSTERS_AND_THIEVES, COLD_RACES, ELEMENTAL_CHAOS, PLAGUE, GOBLINS;
	}

	public enum OUTSIDE_TYPE {

		ANIMAL_RACES, FEY_AND_FLOWERS, DRAGONS, ELEMENTALS, FIRE_AND_MASTERS, GIANTS, COLD_AND_MASTERS, FEY, DRAKES,
		WHORES, WISPS_AND_ANIMALS, BUGS_AND_VERMIN, MONSTER_RACE_1, MONSTER_RACE_2, MONSTER_RACE_3, DOG_PEOPLE,
		MORE_ANIMALS, ANIMALS_AND_KEEPERS, GUARDS, ROAMING_UNDEAD, BANDITS, MONSTERS_AND_THIEVES, COLD_RACES,
		DRAGONISH_RACES;
	}

}
