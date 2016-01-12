package com.jack.bad.ce;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jack.bad.Core;

public class BtooomComands implements CommandExecutor {

	public Core core;
	public static Location start;
	public static Location end;

	public static Location nt_begin;

	public BtooomComands(Core c) {
		this.core = c;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

		return true;
	}

	public boolean findGetLocationFromID(CommandSender sender, Command cmd, String s, String[] args) {
		return true;
	}

	public boolean sendHelpMessage(CommandSender sender, Command cmd, String s, String[] args) {
		return true;
	}

}
