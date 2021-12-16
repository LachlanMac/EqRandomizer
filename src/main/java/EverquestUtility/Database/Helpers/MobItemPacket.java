package EverquestUtility.Database.Helpers;

public class MobItemPacket {

	private String zone;
	int chance;
	Mob m;

	public MobItemPacket(String zone, Mob m, int chance) {
		this.zone = zone;
		this.m = m;
		this.chance = chance;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public Mob getM() {
		return m;
	}

	public void setM(Mob m) {
		this.m = m;
	}

	public int getChance() {
		return chance;
	}

}
