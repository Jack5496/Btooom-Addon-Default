package com.jack.bad.explosions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Firework;
import org.bukkit.scheduler.BukkitRunnable;

public class FireWorkExplode extends BukkitRunnable {

	private final Firework firework;

	public FireWorkExplode(Firework fire) {
		super();
		firework = fire;
	}

	@Override
	public void run() {
		Bukkit.broadcastMessage("Detonating firework now");
		firework.detonate();
		Bukkit.broadcastMessage("Detonated");
	}
}
