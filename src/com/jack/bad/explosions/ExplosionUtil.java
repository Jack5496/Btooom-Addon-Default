package com.jack.bad.explosions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ExplosionUtil {
	
	public static Collection<Entity> getEntityesAround(Location loc, int radius){
		return loc.getWorld().getNearbyEntities(loc, radius, radius, radius);
	}
	
	public static List<Player> getPlayersAround(Location loc, int radius){
		List<Player> back = new ArrayList<Player>();
		
		for(Entity ent : getEntityesAround(loc, radius)){
			if(ent instanceof Player){
				back.add((Player)ent);
			}
		}
		return back;
	}

	public static List<Location> getTopGroundCircle(Location loc, int radius) {
		List<Location> sphere = getSphereFromCenter(loc, radius);
		List<Location> back = new ArrayList<Location>();

		for (Location l : sphere) {
			Location abouve = l.getBlock().getRelative(BlockFace.UP).getLocation();
			if (l.getBlock().getType() != Material.AIR && abouve.getBlock().getType() == Material.AIR) {
				back.add(l);
			}
		}

		return back;
	}

	public static List<Location> getSphereFromCenter(Location loc, int radius) {
		List<Location> back = new ArrayList<Location>();

		for (Location l : getCubeFromCenter(loc, radius, radius, radius)) {
			if (inCircleRadius(loc, l, radius))
				back.add(l);
		}

		return back;
	}
	
	public static List<Location> getCircleFromCenter(Location loc, int radius) {
		List<Location> back = new ArrayList<Location>();

		for (Location l : getCubeFromCenter(loc, radius, radius, 1)) {
			if (inCircleRadius(loc, l, radius))
				back.add(l);
		}

		return back;
	}

	public static List<Location> get6Neigh(List<Location> locs) {
		List<Location> back = new ArrayList<Location>();
		List<Location> ret = new ArrayList<Location>();

		for (Location loc : locs) {
			back.add(loc);
			back.add(new Location(loc.getWorld(), loc.getBlockX() + 1, loc.getBlockY(), loc.getBlockZ()));
			back.add(new Location(loc.getWorld(), loc.getBlockX() - 1, loc.getBlockY(), loc.getBlockZ()));
			back.add(new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ()));
			back.add(new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ()));
			back.add(new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() + 1));
			back.add(new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() - 1));
		}

		for (Location l : back) {
			if (l.getBlock().getType() == Material.AIR) {
				if (!ret.contains(l)) {
					ret.add(l);
				}
			}
		}

		return ret;
	}

	public static List<Location> getCubeFromCenter(Location loc, int depthX, int depthZ, int height) {
		List<Location> back = new ArrayList<Location>();

		for (int y = height; y > -height; y--) {
			for (int x = -depthX; x < depthX; x++) {
				for (int z = -depthZ; z < depthZ; z++) {
					Location l = new Location(loc.getWorld(), loc.getBlockX() + x, loc.getBlockY() + y,
							loc.getBlockZ() + z);
					back.add(l);
				}
			}
		}

		return back;
	}

	public static boolean inCircleRadius(Location center, Location check, int radius) {
		return center.distance(check) < radius;
	}

}
