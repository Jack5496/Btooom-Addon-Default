package com.jack.btooom.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.jack.btooom.Core;
import com.jack.btooom.beams.Barrier;
import com.jack.btooom.beams.BlazingGas;
import com.jack.btooom.beams.Cracker;
import com.jack.btooom.beams.Flame;
import com.jack.btooom.beams.Homing;
import com.jack.btooom.beams.Implosion;
import com.jack.btooom.beams.RemoteControl;
import com.jack.btooom.beams.Timer;
import com.jack.btooom.heads.Heads;

public class ConfigLoader {

	public static void loadAllConfigs() {
		loadConfig();
		loadMessages();
		loadSignConfigs();

		loadHeadList();
		loadBIMList();
		
		BIMStore.loadAllConfigs();
	}

	public static void loadConfig() {
		File adr = Core.configfile;
		if (!adr.exists()) {
			try {
				adr.createNewFile();
				YamlConfiguration temp = YamlConfiguration.loadConfiguration(adr);
				temp.addDefault("Decay_Tree", "true");
				temp.addDefault("Tree_Menu", "true");
				temp.addDefault("Tree_PvP", "false");
				temp.addDefault("Damage_All_Blocks_on_Explosion", "true");
				temp.addDefault("Only_Damage_Tree_on_Explosion", "false");
				temp.addDefault("Sapling_must_Set_Next_To_Log", "true");
				temp.addDefault("Debug_Mode", "false");
				temp.addDefault("Only_Walk_on_Log", "true");
				temp.addDefault("Create_Sign_On_Knot", "false");
				temp.options().copyDefaults(true);
				temp.save(adr);
			} catch (IOException ex) {

			}
		}
		YamlConfiguration ycf = YamlConfiguration.loadConfiguration(adr);
		for (String name : ycf.getKeys(false)) {

			try {
				if (name.equalsIgnoreCase("Decay_Tree")) {
					Core.configs.setDecay_tree(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Tree_Menu")) {
					Core.configs.setTree_menu(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Tree_PvP")) {
					Core.configs.setTree_Pvp(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Damage_All_Blocks_on_Explosion")) {
					Core.configs.setDamage_All_Blocks_on_Explosion(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Only_Damage_Tree_on_Explosion")) {
					Core.configs.setOnly_Damage_Tree_on_Explosion(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Only_Walk_on_Log")) {
					Core.configs.setOnly_Walk_on_Log(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Sapling_must_Set_Next_To_Log")) {
					Core.configs.setSapling_must_Set_Next_To_Log(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Debug_Mode")) {
					Core.configs.setDebug_Mode(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Create_Sign_On_Knot")) {
					Core.configs.setCreate_Sign_On_Knot(ycf.getString(name));
				}

			} catch (Exception ex) {
				ycf.set(name, "INVAILD");
			}
		}
	}

	public static void setConfigsInYML(String name, String value) {
		File adr = Core.configfile;
		if (adr.exists()) {
			try {
				YamlConfiguration temp = YamlConfiguration.loadConfiguration(adr);
				temp.set(name, value);
				temp.options().copyDefaults(true);
				temp.save(adr);
			} catch (IOException ex) {

			}
		} else {
			loadConfig();
		}
	}

	public static void loadSignConfigs() {
		File adr = Core.signsfile;
		if (!adr.exists()) {
			try {
				adr.createNewFile();
				YamlConfiguration temp = YamlConfiguration.loadConfiguration(adr);
				temp.addDefault("Tree Text", "Tree");
				temp.addDefault("Tree Player Text", "T Player");
				temp.addDefault("To Root Text", "To Root");
				temp.addDefault("Total Tree's", "Roots");

				temp.options().copyDefaults(true);
				temp.save(adr);
			} catch (IOException ex) {

			}
		}
		YamlConfiguration ycf = YamlConfiguration.loadConfiguration(adr);
		for (String name : ycf.getKeys(false)) {

			try {
				if (name.equalsIgnoreCase("Tree Text")) {
					Core.signconfigs.set_tree_text(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Tree Player Text")) {
					Core.signconfigs.set_tree_player_text(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("To Root Text")) {
					Core.signconfigs.set_to_root_text(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Total Tree's")) {
					Core.signconfigs.set_roots_text(ycf.getString(name));
				}

			} catch (Exception ex) {
				ycf.set(name, "INVAILD");
			}
		}
	}

	public static void loadMessages() {
		File adr = Core.messagefile;
		if (!adr.exists()) {
			try {
				adr.createNewFile();
				YamlConfiguration temp = YamlConfiguration.loadConfiguration(adr);
				temp.addDefault("Inform_Guild_on_Respawn", "true");
				temp.addDefault("Inform_Guild_on_PlayerJoin", "true");
				temp.addDefault("Inform_Player_of_Non_Tree_PvP", "true");
				temp.addDefault("Inform_Guild_on_Fire", "true");
				temp.addDefault("Inform_Guild_on_Enemy_Grief", "true");
				temp.addDefault("Inform_Guild_on_Explosion", "true");
				temp.addDefault("Inform_Guild_on_TreeGrow", "true");
				temp.addDefault("Inform_Guild_on_RootDestroy", "true");
				temp.addDefault("Inform_Guild_on_GuildJoin", "true");
				temp.addDefault("Inform_Guild_on_GuildLeave", "true");

				temp.addDefault("Inform_Player_on_RootPlace", "true");
				temp.addDefault("Inform_Player_on_Sapling_must_Set_Next_To_Log", "true");
				temp.addDefault("Inform_Player_on_Log_must_Set_Next_To_Log", "false");

				temp.options().copyDefaults(true);
				temp.save(adr);
			} catch (IOException ex) {

			}
		}
		YamlConfiguration ycf = YamlConfiguration.loadConfiguration(adr);
		for (String name : ycf.getKeys(false)) {

			try {
				if (name.equalsIgnoreCase("Inform_Guild_on_Respawn")) {
					Core.getMessenger().setInform_Guild_on_Respawn(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Inform_Guild_on_PlayerJoin")) {
					Core.getMessenger().setInform_Guild_on_PlayerJoin(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Inform_Player_of_Non_Tree_PvP")) {
					Core.getMessenger().setInform_Player_of_Non_Tree_PvP(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Inform_Guild_on_Fire")) {
					Core.getMessenger().setInform_Guild_on_Fire(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Inform_Guild_on_Enemy_Grief")) {
					Core.getMessenger().setInform_Guild_on_Enemy_Grief(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Inform_Guild_on_Explosion")) {
					Core.getMessenger().setInform_Guild_on_Explosion(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Inform_Guild_on_TreeGrow")) {
					Core.getMessenger().setInform_Guild_on_TreeGrow(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Inform_Guild_on_RootDestroy")) {
					Core.getMessenger().setInform_Guild_on_RootDestroy(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Inform_Guild_on_GuildJoin")) {
					Core.getMessenger().setInform_Guild_on_GuildJoin(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Inform_Guild_on_GuildLeave")) {
					Core.getMessenger().setInform_Guild_on_GuildLeave(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Inform_Player_on_RootPlace")) {
					Core.getMessenger().setInform_Player_on_RootPlace(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Inform_Player_on_Sapling_must_Set_Next_To_Log")) {
					Core.getMessenger().setInform_Player_on_Sapling_must_Set_Next_To_Log(ycf.getString(name));
				}
				if (name.equalsIgnoreCase("Inform_Player_on_Log_must_Set_Next_To_Log")) {
					Core.getMessenger().setInform_Player_on_Log_must_Set_Next_To_Log(ycf.getString(name));
				}

			} catch (Exception ex) {
				ycf.set(name, "INVAILD");
			}
		}
	}

	/**
	 * Läd die Aktuellen erlauben Materialien
	 * 
	 * @return
	 */

	// http://heads.freshcoal.com/heads.php?query=bomb to find Skins
	// https://ostermiller.org/calc/encode.html to decode to get http

	private static String DirtBomb = "http://textures.minecraft.net/texture/72b362e04530b42d3a92a0a844e1d641d9a1c382369212f5f4be54a0e2db3388";
	private static String TimeBomb = "http://textures.minecraft.net/texture/ad3bd0534027b540aea5ad3a315dd9c722a9d7abbc2769b68f77b2d38878815";
	private static String VaporizingBomb = "http://textures.minecraft.net/texture/12ac8269bc13edc012eed14438c4fd6b7350c974a1ee7d3f4bef64d50f63bf9";
	private static String FunkyBomb = "http://textures.minecraft.net/texture/3d6044836f7b658b5648774766b6ea3da5135149b2caac06a737f5fae6fca";
	private static String Trap = "http://textures.minecraft.net/texture/7096f16e3332be9da7213bc39204642f36da7abd9ed5184e41f81641397d14";
	private static String Illusion = "http://textures.minecraft.net/texture/4d5b4d48c4b48a3ebaa9eb10cfb449d8314857ec227601cbe5b53d2c2972a";
	private static String AirOrb = "http://textures.minecraft.net/texture/7619346d77514434718864fb525eb5e6fc6b8c3c314c89c453345d5f9995dd";
	private static String GreenEye = "http://textures.minecraft.net/texture/d0b520b9e7af56944fda787122832b66767112cfc6855ad48b15855367192";

	private static List<String> bims = new ArrayList<String>();

	public static void loadBIMList() {
		addBIMToBIMList(Barrier.type);
		addBIMToBIMList(BlazingGas.type);
		addBIMToBIMList(Cracker.type);
		addBIMToBIMList(Flame.type);
		addBIMToBIMList(Homing.type);
		addBIMToBIMList(Implosion.type);
		addBIMToBIMList(RemoteControl.type);
		addBIMToBIMList(Timer.type);
	}

	public static void addBIMToBIMList(String name) {
		bims.add(name);
	}

	public static boolean isBIMName(String name) {
		return bims.contains(name);
	}

	public static List<String> getBIMList() {
		return bims;
	}

	private static String ZeroURL = "http://textures.minecraft.net/texture/55a224807693978ed834355f9e5145f9c56ef68cf6f2c9e1734a46e246aae1";
	private static String OneURL = "http://textures.minecraft.net/texture/4246323c9fb319326ee2bf3f5b63ec3d99df76a12439bf0b4c3ab32d13fd9";
	private static String TwoURL = "http://textures.minecraft.net/texture/acb419d984d8796373c9646233c7a02664bd2ce3a1d3476dd9b1c5463b14ebe";
	private static String TreeURL = "http://textures.minecraft.net/texture/f8ebab57b7614bb22a117be43e848bcd14daecb50e8f5d0926e4864dff470";
	private static String FourURL = "http://textures.minecraft.net/texture/62bfcfb489da867dce96e3c3c17a3db7c79cae8ac1f9a5a8c8ac95e4ba3";
	private static String FiveURL = "http://textures.minecraft.net/texture/ef4ecf110b0acee4af1da343fb136f1f2c216857dfda6961defdbee7b9528";
	private static String SixURL = "http://textures.minecraft.net/texture/f331a6a6fcd6995b62088d353bfb68d9b89ae258325caf3f2886464f54a7329";
	private static String SevenURL = "http://textures.minecraft.net/texture/d4ba6ac07d422377a855793f36dea2ed240223f52fd1648181612ecd1a0cfd5";
	private static String EightURL = "http://textures.minecraft.net/texture/c61a8a641437be9aea207253dd3f25440d954ea2b5866c552f386b29ac4d049";
	private static String NineURL = "http://textures.minecraft.net/texture/a1928e1bfd86a9b79397c4cb4b65ef99af49b7d5f7957ad62c0c699a622cfbe";
	
	

	public static YamlConfiguration loadHeadList() {
		File adr = Core.bimfile;
		if (!adr.exists()) {
			try {
				adr.createNewFile();
				YamlConfiguration list = YamlConfiguration.loadConfiguration(adr);

				addToNewBIMFile(list, Cracker.type, DirtBomb, "Cracker", "Thats a Crack!");
				addToNewBIMFile(list, Implosion.type, Illusion, "Implosion", "Its like a Black Hole!");
				addToNewBIMFile(list, RemoteControl.type, VaporizingBomb, "Remote Contol", "Do it from Distance!");
				addToNewBIMFile(list, Timer.type, TimeBomb, "Timer", "Count up to 10!");
				addToNewBIMFile(list, Barrier.type, Trap, "Barrier", "Make them stop!");
				addToNewBIMFile(list, BlazingGas.type, AirOrb, "Blazing Gas", "Let it Rip!");
				addToNewBIMFile(list, Homing.type, GreenEye, "Homing", "It will find them!");
				addToNewBIMFile(list, Flame.type, FunkyBomb, "Flame", "Let them burn!");

				addToNewBIMFile(list, Timer.Zero, ZeroURL, "0", "");
				addToNewBIMFile(list, Timer.One, OneURL, Timer.One, "");
				addToNewBIMFile(list, Timer.Two, TwoURL, Timer.Two, "");
				addToNewBIMFile(list, Timer.Three, TreeURL, Timer.Three, "");
				addToNewBIMFile(list, Timer.Four, FourURL, Timer.Four, "");
				addToNewBIMFile(list, Timer.Five, FiveURL, Timer.Five, "");
				addToNewBIMFile(list, Timer.Six, SixURL, Timer.Six, "");
				addToNewBIMFile(list, Timer.Seven, SevenURL, Timer.Seven, "");
				addToNewBIMFile(list, Timer.Eight, EightURL, Timer.Eight, "");
				addToNewBIMFile(list, Timer.Nine, NineURL, Timer.Nine, "");

				list.options().copyDefaults(true);
				list.save(adr);
			} catch (IOException ex) {

			}
		}
		return YamlConfiguration.loadConfiguration(adr);
	}

	private static String displayNameTag = "displayName";
	private static String loreTag = "lore";
	private static String urlTag = "url";

	private static void addToNewBIMFile(YamlConfiguration list, String name, String url, String displayName,
			List<String> lores) {
		list.set(name + "." + urlTag, url);

		list.set(name + "." + displayNameTag, displayName);
		list.set(name + "." + loreTag, lores);
	}

	/**
	 * Adding Skull BIM to uncreated BIM File
	 */
	private static void addToNewBIMFile(YamlConfiguration list, String name, String url, String displayName,
			String lore) {
		List<String> lores = new ArrayList<String>();
		lores.add(lore);
		addToNewBIMFile(list, name, url, displayName, lores);
	}

	/**
	 * Adding Skull BIM to exsisting BIM File
	 */
	public static void addToBIMFile(String name, String url, String displayName, List<String> lores) {
		YamlConfiguration list = loadHeadList();

		addToNewBIMFile(list, name, url, displayName, lores);

		try {
			list.options().copyDefaults(true);
			list.save(Core.bimfile);
		} catch (IOException e) {

		}
	}

	public static String getURL(String name) {
		YamlConfiguration list = loadHeadList();
		String url = list.getString(name + "." + urlTag);
		return url;
	}

	public static ItemStack getNewBIMItem(String name) {
		String id = BIMStore.createID();

		return getBIMItem(name, id);
	}

	public static ItemStack getExsistingBIMItem(String id) {
		String headName = BIMStore.getBIMHeadName(id);
		
		return getBIMItem(headName, id);
	}
	
	private static ItemStack getBIMItem(String name, String id){
		YamlConfiguration list = loadHeadList();
		ItemStack item = new ItemStack(Material.COBBLESTONE);
		
		String url = getURL(name);
		item = Heads.getHead(url, id);
		String display = list.getString(name + "." + displayNameTag);
		List<String> lores = new ArrayList<String>();
		lores.add(id);
		lores.addAll(list.getStringList(name + "." + loreTag));
		item = setItemProperties(item, display, lores);
		return item;
	}

	public static ItemStack setItemProperties(ItemStack item, String display, List<String> lores) {
		item = setDisplayName(item, display);
		item = setLores(item, lores);
		return item;
	}

	public static ItemStack setDisplayName(ItemStack item, String displayName) {
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(displayName);
		item.setItemMeta(im);
		return item;
	}

	public static ItemStack setLores(ItemStack item, List<String> lores) {
		ItemMeta im = item.getItemMeta();
		im.setLore(lores);
		item.setItemMeta(im);
		return item;
	}

	public static List<String> getLores(ItemStack item) {
		if (item == null)
			return null;
		ItemMeta im = item.getItemMeta();
		if (im == null)
			return null;
		return im.getLore();
	}

}
