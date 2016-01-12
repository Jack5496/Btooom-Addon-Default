package com.jack.bad;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.jack.bad.beams.Barrier;
import com.jack.btooom.API.AddonAPI;
import com.jack.btooom.API.BIMStoreAPI;
import com.jack.btooom.API.Btooom;
import com.jack.btooom.beams.BIM;

import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;

import com.jack.bad.beams.BIMs;

public class Core extends JavaPlugin implements AddonAPI {

	public static Btooom btooom;

	public static Core instance;

	// Imported EffectLibs
	// http://dev.bukkit.org/bukkit-plugins/effectlib/
	// ArcEffect - Create architectual correct arc of particles
	// AtomEffect - Create the orbital-model of the atom
	// BigBangEffect - Create a large cluster of fireworks.
	// BleedEffect - Let the target entity bleed.
	// ConeEffect - Cast a cone in all possible directions
	// EarthEffect - Create a model of the Earth
	// DnaEffect - Create DNA molecule
	// ExplodeEffect - Create a explosion at location.
	// FlameEffect - Let the target entity burn.
	// FountainEffect - Create a foundtain for you well
	// GridEffect - Customizable grid for you signwall
	// HelixEffect - Create a customizable static helix.
	// JumpEffect - Forces an entity to jump naturally.
	// LineEffect - Draw line from A to B
	// LoveEffect - The target entity is in love.
	// MusicEffect - Circle of notes appeares above the entity.
	// MusicEffect - Circle of notes appeares at a location.
	// ShieldEffect - Spherical Shield around an entity.
	// SkyRocketEffect - Foces an entity to fly into the sky.
	// SmokeEffect - Let the target entity smoke.
	// StarEffect - Create fully customizable 3D star
	// TextEffect - Create particle-text with custom font, size and text
	// TraceEffect - Create a trace along an entitys path.
	// TurnPlayerEffect - Forces the player to turn in a circle.
	// VortexEffect - Create a vortex of particles at location
	// WarpEffect - Create a warp-effect around an entity
	// WaveEffect - Create waves with surf.
	public static EffectManager em;

	public final Logger logger = Logger.getLogger("Minecraft");
	

	public void onEnable() {
		if (loadAddon()) {
			EffectLib lib = EffectLib.instance();
			em = new EffectManager(lib);
			
			logger.info("[" + getName() + "] enabled");
		}
	}

	public boolean loadAddon() {
		instance = this;
		btooom = (Btooom) this.getServer().getPluginManager().getPlugin("Btooom");
		return btooom.enableNewAddon(instance);
	}
	
	@Override
	public List<Class<? extends BIM>> getAllBIMs() {
		return BIMs.getAllBIMs();
	}

	@Override
	public void onDisable() {
		saveInformationsForDisable();
		if (em != null) {
			em.dispose();
			em.disposeOnTermination();
		}
		logger.info("[" + getName() + "] disabled!");
	}

	public static Core getInstance() {
		return instance;
	}

	public static BIMStoreAPI getBIMStore() {
		return btooom.getBIMStore();
	}

	@Override
	public void saveInformationsForDisable() {

	}

	@Override
	public File getFileDest() {
		return this.getFile();
	}



}