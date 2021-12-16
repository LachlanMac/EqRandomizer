package EverquestUtility.Database.Helpers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import EverquestUtility.FactionBuilder.FactionBuilder;
import EverquestUtility.Util.ConfigReader;
import EverquestUtility.Util.Debug;

public class Mob implements Comparable, Cloneable {

	private boolean isMage = false;
	private boolean isNecro = false;
	private boolean isSK = false;

	public static ArrayList<Mob> Mobs = new ArrayList<Mob>();

	public static ArrayList<Mob> GetMobsByLevel(int level) {

		ArrayList<Mob> possible = new ArrayList<Mob>();
		for (Mob m : Mobs) {

			if (m.level == level) {
				possible.add(m);
			}

		}
		if (possible.size() == 0) {
			possible.add(Mobs.get(0));
		}

		return possible;

	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public static Mob GetMobByID(int id) {

		Mob mob = null;

		for (Mob m : Mobs) {

			if (m.getId() == id)
				return m;

		}

		return mob;

	}

	public int id, level, race, Class, bodytype, hp, mana, gender, texture, hp_regen_rate, mana_regen_rate,
			loottable_id, alt_currency_id, npc_spells_id, npc_spells_effects_id, mindmg, maxdmg, attack_count,
			aggroRadius, assistRadius, prim_melee_type, sec_melee_type, ranged_type, runspeed, MR, CR, DR, FR, PR,
			Corrup, PhR, see_invis, see_invis_undead, qglobal, AC, npc_aggro, spawn_limit, attack_speed, attack_delay,
			finadable, STR, STA, DEX, AGI, _INT, WIS, CHA, see_hide, see_improved_hide, trackable, isbot, exclude, atk,
			accuracy, avoidance, slow_mitigation, version, maxlevel, emoteid, spellscale, healscale, no_target_hotkey,
			light, walkspeed, peqid, unique_, fixed, ignore_despawn, show_name, charm_ac, charm_min_dmg, charm_max_dmg,
			charm_attack_delay, charm_accuracy_rating, charm_avoidance_rating, charm_atk, skip_global_loot, rarespawn,
			raidtarget, untargetable, stuck_behavior, model, flymode, always_aggro, exp_mod, size, findable, scalerate,
			privatecorpse, unique_spawn_by_name, underwater, isquest, adventureID, trapID, factionID, merchantID,
			globalQ;

	public String name, npcSpecialAttacks, specialAbilities, lastName;
	private NPCFaction faction;
	private Faction primaryFaction;
	private Random rn;

	String insertString = "INSERT INTO `npc_types` VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

	public boolean FilterMob() {
		boolean filter = false;

		if (name.toLowerCase().contains("_pet_"))
			return true;
		if (name.contains("Animation")) {
			return true;
		}
		if (lastName != "")
			return true;
		if (merchantID != 0) {
			return true;
		}
		if (level >= 80) {
			return true;
		}
		if (adventureID != 0) {
			return true;
		}
		if (runspeed <= 0) {
			return true;
		}
		if (findable != 0) {
			return true;
		}
		if (race == 73 || race == 72)
			return true;
		return filter;
	}

	public void writeMob(java.sql.Connection connection) {
		try {

			if (Debug.SKIP_SQL) {
				return;
			}

			PreparedStatement stmt = connection.prepareStatement(insertString);

			stmt.setInt(1, id);
			stmt.setString(2, name);
			stmt.setString(3, lastName);
			stmt.setInt(4, level);
			stmt.setInt(5, race);
			stmt.setInt(6, Class);
			stmt.setInt(7, bodytype);
			stmt.setInt(8, hp);
			stmt.setInt(9, mana);
			stmt.setInt(10, gender);
			stmt.setInt(11, texture);
			stmt.setInt(12, 0);
			stmt.setInt(13, 0);
			stmt.setInt(14, size);
			stmt.setInt(15, hp_regen_rate);
			stmt.setInt(16, mana_regen_rate);
			stmt.setInt(17, loottable_id);
			stmt.setInt(18, merchantID);
			stmt.setInt(19, alt_currency_id);
			stmt.setInt(20, npc_spells_id);
			stmt.setInt(21, npc_spells_effects_id);
			stmt.setInt(22, factionID);
			stmt.setInt(23, adventureID);
			stmt.setInt(24, trapID);
			stmt.setInt(25, mindmg);
			stmt.setInt(26, maxdmg);
			stmt.setInt(27, attack_count);
			stmt.setString(28, npcSpecialAttacks);
			stmt.setString(29, specialAbilities);

			stmt.setInt(30, rn.nextInt(15) + 30);
			stmt.setInt(31, rn.nextInt(15) + 30);
			stmt.setInt(32, 0);
			stmt.setInt(33, 0);
			stmt.setInt(34, 0);
			stmt.setInt(35, 0);
			stmt.setInt(36, 0);
			stmt.setInt(37, 0);
			stmt.setInt(38, 0);
			stmt.setInt(39, 0);
			stmt.setInt(40, 0);
			stmt.setInt(41, 0);
			stmt.setInt(42, 0);
			stmt.setInt(43, 0);
			stmt.setInt(44, 0);
			stmt.setInt(45, 0);
			stmt.setInt(46, 0);
			stmt.setInt(47, 0);
			stmt.setInt(48, 0);
			stmt.setInt(49, prim_melee_type);
			stmt.setInt(50, sec_melee_type);
			stmt.setInt(51, ranged_type);
			stmt.setInt(52, runspeed);
			stmt.setInt(53, MR);
			stmt.setInt(54, CR);
			stmt.setInt(55, DR);
			stmt.setInt(56, FR);
			stmt.setInt(57, PR);
			stmt.setInt(58, Corrup);
			stmt.setInt(59, PhR);
			stmt.setInt(60, see_invis);
			stmt.setInt(61, see_invis_undead);
			stmt.setInt(62, qglobal);
			stmt.setInt(63, AC);
			stmt.setInt(64, npc_aggro);
			stmt.setInt(65, spawn_limit);
			stmt.setInt(66, attack_speed);
			stmt.setInt(67, attack_delay);
			stmt.setInt(68, findable);
			stmt.setInt(69, STR);
			stmt.setInt(70, STA);
			stmt.setInt(71, DEX);
			stmt.setInt(72, AGI);
			stmt.setInt(73, _INT);
			stmt.setInt(74, WIS);
			stmt.setInt(75, CHA);
			stmt.setInt(76, see_hide);
			stmt.setInt(77, see_improved_hide);
			stmt.setInt(78, trackable);
			stmt.setInt(79, isbot);
			stmt.setInt(80, exclude);
			stmt.setInt(81, atk);
			stmt.setInt(82, accuracy);
			stmt.setInt(83, avoidance);
			stmt.setInt(84, slow_mitigation);
			stmt.setInt(85, version);
			stmt.setInt(86, maxlevel);
			stmt.setInt(87, scalerate);
			stmt.setInt(88, privatecorpse);
			stmt.setInt(89, unique_spawn_by_name);
			stmt.setInt(90, underwater);
			stmt.setInt(91, isquest);
			stmt.setInt(92, emoteid);
			stmt.setInt(93, spellscale);
			stmt.setInt(94, healscale);
			stmt.setInt(95, no_target_hotkey);
			stmt.setInt(96, raidtarget);
			stmt.setInt(97, 0);
			stmt.setInt(98, 0);
			stmt.setInt(99, 0);
			stmt.setInt(100, 0);
			stmt.setInt(101, 0);
			stmt.setInt(102, light);
			stmt.setInt(103, walkspeed);
			stmt.setInt(104, peqid);
			stmt.setInt(105, unique_);
			stmt.setInt(106, fixed);
			stmt.setInt(107, ignore_despawn);
			stmt.setInt(108, show_name);
			stmt.setInt(109, untargetable);
			stmt.setInt(110, charm_ac);
			stmt.setInt(111, charm_min_dmg);
			stmt.setInt(112, charm_max_dmg);
			stmt.setInt(113, charm_attack_delay);
			stmt.setInt(114, charm_accuracy_rating);
			stmt.setInt(115, charm_avoidance_rating);
			stmt.setInt(116, charm_atk);
			stmt.setInt(117, skip_global_loot);
			stmt.setInt(118, rarespawn);
			stmt.setInt(119, stuck_behavior);
			stmt.setInt(120, model);
			stmt.setInt(121, flymode);
			stmt.setInt(122, always_aggro);
			stmt.setInt(123, exp_mod);
			stmt.execute();
			// stmt.setInt(124, value);
			// stmt.setInt(125, value);
		} catch (SQLException e) {

			System.out.println("SQL ERROR[WRITING MOB] " + e.getMessage());
			System.exit(0);

		}

	}

	public Mob(int id, String name, int level, int adventureID, int trapID, int factionID, int merchantID, int globalQ,
			int raidTarget, int untargetable, int race) {
		this.id = id;
		this.name = name;
		this.level = level;
		this.adventureID = adventureID;
		this.trapID = trapID;
		this.factionID = factionID;
		this.merchantID = merchantID;
		this.raidtarget = raidTarget;
		this.untargetable = untargetable;
		this.globalQ = globalQ;
		this.race = race;
		rn = new Random();

	}

	/*
	 * public int id, level, race, Class, bodytype, hp, mana, gender, texture,
	 * hp_regen_rate, mana_regen_rate, loottable_id, alt_currency_id, npc_spells_id,
	 * npc_spells_effects_id, mindmg, maxdmg, attack_count, aggroRadius,
	 * assistRadius, prim_melee_type, sec_melee_type, ranged_type, runspeed, MR, CR,
	 * DR, FR, PR, Corrup, PhR, see_invis, see_invis_undead, qglobal, AC, npc_aggro,
	 * spawn_limit, attack_speed, attack_delay, finadable, STR, STA, DEX, AGI, _INT,
	 * WIS, CHA, see_hide, see_improved_hide, trackable, isbot, exclude, atk,
	 * accuracy, avoidance, slow_mitigation, version, maxlevel, emoteid, spellscale,
	 * healscale, no_target_hotkey, light, walkspeed, peqid, unique_, fixed,
	 * ignore_despawn, show_name, charm_ac, charm_min_dmg, charm_max_dmg,
	 * charm_attack_delay, charm_accuracy_rating, charm_avoidance_rating, charm_atk,
	 * skip_global_loot, rarespawn, raidtarget, untargetable, stuck_behavior, model,
	 * flymode, always_aggro, exp_mod, size, findable, scalerate, privatecorpse,
	 * unique_spawn_by_name, underwater, isquest, adventureID, trapID, factionID,
	 * merchantID, globalQ;
	 * 
	 * public String name, npcSpecialAttacks, specialAbilities, lastName; private
	 * NPCFaction faction; private Faction primaryFaction;
	 */

	public boolean isProbablyNamed() {

		for (String s : ConfigReader.NORMAL_MOB_PREFIXES) {
			if (name.toLowerCase().substring(0, 3).contains(s)) {
				return false;
			}
		}

		return false;

	}

	public boolean AllowedNoobMobs() {
		boolean allowed = false;

		for (String s : ConfigReader.NORMAL_MOB_PREFIXES) {
			if (name.toLowerCase().substring(0, 3).contains(s.toLowerCase())) {
				return true;
			}
		}
		if (name.toLowerCase().contains("runaway_clockwork")) {
			return true;
		}
		return allowed;

	}

	public String toString() {

		return "[" + id + "] " + name + " lvl:" + level + "  Faction[" + primaryFaction.getId() + "]"
				+ primaryFaction.getName();

	}

	public Faction getPrimaryFaction() {
		return primaryFaction;
	}

	public void setPrimaryFaction(Faction primaryFaction) {
		this.faction.setPrimaryFaction(primaryFaction);
		this.primaryFaction = primaryFaction;
	}

	public NPCFaction getFaction() {
		return faction;
	}

	public void setFaction(NPCFaction faction) {
		this.faction = faction;
	}

	public int getGlobalQ() {
		return globalQ;
	}

	public void setGlobalQ(int globalQ) {
		this.globalQ = globalQ;
	}

	public static ArrayList<Mob> getMobs() {
		return Mobs;
	}

	public static void setMobs(ArrayList<Mob> mobs) {
		Mobs = mobs;
	}

	public int getAdventureID() {
		return adventureID;
	}

	public void setAdventureID(int adventureID) {
		this.adventureID = adventureID;
	}

	public int getTrapID() {
		return trapID;
	}

	public void setTrapID(int trapID) {
		this.trapID = trapID;
	}

	public int getFactionID() {
		return factionID;
	}

	public void setFactionID(int factionID) {
		this.factionID = factionID;
	}

	public int getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(int merchantID) {
		this.merchantID = merchantID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		FactionBuilder.CURRENT_NPC_ID++;
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRace() {
		return race;
	}

	public void setRace(int race) {
		this.race = race;
	}

	@Override
	public int compareTo(Object faction) {

		Mob f = (Mob) faction;
		if (this.getLevel() < f.getLevel()) {
			return 1;
		} else if (this.getLevel() == f.getLevel()) {
			return 0;
		} else {

			return -1;
		}

	}

	public boolean isNecro() {
		return isNecro;
	}

	public void setNecro(boolean isNecro) {
		this.isNecro = isNecro;
	}

	public boolean isMage() {
		return isMage;
	}

	public void setMage(boolean isMage) {
		this.isMage = isMage;
	}

	public boolean isSK() {
		return isSK;
	}

	public void setSK(boolean isSK) {
		this.isSK = isSK;
	}

}
