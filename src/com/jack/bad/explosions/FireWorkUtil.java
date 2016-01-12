package com.jack.bad.explosions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.jack.bad.Core;

public class FireWorkUtil{
	
	static Random r = new Random(); 
	
	public static void spawnRocket(Location loc){
		spawnRocket(loc, getRandomColor(), getRandomColor(), 2, 4, getRandomEffect());
	}
	
	public static void spawnRocketBigWhiteBall(Location loc){
		spawnRocket(loc, Color.WHITE, Color.WHITE, 2, 4, FireworkEffect.Type.BALL_LARGE);
	}
	
	public static void spawnRocketGreenCrepper(Location loc){
		spawnRocket(loc, Color.LIME, Color.LIME, 2, 4, FireworkEffect.Type.CREEPER);
	}
	
	public static void spawnRocketGoldStar(Location loc){
		spawnRocket(loc, Color.YELLOW, Color.YELLOW, 2, 4, FireworkEffect.Type.STAR);
	}
	
	public static void spawnRocket(Location loc, Color a, Color b, int power, int tickToExplode, FireworkEffect.Type typ){
		Firework firework = loc.getWorld().spawn(loc, Firework.class);
		FireworkMeta meta = firework.getFireworkMeta();
		meta.setPower(power);
		meta.addEffect(FireworkEffect.builder().withFlicker().withTrail().with(typ).withColor(a).withFade(b).build());
		firework.setFireworkMeta(meta);
		Bukkit.getScheduler().runTaskLater(Core.getInstance(), new FireWorkExplode(firework), tickToExplode);
	}
	
	static List<FireworkEffect.Type> effects = new ArrayList<FireworkEffect.Type>();
	
	public static FireworkEffect.Type getRandomEffect(){
		if(effects.size()==0) initEffectList();
		return effects.get(r.nextInt(effects.size()));
	}
	
	private static void initEffectList(){
		effects.add(FireworkEffect.Type.BALL);
		effects.add(FireworkEffect.Type.BALL_LARGE);
		effects.add(FireworkEffect.Type.BURST);
		effects.add(FireworkEffect.Type.CREEPER);
		effects.add(FireworkEffect.Type.STAR);
	}
	
	public static Color getRandomColor(){
		//Get our random colours  
		
		
		
        int r1i = r.nextInt(17) + 1;
        Color c1 = getColor(r1i);
        return c1;
	}
	
	private static Color getColor(int i) {
		Color c = null;
		if(i==1){
		c=Color.AQUA;
		}
		if(i==2){
		c=Color.BLACK;
		}
		if(i==3){
		c=Color.BLUE;
		}
		if(i==4){
		c=Color.FUCHSIA;
		}
		if(i==5){
		c=Color.GRAY;
		}
		if(i==6){
		c=Color.GREEN;
		}
		if(i==7){
		c=Color.LIME;
		}
		if(i==8){
		c=Color.MAROON;
		}
		if(i==9){
		c=Color.NAVY;
		}
		if(i==10){
		c=Color.OLIVE;
		}
		if(i==11){
		c=Color.ORANGE;
		}
		if(i==12){
		c=Color.PURPLE;
		}
		if(i==13){
		c=Color.RED;
		}
		if(i==14){
		c=Color.SILVER;
		}
		if(i==15){
		c=Color.TEAL;
		}
		if(i==16){
		c=Color.WHITE;
		}
		if(i==17){
		c=Color.YELLOW;
		}
		 
		return c;
		}
	
}

