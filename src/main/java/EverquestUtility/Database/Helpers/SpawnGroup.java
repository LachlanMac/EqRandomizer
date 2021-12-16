package EverquestUtility.Database.Helpers;

import java.util.ArrayList;

import EverquestUtility.Database.EQDao;

public class SpawnGroup {
	private int id;

	private ArrayList<SpawnEntry> entries;
	private String zone;
	private Faction primaryFaction;
	private int totalSpawns = 0;
	private ArrayList<Mob> mobs;
	private ArrayList<String> zones;
	private boolean forbiddenSpawngroup = false;
	private boolean isWaterSpawn = false;

	public SpawnGroup(int id, ArrayList<SpawnEntry> entries, String zone) {
		this.id = id;
		this.entries = entries;
		this.zone = zone;
		zones = new ArrayList<String>();
		mobs = new ArrayList<Mob>();

	}

	public SpawnGroup(int id) {
		this.id = id;
		mobs = new ArrayList<Mob>();
		entries = new ArrayList<SpawnEntry>();
		zones = new ArrayList<String>();
	}

	public SpawnGroup(int id, String zone) {
		this.id = id;
		this.zone = zone;
		entries = new ArrayList<SpawnEntry>();
		mobs = new ArrayList<Mob>();
		zones = new ArrayList<String>();

	}

	public Faction getPrimaryFaction() {
		return primaryFaction;
	}

	public ArrayList<String> GetZones() {
		return zones;
	}

	public void checkForDiscrepencies() {

		ArrayList<Integer> entryIds = new ArrayList<Integer>();
		for (Mob m : mobs) {
			entryIds.add(m.id);
		}
		for (SpawnEntry se : entries) {

			boolean exists = false;
			for (Integer i : entryIds) {
				if (se.getNpcId() == i) {
					exists = true;
				}
			}
			if (!exists) {

				// System.out.println("PROBLEM: " + se.getNpcId());

			}

		}

	}

	public boolean isTranslocator() {
		for (Mob m : mobs) {
			if (m.name.toLowerCase().contains("translocator")) {
				return true;
			}
		}
		return false;
	}

	public void setPrimaryFaction(EQDao dao) {

		primaryFaction = dao.GetFactionFromSpawnGroup(this);

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void AddEntry(SpawnEntry se) {

		for (SpawnEntry e : entries) {
			if (se.getId() == e.getId() && se.getNpcId() == e.getNpcId())
				return;
		}

		entries.add(se);
	}

	public ArrayList<SpawnEntry> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<SpawnEntry> entries) {
		this.entries = entries;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (entries == null) {
			entries = new ArrayList<SpawnEntry>();
		}
		for (SpawnEntry se : entries) {
			sb.append(" " + se.getId() + ":" + se.getNpcId());
		}
		return zone + " SPAWN GROUP:" + id + "(" + sb.toString() + ")";

	}

	public String getFormatZones() {

		StringBuffer sb = new StringBuffer();
		for (String z : zones) {
			sb.append(z);
		}
		return sb.toString();

	}

	public void addZone(String zone) {

		boolean contains = false;
		for (String z : zones) {
			if (z.toLowerCase().trim().contains(zone.toLowerCase().trim()))
				contains = true;
		}
		if (!contains) {
			zones.add(zone);
		}

	}

	public void addMob(Mob m) {
		if (m.underwater != 0) {
			isWaterSpawn = true;
		} else {
			isWaterSpawn = false;
		}
		mobs.add(m);
	}

	public int getTotalSpawns() {
		return totalSpawns;
	}

	public void addTotalSpawn() {
		totalSpawns++;
	}

	public ArrayList<Mob> getMobs() {
		return mobs;
	}

	public void setMobs(ArrayList<Mob> mobs) {
		this.mobs = mobs;
	}

	public void setPrimaryFaction(Faction primaryFaction) {
		this.primaryFaction = primaryFaction;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public boolean isForbiddenSpawngroup() {
		return forbiddenSpawngroup;
	}

	public void forbid() {
		this.forbiddenSpawngroup = true;
	}

	public boolean isWaterSpawn() {
		return isWaterSpawn;
	}

	public void setWaterSpawn(boolean isWaterSpawn) {
		this.isWaterSpawn = isWaterSpawn;
	}
}
