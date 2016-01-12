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

public class Timer extends BIM {
	
	public final static String type = "Timer";

	public final static String displayName = "Timer";
	public final static String skinURL = "http://textures.minecraft.net/texture/ad3bd0534027b540aea5ad3a315dd9c722a9d7abbc2769b68f77b2d38878815";
	public final static String lore = "Count up to 10";
	private final static int explosionRadius = 5;
	
	private static String ZeroURL = "http://textures.minecraft.net/texture/55a224807693978ed834355f9e5145f9c56ef68cf6f2c9e1734a46e246aae1";
	private static String OneURL = "http://textures.minecraft.net/texture/4246323c9fb319326ee2bf3f5b63ec3d99df76a12439bf0b4c3ab32d13fd9";
	private static String TwoURL = "http://textures.minecraft.net/texture/acb419d984d8796373c9646233c7a02664bd2ce3a1d3476dd9b1c5463b14ebe";
	private static String ThreeURL = "http://textures.minecraft.net/texture/f8ebab57b7614bb22a117be43e848bcd14daecb50e8f5d0926e4864dff470";
	private static String FourURL = "http://textures.minecraft.net/texture/62bfcfb489da867dce96e3c3c17a3db7c79cae8ac1f9a5a8c8ac95e4ba3";
	private static String FiveURL = "http://textures.minecraft.net/texture/ef4ecf110b0acee4af1da343fb136f1f2c216857dfda6961defdbee7b9528";
	private static String SixURL = "http://textures.minecraft.net/texture/f331a6a6fcd6995b62088d353bfb68d9b89ae258325caf3f2886464f54a7329";
	private static String SevenURL = "http://textures.minecraft.net/texture/d4ba6ac07d422377a855793f36dea2ed240223f52fd1648181612ecd1a0cfd5";
	private static String EightURL = "http://textures.minecraft.net/texture/c61a8a641437be9aea207253dd3f25440d954ea2b5866c552f386b29ac4d049";
	private static String NineURL = "http://textures.minecraft.net/texture/a1928e1bfd86a9b79397c4cb4b65ef99af49b7d5f7957ad62c0c699a622cfbe";

	public final static String Zero = "0";
	public final static String One = "1";
	public final static String Two = "2";
	public final static String Three = "3";
	public final static String Four = "4";
	public final static String Five = "5";
	public final static String Six = "6";
	public final static String Seven = "7";
	public final static String Eight = "8";
	public final static String Nine = "9";
	
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
	public void loadBIM(Btooom btooom) {
		
		btooom.addonRegisterNewBIMType(type);
		
		List<String> lores = new ArrayList<String>();
		lores.add(lore);
		btooom.addonRegisterNewBIMHead(type, displayName, skinURL, lores);
		
		lores = new ArrayList<String>();
		
		btooom.addonRegisterNewBIMHead(Zero, Zero, ZeroURL, lores);
		btooom.addonRegisterNewBIMHead(One, One, OneURL, lores);
		btooom.addonRegisterNewBIMHead(Two, Two, TwoURL, lores);
		btooom.addonRegisterNewBIMHead(Three, Three, ThreeURL, lores);
		btooom.addonRegisterNewBIMHead(Four, Four, FourURL, lores);
		btooom.addonRegisterNewBIMHead(Five, Five, FiveURL, lores);
		btooom.addonRegisterNewBIMHead(Six, Six, SixURL, lores);
		btooom.addonRegisterNewBIMHead(Seven, Seven, SevenURL, lores);
		btooom.addonRegisterNewBIMHead(Eight, Eight, EightURL, lores);
		btooom.addonRegisterNewBIMHead(Nine, Nine, NineURL, lores);
	}
	
	@Override
	public void unloadBIM(Btooom btooom) {
		
		btooom.addonUnRegisterNewBIMType(type);
		
		List<String> lores = new ArrayList<String>();
		lores.add(lore);
		btooom.addonUnRegisterNewBIMHead(type);
		
		lores = new ArrayList<String>();
		
		btooom.addonUnRegisterNewBIMHead(Zero);
		btooom.addonUnRegisterNewBIMHead(One);
		btooom.addonUnRegisterNewBIMHead(Two);
		btooom.addonUnRegisterNewBIMHead(Three);
		btooom.addonUnRegisterNewBIMHead(Four);
		btooom.addonUnRegisterNewBIMHead(Five);
		btooom.addonUnRegisterNewBIMHead(Six);
		btooom.addonUnRegisterNewBIMHead(Seven);
		btooom.addonUnRegisterNewBIMHead(Eight);
		btooom.addonUnRegisterNewBIMHead(Nine);
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
			boolean activ = Core.getBIMStore().isBIMActiv(clicked);

			if (inactiv) {
				Core.getBIMStore().activateBIM(clicked);
				p.sendMessage("Bomb activ!");
			}
			// if (activ) {
			// countBombDown(clicked);
			// }
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
			boolean activ = Core.getBIMStore().isBIMActiv(clicked);

			if (inactiv) {
				if (isResetedBomb(clicked)) {
					Core.getBIMStore().BIMbreak(event);
					return;
				}
				resetBomb(clicked);
				p.sendMessage("Bomb: reseted");
			}
			if (activ) {
				Core.getBIMStore().deactivateBIM(clicked);
				p.sendMessage("Bomb deactivated!");
			}
		} else {
			p.sendMessage("Not your Bomb!");
		}
	}

	@Override
	public void leftClickAir(PlayerInteractEvent event) {
		event.setCancelled(true);

		Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();

		boolean isOwnBomb = Core.getBIMStore().isBIMOwner(p, item);
		if (isOwnBomb) {
			boolean inactiv = Core.getBIMStore().isBIMInactiv(item);
			boolean activ = Core.getBIMStore().isBIMActiv(item);

			if (inactiv) {

				resetBomb(item);
				p.sendMessage("Bomb reseted");
			}
			if (activ) {
				Core.getBIMStore().deactivateBIM(item);
				p.sendMessage("Bomb deactivated!");

			}
		} else {
			p.sendMessage("Not your Bomb!");
		}
	}

	@Override
	public boolean isResetedBomb(Block b) {
		return Core.getBIMStore().getBIMHeadName(b).equals(getInitHeadName());
	}
	
	@Override
	public String getInitHeadName() {
		return type;
	}

	@Override
	public void resetBomb(Block b) {
		Core.getBIMStore().setBIMHeadName(b, getInitHeadName());
		Core.getBIMStore().updateApearence(b);
	}

	@Override
	public void resetBomb(ItemStack item) {
		Core.getBIMStore().setBIMHeadName(item, getInitHeadName());
		Core.getBIMStore().updateApearence(item);
	}

	@Override
	public boolean timeIsZero(String time) {
		return time.equals(Zero);
	}

	@Override
	public void countBombDown(String id) {
		String time = Core.getBIMStore().getBIMHeadName(id);

		if (timeIsZero(time)) {
			Location loc = Core.getBIMStore().getBIMLocation(id);
			Core.getBIMStore().destroyBIM(id);
			detonateBIM(loc);
		} else {
			String headName = getNumberDown(time, Nine);
			Core.getBIMStore().setBIMHeadName(id, headName);
			Core.getBIMStore().updateApearence(id);
		}
	}

	@Override
	public void detonateBIM(Location loc) {
		// Explosions.normalExplostion(toDetonate, explosionRadius);
		Explosions.normalExplosion(loc, explosionRadius);
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

	public static String getNumberDown(String time, String defaultTime) {
		switch (time) {
		case Zero:
			return Zero;
		case One:
			return Zero;
		case Two:
			return One;
		case Three:
			return Two;
		case Four:
			return Three;
		case Five:
			return Four;
		case Six:
			return Five;
		case Seven:
			return Six;
		case Eight:
			return Seven;
		case Nine:
			return Eight;
		}
		return defaultTime;
	}

	@Override
	public void updateAfterSecond(String id) {
		countBombDown(id);
	}

}
