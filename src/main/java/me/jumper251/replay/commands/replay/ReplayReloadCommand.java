package me.jumper251.replay.commands.replay;



import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.filesystem.ConfigManager;

public class ReplayReloadCommand extends SubCommand {

	public ReplayReloadCommand(AbstractCommand parent) {
		super(parent, "reload", "Reloads the config", "reload", false);
	}

	@Override
	public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
		ConfigManager.reloadConfig();
		cs.sendMessage(ReplaySystem.PREFIX + "§aSuccessfully reloaded the configuration.");
		return true;
	}

	
}
