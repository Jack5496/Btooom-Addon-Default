package com.jack.btooom.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.jack.btooom.Core;
import com.jack.btooom.beams.BIMs;
import com.jack.btooom.heads.Heads;
import com.jack.btooom.threads.BIMTimeController;
import com.jack.btooom.util.ItemHelper;
import com.jack.btooom.util.SerializeLocation;
import com.jack.btooom.util.UtilRandom;
import com.jack.btooom.util.BIMThrowAndHit;

public class BIMStore {

	public static void loadAllConfigs() {
		loadHeadList();
		removeUnusedBIMs();
	}

	public static YamlConfiguration loadHeadList() {
		File adr = Core.bimfile;
		if (!adr.exists()) {
			try {
				adr.createNewFile();
				YamlConfiguration list = YamlConfiguration.loadConfiguration(adr);

				list.options().copyDefaults(true);
				list.save(adr);
			} catch (IOException ex) {

			}
		}
		return YamlConfiguration.loadConfiguration(adr);
	}

	private static int maxLiveHoursOfBIM = 24;
	private static String lastUse = "lastUse";
	private static boolean updateLastTimeUse = false;

	public static void updateLastUseTime(String id) {
		YamlConfiguration list = loadHeadList();

		list.set(id + "." + lastUse, new Date());
		saveFile(list);
	}

	public static Date getLastUseTime(String id) {
		YamlConfiguration list = loadHeadList();
		return (Date) list.get(id + "." + lastUse);
	}

	public static void removeUnusedBIMs() {
		YamlConfiguration list = loadHeadList();
		Date now = new Date();
		List<String> toDelete = new ArrayList<String>();
		for (String key : list.getKeys(false)) {
			String id = list.getString(key);
			Date item = getLastUseTime(id);
			if (item != null) {
				Calendar help = Calendar.getInstance();
				help.setTime(item);
				help.add(Calendar.HOUR_OF_DAY, maxLiveHoursOfBIM);
				item = help.getTime();
				if (now.after(item)) {
					toDelete.add(id);
				}
			}
		}
		for (String id : toDelete) {
			list.set(id, null);
		}
	}

	// activ + split + type + split + headName + split + owner;
	private static String activTyp = "activ";
	private static String typTyp = "typ";
	private static String headNameTyp = "headName";
	private static String ownerTyp = "owner";

	private static String storeTyp = "storedAs";
	private final static String storedAsBlock = "block";
	private final static String storedAsPlayerHold = "player";

	private final static String storedAsChestHold = "chest";

	private final static String storedAsEntity = "entity";

	/**
	 * Adding Skull BIM to exsisting BIM File
	 */
	public static void addToBIMFile(String id, String activ, String typ, String headName, String owner) {
		setBIMActiv(id, activ);
		setBIMTyp(id, typ);
		setBIMHeadName(id, headName);
		setBIMOwner(id, owner);
	}

	private static int length = 16;

	public static String createID() {
		return UtilRandom.getRandomString(length);
	}

	/**
	 * ******************PLACE AND BREAK BLOCK******************
	 */

	public static void BIMplace(PlayerInteractEvent event) {
		event.setCancelled(true);

		Player p = event.getPlayer();
		ItemStack bim = p.getItemInHand();
		Block clicked = event.getClickedBlock();
		BlockFace face = event.getBlockFace();
		Vector v = p.getLocation().getDirection();

		Block b = clicked.getRelative(face);

		BIMStore.BIMplace(b, bim, face, v, p);
	}

	public static void BIMplace(Block b, ItemStack item, BlockFace face, Vector v, Player p) {
		// Place Block from Item
		Heads.setSkullBlock(b, item);
		Heads.setSkullRotationAutomatic(b, face, v);
		ItemHelper.getOneItemLess(p, item);

		// Change data in BIMFile
		String id = getBIMIdentity(item);
		resetStoreTypAndStoredAsValues(id);
		setBIMStoreTyp(id, b);
	}

	public static void BIMbreak(PlayerInteractEvent event) {
		event.setCancelled(true);
		Player p = event.getPlayer();
		Block b = event.getClickedBlock();
		BIMbreak(b, p);
	}

	public static void BIMbreak(Block b, Player p) {
		String id = Heads.getSkullID(b);
		ItemStack item = ConfigLoader.getExsistingBIMItem(id);
		b.setType(Material.AIR);

		BIMGivePlayer(item, id, p);
	}

	public static void BIMGivePlayer(ItemStack item, String id, Player p) {
		if (ItemHelper.pickUpItem(p, item)) {
			resetStoreTypAndStoredAsValues(id);
			setBIMStoreTyp(id, p);
		}
	}

	public static void BIMChangeInventory(Inventory from, Inventory to, Player p, ItemStack bim, Location loc) {
		Bukkit.broadcastMessage("Change BIM from: " + from.getType() + " to: " + to.getType());

		if (ItemHelper.hasFreeSpaceInInventory(to)) {
			String id = getBIMIdentity(bim);

			boolean toContainer = !p.getInventory().equals(to);
			Bukkit.broadcastMessage("Into Container?: " + toContainer);

			resetStoreTypAndStoredAsValues(id);

			ItemHelper.getOneItemLess(from, bim);
			ItemHelper.pickUpItem(to, ConfigLoader.getExsistingBIMItem(id));

			if (toContainer) {
				setBIMStoreTyp(id, loc);
			} else {

				setBIMStoreTyp(id, p);
			}
		} else {
			return;
		}

	}

	public static boolean BIMThrow(ItemStack item, Player p) {
		String id = getBIMIdentity(item);
		ItemHelper.getOneItemLess(p, item);
		if (p.getItemInHand().getType() == Material.AIR) {
			Entity entity = BIMThrowAndHit.throwNormal(id, p);
			entity.setCustomName(id);

			resetStoreTypAndStoredAsValues(id);
			setBIMStoreTyp(id, entity);
			return true;
		}
		else{
			p.setItemInHand(ConfigLoader.getExsistingBIMItem(id));
			return false;
		}
	}

	public static void BIMDropLikeThrow(ItemSpawnEvent event) {
		event.setCancelled(true);
		ItemStack item = event.getEntity().getItemStack();
		String id = getBIMIdentity(item);

		Player thrower = null;
		try {
			thrower = (Player) event.getEntity().getNearbyEntities(0, 0, 0).get(0);
		} catch (IndexOutOfBoundsException e) {

		}

		Entity entity = BIMThrowAndHit.throwLikeDrop(id, thrower);

		resetStoreTypAndStoredAsValues(id);
		setBIMStoreTyp(id, entity);
	}

	public static void BIMDropLikeDrop(ItemSpawnEvent event) {
		event.setCancelled(false);

		BIMDropLikeDrop(event.getEntity());
	}

	public static void BIMDropLikeDrop(Item entity) {
		ItemStack item = entity.getItemStack();

		String id = getBIMIdentity(item);
		entity.setCustomName(id);

		resetStoreTypAndStoredAsValues(id);
		setBIMStoreTyp(id, entity);
	}

	public static void BIMPickup(PlayerPickupItemEvent event) {
		event.setCancelled(true);
		BIMPickup(event.getItem(), event.getPlayer());
	}

	private static void BIMPickup(Entity pickup, Player p) {
		String id = getBIMIdentity(pickup);
		ItemStack item = ConfigLoader.getExsistingBIMItem(id);

		if (ItemHelper.pickUpItem(p, item)) {
			pickup.remove();
			resetStoreTypAndStoredAsValues(id);
			setBIMStoreTyp(id, p);
		}
	}

	public static void givePlayerNewBIM(Player p, String typ) {
		ItemStack bim = ConfigLoader.getNewBIMItem(typ);

		registerBIM(bim, p, typ);
		p.getInventory().addItem(bim);
	}

	private static void registerBIM(ItemStack item, Player p, String typ) {
		String id = getBIMIdentity(item);
		addToBIMFile(id, BIMs.inactiv, typ, typ, p.getUniqueId().toString());

		if (updateLastTimeUse) {
			updateLastUseTime(id);
		}

		setBIMStoreTyp(id, p);
	}

	public static void destroyBIM(String id) {
		String storeTyp = getBIMStoreTyp(id);

		switch (storeTyp) {
		case storedAsBlock:
			getBIMStoredAsBlockBlock(id).setType(Material.AIR);
			break;
		case storedAsPlayerHold:
			Player p = getBIMStorePlayerHoldPlayer(id);
			removeBIMFromInventory(p.getInventory(), id);
			break;
		case storedAsChestHold:
			Inventory inv = getBIMStoredAsChestHold(id);
			removeBIMFromInventory(inv, id);
			break;
		case storedAsEntity:
			Entity entity = getBIMStoredAsEntityEntity(id);
			entity.remove();
			break;
		}

		// unregisterBIM in all Case
		unregisterBIM(id);
	}

	public static void removeBIMFromInventory(Inventory inv, String id) {
		int place = ItemHelper.getPlaceNumberOfItemStackInInventory(inv, id);
		if (place != -1) {
			Bukkit.broadcastMessage("BIMStore: set AIR");
			inv.setItem(place, new ItemStack(Material.AIR));
		}
	}

	public static Location getBIMLocation(String id) {
		String storeTyp = getBIMStoreTyp(id);

		switch (storeTyp) {
		case storedAsBlock:
			return getBIMStoredAsBlockBlock(id).getLocation();
		case storedAsPlayerHold:
			return getBIMStorePlayerHoldPlayer(id).getLocation();
		case storedAsChestHold:
			return getBIMStoredAsChestHoldLocation(id);
		case storedAsEntity:
			Entity entity = getBIMStoredAsEntityEntity(id);
			if (entity == null)
				return null;
			return entity.getLocation();
		}
		return null;
	}

	public static void unregisterBIM(String id) {
		BIMTimeController.removeActivBIMID(id);

		YamlConfiguration list = loadHeadList();

		list.set(id, null);
		saveFile(list);
	}

	/**
	 * *****************GETTER AND SETTER************************************
	 */

	private static void saveFile(YamlConfiguration list) {
		try {
			list.options().copyDefaults(true);
			list.save(Core.bimfile);
		} catch (IOException e) {

		}
	}

	private static void resetStoreTypAndStoredAsValues(String id) {
		YamlConfiguration list = loadHeadList();

		list.set(id + "." + storeTyp, null);

		list.set(id + "." + storedAsBlock, null);
		list.set(id + "." + storedAsPlayerHold, null);
		list.set(id + "." + storedAsChestHold, null);
		list.set(id + "." + storedAsEntity, null);

		if (updateLastTimeUse) {
			list.set(id + "." + lastUse, new Date());
		}

		saveFile(list);
	}

	/**
	 * ************Storetyp**********
	 */

	public static void setBIMStoreTyp(String id, String storedAs) {
		YamlConfiguration list = loadHeadList();

		list.set(id + "." + storeTyp, storedAs);
		saveFile(list);
	}

	public static String getBIMStoreTyp(String id) {
		YamlConfiguration list = loadHeadList();
		return (String) list.get(id + "." + storeTyp);
	}

	public static void setBIMStoreTyp(String id, Player p) {
		setBIMStoreTyp(id, storedAsPlayerHold);

		YamlConfiguration list = loadHeadList();
		list.set(id + "." + storedAsPlayerHold, p.getUniqueId().toString());
		saveFile(list);
	}

	public static void setBIMStoreTyp(String id, Block b) {
		setBIMStoreTyp(id, storedAsBlock);

		YamlConfiguration list = loadHeadList();
		list.set(id + "." + storedAsBlock, SerializeLocation.toString(b.getLocation()));
		saveFile(list);
	}

	public static void setBIMStoreTyp(String id, Entity item) {
		setBIMStoreTyp(id, storedAsEntity);

		YamlConfiguration list = loadHeadList();
		list.set(id + "." + storedAsEntity, item.getEntityId());
		saveFile(list);
	}

	public static void setBIMStoreTyp(String id, Location loc) {
		setBIMStoreTyp(id, storedAsChestHold);

		YamlConfiguration list = loadHeadList();
		list.set(id + "." + storedAsChestHold, SerializeLocation.toString(loc));
		saveFile(list);
	}

	/*
	 * *************BlockStoreTyp**********
	 */

	public static String getBIMStoreBlockLocationAsString(String id) {
		YamlConfiguration list = loadHeadList();
		return list.getString(id + "." + storedAsBlock);
	}

	public static Block getBIMStoredAsBlockBlock(String id) {
		String locAsString = getBIMStoreBlockLocationAsString(id);
		Location loc = SerializeLocation.fromString(locAsString);
		return loc.getBlock();
	}

	/*
	 * ************PlayerStoreTyp**********
	 */

	private static String getBIMStorePlayerHoldString(String id) {
		YamlConfiguration list = loadHeadList();
		return list.getString(id + "." + storedAsPlayerHold);
	}

	public static Player getBIMStorePlayerHoldPlayer(String id) {
		return Bukkit.getPlayer(UUID.fromString(getBIMStorePlayerHoldString(id)));
	}

	/*
	 * ***********EntityStoreTyp***********
	 */

	private static String getBIMStoredAsItemString(String id) {
		YamlConfiguration list = loadHeadList();
		return list.getString(id + "." + storedAsEntity);
	}

	public static Entity getBIMStoredAsEntityEntity(String id) {
		String sid = getBIMStoredAsItemString(id);
		Player p = getBIMOwner(id);
		int eid = Integer.parseInt(sid);
		List<Entity> ents = p.getWorld().getEntities();
		for (Entity ent : ents) {
			if (ent.getEntityId() == eid) {
				return ent;
			}
		}
		return null;
	}

	/*
	 * **********InventoryHolder************
	 */

	public static String getBIMStoreChestHoldLocationAsString(String id) {
		YamlConfiguration list = loadHeadList();
		return list.getString(id + "." + storedAsChestHold);
	}

	public static Location getBIMStoredAsChestHoldLocation(String id) {
		String locAsString = getBIMStoreChestHoldLocationAsString(id);
		return SerializeLocation.fromString(locAsString);
	}

	public static Inventory getBIMStoredAsChestHold(String id) {
		Location loc = getBIMStoredAsChestHoldLocation(id);
		BlockState state = loc.getBlock().getState();

		if (state instanceof InventoryHolder) {
			return ((InventoryHolder) state).getInventory();
		} else {
			return null;
		}

	}

	/**
	 * ***********OWNER***********
	 */

	public static void setBIMOwner(String id, String owner) {
		YamlConfiguration list = loadHeadList();

		list.set(id + "." + ownerTyp, owner);
		saveFile(list);
	}

	private static String getBIMOwnerUUID(String id) {
		YamlConfiguration list = loadHeadList();
		return (String) list.get(id + "." + ownerTyp);
	}

	public static Player getBIMOwner(String id) {
		UUID uuid = UUID.fromString(getBIMOwnerUUID(id));
		return Bukkit.getPlayer(uuid);
	}

	public static void setBIMOwner(ItemStack item, String owner) {
		setBIMOwner(getBIMIdentity(item), owner);
	}

	public static String getBIMOwner(ItemStack item) {
		return getBIMOwnerUUID(getBIMIdentity(item));
	}

	public static void setBIMOwner(Block b, String owner) {
		setBIMOwner(getBIMIdentity(b), owner);
	}

	public static String getBIMOwner(Block b) {
		return getBIMOwnerUUID(getBIMIdentity(b));
	}

	/*
	 * ***************isOWner***********
	 */

	public static boolean isBIMOwner(Player p, String id) {
		return getBIMOwnerUUID(id).equals(p.getUniqueId().toString());
	}

	public static boolean isBIMOwner(Player p, ItemStack item) {
		return getBIMOwnerUUID(getBIMIdentity(item)).equals(p.getUniqueId().toString());
	}

	public static boolean isBIMOwner(Player p, Block b) {
		return getBIMOwnerUUID(getBIMIdentity(b)).equals(p.getUniqueId().toString());
	}

	/**
	 * ****************isBIM****************
	 */

	public static boolean isBIM(String id) {
		if (id == null)
			return false;
		return loadHeadList().contains(id);
	}

	public static boolean isBIM(Block b) {
		return isBIM(getBIMIdentity(b));
	}

	public static boolean isBIM(ItemStack item) {
		return isBIM(getBIMIdentity(item));
	}

	public static boolean isBIM(Entity item) {
		return isBIM(getBIMIdentity(item));
	}

	/**
	 * ***********ACTIV***********
	 */

	private static void setBIMActiv(String id, String activ) {
		YamlConfiguration list = loadHeadList();

		list.set(id + "." + activTyp, activ);
		saveFile(list);

		if (activ.equals(BIMs.activ))
			BIMTimeController.addActivBIMID(id);
		if (activ.equals(BIMs.inactiv))
			BIMTimeController.removeActivBIMID(id);
	}

	public static String getBIMActive(String id) {
		YamlConfiguration list = loadHeadList();
		return (String) list.get(id + "." + activTyp);
	}

	public static void setBIMActive(ItemStack item, String activ) {
		setBIMActiv(getBIMIdentity(item), activ);
	}

	public static String getBIMActive(ItemStack item) {
		return getBIMActive(getBIMIdentity(item));
	}

	public static void setBIMActive(Block b, String activ) {
		setBIMActiv(getBIMIdentity(b), activ);
	}

	public static String getBIMActive(Block b) {
		return getBIMActive(getBIMIdentity(b));
	}

	/*
	 * ************Activate**********
	 */

	public static void activateBIM(String id) {
		setBIMActiv(id, BIMs.activ);
	}

	public static void activateBIM(ItemStack item) {
		activateBIM(getBIMIdentity(item));
	}

	public static void activateBIM(Block b) {
		activateBIM(getBIMIdentity(b));
	}

	/*
	 * ***********Deactivate**********
	 */

	public static void deactivateBIM(String id) {
		setBIMActiv(id, BIMs.inactiv);
	}

	public static void deactivateBIM(ItemStack item) {
		deactivateBIM(getBIMIdentity(item));
	}

	public static void deactivateBIM(Block b) {
		deactivateBIM(getBIMIdentity(b));
	}

	/*
	 * ************isActiv*********
	 */

	public static boolean isBIMActiv(String id) {
		return getBIMActive(id).equals(BIMs.activ);
	}

	public static boolean isBIMActiv(ItemStack item) {
		return isBIMActiv(getBIMIdentity(item));
	}

	public static boolean isBIMActiv(Block b) {
		return isBIMActiv(getBIMIdentity(b));
	}

	/*
	 * ***********isInactiv*********
	 */

	public static boolean isBIMInactiv(String id) {
		return getBIMActive(id).equals(BIMs.inactiv);
	}

	public static boolean isBIMInactiv(ItemStack item) {
		return isBIMInactiv(getBIMIdentity(item));
	}

	public static boolean isBIMInactiv(Block b) {
		return isBIMInactiv(getBIMIdentity(b));
	}

	/**
	 * ***********TYP***********
	 */

	private static void setBIMTyp(String id, String typ) {
		YamlConfiguration list = loadHeadList();

		list.set(id + "." + typTyp, typ);
		saveFile(list);
	}

	public static String getBIMType(String id) {
		YamlConfiguration list = loadHeadList();
		return (String) list.get(id + "." + typTyp);
	}

	public static void setBIMType(ItemStack item, String typ) {
		setBIMTyp(getBIMIdentity(item), typ);
	}

	public static String getBIMType(ItemStack item) {
		return getBIMType(getBIMIdentity(item));
	}

	public static void setBIMType(Block b, String typ) {
		setBIMTyp(getBIMIdentity(b), typ);
	}

	public static String getBIMType(Block b) {
		return getBIMType(getBIMIdentity(b));
	}

	/**
	 * ****************Identity****************
	 */

	public static String getBIMIdentity(Block b) {
		if (b.getType() != Material.SKULL)
			return null;
		return Heads.getSkullID(b);
	}

	public static String getBIMIdentity(ItemStack item) {
		List<String> lores = ConfigLoader.getLores(item);
		if (lores == null)
			return null;
		return lores.get(0);
	}

	public static String getBIMIdentity(Entity item) {
		return item.getCustomName();
	}

	/**
	 * ****************HeadName****************
	 */

	private static void setBIMHeadNameWithApearence(String id, String headName) {
		setBIMHeadName(id, headName);
		updateApearence(id);
	}

	public static void setBIMHeadName(String id, String headName) {
		YamlConfiguration list = loadHeadList();

		list.set(id + "." + headNameTyp, headName);
		saveFile(list);
	}

	public static String getBIMHeadName(String id) {
		YamlConfiguration list = loadHeadList();
		return (String) list.get(id + "." + headNameTyp);
	}

	public static void setBIMHeadName(ItemStack item, String headName) {
		setBIMHeadNameWithApearence(getBIMIdentity(item), headName);
	}

	public static void setBIMHeadName(Block b, String headName) {
		setBIMHeadNameWithApearence(getBIMIdentity(b), headName);
	}

	public static String getBIMHeadName(ItemStack item) {
		return getBIMHeadName(getBIMIdentity(item));
	}

	public static String getBIMHeadName(Block b) {
		return getBIMHeadName(getBIMIdentity(b));
	}

	/*
	 * **************UpdateItemAndBlock**********
	 */

	public static void updateApearence(String id) {
		String storeTyp = getBIMStoreTyp(id);

		switch (storeTyp) {
		case storedAsBlock:
			updateBlockApearence(id);
			break;
		case storedAsPlayerHold:
			updatePlayerHoldApearence(id);
			break;
		case storedAsChestHold:
			updateChestHoldApearence(id);
			break;
		case storedAsEntity:
			updateBIMEntity(id);
			break;
		}
	}

	public static void updateApearence(Block b) {
		updateApearence(getBIMIdentity(b));
	}

	public static void updateApearence(ItemStack item) {
		updateApearence(getBIMIdentity(item));
	}

	public static void updateChestHoldApearence(String id) {
		Inventory inv = getBIMStoredAsChestHold(id);
		updateInventoryHoldApearence(inv, id);
	}

	private static void updateBlockApearence(String id) {
		String stringLocation = getBIMStoreBlockLocationAsString(id);
		Location loc = SerializeLocation.fromString(stringLocation);
		Block b = loc.getBlock();

		int rotation = Heads.getSkullRotation(b);
		int facing = Heads.getSkullFacing(b);

		Heads.setSkullBlock(b, id);

		Heads.setSkullRotationAndFacing(b, rotation, facing);
	}

	private static void updatePlayerHoldApearence(String id) {
		Player p = getBIMStorePlayerHoldPlayer(id);
		updatePlayerHoldApearence(p, id);
	}

	public static boolean isSameBIM(ItemStack a, ItemStack b) {
		return getBIMIdentity(a).equals(getBIMIdentity(b));
	}

	private static void updatePlayerHoldApearence(Player p, String id) {
		updateInventoryHoldApearence(p.getInventory(), id);
	}

	public static void updateInventoryHoldApearence(Inventory inv, String id) {
		int number = ItemHelper.getPlaceNumberOfItemStackInInventory(inv, id);
		if (number == -1)
			return;

		ItemStack lookAt = inv.getItem(number);
		inv.setItem(number, updateBIMItem(lookAt));

	}

	private static ItemStack updateBIMItem(ItemStack item) {
		String id = getBIMIdentity(item);
		ItemStack back = ConfigLoader.getExsistingBIMItem(id);
		back.setAmount(item.getAmount());
		return back;
	}

	private static void updateBIMEntity(String id) {
		Entity item = getBIMStoredAsEntityEntity(id);
		if (!(item instanceof Snowball)) {
			Location loc = item.getLocation();
			Location clear = new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY(),
					loc.getBlockZ() + 0.5);
			item.remove();
			Entity newItem = loc.getWorld().dropItem(clear, ConfigLoader.getExsistingBIMItem(id));
			newItem.setVelocity(newItem.getVelocity().zero());
			newItem.setFallDistance(0);
			setBIMStoreTyp(id, newItem);
		}
	}

}
