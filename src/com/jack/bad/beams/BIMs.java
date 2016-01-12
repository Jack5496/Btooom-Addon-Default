package com.jack.bad.beams;

import java.util.ArrayList;
import java.util.List;

import com.jack.bad.Core;
import com.jack.btooom.beams.BIM;

public class BIMs {

	static List<Class<? extends BIM>> classes = new ArrayList<Class<? extends BIM>>();

	static {
		classes.add(Cracker.class);
		classes.add(Implosion.class);
		classes.add(RemoteControl.class);
		classes.add(Timer.class);
		classes.add(Barrier.class);
		classes.add(BlazingGas.class);
		classes.add(Flame.class);
		classes.add(Homing.class);
		classes.add(Ice.class);
		classes.add(Lightning.class);
		classes.add(Party.class);
		classes.add(Lava.class);
		classes.add(EarthQuake.class);
	}

	public static List<Class<? extends BIM>> getAllBIMs() {
		return classes;
	}
	
	

}
