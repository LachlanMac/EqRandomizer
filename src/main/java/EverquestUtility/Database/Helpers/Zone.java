package EverquestUtility.Database.Helpers;

import java.util.ArrayList;
import java.util.List;

public class Zone {

	private int id;
	private String name;
	private List<Spawn> spawns;
	private List<NPCFaction> factions;
	private List<SpawnGroup> spawnGroups;
	private ArrayList<Mob> mobsInZone;
	private String longName;
	private int version;

	public Zone(int id, String name) {
		this.id = id;
		this.name = name;
		factions = new ArrayList<NPCFaction>();
		spawns = new ArrayList<Spawn>();
		mobsInZone = new ArrayList<Mob>();
		spawnGroups = new ArrayList<SpawnGroup>();
	}

	public List<SpawnGroup> getSpawnGroups() {
		return spawnGroups;
	}

	public void setSpawnGroups(List<SpawnGroup> spawnGroups) {
		this.spawnGroups = spawnGroups;
	}

	public void addSpawnGroup(SpawnGroup sg) {

		for (SpawnGroup s : spawnGroups) {

			if (s.getId() == sg.getId())
				return;

		}

		spawnGroups.add(sg);

	}

	public void addMob(Mob m) {
		mobsInZone.add(m);
	}

	public ArrayList<Mob> getMobsInZone() {
		return mobsInZone;
	}

	public void setMobsInZone(ArrayList<Mob> mobsInZone) {
		this.mobsInZone = mobsInZone;
	}

	public void addFaction(NPCFaction f) {

		if (!factions.contains(f)) {
			System.out.println("Adding Faction: " + f.getName() + " to " + name);
			factions.add(f);

		}
	}

	public List<NPCFaction> getFactions() {
		return factions;
	}

	public void addSpawn(Spawn s) {
		spawns.add(s);

	}

	public List<Spawn> getSpawns() {
		return spawns;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
