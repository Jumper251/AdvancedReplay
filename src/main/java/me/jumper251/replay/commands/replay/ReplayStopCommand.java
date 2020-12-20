package me.jumper251.replay.commands.replay;


import java.util.List;

import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.filesystem.saving.DefaultReplaySaver;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.utils.ReplayManager;

public class ReplayStopCommand extends SubCommand {

	public ReplayStopCommand(AbstractCommand parent) {
		super(parent, "stop", "Stops and saves a replay", "stop <Name>", false);
	}

	@Override
	public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length != 2) return false;
		
		String name = args[1];
		
		if (ReplayManager.activeReplays.containsKey(name) && ReplayManager.activeReplays.get(name).isRecording()) {
			Replay replay = ReplayManager.activeReplays.get(name);
			cs.sendMessage(ReplaySystem.PREFIX + "Saving replay §e" + name + "§7...");
			replay.getRecorder().stop(true);
			
			String path = ReplaySaver.replaySaver instanceof DefaultReplaySaver ? ReplaySystem.getInstance().getDataFolder() + "/replays/" + name + ".replay" : null;
			cs.sendMessage(ReplaySystem.PREFIX + "§7Successfully saved replay" + (path != null ? " to §o" + path : ""));
			
		} else {
			cs.sendMessage(ReplaySystem.PREFIX + "§cReplay not found.");
		}
		
		return true;
	}
	
	@Override
	public List<String> onTab(CommandSender cs, Command cmd, String label, String[] args) {
		return ReplayManager.activeReplays.keySet().stream()
				.filter(name -> StringUtil.startsWithIgnoreCase(name, args.length > 1 ? args[1] : null))
				.collect(Collectors.toList());
	}

	
}
