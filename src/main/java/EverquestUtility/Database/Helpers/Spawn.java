package EverquestUtility.Database.Helpers;

import java.util.ArrayList;
import java.util.List;

public class Spawn {
	private int id, spawngroupId;
	private String zone;
	private List<Mob> mobs;

	public Spawn(int id, int spawngroupId, String zone) {
		this.id = id;
		this.spawngroupId = spawngroupId;
		this.zone = zone;
		mobs = new ArrayList<Mob>();
	}

	
	
	
	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSpawngroupId() {
		return spawngroupId;
	}

	public void setSpawngroupId(int spawngroupId) {
		this.spawngroupId = spawngroupId;
	}

}
