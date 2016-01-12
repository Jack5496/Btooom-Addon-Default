package com.jack.bad.explosions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.jack.bad.Core;
import de.slikey.effectlib.effect.TextEffect;

public class Explosions {

	/**
	 * Idee: Realistische Explosion Blöcke fliegen weg
	 * 
	 * 
	 * double x = 0; double y = 0; double z = 0; Location eLoc; if(e.getEntity()
	 * == null){ eLoc = e.getLocation(); }else{ eLoc =
	 * e.getEntity().getLocation(); } World w = eLoc.getWorld(); for (int i = 0;
	 * i < e.blockList().size();i++){ Block b = e.blockList().get(i); Location
	 * bLoc = b.getLocation(); if (disallowedBlocks.contains(b.getType()))
	 * continue; x = bLoc.getX() - eLoc.getX(); y = bLoc.getY() - eLoc.getY() +
	 * 0.5; z = bLoc.getZ() - eLoc.getZ(); FallingBlock fb =
	 * w.spawnFallingBlock(bLoc, b.getType(), (byte)b.getData());
	 * fb.setDropItem(false); fb.setVelocity(new Vector(x,y,z)); }
	 */

	/*
	 * "Booom !" with
	 */

	public static void normalExplosion(Location loc, int radius) {

		// ExplodeEffect effect = new ExplodeEffect(Core.em);
		// effect.setLocation(loc);
		// effect.iterations = 1;
		// effect.sound = Sound.EXPLODE;
		// effect.start();

		TextEffect text = new TextEffect(Core.em);
		text.setLocation(loc);
		text.color = Color.GREEN;
		text.autoOrient = true;
		text.size = 0.2f * 2 / 3;
		text.iterations = 1;
		text.text = "Booom !";
		text.start();

		for (Location l : ExplosionUtil.getSphereFromCenter(loc, radius)) {
			Core.btooom.destroyBlockWithBIM(l);
		}
	}

	public static void gas(Location loc, int radius) {
		List<Location> makeCob = new ArrayList<Location>();
		makeCob.add(loc);

		for (int i = 0; i < radius; i++) {
			makeCob = ExplosionUtil.get6Neigh(makeCob);
		}

		for (Location l : makeCob) {
			l.getBlock().setType(Material.WEB);
		}
	}

	public static void flame(Location loc, int radius) {
		double chance = .5;
		List<Location> ground = ExplosionUtil.getTopGroundCircle(loc, radius);
		setBlocksOnFire(ground, radius, chance);
	}

	public static void flameCircle(Location loc, int radius, int depth) {
		if (depth == 0 || radius < depth)
			return;

		double chance = .75;
		List<Location> total = ExplosionUtil.getTopGroundCircle(loc, radius);
		List<Location> inner = ExplosionUtil.getTopGroundCircle(loc, radius - depth);
		List<Location> ground = difference(total, inner);

		setBlocksOnFire(ground, radius, chance);
	}

	public static List<Location> difference(List<Location> list1, List<Location> list2) {
		for (Location l : list2) {
			list1.remove(l);
		}
		return list1;
	}

	public static List<Location> schnitt(List<Location> list1, List<Location> list2) {
		return list1.stream().filter(f -> list2.contains(f)).collect(Collectors.toList());
	}

	public static void setBlocksOnFire(List<Location> ground, int radius, double chance) {
		for (Location l : ground) {
			if (rollCahnce(chance)) {
				Location abouve = l.getBlock().getRelative(BlockFace.UP).getLocation();
				abouve.getBlock().setType(Material.FIRE);
			}
		}
	}

	public static void implosion(Location loc, int radius) {

		FireWorkUtil.spawnRocketBigWhiteBall(loc);

		for (Entity ent : ExplosionUtil.getEntityesAround(loc, radius)) {
			ent.teleport(new Location(loc.getWorld(), loc.getBlockX(), -1, loc.getBlockZ()));
		}
		for (Location l : ExplosionUtil.getSphereFromCenter(loc, radius)) {
			Core.btooom.destroyBlockWithBIM(l);
		}
	}

	public static void lightning(Location loc, int radius) {
		double chance = 0.8;

		for (Location l : ExplosionUtil.getTopGroundCircle(loc, radius)) {
			if (rollCahnce(chance)) {
				l.getWorld().strikeLightning(l);
				Material mat = l.getBlock().getType();
				switch (mat) {
				case SAND:
					turnIntoMaterial(l, Material.GLASS, 1);
					break;
				case WATER:
					turnIntoMaterial(l, Material.AIR, 1);
					break;
				}
			}
		}
	}

	public static void ice(Location loc, int radius) {

		int time = 7;

		PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, time * 20, 2);

		for (Entity ent : ExplosionUtil.getEntityesAround(loc, radius)) {
			if (ent instanceof LivingEntity) {
				slowness.apply((LivingEntity) ent);
			}
		}

		for (Location l : ExplosionUtil.getSphereFromCenter(loc, radius)) {
			turnIntoSnowOrIce(l);
		}
	}

	public static void turnIntoSnowOrIce(Location loc) {
		Material mat = loc.getBlock().getType();

		switch (mat) {
		case SKULL:
			break;
		case AIR:
		case LONG_GRASS:
			break;
		case WATER:
			turnIntoMaterial(loc, Material.ICE, 0.8);
			break;
		case STONE:
		case GRAVEL:
		case SAND:
		case DIRT:
			turnIntoMaterial(loc, Material.SNOW_BLOCK, 0.8);
			break;
		default:
			turnIntoMaterial(loc, Material.ICE, 0.8);
			break;
		}
	}

	public static void turnIntoMaterial(Location loc, Material mat, double chance) {
		if (rollCahnce(chance)) {
			loc.getBlock().setType(mat);
		}
	}

	public static boolean rollCahnce(double chance) {
		Random ran = new Random();
		double rc = ran.nextDouble();
		rc -= chance;
		return (rc < 0);
	}

	public static void blackHole(Location loc, int radius) {
		//
		// float totalSeconds = 5;
		// List<Location> allBlocks = ExplosionUtil.getSphereFromCenter(loc,
		// radius);
		// int totalBlocks = allBlocks.size();
		// Bukkit.broadcastMessage("BlackHole: total: " + totalBlocks);
		// float blocksPerSecond = (float) totalBlocks / totalSeconds;
		// long blocksPerDelay = (long) (blocksPerSecond / 60);
		// Bukkit.broadcastMessage("BlackHole: blocksPerDelay: " +
		// blocksPerDelay);
		//
		// final boolean[] stop = new boolean[1];
		// stop[0] = false;
		//
		// final int[] radProg = new int[1];
		// radProg[0] = 1;
		// final Object[] toRemove = new Object[1];
		//
		// final int ID[] = new int[1];
		//
		// List<Location> orderToRemove = new ArrayList<Location>();
		//
		// Random ran = new Random();
		//
		// int amount = 0;
		//
		// List<Location> act = null;
		//
		// for (int i = 0; i < radius+1; i++) {
		// act = ExplosionUtil.getSphereFromCenter(loc, i);
		//
		// for (int r = 0; r < act.size(); r++) {
		// int pos = ran.nextInt(act.size());
		// Location l = act.get(r);
		//
		// if (!orderToRemove.contains(l)) {
		// amount++;
		// orderToRemove.add(l);
		// }
		// }
		// }
		//
		// Bukkit.broadcastMessage("BlackHole: Added: " + amount);
		// Bukkit.broadcastMessage("BlackHole: Added: " + act.size());
		//
		// toRemove[0] = orderToRemove;
		//
		// int mul = 5;
		//
		// Bukkit.broadcastMessage("Start Black hole: " + radius);
		// ID[0] =
		// Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Core.getInstance(),
		// new Runnable() {
		// @Override
		// public void run() {
		// if (stop[0]) {
		// Bukkit.broadcastMessage("BlackHole: stop");
		// Bukkit.getServer().getScheduler().cancelTask(ID[0]);
		// }
		//
		// List<Location> list = (List<Location>) toRemove[0];
		//
		// for (int i = 0; i < blocksPerDelay*mul; i++) {
		// if (list.isEmpty()) {
		// stop[0] = true;
		// } else {
		// ExplosionUtil.destroyBlockWithBIM(list.remove(0));
		// }
		// }
		// }
		// }, 0L, mul*1L);

	}

}
