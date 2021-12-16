package EverquestUtility.Database.Helpers;

import java.util.ArrayList;

public class LootEntry {

	private int lootdrop_id;
	private int item_id;
	private ArrayList<Integer> loottable_ids;

	public LootEntry(Item item) {
		this.item_id = item.getId();
		loottable_ids = new ArrayList<Integer>();
	}

	public ArrayList<Integer> getLoottable_ids() {
		return loottable_ids;
	}

	public void setLoottable_ids(ArrayList<Integer> loottable_ids) {
		this.loottable_ids = loottable_ids;
	}

	public int getLootdrop_id() {
		return lootdrop_id;
	}

	public void setLootdrop_id(int lootdrop_id) {
		this.lootdrop_id = lootdrop_id;
	}

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

}
