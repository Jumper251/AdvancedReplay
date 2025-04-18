package me.jumper251.replay.commands.replay;




import me.jumper251.replay.filesystem.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import me.jumper251.replay.replaysystem.replaying.Replayer;
import me.jumper251.replay.utils.MathUtils;

public class ReplayJumpCommand extends SubCommand {

	public ReplayJumpCommand(AbstractCommand parent) {
		super(parent, "jump", "Jump to a specific moment", "jump <Time in Seconds>", true);
	}

	@Override
	public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {		
		if (args.length != 2) return false;
		
		Player p = (Player) cs;

		if (ReplayHelper.replaySessions.containsKey(p.getName())) {
			Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
			int duration = replayer.getReplay().getData().getDuration() / 20;
			
			if (MathUtils.isInt(args[1]) && Integer.valueOf(args[1]) > 0 && Integer.valueOf(args[1]) < duration) {
				int seconds = Integer.valueOf(args[1]);
				
				Messages.REPLAY_JUMP.arg("time", seconds).send(p);
				ReplayAPI.getInstance().jumpToReplayTime(p, seconds);
			} else {
				Messages.REPLAY_JUMP_INVALID.arg("duration", (duration - 1)).send(cs);
			}
			
		} else {
			Messages.REPLAY_JUMP_NOT_IN_REPLAY.send(cs);
		}
		
		return true;
	}

	
}
