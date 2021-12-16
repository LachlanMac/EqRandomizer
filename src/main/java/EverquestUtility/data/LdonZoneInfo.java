package EverquestUtility.data;

public class LdonZoneInfo {

	private String zoneName;
	private String expeditionName;
	private String lockoutName = "lockout";
	private int lockoutTimer = 210000;
	private int version = 2;
	private int bossSpawnGroupId = 0;
	private int zoneInX;
	private int zoneInY;
	private int zoneInZ;
	private int zoneInHeading;
	private String duration = "3h";
	private String npc;
	
	public final static String RETURN_ZONE = "ecommons";
	public final static int RETURN_X = -291;
	public final static int RETURN_Y = 1819;
	public final static int RETURN_Z = 4;
	public final static int RETURN_HEADING = 107;

	public LdonZoneInfo(String zoneName, String expeditionName) {
		this.zoneName = zoneName;
		this.expeditionName = expeditionName;
	}

	public void SetZoneIn(int x, int y, int z, int h) {
		this.zoneInX = x;
		this.zoneInY = y;
		this.zoneInZ = z;
		this.zoneInHeading = h;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getExpeditionName() {
		return expeditionName;
	}

	public void setExpeditionName(String expeditionName) {
		this.expeditionName = expeditionName;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getBossSpawnGroupId() {
		return bossSpawnGroupId;
	}

	public void setBossSpawnGroupId(int bossSpawnGroupId) {
		this.bossSpawnGroupId = bossSpawnGroupId;
	}

	public int getZoneInX() {
		return zoneInX;
	}

	public void setZoneInX(int zoneInX) {
		this.zoneInX = zoneInX;
	}

	public int getZoneInY() {
		return zoneInY;
	}

	public void setZoneInY(int zoneInY) {
		this.zoneInY = zoneInY;
	}

	public int getZoneInZ() {
		return zoneInZ;
	}

	public void setZoneInZ(int zoneInZ) {
		this.zoneInZ = zoneInZ;
	}

	public int getZoneInHeading() {
		return zoneInHeading;
	}

	public void setZoneInHeading(int zoneInHeading) {
		this.zoneInHeading = zoneInHeading;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getNpc() {
		return npc;
	}

	public void setNpc(String npc) {
		this.npc = npc;
	}

	public static String getReturnZone() {
		return RETURN_ZONE;
	}

	public static int getReturnX() {
		return RETURN_X;
	}

	public static int getReturnY() {
		return RETURN_Y;
	}

	public static int getReturnZ() {
		return RETURN_Z;
	}

	public static int getReturnHeading() {
		return RETURN_HEADING;
	}

}
