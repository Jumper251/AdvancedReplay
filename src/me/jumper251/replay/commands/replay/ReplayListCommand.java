package me.jumper251.replay.commands.replay;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.filesystem.saving.ReplaySaver;

public class ReplayListCommand extends SubCommand {

	public ReplayListCommand(AbstractCommand parent) {
		super(parent, "list", "Lists all replays", "list", false);
	}

	@Override
	public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
		if (ReplaySaver.getReplays().size() > 0) {
			List<String> replayNames = new ArrayList<String>();

			for (String file : ReplaySaver.getReplays()) {
				replayNames.add("¤e" + file + "¤7");
				
			}
			cs.sendMessage(ReplaySystem.PREFIX + "Available replays: ¤8(¤6" + ReplaySaver.getReplays().size() + "¤8)");
			String showList = replayNames.toString().replace("[", "").replace("]", "");
			cs.sendMessage("¤7" + showList);
		} else {
			cs.sendMessage(ReplaySystem.PREFIX + "¤cNo replays found.");
		}
		return true;
	}

	
}
