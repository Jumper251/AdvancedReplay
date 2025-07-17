package me.jumper251.replay.commands.replay;

import java.util.ArrayList;
import java.util.List;

import me.jumper251.replay.filesystem.Messages;
import me.jumper251.replay.filesystem.saving.DefaultReplaySaver;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.utils.MathUtils;
import me.jumper251.replay.utils.ReplayManager;
import me.jumper251.replay.utils.StringUtils;

public class ReplayStartCommand extends SubCommand {

	public ReplayStartCommand(AbstractCommand parent) {
		super(parent, "start", "Records a new replay", "start [Name][:Duration] [<Players ...>]", false); 
	}

	@Override
	public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length < 1) return false;
		
		String name = parseName(args);
		int duration = parseDuration(args);
		
		if (name == null || duration < 0) {
			return false;
		}
		if (name.length() > 40) {
			Messages.REPLAY_START_TOO_LONG.send(cs);
			return true;
		}

		if (!DefaultReplaySaver.isValidName(name)) {
			Messages.REPLAY_START_INVALID_NAME.send(cs);
			return true;
		}

		if (ReplayManager.activeReplays.containsKey(name)) {
			Messages.REPLAY_START_EXISTS.send(cs);
			return true;
		}
		
		List<Player> toRecord = new ArrayList<>();

		if (args.length <= 2) {
			toRecord.addAll(Bukkit.getOnlinePlayers());

		} else {
			for (int i = 2; i < args.length; i++) {
				if (Bukkit.getPlayer(args[i]) != null) {
					toRecord.add(Bukkit.getPlayer(args[i]));
				}
			}

		}
		
		ReplayAPI.getInstance().recordReplay(name, cs, toRecord);

		if (duration <= 0) {
			Messages.REPLAY_START.arg("replay", name).send(cs);
		} else {
			Messages.REPLAY_START_TIMED
					.arg("replay", name)
					.arg("duration", duration)
					.send(cs);

			new BukkitRunnable() {
				
				@Override
				public void run() {
					ReplayAPI.getInstance().stopReplay(name, true, true);
				}
			}.runTaskLater(ReplaySystem.getInstance(), duration * 20);
		}
		
		if (args.length <= 2) {
			Messages.REPLAY_START_INFO.send(cs);
		}
		
		return true;
	}
	
	
	private String parseName(String[] args) {
		if (args.length >= 2) {			
			String[] split = args[1].split(":");	
			
			if (args[1].contains(":")) {
				if (split.length == 2 && split[0].length() > 0) return split[0];
			} else {
				return args[1];
			}
		}

		
		return StringUtils.getRandomString(6);
	}
	
	private int parseDuration(String[] args) {
		if (args.length < 2 || !args[1].contains(":")) return 0;
		String[] split = args[1].split(":");

		if (split.length == 2 && MathUtils.isInt(split[1])) {
			return Integer.parseInt(split[1]);
		}
		
		if (split.length == 1) {
			if (!split[0].startsWith(":") || !MathUtils.isInt(split[0])) return -1;
			
			return Integer.parseInt(split[0]);
		}
		
		return 0;
	}

	
}
