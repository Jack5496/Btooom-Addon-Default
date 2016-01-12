package com.jack.bad.beams;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.jack.bad.Core;
import com.jack.btooom.API.Btooom;
import com.jack.btooom.beams.BIM;

public class RemoteControl extends BIM {

public final static String type = "RemoteControl";
	
	public final static String displayName = "RemoteControl";
	public final static String skinURL = "http://textures.minecraft.net/texture/12ac8269bc13edc012eed14438c4fd6b7350c974a1ee7d3f4bef64d50f63bf9";
	public final static String lore = "Finish it from a Far";
	private final static int explosionRadius = 5;
	
	
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
		// lass nichts werfen
		event.setCancelled(true);
		event.setCancelled(true);

		Player p = event.getPlayer();

		// entferne ein item
		ItemStack item = p.getItemInHand();
		if (item.getAmount() > 1) {
			item.setAmount(item.getAmount() - 1);
			p.setItemInHand(item);
		} else {
			p.setItemInHand(new ItemStack(Material.AIR));
		}

		// mache eigenen Wurf
		Snowball snowball = p.getWorld().spawn(p.getEyeLocation(), Snowball.class);
		snowball.setCustomName("Cracker");
		// snowball.setCustomNameVisible(true);
		snowball.setVelocity(p.getLocation().getDirection().multiply(1.5));
		snowball.setShooter(p);
	}

	@Override
	public void rightClickBlock(PlayerInteractEvent event) {
		rightClickAir(event);
	}

	@Override
	public void rightClickBIM(PlayerInteractEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void leftClickBIM(PlayerInteractEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void leftClickAir(PlayerInteractEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAfterSecond(String id) {

	}

	@Override
	public void detonateBIM(Location loc) {

	}

	@Override
	public void onHitGround(String id) {

	}

	@Override
	public void dropBIM(ItemSpawnEvent event) {

	}
}
