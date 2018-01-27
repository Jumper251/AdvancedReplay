package me.jumper251.replay.commands;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.filesystem.saving.DefaultReplaySaver;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import me.jumper251.replay.utils.ReplayManager;


public class ReplayCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(!(cs instanceof Player)){ 
			cs.sendMessage("You must be a player to execute this command."); 
			return true; 
		}
		
		final Player p = (Player)cs;	
		
		String arg = args.length >= 1 ? args[0] : "overview";
		
		if (p.hasPermission("replay.command." + arg)) {
			
			if (args.length == 0) {
				p.sendMessage(ReplaySystem.PREFIX + "ReplaySystem ¤ev" + ReplaySystem.getInstance().getDescription().getVersion());
				p.sendMessage("¤6/Replay start <Name> [<Players ...>] ¤7 - Records a new replay");
				p.sendMessage("¤6/Replay stop <Name> ¤7 - Stops and saves a replay");
				p.sendMessage("¤6/Replay play <Name> ¤7 - Starts a recorded replay");
				p.sendMessage("¤6/Replay delete <Name> ¤7 - Deletes a replay");
				p.sendMessage("¤6/Replay list ¤7 - Lists all replays");
				p.sendMessage("¤6/Replay reload ¤7 - Reloads the config");

			}
			
			if (args.length == 1) {
				if(arg.equalsIgnoreCase("list")) {
					File dir = new File(ReplaySystem.getInstance().getDataFolder() + "/replays/");
					if (dir.exists()) {
						List<String> replayNames = new ArrayList<String>();
						
						File[] files = dir.listFiles();
						for (int i = 0; i < files.length; i++) {
							if (files[i].isFile()) {
								replayNames.add("¤e" + files[i].getName() + "¤7");
							}
						}
						p.sendMessage(ReplaySystem.PREFIX + "Available replays: ¤8(¤6" + files.length + "¤8)");
						String showList = replayNames.toString().replace("[", "").replace("]", "").replaceAll("\\.replay", "");
						p.sendMessage("¤7" + showList);
					} else {
						p.sendMessage(ReplaySystem.PREFIX + "¤cNo replays found.");
					}
					
				}
				
				if (arg.equalsIgnoreCase("reload")) {
					ConfigManager.reloadConfig();
					p.sendMessage(ReplaySystem.PREFIX + "¤aSuccessfully reloaded the configuration.");
				}
				
				if(arg.equalsIgnoreCase("start")){
					p.sendMessage(ReplaySystem.PREFIX + "Usage: ¤6/Replay start <Name> [<Players ...>]");
				}
				if(arg.equalsIgnoreCase("stop")){
					p.sendMessage(ReplaySystem.PREFIX + "Usage: ¤6/Replay stop <Name>");
				}
				if(arg.equalsIgnoreCase("play")){
					p.sendMessage(ReplaySystem.PREFIX + "Usage: ¤6/Replay play <Name>");
				}
				if(arg.equalsIgnoreCase("delete")){
					p.sendMessage(ReplaySystem.PREFIX + "Usage: ¤6/Replay delete <Name>");
				}
				
			}
			
			if(args.length == 2) {
				String name = args[1];
				if(arg.equalsIgnoreCase("start")) {
					if (!ReplaySaver.exists(name) && !ReplayManager.activeReplays.containsKey(name)) {
						 ReplayAPI.getInstance().recordReplay(name);
						 
						 p.sendMessage(ReplaySystem.PREFIX + "¤aSuccessfully started recording ¤e" + name + "¤7.\n¤7Use ¤6/Replay stop " + name + "¤7 to save it.");
						 p.sendMessage("¤7INFO: You are recording all online players.");
						 
					} else {
						p.sendMessage(ReplaySystem.PREFIX + "¤cReplay already exists.");
					}
				}
				
				if(arg.equalsIgnoreCase("stop")) {
					if (ReplayManager.activeReplays.containsKey(name) && ReplayManager.activeReplays.get(name).isRecording()) {
						Replay replay = ReplayManager.activeReplays.get(name);
						p.sendMessage(ReplaySystem.PREFIX + "Saving replay ¤e" + name + "¤7...");
						replay.getRecorder().stop(true);
						
						String path = ReplaySaver.replaySaver instanceof DefaultReplaySaver ? ReplaySystem.getInstance().getDataFolder() + "/replays/" + name + ".replay" : null;
						p.sendMessage(ReplaySystem.PREFIX + "¤7Successfully saved replay" + (path != null ? " to ¤o" + path : ""));
						
					} else {
						p.sendMessage(ReplaySystem.PREFIX + "¤cReplay not found.");
					}

				}
				
				if (arg.equalsIgnoreCase("play")) {
					if (ReplaySaver.exists(name) && !ReplayHelper.replaySessions.containsKey(p.getName())) {
						p.sendMessage(ReplaySystem.PREFIX + "Loading replay ¤e" + name + "¤7...");
						Replay replay = ReplaySaver.load(args[1]);
						p.sendMessage(ReplaySystem.PREFIX + "Replay loaded. Duration ¤e" + (replay.getData().getDuration() / 20) + "¤7 seconds.");
						replay.play(p);
						
					} else {
						p.sendMessage(ReplaySystem.PREFIX + "¤cReplay not found.");
					}
				}
				
				if (arg.equalsIgnoreCase("delete")) {
					if (ReplaySaver.exists(name)) {
						ReplaySaver.delete(name);
						p.sendMessage(ReplaySystem.PREFIX + "¤aSuccessfully deleted replay.");
					} else {
						p.sendMessage(ReplaySystem.PREFIX + "¤cReplay not found.");
					}
				}
			}
			
			if (args.length > 2) {
				String name = args[1];
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
						ReplayAPI.getInstance().recordReplay(name, players);
					 
						p.sendMessage(ReplaySystem.PREFIX + "¤aSuccessfully started recording ¤e" + name + "¤7.\n¤7Use ¤6/Replay stop " + name + "¤7 to save it.");
					}
					 
				} else {
					p.sendMessage(ReplaySystem.PREFIX + "¤cReplay already exists.");
				}
			}
			
			
		} else {
			p.sendMessage(ReplaySystem.PREFIX + "¤cInsufficient permissions");

		}
		return true;
	}

}
