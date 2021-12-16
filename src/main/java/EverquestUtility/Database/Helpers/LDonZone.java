package EverquestUtility.Database.Helpers;

public class LDonZone extends Zone {

	private int version;

	public LDonZone(int id, String name, int version) {
		super(id, name);
		this.version = version;
	}

}
