package EverquestUtility.FactionBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import EverquestUtility.Database.EQDao;
import EverquestUtility.Database.Helpers.Faction;
import EverquestUtility.Database.Helpers.Mob;
import EverquestUtility.Database.Helpers.Zone;
import EverquestUtility.FactionBuilder.FactionBuilder.RACE;
import EverquestUtility.Util.ConfigReader;

public class BuiltFaction extends Faction {

	private EQDao daoRef;
	private Zone zone;

	private int[] levelspread1 = new int[100];
	private int[] levelspread2 = new int[100];
	private int[] levelspread3 = new int[100];
	private int[] levelspread4 = new int[100];
	private int[] levelspread5 = new int[100];
	private int[] levelspread6 = new int[100];
	private int[] levelspread7 = new int[100];
	private int[] levelspread8 = new int[100];
	private int[] levelspread9 = new int[100];
	private int[] levelspread10 = new int[100];
	private int baseFaction = 0;

	private int high = 0;
	private int low = 9999;
	private int average = 0;

	private HashMap<Integer, Integer> hash;

	public BuiltFaction(int id, String name, ArrayList<Integer> raceIds, EQDao daoRef, Zone z, int baseFaction,
			boolean uniformTexture) {
		super(id, name);

		this.baseFaction = baseFaction;
		setRaceIds(raceIds);
		FactionBuilder.CURRENT_FACTION_LIST_ID++;
		this.daoRef = daoRef;
		this.zone = z;
		buildFaction(uniformTexture);

	}

	public int[] GetLevelSpreadByIndex(int index) {

		switch (index) {
		case 0:
			return levelspread1;
		case 1:
			return levelspread2;
		case 2:
			return levelspread3;
		case 3:
			return levelspread4;
		case 4:
			return levelspread5;
		case 5:
			return levelspread6;
		case 6:
			return levelspread7;
		case 7:
			return levelspread8;
		case 8:
			return levelspread9;
		case 9:
			return levelspread10;
		default:
			return levelspread1;

		}

	}

	String getRaceIDInfo() {
		String idStr = "";

		for (Integer i : getRaceIds()) {
			idStr += " [" + i + "]";

		}

		return idStr;
	}

	public void buildElementalChaos() {
		ArrayList<Mob> shuffled = (ArrayList<Mob>) daoRef.mageMobs;
		Collections.shuffle(shuffled);
		ArrayList<Mob> potentialMobs = new ArrayList<Mob>();

		for (Mob mob : shuffled) {
			if (mob.level >= low && mob.level <= high) {
				potentialMobs.add(mob);
			}
		}

		int[] ROBES = new int[] { 16, 11, 10, 12 };
		Random rn = new Random();
		// lets get up to 10...
		for (int i = 0; i < 25; i++) {
			Mob mCopy = (Mob) potentialMobs.get(rn.nextInt(potentialMobs.size()));
			mCopy.texture = ROBES[rn.nextInt(ROBES.length)];
			mCopy.race = ConfigReader.MAGE_RACES[rn.nextInt(ConfigReader.MAGE_RACES.length)];
			mCopy.setMage(true);
			mobs.add(mCopy);
		}

	}

	public void buildNecros() {
		ArrayList<Mob> shuffled = (ArrayList<Mob>) daoRef.necroMobs;

		Collections.shuffle(shuffled);
		ArrayList<Mob> potentialMobs = new ArrayList<Mob>();

		for (Mob mob : shuffled) {
			if (mob.level >= low && mob.level <= high) {
				potentialMobs.add(mob);
			}
		}

		int[] ROBES = new int[] { 16, 11, 10, 12 };
		Random rn = new Random();
		// lets get up to 10...
		for (int i = 0; i < 25; i++) {
			Mob mCopy = (Mob) potentialMobs.get(rn.nextInt(potentialMobs.size()));
			mCopy.texture = ROBES[rn.nextInt(ROBES.length)];
			mCopy.race = ConfigReader.NECROMANCER_RACES[rn.nextInt(ConfigReader.NECROMANCER_RACES.length)];
			mCopy.setNecro(true);
			mobs.add(mCopy);
		}
		shuffled = (ArrayList<Mob>) daoRef.skMobs;

		Collections.shuffle(shuffled);
		potentialMobs = new ArrayList<Mob>();

		for (Mob mob : shuffled) {
			if (mob.level >= low && mob.level <= high) {
				potentialMobs.add(mob);
			}
		}

		// lets get up to 10...
		for (int i = 0; i < 20; i++) {
			Mob mCopy = (Mob) potentialMobs.get(rn.nextInt(potentialMobs.size()));
			mCopy.texture = 1;
			mCopy.race = ConfigReader.SHADOWKNGIHT_RACES[rn.nextInt(ConfigReader.SHADOWKNGIHT_RACES.length)];
			mCopy.setSK(true);
			mobs.add(mCopy);
		}
	}

	public void buildGoblins() {

		Random rn = new Random();
		getRaceIds().add(ConfigReader.BLOODGIL_GOBLIN_RACE);
		getRaceIds().add(ConfigReader.KUNARK_GOBLIN_RACE);
		getRaceIds().add(ConfigReader.GOBLIN_RACE);

		int randomTextureID = ConfigReader.GOBLIN_TEXTURES[rn.nextInt(ConfigReader.GOBLIN_TEXTURES.length)];

		ArrayList<Mob> filteredCopy = (ArrayList<Mob>) daoRef.filteredMobs.clone();
		Collections.shuffle(filteredCopy);

		for (Mob mobCopy : filteredCopy) { // ITERATE THROUGH THE FILITERD COPY

			int counter = 0;
			for (Integer i : getRaceIds()) { // IF THIS IS INCLUDED IN THE RACE IDS....
				// Make a new arrayList for this raceID

				if (mobCopy.race == i.intValue()) {
					Mob m = null;
					try {
						m = (Mob) mobCopy.clone();
					} catch (CloneNotSupportedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (GetLevelSpreadByIndex(counter)[m.level] <= 3) { // IF THERE ARE LESS THAN IN THIS LEVEL

						mobs.add(m); // ADD THE MONSTER

						if (m.level < low) {
							low = m.level;
						}
						if (m.level > high) {
							high = m.level;
						}

						GetLevelSpreadByIndex(counter)[m.level]++;

					}
				}
				counter++;

			}
		}

		for (Mob mm : mobs) {
			mm.race = ConfigReader.GOBLIN_RACE;
			mm.texture = randomTextureID;
		}

	}

	public void buildBandits() {

		ArrayList<String> identifiers = new ArrayList<String>();
		Random rn = new Random();

		identifiers.add(ConfigReader.BANDIT_INDICATORS[rn.nextInt(ConfigReader.BANDIT_INDICATORS.length)]);
		identifiers.add(ConfigReader.BANDIT_INDICATORS[rn.nextInt(ConfigReader.BANDIT_INDICATORS.length)]);
		identifiers.add(ConfigReader.BANDIT_INDICATORS[rn.nextInt(ConfigReader.BANDIT_INDICATORS.length)]);

		ArrayList<Mob> filteredCopy = (ArrayList<Mob>) daoRef.filteredMobs.clone();
		Collections.shuffle(filteredCopy);

		for (Mob mobCopy : filteredCopy) { // ITERATE THROUGH THE FILITERD COPY

			int counter = 0;

			for (String i : identifiers) { // IF THIS IS INCLUDED IN THE RACE IDS....
				// Make a new arrayList for this raceID

				if (mobCopy.name.toLowerCase().contains(i)) {
					Mob m = null;
					try {
						m = (Mob) mobCopy.clone();
					} catch (CloneNotSupportedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (GetLevelSpreadByIndex(counter)[m.level] <= 3) { // IF THERE ARE LESS THAN IN THIS LEVEL
						mobs.add(m); // ADD THE MONSTER
					}
				}
				counter++;

			}
			counter = 0;
			for (Integer i : getRaceIds()) { // IF THIS IS INCLUDED IN THE RACE IDS....
				// Make a new arrayList for this raceID

				if (mobCopy.race == i.intValue()) {
					Mob m = null;
					try {
						m = (Mob) mobCopy.clone();
					} catch (CloneNotSupportedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (GetLevelSpreadByIndex(counter)[m.level] <= 5) { // IF THERE ARE LESS THAN IN THIS LEVEL

						mobs.add(m); // ADD THE MONSTER
						GetLevelSpreadByIndex(counter)[m.level]++;
					}
				}
				counter++;

			}
		}

		for (Integer i : getRaceIds()) {

		}

	}

	public void buildFaction(boolean uniformTexture) {
		Random rn = new Random();
		ArrayList<ArrayList> listOfLists = new ArrayList<ArrayList>();
		for (int i = 0; i < getRaceIds().size(); i++) {
			listOfLists.add(new ArrayList<Integer>());
		}
		ArrayList<Mob> filteredCopy = (ArrayList<Mob>) daoRef.filteredMobs.clone();
		Collections.shuffle(filteredCopy);

		for (Mob mobCopy : filteredCopy) { // ITERATE THROUGH THE FILITERD COPY

			int counter = 0;
			for (Integer i : getRaceIds()) { // IF THIS IS INCLUDED IN THE RACE IDS....
				// Make a new arrayList for this raceID

				if (mobCopy.race == i.intValue()) {
					Mob m = null;
					try {
						m = (Mob) mobCopy.clone();
					} catch (CloneNotSupportedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (GetLevelSpreadByIndex(counter)[m.level] <= 3) { // IF THERE ARE LESS THAN IN THIS LEVEL

						mobs.add(m); // ADD THE MONSTER

						if (m.level < low) {
							low = m.level;
						}
						if (m.level > high) {
							high = m.level;
						}

						GetLevelSpreadByIndex(counter)[m.level]++;
						listOfLists.get(counter).add(new Integer(m.texture));
					}
				}
				counter++;

			}
		}

		int counter = 0;
		for (Integer i : getRaceIds()) {

			ArrayList<Integer> list = listOfLists.get(counter);
			if (list.size() == 0) {

				// System.exit(0);
				continue;
			}
			Integer randomVal = list.get(rn.nextInt(list.size()));
			int randomTexture = randomVal.intValue();

			for (Mob mm : mobs) {
				if (mm.race == i.intValue()) {
					mm.texture = randomTexture;
				}
			}
			counter++;
		}

		low += 3;
		high -= 3;

		// Collections.sort(mobs);
	}

	@Override
	public void updateFaction() {
		super.updateFaction();

	}

	public EQDao getDaoRef() {
		return daoRef;
	}

	public void setDaoRef(EQDao daoRef) {
		this.daoRef = daoRef;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public int getBaseFaction() {
		return baseFaction;
	}

	public void setBaseFaction(int baseFaction) {
		this.baseFaction = baseFaction;
	}

}
