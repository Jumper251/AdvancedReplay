package me.jumper251.replay.commands.replay;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.CommandPagination;
import me.jumper251.replay.commands.IPaginationExecutor;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.filesystem.saving.DefaultReplaySaver;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.utils.MathUtils;

public class ReplayListCommand extends SubCommand {

	public ReplayListCommand(AbstractCommand parent) {
		super(parent, "list", "Lists all replays", "list [Page]", false);
	}

	@Override
	public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length > 2) return false;

		
		if (ReplaySaver.getReplays().size() > 0) {
			int page = 1;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			if (args.length == 2 && MathUtils.isInt(args[1])) page = Integer.valueOf(args[1]);
			

			CommandPagination<String> pagination = new CommandPagination<>(ReplaySaver.getReplays(), 9);
			cs.sendMessage(ReplaySystem.PREFIX + "Available replays: §8(§6" + ReplaySaver.getReplays().size() + "§8) §7| Page: §e" + page + "§7/§e" + pagination.getPages());

			pagination.printPage(page, new IPaginationExecutor<String>() {

				@Override
				public void print(String element) {
					cs.sendMessage("§e " + element + (getCreationDate(element) != null ? "§7 - Date: §6" + format.format(getCreationDate(element)) : null));
					
				}
			});
						
			
		} else {
			cs.sendMessage(ReplaySystem.PREFIX + "§cNo replays found.");
		}
		return true;
	}
	
	private Date getCreationDate(String replay) {
		if (ReplaySaver.replaySaver instanceof DefaultReplaySaver) {
			return new Date(new File(DefaultReplaySaver.DIR, replay + ".replay").lastModified());
		}
		
		return null;
	}
	
	@Override
	public List<String> onTab(CommandSender cs, Command cmd, String label, String[] args) {
		return IntStream.range(1, new CommandPagination<>(ReplaySaver.getReplays(), 9).getPages() + 1)
				.boxed()
				.map(String::valueOf)
				.collect(Collectors.toList());
	}

	
}
