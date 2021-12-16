package EverquestUtility.FactionBuilder;

import EverquestUtility.Database.Helpers.Faction;
import EverquestUtility.Database.Helpers.Mob;

public class FactionBundle {

	private Faction original;
	private Faction replacement;

	public FactionBundle(Faction orginal) {
		this.original = orginal;
	}

	public Faction getOriginal() {
		return original;
	}

	public void setOriginal(Faction original) {
		this.original = original;
	}

	public Faction getReplacement() {
		return replacement;
	}

	public void setReplacement(Faction replacement) {
		this.replacement = replacement;
	}

	public String toString() {
		return "Replacing " + original.getName() + "[" + original.getId() + "]  with " + replacement.getName();
	}

}
