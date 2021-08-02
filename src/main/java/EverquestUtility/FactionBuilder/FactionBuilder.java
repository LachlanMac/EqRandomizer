package EverquestUtility.FactionBuilder;

import java.util.ArrayList;
import java.util.Random;

import EverquestUtility.Database.EQDao;
import EverquestUtility.Database.Helpers.Mob;
import EverquestUtility.Database.Helpers.Zone;
import EverquestUtility.Util.ConfigReader;

public class FactionBuilder {

	private int high, low, average;

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
		default:
			break;
		}
		name += " of " + z.getLongName();
		return name;

	}

	public BuiltFaction BuildFaction(TYPE t, Zone z, EQDao dao, int FORCE) {
		BuiltFaction bf = null;
		ArrayList<Integer> race_ids = new ArrayList<Integer>();
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
				race_ids.add(ConfigReader.GOLEM_RACES[rn.nextInt(ConfigReader.GOLEM_RACES.length)]);
				race_ids.add(ConfigReader.GOLEM_RACES[rn.nextInt(ConfigReader.GOLEM_RACES.length)]);
				race_ids.add(ConfigReader.GOLEM_RACES[rn.nextInt(ConfigReader.GOLEM_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.GOLEM), race_ids, dao, z);
				bf.updateFaction();
				break;
			case LIVE_WITH_SECONDARY:
				race_ids.add(ConfigReader.DUNGEON_MONSTER_RACES[rn.nextInt(ConfigReader.DUNGEON_MONSTER_RACES.length)]);
				race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z);
				bf.updateFaction();
				break;
			case NECROMANCER:
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.UNDEAD), race_ids, dao, z);

				bf.buildNecroDungeon();
				bf.updateFaction();
				break;
			case PURE_UNDEAD:
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);

				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.UNDEAD), race_ids, dao, z);
				bf.updateFaction();
				break;
			case UNDEAD_WITH_MONSTERS:
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z);
				bf.updateFaction();
				break;
			case UNDEAD_LIVE_MIX:
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.DUNGEON_MONSTER_RACES[rn.nextInt(ConfigReader.DUNGEON_MONSTER_RACES.length)]);
				race_ids.add(ConfigReader.DUNGEON_MONSTER_RACES[rn.nextInt(ConfigReader.DUNGEON_MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z);
				bf.updateFaction();
				break;
			case LIVE_WITH_ANIMALS:
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.DUNGEON_MONSTER_RACES[rn.nextInt(ConfigReader.DUNGEON_MONSTER_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_MONSTER_RACES[rn.nextInt(ConfigReader.ANIMAL_MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z);
				bf.updateFaction();
				break;
			case FEY:
				race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				race_ids.add(ConfigReader.PLANT_RACES[rn.nextInt(ConfigReader.PLANT_RACES.length)]);
				race_ids.add(ConfigReader.PLANT_RACES[rn.nextInt(ConfigReader.PLANT_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.FEY), race_ids, dao, z);
				bf.updateFaction();
				break;
			case CHAOS:
				race_ids.add(ConfigReader.DUNGEON_MONSTER_RACES[rn.nextInt(ConfigReader.DUNGEON_MONSTER_RACES.length)]);
				race_ids.add(ConfigReader.DUNGEON_MONSTER_RACES[rn.nextInt(ConfigReader.DUNGEON_MONSTER_RACES.length)]);
				race_ids.add(ConfigReader.GOLEM_RACES[rn.nextInt(ConfigReader.GOLEM_RACES.length)]);
				race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);

				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z);
				bf.updateFaction();
				break;
			case PURE_MONSTER:
				race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);
				race_ids.add(ConfigReader.MINOR_DUNGEON_RACES[rn.nextInt(ConfigReader.MINOR_DUNGEON_RACES.length)]);

				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z);

			default:
				race_ids.add(ConfigReader.DUNGEON_MONSTER_RACES[rn.nextInt(ConfigReader.DUNGEON_MONSTER_RACES.length)]);
				race_ids.add(ConfigReader.DUNGEON_MONSTER_RACES[rn.nextInt(ConfigReader.DUNGEON_MONSTER_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				break;

			}
			return bf;
		case NOOB:
			NOOB_TYPE dd;
			if (FORCE == 0) {
				dd = NOOB_TYPE.values()[rn.nextInt(NOOB_TYPE.values().length)];
			} else {
				dd = NOOB_TYPE.values()[FORCE + 1];
			}
			switch (dd) {
			case BUGS:
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.VERMIN), race_ids, dao, z);
				bf.updateFaction();
				break;
			case BUGS_AND_VERMIN:
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.VERMIN), race_ids, dao, z);
				bf.updateFaction();
				break;
			case FEYLINGS:
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				race_ids.add(ConfigReader.FEY_RACES[rn.nextInt(ConfigReader.FEY_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.FEY), race_ids, dao, z);
				bf.updateFaction();
				break;
			case MIXED:
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z);
				bf.updateFaction();
				break;
			case PURE_MONSTER:
				race_ids.add(ConfigReader.MONSTER_RACES[rn.nextInt(ConfigReader.MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z);
				bf.updateFaction();
				break;
			case MONSTER_RACE:
				race_ids.add(ConfigReader.MONSTER_RACES[rn.nextInt(ConfigReader.MONSTER_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.MONSTER), race_ids, dao, z);
				bf.updateFaction();
				break;
			case MONSTERS_AND_THIEVES:
				race_ids.add(ConfigReader.MONSTER_RACES[rn.nextInt(ConfigReader.MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.VERMIN), race_ids, dao, z);
				bf.buildBandits();
				bf.updateFaction();
			case PURE_ANIMALS:
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.ANIMAL), race_ids, dao, z);
				bf.updateFaction();
				break;
			case UNDEAD:
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				race_ids.add(ConfigReader.UNDEAD_RACES[rn.nextInt(ConfigReader.UNDEAD_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.UNDEAD), race_ids, dao, z);
				bf.updateFaction();
				break;
			case VERMIN_AND_BEETLES:
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.BUG_RACES[rn.nextInt(ConfigReader.BUG_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				race_ids.add(ConfigReader.VERMIN_RACES[rn.nextInt(ConfigReader.VERMIN_RACES.length)]);
				race_ids.add(ConfigReader.ANIMAL_RACES[rn.nextInt(ConfigReader.ANIMAL_RACES.length)]);
				race_ids.add(ConfigReader.MONSTER_RACES[rn.nextInt(ConfigReader.MONSTER_RACES.length)]);
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.VERMIN), race_ids, dao, z);
				bf.updateFaction();
			case BANDITS:
				bf = new BuiltFaction(CURRENT_FACTION_LIST_ID, getName(z, RACE.VERMIN), race_ids, dao, z);
				bf.buildBandits();
				bf.updateFaction();

				break;
			default:
				break;

			}

		case OUTSIDE:

		case RAID:

		}

		return bf;

	}

	public static final int STARTING_FACTION_LIST_ID = 6000;
	public static final int STARTING_NPC_ID = 1000000;
	public static final int STARTING_NPC_FACTION_ID = 6000;
	public static int CURRENT_NPC_ID = STARTING_NPC_ID;
	public static int CURRENT_FACTION_LIST_ID = STARTING_FACTION_LIST_ID;
	private Random rn;

	public final String[] UNDEAD_NAMES = new String[] { "Haunted", "Bonemongers", "Death Legion" };
	public final String[] ANIMAL_NAMES = new String[] { "Animals", "Wild", "Herd" };
	public final String[] VERMIN_NAMES = new String[] { "Vermin", "Filth" };
	public final String[] BUG_NAMES = new String[] { "Crawlers", "Swarm", "Host" };
	public final String[] PLANT_NAMES = new String[] { "Foliage", "Growth", "Thorns" };
	public final String[] GIANT_NAMES = new String[] { "Giantkind", "Goliaths", "Titans" };
	public final String[] GOLEM_NAMES = new String[] { "Reanimated", "Golems", "Constructs", "Builders" };
	public final String[] MONSTER_NAMES = new String[] { "Legion", "Army", "Horde", "Protectors", "Skirmishers" };
	public final String[] FEY_NAMES = new String[] { "Fey Outpost", "Pixie Invasion", "Feywild" };

	public enum RACE {
		UNDEAD, GIANT, MONSTER, ANIMAL, VERMIN, BUG, PLANT, GOLEM, FEY
	}

	public enum TYPE {
		DUNGEON, NOOB, OUTSIDE, RAID
	}

	public enum NOOB_TYPE {
		VERMIN_AND_BEETLES, PURE_ANIMALS, MONSTER_RACE, UNDEAD, BUGS, BUGS_AND_VERMIN, MIXED, FEYLINGS, PURE_MONSTER,
		BANDITS, MONSTERS_AND_THIEVES
	}

	public enum DUNGEON_TYPE {
		UNDEAD_LIVE_MIX, PURE_UNDEAD, NECROMANCER, LIVE_WITH_GOLEM, LIVE_WITH_SECONDARY, LIVE_WITH_ANIMALS, FEY,
		UNDEAD_WITH_MONSTERS, PURE_MONSTER, CHAOS
	}

}
