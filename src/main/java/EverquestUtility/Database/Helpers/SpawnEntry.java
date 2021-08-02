package EverquestUtility.Database.Helpers;

public class SpawnEntry {
	
	private int id, npcId;
	private Zone zone;
	public SpawnEntry(int id, int npcId)
	{
		this.id =id;
		this.npcId = npcId;
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
