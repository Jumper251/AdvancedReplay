package me.jumper251.replay.commands.replay;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.util.stream.Collectors;

import me.jumper251.replay.filesystem.Messages;
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
		super(parent, "stop", "Stops and saves a replay", "stop <Name> [Options]", false);
	}

	@Override
	public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length > 3 || args.length < 2) return false;
		
		String name = args[1];
		boolean isForce = args.length == 3 && args[2].equals("-force");
		boolean isNoSave = args.length == 3 && args[2].equals("-nosave");

		if (ReplayManager.activeReplays.containsKey(name) && ReplayManager.activeReplays.get(name).isRecording()) {
			Replay replay = ReplayManager.activeReplays.get(name);

			if (isNoSave || replay.getRecorder().getData().getActions().size() == 0) {
				replay.getRecorder().stop(false);

				Messages.REPLAY_STOP_NO_SAVE.arg("replay", name).send(cs);
			} else {
				if (ReplaySaver.exists(name) && !isForce) {
					Messages.REPLAY_STOP_EXISTS.arg("replay", name).send(cs);
					return true;
				}
				
				Messages.REPLAY_STOP_SAVING.arg("replay", name).send(cs);
				replay.getRecorder().stop(true);
			
				String path = ReplaySaver.replaySaver instanceof DefaultReplaySaver ? ReplaySystem.getInstance().getDataFolder() + "/replays/" + name + ".replay" : null;

				if (path == null) {
					Messages.REPLAY_STOP_SAVED.arg("replay", name).send(cs);
				} else {
					Messages.REPLAY_STOP_SAVED_TO
							.arg("replay", name)
							.arg("path", path)
							.send(cs);
				}
			}
			
		} else {
			Messages.REPLAY_NOT_FOUND.send(cs);
		}
		
		return true;
	}
	
	@Override
	public List<String> onTab(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length == 3) return Arrays.asList("-nosave", "-force");
		
		return ReplayManager.activeReplays.keySet().stream()
				.filter(name -> StringUtil.startsWithIgnoreCase(name, args.length > 1 ? args[1] : null))
				.collect(Collectors.toList());
	}

	
}
