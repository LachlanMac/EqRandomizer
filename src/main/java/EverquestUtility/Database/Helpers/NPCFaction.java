package EverquestUtility.Database.Helpers;

public class NPCFaction {

	private int id, primaryFaction;
	private String name;
	private Faction faction;

	public NPCFaction(int id, String name, int primaryFaction) {
		this.id = id;
		this.name = name;
		this.primaryFaction = primaryFaction;
	}

	public Faction getPrimaryFaction() {
		return faction;
	}

	public void setPrimaryFaction(Faction faction) {
		this.faction = faction;
	}

	public int getPrimaryFactionID() {
		return primaryFaction;
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

}
