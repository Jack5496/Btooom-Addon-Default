package com.jack.bad.beams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import com.jack.bad.Core;
import com.jack.bad.explosions.ExplosionUtil;
import com.jack.bad.explosions.Explosions;
import com.jack.btooom.API.Btooom;
import com.jack.btooom.beams.BIM;

public class Lava extends BIM {

	public final static String type = "Lava";

	public final static String displayName = "Lava";
	public final static String skinURL = "http://textures.minecraft.net/texture/72b362e04530b42d3a92a0a844e1d641d9a1c382369212f5f4be54a0e2db3388";
	public final static String lore = "Im a default Lore";
	private final static int explosionRadius = 5;

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String getSkinURL() {
		return skinURL;
	}

	@Override
	public String getLore() {
		return lore;
	}

	@Override
	public int getRadius() {
		return explosionRadius;
	}

	@Override
	public void loadBIM(Btooom btooom) {

		btooom.addonRegisterNewBIMType(type);

		List<String> lores = new ArrayList<String>();
		lores.add(lore);
		btooom.addonRegisterNewBIMHead(type, displayName, skinURL, lores);
	}

	@Override
	public void unloadBIM(Btooom btooom) {

		btooom.addonUnRegisterNewBIMType(type);

		List<String> lores = new ArrayList<String>();
		lores.add(lore);
		btooom.addonUnRegisterNewBIMHead(type);
	}

	@Override
	public void rightClickAir(PlayerInteractEvent event) {
		// lass nichts werfen
		event.setCancelled(true);

		Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();

		boolean isOwnBomb = Core.getBIMStore().isBIMOwner(p, item);
		if (isOwnBomb) {
			Core.getBIMStore().BIMThrow(item, p);
		}
	}

	@Override
	public void rightClickBlock(PlayerInteractEvent event) {
		rightClickAir(event);
	}

	@Override
	public void rightClickBIM(PlayerInteractEvent event) {
		rightClickAir(event);
	}

	@Override
	public void leftClickBIM(PlayerInteractEvent event) {

	}

	@Override
	public void leftClickAir(PlayerInteractEvent event) {

	}

	@Override
	public void updateAfterSecond(String id) {

	}

	@Override
	public void detonateBIM(Location loc) {
		List<Location> locs = ExplosionUtil.getSphereFromCenter(loc, explosionRadius);

		for (Location l : locs) {
			Material m = l.getBlock().getType();
			switch (m) {
			case ICE:
			case WATER:
				Explosions.turnIntoMaterial(l, Material.LAVA, 1);
				break;
			case GRASS:
			case STONE:
			case SAND:
			case DIRT:
				Explosions.turnIntoMaterial(l, Material.NETHERRACK, 1);
				Explosions.turnIntoMaterial(l, Material.SOUL_SAND, 0.3);
				break;
			}
		}
		for (Location l : locs) {
			Material m = l.getBlock().getType();
			if(m==Material.OBSIDIAN) l.getBlock().setType(Material.LAVA);
		}
	}

	@Override
	public void onHitGround(String id) {
		Location loc = Core.getBIMStore().getBIMLocation(id);
		Core.getBIMStore().destroyBIM(id);
		detonateBIM(loc);
	}

	@Override
	public void dropBIM(ItemSpawnEvent event) {
		Core.getBIMStore().BIMDropLikeDrop(event);
	}

}
