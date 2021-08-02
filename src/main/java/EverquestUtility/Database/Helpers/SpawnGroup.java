package EverquestUtility.Database.Helpers;

import java.util.ArrayList;

import EverquestUtility.Database.EQDao;

public class SpawnGroup {
	private int id;

	private ArrayList<SpawnEntry> entries;
	private String zone;
	private Faction primaryFaction;

	private ArrayList<Mob> mobs;

	public SpawnGroup(int id, ArrayList<SpawnEntry> entries, String zone) {
		this.id = id;
		this.entries = entries;
		this.zone = zone;
		mobs = new ArrayList<Mob>();

	}

	public SpawnGroup(int id) {
		this.id = id;
		mobs = new ArrayList<Mob>();
		entries = new ArrayList<SpawnEntry>();
	}

	public SpawnGroup(int id, String zone) {
		this.id = id;
		this.zone = zone;
		entries = new ArrayList<SpawnEntry>();
		mobs = new ArrayList<Mob>();

	}

	public Faction getPrimaryFaction() {
		return primaryFaction;
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

	public void addMob(Mob m) {

		mobs.add(m);
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

}
