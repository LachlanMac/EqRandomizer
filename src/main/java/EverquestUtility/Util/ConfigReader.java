package EverquestUtility.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigReader {

	public static int[] LDON_ZONE_IDS = new int[] { 147, 148, 149, 150, 151, 152, 153, 154, 155, 213, 214, 215, 216,
			217, 218, 219, 220, 221, 222, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 319, 320, 321, 322, 323,
			324, 325, 326, 327, 328, 377, 378, 379, 380, 381, 382, 383, 384, 387, 5966, 5967, 5974, 5970, 5974, 6018,
			6019 };

	// 183,157

	public static int ANIMAL_FACTION = 5014;
	public static int BEETLE_FACTION = 1159;
	public static int KOS_FACTION = 5013;
	public static int[] ZONE_IDS;
	public static int[] NOOB_REPLACE_FACTION_IDS;
	public static int[] DUNGEON_ZONE_IDS;
	public static int[] RAID_ZONE_IDS;
	public static int[] REJECTED_FACTION_IDS;
	public static int[] GIANT_FACTIONS;
	public static int[] NOOB_REP_RANDOM_IDS;
	public static int[] NON_COMBAT_ZONE_IDS;
	public static int[] OUTDOOR_SKIP;
	public static int[] FORBIDDEN_SPAWNGROUPS;
	public static int[] NO_FACTION_BANNED_LIST;

	// ANIMALS
	public static final int LION_RACE = 50;
	public static final int WOLF_RACE = 42;
	public static final int BEAR_RACE = 43;
	public static final int PUMA_RACE = 439;// 76
	public static final int OWLBEAR_RACE = 206;
	public static final int RHINO_RACE = 135;
	public static final int MAMMOTH_RACE = 107;
	public static final int SABERTOOTH_RACE = 119;
	public static final int WALRUS_RACE = 177;
	public static final int TIGER_RACE = 63;
	public static final int GRIFFON_RACE = 47;
	public static final int GORILLA_RACE = 41;
	public static final int ROCK_HOPPER_RACE = 200;
	public static final int RAPTOR_RACE = 163;
	public static final int ZELNIAK_RACE = 222;
	public static final int SONIC_WOLF_RACE = 232;
	public static final int YETI_RACE = 138;
	public static final int DIRE_WOLF_RACE = 171;
	public static final int MANTICORE_RACE = 172;

	public static int[] ANIMAL_RACES = new int[] { LION_RACE, WOLF_RACE, BEAR_RACE, PUMA_RACE, OWLBEAR_RACE, RHINO_RACE,
			MAMMOTH_RACE, SABERTOOTH_RACE, WALRUS_RACE, TIGER_RACE, GRIFFON_RACE, GORILLA_RACE, ROCK_HOPPER_RACE,
			RAPTOR_RACE, ZELNIAK_RACE, SONIC_WOLF_RACE, YETI_RACE, DIRE_WOLF_RACE, MANTICORE_RACE };

	// PLANTS
	public static final int PLANT_RACE = 162;
	public static final int MUSHROOM_RACE = 227;
	public static final int CACTUS_RACE = 167;
	public static final int TREANT_RACE = 64;
	public static final int ENT_RACE = 244;
	public static final int MUSHROOM_MAN_RACE = 28;
	public static final int SUNFLOWER_RACE = 225;

	public static int[] PLANT_RACES = new int[] { PLANT_RACE, MUSHROOM_RACE, CACTUS_RACE, TREANT_RACE, ENT_RACE,
			MUSHROOM_MAN_RACE, SUNFLOWER_RACE };

	public static final int FAIRY_RACE = 25;
	public static final int PIXIE_RACE = 56;
	public static final int DRIXIE_RACE = 113;
	public static final int UNICORN_RACE = 124;
	public static final int FAE_DRAKE_RACE = 154;
	public static final int PEGASUS_RACE = 125;
	public static final int BROWNIE_RACE = 15;
	public static final int WISP_RACE = 69;
	public static final int NAIAD_RACE = 242;
	public static final int NYMPH_RACE = 243;

	public static final int THOUGHT_HORROR_RACE = 214;
	public static final int FUNGAL_FIEND_RACE = 218;

	public static int[] PIRATE_RACES = new int[] { 335, 336, 337, 338, 339, 340, 341, 342, 205, 524, 205 };
	public static int[] HUMANOID_RACES = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 128, 130, 330, 522, 71, 139,
			566, 433, 331, 332, 333 };

	public static int[] FEY_RACES = new int[] { FAIRY_RACE, PIXIE_RACE, DRIXIE_RACE, UNICORN_RACE, FAE_DRAKE_RACE,
			PEGASUS_RACE, BROWNIE_RACE, WISP_RACE, NAIAD_RACE, NYMPH_RACE };

	public static final int IKSAR_HAND_RACE = 166;
	public static final int HAND_RACE = 80;
	public static final int GOO_RACE = 145;
	public static final int DEVOURER_RACE = 159;
	public static final int HOLGRESH_RACE = 168;
	public static final int TENTACLE_RACE = 68;
	public static final int EVIL_EYE_RACE = 21;
	public static final int GARGOYLE_RACE = 29;
	public static final int MINOTAUR_RACE = 53;
	public static final int CUBE_RACE = 31;

	public static int[] MINOR_DUNGEON_RACES = new int[] { IKSAR_HAND_RACE, HAND_RACE, GOO_RACE, DEVOURER_RACE,
			HOLGRESH_RACE, TENTACLE_RACE, EVIL_EYE_RACE, GARGOYLE_RACE, MINOTAUR_RACE, THOUGHT_HORROR_RACE, CUBE_RACE };

	// VERMIN_juggernaut
	public static final int BAT_RACE = 34;
	public static final int SNAKE_RACE = 37;
	public static final int RAT_RACE = 36;
	public static final int SKUNK_RACE = 83;
	public static final int BUNNY_RACE = 176;
	public static final int COCKATRICE_RACE = 96;
	public static final int ARMADILLO_RACE = 87;

	public static final int EFREETI_RACE = 101;
	public static final int DJINN_RACE = 126;
	public static final int AIR_ELEMENTAL = 210;
	public static final int EARTH_ELEMENTAL = 209;
	public static final int WATER_ELEMENTAL = 211;
	public static final int FIRE_ELEMENTAL = 212;

	// BUGS
	public static final int LEECH_RACE = 104;
	public static final int LIGHT_CRAWLER_RACE = 223;
	public static final int SPIDER_RACE = 38;
	public static final int BEETLE_RACE = 22;
	public static final int RHINO_BEETLE_RACE = 207;
	public static final int MOSQUITO_RACE = 134;
	public static final int MUCK_DIGGER_RACE = 201;
	public static final int SILK_WORM_RACE = 203;
	public static final int SCORPION_RACE = 129;
	public static final int WASP_RACE = 109;
	public static final int SHISSAR_RACE = 217;
	public static final int SHIKNAR_RACE = 199;

	public static final int GUARD_RACE_1 = 44;
	public static final int GUARD_RACE_2 = 106;
	public static final int GUARD_RACE_3 = 112;

	public static int[] GUARD_RACES = new int[] { GUARD_RACE_1, GUARD_RACE_2, GUARD_RACE_3 };

	public static String[] NORMAL_MOB_PREFIXES = new String[] { "AN_", "A_" };
	public static String[] NORMAL_MOB_INDICATORS = new String[] { "orc", "gnoll", "skeletal", "skeleton", "tormented_",
			"crypt_", "froglok", "remains", "greater", "lesser", "kobold", "undead", "_summoning", "tortured",
			"_warrior", "_drixie", "decayed_", "fallen_", "_guardian", "xi_", "_juggernaut", "ravener", "_ancient_",
			"rotting", "risen", "zombie", "minion", "warder_of", "_elite_", "messenger", "mystic_of", "goblin",
			"tainted", "erudite", "servant_of", "Burynai", "Guard", "Battle-Hardened", "bonecrawler", "_knight", };

	public static String[] BANDIT_INDICATORS = new String[] { "bandit", "poacher", "recondite", "pirate", "thief" };

	public static int[] BUG_RACES = new int[] { LEECH_RACE, LIGHT_CRAWLER_RACE, SPIDER_RACE, BEETLE_RACE,
			RHINO_BEETLE_RACE, MOSQUITO_RACE, MUCK_DIGGER_RACE, SILK_WORM_RACE, SCORPION_RACE, WASP_RACE };

	// DRAGONS
	public static final int DRAGON_RACE = 49;
	public static final int DRAGON_RACE_2 = 184;
	public static final int DRAGON_RACE_3 = 192;
	public static final int DRAGON_RACE_4 = 195;
	public static final int DRAGON_RACE_5 = 196;

	public static int[] DRAGON_RACES = new int[] { DRAGON_RACE, DRAGON_RACE_2, DRAGON_RACE_3, DRAGON_RACE_4,
			DRAGON_RACE_5 };

	public static final int CLAM_RACE = 115;
	public static final int SEA_HORE_RACE = 116;
	public static final int COLDAIN_RACE = 183;

	public static final int DRACHNID_RACE = 57;
	public static final int HARPY_RACE = 111;
	public static final int GORGON_RACE = 121;
	public static final int SIREN_RACE = 187;
	public static final int HAG_RACE = 185;

	public static int[] VERMIN_RACES = new int[] { BAT_RACE, SNAKE_RACE, RAT_RACE, SKUNK_RACE, BUNNY_RACE,
			COCKATRICE_RACE, ARMADILLO_RACE, SHIKNAR_RACE };
	public static int[] FIRE_RACES = new int[] { EFREETI_RACE, FIRE_ELEMENTAL };
	public static int[] ELEMENTAL_RACES = new int[] { EFREETI_RACE, FIRE_ELEMENTAL, EARTH_ELEMENTAL, WATER_ELEMENTAL,
			AIR_ELEMENTAL, DJINN_RACE };

	public static int[] WHORE_RACES = new int[] { HARPY_RACE, HAG_RACE, GORGON_RACE, SIREN_RACE };

	public static final int WEREWOLF_RACE_1 = 14;
	public static final int WEREWOLF_RACE_2 = 241;

	public static int[] DOG_PEOPLE_RACES = new int[] { WEREWOLF_RACE_1, WEREWOLF_RACE_2, DIRE_WOLF_RACE, WOLF_RACE };

	public static final int DRAKE_RACE = 89;
	public static final int WURM_RACE = 158;
	public static final int WYVERN_RACE = 157;

	public static int[] DRAGONISH_RACES = new int[] { DRAKE_RACE, WURM_RACE, WYVERN_RACE };
	// MONSTER RACES
	public static final int DROLVARG_RACE = 133;
	public static final int GNOLL_RACE = 39;
	public static final int ORC_RACE = 54;
	public static final int GOBLIN_RACE = 40;
	public static final int KUNARK_GOBLIN_RACE = 137;
	public static final int KOBOLD_RACE = 48;
	public static final int NEW_KOBOLD_RACE = 455;
	public static final int OTHMIR_RACE = 190;
	public static final int GRIMLING_RACE = 202;
	public static final int FROGLOK_RACE = 26;

	public static final int BURYNAI_RACE = 144;
	public static final int NETHERBIAN_RACE = 229;
	public static final int GALORIAN_RACE = 228;
	public static final int WALRUS_MAN_RACE = 191;
	public static final int RAT_MAN_RACE = 156;
	public static final int SARNAK_RACE = 131;
	public static final int LIZARD_MAN_RACE = 51;
	public static final int AVIAK_RACE = 13;
	public static final int TEGI_RACE = 215;

	public static final int BLOODGIL_GOBLIN_RACE = 59;

	public static final int CENTAUR_RACE = 16;

	public static final int ABHORRENT_RACE = 193;

	public static final int MUTANT_RACE = 235;
	public static int[] FUNGUS_DUNGEON_RACES = new int[] { FUNGAL_FIEND_RACE, MUSHROOM_MAN_RACE, MUSHROOM_RACE,
			MUTANT_RACE };

	public static int[] COLD_DUNGEON_RACES = new int[] { CLAM_RACE, YETI_RACE, BEAR_RACE, COLDAIN_RACE };

	public static int[] ANIMAL_MONSTER_RACES = new int[] { RAT_MAN_RACE, WALRUS_MAN_RACE, OTHMIR_RACE, AVIAK_RACE,
			CENTAUR_RACE };

	public static int[] DUNGEON_MONSTER_RACES = new int[] { DROLVARG_RACE, GNOLL_RACE, ORC_RACE, BURYNAI_RACE,
			KOBOLD_RACE, NEW_KOBOLD_RACE, GRIMLING_RACE, FROGLOK_RACE, NETHERBIAN_RACE, GALORIAN_RACE, SARNAK_RACE,
			LIZARD_MAN_RACE, TEGI_RACE, SHISSAR_RACE };

	public static int[] RANDOM_RAID_RACES = new int[] { GNOLL_RACE, ORC_RACE, GOBLIN_RACE, KUNARK_GOBLIN_RACE,
			FROGLOK_RACE, LIZARD_MAN_RACE, SARNAK_RACE, NETHERBIAN_RACE };

	public static int[] MONSTER_RACES = new int[] { DROLVARG_RACE, GNOLL_RACE, ORC_RACE, GOBLIN_RACE,
			KUNARK_GOBLIN_RACE, KOBOLD_RACE, NEW_KOBOLD_RACE, OTHMIR_RACE, GRIMLING_RACE, FROGLOK_RACE, MINOTAUR_RACE,
			EVIL_EYE_RACE, BURYNAI_RACE, NETHERBIAN_RACE, GALORIAN_RACE, RAT_MAN_RACE, SARNAK_RACE, LIZARD_MAN_RACE,
			AVIAK_RACE, TEGI_RACE, SHISSAR_RACE, BLOODGIL_GOBLIN_RACE };

	// GOLEM RACES
	public static final int IKSAR_GOLEM_RACE = 160;
	public static final int SARNAK_GOLEM_RACE = 164;
	public static final int ROCK_GEM_RACE = 178;
	public static final int MUDMAN_RACE = 17;

	public static int[] GOLEM_RACES = new int[] { IKSAR_GOLEM_RACE, SARNAK_GOLEM_RACE, ROCK_GEM_RACE, MUDMAN_RACE };

	// GIANT RACES

	public static final int FROST_GIANT_RACE = 188;
	public static final int STORM_GIANT_RACE = 189;
	public static final int FOREST_GIANT_RACE = 140;
	public static final int CYCLOPS_RACE = 18;

	public static int[] COLD_RACES = new int[] { FROST_GIANT_RACE, WALRUS_RACE, WALRUS_MAN_RACE, OTHMIR_RACE,
			MAMMOTH_RACE, OTHMIR_RACE, COLDAIN_RACE };

	public static int[] GIANT_RACES = new int[] { FROST_GIANT_RACE, STORM_GIANT_RACE, FOREST_GIANT_RACE, CYCLOPS_RACE };
	// UNDEAD RACES
	public static final int SKELETON_RACE = 161;
	public static final int SKELETON_RACE_2 = 367;
	public static final int GHOUL_RACE = 33;
	public static final int GHOST_RACE = 118;
	public static final int ZOMBIE_RACE = 70;
	public static final int ELF_VAMPIRE_RACE = 98;
	public static final int UNDEAD_FROGLOK_RACE = 27;
	public static final int SHADE_RACE = 224;
	public static final int SPECTRE_RACE = 85;
	public static final int DWARF_SPIRIT_RACE = 117;
	public static final int SCARECROW_RACE = 82;
	public static final int SARNAK_SKELETON_RACE = 155;
	public static final int VAMPIRE_RACE = 208;
	public static final int SARNAK_SPIRIT_RACE = 146;
	public static final int IKSAR_SPIRIT_RACE = 147;
	public static final int VAH_SHIR_SKELETON = 234;
	public static int[] SHADOWKNGIHT_RACES = new int[] { 1, 3, 6, 9, 10 };
	public static int[] NECROMANCER_RACES = new int[] { 1, 3, 5, 6, 12, 128, 330 };
	public static int[] MAGE_RACES = new int[] { 1, 3, 5, 6, 12, 128, 330 };
	public static int[] NOOB_UNDEAD_RACES = new int[] { SKELETON_RACE, SKELETON_RACE_2, ZOMBIE_RACE, SCARECROW_RACE,
			GHOUL_RACE };

	public static int[] UNDEAD_RACES = new int[] { SKELETON_RACE, SKELETON_RACE_2, GHOUL_RACE, GHOST_RACE, ZOMBIE_RACE,
			ELF_VAMPIRE_RACE, UNDEAD_FROGLOK_RACE, SHADE_RACE, SPECTRE_RACE, DWARF_SPIRIT_RACE, SCARECROW_RACE,
			SARNAK_SKELETON_RACE, VAMPIRE_RACE, VAH_SHIR_SKELETON };

	public static int[] GOBLINOID_RACES = new int[] { GOBLIN_RACE, KUNARK_GOBLIN_RACE, BLOODGIL_GOBLIN_RACE };
	public static int[] GOBLIN_TEXTURES = new int[] { 2, 4, 5, 9, 10, 7 };

	public static final int SHARK_RACE = 61;
	public static final int BARRACUDA_RACE = 24;
	public static final int FISH_RACE = 74;
	public static final int LONGNOSE_RACE = 105;
	public static final int TIDE_FEASTER = 315;
	public static final int SWARM_RACE = 230;
	public static final int HRAQUIS_RACE = 102;
	public static final int REGRUA_RACE = 302;
	public static final int ANGLER_RACE = 165;
	public static final int MINNOW_RACE = 213;
	public static final int SEAHORSE_RACE = 116;
	public static final int NERIAD_RACE = 110;
	public static final int BARRACUDA_2_RACE = 148;

	// public static int

	public static void ReadConfig() {

		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("data/noobzones.txt")));

			String line;
			while ((line = br.readLine()) != null) {

				String[] split = line.split(",");
				// NO_FACTION_BANNED_LIST
				if (split[0].equals("noobzones")) {
					ZONE_IDS = new int[split.length - 1];
					for (int i = 1; i < split.length; i++) {
						ZONE_IDS[i - 1] = Integer.parseInt(split[i]);
					}

				} else if (split[0].equals("noobreplace")) {
					NOOB_REPLACE_FACTION_IDS = new int[split.length - 1];
					for (int i = 1; i < split.length; i++) {

						NOOB_REPLACE_FACTION_IDS[i - 1] = Integer.parseInt(split[i]);
					}
				} else if (split[0].equals("nofactionban")) {
					NO_FACTION_BANNED_LIST = new int[split.length - 1];
					for (int i = 1; i < split.length; i++) {
						NO_FACTION_BANNED_LIST[i - 1] = Integer.parseInt(split[i]);
					}
				} else if (split[0].equals("dungeons")) {
					DUNGEON_ZONE_IDS = new int[split.length - 1];
					for (int i = 1; i < split.length; i++) {

						DUNGEON_ZONE_IDS[i - 1] = Integer.parseInt(split[i]);
					}
				} else if (split[0].equals("rejected")) {
					REJECTED_FACTION_IDS = new int[split.length - 1];
					for (int i = 1; i < split.length; i++) {

						REJECTED_FACTION_IDS[i - 1] = Integer.parseInt(split[i]);
					}
				} else if (split[0].equals("giants")) {
					GIANT_FACTIONS = new int[split.length - 1];
					for (int i = 1; i < split.length; i++) {

						GIANT_FACTIONS[i - 1] = Integer.parseInt(split[i]);
					}
				} else if (split[0].equals("noobreprandom")) {
					NOOB_REP_RANDOM_IDS = new int[split.length - 1];
					for (int i = 1; i < split.length; i++) {
						NOOB_REP_RANDOM_IDS[i - 1] = Integer.parseInt(split[i]);
					}
				} else if (split[0].equals("raids")) {
					RAID_ZONE_IDS = new int[split.length - 1];
					for (int i = 1; i < split.length; i++) {
						RAID_ZONE_IDS[i - 1] = Integer.parseInt(split[i]);
					}
				} else if (split[0].equals("raids")) {
					RAID_ZONE_IDS = new int[split.length - 1];
					for (int i = 1; i < split.length; i++) {
						RAID_ZONE_IDS[i - 1] = Integer.parseInt(split[i]);
					}
				} else if (split[0].equals("noncombatzones")) {
					NON_COMBAT_ZONE_IDS = new int[split.length - 1];
					for (int i = 1; i < split.length; i++) {
						NON_COMBAT_ZONE_IDS[i - 1] = Integer.parseInt(split[i]);
					}
				} else if (split[0].equals("outdoorskip")) {
					OUTDOOR_SKIP = new int[split.length - 1];
					for (int i = 1; i < split.length; i++) {
						OUTDOOR_SKIP[i - 1] = Integer.parseInt(split[i]);
					}
				} else if (split[0].equals("forbiddensgs")) {
					FORBIDDEN_SPAWNGROUPS = new int[split.length - 1];
					for (int i = 1; i < split.length; i++) {
						FORBIDDEN_SPAWNGROUPS[i - 1] = Integer.parseInt(split[i]);
					}
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
