package EverquestUtility.FactionBuilder;

import java.util.ArrayList;
import java.util.Collections;

import EverquestUtility.Database.EQDao;
import EverquestUtility.Database.Helpers.Faction;
import EverquestUtility.Database.Helpers.Mob;
import EverquestUtility.Database.Helpers.Zone;
import EverquestUtility.Util.ConfigReader;

public class BuiltFaction extends Faction {

	private EQDao daoRef;
	private Zone zone;

	private ArrayList<Integer> raceIds;
	private int[] levelspread = new int[100];

	public BuiltFaction(int id, String name, ArrayList<Integer> raceIds, EQDao daoRef, Zone z) {
		super(id, name);

		System.out.println(name);
		this.raceIds = raceIds;
		FactionBuilder.CURRENT_FACTION_LIST_ID++;
		this.daoRef = daoRef;
		this.zone = z;
		mobs = new ArrayList<Mob>();
		buildFaction();
		Collections.sort(mobs);

	}

	public void buildAnimalFaction() {

	}

	public void buildFaction() {
		ArrayList<Mob> filteredCopy = (ArrayList<Mob>) daoRef.filteredMobs.clone();
		Collections.shuffle(filteredCopy);
		for (Mob m : filteredCopy) {
			for (Integer i : raceIds) {
				if (m.race == i.intValue()) {
					if (levelspread[m.level] <= 6) {
						mobs.add(m);
						levelspread[m.level]++;
					}
				}
			}
		}
	}

	public void setUnique() {

		for (Mob m : mobs) {
			// name check...
			String name = m.name.toLowerCase();
			String subName = name.substring(0, 4);
			if (name.contains("fabled")) {
				namedMobs.add(m);
				continue;
			}
			if (subName.contains("a_") || subName.contains("an_")) {
				normalMobs.add(m);
				continue;
			}

			if (name.contains("#")) {
				if (subName.contains("#a_") || subName.contains("#an_")) {
					normalMobs.add(m);
					continue;
				} else {
					namedMobs.add(m);
					continue;
				}
			}

			if (m.spawn_limit == 1) {
				namedMobs.add(m);
				continue;
			}
			boolean probablyNamed = true;
			for (String s : ConfigReader.NORMAL_MOB_INDICATORS) {

				if (name.contains("sorceror_")) {
					break;
				}

				if (name.contains(s)) {
					normalMobs.add(m);
					probablyNamed = false;
					break;
				}

			}
			if (probablyNamed)
				namedMobs.add(m);
			// check for other normal shit

		}

		for (Mob m : namedMobs) {
			m.unique_spawn_by_name = 1;
			m.unique_ = 1;
			m.spawn_limit = 1;
		}

	}

	public void buildNecroDungeon() {

		for (Mob m : daoRef.filteredMobs) {
			if (m.getName().contains("necromancer") && !m.getName().contains("pet")) {
				mobs.add(m);
			}
		}

	}

	public void buildBandits() {

		for (Mob m : daoRef.filteredMobs) {
			if (m.getName().toLowerCase().contains("loda_kai") || m.getName().toLowerCase().contains("bandit")
					|| m.getName().toLowerCase().contains("recondite")) {
				mobs.add(m);
			}
		}

	}

	public ArrayList<Mob> GetMobsToWrite() {
		return mobs;
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

}
