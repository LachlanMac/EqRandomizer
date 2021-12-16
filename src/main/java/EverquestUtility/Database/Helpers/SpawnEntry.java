package EverquestUtility.Database.Helpers;

public class SpawnEntry {

	private int id, npcId, chance;
	private Zone zone;

	public SpawnEntry(int id, int npcId, int chance) {
		this.id = id;
		this.npcId = npcId;
		this.chance = chance;
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

}
