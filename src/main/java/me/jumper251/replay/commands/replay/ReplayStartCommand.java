package me.jumper251.replay.commands.replay;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.utils.ReplayManager;

public class ReplayStartCommand extends SubCommand {

	public ReplayStartCommand(AbstractCommand parent) {
		super(parent, "start", "Records a new replay", "start <Name> [<Players ...>]", false);
	}

	@Override
	public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length < 2) return false;
		
		String name = args[1];
		if (name.length() > 40) {
			cs.sendMessage(ReplaySystem.PREFIX + "§cReplay name is too long.");
			return true;
		}
		
		if (args.length == 2) {
			if (!ReplaySaver.exists(name) && !ReplayManager.activeReplays.containsKey(name)) {
				 ReplayAPI.getInstance().recordReplay(name, cs);
				 
				 cs.sendMessage(ReplaySystem.PREFIX + "§aSuccessfully started recording §e" + name + "§7.\n§7Use §6/Replay stop " + name + "§7 to save it.");
				 cs.sendMessage("§7INFO: You are recording all online players.");
				 
			} else {
				cs.sendMessage(ReplaySystem.PREFIX + "§cReplay already exists.");
			}
			
		} else {
			if (!ReplaySaver.exists(name) && !ReplayManager.activeReplays.containsKey(name)) {
				
				List<Player> playerList = new ArrayList<Player>();
				for (int i = 2; i < args.length; i++) {
					if (Bukkit.getPlayer(args[i]) != null) {
						playerList.add(Bukkit.getPlayer(args[i]));
					}
				}
				
				Player[] players = new Player[playerList.size()];
				players = playerList.toArray(players);
				if (players.length > 0) {
					ReplayAPI.getInstance().recordReplay(name, cs, players);
				 
					cs.sendMessage(ReplaySystem.PREFIX + "§aSuccessfully started recording §e" + name + "§7.\n§7Use §6/Replay stop " + name + "§7 to save it.");
				}
				 
			} else {
				cs.sendMessage(ReplaySystem.PREFIX + "§cReplay already exists.");
			}
		} 
		
		
		return true;
	}

	
}
