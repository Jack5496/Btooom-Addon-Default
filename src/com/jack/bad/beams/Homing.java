package com.jack.bad.beams;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.jack.btooom.API.Btooom;
import com.jack.btooom.beams.BIM;

public class Homing extends BIM {

public final static String type = "Homing";
	
	public final static String displayName = "Homing";
	public final static String skinURL = "http://textures.minecraft.net/texture/d0b520b9e7af56944fda787122832b66767112cfc6855ad48b15855367192";
	public final static String lore = "Let it do the job";
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
		event.setCancelled(true);
	}

	@Override
	public void rightClickBlock(PlayerInteractEvent event) {
		rightClickAir(event);
	}

	@Override
	public void rightClickBIM(PlayerInteractEvent event) {

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
