package EverquestUtility.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigReader {

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

	// ANIMALS
	public static int LION_RACE = 50;
	public static int WOLF_RACE = 42;
	public static int BEAR_RACE = 43;
	public static int PUMA_RACE = 439;// 76
	public static int OWLBEAR_RACE = 206;
	public static int RHINO_RACE = 135;
	public static int MAMMOTH_RACE = 107;
	public static int SABERTOOTH_RACE = 119;
	public static int WALRUS_RACE = 117;
	public static int TIGER_RACE = 63;
	public static int GRIFFON_RACE = 47;
	public static int GORILLA_RACE = 41;
	public static int ROCK_HOPPER_RACE = 200;
	public static int RAPTOR_RACE = 163;
	public static int ZELNIAK_RACE = 222;
	public static int SONIC_WOLF_RACE = 232;
	public static int YETI_RACE = 138;

	public static int[] ANIMAL_RACES = new int[] { LION_RACE, WOLF_RACE, BEAR_RACE, PUMA_RACE, OWLBEAR_RACE, RHINO_RACE,
			MAMMOTH_RACE, SABERTOOTH_RACE, WALRUS_RACE, TIGER_RACE, GRIFFON_RACE, GORILLA_RACE, ROCK_HOPPER_RACE,
			RAPTOR_RACE, ZELNIAK_RACE, SONIC_WOLF_RACE, YETI_RACE };

	// PLANTS
	public static int PLANT_RACE = 162;
	public static int MUSHROOM_RACE = 227;
	public static int CACTUS_RACE = 167;
	public static int TREANT_RACE = 64;
	public static int ENT_RACE = 244;
	public static int MUSHROOM_MAN_RACE = 28;
	public static int SUNFLOWER_RACE = 225;

	public static int[] PLANT_RACES = new int[] { PLANT_RACE, MUSHROOM_RACE, CACTUS_RACE, TREANT_RACE, ENT_RACE,
			MUSHROOM_MAN_RACE, SUNFLOWER_RACE };

	public static int FAIRY_RACE = 25;
	public static int PIXIE_RACE = 56;
	public static int DRIXIE_RACE = 113;
	public static int UNICORN_RACE = 124;
	public static int FAE_DRAKE_RACE = 154;
	public static int PEGASUS_RACE = 125;
	public static int BROWNIE_RACE = 15;

	public static int[] FEY_RACES = new int[] { FAIRY_RACE, PIXIE_RACE, DRIXIE_RACE, UNICORN_RACE, FAE_DRAKE_RACE,
			PEGASUS_RACE, BROWNIE_RACE };

	public static int IKSAR_HAND_RACE = 166;
	public static int HAND_RACE = 80;
	public static int GOO_RACE = 145;
	public static int DEVOURER_RACE = 159;
	public static int HOLGRESH_RACE = 168;
	public static int TENTACLE_RACE = 3;
	public static int EVIL_EYE_RACE = 21;
	public static int GARGOYLE_RACE = 29;
	public static int MINOTAUR_RACE = 53;

	public static int[] MINOR_DUNGEON_RACES = new int[] { IKSAR_HAND_RACE, HAND_RACE, GOO_RACE, DEVOURER_RACE,
			HOLGRESH_RACE, TENTACLE_RACE, EVIL_EYE_RACE, GARGOYLE_RACE, MINOTAUR_RACE };

	// VERMIN_juggernaut
	public static int BAT_RACE = 34;
	public static int SNAKE_RACE = 37;
	public static int RAT_RACE = 36;
	public static int SKUNK_RACE = 83;
	public static int BUNNY_RACE = 176;
	public static int COCKATRICE_RACE = 96;
	public static int ARMADILLO_RACE = 87;

	public static int[] VERMIN_RACES = new int[] { BAT_RACE, SNAKE_RACE, RAT_RACE, SKUNK_RACE, BUNNY_RACE,
			COCKATRICE_RACE, ARMADILLO_RACE };

	// BUGS
	public static int LEECH_RACE = 104;
	public static int LIGHT_CRAWLER_RACE = 223;
	public static int SPIDER_RACE = 38;
	public static int BEETLE_RACE = 22;
	public static int RHINO_BEETLE_RACE = 207;
	public static int MOSQUITO_RACE = 134;
	public static int MUCK_DIGGER_RACE = 201;
	public static int SILK_WORM_RACE = 203;
	public static int SCORPION_RACE = 129;
	public static int WASP_RACE = 109;

	public static String[] NORMAL_MOB_PREFIXES = new String[] { "AN_", "A_" };
	public static String[] NORMAL_MOB_INDICATORS = new String[] { "orc", "gnoll", "skeletal", "skeleton", "tormented_",
			"crypt_", "froglok", "remains", "greater", "lesser", "kobold", "undead", "_summoning", "tortured",
			"_warrior", "_drixie", "decayed_", "fallen_", "_guardian", "xi_", "_juggernaut", "ravener", "_ancient_",
			"rotting", "risen", "zombie", "minion", "warder_of", "_elite_", "messenger", "mystic_of", "goblin",
			"tainted", "erudite", "servant_of" };

	public static int[] BUG_RACES = new int[] { LEECH_RACE, LIGHT_CRAWLER_RACE, SPIDER_RACE, BEETLE_RACE,
			RHINO_BEETLE_RACE, MOSQUITO_RACE, MUCK_DIGGER_RACE, SILK_WORM_RACE, SCORPION_RACE, WASP_RACE };

	// DRAGONS
	public static int DRAKE_RACE = 89;
	public static int WURM_RACE = 158;
	// MONSTER RACES
	public static int DROLVARG_RACE = 133;
	public static int GNOLL_RACE = 39;
	public static int ORC_RACE = 54;
	public static int GOBLIN_RACE = 40;
	public static int KUNARK_GOBLIN_RACE = 137;
	public static int KOBOLD_RACE = 48;
	public static int NEW_KOBOLD_RACE = 455;
	public static int OTHMIR_RACE = 190;
	public static int GRIMLING_RACE = 202;
	public static int FROGLOK_RACE = 27;

	public static int BURYNAI_RACE = 144;
	public static int NETHERBIAN_RACE = 229;
	public static int GALORIAN_RACE = 228;
	public static int WALRUS_MAN_RACE = 191;
	public static int RAT_MAN_RACE = 156;
	public static int SARNAK_RACE = 131;
	public static int LIZARD_MAN_RACE = 51;
	public static int AVIAK_RACE = 13;
	public static int TEGI_RACE = 215;

	public static int[] ANIMAL_MONSTER_RACES = new int[] { RAT_MAN_RACE, WALRUS_MAN_RACE, OTHMIR_RACE, AVIAK_RACE };

	public static int[] DUNGEON_MONSTER_RACES = new int[] { DROLVARG_RACE, GNOLL_RACE, ORC_RACE, GOBLIN_RACE,
			BURYNAI_RACE, KUNARK_GOBLIN_RACE, KOBOLD_RACE, NEW_KOBOLD_RACE, GRIMLING_RACE, FROGLOK_RACE,
			NETHERBIAN_RACE, GALORIAN_RACE, SARNAK_RACE, LIZARD_MAN_RACE, TEGI_RACE };

	public static int[] MONSTER_RACES = new int[] { DROLVARG_RACE, GNOLL_RACE, ORC_RACE, GOBLIN_RACE,
			KUNARK_GOBLIN_RACE, KOBOLD_RACE, NEW_KOBOLD_RACE, OTHMIR_RACE, GRIMLING_RACE, FROGLOK_RACE, MINOTAUR_RACE,
			EVIL_EYE_RACE, BURYNAI_RACE, NETHERBIAN_RACE, GALORIAN_RACE, WALRUS_RACE, RAT_MAN_RACE, SARNAK_RACE,
			LIZARD_MAN_RACE, AVIAK_RACE, TEGI_RACE };

	// GOLEM RACES
	public static int IKSAR_GOLEM_RACE = 160;
	public static int SARNAK_GOLEM_RACE = 164;
	public static int ROCK_GEM_RACE = 178;
	public static int MUDMAN_RACE = 17;

	public static int[] GOLEM_RACES = new int[] { IKSAR_GOLEM_RACE, SARNAK_GOLEM_RACE, ROCK_GEM_RACE, MUDMAN_RACE };

	// GIANT RACES
	public static int FROST_GIANT_RACE = 188;
	public static int STORM_GIANT_RACE = 189;
	public static int FOREST_GIANT_RACE = 140;
	public static int CYCLOPS_RACE = 18;

	public static int[] GIANT_RACES = new int[] { FROST_GIANT_RACE, STORM_GIANT_RACE, FOREST_GIANT_RACE, CYCLOPS_RACE };
	// UNDEAD RACES
	public static int SKELETON_RACE = 161;
	public static int SKELETON_RACE_2 = 367;
	public static int GHOUL_RACE = 33;
	public static int GHOST_RACE = 118;
	public static int ZOMBIE_RACE = 70;
	public static int ELF_VAMPIRE_RACE = 98;
	public static int UNDEAD_FROGLOK_RACE = 26;
	public static int SHADE_RACE = 224;
	public static int SPECTRE_RACE = 85;
	public static int DWARF_SPIRIT_RACE = 117;
	public static int SCARECROW_RACE = 82;
	public static int SARNAK_SKELETON_RACE = 155;
	public static int VAMPIRE_RACE = 208;

	public static int[] UNDEAD_RACES = new int[] { SKELETON_RACE, SKELETON_RACE_2, GHOUL_RACE, GHOST_RACE, ZOMBIE_RACE,
			ELF_VAMPIRE_RACE, UNDEAD_FROGLOK_RACE, SHADE_RACE, SPECTRE_RACE, DWARF_SPIRIT_RACE, SCARECROW_RACE,
			SARNAK_SKELETON_RACE, VAMPIRE_RACE };

	// public static int

	public static void ReadConfig() {

		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("data/noobzones.txt")));

			String line;
			while ((line = br.readLine()) != null) {

				String[] split = line.split(",");

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
				}
			}

			System.out.println(NOOB_REPLACE_FACTION_IDS.length);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
