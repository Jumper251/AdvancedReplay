package me.jumper251.replay.commands.replay;




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
				
				p.sendMessage(ReplaySystem.PREFIX +"Jumping to §e" + seconds + " §7seconds...");
				ReplayAPI.getInstance().jumpToReplayTime(p, seconds);
			} else {
				p.sendMessage(ReplaySystem.PREFIX + "§cTime has to be between 1 and " + (duration - 1));
			}
			
		} else {
			p.sendMessage(ReplaySystem.PREFIX + "§cYou need to play a Replay first");
		}
		
		return true;
	}

	
}
