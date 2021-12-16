package EverquestUtility.Util;

import java.util.ArrayList;
import java.util.Random;

import EverquestUtility.Database.Helpers.Faction;
import EverquestUtility.Database.Helpers.Mob;
import EverquestUtility.Database.Helpers.SpawnEntry;
import EverquestUtility.FactionBuilder.FactionBundle;

public class Util {

	public static String[] normals = new String[] { "hunter", "elite", "large", "greater", "lesser", "small", "orc",
			"pawn" };

	public static boolean IsProbablyNamed(String name) {

		if (name.length() <= 3) {
			System.out.println("HOW DID THIS MAKE IT??? " + name);
		}

		String firstThree = name.substring(0, 3).toLowerCase();

		if (firstThree.contains("a_") || firstThree.contains("an_")) {
			return false;
		}

		for (String s : normals) {

			if (name.toLowerCase().contains(s)) {
				return false;
			}

		}

		return true;
	}

	public static FactionBundle GetPossibleFaction(Mob m, ArrayList<FactionBundle> factions) {

		for (FactionBundle faction : factions) {
			if (m.getPrimaryFaction().getId() == faction.getOriginal().getId()) {
				return faction;
			}
		}

		return null;

	}

	public static int[] GetChancesFor(int x) {

		Random rn = new Random();
		int randomNumber = rn.nextInt(3) + 1;

		int[] chances = new int[x];

		switch (x) {

		case 1:
			chances[0] = 100;
			return chances;
		case 2:
			if (randomNumber == 1) {
				chances[0] = 50;
				chances[1] = 50;
			} else if (randomNumber == 2) {
				chances[0] = 40;
				chances[1] = 60;
			} else if (randomNumber == 3) {
				chances[0] = 20;
				chances[1] = 80;
			} else {
				System.out.println("WHAT???" + x + ", " + randomNumber);
				System.exit(0);
			}
			return chances;
		case 3:

			if (randomNumber == 1) {
				chances[0] = 33;
				chances[1] = 33;
				chances[2] = 33;
			} else if (randomNumber == 2) {
				chances[0] = 20;
				chances[1] = 40;
				chances[2] = 40;
			} else if (randomNumber == 3) {
				chances[0] = 70;
				chances[1] = 10;
				chances[2] = 20;

			} else {
				System.out.println("WHAT???" + x + ", " + randomNumber);
				System.exit(0);
			}
			return chances;
		case 4:
			if (randomNumber == 1) {
				chances[0] = 25;
				chances[1] = 25;
				chances[2] = 25;
				chances[3] = 25;
			} else if (randomNumber == 2) {
				chances[0] = 10;
				chances[1] = 10;
				chances[2] = 40;
				chances[3] = 40;
			} else if (randomNumber == 3) {
				chances[0] = 60;
				chances[1] = 20;
				chances[2] = 10;
				chances[3] = 10;
			} else {
				System.out.println("WHAT???" + x + ", " + randomNumber);
				System.exit(0);
			}
			return chances;
		case 5:
			if (randomNumber == 1) {
				chances[0] = 20;
				chances[1] = 20;
				chances[2] = 20;
				chances[3] = 20;
				chances[4] = 20;
			} else if (randomNumber == 2) {
				chances[0] = 50;
				chances[1] = 10;
				chances[2] = 20;
				chances[3] = 10;
				chances[4] = 10;
			} else if (randomNumber == 3) {
				chances[0] = 20;
				chances[1] = 40;
				chances[2] = 20;
				chances[3] = 10;
				chances[4] = 10;
			} else {
				System.out.println("WHAT???" + x + ", " + randomNumber);
				System.exit(0);
			}
			return chances;

		case 6:
			chances[0] = 15;
			chances[1] = 15;
			chances[2] = 15;
			chances[3] = 15;
			chances[4] = 20;
			chances[5] = 20;

			return chances;

		case 7:
			chances[0] = 15;
			chances[1] = 15;
			chances[2] = 15;
			chances[3] = 15;
			chances[4] = 20;
			chances[5] = 10;
			chances[6] = 10;
			return chances;

		case 8:
			chances[0] = 10;
			chances[1] = 10;
			chances[2] = 15;
			chances[3] = 15;
			chances[4] = 20;
			chances[5] = 10;
			chances[6] = 10;
			chances[7] = 10;
			return chances;
		case 9:
			chances[0] = 10;
			chances[1] = 10;
			chances[2] = 10;
			chances[3] = 10;
			chances[4] = 10;
			chances[5] = 10;
			chances[6] = 10;
			chances[7] = 10;
			chances[8] = 20;
			return chances;
		case 10:
			chances[0] = 10;
			chances[1] = 10;
			chances[2] = 10;
			chances[3] = 10;
			chances[4] = 10;
			chances[5] = 10;
			chances[6] = 10;
			chances[7] = 10;
			chances[8] = 10;
			chances[9] = 10;
			return chances;
		case 11:
			chances[0] = 10;
			chances[1] = 10;
			chances[2] = 10;
			chances[3] = 10;
			chances[4] = 10;
			chances[5] = 10;
			chances[6] = 10;
			chances[7] = 10;
			chances[8] = 10;
			chances[9] = 5;
			chances[10] = 5;
			return chances;
		case 12:
			chances[0] = 10;
			chances[1] = 10;
			chances[2] = 10;
			chances[3] = 10;
			chances[4] = 10;
			chances[5] = 10;
			chances[6] = 10;
			chances[7] = 10;
			chances[8] = 5;
			chances[9] = 5;
			chances[10] = 5;
			chances[11] = 5;
			return chances;

		default:
			int total = 0;
			for (int i = 0; i < chances.length - 1; i++) {

				chances[i] = 3;
				total += 3;

			}

			if (total >= 100) {

				total = 100;
			}

			chances[chances.length - 1] = 100 - total;

			return chances;
		}

	}

	public static int caclulateExtraSpawns(int totalSpawns) {

		if (totalSpawns <= 1) {
			return 0;
		}
		if (totalSpawns < 2 && totalSpawns >= 4) {
			return 2;
		}
		if (totalSpawns > 5 && totalSpawns <= 8) {
			return 4;
		} else {
			return 5;
		}

	}

}
