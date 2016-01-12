package com.jack.bad.beams;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.jack.bad.Core;
import com.jack.bad.explosions.Explosions;
import com.jack.bad.util.ItemHelper;
import com.jack.btooom.API.Btooom;
import com.jack.btooom.beams.BIM;

public class BlazingGas extends BIM {
	
	public final static String type = "BlazingGas";

	public final static String displayName = "BlazingGas";
	public final static String skinURL = "http://textures.minecraft.net/texture/7619346d77514434718864fb525eb5e6fc6b8c3c314c89c453345d5f9995dd";
	public final static String lore = "Im a default Lore";
	private final static int explosionRadius = 7;
	
	@Override
	public String getType(){
		return type;
	}
	
	@Override
	public String getDisplayName(){
		return displayName;
	}
	
	@Override
	public String getSkinURL(){
		return skinURL;
	}
	
	@Override
	public String getLore(){
		return lore;
	}
	
	@Override
	public int getRadius(){
		return explosionRadius;
	}

	@Override
	public void loadBIM(Btooom btooom){
		
		btooom.addonRegisterNewBIMType(type);
		
		List<String> lores = new ArrayList<String>();
		lores.add(lore);
		btooom.addonRegisterNewBIMHead(type, displayName, skinURL, lores);
	}
	
	@Override
	public void unloadBIM(Btooom btooom){
		
		btooom.addonUnRegisterNewBIMType(type);
		
		List<String> lores = new ArrayList<String>();
		lores.add(lore);
		btooom.addonUnRegisterNewBIMHead(type);
	}

	@Override
	public void rightClickAir(PlayerInteractEvent event) {
		event.setCancelled(true);

		Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();

		boolean isOwnBomb = Core.getBIMStore().isBIMOwner(p, item);
		if (isOwnBomb) {
			boolean inactiv = Core.getBIMStore().isBIMInactiv(item);
			boolean activ = Core.getBIMStore().isBIMActiv(item);

			if (inactiv) {
				Core.getBIMStore().activateBIM(item);
				p.sendMessage("Bomb activ!");
			}
			if (activ) {
				Core.getBIMStore().BIMThrow(item, p);
			}
		} else {
			p.sendMessage("Not your Bomb!");
		}
	}

	@Override
	public void rightClickBlock(PlayerInteractEvent event) {
		Core.getBIMStore().BIMplace(event);
	}

	@Override
	public void rightClickBIM(PlayerInteractEvent event) {
		event.setCancelled(true);

		Block clicked = event.getClickedBlock();
		Player p = event.getPlayer();

		boolean isOwnBomb = Core.getBIMStore().isBIMOwner(p, clicked);
		if (isOwnBomb) {
			boolean inactiv = Core.getBIMStore().isBIMInactiv(clicked);

			if (inactiv) {
				Core.getBIMStore().activateBIM(clicked);
				p.sendMessage("Bomb activ!");
			}
		} else {
			p.sendMessage("Not your Bomb!");
		}
	}

	@Override
	public void leftClickBIM(PlayerInteractEvent event) {
		event.setCancelled(true);

		Block clicked = event.getClickedBlock();
		Player p = event.getPlayer();

		boolean isOwnBomb = Core.getBIMStore().isBIMOwner(p, clicked);
		if (isOwnBomb) {
			boolean inactiv = Core.getBIMStore().isBIMInactiv(clicked);
			// boolean activ = BIMStore.isBIMActiv(clicked);

			if (inactiv) {
				if (isResetedBomb(clicked)) {
					Core.getBIMStore().BIMbreak(event);
					return;
				}
			}
			// if (activ) {
			// BIMStore.deactivateBIM(clicked);
			// p.sendMessage("Bomb deactivated!");
			// }
		} else {
			p.sendMessage("Not your Bomb!");
		}
	}

	@Override
	public void leftClickAir(PlayerInteractEvent event) {
		event.setCancelled(true);
	}

	public boolean isResetedBomb(Block b) {
		return Core.getBIMStore().getBIMHeadName(b).equals(getInitHeadName());
	}

	@Override
	public String getInitHeadName() {
		return type;
	}

	@Override
	public boolean timeIsZero(String time) {
		return time.equals(Timer.Zero);
	}

	@Override
	public void countBombDown(String id) {
		String time = Core.getBIMStore().getBIMHeadName(id);

		if (timeIsZero(time)) {
			Location loc = Core.getBIMStore().getBIMLocation(id);
			Core.getBIMStore().destroyBIM(id);
			detonateBIM(loc);
		} else {
			String headName = Timer.getNumberDown(time, Timer.Three);
			Core.getBIMStore().setBIMHeadName(id, headName);
			Core.getBIMStore().updateApearence(id);
		}
	}

	@Override
	public void updateAfterSecond(String id) {
		countBombDown(id);
	}

	@Override
	public void detonateBIM(Location loc) {
		Explosions.gas(loc, explosionRadius);
	}

	@Override
	public void onHitGround(String id) {

	}

	@Override
	public void dropBIM(ItemSpawnEvent event) {
		String id = Core.getBIMStore().getBIMIdentity(event.getEntity().getItemStack());
		if (Core.getBIMStore().isBIMActiv(id)) {
			Player p = Core.getBIMStore().getBIMStorePlayerHoldPlayer(id);
			p.sendMessage("Deactivate BIM first");
			ItemHelper.pickUpItem(p, Core.btooom.getExsistingBIMItem(id));
			event.setCancelled(true);
		} else {
			Core.getBIMStore().BIMDropLikeDrop(event);
		}
	}
}
