package EverquestUtility.Util;

import java.util.ArrayList;

import EverquestUtility.Database.Helpers.Faction;
import EverquestUtility.Database.Helpers.Mob;

public class ComparableFactionObject {

	private Faction primaryFaction;
	private ArrayList<Mob> mobs;
	private int lowest, highest;

	public ComparableFactionObject(Faction primaryFaction, ArrayList<Mob> mobs, int lowest, int highest) {
		this.primaryFaction = primaryFaction;
		this.mobs = mobs;
		this.lowest = lowest;
		this.highest = highest;
	}

	public String toString() {

		return "Faction:" + primaryFaction.getName() + " M#:" + mobs.size() + " H:" + highest + " L:" + lowest;
	}

	public Faction getPrimaryFaction() {
		return primaryFaction;
	}

	public void setPrimaryFaction(Faction primaryFaction) {
		this.primaryFaction = primaryFaction;
	}

	public ArrayList<Mob> getMobs() {
		return mobs;
	}

	public void setMobs(ArrayList<Mob> mobs) {
		this.mobs = mobs;
	}

}
